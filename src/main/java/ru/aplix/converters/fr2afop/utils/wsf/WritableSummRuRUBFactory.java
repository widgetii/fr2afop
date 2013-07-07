package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuRUB;

public class WritableSummRuRUBFactory {

	private static WritableSummRuRUB instance;

	public static WritableSummRuRUB getInstance() {
		if (instance == null) {
			instance = new WritableSummRuRUB();
		}
		return instance;
	}
}
