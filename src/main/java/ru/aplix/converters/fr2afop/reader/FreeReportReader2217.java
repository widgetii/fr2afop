package ru.aplix.converters.fr2afop.reader;

import java.io.IOException;

public class FreeReportReader2217 extends FreeReportReader {

	@Override
	protected void readFileVersion() throws IOException {
		super.readFileVersion();

		if (version == VERSION_CURRENT) {
			version = VERSION_PREVIOUS;
		}
	}

	@Override
	protected String readString() throws IOException {
		String result;

		int strLen = dis.readInt();
		byte[] strContents = new byte[strLen];
		dis.readFully(strContents);
		result = new String(strContents);

		return normalizeString(result);
	}

	@Override
	protected String readMemo() throws IOException {
		trim = false;
		try {
			return readString();
		} finally {
			trim = true;
		}
	}

	@Override
	protected float[] getDefaultCoeff() {
		return new float[] { 93f / 1.0012f, 93f / 1.0014f };
	}
}
