package ru.aplix.converters.fr2afop.utils;

public class CommandLine {

	private String[] args;
	private int index;

	public CommandLine(String[] args) {
		this.args = args;
		index = 0;
	}

	public void reset() {
		index = 0;
	}

	public boolean hasArg() {
		return index < args.length;
	}

	public void next() {
		index++;
	}

	public String getArg() {
		return (index < args.length) ? args[index] : null;
	}

	public String getNextArg() {
		next();
		return getArg();
	}
}
