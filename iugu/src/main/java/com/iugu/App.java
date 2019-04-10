package com.iugu;

import sun.misc.BASE64Encoder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static String original = "23793.38128 50009.493169 16000.050803 9 71100000000254";
	public static String value = "23793381285000949316916000050803971100000000254";
	public static String mask =  "AAAAA.AAAAA AAAAA.AAAAAA AAAAA.AAAAAA A AAAAAAAAAAAAAA";
	

	public static void main(String args[]) {
		  String url = "https://api.iugu.com/v1/invoices/F8CF05C4B98A4734A1A0398F6BFC7B32/cancel";
	        String name = "1ff25a762d28d51bd34863406cbb8c2b";
	        String password = "";
	        String authString = name + ":" + password;
	        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
	        System.out.println("Base64 encoded auth string: " + authStringEnc);
	        Client restClient = Client.create();
	        WebResource webResource = restClient.resource(url);
	        ClientResponse resp = webResource.accept("application/json")
	                                         .header("Authorization", "Basic " + authStringEnc)
	                                         .put(ClientResponse.class);
	        if(resp.getStatus() != 200){
	            System.err.println("Unable to connect to the server");
	        }
	        String output = resp.getEntity(String.class);
	        System.out.println("response: "+output);
	}

	public static String formatString(String string, String mask) throws java.text.ParseException {
		javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(string);
	}
    
 
}
