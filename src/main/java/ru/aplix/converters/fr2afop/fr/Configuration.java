package ru.aplix.converters.fr2afop.fr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ru.aplix.converters.fr2afop.fr.dataset.Database;
import ru.aplix.converters.fr2afop.fr.dataset.Dataset;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fr2afop")
public class Configuration {

	@XmlElement(name = "ReplacementFile")
	private ReplacementFile replacementFile;

	@XmlElementWrapper(name = "Databases")
	@XmlElement(name = "Database", type = Database.class)
	private List<Database> databases;

	@XmlElementWrapper(name = "Datasets")
	@XmlElement(name = "Dataset", type = Dataset.class)
	private List<Dataset> datasets;

	public ReplacementFile getReplacementFile() {
		if (replacementFile == null) {
			replacementFile = new ReplacementFile();
		}
		return replacementFile;
	}

	public void setReplacementFile(ReplacementFile replacementFile) {
		this.replacementFile = replacementFile;
	}

	public List<Database> getDatabases() {
		if (databases == null) {
			databases = new ArrayList<Database>();
		}
		return databases;
	}

	public void setDatabases(List<Database> databases) {
		this.databases = databases;
	}

	public List<Dataset> getDatasets() {
		if (datasets == null) {
			datasets = new ArrayList<Dataset>();
		}
		return datasets;
	}

	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}
}
