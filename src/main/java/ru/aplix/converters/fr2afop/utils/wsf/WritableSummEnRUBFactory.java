package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnRUB;

public class WritableSummEnRUBFactory {

	private static WritableSummEnRUB instance;

	public static WritableSummEnRUB getInstance() {
		if (instance == null) {
			instance = new WritableSummEnRUB();
		}
		return instance;
	}
}
