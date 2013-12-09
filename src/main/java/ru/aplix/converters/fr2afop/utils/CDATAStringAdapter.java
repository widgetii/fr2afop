package ru.aplix.converters.fr2afop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAStringAdapter extends XmlAdapter<String, String> {
	
	private static final Pattern PATTERN = Pattern.compile("((?<=\\<\\!\\[CDATA\\[)[\\S\\s]+(?=\\]\\]\\>))");
	private static final String CDATA_START = "<![CDATA[";
	private static final String CDATA_END = "]]>";

	@Override
	public String marshal(String normalString) throws Exception {
		if (normalString == null) {
			return null;
		}
		return CDATA_START + normalString + CDATA_END;
	}

	@Override
	public String unmarshal(String cdataString) throws Exception {
		if (cdataString == null) {
			return null;
		}
		Matcher matcher = PATTERN.matcher(cdataString);
		if (matcher.find()) {
			return matcher.group();
		}
		return cdataString.trim();
	}
}
