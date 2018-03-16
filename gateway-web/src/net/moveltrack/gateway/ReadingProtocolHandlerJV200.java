package net.moveltrack.gateway;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import net.moveltrack.dao.SmsDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;
import net.moveltrack.domain.SmsTipo;
import net.moveltrack.gateway.beans.threadlocal.Imei;
import net.moveltrack.gateway.beans.threadlocal.Serial1;
import net.moveltrack.gateway.beans.threadlocal.Serial2;
import net.moveltrack.gateway.beans.threadlocal.StatusLoc;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Crc16;
import net.moveltrack.gateway.utils.GerenciaAlarmes;
import net.moveltrack.gateway.utils.HashMaps;
import net.moveltrack.gateway.utils.Utils;

public class ReadingProtocolHandlerJV200 extends ReadingProtocolHandler {

	boolean DEBUG_MODE = Constantes.DEBUG_JV200;
	
	public ReadingProtocolHandlerJV200()  {
	}
	
	
	@Override
	public void startReading(Socket socket) throws IOException {


		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int previous = 0;
		int current = 0;
		byte[] array;

		Utils.log(DEBUG_MODE," - START READING PROTOCOL JV200");
		InputStream in = socket.getInputStream();
		while ((current=in.read()) != -1){
			buffer.write(current);
			if (current==0x0A && previous==0x0D && buffer.size()>3){
				int len;
				array = buffer.toByteArray();
				if(array[0]==0x79)
					len = (int)((array[2] & 0xff) << 8) | (array[3] & 0xff) + 6; //Por causa do comando retornado pelo JV200 que tem 2 bytes para o Packet Length ao inv�s de 1
				else	
					len = (int)(array[2] & 0xFF) + 5;
				if(buffer.size()==len || buffer.size() > 255){
					transmissionAnalisys(array,len,socket);
					buffer = new ByteArrayOutputStream();
				}else 
					previous = current;
			}else{
				previous = current;
			}
		} 	
		Utils.log(DEBUG_MODE," - STOP READING PROTOCOL JV200");
	} 	
	

	private void transmissionAnalisys(byte[] byteArray,int len, Socket socket) {
		
		Utils.log(DEBUG_MODE,"...receiving package with size = "+len+" of type");
		
		switch (byteArray[0]==0x79?byteArray[4]:byteArray[3]) {
		case 0x01:
			Utils.logSemData(DEBUG_MODE," LOGIN");
			sendPassword(byteArray,socket);
			break;
			
		case 0x22:
			Utils.logSemData(DEBUG_MODE," LOCATION from "+Imei.getImei()+"->");
			Location loc = getLocationFromLocationPackage(byteArray);
			saveLocation(loc,ModeloRastreador.JV200);
			break;
			
		case 0x13:
			Utils.logSemData(DEBUG_MODE," HEARTBEAT from "+Imei.getImei()+"->");
			heartBeatResponse(byteArray, socket);
			break;
			
		case 0x21:
			Utils.logSemData(DEBUG_MODE," COMMAND RESPONSE from "+Imei.getImei()+" - "+ socket.getInetAddress());
			printBuff(DEBUG_MODE,byteArray);
			readCommand(byteArray);
			break;
			
		case 0x26:
			Utils.logSemData(DEBUG_MODE," ALARM from "+Imei.getImei()+"->");
			Location alarmLocation = getLocationFromAlarmPackage(byteArray);
			saveLocation(alarmLocation,ModeloRastreador.JV200);
			enviaEmailDoAlarme(alarmLocation);			
			printBuff(true,byteArray);
			break;

		default:
			Utils.logSemData(DEBUG_MODE," UNKNOW from "+Imei.getImei()+"->");
			printBuff(DEBUG_MODE,byteArray);
			break;
		}
		
		Serial1.setSerial1(byteArray[byteArray.length-6]); 
		Serial2.setSerial2(byteArray[byteArray.length-5]);
		

		if(Imei.getImei()!=null) {
				HashMaps.getInstance().setSeriais(Imei.getImei(),Serial1.getSerial1(),Serial2.getSerial2());
				Socket previousSocket = HashMaps.getInstance().getSocketByImei(Imei.getImei());
				if(previousSocket==null || !previousSocket.isConnected() || previousSocket.isClosed()){
					HashMaps.getInstance().setSocketInImei(Imei.getImei(),socket);
				}
		}
		//HashMaps.getInstance().printStatus(DEBUG_MODE);
		
	}
	

	
	public void enviaEmailDoAlarme(final Location loc) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				GerenciaAlarmes.gerenciaAlarme(loc);
			}
		}).start();
	}


	private void sendPassword(byte[] login,Socket socket) {
		if(Crc16.isCrcOk(login)){
			setImei(login);
			byte[] password = new byte[]{
					0x78,0x78,               //start
					0x05,                    //length 
					0x01,                    //protocol
					login[16],login[17],	 //serial
					0x00,0x00,               //crc
					0x0D,0x0A                //stop
			};
			password = Crc16.setCrc(password);
			Utils.log(DEBUG_MODE,"...sending password to imei: "+Imei.getImei()+"->");
			printBuff(DEBUG_MODE,password);
			sendToTerminal(password,socket);
		}else{
			Utils.log(DEBUG_MODE,"...discarding login package from "+Imei.getImei()+": CRC error!");
		}
	}	
	
	
	private void heartBeatResponse(byte[] heartBeat,Socket socket) {
			if(Crc16.isCrcOk(heartBeat)){
				byte[] heartBeatResponse = new byte[]{
						0x78,0x78,               	//start
						0x05,                    	//length 
						0x13,                    	//protocol
						heartBeat[9],heartBeat[10],	//serial
						0x00,0x00,               	//crc
						0x0D,0x0A                	//stop
				};
				heartBeatResponse = Crc16.setCrc(heartBeatResponse);
				Utils.log(DEBUG_MODE,"...sending heartbeat response to "+Imei.getImei()+"->");
				printBuff(DEBUG_MODE,heartBeatResponse);
				sendToTerminal(heartBeatResponse,socket);
				StatusLoc.setStatusLoc(getHeartBeatStatus(heartBeat));
				
				byte[] commandLocation = CommandHandlerJV200.buildJV200Command(CommandHandlerJV200.COMMAND_LOCATION,Serial1.getSerial1(),Serial2.getSerial2());
				
				if(Crc16.isCrcOk(commandLocation)){
					Utils.log(DEBUG_MODE,"...SENDING COMMAND_LOCATION CRC OK. Sending to "+Imei.getImei()+"->");
					printBuff(DEBUG_MODE,commandLocation);
					sendToTerminal(commandLocation,socket);	
				}else
					Utils.log(DEBUG_MODE,"...COMMAND_LOCATION BAD CRC to "+Imei.getImei()+"->");
			}else{
				Utils.log(DEBUG_MODE,"...discarding heartBeat package from "+Imei.getImei()+": CRC error!");
			}
	}
	

	public Location getLocationFromAlarmPackage(byte[] alarm) {
		Location loc = new Location();
		try {
			
			
			//Dados de localiza��o do Alarme----------------------------------------------------------------------------------------
			//--------------------------------------------------------------------------------------------------------
			
			loc.setImei(Imei.getImei());
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2000+(int)alarm[4],(int)alarm[5]-1,(int)alarm[6],
					          (int)alarm[7],(int)alarm[8],  (int)alarm[9]);
		
			Date dateLoc = calendar.getTime();
			
			loc.setDateLocation(dateLoc);
			loc.setDateLocationInicio(dateLoc);
			loc.setSatelites((int)(alarm[10] & 0x0F));
			
			BitSet courseStatus = fromByte(alarm[20]);
			boolean isNorth = courseStatus.get(2);
			boolean isWest  = courseStatus.get(3);

			byte[] latitude  = {alarm[11],alarm[12],alarm[13],alarm[14]};
			byte[] longitude = {alarm[15],alarm[16],alarm[17],alarm[18]};
			BigInteger latitudeBI = new BigInteger(latitude);
			BigInteger longitudeBI = new BigInteger(longitude);
			double latitudeF = (isNorth?1:-1)*latitudeBI.doubleValue()/1800000;
			double longitudeF = (isWest?-1:1)*longitudeBI.doubleValue()/1800000;
			loc.setLatitude(latitudeF);
			loc.setLongitude(longitudeF);
			loc.setVelocidade((int)alarm[19]);
			
			if(loc.getVelocidade()<=3)
				loc.setVelocidade(0);
			
			byte[] mcc = {alarm[23],alarm[24]};
			BigInteger mccBI = new BigInteger(mcc);
			loc.setMcc(mccBI.intValue());			
			

			//Dados de Status do Alarme----------------------------------------------------------------------------------------
			//--------------------------------------------------------------------------------------------------------
			BitSet terminalStatus = fromByte(alarm[31]); //terminal Information Content
			//Utils.log(DEBUG_MODE,"...READING  HEARTBEAT STATUS FROM "+Imei.getImei());
			loc.setAlarm(terminalStatus.get(0)?"1":"0");
			loc.setIgnition(terminalStatus.get(1)?"1":"0");
			loc.setBattery(terminalStatus.get(2)?"1":"0");
			
			boolean bit3 = terminalStatus.get(3);
			boolean bit4 = terminalStatus.get(4);
			boolean bit5 = terminalStatus.get(5);
			
			if(bit5 && !bit4 && !bit3) //100
				loc.setAlarmType("100");//SOS");
			if(!bit5 && bit4 && bit3) //011
				loc.setAlarmType("011");//Low Battery");
			if(!bit5 && bit4 && !bit3) //010
				loc.setAlarmType("010");//Power Cut");
			if(!bit5 && !bit4 && bit3) //001
				loc.setAlarmType("001");//Vibration Alarm");
			if(!bit5 && !bit4 && !bit3) //000
				loc.setAlarmType("000");//Normal");
			
			loc.setGps(terminalStatus.get(6)?"On":"Off");
			loc.setBloqueio(terminalStatus.get(7)?"Sim":"Nao");
			
			byte voltageLevel = alarm[32];
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
			
			byte gsm = alarm[33];
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
			
			byte alarmTypeFormer = alarm[34];
			
			if(alarmTypeFormer== 0x04)
				loc.setAlarmType("004");//Fence In
			else if(alarmTypeFormer== 0x05)
				loc.setAlarmType("005");//Fence Out
			else if(alarmTypeFormer== 0x06)
				loc.setAlarmType("006"); //Over-speed
			else if(alarmTypeFormer== 0x09)
				loc.setAlarmType("009"); //Moving-alarm
					
					
				
				
			//Fim Status do Alarme----------------------------------------------------------------------------------------
			//------------------------------------------------------------------------------------------------------------
				

			Utils.log(DEBUG_MODE,"...ALARM READING END");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return loc;
	}
	
	
	
	
	private Location getHeartBeatStatus(byte[] heartBeat) {
		Location loc = new Location();
		try{
			BitSet terminalStatus = fromByte(heartBeat[4]); //terminal Information Content
			Utils.log(DEBUG_MODE,"...READING  HEARTBEAT STATUS FROM "+Imei.getImei());
			loc.setAlarm(terminalStatus.get(0)?"1":"0");
			loc.setIgnition(terminalStatus.get(1)?"1":"0");
			loc.setBattery(terminalStatus.get(2)?"1":"0");
			
			boolean bit3 = terminalStatus.get(3);
			boolean bit4 = terminalStatus.get(4);
			boolean bit5 = terminalStatus.get(5);
			
			if(bit5 && !bit4 && !bit3) //100
				loc.setAlarmType("100");//SOS");
			if(!bit5 && bit4 && bit3) //011
				loc.setAlarmType("011");//Low Battery");
			if(!bit5 && bit4 && !bit3) //010
				loc.setAlarmType("010");//Power Cut");
			if(!bit5 && !bit4 && bit3) //001
				loc.setAlarmType("001");//Shock Alarm");
			if(!bit5 && !bit4 && !bit3) //000
				loc.setAlarmType("000");//Normal");
			
			loc.setGps(terminalStatus.get(6)?"On":"Off");
			loc.setBloqueio(terminalStatus.get(7)?"Sim":"Nao");
			
			byte voltageLevel = heartBeat[5];
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
			
			byte gsm = heartBeat[6];
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


	private void readCommand(byte[] message) {
		/*		byte[] message = {0x79, 0x79  , 0x00 , 0x72 , 0x21 , 0x00 , 0x01 ,(byte)0xa9 , 0x63 , 0x01 , 0x43 , 0x75 , 0x72 , 0x72 , 0x65 ,       0x6e , 0x74 ,       0x20 , 0x70 , 0x6f 
		        , 0x73 , 0x69 , 0x74 , 0x69 , 0x6f , 0x6e , 0x21 ,      0x20 , 0x4c , 0x61 , 0x74 , 0x3a , 0x53 , 0x33 , 0x2e ,       0x37 , 0x34 ,       0x32 , 0x38 , 0x37 
		        , 0x38 , 0x2c , 0x4c , 0x6f , 0x6e , 0x3a , 0x57 ,      0x33 , 0x38 , 0x2e , 0x35 , 0x32 , 0x33 , 0x31 , 0x38 ,       0x36 , 0x2c ,       0x43 , 0x6f , 0x75 
		        , 0x72 , 0x73 , 0x65 , 0x3a , 0x32 , 0x38 , 0x30 ,      0x2e , 0x34 , 0x37 , 0x2c , 0x53 , 0x70 , 0x65 , 0x65 ,       0x64 , 0x3a ,       0x30 , 0x2e , 0x30 
		        , 0x30 , 0x4b , 0x6d , 0x2f , 0x68 , 0x2c , 0x44 ,      0x61 , 0x74 , 0x65 , 0x54 , 0x69 , 0x6d , 0x65 , 0x3a ,       0x32 , 0x30 ,       0x31 , 0x35 , 0x2d 
		        , 0x30 , 0x36 , 0x2d , 0x31 , 0x39 , 0x20 , 0x30 ,      0x32 , 0x3a , 0x34 , 0x30 , 0x3a , 0x33 , 0x39 , 0x00 , (byte)0x88 , 0x75 , (byte)0xb3 , 0x0d , 0x0a };
		 */

		if(Crc16.isCrcOk(message)){
			int len = (int)((message[2] & 0xff) << 8) | (message[3] & 0xff) + 6;
			int contentSize = len - 16;
			int finalIndex = contentSize + 10;
			byte[] content = new byte[contentSize];
			for (int i = 10; i < finalIndex; i++) {
				content[i-10] = message[i];
			}
			String receivedCommand = new String(content); 
			receivedCommandAnalisys(receivedCommand);
		}

	}
	
	private void receivedCommandAnalisys(String receivedCommand) {
		Utils.log(DEBUG_MODE,"Conte�do do comando: "+receivedCommand);
		if(receivedCommand.contains("Lat:")){
			Location loc = getLocationFromReceivedCommand(receivedCommand);
			saveLocation(loc,ModeloRastreador.JV200);
		}else {
			Sms sms = new Sms();
			sms.setCelular(Utils.getCelularOfImei(Imei.getImei()));
			sms.setDataUltimaAtualizacao(new Date());
			sms.setImei(Imei.getImei());
			sms.setMensagem(receivedCommand);
			sms.setStatus(SmsStatus.RECEBIDO);
			if(receivedCommand.contains("DYD=Success"))
				sms.setTipo(SmsTipo.BLOQUEIO);
			else if(receivedCommand.contains("HFYD=Success"))
				sms.setTipo(SmsTipo.DESBLOQUEIO);
			else if(receivedCommand.contains("Already in the state of fuel supply to resume") || receivedCommand.contains("Restore fuel supply: Success!"))
				sms.setTipo(SmsTipo.DESBLOQUEIO);
			else if(receivedCommand.contains("Already in the state of fuel supply cut off") || receivedCommand.contains("Cut off the fuel supply: Success!"))
				sms.setTipo(SmsTipo.BLOQUEIO);
			else	
				sms.setTipo(SmsTipo.AVISO);
			
			System.out.println("Salvando SMS de BLOQUEIO ou DESBLOQUEIO");
			smsDao.salvar(sms);
			System.out.println("SMS de BLOQUEIO ou DESBLOQUEIO Salvo!");
			
			//SmsDAO.getInstance().insert(sms);
		}
	}
	
	@Inject SmsDao smsDao;
	
	private Location getLocationFromReceivedCommand(String receivedCommnad) {
		Location loc = new Location();
		try{

			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			GpsCommand gc = new GpsCommand();
			
			String[] quebra =  receivedCommnad.substring(receivedCommnad.indexOf("Lat:")).split(",");
			
			float latitude = Float.parseFloat(quebra[0].substring(5));
			float longitude = Float.parseFloat(quebra[1].substring(5));
			
			gc.setNs(quebra[0].substring(4,5));
			gc.setEw(quebra[1].substring(4,5));
			gc.setDatetime(quebra[4].substring("DateTime:".length()));
			gc.setSpeed(quebra[3].substring("Speed:".length(),quebra[3].indexOf("Km/h")));
			
			
			loc.setImei(Imei.getImei());
			loc.setLatitude(gc.getNs().equals("N")?latitude:(-1)*latitude);
			loc.setLongitude(gc.getEw().equals("E")?longitude:(-1)*longitude);
			loc.setDateLocationInicio(sdf.parse(gc.getDatetime()));
			loc.setDateLocation(sdf.parse(gc.getDatetime()));
			loc.setVelocidade(Double.parseDouble(gc.getSpeed()));
			
			if(loc.getVelocidade()<=3)
				loc.setVelocidade(0);
			
			if(StatusLoc.getStatusLoc()!=null){
				Utils.log(DEBUG_MODE,"...STORING STATUS from "+Imei.getImei()+"!");
				loc.setSatelites(lastLocation!=null?lastLocation.getSatelites():-1);
				loc.setMcc(lastLocation!=null?lastLocation.getMcc():-1);
				loc.setAlarm(StatusLoc.getStatusLoc().getAlarm());
				loc.setAlarmType(StatusLoc.getStatusLoc().getAlarmType());
				loc.setBattery(StatusLoc.getStatusLoc().getBattery());
				loc.setBloqueio(StatusLoc.getStatusLoc().getBloqueio());
				loc.setGps(StatusLoc.getStatusLoc().getGps());
				loc.setGsm(StatusLoc.getStatusLoc().getGsm());
				loc.setIgnition(StatusLoc.getStatusLoc().getIgnition());
				loc.setSos(StatusLoc.getStatusLoc().getSos());
				loc.setVolt(StatusLoc.getStatusLoc().getVolt());
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}

	private Location getLocationFromLocationPackage(byte[] location) {
		Location loc = new Location();
		try{
			if(Crc16.isCrcOk(location)){
				
				
				
				
				Utils.log(DEBUG_MODE,"...getting good JV200 location package from "+Imei.getImei()+" --> ");
				printBuff(DEBUG_MODE, location);
				loc.setImei(Imei.getImei());
				
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.set(2000+(int)location[4],(int)location[5]-1,(int)location[6],
						          (int)location[7],(int)location[8],  (int)location[9]);
				
				calendar.add(Calendar.HOUR,-3);
			
				Date dateLoc = calendar.getTime();
				
				loc.setDateLocation(dateLoc);
				loc.setDateLocationInicio(dateLoc);
				loc.setSatelites((int)(location[10] & 0x0F));
				
				BitSet courseStatus = fromByte(location[20]);
				boolean isNorth = courseStatus.get(2);
				boolean isWest  = courseStatus.get(3);
	
				byte[] latitude  = {location[11],location[12],location[13],location[14]};
				byte[] longitude = {location[15],location[16],location[17],location[18]};
				BigInteger latitudeBI = new BigInteger(latitude);
				BigInteger longitudeBI = new BigInteger(longitude);
				double latitudeF = (isNorth?1:-1)*latitudeBI.doubleValue()/1800000;
				double longitudeF = (isWest?-1:1)*longitudeBI.doubleValue()/1800000;
				loc.setLatitude(latitudeF);
				loc.setLongitude(longitudeF);
				loc.setVelocidade((int)location[19]);
				
				if(loc.getVelocidade()<=3)
					loc.setVelocidade(0);
				
				byte[] mcc = {location[22],location[23]};
				BigInteger mccBI = new BigInteger(mcc);
				loc.setMcc(mccBI.intValue());
				
				int acc = (int)location[30];
				loc.setIgnition(acc==0?"0":"1");
				
				
				
			}else{
				Utils.log(DEBUG_MODE,"...discarding location package from "+Imei.getImei()+": CRC error!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}
	
	private void setImei(byte[] login) {
		Imei.setImei(String.format("%1x%02x%02x%02x%02x%02x%02x%02x", login[4], login[5], login[6], login[7], login[8], login[9], login[10], login[11]));
	}

	
	public static void main(String[] args) {
		ReadingProtocolHandlerJV200 p = new ReadingProtocolHandlerJV200();
		byte[] message = {0x79, 0x79  , 0x00 , 0x72 , 0x21 , 0x00 , 0x01 ,(byte)0xa9 , 0x63 , 0x01 , 0x43 , 0x75 , 0x72 , 0x72 , 0x65 ,       0x6e , 0x74 ,       0x20 , 0x70 , 0x6f 
				        , 0x73 , 0x69 , 0x74 , 0x69 , 0x6f , 0x6e , 0x21 ,      0x20 , 0x4c , 0x61 , 0x74 , 0x3a , 0x53 , 0x33 , 0x2e ,       0x37 , 0x34 ,       0x32 , 0x38 , 0x37 
				        , 0x38 , 0x2c , 0x4c , 0x6f , 0x6e , 0x3a , 0x57 ,      0x33 , 0x38 , 0x2e , 0x35 , 0x32 , 0x33 , 0x31 , 0x38 ,       0x36 , 0x2c ,       0x43 , 0x6f , 0x75 
				        , 0x72 , 0x73 , 0x65 , 0x3a , 0x32 , 0x38 , 0x30 ,      0x2e , 0x34 , 0x37 , 0x2c , 0x53 , 0x70 , 0x65 , 0x65 ,       0x64 , 0x3a ,       0x30 , 0x2e , 0x30 
				        , 0x30 , 0x4b , 0x6d , 0x2f , 0x68 , 0x2c , 0x44 ,      0x61 , 0x74 , 0x65 , 0x54 , 0x69 , 0x6d , 0x65 , 0x3a ,       0x32 , 0x30 ,       0x31 , 0x35 , 0x2d 
				        , 0x30 , 0x36 , 0x2d , 0x31 , 0x39 , 0x20 , 0x30 ,      0x32 , 0x3a , 0x34 , 0x30 , 0x3a , 0x33 , 0x39 , 0x00 , (byte)0x88 , 0x75 , (byte)0xb3 , 0x0d , 0x0a };
		
		
		if(Crc16.isCrcOk(message)){
			int len = (int)((message[2] & 0xff) << 8) | (message[3] & 0xff) + 6;
			int contentSize = len - 16;
			int finalIndex = contentSize + 10;
			byte[] content = new byte[contentSize];
			for (int i = 10; i < finalIndex; i++) {
				content[i-10] = message[i];
			}
			String receivedCommand = new String(content); 
			p.receivedCommandAnalisys(receivedCommand);
		}
	}	

	
}
