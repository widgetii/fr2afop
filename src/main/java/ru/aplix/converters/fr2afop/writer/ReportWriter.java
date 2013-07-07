package ru.aplix.converters.fr2afop.writer;

import ru.aplix.converters.fr2afop.fr.Report;

public interface ReportWriter {

	void writeToStream(Report report, OutputStreamOpener os) throws Exception;
}
