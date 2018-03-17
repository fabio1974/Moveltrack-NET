package com.iugu;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static String original = "23793.38128 50009.493169 16000.050803 9 71100000000254";
	public static String value = "23793381285000949316916000050803971100000000254";
	public static String mask =  "AAAAA.AAAAA AAAAA.AAAAAA AAAAA.AAAAAA A AAAAAAAAAAAAAA";
	

	public static void main(String args[]) {
		try {
			System.out.println(formatString(value, mask));
			System.out.println(original);
			// output : A-1234-B567-Z
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}

	public static String formatString(String string, String mask) throws java.text.ParseException {
		javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(string);
	}
    
 
}
