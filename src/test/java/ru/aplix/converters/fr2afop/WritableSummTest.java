package ru.aplix.converters.fr2afop;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aplix.converters.fr2afop.utils.ws.WritableSumm;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummEn;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnEUR;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnRUB;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummEnUSD;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummRu;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuEUR;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuRUB;
import ru.aplix.converters.fr2afop.utils.ws.WritableSummRuUSD;

public class WritableSummTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	public void test(Class<? extends WritableSumm> cls) {
		WritableSumm w;
		try {
			w = (WritableSumm) cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (int i = 0; i < 5; i++) {
			double n = (double) ((long) (Math.random() * 1000000000L)) / 100;
			log.info(n + "\t" + w.numberToString(n));
		}
		log.info(1000 + "\t" + w.numberToString(1000));
		log.info(0 + "\t" + w.numberToString(0));
		log.info("");
	}

	public void test() {
		log.info("Testing number to word conversion...");
		test(WritableSummRu.class);
		test(WritableSummRuRUB.class);
		test(WritableSummRuUSD.class);
		test(WritableSummRuEUR.class);
		test(WritableSummEn.class);
		test(WritableSummEnRUB.class);
		test(WritableSummEnUSD.class);
		test(WritableSummEnEUR.class);
	}
}
