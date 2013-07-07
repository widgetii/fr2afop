package ru.aplix.converters.fr2afop.fr;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import ru.aplix.converters.fr2afop.fr.type.FunctionType;
import ru.aplix.converters.fr2afop.fr.type.VariableType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Variable")
public class Variable {

	@XmlAttribute(name = "Name")
	private String name;
	@XmlAttribute(name = "Category")
	private String category;
	@XmlAttribute(name = "Type")
	private VariableType type;
	@XmlAttribute(name = "Function")
	private FunctionType function;
	@XmlAttribute(name = "Dataset")
	private String dataSet;
	@XmlAttribute(name = "Content")
	private String content;
	@XmlValue
	private String value;

	private transient Set<Variable> next;
	private transient Set<Variable> previous;

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public VariableType getType() {
		return type;
	}

	public void setType(VariableType value) {
		this.type = value;
	}

	public FunctionType getFunction() {
		return function;
	}

	public void setFunction(FunctionType value) {
		this.function = value;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String value) {
		this.dataSet = value;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean hasNext() {
		return next != null && next.size() > 0;
	}

	public Set<Variable> getNext() {
		if (next == null) {
			next = new HashSet<Variable>();
		}
		return next;
	}

	public boolean hasPrevious() {
		return previous != null && previous.size() > 0;
	}

	public Set<Variable> getPrevious() {
		if (previous == null) {
			previous = new HashSet<Variable>();
		}
		return previous;
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}
}
