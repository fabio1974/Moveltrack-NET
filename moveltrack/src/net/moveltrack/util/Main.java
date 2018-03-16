package net.moveltrack.util;

public class Main {

	public static void main(String[] args) {
		
		//System.out.println(Utils.priceWithDecimal(10778.358));
		
		String s = "R$10.778,36";

		s = s.replaceAll("[^\\d]", "");
 
		System.out.println(Float.parseFloat(s)/100);

	}

}
