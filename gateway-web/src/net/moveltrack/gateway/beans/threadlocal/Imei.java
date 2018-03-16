package net.moveltrack.gateway.beans.threadlocal;

public class Imei {

	public static final ThreadLocal<String> instance = new ThreadLocal<String>();
	
	public static void setImei(String imei) {
		instance.set(imei);
	}

	public static String getImei() {
		return instance.get();
	}

}