package ru.aplix.converters.fr2afop.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import ru.aplix.converters.fr2afop.fr.type.FrameType;


public class FrameTypeAdapter extends XmlAdapter<String, FrameType> {

	public FrameType unmarshal(String s) {
		return FrameType.fromValue(s);
	}

	public String marshal(FrameType v) {
		return (v != null) ? v.toString() : null;
	}
}
