package ru.aplix.converters.fr2afop.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.database.ValueResolver;
import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Report;
import ru.aplix.converters.fr2afop.reader.InputStreamOpener;
import ru.aplix.converters.fr2afop.reader.ReportModifier;
import ru.aplix.converters.fr2afop.reader.ReportReader;
import ru.aplix.converters.fr2afop.reader.XMLReportReader;
import ru.aplix.converters.fr2afop.utils.Utils;
import ru.aplix.converters.fr2afop.writer.OutputStreamOpener;
import ru.aplix.converters.fr2afop.writer.ReportWriter;
import ru.aplix.converters.fr2afop.writer.XMLReportWriter;

public class ConvertReportToXMLImpl extends CommandImpl implements ConvertReportToXML {

	private final Log log = LogFactory.getLog(getClass());

	private static final int BUFFER_SIZE = 10240;

	public static final String CONF_FILE = "/conf/fr2afop.xconf";

	private String reportReaderClassName;
	private String configFileName;
	private OutputStreamOpener outputStreamOpener;

	@Override
	public void execute() throws Exception {
		// Get config file
		if (configFileName == null) {
			configFileName = Utils.getJarFolder(getClass()) + CONF_FILE;
		}
		final File configurationFile = new File(configFileName);
		final Configuration configuration = Utils.fileToObject(configurationFile, Configuration.class);

		log.info("Converting file...");
		log.info(String.format("  source: \"%s\"", getInputFileName()));
		log.info(String.format("  output: \"%s\"", outputStreamOpener == null ? getOutputFileName() : "memory buffer"));
		log.info("");

		if (reportReaderClassName == null) {
			throw new Exception("Command is invalid - report reader was not specified.");
		}

		// Convert report to object
		Class<?> c = Class.forName(reportReaderClassName);
		ReportReader reportReader = (ReportReader) c.newInstance();

		Report report = reportReader.readFromStream(new InputStreamOpener() {
			@Override
			public InputStream openStream() throws IOException {
				return new FileInputStream(getInputFileName());
			}
		}, new ReportModifier() {
			@Override
			public Report modify(Report report) throws Exception {
				// Write report in XML format
				File xsltFile = null;
				if (configuration.getReplacementFile() != null) {
					xsltFile = new File(configuration.getReplacementFile());
					if (xsltFile == null || !xsltFile.exists()) {
						xsltFile = new File(configurationFile.getParent(), configuration.getReplacementFile());
					}

					// Save report to memory stream and restore immediately
					// in order to perform XSLT replacements
					final ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
					try {
						ReportWriter reportWriter = new XMLReportWriter(xsltFile);
						reportWriter.writeToStream(report, new OutputStreamOpener() {
							@Override
							public OutputStream openStream() throws IOException {
								return baos;
							}
						});

						ReportReader newReportReader = new XMLReportReader();
						Report newReport = newReportReader.readFromStream(new InputStreamOpener() {
							@Override
							public InputStream openStream() throws IOException {
								return new ByteArrayInputStream(baos.toByteArray());
							}
						}, null, configuration);
						return newReport;
					} finally {
						baos.close();
					}
				}
				return report;
			}
		}, configuration);

		// Select data from datasets
		ValueResolver vr = new ValueResolver();
		vr.resolve(report, configuration);

		ReportWriter reportWriter = new XMLReportWriter();
		reportWriter.writeToStream(report, outputStreamOpener != null ? outputStreamOpener : new OutputStreamOpener() {
			@Override
			public OutputStream openStream() throws IOException {
				return new FileOutputStream(getOutputFileName());
			}
		});

		log.info("Done\n");
	}

	@Override
	public void setReportReaderClassName(String reportReaderClassName) {
		this.reportReaderClassName = reportReaderClassName;
	}

	@Override
	public void setConvConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public void setOutputStreamOpener(OutputStreamOpener outputStreamOpener) {
		this.outputStreamOpener = outputStreamOpener;
	}

	@Override
	public void copy(Command dest) {
		super.copy(dest);

		if (dest instanceof ConvertReportToXML) {
			ConvertReportToXML d = (ConvertReportToXML) dest;
			d.setReportReaderClassName(reportReaderClassName);
			d.setConvConfigFileName(configFileName);
		}
	}
}
