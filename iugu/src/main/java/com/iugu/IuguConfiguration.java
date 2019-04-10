package com.iugu;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.iugu.utils.OSValidator;

@Named
@ApplicationScoped
public class IuguConfiguration implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String URL = "https://api.iugu.com/v1";
	private String tokenId;

	@PostConstruct
	public void init(){
		if(OSValidator.isUnix())
			tokenId = "929467175fcce7aa3378e74d4c5304c7";  //produção
		else
			tokenId = "1ff25a762d28d51bd34863406cbb8c2b"; //homologacao
			
		
	}
	
	
	public Client getNewClient()   {
		return ResteasyClientBuilder.newClient().register(new Authenticator(tokenId, ""));
	}

	public Client getNewClientNotAuth() {
		return ResteasyClientBuilder.newClient();
	}

	public static String url(String endpoint) {
		return URL + endpoint;
	}

}