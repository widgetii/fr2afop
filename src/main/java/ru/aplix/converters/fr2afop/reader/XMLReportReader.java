package ru.aplix.converters.fr2afop.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Report;

public class XMLReportReader implements ReportReader {

	@Override
	public Report readFromStream(InputStreamOpener so, ReportModifier modifier, Configuration configuration) throws Exception {
		InputStreamReader isreader = new InputStreamReader(so.openStream(), "UTF-8");
		Reader in = new BufferedReader(isreader);
		try {
			JAXBContext inst = JAXBContext.newInstance(Report.class);

			Unmarshaller unmarshaller = inst.createUnmarshaller();
			Report report = (Report) unmarshaller.unmarshal(in);
			return modifier != null ? modifier.modify(report) : report;
		} finally {
			in.close();
		}
	}
}
