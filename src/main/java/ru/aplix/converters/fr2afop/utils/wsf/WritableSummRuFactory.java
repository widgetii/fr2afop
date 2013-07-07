package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummRu;

public class WritableSummRuFactory {

	private static WritableSummRu instance;

	public static WritableSummRu getInstance() {
		if (instance == null) {
			instance = new WritableSummRu();
		}
		return instance;
	}
}
