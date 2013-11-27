package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "BarCodeType")
@XmlEnum
public enum BarCodeType {

	@XmlEnumValue("intl2of5")
	BCD_2_5_interleaved,

	@XmlEnumValue("indt2of5")
	BCD_2_5_industrial,

	@XmlEnumValue("datamatrix")
	BCD_2_5_matrix,

	@XmlEnumValue("code39")
	BCD_39,

	@XmlEnumValue("code39ext")
	BCD_39Extended,

	@XmlEnumValue("code128")
	BCD_128A,

	@XmlEnumValue("code128B")
	BCD_128B,

	@XmlEnumValue("code128C")
	BCD_128C,

	@XmlEnumValue("code93")
	BCD_93,

	@XmlEnumValue("code93ext")
	BCD_93Extended,

	@XmlEnumValue("msi")
	BCD_MSI,

	@XmlEnumValue("postnet")
	BCD_PostNet,

	@XmlEnumValue("codabar")
	BCD_Codabar,

	@XmlEnumValue("ean-8")
	BCD_EAN8,

	@XmlEnumValue("ean-13")
	BCD_EAN13,

	@XmlEnumValue("qr")
	BCD_QR
}
