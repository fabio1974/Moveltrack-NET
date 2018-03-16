package net.moveltrack.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import net.moveltrack.domain.Location;
import net.moveltrack.gateway.utils.Utils;


public class ReadingProtocolHandlerH02 {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	boolean DEBUG_MODE = false;
	
	public ReadingProtocolHandlerH02(Socket clientSocket, InputStream is, OutputStream out) throws IOException {
		
		 PrintWriter pw = new PrintWriter(out, true);
		 String completeMessage="";
	     int readByte = 0; 
	     Utils.log(DEBUG_MODE," - START READING PROTOCOL H02");
	     
    	 //*HQ,354188055529801,V1,032502,A,344.5673,S,3831.3971,W,0.00,0.00,170213,ffffffff,0002d4,000002,00a20d,002c26#";
    	 //*HQ,354188055529801,XT,V,000000,000000#"
	     
	     while ((readByte=is.read()) != -1){
	    	 //System.out.printf("%02x ",readByte);
	    	 char readChar = (char) readByte;
	    	 completeMessage += readChar;
	    	 
	    	 
	    	 
	    	 if (readChar=='#'){
	    		if(DEBUG_MODE)
	    				System.out.println(sdf.format(new Date())+" - "+completeMessage);
		        if(completeMessage.contains("XT")){
		        	//pw.println("LOAD");
          		    //System.out.println(sdf.format(new Date())+" - ENVIOU LOAD!");
		        }else {
		        	insertPoinOnDatabase(completeMessage);
		        }
	    		completeMessage = "";
	    	 }
	     } 	
	}

	private void insertPoinOnDatabase(String completeMessage) {
	
		GpsCommand gc = new GpsCommand();
		String[] quebra =  completeMessage.split(",");
		
		try{
			gc.setImei(quebra[1]);
			gc.setType(quebra[2]);
			gc.setDatetime(quebra[11]); //170213
			//gc.setX1(quebra[3]);
			//gc.setF(quebra[4]);
			gc.setTime(quebra[3]); //032502
			//gc.setA(quebra[6]);
			gc.setLat(quebra[5]);
			gc.setNs(quebra[6]);
			gc.setLon(quebra[7]);
			gc.setEw(quebra[8]);
			gc.setSpeed(quebra[9]);
			//gc.setX2(quebra[12]);
			
			//EntityManager em = EntityProviderUtil.get().getEntityManager(Location.class);
			Location loc = new Location();
			loc.setImei(gc.getImei());
			
			String dt = gc.getDatetime();
			String t = gc.getTime();
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2000+Integer.parseInt(dt.substring(4,6)),
					Integer.parseInt(dt.substring(2,4))-1,
					Integer.parseInt(dt.substring(0,2)),
					Integer.parseInt(t.substring(0,2)),
					Integer.parseInt(t.substring(2,4)),
					Integer.parseInt(t.substring(4,6)));
			
			calendar.add(GregorianCalendar.HOUR_OF_DAY,-3);
			
			String lat = gc.getLat();
			String lon = gc.getLon();
			float latitude = Float.parseFloat(lat.substring(0,1)) + Float.parseFloat(lat.substring(1))/60;  //344.5673
			float longitude = Float.parseFloat(lon.substring(0,2)) + Float.parseFloat(lon.substring(2))/60;  //3831.3971
			
			loc.setDateLocation(calendar.getTime());
			loc.setLatitude(gc.getNs().equals("N")?latitude:(-1)*latitude);
			loc.setLongitude(gc.getEw().equals("E")?longitude:(-1)*longitude);
			loc.setVelocidade(Float.parseFloat(gc.getSpeed())/1.852);
			loc.setComando(gc.getType());
			//if(loc.getLatitude()!=0)
				//LocationDAO.getInstance().insert(loc);
		}catch(Exception e){
			e.printStackTrace();
			
		}

	
		
	}	

	

}
