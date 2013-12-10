package ru.aplix.converters.fr2afop.reader;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.attribute.standard.PrinterResolution;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.database.Functions;
import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Page;
import ru.aplix.converters.fr2afop.fr.Report;
import ru.aplix.converters.fr2afop.fr.Variable;
import ru.aplix.converters.fr2afop.fr.cnst.ViewFlag;
import ru.aplix.converters.fr2afop.fr.dataset.Column;
import ru.aplix.converters.fr2afop.fr.dataset.Dataset;
import ru.aplix.converters.fr2afop.fr.dataset.Row;
import ru.aplix.converters.fr2afop.fr.image.AbstractImage;
import ru.aplix.converters.fr2afop.fr.image.BMPImage;
import ru.aplix.converters.fr2afop.fr.image.JPEGImage;
import ru.aplix.converters.fr2afop.fr.type.BandType;
import ru.aplix.converters.fr2afop.fr.type.BarCodeType;
import ru.aplix.converters.fr2afop.fr.type.FontStyle;
import ru.aplix.converters.fr2afop.fr.type.FontWeight;
import ru.aplix.converters.fr2afop.fr.type.FrameStyle;
import ru.aplix.converters.fr2afop.fr.type.FrameTypeEnum;
import ru.aplix.converters.fr2afop.fr.type.FunctionType;
import ru.aplix.converters.fr2afop.fr.type.PageOrientation;
import ru.aplix.converters.fr2afop.fr.type.TextAlign;
import ru.aplix.converters.fr2afop.fr.type.TextDecoration;
import ru.aplix.converters.fr2afop.fr.type.VariableType;
import ru.aplix.converters.fr2afop.fr.type.VerticalAlign;
import ru.aplix.converters.fr2afop.fr.view.BandView;
import ru.aplix.converters.fr2afop.fr.view.BarCodeView;
import ru.aplix.converters.fr2afop.fr.view.LineView;
import ru.aplix.converters.fr2afop.fr.view.MemoView;
import ru.aplix.converters.fr2afop.fr.view.PictureView;
import ru.aplix.converters.fr2afop.fr.view.SubReportView;
import ru.aplix.converters.fr2afop.fr.view.View;
import ru.aplix.converters.fr2afop.utils.Rectangle;
import ru.aplix.converters.fr2afop.utils.ReverseDataInputStream;

public class FreeReportReader implements ReportReader {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	protected static final int VERSION_CURRENT = 23;
	protected static final int VERSION_PREVIOUS = 22;
	protected static final int VERSION_VERY_OLD = 21;

	private Map<Class<?>, Integer> viewIDs;
	protected Set<String> viewNames;
	protected int version;
	protected boolean trim = true;

	protected InputStreamOpener so;
	protected ReverseDataInputStream dis;

	protected PrinterResolution printerResolution;
	protected float mmInUnitsX;
	protected float mmInUnitsY;

	// General pattern to parse expressions in brackets
	protected static final Pattern pBrackets = Pattern.compile("\\[([^\\[\\]]+)\\]");
	protected static final Pattern pTable = Pattern.compile("([\\w\\.]+)\\.\"([\\w]+)\"");
	protected static final Pattern pTableInExp = Pattern.compile("[,\\(]{1}[\\s]*([\\w\\.]+\\.\"[\\w]+\")[\\s]*[,\\)]{1}");

	// Map to function types
	protected static final Map<String, FunctionType> specialFuncMap = new HashMap<String, FunctionType>();
	{
		specialFuncMap.put("PAGE#", FunctionType.PAGE_NUM);
		specialFuncMap.put("DATE", FunctionType.DATE);
		specialFuncMap.put("TIME", FunctionType.TIME);
		specialFuncMap.put("LINE#", FunctionType.LINE_NUM);
		specialFuncMap.put("LINETHROUGH#", FunctionType.LINE_THROUGH_NUM);
		specialFuncMap.put("COLUMN#", FunctionType.COLUMN_NUM);
		specialFuncMap.put("CURRENT#", FunctionType.CURRENT_NUM);
		specialFuncMap.put("TOTALPAGES", FunctionType.TOTAL_PAGES);
	}

	/**
	 * Class constructor.
	 */
	public FreeReportReader() {
		viewIDs = new HashMap<Class<?>, Integer>();
		viewNames = new HashSet<String>();
	}

	@Override
	public Report readFromStream(InputStreamOpener so, ReportModifier modifier, Configuration configuration) throws Exception {
		try {
			this.so = so;

			viewIDs.clear();
			viewNames.clear();

			Report report = new Report();
			openStream();
			readFileVersion();
			readReport(report);

			if (modifier != null) {
				report = modifier.modify(report);
			}

			substituteVariables(report);
			buildVariableRelationship(report);
			tossViews(report);
			formDatasets(report, configuration);

			return report;
		} finally {
			if (dis != null) {
				dis.close();
			}
		}
	}

	protected void openStream() throws IOException {
		if (dis != null) {
			dis.close();
		}

		InputStream is = so.openStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		dis = new ReverseDataInputStream(bis);
	}

	protected void readFileVersion() throws IOException {
		dis.mark(1);
		version = dis.readByte();

		if (version < VERSION_VERY_OLD) {
			version = VERSION_VERY_OLD;
			dis.reset();
		}

		if (version > VERSION_CURRENT) {
			throw new IOException("Unsupported FRF format");
		}
	}

	protected void readReport(Report report) throws Exception {
		// Read general parameters of the report
		report.setFileVersion(version);
		report.setPrintToDefault(readWordBool());
		report.setDoublePass(readWordBool());
		report.setPrinterName(readString());

		determinePrinterResolution(report);

		// Read report content until end of stream will be reached
		try {
			while (true) {
				int b = dis.readUnsignedByte();
				switch (b) {
				case 0x0:
					MemoView mv = addView(MemoView.class, report);
					readView(mv);
					break;
				case 0x1:
					PictureView pv = addView(PictureView.class, report);
					readView(pv);
					break;
				case 0x2:
					BandView bv = addView(BandView.class, report);
					readView(bv);
					break;
				case 0x3:
					SubReportView srv = addView(SubReportView.class, report);
					readView(srv);
					break;
				case 0x4:
					LineView lv = addView(LineView.class, report);
					readView(lv);
					break;
				case 0xA:
					String viewClasName = readString();
					if ("TFRFRAMEDMEMOVIEW".equalsIgnoreCase(viewClasName)) {
						MemoView mv2 = addView(MemoView.class, report);
						readView(mv2);
						dis.skip(8);
					} else if ("TfrBarCodeView".equalsIgnoreCase(viewClasName)) {
						BarCodeView bc = addView(BarCodeView.class, report);
						readView(bc);
					} else {
						throw new Exception(String.format("Unknown object type: %d", b));
					}
					break;
				case 0xFF: // page info
					readPage(report);
					break;
				case 0xFE: // values and variables
					readVariables(report);
					readVariableCategories(report);
					break;
				case 0xFD: // dataset
					log.warn("Custom dataset found, skipping...");
					return;
				default: // views
					throw new Exception(String.format("Unknown object type: %d", b));
				}
			}
		} catch (EOFException e) {
			// End of stream reached, OKO
		}
	}

	protected void determinePrinterResolution(Report report) throws PrinterException {
		PrintService printService = null;

		// Find printer by name
		PrintService[] services = PrinterJob.lookupPrintServices();
		int i = 0;
		while (printService == null && i < services.length) {
			PrintService service = services[i];
			if (service.getName().equals(report.getPrinterName())) {
				printService = service;
			}
			i++;
		}

		// Log printer name
		if (printService != null) {
			log.debug(String.format("Selecting \"%s\" printer", printService.getName()));
		} else {
			log.debug(String.format("Selecting default printer"));
		}

		// Get printer resolution
		if (printService != null) {
			printerResolution = (PrinterResolution) printService.getDefaultAttributeValue(PrinterResolution.class);
		}

		if (printService != null || printerResolution == null) {
			printerResolution = new PrinterResolution(600, 600, PrinterResolution.DPI);
		}
	}

	protected float[] getDefaultCoeff() {
		return new float[] { 93f / 1.022f, 93f / 1.015f };
	}

	protected void readPage(Report report) throws IOException, PrinterException {
		Page page = new Page();

		page.setSize(dis.readInt());
		page.setWidth(dis.readInt() / 10f); // page width in mm
		page.setHeight(dis.readInt() / 10f); // page height in mm

		int[] pr = printerResolution.getResolution(PrinterResolution.DPI);
		float pageWidthInPixels = page.getWidth() / 25.4f * (float) pr[0];
		float pageHeightInPixels = page.getHeight() / 25.4f * (float) pr[1];

		float[] defK = getDefaultCoeff();
		float pageWidthInUnits = pageWidthInPixels * defK[0] / (float) pr[0];
		float pageHeightInUnits = pageHeightInPixels * defK[1] / (float) pr[1];

		mmInUnitsX = page.getWidth() / pageWidthInUnits;
		mmInUnitsY = page.getHeight() / pageHeightInUnits;

		// Set margins in mm
		page.setMarginLeft(dis.readInt() * mmInUnitsX);
		page.setMarginTop(dis.readInt() * mmInUnitsY);
		page.setMarginRight(dis.readInt() * mmInUnitsX);
		page.setMarginBottom(dis.readInt() * mmInUnitsY);
		page.setOrientation(dis.readBoolean() ? PageOrientation.LANDSCAPE : PageOrientation.PORTRAIT);

		if (version < VERSION_CURRENT) {
			dis.skip(6);
		}

		page.setPrintToPrevPage(readWordBool());
		page.setUseMargins(readWordBool());
		page.setColumns(dis.readInt());
		page.setColumnGap(dis.readInt());

		report.getPages().add(page);
	}

	protected void readVariables(Report report) throws IOException {
		List<Variable> variables = report.getVariables();
		variables.clear();

		// Read variable from stream
		int numValues = dis.readInt();
		for (int i = 0; i < numValues; i++) {
			Variable variable = new Variable();

			int type = dis.readUnsignedByte();
			variable.setType(type == 0 ? VariableType.NOT_ASSIGNED : type == 1 ? VariableType.DB_FIELD : type == 2 ? VariableType.FUNCTION
					: VariableType.EXPRESSION);

			int sf = dis.readInt();
			if (sf == 1) {
				variable.setType(VariableType.EXPRESSION);
			}

			if (VariableType.FUNCTION.equals(variable.getType())) {
				variable.setFunction(sf == 0 ? FunctionType.PAGE_NUM : sf == 2 ? FunctionType.DATE : sf == 3 ? FunctionType.TIME
						: sf == 4 ? FunctionType.LINE_NUM : sf == 5 ? FunctionType.LINE_THROUGH_NUM : sf == 6 ? FunctionType.COLUMN_NUM
								: sf == 7 ? FunctionType.CURRENT_NUM : FunctionType.TOTAL_PAGES);
			}

			variable.setDataSet(readShortString());
			variable.setContent(readShortString());
			variable.setName(readShortString());

			variables.add(variable);
		}
	}

	protected void substituteVariables(Report report) {
		// Get list of report variables
		List<Variable> variables = report.getVariables();

		// Do the same things with previously created variables
		int count = variables.size();
		while (--count >= 0) {
			Variable variable = variables.get(count);
			if (VariableType.EXPRESSION.equals(variable.getType())) {
				variable.setContent(substituteVariables(variable.getContent(), variables));
			}
		}

		// Enumerate all pages and all views
		for (Page page : report.getPages()) {
			for (View view : page.getViews()) {
				// If memo contains expressions,
				// then extract them as new variables
				String memo = view.getMemo();
				if (memo == null) {
					continue;
				}

				memo = substituteVariables(memo, variables);
				view.setMemo(memo);
			}
		}

		// Replace function language
		for (Variable variable : variables) {
			if (VariableType.EXPRESSION.equals(variable.getType())) {
				String newContent = variable.getContent();
				for (String[] pair : Functions.FUNC_REPLACEMENT) {
					newContent = newContent.replaceAll(pair[0], pair[1]);
				}
				variable.setContent(newContent);
			}
		}
	}

	protected String substituteVariables(String text, List<Variable> variables) {
		// Find the next subsequence of the input sequence
		// that matches the pattern
		Matcher mTableInExp = pTableInExp.matcher(text);
		Matcher mBrackets = pBrackets.matcher(text);
		while (true) {
			Variable existingVariable = null;

			boolean replaceBrackets = false;
			final String expression;
			if (mTableInExp.find() && mTableInExp.groupCount() == 1) {
				expression = mTableInExp.group(1);
			} else if (mBrackets.find() && mBrackets.groupCount() == 1) {
				expression = mBrackets.group(1);
				replaceBrackets = true;

				// If expression equals to variable name
				// Check whether variable already exists
				existingVariable = (Variable) CollectionUtils.find(variables, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						Variable o = ((Variable) object);
						return o.getName().equalsIgnoreCase(expression);
					}
				});
			} else {
				break;
			}

			// Do substitution if expression is not a variable name
			// or such variable does not exist yet
			if (existingVariable == null) {
				final Matcher mTable = pTable.matcher(expression);
				final boolean dbFieldVar = (mTable.matches() && mTable.groupCount() == 2);
				final FunctionType ft = dbFieldVar ? null : specialFuncMap.get(expression.toUpperCase());

				// The expression can be equal to existing variable
				// value. Try to find such variable
				Variable variable = (Variable) CollectionUtils.find(variables, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						Variable o = ((Variable) object);
						if (dbFieldVar) {
							return o.getDataSet() != null && o.getContent() != null && o.getDataSet().equalsIgnoreCase(mTable.group(1))
									&& o.getContent().equalsIgnoreCase(mTable.group(2));
						} else if (ft != null) {
							return ft.equals(o.getFunction());
						} else {
							return o.getContent() != null && o.getContent().equalsIgnoreCase(expression);
						}
					}
				});

				// Create a new variable if not found by name or
				// value. Restoring brackets.
				if (variable == null) {
					variable = new Variable();

					if (dbFieldVar) {
						variable.setType(VariableType.DB_FIELD);
						variable.setDataSet(mTable.group(1));
						variable.setContent(mTable.group(2));
					} else if (ft != null) {
						variable.setType(VariableType.FUNCTION);
						variable.setFunction(ft);
					} else {
						variable.setType(VariableType.EXPRESSION);
						variable.setContent(StringUtils.replaceEach(expression, new String[] { "{", "}" }, new String[] { "[", "]" }));
					}

					variable.setName(createUniqueName(variable.getClass()));
					variables.add(variable);
				}

				existingVariable = variable;
			}

			// Change expression to a new (or found by value)
			// variable name.
			// Temporary replace brackets in order to not parse
			// them again.
			if (replaceBrackets) {
				text = StringUtils.replace(text, "[" + expression + "]", "{" + existingVariable.getName() + "}", 1);
			} else {
				text = StringUtils.replace(text, expression, "{" + existingVariable.getName() + "}", 1);
			}

			mTableInExp = pTableInExp.matcher(text);
			mBrackets = pBrackets.matcher(text);
		}

		// Change view memo, restoring brackets
		text = StringUtils.replaceEach(text, new String[] { "{", "}" }, new String[] { "[", "]" });
		return text;
	}

	protected void buildVariableRelationship(Report report) {
		// Get list of report variables
		List<Variable> variableList = report.getVariables();

		// Build variable map
		Map<String, Variable> variableMap = new HashMap<String, Variable>();
		variableMap.clear();
		for (Variable variable : variableList) {
			if (VariableType.NOT_ASSIGNED.equals(variable.getType())) {
				continue;
			}

			variableMap.put(variable.getName(), variable);
		}

		// Determine variable dependencies
		for (Variable variable : variableList) {
			if (VariableType.EXPRESSION.equals(variable.getType())) {
				// Try to find dependence variable in brackets
				Matcher mBrackets = pBrackets.matcher(variable.getContent());
				while (mBrackets.find() && mBrackets.groupCount() == 1) {
					// If found then establish relationship
					String name = mBrackets.group(1);
					Variable depVar = variableMap.get(name);
					if (depVar != null) {
						depVar.getPrevious().add(variable);
						variable.getNext().add(depVar);
					}
				}
			}
		}
	}

	protected void tossViews(Report report) {
		for (Page page : report.getPages()) {
			// Prepare map of band views.
			// Calculate band view rectangle.
			Map<BandView, Rectangle> bandRects = new HashMap<BandView, Rectangle>();
			for (View view : page.getViews()) {
				if (view instanceof BandView) {
					BandView bandView = (BandView) view;
					Rectangle rectangle = new Rectangle(bandView.getLeft(), bandView.getTop(), bandView.getWidth(), bandView.getHeight());
					bandRects.put(bandView, rectangle);
				}
			}

			// Enumerate all views again.
			// Move views to corresponding band views.
			List<View> views = page.getViews();

			int i = 0;
			while (i < views.size()) {
				View view = views.get(i);

				if (!(view instanceof BandView)) {
					// Calculate view rectangle
					final Rectangle rectangle = new Rectangle(view.getLeft(), view.getTop(), view.getWidth(), view.getHeight());

					// Try to find corresponding band view
					@SuppressWarnings("unchecked")
					Map.Entry<BandView, Rectangle> entry = (Map.Entry<BandView, Rectangle>) CollectionUtils.find(bandRects.entrySet(), new Predicate() {
						@Override
						public boolean evaluate(Object object) {
							Map.Entry<BandView, Rectangle> o = (Map.Entry<BandView, Rectangle>) object;
							return o.getValue().contains(rectangle.getX(), rectangle.getY())
									|| o.getValue().contains(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());
						}
					});

					// If band view has found,
					// attach view to the band and remove view from main list.
					if (entry != null) {
						BandView bandView = entry.getKey();
						bandView.getChildViews().add(view);
						views.remove(i);

						// Recalculate view coordinates
						// in relation to band view
						view.setTop((view.getTop() - bandView.getTop()) / bandView.getHeight() * 100f);
						view.setHeight(view.getHeight() / bandView.getHeight() * 100f);

						continue;
					}
				}

				i++;
			}
		}
	}

	protected void formDatasets(Report report, Configuration configuration) {
		List<Dataset> datasets = report.getDatasets();
		datasets.clear();

		// Build configuration data-sets hash-map
		Map<String, Dataset> configDatasetMap = new HashMap<String, Dataset>();
		for (Dataset dataset : configuration.getDatasets()) {
			configDatasetMap.put(dataset.getName(), dataset);
			for (String synonym : dataset.getSynonyms()) {
				configDatasetMap.put(synonym, dataset);
			}
		}

		// Build report data-sets hash-map
		Map<String, Dataset> reportDatasetMap = new HashMap<String, Dataset>();

		// Enumerate all db-field variables
		for (final Variable variable : report.getVariables()) {
			String datasetName = hasDBDependence(variable);
			if (datasetName != null) {
				Dataset dataset = reportDatasetMap.get(datasetName);
				if (dataset == null) {
					dataset = new Dataset();
					dataset.setName(datasetName);
					datasets.add(dataset);
					reportDatasetMap.put(dataset.getName(), dataset);

					Dataset configDataset = configDatasetMap.get(dataset.getName());
					if (configDataset != null) {
						dataset.setName(configDataset.getName());
						for (String synonym : configDataset.getSynonyms()) {
							dataset.getSynonyms().add(synonym);
							reportDatasetMap.put(synonym, dataset);
						}
					}
				}

				Row row;
				if (dataset.getRows().size() > 0) {
					row = dataset.getRows().get(0);
				} else {
					row = new Row();
					dataset.getRows().add(row);
				}

				Column column = (Column) CollectionUtils.find(row.getColumns(), new Predicate() {
					@Override
					public boolean evaluate(Object arg0) {
						Column item = (Column) arg0;
						return variable.getName().equalsIgnoreCase(item.getName());
					}
				});

				if (column == null) {
					column = new Column();
					column.setName(variable.getName());
					column.setValue("");
					row.getColumns().add(column);
				}
			}
		}
	}

	protected String hasDBDependence(Variable variable) {
		if (VariableType.DB_FIELD.equals(variable.getType())) {
			return variable.getDataSet();
		}

		if (variable.hasNext()) {
			for (Variable next : variable.getNext()) {
				String dataset = hasDBDependence(next);
				if (dataset != null) {
					return dataset;
				}
			}
		}
		return null;
	}

	protected void readVariableCategories(Report report) throws IOException {
		String wholeStr = readMemo();
		if (wholeStr != null && wholeStr.length() > 0) {
			// Parse variable categories
			// String[] items = wholeStr.split("\n");
			// for (String s : items) {
			// String name = normalizeString(s);
			// if (s.startsWith(" ")) {
			// } else {
			// }
			// }
		}
	}

	protected <T extends View> T addView(Class<T> viewClass, Report report) throws Exception {
		int pageNum = dis.readUnsignedByte();
		if (pageNum < 0 || pageNum >= report.getPages().size()) {
			throw new Exception(String.format("Page #%d does not exist", pageNum));
		}

		T view = viewClass.newInstance();

		Page page = report.getPages().get(pageNum);
		page.getViews().add(view);

		readView(page, view);

		return view;
	}

	protected void readView(Page page, View view) throws IOException {
		String name;
		if (version >= VERSION_CURRENT) {
			name = readString();
		} else {
			name = createUniqueName(view.getClass());
		}

		view.setName(name);
		viewNames.add(name);

		float left = dis.readInt();
		float top = dis.readInt();
		float width = dis.readInt();
		float height = dis.readInt();
		float boundLeft = left;
		float boundTop = top;
		float boundWidth = width;
		float boundHeight = height;

		int flags = dis.readUnsignedShort();
		view.setFlags(flags);
		view.setStretched((flags & ViewFlag.Stretched) == ViewFlag.Stretched);

		int frameType = dis.readUnsignedShort();
		if (view instanceof BandView) {
			((BandView) view).setType(convertToBandType(frameType));
		} else {
			view.getFrameType().setNone();
			if ((frameType & 0x01) == 0x01) {
				view.getFrameType().setEdge(FrameTypeEnum.RIGHT);
			}
			if ((frameType & 0x02) == 0x02) {
				view.getFrameType().setEdge(FrameTypeEnum.BOTTOM);
			}
			if ((frameType & 0x04) == 0x04) {
				view.getFrameType().setEdge(FrameTypeEnum.LEFT);
			}
			if ((frameType & 0x08) == 0x08) {
				view.getFrameType().setEdge(FrameTypeEnum.TOP);
			}
		}

		view.setFrameWidth(dis.readFloat());
		view.setFrameColor(readColor());

		int frameStyle = dis.readUnsignedShort();
		view.setFrameStyle(convertFrameStyle(frameStyle));

		view.setFillColor(readColor());

		view.setFormat(dis.readInt());
		if (view instanceof BandView) {
			((BandView) view).setDataset(readString());
		} else {
			view.setFormatString(readString());
		}
		view.setMemo(readMemo());
		if (version >= VERSION_CURRENT) {
			view.setScript(readMemo());
			view.setVisible(readWordBool());
		}

		int w = Float.floatToIntBits(view.getFrameWidth());
		if (w <= 10) {
			w *= 1000;
		}
		view.setFrameWidth(w / 1000);

		if (!(view instanceof BandView)) {
			left += view.getFrameWidth() / 2f;
			top += view.getFrameWidth() / 2f;
			width -= view.getFrameWidth();
			height -= view.getFrameWidth();
		}

		left = left * mmInUnitsX;
		top = top * mmInUnitsY;
		width = width * mmInUnitsX;
		height = height * mmInUnitsY;
		boundLeft = boundLeft * mmInUnitsX;
		boundTop = boundTop * mmInUnitsY;
		boundWidth = boundWidth * mmInUnitsX;
		boundHeight = boundHeight * mmInUnitsY;

		float areaLeft, areaTop, areaWidth, areaHeight;
		if (page.isUseMargins()) {
			areaLeft = page.getMarginLeft();
			areaTop = page.getMarginTop();
			areaWidth = page.getWidth() - page.getMarginLeft() - page.getMarginRight();
			areaHeight = page.getHeight() - page.getMarginTop() - page.getMarginBottom();
		} else {
			areaLeft = 0;
			areaTop = 0;
			areaWidth = page.getWidth();
			areaHeight = page.getHeight();
		}

		view.setLeft((left - areaLeft) / areaWidth * 100f);
		view.setTop((top - areaTop) / areaHeight * 100f);
		view.setWidth(width / areaWidth * 100f);
		view.setHeight(height / areaHeight * 100f);
		view.setBoundLeft((boundLeft - areaLeft) / areaWidth * 100f);
		view.setBoundTop((boundTop - areaTop) / areaHeight * 100f);
		view.setBoundWidth(boundWidth / areaWidth * 100f);
		view.setBoundHeight(boundHeight / areaHeight * 100f);
	}

	protected void readView(MemoView view) throws IOException {
		view.setFontName(readString());
		view.setFontSize(dis.readInt());

		int fontStyle = dis.readUnsignedShort();
		view.setFontStyle(((fontStyle & 0x01) == 0x01) ? FontStyle.ITALIC : FontStyle.NORMAL);
		view.setFontWeight(((fontStyle & 0x02) == 0x02) ? FontWeight.BOLD : FontWeight.NORMAL);
		view.setTextDecoration(((fontStyle & 0x04) == 0x04) ? TextDecoration.UNDERLINE : TextDecoration.NONE);

		view.setFontColor(readColor());

		int adjust = dis.readInt();
		view.setTextAlign(convertTextAlign(adjust));
		view.setVerticalAlign(convertVerticalAlign(adjust));
		view.setRotate(((adjust & 0x04) == 0x04));

		dis.readUnsignedShort(); // Font Charset

		fontStyle = dis.readUnsignedShort();
		view.getHighlight().setFontStyle(((fontStyle & 0x01) != 0) ? FontStyle.ITALIC : FontStyle.NORMAL);
		view.getHighlight().setFontWeight(((fontStyle & 0x02) != 0) ? FontWeight.BOLD : FontWeight.NORMAL);
		view.getHighlight().setTextDecoration(((fontStyle & 0x04) != 0) ? TextDecoration.UNDERLINE : TextDecoration.NONE);

		view.getHighlight().setFontColor(readColor());
		view.getHighlight().setFillColor(readColor());
		view.getHighlight().setText(readString());

		if (version == VERSION_VERY_OLD) {
			view.setFlags(view.getFlags() | ViewFlag.WordWrap);
		}
	}

	protected void readView(PictureView view) throws IOException {
		view.setCentered((view.getFlags() & ViewFlag.PictCenter) == ViewFlag.PictCenter);
		view.setKeepRatio((view.getFlags() & ViewFlag.PictRatio) == ViewFlag.PictRatio);

		AbstractImage<Image> image = null;

		int pictureType = dis.readUnsignedByte();
		switch (pictureType) {
		case 0: // no picture
			break;
		case 1: // BMP
			image = new BMPImage();
			break;
		case 4: // JPEG
			image = new JPEGImage();
			break;
		default:
			log.warn(String.format("Picture type %d is not supported, skipping picture...", pictureType));
		}

		int nextPos = dis.readInt();

		if (image != null) {
			BufferedImage bi = ImageIO.read(dis);
			image.setImage(bi);
			view.setImage(image);
		}

		openStream();
		dis.skip(nextPos);
	}

	protected void readView(BandView view) throws IOException {
		view.setFormat(null);
		view.setFrameType(null);
	}

	protected void readView(SubReportView view) throws IOException {
		view.setSubPage(dis.readInt());
	}

	protected void readView(LineView view) throws IOException {

	}

	protected void readView(BarCodeView view) throws IOException {
		view.setCheckSum(dis.readBoolean());
		view.setShowText(dis.readBoolean());

		dis.readByte();

		int bcType = dis.readUnsignedByte();
		view.setBarCodeType(convertToBarCodeType(bcType));

		dis.readInt();
		dis.readDouble();
		view.setAngle((int) Math.round(dis.readDouble()));
	}

	protected boolean readWordBool() throws IOException {
		return (dis.readUnsignedShort() > 0);
	}

	protected String readShortString() throws IOException {
		int strLen = dis.readUnsignedByte();
		byte[] strContents = new byte[strLen];
		dis.readFully(strContents);
		String result = new String(strContents);
		return normalizeString(result);
	}

	protected String readString() throws IOException {
		String result;

		if (version >= VERSION_CURRENT) {
			int strLen = dis.readUnsignedShort();
			byte[] strContents = new byte[strLen];
			dis.readFully(strContents);
			dis.read();
			result = new String(strContents);
		} else {
			StringBuffer sb = new StringBuffer(4096);
			byte[] b = new byte[1];
			do {
				dis.readFully(b);
				sb.append(new String(b));
			} while (b[0] != 0);
			result = sb.toString();
		}

		return normalizeString(result);
	}

	protected String readMemo() throws IOException {
		trim = false;
		try {
			StringBuffer result = new StringBuffer(4096);
			if (version >= VERSION_CURRENT) {
				int hasNext = dis.readUnsignedShort();
				if (hasNext == 0) {
					dis.read();
				} else {
					while (hasNext > 0) {
						int strLen = dis.readUnsignedShort();
						byte[] strContents = new byte[strLen];
						dis.readFully(strContents);
						if (result.length() > 0) {
							result.append("\n");
						}
						result.append(normalizeString(new String(strContents)));
						hasNext = dis.readUnsignedByte();
					}
				}
			} else {
				byte[] b = new byte[1];
				do {
					dis.readFully(b);
					if ((b[0] == 13) && (result.length() > 0)) {
						result.append("\n");
					} else if (b[0] != 0) {
						result.append(new String(b));
					}
				} while (b[0] != 0);
			}

			return normalizeString(result.toString());
		} finally {
			trim = true;
		}
	}

	protected Color readColor() throws IOException {
		int value = dis.readInt();
		if (value == 0x1FFFFFFF) {
			return new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 0x00);
		} else {
			return new Color(value);
		}
	}

	protected String normalizeString(String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		String result = StringUtils.replaceEach(value, new String[] { "\r\n", "\n\r", "\r" }, new String[] { "\n", "\n", "\n" });
		if (trim) {
			result = result.trim();
		}
		return (result != null && result.length() > 0) ? result : null;
	}

	protected String createUniqueName(Class<?> viewClass) {
		Integer count = viewIDs.get(viewClass);
		if (count == null) {
			count = new Integer(0);
		}

		String name;
		do {
			count++;
			name = viewClass.getSimpleName() + count;
		} while (viewNames.contains(name));

		viewIDs.put(viewClass, count);
		return name;
	}

	protected FrameStyle convertFrameStyle(int value) {
		return (value == 0x05) ? FrameStyle.DOUBLE : (value == 0x04) ? FrameStyle.DOTTED : (value == 0x03) ? FrameStyle.DASHED
				: (value == 0x02) ? FrameStyle.DOTTED : (value == 0x01) ? FrameStyle.DASHED : FrameStyle.SOLID;
	}

	protected BandType convertToBandType(int value) {
		return value == 0 ? BandType.REPORT_TITLE : value == 1 ? BandType.REPORT_SUMMARY : value == 2 ? BandType.PAGE_HEADER
				: value == 3 ? BandType.PAGE_FOOTER : value == 4 ? BandType.MASTER_HEADER : value == 5 ? BandType.MASTER_DATA
						: value == 6 ? BandType.MASTER_FOOTER : value == 7 ? BandType.DETAIL_HEADER : value == 8 ? BandType.DETAIL_DATA
								: value == 9 ? BandType.DETAIL_FOOTER : value == 10 ? BandType.SUB_DETAIL_HEADER : value == 11 ? BandType.SUB_DETAIL_DATA
										: value == 12 ? BandType.SUB_DETAIL_FOOTER : value == 13 ? BandType.OVERLAY : value == 14 ? BandType.COLUMN_HEADER
												: value == 15 ? BandType.COLUMN_FOOTER : value == 16 ? BandType.GROUP_HEADER
														: value == 17 ? BandType.GROUP_FOOTER : value == 18 ? BandType.CROSS_HEADER
																: value == 19 ? BandType.CROSS_DATA : value == 20 ? BandType.CROSS_FOOTER : BandType.NONE;
	}

	protected TextAlign convertTextAlign(int adjust) {
		return ((adjust & 0x03) == 0x03) ? TextAlign.JUSTIFY : ((adjust & 0x02) == 0x02) ? TextAlign.CENTER : ((adjust & 0x01) == 0x01) ? TextAlign.RIGHT
				: TextAlign.LEFT;
	}

	protected VerticalAlign convertVerticalAlign(int adjust) {
		return ((adjust & 0x08) == 0x08) ? VerticalAlign.MIDDLE : ((adjust & 0x10) == 0x10) ? VerticalAlign.BOTTOM : VerticalAlign.TOP;
	}

	protected BarCodeType convertToBarCodeType(int value) {
		return value == 0 ? BarCodeType.BCD_2_5_interleaved : value == 1 ? BarCodeType.BCD_2_5_industrial : value == 2 ? BarCodeType.BCD_2_5_matrix
				: value == 3 ? BarCodeType.BCD_39 : value == 4 ? BarCodeType.BCD_39Extended : value == 5 ? BarCodeType.BCD_128A
						: value == 6 ? BarCodeType.BCD_128B : value == 7 ? BarCodeType.BCD_128C : value == 8 ? BarCodeType.BCD_93
								: value == 9 ? BarCodeType.BCD_93Extended : value == 10 ? BarCodeType.BCD_MSI : value == 11 ? BarCodeType.BCD_PostNet
										: value == 12 ? BarCodeType.BCD_Codabar : value == 13 ? BarCodeType.BCD_EAN8 : BarCodeType.BCD_EAN13;
	}
}
