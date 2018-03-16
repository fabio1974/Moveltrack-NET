package net.moveltrack.util;

import java.text.Normalizer;

public class RemoveAcentosEspacos {
        static String acentuado = "çÇáéíóúý�?É�?ÓÚ�?àèìòùÀÈÌÒÙãõñäëïöüÿÄË�?ÖÜÃÕÑâêîôûÂÊÎÔÛ";  
        static String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";  
        static char[] tabela;  
        static {  
            tabela = new char[256];  
            for (int i = 0; i < tabela.length; ++i) {  
            tabela [i] = (char) i;  
            }  
            for (int i = 0; i < acentuado.length(); ++i) {  
                tabela [acentuado.charAt(i)] = semAcento.charAt(i);  
            }  
        }  
         public static String removeAcentosEEspacos(final String s) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < s.length(); ++i) {  
                char ch = s.charAt (i);  
                if (ch < 256) {   
                    sb.append (tabela [ch]);  
                } else {  
                    sb.append (ch);  
                }  
            }  
            String r = sb.toString(); 
            return r.replace(" ",""); 
        }
        
        public static String removeAcentos(String str) {
        	 
        	  str = Normalizer.normalize(str, Normalizer.Form.NFD);
        	  str = str.replaceAll("[^\\p{ASCII}]", "");
        	  return str;
        	 
       }
        
        
        
 }
