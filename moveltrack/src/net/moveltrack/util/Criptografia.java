package net.moveltrack.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.jboss.security.Base64Encoder;

import com.sun.xml.internal.fastinfoset.Encoder;

public class Criptografia {

	public Criptografia() {
		// TODO Auto-generated constructor stub
	}
	
	private  MessageDigest md;
	private  Base64Encoder encoder = new Base64Encoder();

	public String calculaHash(String texto) {
		String r=null;
		try {
			md = MessageDigest.getInstance("MD5");
		    byte[] bytes = texto.getBytes();
		    byte[] hash = md.digest(bytes);
			r= encoder.encode(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}	
	
	public static void main(String[] args) {
		String nome = "fabio";
		Criptografia c = new Criptografia();
		
		try {
			String cript = Base64.getEncoder().encode(nome.getBytes()).toString();
			String decoded = Base64.getDecoder().decode(cript.getBytes()).toString();
			System.out.println(cript);
			System.out.println(decoded);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
