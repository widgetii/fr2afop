package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuUSD;

public class WritableSummRuUSDFactory {

	private static WritableSummRuUSD instance;

	public static WritableSummRuUSD getInstance() {
		if (instance == null) {
			instance = new WritableSummRuUSD();
		}
		return instance;
	}
}
