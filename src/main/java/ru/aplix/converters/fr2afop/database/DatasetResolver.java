package ru.aplix.converters.fr2afop.database;

import ru.aplix.converters.fr2afop.fr.dataset.Dataset;

public interface DatasetResolver {

	Dataset lookupDataset(String name);
}
