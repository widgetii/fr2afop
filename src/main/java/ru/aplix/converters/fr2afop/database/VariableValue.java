package ru.aplix.converters.fr2afop.database;

import ru.aplix.converters.fr2afop.fr.Variable;
import ru.aplix.converters.fr2afop.fr.type.VariableType;

public class VariableValue extends Number {

	/**
	 * Use serialVersionUID from JDK 1.0.2 for interoperability.
	 */
	private static final long serialVersionUID = -7729880724102103696L;

	private String name;
	private String dataSet;
	private String value;

	public VariableValue(Variable var) {
		this.name = var.getName();
		this.value = var.getValue();

		if (VariableType.DB_FIELD.equals(var.getType())) {
			this.dataSet = var.getDataSet();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value != null ? value : "";
	}

	@Override
	public int intValue() {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException npe) {
			return 0;
		}
	}

	@Override
	public long longValue() {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException npe) {
			return 0L;
		}
	}

	@Override
	public float floatValue() {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException npe) {
			return Float.NaN;
		}
	}

	@Override
	public double doubleValue() {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException npe) {
			return Double.NaN;
		}
	}
}
