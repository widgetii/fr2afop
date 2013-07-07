package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "SpecialFunc")
@XmlEnum
public enum FunctionType {

	@XmlEnumValue("page-num")
	PAGE_NUM,

	@XmlEnumValue("date")
	DATE,

	@XmlEnumValue("time")
	TIME,

	@XmlEnumValue("line-num")
	LINE_NUM,

	@XmlEnumValue("line-through-num")
	LINE_THROUGH_NUM,

	@XmlEnumValue("columns-num")
	COLUMN_NUM,

	@XmlEnumValue("current-num")
	CURRENT_NUM,

	@XmlEnumValue("total-pages")
	TOTAL_PAGES
}
