package com.iugu.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.iugu.IuguConfiguration;
import com.iugu.exceptions.IuguException;
import com.iugu.model.Invoice;
import com.iugu.responses.InvoiceResponse;
import com.iugu.utils.OSValidator;
import com.iugu.utils.Utils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import sun.misc.BASE64Encoder;


@Named 
@Stateless
public class InvoiceService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7336309361080831106L;

	private final String CREATE_URL = IuguConfiguration.url("/invoices");
	private final String FIND_URL = IuguConfiguration.url("/invoices/%s");
	private final String DUPLICATE_URL = IuguConfiguration.url("/invoices/%s/duplicate");
	private final String REMOVE_URL = IuguConfiguration.url("/invoices/%s");
	private final String CANCEL_URL = IuguConfiguration.url("/invoices/%s/cancel");
	private final String REFUND_URL = IuguConfiguration.url("/invoices/%s/refund");
	private String TOKEN = "";
	private String AUTH_STRING_ENC="";
	
	@Inject IuguConfiguration iuguConfiguration;
	
	@PostConstruct
	public void init(){
			if(OSValidator.isUnix())
				TOKEN = "929467175fcce7aa3378e74d4c5304c7";  //produção
			else
				TOKEN = "1ff25a762d28d51bd34863406cbb8c2b"; //homologacao
	        String authString = TOKEN + ":" + "";
	        AUTH_STRING_ENC = new BASE64Encoder().encode(authString.getBytes());

	}
	
	public InvoiceResponse create(Invoice invoice) throws IuguException {
		
		
		Response response = iuguConfiguration.getNewClient().target(CREATE_URL).request().post(Entity.entity(invoice, MediaType.APPLICATION_JSON));

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error creating invoice!", ResponseStatus, ResponseText);
	}
	
	
/*	public InvoiceResponse createJersey(Invoice invoice) throws IuguException {
		String url = CREATE_URL;
		WebResource webResource = Client.create().resource(url);
        webResource.type("application/json").accept("application/json").header("Authorization", "Basic " + AUTH_STRING_ENC);
        Entity<Invoice> invoiceEntity = Entity.entity(invoice, MediaType.APPLICATION_JSON);
        ClientResponse resp = webResource.post(ClientResponse.class,invoice);
	        if(resp.getStatus() != 200){
	            System.err.println("Unable to connect to the server");
	        }
	        InvoiceResponse output = resp.getEntity(InvoiceResponse.class);
	        System.out.println("response: "+output);
		return output;
	}*/
	
	
	
	public InvoiceResponse cancel(String id) {
		Response response = iuguConfiguration.getNewClient().target(String.format(CANCEL_URL, id)).request().put(null);

		InvoiceResponse invoiceResponse = response.readEntity(InvoiceResponse.class);
		invoiceResponse.setResponse(response);
		response.close();
		return invoiceResponse;
	}

	
	
/*	public InvoiceResponse cancelJersey(String id)  {
		String url = String.format(CANCEL_URL, id);
		WebResource webResource = Client.create().resource(url);
        webResource.accept("application/json").header("Authorization", "Basic " + AUTH_STRING_ENC);
        ClientResponse resp = webResource.put(ClientResponse.class);
	        if(resp.getStatus() != 200){
	            System.err.println("Error cencelando invoice with id: " + id + " - "+  resp.getStatus() + " - " + resp.getEntity(String.class));
	        }
	        InvoiceResponse output = resp.getEntity(InvoiceResponse.class);
	        System.out.println("response: "+output);
		return output;
	}
*/	
	

	public InvoiceResponse find(String id) throws IuguException {
		Response response = iuguConfiguration.getNewClient().target(String.format(FIND_URL, id)).request().get();

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error finding invoice with id: " + id, ResponseStatus, ResponseText);
	}
	
/*	public InvoiceResponse findJersey(String id) throws IuguException {
		String url = String.format(FIND_URL, id);
		WebResource webResource = Client.create().resource(url);
        webResource.accept("application/json").header("Authorization", "Basic " + AUTH_STRING_ENC);
        ClientResponse resp = webResource.get(ClientResponse.class);
	        if(resp.getStatus() != 200){
	            System.err.println("Unable to connect to the server");
	        }
	        InvoiceResponse output = resp.getEntity(InvoiceResponse.class);
	        System.out.println("response: "+output);
		return output;
	}
*/
	

	public InvoiceResponse duplicate(String id, Date date) throws IuguException {
		SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
		Form form = new Form();

		form.param("due_date", sm.format(date));

		Response response = iuguConfiguration.getNewClient().target(String.format(DUPLICATE_URL, id)).request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error duplicating invoice with id: " + id, ResponseStatus, ResponseText);
	}

	public InvoiceResponse duplicate(String id, Date date, boolean ignoreCanceledEmail, boolean currentFinesOption) throws IuguException {
		SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
		Form form = new Form();

		form.param("due_date", sm.format(date));
		form.param("ignore_canceled_email", Utils.booleanToString(ignoreCanceledEmail));
		form.param("current_fines_option", Utils.booleanToString(currentFinesOption));

		Response response = iuguConfiguration.getNewClient().target(String.format(DUPLICATE_URL, id)).request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (response.getStatus() == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error duplicating invoice with id: " + id, ResponseStatus, ResponseText);
	}

	public InvoiceResponse remove(String id) throws IuguException {
		Response response = iuguConfiguration.getNewClient().target(String.format(REMOVE_URL, id)).request().delete();

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error removing invoice with id: " + id, ResponseStatus, ResponseText);
	}



	public InvoiceResponse refund(String id) throws IuguException {
		Response response = iuguConfiguration.getNewClient().target(String.format(REFUND_URL, id)).request().post(null);

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(InvoiceResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error refunding invoice with id: " + id, ResponseStatus, ResponseText);
	}

	// TODO Listar as faturas
}