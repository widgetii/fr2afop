package ru.aplix.converters.fr2afop.database;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.fr.dataset.Column;
import ru.aplix.converters.fr2afop.fr.dataset.Dataset;

public abstract class AbstractFunctions implements DatasetResolver {

	private final Log log = LogFactory.getLog(getClass());

	private DatasetResolver datasetResolver;

	public DatasetResolver getDatasetResolver() {
		return datasetResolver;
	}

	public void setDatasetResolver(DatasetResolver datasetResolver) {
		this.datasetResolver = datasetResolver;
	}

	@Override
	public Dataset lookupDataset(String name) {
		Dataset result = null;
		if (datasetResolver != null) {
			result = datasetResolver.lookupDataset(name);
		}
		if (result == null) {
			log.warn(String.format("Dataset '%s' not found", name));
		}
		return result;
	}

	public Dataset lookupDataset(Object o) {
		if (o != null) {
			if (o instanceof Dataset) {
				return (Dataset) o;
			} else if (o instanceof VariableValue) {
				VariableValue vv = (VariableValue) o;
				if (vv.getDataSet() != null) {
					return lookupDataset(vv.getDataSet());
				} else {
					log.warn(String.format("Cannot lookup dataset, because variable '%s' is not db-field variable", vv.getName()));
				}
			}
		}

		return null;
	}

	public int getColumnIndex(List<Column> columns, String columnName) {
		int result = -1;
		int i = 0;
		while (result == -1 && i < columns.size()) {
			Column column = columns.get(i);
			if (columnName.equalsIgnoreCase(column.getName())) {
				result = i;
			}
			i++;
		}
		return result;
	}
}
