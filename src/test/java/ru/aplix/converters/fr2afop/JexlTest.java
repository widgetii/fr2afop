package ru.aplix.converters.fr2afop;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.database.Functions;

public class JexlTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	private static final JexlEngine jexl = new JexlEngine();

	static {
		jexl.setCache(512);
		jexl.setLenient(false);
		jexl.setSilent(false);

		Map<String, Object> funcs = new HashMap<String, Object>();
		funcs.put("f", new Functions());
		jexl.setFunctions(funcs);
	}

	public void testExpression() {
		log.info("Checking JEXL...");
		String expression = "(f:round((G1 + G2 + G3) * 0.1)) + G4";
		log.info(String.format("Expression: %s", expression));
		Expression e = jexl.createExpression(expression);

		// populate the context
		JexlContext context = new MapContext();
		context.set("G1", 1.5f);
		context.set("G2", 2.6f);
		context.set("G3", 3.7f);
		context.set("G4", -0.5f);

		// work it out
		Object result = e.evaluate(context);
		log.info(String.format("Result: %s", result.toString()));
	}
}
