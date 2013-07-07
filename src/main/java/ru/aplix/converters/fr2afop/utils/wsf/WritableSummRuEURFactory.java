package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuEUR;

public class WritableSummRuEURFactory {

	private static WritableSummRuEUR instance;

	public static WritableSummRuEUR getInstance() {
		if (instance == null) {
			instance = new WritableSummRuEUR();
		}
		return instance;
	}
}
