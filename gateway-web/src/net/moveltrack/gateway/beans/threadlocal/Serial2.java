package net.moveltrack.gateway.beans.threadlocal;

public class Serial2 {

	public static final ThreadLocal<Byte> instance = new ThreadLocal<Byte>();
	
	public static void setSerial2(Byte serial1) {
		instance.set(serial1);
	}

	public static Byte getSerial2() {
		return instance.get();
	}

}