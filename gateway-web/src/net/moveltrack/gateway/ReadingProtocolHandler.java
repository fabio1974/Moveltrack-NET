package net.moveltrack.gateway;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.gateway.persistence.EquipamentoDAO;
import net.moveltrack.gateway.persistence.LocationDAO;

public abstract class ReadingProtocolHandler implements Serializable{
	
	boolean DEBUG_SERVICE = false;

	public ReadingProtocolHandler() {
		
	}

	public abstract void startReading(Socket clientSocket) throws IOException ;

	
	public void sendToTerminal(byte[] response,Socket socket) {
		try {
			if(socket!=null && socket.isConnected() && !socket.isClosed()) {
				OutputStream out = socket.getOutputStream();
				out.write(response);
			}
		} catch (IOException e) {
			if(DEBUG_SERVICE)
				e.printStackTrace();
		}
	}
	
	Location lastLocation;
	private void saveLocation(Location loc){
		
		if(lastLocation==null || 
				lastLocation.getLatitude()!=loc.getLatitude() || 
				lastLocation.getLongitude()!=loc.getLongitude() || 
				lastLocation.getDateLocation().getTime()!=loc.getDateLocation().getTime()){

			if(loc.getLatitude()!=0 && loc.getVelocidade()<200 && loc.getLatitude()> -90){
				LocationDAO.getInstance().insert(loc,"Location");
				LocationDAO.getInstance().insert(loc,"Location2");
				lastLocation = loc;
			}
		}
	}
	
	static SimpleDateFormat sdf  =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/** rotina de correção de horas adiantadas a hora atual 
	 * e que nao puderam ser setadas no aparelho, porque o comando GMT não setou para o GPRS. Isso acontece com o JV200 e com alguns TK06
	 * @param loc
	 * @param modelo
	 */
	public void saveLocation(Location loc, ModeloRastreador modelo) {
		
		Date now = new Date();
		int minDiff = 0;
		
		if(modelo==ModeloRastreador.TK06){

			minDiff = Math.round((loc.getDateLocation().getTime() - now.getTime())/60000);
			int atrasoGmt = EquipamentoDAO.getInstance().getAtraso(loc.getImei());
			
			if(minDiff>=1){ //TK06 defeituosos
				
				if(loc.getGps().equals("Off")){ //Para os TK06 defeituosos, notou-se que quando o gps desliga, o horario fica maluco!  Optamos por descartar o ponto!

					loc.setDateLocation(now);
					loc.setDateLocationInicio(now);
					
/*					System.out.println("======================================================================================================================");
					System.out.println(sdf.format(now)+" - DESCARTANDO: "+sdf.format(loc.getDateLocation()) + " - minDiff="+minDiff+ " Imei:"+loc.getImei() + " Gps="+loc.getGps());
					System.out.println("======================================================================================================================");
*/					
					
				}else if(minDiff%60==0 || minDiff%60==1 || minDiff%60==59){ //Defeituoso, com GPS On, calcula-se o atraso para correção posterior
				
/*					System.out.println("======================================================================================================================");
					System.out.println(sdf.format(now)+" - DATA ANTIGA: "+sdf.format(loc.getDateLocation()) + " - minDiff="+minDiff+ " Imei:"+loc.getImei());
					System.out.println("atrasoGmt do banco= "+atrasoGmt);
					System.out.println("gps="+loc.getGps());
*/					
					int atrasoHoras = (int)Math.round((double)minDiff/60);
//					System.out.println("atrasoGmt Horas calculado= "+atrasoHoras);
					
					Date corrigida = corrige(loc,atrasoHoras);
					loc.setDateLocation(corrigida);
					loc.setDateLocationInicio(corrigida);
					
					if(atrasoGmt<atrasoHoras)
						EquipamentoDAO.getInstance().salvaAtraso(loc.getImei(),atrasoHoras);
					
/*					System.out.println(sdf.format(now)+" - DATA CORRIGIDA: "+sdf.format(loc.getDateLocation()) + " - minDiff="+minDiff+ " Imei:"+loc.getImei());
					System.out.println("=======================================================================================================================");
*/				}else{

/*					System.out.println("======================================================================================================================");
					System.out.println(sdf.format(now)+" - ELSE DO ELSE: "+sdf.format(loc.getDateLocation()) + " - minDiff="+minDiff+ " Imei:"+loc.getImei());
					System.out.println("======================================================================================================================");
*/				}

			}else{  //TKS  Bons  ou armazenados 
				Date corrigida = corrige(loc,atrasoGmt);
				loc.setDateLocation(corrigida);
				loc.setDateLocationInicio(corrigida);
			}
		}
		

		if(loc.getDateLocation().getTime() - now.getTime() < 300000)
			saveLocation(loc);
		else{
/*			System.out.println("###########################");
			System.out.println(loc.getImei()+ " enviando pontos no futuro - "+ sdf.format(loc.getDateLocation()) + " - minDiff="+minDiff +" resto="+(minDiff%60));
			System.out.println("###########################");
*/		}	
		
	}
	
	

	
	
	private Date corrige(Location loc,int atrasoGmt) {
		Calendar c = Calendar.getInstance();
		c.setTime(loc.getDateLocation());
		c.add(Calendar.HOUR,-atrasoGmt);
		return c.getTime();
	}

	//@Inject EquipamentoDao equipamentoDao;
	
	
//		Date inicio = new Date();	
/*			loc.setId(lastLocation.getId());
			loc.setDateLocationInicio(lastLocation.getDateLocationInicio());
			lastLocation.setVersion(lastLocation.getVersion()+1);
			loc.setVersion(lastLocation.getVersion());
			if(!LocationDAO.getInstance().update(loc))
				lastLocation = null;
		}else{*/
//			if(loc.getLatitude()!=0){
				/*int id = LocationDAO.getInstance().insert(loc,"location");
				LocationDAO.getInstance().insert(loc,"location2");
				//loc.setId(id);
				lastLocation = loc;*/
		//}
//		Date fim = new Date();
//		long diff = fim.getTime()-inicio.getTime();
		//if(diff>10)
			//Utils.log(true,"save tipo "+tipo+" em "+diff+ " miliseconds.");



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
				System.out.println(title+ " "+(socket.isConnected()?"connected":"not conected"));
				System.out.println(title+ " "+(socket.isClosed()?"closed":"not closed"));
				System.out.println(title+ " "+"Inet Address: "+socket.getInetAddress());
			}
		}
	}    
	
	
}