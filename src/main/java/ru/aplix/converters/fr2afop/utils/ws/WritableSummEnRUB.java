package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummEnRUB extends WritableSummEn {

	{
		s4[0] = "kop";
		s4[1] = "rouble";
	}

	@Override
	protected boolean use100thShares() {
		return true;
	}
}
