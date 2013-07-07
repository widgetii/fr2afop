package ru.aplix.converters.fr2afop.reader;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamOpener {

	InputStream openStream() throws IOException;
}
