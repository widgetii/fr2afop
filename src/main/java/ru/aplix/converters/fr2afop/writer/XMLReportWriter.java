package ru.aplix.converters.fr2afop.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.CharEncoding;
import org.w3c.dom.Document;

import ru.aplix.converters.fr2afop.fr.Report;

public class XMLReportWriter implements ReportWriter {

	private File xsltFile = null;

	public XMLReportWriter() {
	}

	public XMLReportWriter(File xsltFile) {
		this.xsltFile = xsltFile;
	}

	@Override
	public void writeToStream(Report report, OutputStreamOpener so) throws Exception {
		Writer out = new BufferedWriter(new OutputStreamWriter(so.openStream(), CharEncoding.UTF_8));
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			Document document = docBuilderFactory.newDocumentBuilder().newDocument();

			JAXBContext inst = JAXBContext.newInstance(Report.class);
			Marshaller marshaller = inst.createMarshaller();
			marshaller.marshal(report, document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			if (xsltFile != null) {
				Source xslt = new StreamSource(xsltFile);
				transformer = transformerFactory.newTransformer(xslt);
			} else {
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "Content");
				transformer.setOutputProperty(OutputKeys.ENCODING, CharEncoding.UTF_8);
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			}

			transformer.transform(new DOMSource(document), new StreamResult(out));

			out.flush();
		} finally {
			out.close();
		}
	}
}
