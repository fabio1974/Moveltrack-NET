package net.moveltrack.gateway.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.moveltrack.domain.Location;
import net.moveltrack.gateway.GpsCommand;

public class Teste {

	
	
	final static byte[] COMMAND_LOCATION =      {0x44,0x57,0x58,0x58,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
    // D    W    X    X    ,    0    0    0    0    0    0    #

	final static byte[] COMMAND_CONNECT_OIL =   {0x48,0x46,0x59,0x44,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
    // H    F    Y    D    ,    0    0    0    0    0    0    #

	final static byte[] COMMAND_CUT_OIL =       {0x44,0x59,0x44,0x2C,0x30,0x30,0x30,0x30,0x30,0x30,0x23};
    // D    Y    D    ,    0    0    0    0    0    0    #
	
	final static byte[] heartBeat = {0x78,0x78,0x0a,0x13,0x50,0x06,0x03,0x00,0x02,0x03,(byte)0xd2,(byte)0x99,(byte)0xc9,0x0d,0x0a} ;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//buildCommand(COMMAND_CONNECT_OIL,(byte)0x00,(byte)0xA0);
		//readCommand();
		//String receivedCommnad = "DWXX=Lat:S3.812803,Lon:W38.544443,Course:115.22,Speed:0.8630Km/h,DateTime:13-11-30 20:22:35";
		//Location loc = getLocationFromReceivedCommand(receivedCommnad);
		//System.out.print(loc.toString());
		storeHeartBeat(heartBeat);
		

	}
	
	static String imei = "876543210912930";
	
	
	private static void storeHeartBeat(byte[] heartBeat) {
		
		try{
		
			BitSet terminalStatus = fromByte(heartBeat[4]); //terminal Information Content
			
			Utils.log(DEBUG_MODE,"...READING  HEARTBEAT STATUS FROM "+imei);
			
			Location lastLocationWithMcc = new Location();
	
			lastLocationWithMcc.setAlarm(terminalStatus.get(0)?"1":"0");
			lastLocationWithMcc.setIgnition(terminalStatus.get(1)?"1":"0");
			lastLocationWithMcc.setBattery(terminalStatus.get(2)?"1":"0");
			
			boolean bit3 = terminalStatus.get(3);
			boolean bit4 = terminalStatus.get(4);
			boolean bit5 = terminalStatus.get(5);
			
			if(bit5 && !bit4 && !bit3) //100
				lastLocationWithMcc.setAlarmType("100");//SOS");
			if(!bit5 && bit4 && bit3) //011
				lastLocationWithMcc.setAlarmType("011");//Low Battery");
			if(!bit5 && bit4 && !bit3) //010
				lastLocationWithMcc.setAlarmType("010");//Power Cut");
			if(!bit5 && !bit4 && bit3) //001
				lastLocationWithMcc.setAlarmType("001");//Shock Alarm");
			if(!bit5 && !bit4 && !bit3) //000
				lastLocationWithMcc.setAlarmType("000");//Normal");
			
			lastLocationWithMcc.setGps(terminalStatus.get(6)?"On":"Off");
			lastLocationWithMcc.setBloqueio(terminalStatus.get(7)?"On":"Off");
			
			
			byte voltageLevel = heartBeat[5];
			
			if(voltageLevel== 0x00)
				lastLocationWithMcc.setVolt("0%");// - No Power");
			else if(voltageLevel== 0x01)
				lastLocationWithMcc.setVolt("<5%");// - Extremely Low Battery");
			else if(voltageLevel== 0x02)
				lastLocationWithMcc.setVolt("<10%");// - Very Low Battery");
			else if(voltageLevel== 0x03)
				lastLocationWithMcc.setVolt("<30%");// - Low Battery");
			else if(voltageLevel== 0x04)
				lastLocationWithMcc.setVolt("~50%");// - Medium");
			else if(voltageLevel== 0x05)
				lastLocationWithMcc.setVolt(">70%");// - High");
			else if(voltageLevel== 0x06)
				lastLocationWithMcc.setVolt(">90%");// - Very high");
			
			byte gsm = heartBeat[6];
			
			if(gsm== 0x00)
				lastLocationWithMcc.setGsm("0");//Sem sinal");// - No signal");
			else if(gsm== 0x01)
				lastLocationWithMcc.setGsm("1");//Muito fraco");// - Extremely weak signal");
			else if(gsm== 0x02)
				lastLocationWithMcc.setGsm("2");//Fraco");// - Very weak signal");
			else if(gsm== 0x03)
				lastLocationWithMcc.setGsm("3");//Bom");// - Good signal");
			else if(gsm== 0x04)
				lastLocationWithMcc.setGsm("4");//Muito Bom");// - Strong signal");
			
			Utils.log(DEBUG_MODE,"...HEARTBEAT STATUS END");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		
	}

	static boolean DEBUG_MODE = false;
	
	
    public static BitSet fromByte(byte b)  
    {  
        BitSet bits = new BitSet(8);  
        for (int i = 0; i < 8; i++)  
        {  
            bits.set(i, (b & 1) == 1);  
            if(DEBUG_MODE)
            	System.out.print(bits.get(i));
            b >>= 1;  
        }  
        return bits;  
    }  			
	
	
	private static Location getLocationFromReceivedCommand(String receivedCommnad) {
		//Lat:S3.812803,Lon:W38.544443,Course:115.22,Speed:0.8630Km/h,DateTime:13-11-30 20:22:35
		Location loc = new Location();
		try{

			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			GpsCommand gc = new GpsCommand();
			
			String[] quebra =  receivedCommnad.substring("DWXX=".length()).split(",");
			
			float latitude = Float.parseFloat(quebra[0].substring(5));
			float longitude = Float.parseFloat(quebra[1].substring(5));
			
			gc.setNs(quebra[0].substring(4,5));
			gc.setEw(quebra[1].substring(4,5));
			gc.setDatetime(quebra[4].substring("DateTime:".length()));
			gc.setSpeed(quebra[3].substring("Speed:".length(),quebra[3].indexOf("Km/h")));
			
			
			loc.setImei(imei);
			loc.setLatitude(gc.getNs().equals("N")?latitude:(-1)*latitude);
			loc.setLongitude(gc.getEw().equals("E")?longitude:(-1)*longitude);
			loc.setDateLocationInicio(sdf.parse(gc.getDatetime()));
			loc.setDateLocation(sdf.parse(gc.getDatetime()));
			loc.setVelocidade(Double.parseDouble(gc.getSpeed()));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}	
	
	
	
	private static void readCommand() {
		
		byte[] message = {0x78,0x78,0x64,0x15,0x5C,0x00,0x01,(byte)0xA9,0x67,0x44,0x57,0x58,0x58,0x3D,0x4C,0x61,0x74,0x3A,0x4E,0x32,0x33,0x2E,0x31
		,0x31,0x31,0x36,0x38,0x32,0x2C,0x4C,0x6F,0x6E,0x3A,0x45,0x31,0x31,0x34,0x2E,0x34,0x30,0x39,0x32,0x31,0x37,0x2C,0x43,0x6F,0x75,0x72
		,0x73,0x65,0x3A,0x30,0x2E,0x30,0x30,0x2C,0x53,0x70,0x65,0x65,0x64,0x3A,0x30,0x2E,0x33,0x35,0x31,0x38,0x2C,0x44,0x61,0x74,0x65,0x54
		,0x69,0x6D,0x65,0x3A,0x31,0x31,0x2D,0x31,0x31,0x2D,0x31,0x35,0x20,0x20,0x31,0x31,0x3A,0x35,0x33,0x3A,0x34,0x33,0x00,0x02,0x00,0x23,0x07,(byte)0xAE,0x0D,0x0A};

		
		if(Crc16.isCrcOk(message)){
			
			
			
			
			//Utils.log(DEBUG_MODE,"...reading command transmmited from "+imei+"!");
			
			int contentSize = (int)message[4] - 4;
			int finalIndex = contentSize + 9;
			
			byte[] content = new byte[contentSize];
			
			for (int i = 9; i < finalIndex; i++) {
				content[i-9] = message[i];
			}
			
			if(DEBUG_MODE)
				System.out.print("->"+new String(content)+"<-");
			
			
			
			
			
		}
		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static byte[] buildCommand(byte[] commandContent, byte serial1, byte serial2) {
		
		List<Byte> commandList = new ArrayList<Byte>();
		
		
		int informationContentSize = 4 + commandContent.length;
		int packageSize = 6 + informationContentSize;
		
		commandList.add((byte)0x78);commandList.add((byte)0x78);   															//start
		commandList.add((byte)packageSize);               																	//package size 
		commandList.add((byte)0x80);                      								                                	//protocol
		commandList.add((byte)informationContentSize);               														//information content size
		commandList.add((byte)0x00);commandList.add((byte)0x01);commandList.add((byte)0xA9);commandList.add((byte)0x63);	//server flag bit 00 01 A9 67 
		
		for (int i = 0; i < commandContent.length; i++) {
			commandList.add((byte)commandContent[i]);                      										        	//content
		}
		
		commandList.add(serial1);commandList.add(serial2);         											             	//serial
		commandList.add((byte)0x78);commandList.add((byte)0x78);   															//crc
		commandList.add((byte)0x0D);commandList.add((byte)0x0A);    														//start
		
		Object[] commandArray = commandList.toArray();

		byte[] command = new byte[commandArray.length];
		
		for (int i = 0; i < command.length; i++) {
			command[i]= (Byte)commandArray[i];
		}
		
		command = Crc16.setCrc(command);
		printBuff(command);
		return null;
	}	
	
	private static void printBuff(byte[] buff){
		for (int i = 0; i < buff.length; i++) {
			System.out.printf("%02x ",buff[i]);
		}
		if(DEBUG_MODE)
			System.out.println();
	}	

}
