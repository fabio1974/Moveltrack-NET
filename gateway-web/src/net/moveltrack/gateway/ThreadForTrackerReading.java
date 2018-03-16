package net.moveltrack.gateway;

import java.net.Socket;

import net.moveltrack.gateway.utils.Utils;


public class ThreadForTrackerReading  extends Thread{
	Socket socket;
	boolean DEBUG_MODE;
	
	ReadingProtocolHandler ph;
	
	public ThreadForTrackerReading(ReadingProtocolHandler ph,Socket socket){
		this.socket = socket;
		DEBUG_MODE = ph.getClass().getName().endsWith("CRX3");
		this.ph = ph;
	}
	
	public void run() { 
		try{ 
			Utils.log(DEBUG_MODE," GOT CLIENT WITH IP:->"+ socket.getInetAddress()+"<- ON SERVER PORT "+ socket.getPort()+ "-" + socket.getLocalPort()+ " - "+ ph.getClass().getName());
			socket.setSoTimeout(10*60*1000);
			ph.startReading(socket);
		} 
		catch (Exception e){ 
			if(DEBUG_MODE)
				e.printStackTrace();
		}
		finally{ 
			try{
				Utils.log(DEBUG_MODE," - STOP THREAD FOR SOCKET AT "+socket.getInetAddress()+". Closing socket...");
				if(socket!=null) 
					socket.close();
			}catch(Exception e){
				if(DEBUG_MODE)				
					e.printStackTrace();
			} 
		}
	}
}


