package net.moveltrack.gateway.utils;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class HashMaps {
	
	public static Map<String,Socket> socketsMap;
	public static Map<String,Byte[]> seriais;
	private static HashMaps hashMaps;
	
	private HashMaps() {
		socketsMap = new HashMap<String, Socket>();
		seriais = new HashMap<String,Byte[]>();
	}

	
	public synchronized static HashMaps getInstance() {
		if(hashMaps==null)
			hashMaps = new HashMaps();
		return hashMaps;
		
	}


	public synchronized Socket getSocketByImei(String imei) {
		return getInstance().socketsMap.get(imei);
	}


	public synchronized byte getSerial1ByImei(String imei) {
		try {
			return (byte)(getInstance().seriais.get(imei)[0]);	
		} catch (Exception e) {
			return (byte)0x00;
		}
		
	}
	
	public void printStatus(boolean DEBUG_MODE) {
		if(!DEBUG_MODE)
			return;
		Set<String> imeis = getInstance().socketsMap.keySet();
		for (String imei : imeis) {
			System.out.println(imei+" -> "+ getSocketByImei(imei).getInetAddress());
		}
	}
	
	public synchronized byte getSerial2ByImei(String imei) {
		try {
			return (byte)(getInstance().seriais.get(imei)[1]);	
		} catch (Exception e) {
			return (byte)0x00;
		}
	}


	public synchronized void setSocketInImei(String imei, Socket socket) {
		getInstance().socketsMap.put(imei,socket);
	}


	public synchronized void setSeriais(String imei, byte serial1, byte serial2) {
		Byte[] bt = {serial1,serial2};
		//printBuff(imei,true,serial1,serial2);
		getInstance().seriais.put(imei,bt);
		
	}
	
	
	/*public void printBuff(String imei,Boolean DEBUG_MODE,byte...buff){
		Utils.log(DEBUG_MODE, "Seriais do imei "+imei);
		for (int i = 0; i < buff.length && DEBUG_MODE; i++) {
			System.out.printf("%02x ",buff[i]);
		}
		if(DEBUG_MODE)
			System.out.println();
	}*/	
	
	
	
	
}
