package ru.aplix.converters.fr2afop.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.apps.TTFReader;
import org.apache.fop.fonts.truetype.TTFFile;

public class ConvertFontToXMLImpl extends CommandImpl implements ConvertFontToXML {

	private final Log log = LogFactory.getLog(getClass());

	@Override
	public void execute() throws Exception {
		String embFile = null;
		String embResource = null;
		String className = null;
		String fontName = null;
		String ttcName = null;
		boolean isCid = true;
		boolean useKerning = true;
		boolean useAdvanced = true;

		TTFReader app = new TTFReader();
		log.info("");
		log.info("Parsing font...");
		TTFFile ttf = app.loadTTF(getInputFileName(), ttcName, useKerning, useAdvanced);
		if (ttf != null) {
			org.w3c.dom.Document doc = app.constructFontXML(ttf, fontName, className, embResource, embFile, isCid, ttcName);

			if (isCid) {
				log.info("Creating CID encoded metrics...");
			} else {
				log.info("Creating WinAnsi encoded metrics...");
			}

			if (doc != null) {
				app.writeFontXML(doc, getOutputFileName());
			}

			if (ttf.isEmbeddable()) {
				log.info("This font contains no embedding license restrictions.");
			} else {
				log.info("** Note: This font contains license retrictions for\n" + "         embedding. This font shouldn't be embedded.");
			}
		}
		log.info("Done\n");
	}
}
