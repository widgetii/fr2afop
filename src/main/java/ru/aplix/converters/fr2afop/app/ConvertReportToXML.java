package ru.aplix.converters.fr2afop.app;

public interface ConvertReportToXML extends Command {

	void setReportReaderClassName(String reportReaderClassName);
	
	public void setConvConfigFileName(String configFileName);
}
