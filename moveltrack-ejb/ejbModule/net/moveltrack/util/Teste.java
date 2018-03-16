package net.moveltrack.util;



public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double r = 159d;
		int rs = (int)Math.floor((r+50)/100)*100;
		rs=(rs<100?100:rs);
		System.out.println(rs);
		//System.out.println(MyPasswordEncrypt.encrypt2("1234"));
	}

}
