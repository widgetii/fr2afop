package ru.aplix.converters.fr2afop.fr.dataset;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Dataset")
public class Dataset {

	@XmlAttribute(name = "Name")
	private String name;

	@XmlAttribute(name = "Database")
	private String database;

	@XmlElementWrapper(name = "Synonyms")
	@XmlElement(name = "Synonym")
	private List<String> synonyms;

	@XmlElementWrapper(name = "Parameters")
	@XmlElement(name = "Parameter")
	private List<Parameter> parameters;

	@XmlElement(name = "Query")
	private String query;

	@XmlElementWrapper(name = "Rows")
	@XmlElement(name = "Row", type = Row.class)
	private List<Row> rows;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public List<String> getSynonyms() {
		if (synonyms == null) {
			synonyms = new ArrayList<String>();
		}
		return synonyms;
	}

	public void setSynonyms(List<String> value) {
		this.synonyms = value;
	}

	public List<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<Parameter>();
		}
		return parameters;
	}

	public void setParameters(List<Parameter> value) {
		this.parameters = value;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Row> getRows() {
		if (rows == null) {
			rows = new ArrayList<Row>();
		}
		return rows;
	}

	public void setRows(List<Row> value) {
		this.rows = value;
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}
}
