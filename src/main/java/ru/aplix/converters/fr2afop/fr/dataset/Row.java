package ru.aplix.converters.fr2afop.fr.dataset;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Row")
public class Row {

	@XmlElement(name = "Column", type = Column.class)
	private List<Column> columns;

	public List<Column> getColumns() {
		if (columns == null) {
			columns = new ArrayList<Column>();
		}
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
}
