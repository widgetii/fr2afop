package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MimeType")
@XmlEnum
public enum MimeType {

	@XmlEnumValue("image/bmp")
	BMP,

	@XmlEnumValue("image/jpeg")
	JPEG,

	@XmlEnumValue("image/x-emf")
	EMF,

	@XmlEnumValue("image/svg+xml")
	SVG
}
