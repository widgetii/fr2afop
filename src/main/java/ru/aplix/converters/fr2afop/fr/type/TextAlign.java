package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "TextAlign")
@XmlEnum
public enum TextAlign {

	@XmlEnumValue("left")
	LEFT,

	@XmlEnumValue("right")
	RIGHT,

	@XmlEnumValue("center")
	CENTER,

	@XmlEnumValue("justify")
	JUSTIFY
}
