package ru.aplix.converters.fr2afop.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
		Writer out = new BufferedWriter(new OutputStreamWriter(so.openStream(), "UTF-8"));
		try {
			JAXBContext inst = JAXBContext.newInstance(Report.class);

			Marshaller marshaller = inst.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			if (xsltFile != null) {
				Source xslt = new StreamSource(xsltFile);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(xslt);

				JAXBSource source = new JAXBSource(marshaller, report);
				transformer.transform(source, new StreamResult(out));
			} else {
				marshaller.marshal(report, out);
			}

			out.flush();
		} finally {
			out.close();
		}
	}
}
