package ru.aplix.converters.fr2afop;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.utils.Utils;

public class PatternTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	public void testParams() {
		String text = "The list of variables: [var1], [var2] and [complex.var.3]!";
		log.info(String.format("String '%s' contains the following variables:", text));

		Pattern p = Pattern.compile("\\[([^\\[\\]]+)\\]");
		Matcher m = p.matcher(text);
		while (m.find() && m.groupCount() == 1) {
			log.info(m.group(1));
		}
	}

	public void testTable() {
		String text = "CustomerData.Customers.\"Company\"";
		log.info(String.format("String '%s' can be separated as follows:", text));

		Pattern p = Pattern.compile("([\\w\\.]+)\\.\"{0,1}([\\w]+)\"{0,1}");
		Matcher m = p.matcher(text);
		if (m.matches()) {
			log.info(String.format("DataSet = %s", m.group(1)));
			log.info(String.format("FieldName = %s", m.group(2)));
		}
	}

	public void testTableInExp() {
		String text = "Copy(1, CustomerData.Customers.\"Company\" , band1, 2 )";
		log.info(String.format("From expression '%s' the following table can be extracted:", text));

		Pattern p = Pattern.compile("[,\\(]{1}[\\s]*([\\w\\.]+\\.\"[\\w]+\")[\\s]*[,\\)]{1}");
		Matcher m = p.matcher(text);
		while (m.find() && m.groupCount() == 1) {
			log.info(m.group(1));
		}
	}

	public void testTokenize() {
		String text = "The list of variables: [var1], [var2] and [complex.var.3]!";
		List<String> list = Utils.tokenize(text);
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s);
		}
		assertEquals(text, sb.toString());
	}
}
