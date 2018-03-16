package net.moveltrack.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.LastLocation;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.SmsStatus;


public class Utils {
	
	
	public static boolean isNoAr(String urlStr){
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			return (code==200);
		} 
		catch (Exception ex) {
			System.out.println("Erro checando "+urlStr+":"+ex.getMessage());
		}
		return false;
	}
	
	
/*	public static String formatString(String string, String mask) {
		try {
			javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(string);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/


	public static String priceWithDecimal (Double price) {
	    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
	    return formatter.format(price);
	}
	
	

	public static String convertMillisecondsToTimeString(long millisecondsSinceEpoch){
		Instant instant = Instant.ofEpochMilli ( millisecondsSinceEpoch );
		ZonedDateTime zdt = ZonedDateTime.ofInstant ( instant , ZoneOffset.UTC );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "HH:mm:ss" );
		return formatter.format ( zdt );
	}


	
	
	public static int daysDiff(Date from, Date to) {
	    return (int) Math.round((from.getTime() - to.getTime()) / 86400000d);
	}

	
    public static boolean validEmail(String email) {
		    //System.out.println("Metodo de validacao de email");
		    Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$"); 
		    Matcher m = p.matcher(email); 
		    if (m.find()){
		      //System.out.println("O email "+email+" e valido");
		      return true;
		    }
		    else{
		      //System.out.println("O E-mail "+email+" é inválido");
		      return false;
		    }  
	}	
	

    
    /*
    public static Sms getUltimoSmsDoChip(String celular){
    	Sms sms = null;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE,-1);
	    EntityManager em = EntityProviderUtil.get().getEntityManager(Sms.class);
		Query query = em.createQuery("select max(s.id) from Sms s where s.celular=:celular and s.dataUltimaAtualizacao >:data");
	    query.setParameter("celular",celular);
	    query.setParameter("data",c.getTime());
	    Long id = (Long)query.getSingleResult();
	    if(id!=null){
	    	sms = Sms.findSms(id);
	    }	
    	return sms;
    }*/	
	    

	
	

	
	
	public static boolean isParada(Location loc){
		if(loc.getVelocidade()>0)
			return false;
		return loc.getDateLocation().getTime() - loc.getDateLocationInicio().getTime() > 3*60*1000;
	}
	
	



	
	
	
	
	public static String getTimeFromMillis(long millis) {
		String r = "";
		try{
			int second = (int)(millis / 1000) % 60;
			int minute = (int)(millis / (1000 * 60)) % 60;
			int hour= (int)(millis / (1000 * 60 * 60)) % 24;
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",second);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}

	public static String getTimeSinceDate(Date date) {
		String r = "";
		try{
			Date now = new Date();
			long millis = now.getTime() - date.getTime();
			int second = (int)(millis / 1000) % 60;
			int minute = (int)(millis / (1000 * 60)) % 60;
			int hour= (int)(millis / (1000 * 60 * 60)) % 24;
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",days)+":"+String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",second);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}
	
	
	public static String getTimeSinceDate(Date now,Date date) {
		String r = "";
		try{
			long millis = now.getTime() - date.getTime();
			int second = (int)(millis / 1000) % 60;
			int minute = (int)(millis / (1000 * 60)) % 60;
			int hour= (int)(millis / (1000 * 60 * 60)) % 24;
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",days)+":"+String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",second);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}
	
	
	public static long getTimeDiffInMillis(Date now,Date date) {
			return now.getTime() - date.getTime();
	}
	
	public static double getTimeDiffInSeconds(Date now,Date date) {
		return (now.getTime() - date.getTime())/1000;
	}
	
	public static double getTimeDiffInMinutes(Date now,Date date) {
		return (now.getTime() - date.getTime())/(1000*60);
	}
	
	public static double getTimeDiffInHours(Date now,Date date) {
		return (now.getTime() - date.getTime())/(3600000d);
	}
	
	/*public static double getTimeDiffInDoubleDays(Date now,Date date) {
		return (now.getTime() - date.getTime())/(1000*60*60*24);
	}*/	
	
	public static double getTimeDiffInDays(Date now,Date date) {
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(now);
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		
		return (double)(c2.getTimeInMillis() - c1.getTimeInMillis())/(1000*60*60*24);
	}
	
	
	public static long getCalendarDiffInDays(Calendar c2, Calendar c1) {
		return (long)(c2.getTimeInMillis() - c1.getTimeInMillis())/(1000*60*60*24);
	}
	
	
	
	
	

	
	
	public static String getTimeSinceDateWithoutSecond(Date date) {
		String r = "";
		try{
			Date now = new Date();
			long millis = now.getTime() - date.getTime();
			int second = (int)(millis / 1000) % 60;
			int minute = (int)(millis / (1000 * 60)) % 60;
			int hour= (int)(millis / (1000 * 60 * 60)) % 24;
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",days)+":"+String.format("%02d",hour)+":"+String.format("%02d",minute);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}
	
	
	public static String getTimeSinceDateWithoutSecond(Date now,Date date) {
		String r = "";
		try{
			long millis = now.getTime() - date.getTime();
			int second = (int)(millis / 1000) % 60;
			int minute = (int)(millis / (1000 * 60)) % 60;
			int hour= (int)(millis / (1000 * 60 * 60)) % 24;
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",days)+":"+String.format("%02d",hour)+":"+String.format("%02d",minute);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}
	
	
	
	public static String getDaysSinceDate(Date date) {
		String r = "";
		try{
			Date now = new Date();
			long millis = now.getTime() - date.getTime();
			int days = (int)(millis / (1000 * 60 * 60 * 24));
			r = String.format("%02d",days);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return r; 
	}
	
	
	public static int getDaysSinceDate(Date now,Date date) {
		//String r = "";
		int days=0;
		try{
			long millis = now.getTime() - date.getTime();
			days = (int)(millis / (1000 * 60 * 60 * 24));
			//r = String.format("%02d",days);
		}catch(Exception e){
			int k = 0;
			k++;
		}
		return days; 
	}
	
	
	public static Location getLocationFromObject(Object obj){
		if(obj instanceof Location)
			return (Location)obj;
		else
			return location2ToLocation(obj);
	}
	
	private static Location location2ToLocation(Object obj) {
		if(obj==null)
			return null;
		Location loc = new Location();
		loc.setIgnition(((Location2)obj).getIgnition());
		loc.setLatitude(((Location2)obj).getLatitude());
		loc.setLongitude(((Location2)obj).getLongitude());
		loc.setVelocidade(((Location2)obj).getVelocidade());
		loc.setDateLocationInicio(((Location2)obj).getDateLocationInicio());
		loc.setDateLocation(((Location2)obj).getDateLocation());
		loc.setAlarm(((Location2)obj).getAlarm());
		loc.setAlarmType(((Location2)obj).getAlarmType());
		loc.setBattery(((Location2)obj).getBattery());
		loc.setBloqueio(((Location2)obj).getBloqueio());
		loc.setComando(((Location2)obj).getComando());
		loc.setGps(((Location2)obj).getGps());
		loc.setGsm(((Location2)obj).getGsm());
		loc.setId(((Location2)obj).getId());
		loc.setImei(((Location2)obj).getImei());
		loc.setMcc(((Location2)obj).getMcc());
		loc.setSatelites(((Location2)obj).getSatelites());
		loc.setSos(((Location2)obj).getSos());
		//loc.setVersion(((Location2)obj).getVersion());
		loc.setVolt(((Location2)obj).getVolt());
		return loc;
	}

	

    public static String getWhere(String... conditions){
	    String where = " ";
	    for (int i = 0; i < conditions.length; i++) {
			if(!conditions[i].equals(""))
				where += conditions[i] + " and ";
		}		
	    if(!where.trim().equals("")){
	    	where = " where " + where;
	    	where = where.substring(0,where.length() - " and ".length());
	    }	
	    return where; 	
    }
    
    
    public static String getWhereSemWhere(String... conditions){
	    String where = "";
	    for (int i = 0; i < conditions.length; i++) {
			if(!conditions[i].equals(""))
				where += conditions[i] + " and ";
		}		
	    if(!where.equals("")){
	    	where = where.substring(0,where.length() - " and ".length());
	    }	
	    return where; 	
    }
    
    

	public static float getFloatFromFieldValue(Object value) {
		float r = 0;
		try{
			r = (Float)value;
		}catch(Exception e){
			
		}
		return r;
	}
	
	


    public static String getComandoBloqueia(Equipamento equipamento){
    	if(equipamento.getModelo()==ModeloRastreador.GT06  
    			|| equipamento.getModelo()==ModeloRastreador.GT06N 
				|| equipamento.getModelo()==ModeloRastreador.JV200
				|| equipamento.getModelo()==ModeloRastreador.CRXN
				|| equipamento.getModelo()==ModeloRastreador.CRX3
    			|| equipamento.getModelo()==ModeloRastreador.CRX1){
    		return "relay,1#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK103A2){
    		return "stop"+equipamento.getSenha();
    	}else if(equipamento.getModelo()==ModeloRastreador.XT009){
    		return "powercar" + equipamento.getSenha()+" 11";
    	}else if(equipamento.getModelo()==ModeloRastreador.GT06B){
    		return "relay" + equipamento.getSenha()+" 1";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK06){
    		return "#stopelec#"+ equipamento.getSenha()+"#" ;
    	}
    	return null;
    }
    
    
    public static SmsStatus getStatusParaBloqueio(Equipamento equipamento){
    	if(equipamento.getModelo()==ModeloRastreador.GT06  
    			|| equipamento.getModelo()==ModeloRastreador.GT06N 
				|| equipamento.getModelo()==ModeloRastreador.JV200
				|| equipamento.getModelo()==ModeloRastreador.CRXN 
				|| equipamento.getModelo()==ModeloRastreador.CRX3
    			|| equipamento.getModelo()==ModeloRastreador.CRX1)
    		return SmsStatus.ESPERANDO_SOCKET;
    	return SmsStatus.ESPERANDO;
    }
    
    
    
    public final static int PORT_TK103 = 6214;
	public final static int PORT_GT06 =  6215;
	public final static int PORT_H02 =   6216;
	public final static int PORT_GT06_B= 6217;
	public final static int PORT_XT009=  6218;
	public final static int PORT_TR02 =  6219;
	public final static int PORT_CRX1 =  6220;
	public final static int PORT_JV200=  6221;
    public final static int PORT_TK06=   6222;
	public final static int PORT_CRXN =  6223;
	public final static int PORT_CRX3 =  6224;
    
    
    public static String getComandoDNS(Equipamento equipamento,String dns,String ip){
    	
    	if(equipamento.getModelo()==ModeloRastreador.TK103A2){
    		return "adminip"+equipamento.getSenha()+" "+ip+" 6214";
    	}else if(equipamento.getModelo()==ModeloRastreador.XT009){
    		return "adminip"+equipamento.getSenha()+" "+ip+" 6218";
    	}else if(equipamento.getModelo()==ModeloRastreador.GT06B){
    		return "IP "+equipamento.getSenha()+" "+ip+" 6217";
    	}else if(equipamento.getModelo()==ModeloRastreador.GT06  || equipamento.getModelo()==ModeloRastreador.GT06N) {
    		return "SERVER,0,"+ip+",6215,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.CRX1) {
    		return "SERVER,0,"+ip+",6220,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.CRX3) {
    		return "SERVER,0,"+ip+",6224,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.CRXN) {
        		return "SERVER,0,"+ip+",6223,0#";	
    	}else if(equipamento.getModelo()==ModeloRastreador.JV200) {
    		return "SERVER,0,"+ip+",6221,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TR02) {
    		return "SERVER,666666,0,"+ip+",6219,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK06) {
    		return "#SERVER#123456#"+ip+"#6222#";
    	}
    	return null;
    }
    
    
    public static String getComandoReset(Equipamento equipamento){
    	
    	if(equipamento.getModelo()==ModeloRastreador.TK103A2){
    		return "RESET"+equipamento.getSenha();
    	}else if(equipamento.getModelo()==ModeloRastreador.XT009){
    		return "RESET"+equipamento.getSenha();
    	}else if(equipamento.getModelo()==ModeloRastreador.GT06  || equipamento.getModelo()==ModeloRastreador.GT06N) {
    		return "RESET#";
    	}else if(equipamento.getModelo()==ModeloRastreador.CRX1 || equipamento.getModelo()==ModeloRastreador.CRX1) {
    		return "RESET#";
    	}else if(equipamento.getModelo()==ModeloRastreador.CRX3 || equipamento.getModelo()==ModeloRastreador.CRX3) {
    		return "RESET#";
    	}else if(equipamento.getModelo()==ModeloRastreador.JV200) {
    		return "RESET#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TR02) {
    		return "RESET,666666#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK06) {
    		return "#reboot#123456#";
    	}
    	return null;
    }
    
    
    public static String getComandoCenter(Equipamento equipamento){
    	if(equipamento.getModelo()==ModeloRastreador.GT06  
    			|| equipamento.getModelo()==ModeloRastreador.GT06N 
				|| equipamento.getModelo()==ModeloRastreador.JV200
				|| equipamento.getModelo()==ModeloRastreador.CRXN
				|| equipamento.getModelo()==ModeloRastreador.CRX3
    			|| equipamento.getModelo()==ModeloRastreador.CRX1) 
    		return "Center,a,85998356104#";
    	return null;
    }
    
    
    public static String getComandoDesbloqueia(Equipamento equipamento){
    	if(equipamento.getModelo()==ModeloRastreador.GT06  
    			|| equipamento.getModelo()==ModeloRastreador.GT06N
				|| equipamento.getModelo()==ModeloRastreador.CRXN
				|| equipamento.getModelo()==ModeloRastreador.CRX3
				|| equipamento.getModelo()==ModeloRastreador.JV200
    			|| equipamento.getModelo()==ModeloRastreador.CRX1){
    		return "relay,0#";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK103A2){
    		return "resume"+equipamento.getSenha();
    	}else if(equipamento.getModelo()==ModeloRastreador.XT009){
    		return "powercar" + equipamento.getSenha()+" 00";
    	}else if(equipamento.getModelo()==ModeloRastreador.GT06B){
    		return "relay" + equipamento.getSenha()+" 0";
    	}else if(equipamento.getModelo()==ModeloRastreador.TK06){
    		return "#supplyelec#"+ equipamento.getSenha()+"#" ;
    	}
    	return null;
    }

	public static String getNossoNumeroDV(String nossoNumero) {
		int m = 1;
		int dv;
		int soma = 0;
		String nn = nossoNumero;
		for (int i = nn.length()-1; i>=0 ; i--) {
			char c = nn.charAt(i);
			int n = Character.getNumericValue(c);
			m+=1;
			if(m==10)
				m = 2;
			soma += m*n;
		}
		if(soma < 11)
			dv = 11 - soma;
		else
			dv = 11 - (soma%11);
		if(dv>9)
			dv = 0;
		return String.valueOf(dv) ;
	}


	public static boolean isDomingo() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}


	public static Date now() {
		//TimeZone timezone = TimeZone.getTimeZone("GMT-3:00");
		//Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.HOUR_OF_DAY,-1);
		return new Date();//.getTime();
	}
	
	public static Calendar getCalendarInstance() {
		//TimeZone timezone = TimeZone.getTimeZone("GMT-3:00");
		//Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.HOUR_OF_DAY,-1);
		return Calendar.getInstance();
	}


	
 /*   public static CarnetConteudo findCarnetConteudoesByVeiculoAnoReferencia(Veiculo veiculo,int anoReferencia) {
        if (veiculo == null) throw new IllegalArgumentException("The veiculo argument is required");
        EntityManager em = CarnetConteudo.entityManager();
        String sql = "SELECT cc FROM CarnetConteudo as cc, Carnet as c "
        			+ "WHERE cc.veiculo = c.veiculo and cc.veiculo=:veiculo and c.anoReferencia=:anoReferencia ";

        TypedQuery<CarnetConteudo> q = em.createQuery(sql,CarnetConteudo.class);
        q.setParameter("veiculo", veiculo);
        q.setParameter("anoReferencia", anoReferencia);
        
        
        return q.getSingleResult();
    }*/


	public static String formatPlaca(String placa) {
		try{
			return placa.substring(0,3)+"-"+placa.substring(3);
		}catch(Exception e){
			return "---";
		}
	}


	
	
	public static boolean isHistorico(Date t1){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(t1);
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,-1);
		c.set(Calendar.HOUR,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		
		return !c1.after(c);
	}


	public static Date getFirstTimeOFToday(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTime();
	}
	
	
	public static Date getLastTimeOFToday(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.MILLISECOND,999);
		return c.getTime();
	}

	public static Float stringToPriceFloat(String s) {
		s = s.replaceAll("[^\\d]", "");
		return (Float.parseFloat(s)/100);
	}
	
	public static Double stringToPriceDouble(String s) {
		s = s.replaceAll("[^\\d]", "");
		return (Double.parseDouble(s)/100);
	}

	








/*	public static boolean isInadimplente(Cliente cliente, int dias) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,-dias);	

		EntityManager em = EntityProviderUtil.get().getEntityManager(Fatura.class);
		Query query = em.createQuery("select count(f) from Fatura f where f.cliente.id=:clienteId and f.situacao=:situacao and f.dataVencimento <=:vencimento");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("situacao",FaturaStatus.Vencida);
		query.setParameter("vencimento",c.getTime());
		long count =  (Long)query.getSingleResult();

		return count>0;
	}*/
	
	
/*	public static int getAtrasoNoPagamento(Cliente cliente) {
		Calendar c = Calendar.getInstance();
	

		EntityManager em = EntityProviderUtil.get().getEntityManager(Fatura.class);
		Query query = em.createQuery("select min(f.dataVencimento) from Fatura f where f.cliente.id=:clienteId and f.situacao=:situacao and f.dataVencimento <:vencimento");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("situacao",FaturaStatus.Vencida);
		query.setParameter("vencimento",c.getTime());
		Date vencimento = null;
		try{
			vencimento = (Date)query.getSingleResult();
		}catch(Exception e){
			return 0;
		}

		return getDaysSinceDate(new Date(),vencimento);
	}	*/
	
	

    /*static List<Permissao> permissoesDoCliente;
	public static List<Permissao> getPermissoesDoCliente() {
		if(permissoesDoCliente==null){
			permissoesDoCliente = new ArrayList<Permissao>();
			permissoesDoCliente.add(Permissao.VER_MAPA);
			permissoesDoCliente.add(Permissao.VER_PESSOA);
			permissoesDoCliente.add(Permissao.VER_BOLETO);
			permissoesDoCliente.add(Permissao.VER_CONTRATO);
			permissoesDoCliente.add(Permissao.CADASTRA_PROPRIA_SENHA);
		}
		return permissoesDoCliente;
	}
	
    static List<Permissao> permissoesDoAdministrador;
	public static List<Permissao> getPermissoesDoAdministrador() {
		if(permissoesDoAdministrador==null){
			permissoesDoAdministrador = new ArrayList<Permissao>();
			permissoesDoAdministrador.add(Permissao.CADASTRA_BOLETO);
			permissoesDoAdministrador.add(Permissao.CADASTRA_CHIP);
			permissoesDoAdministrador.add(Permissao.CADASTRA_CONTRATO);
			permissoesDoAdministrador.add(Permissao.CADASTRA_OUTRAS_SENHAS);
			permissoesDoAdministrador.add(Permissao.CADASTRA_PESSOA);
			permissoesDoAdministrador.add(Permissao.CADASTRA_RASTREADOR);
			permissoesDoAdministrador.add(Permissao.CADASTRA_RECARGA);
			permissoesDoAdministrador.add(Permissao.FILTRA_MAPA);
			permissoesDoAdministrador.add(Permissao.VER_BOLETO);
			permissoesDoAdministrador.add(Permissao.VER_CHIP);
			permissoesDoAdministrador.add(Permissao.VER_CHIP);
			permissoesDoAdministrador.add(Permissao.VER_CONTRATO);
			permissoesDoAdministrador.add(Permissao.VER_MAPA);
			permissoesDoAdministrador.add(Permissao.VER_PAG_SEGURO);
			permissoesDoAdministrador.add(Permissao.VER_PESSOA);
			permissoesDoAdministrador.add(Permissao.VER_RASTREADOR);
			permissoesDoAdministrador.add(Permissao.VER_RECARGA);
			permissoesDoAdministrador.add(Permissao.VER_RELATORIO);			
		}
		return permissoesDoAdministrador;
	}*/
	

    /*static List<Permissao> permissoesDoFinanceiro;
	public static List<Permissao> getPermissoesDoFinanceiro() {
		if(permissoesDoFinanceiro==null){
			permissoesDoFinanceiro = new ArrayList<Permissao>();
			permissoesDoFinanceiro.add(Permissao.CADASTRA_BOLETO);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_CHIP);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_CONTRATO);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_OUTRAS_SENHAS);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_PESSOA);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_RASTREADOR);
			permissoesDoFinanceiro.add(Permissao.CADASTRA_RECARGA);
			permissoesDoFinanceiro.add(Permissao.FILTRA_MAPA);
			permissoesDoFinanceiro.add(Permissao.VER_BOLETO);
			permissoesDoFinanceiro.add(Permissao.VER_CHIP);
			permissoesDoFinanceiro.add(Permissao.VER_CONTRATO);
			permissoesDoFinanceiro.add(Permissao.VER_MAPA);
			permissoesDoFinanceiro.add(Permissao.VER_PAG_SEGURO);
			permissoesDoFinanceiro.add(Permissao.VER_PESSOA);
			permissoesDoFinanceiro.add(Permissao.VER_RASTREADOR);
			permissoesDoFinanceiro.add(Permissao.VER_RECARGA);
			permissoesDoFinanceiro.add(Permissao.VER_RELATORIO);			
		}
		return permissoesDoFinanceiro;
	}
	
	
    static List<Permissao> permissoesDoVendedor;
	public static List<Permissao> getPermissoesDoVendedor() {
		if(permissoesDoFinanceiro==null){
			permissoesDoFinanceiro = new ArrayList<Permissao>();
			permissoesDoFinanceiro.add(Permissao.CADASTRA_BOLETO);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_CHIP);
			permissoesDoFinanceiro.add(Permissao.CADASTRA_CONTRATO);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_OUTRAS_SENHAS);
			permissoesDoFinanceiro.add(Permissao.CADASTRA_PESSOA);
			permissoesDoFinanceiro.add(Permissao.CADASTRA_RASTREADOR);
			//permissoesDoFinanceiro.add(Permissao.CADASTRA_RECARGA);
			permissoesDoFinanceiro.add(Permissao.FILTRA_MAPA);
			permissoesDoFinanceiro.add(Permissao.VER_BOLETO);
			permissoesDoFinanceiro.add(Permissao.VER_CHIP);
			permissoesDoFinanceiro.add(Permissao.VER_CONTRATO);
			permissoesDoFinanceiro.add(Permissao.VER_MAPA);
			permissoesDoFinanceiro.add(Permissao.VER_PAG_SEGURO);
			permissoesDoFinanceiro.add(Permissao.VER_PESSOA);
			permissoesDoFinanceiro.add(Permissao.VER_RASTREADOR);
			permissoesDoFinanceiro.add(Permissao.VER_RECARGA);
			permissoesDoFinanceiro.add(Permissao.VER_RELATORIO);			
		}
		return permissoesDoFinanceiro;
	}*/
	
	
	
	
	
	
	

	/*public static void setPermissoesDoCliente(List<Permissao> permissoesDoCliente) {
		Utils.permissoesDoCliente = permissoesDoCliente;
	}

	public static boolean hasPermission(Pessoa usuario, String string) {
		
		List<Permissao> permissoesDoCliente = new ArrayList<Permissao>();
		permissoesDoCliente.add(Permissao.ACESSA_MAPA);
		
		
		return false;
	}*/	

}
