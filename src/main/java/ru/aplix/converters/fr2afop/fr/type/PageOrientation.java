package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PageOrientation")
@XmlEnum
public enum PageOrientation {

	@XmlEnumValue("portrait")
	PORTRAIT,

	@XmlEnumValue("landscape")
	LANDSCAPE
}
