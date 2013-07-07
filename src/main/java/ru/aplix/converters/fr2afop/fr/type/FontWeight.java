package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FontWeight")
@XmlEnum
public enum FontWeight {

	@XmlEnumValue("normal")
	NORMAL,

	@XmlEnumValue("bold")
	BOLD
}
