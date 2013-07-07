package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummEnEUR extends WritableSummEn {

	{
		s4[0] = "cent";
		s4[1] = "euro";
	}

	@Override
	protected boolean use100thShares() {
		return true;
	}
}
