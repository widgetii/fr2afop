package ru.aplix.converters.fr2afop.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.print.PrintRenderer;
import org.xml.sax.SAXException;

import ru.aplix.converters.fr2afop.reader.InputStreamOpener;
import ru.aplix.converters.fr2afop.utils.Utils;
import ru.aplix.converters.fr2afop.writer.OutputStreamOpener;

public class RenderXMLToOutputImpl extends CommandImpl implements RenderXMLToOutput {

	private final Log log = LogFactory.getLog(getClass());

	public static final String CONF_FILE = "/conf/fop.xconf";
	public static final String XSLT_FILE = "/xsl/report.xsl";

	private String outputFormat;
	private String configFileName;
	private InputStreamOpener inputStreamOpener;
	private OutputStreamOpener outputStreamOpener;

	private static FopFactory fopFactory = null;
	private static Templates cachedXSLT = null;

	private FopFactory getFopFactory(String confFile) throws SAXException, IOException {
		if (fopFactory == null) {
			// Initialization of FOP factory for version 1.1
			// fopFactory = FopFactory.newInstance();

			// Configure fopFactory using configuration file
			// DefaultConfigurationBuilder cfgBuilder = new
			// DefaultConfigurationBuilder();
			// Configuration cfg = cfgBuilder.buildFromFile(configFile);
			// fopFactory.setUserConfig(cfg);

			fopFactory = FopFactory.newInstance(new File(confFile));
		}
		return fopFactory;
	}

	private Transformer getTransformer() throws TransformerConfigurationException {
		if (cachedXSLT == null) {
			// Setup XSLT
			InputStream xsltis = getClass().getResourceAsStream(XSLT_FILE);
			TransformerFactory factory = TransformerFactory.newInstance();
			cachedXSLT = factory.newTemplates(new StreamSource(xsltis));
		}
		return cachedXSLT.newTransformer();
	}

	@Override
	public void execute() throws Exception {
		// Get config file
		if (configFileName == null) {
			configFileName = Utils.getJarFolder(getClass()) + CONF_FILE;
		}
		File configFile = new File(configFileName);

		if (outputFormat == null) {
			outputFormat = MimeConstants.MIME_PDF;
		}

		log.info("Rendering file...");
		log.info(String.format("  source: \"%s\"", inputStreamOpener == null ? getInputFileName() : "memory buffer"));
		log.info(String.format("  output: \"%s\"", outputStreamOpener == null ? getOutputFileName() : "memory buffer"));
		log.info(String.format("  format: \"%s\"", outputFormat));
		log.info(String.format("  configuration: \"%s\"", configFile.getAbsolutePath()));
		log.info("");

		// Render XML file to output format
		render(configFile);

		log.info("Done\n");
	}

	private void render(File configFile) throws ConfigurationException, SAXException, IOException, TransformerException, PrintException {
		// Create fopFactory
		FopFactory fopFactory = getFopFactory(configFileName);

		// Setup XSLT
		Transformer transformer = getTransformer();

		// Setup output
		OutputStream out = null;
		if (!MimeConstants.MIME_FOP_PRINT.equals(outputFormat)) {
			if (outputStreamOpener == null) {
				out = new BufferedOutputStream(new FileOutputStream(getOutputFileName()));
			} else {
				out = new BufferedOutputStream(outputStreamOpener.openStream());
			}
		}

		try {
			// Setup input for XSLT transformation
			Source src;
			if (inputStreamOpener != null) {
				src = new StreamSource(inputStreamOpener.openStream());
			} else {
				src = new StreamSource(new File(getInputFileName()));
			}

			Result res;
			if (MimeConstants.MIME_XSL_FO.equals(outputFormat)) {
				res = new StreamResult(out);

				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);
			} else {
				// Set up a custom user agent so we can supply our own renderer
				// instance
				FOUserAgent userAgent = fopFactory.newFOUserAgent();
				Fop fop;

				if (MimeConstants.MIME_FOP_PRINT.equals(outputFormat)) {
					// Create print renderer
					PrintRenderer renderer = new PrintRenderer(userAgent);
					PrintRendererConfigurator.createDefaultInstance(userAgent).configure(renderer);
					userAgent.setRendererOverride(renderer);

					// Construct FOP with desired output format
					fop = fopFactory.newFop(userAgent);
				} else {
					// Construct fop with desired output format
					fop = fopFactory.newFop(outputFormat, userAgent, out);
				}

				// Resulting SAX events (the generated FO)
				// must be piped through to FOP
				res = new SAXResult(fop.getDefaultHandler());

				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);

				if (MimeConstants.MIME_FOP_PRINT.equals(outputFormat)) {
					// Set up DocPrintJob instance
					DocPrintJob printJob = createDocPrintJob();
					Doc doc = new SimpleDoc(userAgent.getRendererOverride(), DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
					printJob.print(doc, null);
				}
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	@Override
	public void setFopConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public void setInputStreamOpener(InputStreamOpener inputStreamOpener) {
		this.inputStreamOpener = inputStreamOpener;
	}
	
	public void setOutputStreamOpener(OutputStreamOpener outputStreamOpener) {
		this.outputStreamOpener = outputStreamOpener;
	}

	@Override
	public void copy(Command dest) {
		super.copy(dest);

		if (dest instanceof RenderXMLToOutput) {
			RenderXMLToOutput d = (RenderXMLToOutput) dest;
			d.setOutputFormat(outputFormat);
			d.setFopConfigFileName(configFileName);
		}
	}

	private DocPrintJob createDocPrintJob() throws PrintException {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);

		PrintService printService = null;
		for (PrintService service : services) {
			if (getOutputFileName().equalsIgnoreCase(service.getName())) {
				printService = service;
			}
		}

		if (printService != null) {
			return printService.createPrintJob();
		} else {
			throw new PrintException(String.format("Printer \"%s\" not found.", getOutputFileName()));
		}
	}
}
