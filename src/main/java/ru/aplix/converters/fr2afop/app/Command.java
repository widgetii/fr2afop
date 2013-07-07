package ru.aplix.converters.fr2afop.app;

public interface Command {

	void execute() throws Exception;

	boolean hasInput();

	boolean hasOutput();

	String getInputFileName();

	void setInputFileName(String inputFileName);

	String getOutputFileName();

	void setOutputFileName(String outputFileName);
	
	void copy(Command dest);
}
