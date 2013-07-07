package ru.aplix.converters.fr2afop.utils;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorAdapter extends XmlAdapter<String, Color> {

	private static final String TRANSPARENT = "transparent";

	public Color unmarshal(String s) {
		if (TRANSPARENT.equals(s)) {
			return new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 0x00);
		} else {
			Integer i = Integer.decode(s);

			int chBlue = (i >> 16) & 0xFF;
			int chGreen = (i >> 8) & 0xFF;
			int chRed = (i >> 0) & 0xFF;

			return new Color(chRed, chGreen, chBlue);
		}
	}

	public String marshal(Color c) {
		if (c == null) {
			return null;
		} else {
			if (c.getTransparency() != Color.OPAQUE) {
				return TRANSPARENT;
			} else {
				return String.format("#%02X%02X%02X", c.getBlue(), c.getGreen(), c.getRed());
			}
		}
	}
}
