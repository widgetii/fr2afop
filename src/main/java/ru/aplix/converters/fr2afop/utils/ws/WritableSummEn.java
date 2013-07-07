package ru.aplix.converters.fr2afop.utils.ws;

public class WritableSummEn extends WritableSummRu {

	// @formatter:off
	final String[] s1 = {
			"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
	};
    
	{
        str11 = new String[]{
        	"", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
        };
        
        str10 = new String[]{
        	"", "ten", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninety"
        };
        
        for (int i = 0; i < s1.length; i++) {
            str100[i] = s1[i] + " hundred";
        }
    }
	
    final String[] s4 = {"", "", "thousand", "million", "billion"};
    // @formatter:on

	@Override
	protected String getS1(int n, int gender) {
		return s1[n];
	}

	@Override
	protected int getUnitGender(int idx) {
		return 0;
	}

	@Override
	protected String getUnit(int idx, long count) {
		if (!use100thShares() && idx == 1) {
			return "";
		}

		return s4[idx] + (count != 1 ? "s" : "");
	}
}
