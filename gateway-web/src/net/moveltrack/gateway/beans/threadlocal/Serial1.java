package net.moveltrack.gateway.beans.threadlocal;

public class Serial1 {

	public static final ThreadLocal<Byte> instance = new ThreadLocal<Byte>();
	
	public static void setSerial1(Byte serial1) {
		instance.set(serial1);
	}

	public static Byte getSerial1() {
		return instance.get();
	}

}