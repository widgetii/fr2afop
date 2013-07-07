package ru.aplix.converters.fr2afop;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterResolution;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PrinterTest extends TestCase {

	/**
	 * Local logger instance.
	 */
	private final Log log = LogFactory.getLog(getClass());

	private static final int INCH = 72;

	public void test() throws Exception {
		PrintService[] services = PrinterJob.lookupPrintServices();
		for (PrintService service : services) {
			log.info("**********************************************");
			log.info("" + service);
			log.info("");

			PrinterJob printerJob = PrinterJob.getPrinterJob();
			printerJob.setPrintService(service);
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			PageFormat format = printerJob.defaultPage();// printerJob.pageDialog(attributes);
			if (format == null) {
				log.info("Cancelled");
			} else {
				Paper paper = format.getPaper();

				PrinterResolution pr = (PrinterResolution) service.getDefaultAttributeValue(PrinterResolution.class);
				if (pr == null) {
					pr = new PrinterResolution(600, 600, PrinterResolution.DPI);
				}

				int[] resolution = pr.getResolution(PrinterResolution.DPI);

				long pxPageWidth = Math.round(paper.getWidth() / INCH * (double) resolution[0]);
				long pxPageHeight = Math.round(paper.getHeight() / INCH * (double) resolution[1]);

				log.info(String.format("Paper size: %.1f x %.1f in", paper.getWidth() / INCH, paper.getHeight() / INCH));
				log.info(String.format("Paper size: %d x %d px", pxPageWidth, pxPageHeight));
				log.info(String.format("Imageable size: %.1f x %.1f in", paper.getImageableWidth() / INCH, paper.getImageableHeight() / INCH));
				log.info(String.format("Top left: [%.1f; %.1f] in", paper.getImageableX() / INCH, paper.getImageableY() / INCH));
				log.info(String.format("Resolution: %d x %d dpi", resolution[0], resolution[1]));

				for (Attribute attr : attributes.toArray()) {
					log.info("Attribute: " + attr);
				}
			}
			log.info("");
		}
	}
}
