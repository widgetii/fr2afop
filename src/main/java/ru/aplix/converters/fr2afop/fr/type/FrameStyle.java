package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FrameStyle")
@XmlEnum
public enum FrameStyle {

	@XmlEnumValue("solid")
	SOLID,

	@XmlEnumValue("dashed")
	DASHED,

	@XmlEnumValue("dotted")
	DOTTED,

	@XmlEnumValue("double")
	DOUBLE
}
