package ru.aplix.converters.fr2afop.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Utilities.
 */
public final class Utils {

	/**
	 * Hidden constructor.
	 */
	private Utils() {
	}

	/**
	 * Convert any object instance to XML string.
	 * 
	 * @param o
	 *            object instance
	 * @return XML string
	 * @throws JAXBException
	 *             if any unexpected problem occurs during the marshalling
	 */
	public static String objectToXMLString(Object o) throws JAXBException {
		JAXBContext inst = JAXBContext.newInstance(o.getClass());
		Marshaller marshaller = inst.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter out = new StringWriter();
		marshaller.marshal(o, out);
		return out.toString();
	}

	/**
	 * Convert xml string to an object instance.
	 * 
	 * @param xml
	 *            string
	 * @param objectClass
	 *            class of returned object
	 * @return a new class instance
	 * @throws JAXBException
	 *             if any unexpected problem occurs during the unmarshalling
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlStringToObject(String xml, Class<T> objectClass) throws JAXBException {
		JAXBContext inst = JAXBContext.newInstance(objectClass);
		Unmarshaller unmarshaller = inst.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new StringReader(xml));
	}

	/**
	 * Convert file to an object instance.
	 * 
	 * @param xml
	 *            string
	 * @param objectClass
	 *            class of returned object
	 * @return a new class instance
	 * @throws JAXBException
	 *             if any unexpected problem occurs during the unmarshalling
	 * @throws FileNotFoundException
	 *             if file not found
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fileToObject(File file, Class<T> objectClass) throws JAXBException, FileNotFoundException {
		JAXBContext inst = JAXBContext.newInstance(objectClass);
		Unmarshaller unmarshaller = inst.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new FileReader(file));
	}

	/**
	 * Return full path to jar file.
	 * 
	 * @param c
	 *            class
	 * @return path
	 */
	public static String getJarFolder(Class<?> c) {
		String name = c.getName().replace('.', '/');
		String s = c.getResource("/" + name + ".class").toString();
		s = s.replace('/', File.separatorChar);
		s = s.substring(0, s.indexOf(".jar") + 4);
		s = s.substring(s.lastIndexOf(':') - 1);
		return s.substring(0, s.lastIndexOf(File.separatorChar) + 1);
	}

	/**
	 * Tokenize string in elements.
	 * 
	 * @param text
	 *            text
	 * @return list of tokens
	 */
	public static List<String> tokenize(String text) {
		if (text == null || text.length() == 0) {
			return null;
		}

		List<String> result = new ArrayList<String>();

		char[] delimiter = new char[] { '[', ']' };
		int delimIndex = 0;
		int fromIndex = 0;
		while (fromIndex < text.length()) {
			int delimiterPos = text.indexOf(delimiter[delimIndex], fromIndex);
			if (delimiterPos == -1) {
				delimiterPos = text.length();
			}
			if (delimIndex == 1 && delimiterPos < text.length()) {
				delimiterPos++;
			}
			result.add(text.substring(fromIndex, delimiterPos));
			fromIndex = delimiterPos;
			delimIndex = (delimIndex == 0) ? 1 : 0;
		}

		if (result.size() == 0) {
			result.add(text);
		}
		return result;
	}

	/**
	 * Unwind object from array.
	 * 
	 * @param o
	 *            object
	 * @return object or first array element
	 */
	public static Object unarray(Object o) {
		if (o != null) {
			if (o instanceof Object[] && ((Object[]) o).length == 1) {
				o = ((Object[]) o)[0];
			}
		}
		return o;
	}
}
