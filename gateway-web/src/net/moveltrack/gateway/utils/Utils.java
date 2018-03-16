/*
 * Utils.java
 *
 * Created on 14 de Setembro de 2007, 12:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.moveltrack.gateway.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.text.MaskFormatter;
import javax.xml.datatype.XMLGregorianCalendar;

import net.moveltrack.gateway.persistence.Conexao;




/**
 *
 * @author carlos.ferreira
 */

public class Utils {
    public static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat FORMATO_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat FORMATO_DATA_SEGUNDOS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final NumberFormat numberFormat = NumberFormat.getInstance();//new Locale("pt","BR"));
         
    
 /////   private static final Cursor wait_cursor = new Cursor(Cursor.WAIT_CURSOR);
 /////   private static final Cursor default_cursor = new Cursor(Cursor.DEFAULT_CURSOR);
    // public static final SimpleDateFormat FORMATO_DATAHORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /** Creates a new instance of Utils */
    public Utils() {
    }
    
    
    public static void logImei(boolean debug, String content, String currentImei,String standardImei){
    	if(debug && currentImei.equals(standardImei))
    		System.out.println(FORMATO_DATA_SEGUNDOS.format(new Date())+ " - "+ content);
    }
    
    
    
    public static void log(boolean debug, String content){
    	if(debug)
    		System.out.println(FORMATO_DATA_SEGUNDOS.format(new Date())+ " - "+ content);
    }
    
    public static void logh(boolean debug, String content){
    	if(debug)
    		System.out.print(FORMATO_DATA_SEGUNDOS.format(new Date())+ " - "+ content);
    }
    
    public static void logSemData(boolean debug, String content){
    	if(debug)
    		System.out.println(content);
    }
    
    
    
    public static boolean isBlank(Object obj){
        return obj == null || obj.toString() == null || obj.toString().trim().equals("");
    }
    
    public static String BlankIfNull(String texto){
        if(texto == null) return "";
        return texto;
    }
    
    public static String BlankIfNullOrEmpty(String texto){
        if(texto == null || texto.equals("")) return "";
        return texto;
    }
    
    public static Double formatDouble(double d){
        DecimalFormat decimal = new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US));   
        return new Double(decimal.format(d));
    }    

    
 /*   public static Cursor getWaitCursor(){
        return wait_cursor;
    }
    
    public static Cursor getDefaultCursor(){
        return default_cursor;
    }*/
    
    public static double truncate(double valor, int casasDecimais){
        int inteiro = (int)(valor * Math.pow(10, casasDecimais));
        return inteiro / Math.pow(10, casasDecimais);
    }
    
    public static String toUpperCase(String texto){
        return texto.toUpperCase();
    }
    
    public static Date textToTime(String texto) throws ParseException{
        return FORMATO_HORA.parse(texto);
    }
    
    public static Date textToDate(String texto) throws ParseException{
        return FORMATO_DATA.parse(texto);
    }

    
    public static String numberToText(double numero, int decimais){
        return numberToText(new Double(numero), decimais);
    }
    
    public static String numberToText(Number numero, int decimais){
        if( numero == null) return "";
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(decimais);
        return numberFormat.format(numero);
    }
    
    public static Number textToNumber(String texto) throws ParseException{
        if( texto.trim().equals("")) return null;
        return numberFormat.parse(texto);
    }
    
    public static Double textToDouble(String texto) throws ParseException{
        Number retorno = textToNumber(texto);
        if( retorno == null) return null;
        return new Double(retorno.doubleValue());
    }
    
    /*
    public static Double textToDouble(String texto){
        if(texto.trim().equals("") return null
        return numberFormat.parse(texto);
        //texto = texto.trim().replaceAll("\\,","\\.");
        //return texto.equals("") ? null : new Double(texto);
    }*/
    
    public static Integer TextToInteger(String texto){
        return texto.equals("") ? null : new Integer(texto);
    }
    
    public static String lpad(String valueToPad, String filler, int size) {
        while (valueToPad.length() < size) {
            valueToPad = filler + valueToPad;
        }
        return valueToPad;
    }
    
    public static String getCurrentPath(){
        //Se estiver rodando no pda
        if(System.getProperty("os.arch").equals("x86"))
            return System.getProperty("user.dir") + File.separatorChar;
        
        String javavm = System.getProperty("java.vm.name");
        if( javavm.equals("J9") || javavm.equals("CVM")){
            String caminho = System.getProperty("java.class.path");
            File tmp = new File(caminho);
            return tmp.getParent() + File.separatorChar;
        } else
            return System.getProperty("user.dir") + File.separatorChar;
        
        //return "";
        
    }
    
    /*public static void main(String args[]){

        String teste = getNumAbordagem();

        Date d = new Date();
        System.out.println(d.toString());
        Date h = getDataByDataHora(d);
        System.out.println(h.toString());
        System.out.println(getHoraByDataHora(d).toString());
    }*/

 
    
    
    
    
    public static Date getHoraByDataHora(Date dataHora){
        if( dataHora == null) return null;
        String strhora = FORMATO_HORA.format(dataHora);
        try {
            return FORMATO_HORA.parse(strhora);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
   
    public static String getHoraStringByDataHora(Date dataHora){
        if( dataHora == null) return null;
        return FORMATO_HORA.format(dataHora);
    }
    
    public static Date dataMaisXMinutos(Date data,long x){
        if(data == null) return null;
        Date newDate = new Date(data.getTime()+1000*60*x);
        return newDate;
    }


    public static String getJ9Versao(){
        String j9Versao = "";
        try{
            File f = new File("/Application/J9/bin/j9.exe");
            long dataArq = f.length(); 
            String s = (dataArq+"").substring(0,3);
            if(s.equals("563"))
                j9Versao = "J91.0";
            else if(s.equals("778"))
                j9Versao = "J91.1";
            else if(s.equals("409"))
                j9Versao = "J91.2";
            else
                j9Versao = "J9New";
        }catch(Exception e){
            j9Versao = "J9?";
            e.printStackTrace();
        }
        return j9Versao;
    }

    //563 - 10/11/2006  1.0
    //778 - 15/02/2008  1.1
    //409 - 26/09/2008  1.2


    public static Date dataMaisXSegundos(Date data,long x){
        if(data == null) return null;
        Date newDate = new Date(data.getTime()+1000*x);
        return newDate;
    }
    
    public static Date dataMaisXDias(Date data,long x){
        if(data == null) return null;
        Date newDate = new Date(data.getTime()+1000*60*60*24*x);
        return newDate;
    }
    
    
    public static Timestamp dataAtualMenosXDias(long x){
        Calendar c = Calendar.getInstance();
    	return new Timestamp(c.getTimeInMillis()-1000*60*60*24*x);
    }
    
    public static Timestamp dataMaisXDias(Timestamp data,long x){
    	return new Timestamp(data.getTime()+1000*60*60*24*x);
    }
    
    

    
    public static Date somaDataHora(Date data, Date hora){
        if( data == null) return hora;
        if( hora == null) return data;
        try {
            //Retira a hora da data
            Calendar calData = Calendar.getInstance();
            Calendar calHora = Calendar.getInstance();
            calData.setTime(data);
            calHora.setTime(hora);
            
            calData.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
            calData.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
            return calData.getTime();
            
                        /*
                        String strdata = FORMATO_DATA.format(data);
                        Date novadata = FORMATO_DATA.parse(strdata);
                         
                        String strhora = FORMATO_HORA.format(hora);
                        Date novahora = FORMATO_HORA.parse(strhora);
                        long soma = novadata.getTime() + novahora.getTime() - 10800000;
                        return new Date(soma);*/
            
            
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String convertData(String value){
        if(value == null || value.equals("")) return "";
        if (value.indexOf("/")!=-1) 
            return value;
     //   if(uf.equals("CE"))
     //       return value.substring(4) + "/" + value.substring(2,4) + "/" + value.substring(0,2);
        return value.substring(0,2) + "/" + value.substring(2,4) + "/" + value.substring(4);// + "=" + value;
    }

  /*  public static String convertData(String value){
        if(value == null || value.equals("")) return "";
        if (value.indexOf("/")!=-1)
            return value;
        return value.substring(0,2) + "/" + value.substring(2,4) + "/" + value.substring(4);// + "=" + value;
    }*/



    
    public static Date getDataFromDataHora(Date dataHora){
        if( dataHora == null) return null;
        String data = FORMATO_DATA.format(dataHora);
        try {
            return FORMATO_DATA.parse(data);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static String getDataStringByDataHora(Date dataHora){
        if( dataHora == null) return null;
        return FORMATO_DATA.format(dataHora);
    }
    
    public static boolean isSegura(String url){
        return url.startsWith("https");
    }

    

    
    public static String getHost(String url){
        String host="";    
        try{
            String aux  = url.substring(url.indexOf("://")+3);
            host = aux.substring(0,aux.indexOf("/"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }    
        return host;
    }
    
    public static String getHostName(String url){
        String aux="";    
        try{
            aux  = url.substring(url.indexOf("://")+3);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }    
        return aux;
    }
    
    
    
    public static String stripCharsInBag(String text, String bag){
        if (text == null) return "";
        StringBuffer value=new StringBuffer();
        for(int ndx = 0; ndx < text.length(); ndx++){
            char cc = text.charAt(ndx);
            if (bag.indexOf(cc) == -1) value.append(cc);
        }
        return value.toString();
    }
    
    public static String getMarcaFromMarcaModelo(String marcaModelo){
        try{
            return marcaModelo.substring(marcaModelo.indexOf("/")+1);
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
        return "";
    }

 /*   public static String getNumAbordagem(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String serial = null;
        try {
            serial = ParametrosDAO.getInstance().getByChave("SERIAL").valor;
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
        String UUIDPart1 = serial.substring(0,16);
        String data = sdf.format(new Date());
        return UUIDPart1+data;

    }*/

    
    public static String usingDateFormatterWithTimeZone(long input){
		Date date = new Date(input);
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
		sdf.setCalendar(cal);
		cal.setTime(date);
		return sdf.format(date);

	}    

    public static String formatDate(String format,long time){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String data = sdf.format(new Date(time));
        return data;

    }


    /*public static void imprimeInputStream(InputStream in){
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            while (read.ready()) {
                System.out.println(read.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/

    public static void fillFileHierarchy(File folderFrom) throws IOException {
        File[] files = folderFrom.listFiles();
        for (int x = 0; x < files.length; x++) {
            File f = files[x];
            if (f.isDirectory()) {
                fillFileHierarchy(f);
            } else if (f.isFile()) {
                File fNew = new File(folderFrom.getPath() + File.separator + f.getName());
                FileOutputStream fOut = new FileOutputStream(fNew);
                fOut.close();
            }
        }
    }


    public static void copyFileHierarchy(File folderFrom, File folderTo) throws IOException {
        byte[] buffer = new byte[8192];
        if (!folderTo.exists()) {
            folderTo.mkdirs();
        }
        File[] files = folderFrom.listFiles();
        for (int x = 0; x < files.length; x++) {
            File f = files[x];
            if (f.isDirectory()) {
                copyFileHierarchy(f, new File(folderTo + File.separator + f.getName()));
            } else if (f.isFile()) {
                File fNew = new File(folderTo.getPath() + File.separator + f.getName());

                FileOutputStream fOut = new FileOutputStream(fNew);
                FileInputStream fIn = new FileInputStream(f);
                int bytes_read;
                while((bytes_read = fIn.read(buffer)) != -1) // Read bytes until EOF
                    fOut.write(buffer, 0, bytes_read);            //   write bytes
                fIn.close();
                fOut.close();
            }
        }
    }

    public static  String getListaConcatenada(String[] lista){
        if( lista == null || lista.length == 0) return "";
        StringBuffer sb = new StringBuffer();
        for(int x=0; x < lista.length ; x++)
            if( x <= lista.length - 1)
                sb.append(lista[x]).append(", ");
        return sb.toString();
    }


      public static String getDataFormatadaComBarras(String data, String UF) {

         if (data==null && data.trim().equals("") || data.length() != 8)
             return data;

         if(UF.equals("CE"))
            return data.substring(6,8) + "/" + data.substring(4,6) + "/" + data.substring(0,4);
         return data.substring(0, 2) + "/" + data.substring(2, 4) + "/"+ data.substring(4,8);
     }

     public static Date getDateFromStringFormat(String data, String format) throws ParseException {
          SimpleDateFormat sdf = new SimpleDateFormat(format);
          return  new Date(sdf.parse(data).getTime());
     }
     
    


/**
	 * Retira acentos e cedilha de uma string. Prop�sito inicial � o tratamento
	 * das strings que descrevem restri��es de ve�culos, e v�m com acentos pelo
	 * Infoseg e sem acento pelo SERPRO.
	 *
	 * @param str
	 * @return A string <code>str</code> sem acentos, cedilha e trema.
	 */
	public static String getStringSemCaracteresEspeciaisAlfabeto(String str) {
		return str.replaceAll("([�|�|�|�|�])", "a").replaceAll("([�|�|�|�|�])", "A").
				replaceAll("([�|�|�|�])", "e").replaceAll("([�|�|�|�])", "E").
				replaceAll("([�|�|�|�])", "i").replaceAll("([�|�|�|�])", "I").
				replaceAll("([�|�|�|�|�])", "o").replaceAll("([�|�|�|�|�])", "O").
				replaceAll("([�|�|�|�])", "u").replaceAll("([�|�|�|�])", "U").
				replaceAll("([�])", "c").replaceAll("([�])", "C");
	}



public static String dateStringToTimestampString(String data, String format) {
	if(data==null||data.equals(""))
		return null;
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	Date date=null;
	try {
		date = sdf.parse(data);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	Timestamp timestamp = new Timestamp(date.getTime());
	return timestamp.toString();
}

public static String dateTimestampStringToDateString(String timestampString,String format){
	if(timestampString==null)
		return "";
	return new SimpleDateFormat(format).format(Timestamp.valueOf(timestampString));
}

public static Timestamp dateTimestampStringToTimestamp(String timestampString){
	if(timestampString==null)
		return null;
	Timestamp result=null;
	try {
		result = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestampString).getTime());
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return result; 
}




 
public static String formatarString(String texto, String mascara) throws ParseException {  
	if(texto==null || texto.equals(""))
		return "";
    MaskFormatter mf = new MaskFormatter(mascara);  
    mf.setValueContainsLiteralCharacters(false);  
    return mf.valueToString(texto);  
}


public static String getCelularOfImei(String imei) {
		Connection conn = Conexao.getMovelTrackConnection();
		try {
			
			String sql ="select c.numero from Chip as c, Equipamento as e where c.id = e.chip_id and e.imei =  '"+imei+"'";
			
			Statement stm = conn.createStatement();
			ResultSet rSet = stm.executeQuery(sql);
			if(rSet.next()) {
				String celular = rSet.getString("numero");
				celular = "+55" + celular.replaceAll("[^0-9]","");
				return celular;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


    public static Date toDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        
        //calendar.setTimezone(-3*60);
        
        return calendar.toGregorianCalendar().getTime();
    }








}
