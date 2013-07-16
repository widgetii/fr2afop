package ru.aplix.converters.fr2afop.reader;

import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Report;

public interface ReportReader {

	Report readFromStream(InputStreamOpener so, ReportModifier modifier, Configuration configuration) throws Exception;
}
