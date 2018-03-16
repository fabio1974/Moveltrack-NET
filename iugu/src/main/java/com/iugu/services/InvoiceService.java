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
import com.iugu.utils.Utils;


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

	
	@Inject IuguConfiguration iuguConfiguration;
	
	@PostConstruct
	public void init(){
		
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

	public InvoiceResponse cancel(String id) {
		Response response = iuguConfiguration.getNewClient().target(String.format(CANCEL_URL, id)).request().put(null);

		InvoiceResponse invoiceResponse = response.readEntity(InvoiceResponse.class);
		invoiceResponse.setResponse(response);
		response.close();
		return invoiceResponse;
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