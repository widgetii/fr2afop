package ru.aplix.converters.fr2afop.utils.wsf;

import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnUSD;

public class WritableSummEnUSDFactory {

	private static WritableSummEnUSD instance;

	public static WritableSummEnUSD getInstance() {
		if (instance == null) {
			instance = new WritableSummEnUSD();
		}
		return instance;
	}
}
