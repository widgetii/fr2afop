package ru.aplix.converters.fr2afop.fr.type;

public enum FrameTypeEnum {

	LEFT("left"),

	RIGHT("right"),

	TOP("top"),

	BOTTOM("bottom");

	private final String value;

	private FrameTypeEnum(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static FrameTypeEnum fromValue(String v) {
		for (FrameTypeEnum c : FrameTypeEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
