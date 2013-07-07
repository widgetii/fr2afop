package ru.aplix.converters.fr2afop.fr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.dataset.Dataset;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Report")
public class Report {

	@XmlElementWrapper(name = "Pages")
	@XmlElement(name = "Page", type = Page.class)
	private List<Page> pages;
	@XmlElementWrapper(name = "Variables")
	@XmlElement(name = "Variable", type = Variable.class)
	private List<Variable> variables;
	@XmlElementWrapper(name = "Datasets")
	@XmlElement(name = "Dataset", type = Dataset.class)
	private List<Dataset> datasets;

	@XmlAttribute(name = "PrinterName")
	private String printerName;
	//@XmlAttribute(name = "PrintToDefault")
	private transient boolean printToDefault;
	//@XmlAttribute(name = "DoublePass")
	private transient boolean doublePass;
	@XmlAttribute(name = "FileVersion")
	private int fileVersion;

	public List<Page> getPages() {
		if (pages == null) {
			pages = new ArrayList<Page>();
		}

		return pages;
	}

	public void setPages(List<Page> value) {
		this.pages = value;
	}

	public List<Variable> getVariables() {
		if (variables == null) {
			variables = new ArrayList<Variable>();
		}

		return variables;
	}

	public void setVariables(List<Variable> value) {
		this.variables = value;
	}

	public List<Dataset> getDatasets() {
		if (datasets == null) {
			datasets = new ArrayList<Dataset>();
		}

		return datasets;
	}

	public void setDatasets(List<Dataset> value) {
		this.datasets = value;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String value) {
		this.printerName = value;
	}

	public boolean isPrintToDefault() {
		return printToDefault;
	}

	public void setPrintToDefault(boolean value) {
		this.printToDefault = value;
	}

	public boolean isDoublePass() {
		return doublePass;
	}

	public void setDoublePass(boolean value) {
		this.doublePass = value;
	}

	public int getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(int value) {
		this.fileVersion = value;
	}
}
