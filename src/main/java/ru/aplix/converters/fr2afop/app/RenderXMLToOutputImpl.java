package ru.aplix.converters.fr2afop.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

import ru.aplix.converters.fr2afop.reader.InputStreamOpener;
import ru.aplix.converters.fr2afop.utils.Utils;

public class RenderXMLToOutputImpl extends CommandImpl implements RenderXMLToOutput {

	private final Log log = LogFactory.getLog(getClass());

	public static final String CONF_FILE = "/conf/fop.xconf";
	public static final String XSLT_FILE = "/xsl/report.xsl";

	private String outputFormat;
	private String configFileName;
	private InputStreamOpener inputStreamOpener;

	private static FopFactory fopFactory = null;
	private static Templates cachedXSLT = null;

	private FopFactory getFopFactory() {
		if (fopFactory == null) {
			fopFactory = FopFactory.newInstance();
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
		log.info(String.format("  output: \"%s\"", getOutputFileName()));
		log.info(String.format("  format: \"%s\"", outputFormat));
		log.info(String.format("  configuration: \"%s\"", configFile.getAbsolutePath()));
		log.info("");

		// Render XML file to output format
		render(configFile);

		log.info("Done\n");
	}

	private void render(File configFile) throws ConfigurationException, SAXException, IOException, TransformerException {
		// Create fopFactory
		FopFactory fopFactory = getFopFactory();

		// Configure fopFactory using configuration file
		DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
		Configuration cfg = cfgBuilder.buildFromFile(configFile);
		fopFactory.setUserConfig(cfg);

		// Setup XSLT
		Transformer transformer = getTransformer();

		// Setup output
		OutputStream out = new FileOutputStream(getOutputFileName());
		out = new BufferedOutputStream(out);

		try {
			// Setup input for XSLT transformation
			Source src;
			if (inputStreamOpener != null) {
				src = new StreamSource(inputStreamOpener.openStream());
			} else {
				src = new StreamSource(new File(getInputFileName()));
			}

			Result res;
			if (!MimeConstants.MIME_XSL_FO.equals(outputFormat)) {
				// Construct fop with desired output format
				Fop fop = fopFactory.newFop(outputFormat, out);

				// Resulting SAX events (the generated FO)
				// must be piped through to FOP
				res = new SAXResult(fop.getDefaultHandler());
			} else {
				res = new StreamResult(out);
			}

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} finally {
			out.close();
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

	@Override
	public void copy(Command dest) {
		super.copy(dest);

		if (dest instanceof RenderXMLToOutput) {
			RenderXMLToOutput d = (RenderXMLToOutput) dest;
			d.setOutputFormat(outputFormat);
			d.setFopConfigFileName(configFileName);
		}
	}
}
