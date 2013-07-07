package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "VerticalAlign")
@XmlEnum
public enum VerticalAlign {

	@XmlEnumValue("top")
	TOP,

	@XmlEnumValue("middle")
	MIDDLE,

	@XmlEnumValue("bottom")
	BOTTOM
}
