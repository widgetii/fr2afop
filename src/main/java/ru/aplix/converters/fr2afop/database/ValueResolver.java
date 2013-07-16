package ru.aplix.converters.fr2afop.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.fr.Configuration;
import ru.aplix.converters.fr2afop.fr.Report;
import ru.aplix.converters.fr2afop.fr.Variable;
import ru.aplix.converters.fr2afop.fr.dataset.Column;
import ru.aplix.converters.fr2afop.fr.dataset.Connection;
import ru.aplix.converters.fr2afop.fr.dataset.Database;
import ru.aplix.converters.fr2afop.fr.dataset.Dataset;
import ru.aplix.converters.fr2afop.fr.dataset.Parameter;
import ru.aplix.converters.fr2afop.fr.dataset.Row;
import ru.aplix.converters.fr2afop.fr.type.VariableType;

public class ValueResolver {

	private final Log log = LogFactory.getLog(getClass());

	private Map<String, Variable> variableMap;
	private DatasetResolver datasetResolver;

	private DateFormat dateFormat;
	private DateFormat timeFormat;
	private Date now;

	private static final Pattern pBrackets = Pattern.compile("\\[([^\\[\\]]+)\\]");
	private static final Pattern pCurlyBrackets = Pattern.compile("\\$\\{([\\w]+)\\}");

	private static final JexlEngine jexl = new JexlEngine();
	private static final Functions functions = new Functions();

	static {
		jexl.setCache(512);
		jexl.setLenient(true);
		jexl.setSilent(false);

		Map<String, Object> funcs = new HashMap<String, Object>();
		funcs.put("f", functions);
		jexl.setFunctions(funcs);
	}

	public ValueResolver() {
		dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		now = new Date();

		variableMap = new HashMap<String, Variable>();
	}

	public void resolve(final Report report, Configuration configuration) {
		buildVariableMap(report.getVariables());

		// Build database map
		Map<String, Database> databaseMap = new HashMap<String, Database>();
		for (Database database : configuration.getDatabases()) {
			databaseMap.put(database.getName(), database);
		}

		// Build dataset map
		Map<String, Dataset> datasetMap = new HashMap<String, Dataset>();
		for (Dataset dataset : configuration.getDatasets()) {
			datasetMap.put(dataset.getName(), dataset);
		}

		// Set data set resolver
		datasetResolver = new DatasetResolver() {
			@Override
			public Dataset lookupDataset(String name) {
				for (Dataset dataset : report.getDatasets()) {
					if (dataset.getName().equalsIgnoreCase(name)) {
						return dataset;
					}
					for (String synonyms : dataset.getSynonyms()) {
						if (synonyms.equalsIgnoreCase(name)) {
							return dataset;
						}
					}
				}
				return null;
			}
		};
		functions.setDatasetResolver(datasetResolver);

		// Enumerate datasets in report
		for (Dataset dataset : report.getDatasets()) {
			Dataset configDataset = datasetMap.get(dataset.getName());
			if (configDataset != null) {
				Database database = databaseMap.get(configDataset.getDatabase());
				if (database != null) {
					String query = getParameterizedQuery(configDataset);
					populateDataset(dataset, database.getName(), database.getConnection(), query, report.getDatasets());
				}
			}
		}

		// Populate rest of variables with values
		populateVariables(report.getVariables(), report.getDatasets());
	}

	protected void buildVariableMap(List<Variable> variableList) {
		// Build variable map
		variableMap.clear();
		for (Variable variable : variableList) {
			if (VariableType.NOT_ASSIGNED.equals(variable.getType())) {
				continue;
			}

			variableMap.put(variable.getName(), variable);
		}

		// Determine variable dependencies
		for (Variable variable : variableList) {
			if (VariableType.EXPRESSION.equals(variable.getType())) {
				// Try to find dependence variable in brackets
				Matcher mBrackets = pBrackets.matcher(variable.getContent());
				while (mBrackets.find() && mBrackets.groupCount() == 1) {
					// If found then establish relationship
					String name = mBrackets.group(1);
					Variable depVar = variableMap.get(name);
					if (depVar != null) {
						depVar.getPrevious().add(variable);
						variable.getNext().add(depVar);
					}
				}
			}
		}
	}

	private String getParameterizedQuery(Dataset dataset) {
		String text = dataset.getQuery();

		Matcher mBrackets = pCurlyBrackets.matcher(dataset.getQuery());
		while (mBrackets.find() && mBrackets.groupCount() == 1) {
			final String param = mBrackets.group(1);

			Parameter parameter = (Parameter) CollectionUtils.find(dataset.getParameters(), new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Parameter item = (Parameter) arg0;
					return param.equalsIgnoreCase(item.getName());
				}
			});

			if (parameter != null) {
				text = StringUtils.replace(text, "${" + param + "}", parameter.getValue(), 1);
			}
		}

		return text;
	}

	private void populateDataset(Dataset dataset, String databaseName, Connection connection, String query, List<Dataset> allDatasets) {
		if (dataset == null || dataset.getRows().size() != 1) {
			return;
		}

		log.info(String.format("Fetching data from '%s' using '%s'", dataset.getName(), databaseName));
		int count = 0;
		try {
			// Register database JDBC driver
			Class.forName(connection.getDriver());

			java.sql.Connection con = null;
			java.sql.Statement stmt = null;
			java.sql.ResultSet rs = null;
			try {
				// Connect to the database
				con = java.sql.DriverManager.getConnection(connection.getUrl(), connection.getUserName(), connection.getPassword());
				con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);

				// Execute main query
				stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(query);

				// Gather columns info
				Map<String, Integer> columnsInfo = new HashMap<String, Integer>();
				ResultSetMetaData rsMetaData = rs.getMetaData();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String columnName = rsMetaData.getColumnLabel(i);
					Integer columnType = rsMetaData.getColumnType(i);
					columnsInfo.put(columnName, columnType);
				}

				// Enumerate records in result set
				Row firstRow = dataset.getRows().get(0);
				Row row = null;
				while (rs.next()) {
					// Create a new row with the same columns as first row has
					if (row == null) {
						row = firstRow;
					} else {
						row = new Row();
						for (Column column : firstRow.getColumns()) {
							Column c = new Column();
							c.setName(column.getName());
							c.setValue("");
							row.getColumns().add(c);
						}
						dataset.getRows().add(row);
					}

					// Retrieve column values from result set
					for (Column column : row.getColumns()) {
						String value = findVariableValue(column.getName(), rs, columnsInfo, allDatasets);
						column.setValue(value);
					}

					count++;
				}
			} finally {
				// Close all resources
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}

				if (con != null) {
					con.close();
				}
			}
		} catch (Exception e) {
			log.warn(String.format("Error fetching data from '%s'", dataset.getName()), e);
		}
		log.info(String.format("Fetched %d records", count));
	}

	private void populateVariables(List<Variable> variableList, List<Dataset> datasets) {
		try {
			for (Variable variable : variableList) {
				findVariableValue(variable, null, null, datasets);
			}
		} catch (SQLException e) {
			// This exception will never thrown because result set is absent
			e.printStackTrace();
		}
	}

	private String findVariableValue(String variableName, java.sql.ResultSet rs, Map<String, Integer> columnsInfo, List<Dataset> datasets) throws SQLException {
		// Find variable by name
		Variable variable = variableMap.get(variableName);
		if (variable == null) {
			return null;
		}

		return findVariableValue(variable, rs, columnsInfo, datasets);
	}

	private String findVariableValue(Variable variable, java.sql.ResultSet rs, Map<String, Integer> columnsInfo, List<Dataset> datasets) throws SQLException {
		variable.setValue(variable.getContent());

		// Build list of dependencies
		List<Variable> dependencies = null;
		if (variable.hasNext()) {
			dependencies = new ArrayList<Variable>();
			buildDependencies(variable, variable, dependencies);

			// Calculate dependencies values first
			for (Variable dependency : dependencies) {
				String value = calcVariableValue(dependency, rs, columnsInfo);
				dependency.setValue(value);
			}
		}

		String value;
		if (VariableType.EXPRESSION.equals(variable.getType())) {
			// Calculate expression
			value = calcExpression(variable.getValue(), dependencies, datasets);
		} else {
			// Calculate single value
			value = calcVariableValue(variable, rs, columnsInfo);
		}

		// Return calculated value
		variable.setValue(value);
		return variable.getValue();
	}

	private void buildDependencies(Variable mainVariable, Variable variable, List<Variable> dependencies) {
		if (variable.hasNext()) {
			// Build dependencies and replace nested expressions
			for (Variable next : variable.getNext()) {
				if (VariableType.EXPRESSION.equals(next.getType())) {
					String newValue = StringUtils.replace(mainVariable.getValue(), "[" + next.getName() + "]", next.getContent());
					mainVariable.setValue(newValue);
				} else {
					dependencies.add(next);
				}
			}

			// Go further
			for (Variable next : variable.getNext()) {
				buildDependencies(mainVariable, next, dependencies);
			}
		}
	}

	private String calcVariableValue(Variable variable, java.sql.ResultSet rs, Map<String, Integer> columnsInfo) throws SQLException {
		// Set initial value for variable
		String value = "";

		// Get value and unwind dependent variables
		if (VariableType.DB_FIELD.equals(variable.getType())) {
			if (rs != null && columnsInfo != null && columnsInfo.containsKey(variable.getContent())) {
				Integer type = columnsInfo.get(variable.getContent());
				switch (type) {
				case java.sql.Types.DATE:
					Date date = rs.getDate(variable.getContent());
					value = date != null ? dateFormat.format(date) : "";
					break;
				case java.sql.Types.TIME:
					Date time = rs.getDate(variable.getContent());
					value = time != null ? timeFormat.format(time) : "";
					break;
				case java.sql.Types.TIMESTAMP:
					Date dt = rs.getDate(variable.getContent());
					if (dt != null) {
						long t = DateUtils.getFragmentInSeconds(dt, Calendar.DATE);
						if (t > 0) {
							value = dateFormat.format(dt) + " " + timeFormat.format(dt);
						} else {
							value = dateFormat.format(dt);
						}
					}
					break;
				default:
					value = rs.getString(variable.getContent());
					if (value == null) {
						value = "";
					}
					break;
				}
			} else {
				// Try to resolve data-set if result set is already closed
				Dataset dataset = datasetResolver.lookupDataset(variable.getDataSet());
				if (dataset != null && dataset.getRows().size() > 0) {
					Row row = dataset.getRows().get(dataset.getRows().size() - 1);
					for (Column column : row.getColumns()) {
						if (column.getName().equalsIgnoreCase(variable.getName())) {
							value = column.getValue();
							break;
						}
					}
				}
			}
		} else if (VariableType.FUNCTION.equals(variable.getType())) {
			// Generate function value
			switch (variable.getFunction()) {
			case DATE:
				value = dateFormat.format(now);
				break;
			case TIME:
				value = timeFormat.format(now);
				break;
			case LINE_NUM:
			case LINE_THROUGH_NUM:
			case CURRENT_NUM:
				if (rs != null) {
					value = "" + rs.getRow();
				}
				break;
			default:
				break;
			}
		} else {
			// for not-assigned variables there is no value
			log.debug(String.format("Variable '%s' is not assigned", variable.getName()));
		}

		return value;
	}

	private String calcExpression(String expression, List<Variable> vars, List<Dataset> datasets) {
		Object result = null;
		try {
			// populate the context
			JexlContext context = new MapContext();
			if (vars != null) {
				for (Variable var : vars) {
					VariableValue vv = new VariableValue(var);
					context.set(vv.getName(), vv);
				}
			}
			if (datasets != null) {
				for (Dataset dataset : datasets) {
					context.set(dataset.getName(), dataset);
					for (String synonym : dataset.getSynonyms()) {
						context.set(synonym, dataset);
					}
				}
			}

			expression = prepareExpression(expression);

			// work it out
			Expression e = jexl.createExpression(expression);
			result = e.evaluate(context);
		} catch (Exception exception) {
			log.warn(String.format("Can't evaluate the expression '%s'", expression), exception);
		}

		if (result == null) {
			return "";
		} else if (result instanceof String) {
			return (String) result;
		} else {
			return result.toString();
		}
	}

	private String prepareExpression(String expression) {
		return StringUtils.replaceEach(expression, new String[] { "[", "]", "=" }, new String[] { "", "", "==" });
	}
}
