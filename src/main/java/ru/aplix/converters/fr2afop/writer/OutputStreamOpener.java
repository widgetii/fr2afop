package ru.aplix.converters.fr2afop.writer;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamOpener {

	OutputStream openStream() throws IOException;
}
