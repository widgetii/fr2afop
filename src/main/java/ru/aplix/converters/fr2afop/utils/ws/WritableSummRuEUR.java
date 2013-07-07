package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummRuEUR extends WritableSummRu {

	{
		forms[0] = new String[] { "цент", "цента", "центов", "0" };
		forms[1] = new String[] { "евро", "евро", "евро", "0" };
	}

	@Override
	protected boolean use100thShares() {
		return true;
	}
}
