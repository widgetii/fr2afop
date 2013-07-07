package ru.aplix.converters.fr2afop.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.aplix.converters.fr2afop.reader.InputStreamOpener;
import ru.aplix.converters.fr2afop.reader.XMLReportReader;
import ru.aplix.converters.fr2afop.writer.OutputStreamOpener;

public class TwofoldCommand extends CommandImpl implements ConvertReportToXML, RenderXMLToOutput {

	private static final int BUFFER_SIZE = 10240;

	private String reportReaderClassName;
	private String outputFormat;
	private String fopConfigFileName;
	private String convConfigFileName;

	@Override
	public void execute() throws Exception {
		if (XMLReportReader.class.getName().equals(reportReaderClassName)) {
			RenderXMLToOutput cmd = new RenderXMLToOutputImpl();
			cmd.setInputFileName(getInputFileName());
			cmd.setOutputFileName(getOutputFileName());
			cmd.setOutputFormat(outputFormat);
			cmd.setFopConfigFileName(fopConfigFileName);
			cmd.execute();
		} else {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
			try {
				ConvertReportToXMLImpl crtx = new ConvertReportToXMLImpl();
				crtx.setInputFileName(getInputFileName());
				crtx.setReportReaderClassName(reportReaderClassName);
				crtx.setConvConfigFileName(convConfigFileName);
				crtx.setOutputStreamOpener(new OutputStreamOpener() {
					@Override
					public OutputStream openStream() throws IOException {
						return baos;
					}
				});

				RenderXMLToOutputImpl rxto = new RenderXMLToOutputImpl();
				rxto.setOutputFileName(getOutputFileName());
				rxto.setOutputFormat(outputFormat);
				rxto.setFopConfigFileName(fopConfigFileName);
				rxto.setInputStreamOpener(new InputStreamOpener() {
					@Override
					public InputStream openStream() throws IOException {
						return new ByteArrayInputStream(baos.toByteArray());
					}
				});

				crtx.execute();
				rxto.execute();
			} finally {
				baos.close();
			}
		}
	}

	@Override
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	@Override
	public void setFopConfigFileName(String configFileName) {
		this.fopConfigFileName = configFileName;
	}
	
	@Override
	public void setConvConfigFileName(String configFileName) {
		this.convConfigFileName = configFileName;
	}

	@Override
	public void setReportReaderClassName(String reportReaderClassName) {
		this.reportReaderClassName = reportReaderClassName;
	}

	@Override
	public void copy(Command dest) {
		super.copy(dest);

		if (dest instanceof ConvertReportToXML) {
			ConvertReportToXML d = (ConvertReportToXML) dest;
			d.setReportReaderClassName(reportReaderClassName);
			d.setConvConfigFileName(convConfigFileName);
		}

		if (dest instanceof RenderXMLToOutput) {
			RenderXMLToOutput d = (RenderXMLToOutput) dest;
			d.setOutputFormat(outputFormat);
			d.setFopConfigFileName(fopConfigFileName);
		}
	}
}
