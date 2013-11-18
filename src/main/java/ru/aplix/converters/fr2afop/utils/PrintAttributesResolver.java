package ru.aplix.converters.fr2afop.utils;

import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;

public interface PrintAttributesResolver {

	PrintRequestAttributeSet createPrintAttributes(PrintService printService);
}
