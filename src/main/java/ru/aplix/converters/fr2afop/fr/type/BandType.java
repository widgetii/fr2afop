package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "BandType")
@XmlEnum
public enum BandType {

	@XmlEnumValue("report-title")
	REPORT_TITLE,

	@XmlEnumValue("report-summary")
	REPORT_SUMMARY,

	@XmlEnumValue("page-header")
	PAGE_HEADER,

	@XmlEnumValue("page-footer")
	PAGE_FOOTER,

	@XmlEnumValue("master-header")
	MASTER_HEADER,

	@XmlEnumValue("master-data")
	MASTER_DATA,

	@XmlEnumValue("master-footer")
	MASTER_FOOTER,

	@XmlEnumValue("detail-header")
	DETAIL_HEADER,

	@XmlEnumValue("detail-Data")
	DETAIL_DATA,

	@XmlEnumValue("detail-footer")
	DETAIL_FOOTER,

	@XmlEnumValue("sub-detail-header")
	SUB_DETAIL_HEADER,

	@XmlEnumValue("sub-detail-Data")
	SUB_DETAIL_DATA,

	@XmlEnumValue("sub-detail-Footer")
	SUB_DETAIL_FOOTER,

	@XmlEnumValue("overlay")
	OVERLAY,

	@XmlEnumValue("column-header")
	COLUMN_HEADER,

	@XmlEnumValue("column-footer")
	COLUMN_FOOTER,

	@XmlEnumValue("group-header")
	GROUP_HEADER,

	@XmlEnumValue("group-footer")
	GROUP_FOOTER,

	@XmlEnumValue("cross-header")
	CROSS_HEADER,

	@XmlEnumValue("cross-data")
	CROSS_DATA,

	@XmlEnumValue("cross-footer")
	CROSS_FOOTER,

	@XmlEnumValue("none")
	NONE
}
