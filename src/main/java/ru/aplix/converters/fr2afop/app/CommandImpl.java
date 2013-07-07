package ru.aplix.converters.fr2afop.app;

public abstract class CommandImpl implements Command {

	private String inputFileName = null;
	private String outputFileName = null;

	@Override
	public boolean hasInput() {
		return inputFileName != null;
	}

	@Override
	public boolean hasOutput() {
		return outputFileName != null;
	}

	@Override
	public String getInputFileName() {
		return inputFileName;
	}

	@Override
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	@Override
	public String getOutputFileName() {
		return outputFileName;
	}

	@Override
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	@Override
	public void copy(Command dest) {
		dest.setInputFileName(inputFileName);
		dest.setOutputFileName(outputFileName);
	}
}
