package net.moveltrack.gateway;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.BitSet;
import java.util.Date;
import java.util.GregorianCalendar;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Utils;

public class ReadingProtocolHandlerTR02 extends ReadingProtocolHandler{

	boolean DEBUG_MODE = Constantes.DEBUG_TR02; 
	
	public ReadingProtocolHandlerTR02() {

	}
	
	Location status=null;
	
	@Override
	public void startReading(Socket socket) throws IOException {
		 Utils.log(DEBUG_MODE," - START READING PROTOCOL TR02");
		 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		 String imei=null;
	     int previous = 0;
	     int current = 0;
	     
	     byte[] array;
	     InputStream inputStream = socket.getInputStream();
	     while ((current=inputStream.read()) != -1){
	    	 buffer.write(current);
	    	 if (current==0x0A && previous==0x0D && buffer.size()>3){
	    		 array = buffer.toByteArray(); 
				 int len = (int)(array[2] & 0xFF) + 5;
				 //Utils.log(DEBUG_MODE,"len="+len + " bufferSize="+buffer.size());
				 if(buffer.size()==len || buffer.size() > 255){
				 	  transmissionAnalisys(array,len,socket,imei);
				 	  buffer = new ByteArrayOutputStream();
				 }else 
				 	  previous = current;
	    	 }else{
      	 		 previous = current;
   	 	 }
   	 } 	
	}  	

	

	private void transmissionAnalisys(byte[] byteArray,int len,Socket socket,String imei) {
		
		Utils.log(DEBUG_MODE,"...receiving package with size = "+len+" of type");
		
		switch (byteArray[15]) {
		case 0x00:
			Utils.logSemData(DEBUG_MODE," IP Request->");
			printBuff(DEBUG_MODE,byteArray);
			break;
			
		case 0x10:
			if(imei==null)
				imei = getImei(byteArray);
			Utils.logSemData(DEBUG_MODE," location from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			Location loc = getLocationFromLocationPackage(byteArray,imei);
			saveLocation(loc,ModeloRastreador.TR02);
			break;
			
		case 0x1A:
			if(imei==null)
				imei = getImei(byteArray);
			Utils.logSemData(DEBUG_MODE," heartbeat from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			heartBeatResponse(byteArray,socket,imei);
			status = getHeartBeatStatus(byteArray,len,imei);
			break;
			
		case 0x17:
			Utils.logSemData(DEBUG_MODE," address respond chinese from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;
			
		case (byte)0x97:
			Utils.logSemData(DEBUG_MODE," address respond english from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;
			
		case 0x1B:
			Utils.logSemData(DEBUG_MODE," address request from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;
			
		case 0x1C:
			Utils.logSemData(DEBUG_MODE," respond of issued instruction from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;

		default:
			Utils.logSemData(DEBUG_MODE," unknow from "+imei+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;
		}
	}
	//
	
	private void heartBeatResponse(byte[] heartBeat, Socket socket,String imei) {

		byte[] heartBeatResponse = new byte[]{
				0x54,0x68,               	//start
				0x1A,                    	//protocol
				0x0D,0x0A                	//stop
		};

		Utils.log(DEBUG_MODE,"...sending heartbeat response to "+imei+"->");
		printBuff(DEBUG_MODE,heartBeatResponse);
		sendToTerminal(heartBeatResponse, socket);

	}

	
	private Location getHeartBeatStatus(byte[] heartBeat,int len, String imei) {
		Location loc = new Location();
		try{
			
			Utils.log(DEBUG_MODE,"...READING  HEARTBEAT STATUS FROM "+imei);
			
			loc.setSatelites(len-20);
			
			byte gps = heartBeat[16];
			
			switch (gps) {
			case 0x00:
				loc.setGps("Off");
				break;
			case 0x01:
				loc.setGps("On");
				break;
			case 0x02:
				loc.setGps("Dif");
				break;
			}

			byte voltageLevel = heartBeat[3];
			
			if(voltageLevel== 0x00)
				loc.setVolt("0%");// - No Power");
			else if(voltageLevel== 0x01)
				loc.setVolt("<5%");// - Extremely Low Battery");
			else if(voltageLevel== 0x02)
				loc.setVolt("<10%");// - Very Low Battery");
			else if(voltageLevel== 0x03)
				loc.setVolt("<30%");// - Low Battery");
			else if(voltageLevel== 0x04)
				loc.setVolt("~50%");// - Medium");
			else if(voltageLevel== 0x05)
				loc.setVolt(">70%");// - High");
			else if(voltageLevel== 0x06)
				loc.setVolt(">90%");// - Very high");
			
			byte gsm = heartBeat[4];
			
			if(gsm== 0x00)
				loc.setGsm("0");//Sem sinal");// - No signal");
			else if(gsm== 0x01)
				loc.setGsm("1");//Muito fraco");// - Extremely weak signal");
			else if(gsm== 0x02)
				loc.setGsm("2");//Fraco");// - Very weak signal");
			else if(gsm== 0x03)
				loc.setGsm("3");//Bom");// - Good signal");
			else if(gsm== 0x04)
				loc.setGsm("4");//Muito Bom");// - Strong signal");
			Utils.log(DEBUG_MODE,"...HEARTBEAT STATUS END");
		
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return loc;
	}


	private Location getLocationFromLocationPackage(byte[] location, String imei) {
		Location loc = new Location();
		try{
				Utils.log(DEBUG_MODE,"...storing good location package from "+imei+"!");
				loc.setImei(imei);
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.set(2000+(int)location[16],(int)location[17]-1,(int)location[18],
						          (int)location[19],(int)location[20],  (int)location[21]);
			
				Date dateLoc = calendar.getTime();
				
				loc.setDateLocation(dateLoc);
				loc.setDateLocationInicio(dateLoc);
				
				BitSet courseStatus = fromByte(location[39]);
				boolean isNorth = courseStatus.get(1);
				boolean isWest  = !courseStatus.get(2);
				
				byte[] latitude  = {location[22],location[23],location[24],location[25]};
				byte[] longitude = {location[26],location[27],location[28],location[29]};
				BigInteger latitudeBI = new BigInteger(latitude);
				BigInteger longitudeBI = new BigInteger(longitude);
				double latitudeF = (isNorth?1:-1)*latitudeBI.doubleValue()/1800000;
				double longitudeF = (isWest?-1:1)*longitudeBI.doubleValue()/1800000;
				loc.setLatitude(latitudeF);
				loc.setLongitude(longitudeF);
				loc.setVelocidade((int)location[30]);
				
				if(loc.getVelocidade()<=3)
					loc.setVelocidade(0);
				
				if(status!=null){
					Utils.log(DEBUG_MODE,"...STORING STATUS from "+imei+"!");
					loc.setSatelites(status.getSatelites());
					//loc.setMcc(lastLocation!=null?lastLocation.getMcc():-1);
					//loc.setAlarm(status.getAlarm());
					//loc.setAlarmType(status.getAlarmType());
					//loc.setBattery(status.getBattery());
					//loc.setBloqueio(status.getBloqueio());
					loc.setGps(status.getGps());
					loc.setGsm(status.getGsm());
					//loc.setIgnition(status.getIgnition());
					//loc.setSos(status.getSos());
					loc.setVolt(status.getVolt());
				}

		}catch(Exception e){
			e.printStackTrace();
		}
	
		return loc;
	}
	

	private String getImei(byte[] pac) {
		return String.format("%1x%02x%02x%02x%02x%02x%02x%02x", pac[5], pac[6], pac[7], pac[8], pac[9], pac[10], pac[11], pac[12]);
	}
}
