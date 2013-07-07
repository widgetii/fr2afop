package ru.aplix.converters.fr2afop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Report;
import ru.aplix.converters.fr2afop.reader.FreeReportReader;
import ru.aplix.converters.fr2afop.reader.FreeReportReader2217;
import ru.aplix.converters.fr2afop.reader.InputStreamOpener;
import ru.aplix.converters.fr2afop.reader.ReportReader;
import ru.aplix.converters.fr2afop.writer.OutputStreamOpener;
import ru.aplix.converters.fr2afop.writer.ReportWriter;
import ru.aplix.converters.fr2afop.writer.XMLReportWriter;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testReport1() throws Exception {
		testReport(FreeReportReader.class.getName(), "/format/fmt23.frf", "fmt23.xml");
	}

	public void testReport2() throws Exception {
		testReport(FreeReportReader.class.getName(), "/format/fmt22.frf", "fmt22.xml");
	}

	public void testReport3() throws Exception {
		testReport(FreeReportReader2217.class.getName(), "/format/fmt2217.frf", "fmt2217.xml");
	}

	public void testReport4() throws Exception {
		testReport(FreeReportReader2217.class.getName(), "/samples/f113en_ls.frf", "f113en_ls.xml");
	}

	public void testReport5() throws Exception {
		testReport(FreeReportReader2217.class.getName(), "/samples/f103.frf", "f103.xml");
	}

	public void testReport6() throws Exception {
		testReport(FreeReportReader2217.class.getName(), "/samples/f16.frf", "f16.xml");
	}

	public void testReport7() throws Exception {
		testReport(FreeReportReader2217.class.getName(), "/samples/otm1.frf", "otm1.xml");
	}

	private boolean testReport(String reportReaderClassName, final String reportResName, String outputFileName) throws Exception {
		log.info("Converting internal resource file");

		final Class<?> c = Class.forName(reportReaderClassName);
		ReportReader reportReader = (ReportReader) c.newInstance();

		Report report = reportReader.readFromStream(new InputStreamOpener() {
			boolean firstStart = true;

			@Override
			public InputStream openStream() throws IOException {
				if (firstStart) {
					firstStart = false;
					log.info(String.format("Reading report file \"%s\"", reportResName));
				}
				return c.getResourceAsStream(reportResName);
			}
		}, new Configuration());

		File outputDir = new File("target/test-output/").getCanonicalFile();
		if (!outputDir.exists()) {
			log.info(String.format("Creating output directory \"%s\"", outputDir.getPath()));
			outputDir.mkdirs();
		}
		final File outputFile = new File(outputDir, outputFileName);

		log.info(String.format("Writing output file \"%s\"", outputFile.getPath()));
		ReportWriter reportWriter = new XMLReportWriter();
		reportWriter.writeToStream(report, new OutputStreamOpener() {
			@Override
			public OutputStream openStream() throws IOException {
				return new FileOutputStream(outputFile);
			}
		});

		log.info("Done");
		return true;
	}
}
