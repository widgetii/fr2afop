package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummEnUSD extends WritableSummEn {

	{
		s4[0] = "cent";
		s4[1] = "dollar";
	}

	@Override
	protected boolean use100thShares() {
		return true;
	}
}
