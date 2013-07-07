package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "TextDecoration")
@XmlEnum
public enum TextDecoration {

	@XmlEnumValue("none")
	NONE,

	@XmlEnumValue("underline")
	UNDERLINE,

	@XmlEnumValue("overline")
	OVERLINE,

	@XmlEnumValue("line-through")
	LINE_THROUGH
}
