package net.moveltrack.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.jboss.security.Base64Encoder;

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

}
