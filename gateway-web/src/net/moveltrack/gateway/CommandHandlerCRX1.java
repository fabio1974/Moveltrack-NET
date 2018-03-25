package net.moveltrack.gateway;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.moveltrack.domain.Sms;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Crc16;
import net.moveltrack.gateway.utils.HashMaps;
import net.moveltrack.gateway.utils.Utils;

public class CommandHandlerCRX1 extends CommandHandler {

	boolean DEBUG_MODE = Constantes.DEBUG_CRX1;
	
	public final static byte[] COMMAND_LOCATION =      {0x44,0x57,0x58,0x58,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
							                		   // D    W    X    X    ,    0    0    0    0    0    0    #
	public final static byte[] COMMAND_CONNECT_OIL =   {0x48,0x46,0x59,0x44,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
                                                       // H    F    Y    D    ,    0    0    0    0    0    0    #
	public final static byte[] COMMAND_CUT_OIL =       {0x44,0x59,0x44,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
                                                       // D    Y    D    ,    0    0    0    0    0    0    #
	
	public CommandHandlerCRX1()  {
	}
	
	public void sendCommandToTracker(Sms sms) {
		Utils.log(DEBUG_MODE, "enviando para o imei (sms):"+sms.getImei()); 
		byte[] command = null;
		byte serial1 = HashMaps.getInstance().getSerial1ByImei(sms.getImei());
		byte serial2 = HashMaps.getInstance().getSerial2ByImei(sms.getImei());
		Socket socket = HashMaps.getInstance().getSocketByImei(sms.getImei());
		//printSocketStatus(DEBUG_MODE,"Pegando socket do hash para imei:"+sms.getImei(),socket);
		//HashMaps.getInstance().printStatus(DEBUG_MODE);

		switch (sms.getTipo()) {
		case BLOQUEIO:
			command = buildCRX1Command(COMMAND_CUT_OIL,serial1,serial2);
			Utils.log(DEBUG_MODE,"Comando de Bloqueio");
			printBuff(DEBUG_MODE,command);
			break;
		case DESBLOQUEIO:
			command = buildCRX1Command(COMMAND_CONNECT_OIL,serial1,serial2);
			Utils.log(DEBUG_MODE,"Comando de Desbloqueio");
			printBuff(DEBUG_MODE,command);
			break;
		default:
			break;
		}
		if(command!=null)
			sendToTerminal(command,socket,sms);
	}	

	
	public static byte[] buildCRX1Command(byte[] commandContent,byte ser1,byte ser2) {
		List<Byte> commandList = new ArrayList<Byte>();
		
		int informationContentSize = 4 + commandContent.length;
		int packageSize = 6 + informationContentSize;
		
		commandList.add((byte)0x78);commandList.add((byte)0x78);   															//start
		commandList.add((byte)packageSize);               																	//package size 
		commandList.add((byte)0x80);                      								                                	//protocolo de envio
		commandList.add((byte)informationContentSize);               														//information content size
		commandList.add((byte)0x00);commandList.add((byte)0x01);commandList.add((byte)0xA9);commandList.add((byte)0x63);	//server flag bit 00 01 A9 63 
		
		for (int i = 0; i < commandContent.length; i++) {
			commandList.add((byte)commandContent[i]);                      										        	//content
		}
		
		commandList.add(ser1);commandList.add(ser2);         											             	    //serial
		commandList.add((byte)0x00);commandList.add((byte)0x00);   															//crc
		commandList.add((byte)0x0D);commandList.add((byte)0x0A);    														//stop bit
		
		Object[] commandArray = commandList.toArray();

		byte[] command = new byte[commandArray.length];
		
		for (int i = 0; i < command.length; i++) {
			command[i]= (Byte)commandArray[i];
		}
		
		command = Crc16.setCrc(command);
		return command;
	}
}
