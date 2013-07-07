package ru.aplix.converters.fr2afop.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.fr.dataset.Column;
import ru.aplix.converters.fr2afop.fr.dataset.Dataset;
import ru.aplix.converters.fr2afop.fr.dataset.Row;
import ru.aplix.converters.fr2afop.utils.Utils;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummEnEURFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummEnFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummEnRUBFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummEnUSDFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummRuEURFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummRuFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummRuRUBFactory;
import ru.aplix.converters.fr2afop.utils.wsf.WritableSummRuUSDFactory;

public class Functions extends AbstractFunctions {

	private final Log log = LogFactory.getLog(getClass());

	// @formatter:off
	public static final String[][] FUNC_REPLACEMENT = new String[][] { 
		{ "(?i)Round[\\s]*\\(",   "f:round(" }, 
		{ "(?i)Int[\\s]*\\(",   "f:trunc(" }, 
		{ "(?i)Frac[\\s]*\\(",  "f:frac(" },
		{ "(?i)[\\s]+Mod[\\s]+", " mod " },
		{ "(?i)Sum[\\s]*\\(",   "f:sum(" },
		{ "(?i)Avg[\\s]*\\(",   "f:avg(" },
		{ "(?i)Min[\\s]*\\(",   "f:min(" },
		{ "(?i)Max[\\s]*\\(",   "f:max("   },
		{ "(?i)Count[\\s]*\\(", "f:count(" },
		{ "(?i)Str[\\s]*\\(",   "f:str("  },
		{ "(?i)Copy[\\s]*\\(",  "f:copy(" },
		{ "(?i)UpperCase[\\s]*\\(", "f:upperCase(" },
		{ "(?i)LowerCase[\\s]*\\(", "f:lowerCase(" },
		{ "(?i)NameCase[\\s]*\\(", "f:nameCase(" },
		{ "(?i)ToWordsRU[\\s]*\\(", "f:toWordsRU(" },
		{ "(?i)ToRoublesRU[\\s]*\\(", "f:toRoublesRU(" },
		{ "(?i)ToEutosRU[\\s]*\\(", "f:toEurosRU(" },
		{ "(?i)ToDollarsRU[\\s]*\\(", "f:toDollarsRU(" },
		{ "(?i)ToWordsEN[\\s]*\\(", "f:toWordsEN(" },
		{ "(?i)ToRoublesEN[\\s]*\\(", "f:toRoublesEN(" },
		{ "(?i)ToEutosEN[\\s]*\\(", "f:toEurosEN(" },
		{ "(?i)ToDollarsEN[\\s]*\\(", "f:toDollarsEN(" }
	};
	// @formatter:on

	private Double parseDouble(String str) {
		if (str != null && str.length() > 0) {
			try {
				return Double.valueOf(str);
			} catch (Exception e) {
				log.warn(String.format("Can't convert '%s' to Double", str), e);
				return Double.valueOf(0d);
			}
		} else {
			return Double.valueOf(0d);
		}
	}

	public long round(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = parseDouble(vv.getValue());
		} else if (o instanceof Number) {
			o = parseDouble("" + o);
		}

		return Math.round((Double) o);
	}

	public long trunc(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = parseDouble(vv.getValue());
		} else if (o instanceof Number) {
			o = parseDouble("" + o);
		}

		return ((Double) o).longValue();
	}

	public double frac(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = parseDouble(vv.getValue());
		} else if (o instanceof Number) {
			o = parseDouble("" + o);
		}

		return (Double) o - ((Double) o).longValue();
	}

	public double sum(Object o) {
		o = Utils.unarray(o);
		Dataset ds = lookupDataset(o);
		if (ds != null && (o instanceof VariableValue)) {
			String columnName = ((VariableValue) o).getName();
			int columnIndex = -1;
			try {
				double result = 0d;
				for (Row row : ds.getRows()) {
					if (columnIndex == -1) {
						columnIndex = getColumnIndex(row.getColumns(), columnName);
					}

					Column column = row.getColumns().get(columnIndex);
					if (column.getValue() != null && column.getValue().length() > 0) {
						result += parseDouble(column.getValue());
					} else {
						result += parseDouble(((VariableValue) o).getValue());
					}
				}
				return result;
			} catch (IndexOutOfBoundsException ioobe) {
				log.warn(String.format("Column '%s' not found in '%s'", columnName, ds.getName()));
				return Double.NaN;
			}
		}
		return Double.NaN;
	}

	public double avg(Object o) {
		o = Utils.unarray(o);
		Dataset ds = lookupDataset(o);
		if (ds != null && (o instanceof VariableValue)) {
			String columnName = ((VariableValue) o).getName();
			int columnIndex = -1;
			try {
				double result = 0d;
				double count = 0;
				for (Row row : ds.getRows()) {
					if (columnIndex == -1) {
						columnIndex = getColumnIndex(row.getColumns(), columnName);
					}

					Column column = row.getColumns().get(columnIndex);
					if (column.getValue() != null && column.getValue().length() > 0) {
						result += parseDouble(column.getValue());
					} else {
						result += parseDouble(((VariableValue) o).getValue());
					}
					count++;
				}
				return result / count;
			} catch (IndexOutOfBoundsException ioobe) {
				log.warn(String.format("Column '%s' not found in '%s'", columnName, ds.getName()));
				return Double.NaN;
			}
		}
		return Double.NaN;
	}

	public double min(Object o) {
		o = Utils.unarray(o);
		Dataset ds = lookupDataset(o);
		if (ds != null && (o instanceof VariableValue)) {
			String columnName = ((VariableValue) o).getName();
			int columnIndex = -1;
			try {
				double result = Double.MAX_VALUE;
				for (Row row : ds.getRows()) {
					if (columnIndex == -1) {
						columnIndex = getColumnIndex(row.getColumns(), columnName);
					}

					Column column = row.getColumns().get(columnIndex);
					double d;
					if (column.getValue() != null && column.getValue().length() > 0) {
						d = parseDouble(column.getValue());
					} else {
						d = parseDouble(((VariableValue) o).getValue());
					}
					if (d < result) {
						result = d;
					}
				}
				return result;
			} catch (IndexOutOfBoundsException ioobe) {
				log.warn(String.format("Column '%s' not found in '%s'", columnName, ds.getName()));
				return Double.NaN;
			}
		}
		return Double.NaN;
	}

	public double max(Object o) {
		o = Utils.unarray(o);
		Dataset ds = lookupDataset(o);
		if (ds != null && (o instanceof VariableValue)) {
			String columnName = ((VariableValue) o).getName();
			int columnIndex = -1;
			try {
				double result = Double.MIN_VALUE;
				for (Row row : ds.getRows()) {
					if (columnIndex == -1) {
						columnIndex = getColumnIndex(row.getColumns(), columnName);
					}

					Column column = row.getColumns().get(columnIndex);
					double d;
					if (column.getValue() != null && column.getValue().length() > 0) {
						d = parseDouble(column.getValue());
					} else {
						d = parseDouble(((VariableValue) o).getValue());
					}
					if (d > result) {
						result = d;
					}
				}
				return result;
			} catch (IndexOutOfBoundsException ioobe) {
				log.warn(String.format("Column '%s' not found in '%s'", columnName, ds.getName()));
				return Double.NaN;
			}
		}
		return Double.NaN;
	}

	public int count(Object o) {
		Dataset ds = lookupDataset(o);
		if (ds == null) {
			return -1;
		}
		return ds.getRows().size();
	}

	public String str(Object value) {
		return "" + value;
	}

	public String str(Object o, int digits) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = parseDouble(vv.getValue());
		} else if (o instanceof Number) {
			o = parseDouble("" + o);
		}

		return String.format(String.format("%%.%df", digits), (Double) o);
	}

	public String copy(Object o, int index, int count) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = vv.getValue();
		}

		try {
			index--;
			return ((String) o).substring(index, index + count);
		} catch (IndexOutOfBoundsException ioobe) {
			return (String) o;
		}
	}

	public String upperCase(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = vv.getValue();
		}

		return ((String) o).toUpperCase();
	}

	public String lowerCase(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = vv.getValue();
		}

		return ((String) o).toLowerCase();
	}

	public String nameCase(Object o) {
		o = Utils.unarray(o);
		if (o instanceof VariableValue) {
			VariableValue vv = ((VariableValue) o);
			o = vv.getValue();
		}

		String text = (String) o;
		if (text.length() > 0) {
			return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
		} else {
			return text;
		}
	}

	public String toWordsRU(Number number) {
		return WritableSummRuFactory.getInstance().numberToString(number);
	}

	public String toRoublesRU(Number number) {
		return WritableSummRuRUBFactory.getInstance().numberToString(number);
	}

	public String toEurosRU(Number number) {
		return WritableSummRuEURFactory.getInstance().numberToString(number);
	}

	public String toDollarsRU(Number number) {
		return WritableSummRuUSDFactory.getInstance().numberToString(number);
	}

	public String toWordsEn(Number number) {
		return WritableSummEnFactory.getInstance().numberToString(number);
	}

	public String toRoublesEN(Number number) {
		return WritableSummEnRUBFactory.getInstance().numberToString(number);
	}

	public String toEurosEN(Number number) {
		return WritableSummEnEURFactory.getInstance().numberToString(number);
	}

	public String toDollarsEN(Number number) {
		return WritableSummEnUSDFactory.getInstance().numberToString(number);
	}
}
