package net.moveltrack.gateway;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.BitSet;

import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;
import net.moveltrack.gateway.persistence.SmsDAO;
import net.moveltrack.gateway.utils.Utils;

public abstract class CommandHandler {

	public CommandHandler() {
	}

	public abstract void sendCommandToTracker(Sms sms);
	
	public void sendToTerminal(byte[] response,Socket socket,Sms sms) {
		try {  
			printSocketStatus(true,"smsTipo="+sms.getTipo().toString(),socket);
			socket.setSoTimeout(15*1000);
			Utils.log(false,"Writing to the socket...");
			socket.getOutputStream().write(response);
			Utils.log(false,"Writed to the socket!");
			
			SmsDAO.getInstance().updateStatusTo(sms,SmsStatus.ENVIADO);
			Thread.sleep(2000);
			
		} catch (SocketTimeoutException ste) {
			Utils.log(false,ste.getMessage());
			SmsDAO.getInstance().updateStatusTo(sms,SmsStatus.ESPERANDO);
		} catch (Exception e) {
			SmsDAO.getInstance().updateStatusTo(sms,SmsStatus.ESPERANDO);
		}
	}
	
	private boolean isSocketOk(Socket socket) {
		return !socket.isOutputShutdown()&&socket.isBound()&&!socket.isClosed()&&socket.isConnected();
	}
	
	public void printBuff(Boolean DEBUG_MODE,byte...buff){
		for (int i = 0; i < buff.length && DEBUG_MODE; i++) {
			System.out.printf("%02x ",buff[i]);
		}
		if(DEBUG_MODE)
			System.out.println();
	}	

	public BitSet fromByte(byte b)  
	{  
		BitSet bits = new BitSet(8);  
		for (int i = 0; i < 8; i++)  
		{  
			bits.set(i, (b & 1) == 1);  
			b >>= 1;  
		}  
		return bits;  
	}	

	public void printSocketStatus(Boolean DEBUG_MODE,String title,Socket socket) {
		if(DEBUG_MODE) {
			System.out.println(title+ " "+(socket!=null?"not null":"null"));
			if(socket!=null) {
				System.out.println(title+ " "+(socket.isInputShutdown()?"input shutdown":"input ok"));
				System.out.println(title+ " "+(socket.isOutputShutdown()?"output shutdown":"output ok"));
				System.out.println(title+ " "+(socket.isBound()?"bound":"not bound"));
				System.out.println(title+ " "+(socket.isConnected()?"connected":"not conected"));
				System.out.println(title+ " "+(socket.isClosed()?"closed":"not closed"));
				System.out.println(title+ " "+"Inet Address: "+socket.getInetAddress());
			}
		}
	}    
}