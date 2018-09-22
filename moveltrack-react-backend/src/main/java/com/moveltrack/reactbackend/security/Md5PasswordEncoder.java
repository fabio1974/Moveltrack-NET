package com.moveltrack.reactbackend.security;

import java.security.MessageDigest;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Md5PasswordEncoder implements PasswordEncoder{

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(calculaHash(rawPassword.toString()));
	}


	@Override
	public String encode(CharSequence rawPassword) {
		return calculaHash(rawPassword.toString());
	}
	
	private  MessageDigest md;

	public String calculaHash(String texto) {
		String r=null;
		try {
			md = MessageDigest.getInstance("MD5");
		    byte[] bytes = texto.getBytes();
		    byte[] hash = md.digest(bytes);
			r= Base64.toBase64String(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	
}