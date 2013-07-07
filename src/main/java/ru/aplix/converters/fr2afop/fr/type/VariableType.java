package ru.aplix.converters.fr2afop.fr.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "VariableType")
@XmlEnum
public enum VariableType {

	@XmlEnumValue("not-assigned")
	NOT_ASSIGNED,

	@XmlEnumValue("db-field")
	DB_FIELD,

	@XmlEnumValue("function")
	FUNCTION,

	@XmlEnumValue("expression")
	EXPRESSION,
}
