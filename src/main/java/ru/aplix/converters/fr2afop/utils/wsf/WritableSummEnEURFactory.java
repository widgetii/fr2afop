package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnEUR;

public class WritableSummEnEURFactory {

	private static WritableSummEnEUR instance;

	public static WritableSummEnEUR getInstance() {
		if (instance == null) {
			instance = new WritableSummEnEUR();
		}
		return instance;
	}
}
