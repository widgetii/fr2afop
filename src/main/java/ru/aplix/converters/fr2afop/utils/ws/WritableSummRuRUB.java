package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummRuRUB extends WritableSummRu {

	{
		forms[0] = new String[] { "копейка", "копейки", "копеек", "1" };
		forms[1] = new String[] { "рубль", "рубля", "рублей", "0" };
	}

	@Override
	protected boolean use100thShares() {
		return true;
	}
}
