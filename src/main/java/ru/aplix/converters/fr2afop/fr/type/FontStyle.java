package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FontStyle")
@XmlEnum
public enum FontStyle {

	@XmlEnumValue("normal")
	NORMAL,

	@XmlEnumValue("italic")
	ITALIC
}
