package ru.aplix.converters.fr2afop;

import java.awt.Color;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.fr.Page;
import ru.aplix.converters.fr2afop.fr.Report;
import ru.aplix.converters.fr2afop.fr.Variable;
import ru.aplix.converters.fr2afop.fr.type.FrameTypeEnum;
import ru.aplix.converters.fr2afop.fr.view.MemoView;
import ru.aplix.converters.fr2afop.fr.view.PictureView;
import ru.aplix.converters.fr2afop.utils.Utils;

public class UtilsTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * @throws Exception
	 */
	public void testJAXBMarshalling() throws Exception {
		Report report = createReport();

		String xmlString = Utils.objectToXMLString(report);
		assertTrue("Object was not converted to string.", (xmlString != null) && (xmlString.length() > 0));
		log.info(String.format("Formatting report to XML:%s", xmlString));

		Object wrapper2 = Utils.xmlStringToObject(xmlString, Report.class);
		assertTrue("String was not converted to object.", (wrapper2 != null) && (wrapper2 instanceof Report));

		String xmlString2 = Utils.objectToXMLString(wrapper2);
		log.info(String.format("Formatting report to XML:%s", xmlString2));
		assertTrue("Original and converted objects are not equal.", xmlString.equals(xmlString2));
	}

	private Report createReport() {
		MemoView view1 = new MemoView();
		view1.setName("MemoView1");
		view1.setMemo("MemoView contents");
		view1.setFillColor(Color.YELLOW);
		view1.getHighlight().setFillColor(Color.YELLOW);
		view1.getHighlight().setText("Highligh Text");
		view1.getFrameType().setAll();

		PictureView view2 = new PictureView();
		view2.setName("PictureView1");
		view2.setMemo("PictureView contents");
		view2.getFrameType().setEdge(FrameTypeEnum.LEFT);
		view2.getFrameType().setEdge(FrameTypeEnum.RIGHT);

		Page page = new Page();
		page.getViews().add(view1);
		page.getViews().add(view2);

		Report report = new Report();
		report.getPages().add(page);

		Variable var = new Variable();
		var.setName("Variable1");
		var.setValue("");
		report.getVariables().add(var);

		var = new Variable();
		var.setName("Variable2");
		var.setValue("");
		report.getVariables().add(var);

		return report;
	}
}
