package ru.aplix.converters.fr2afop.fr.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Database")
public class Database {

	@XmlAttribute(name = "Name")
	private String name;

	@XmlElement(name = "Connection", type = Connection.class)
	private Connection connection;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
