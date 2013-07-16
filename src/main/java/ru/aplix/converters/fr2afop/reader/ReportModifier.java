package ru.aplix.converters.fr2afop.reader;

import ru.aplix.converters.fr2afop.fr.Report;

public interface ReportModifier {

	Report modify(Report report) throws Exception;
}
