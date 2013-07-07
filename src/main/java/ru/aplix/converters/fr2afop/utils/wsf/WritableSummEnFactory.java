package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummEn;

public class WritableSummEnFactory {

	private static WritableSummEn instance;

	public static WritableSummEn getInstance() {
		if (instance == null) {
			instance = new WritableSummEn();
		}
		return instance;
	}
}
