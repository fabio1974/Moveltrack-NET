package net.moveltrack.rest;


import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.iugu.model.Event;
import com.iugu.model.Trigger;
import com.iugu.utils.Utils;

import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;

@Path("/iugu")
@RequestScoped
public class ResourceIugu {
	

	@Inject MBoletoDao mBoletoDao;

	
	
	@POST
    @Path("/changeInvoiceStatus")
    @Produces("application/x-www-form-urlencoded")
    @Consumes("application/x-www-form-urlencoded")
    public String changeInvoiceStatus(String gatilho) {
		
		System.err.println("GATILHO===>"+gatilho);
		
		Trigger trigger = Utils.stringToTrigger(gatilho);
		System.err.println("Trigger Status:"+trigger.getData().get("status"));
		if(trigger.getEvent()==Event.invoice_status_changed && 
				(trigger.getData().get("status").equals("paid")	|| trigger.getData().get("status").equals("partially_paid"))){
			System.err.println("Trigger Status:"+trigger.getData().get("status"));
			MBoleto mBoleto = mBoletoDao.findByIuguInvoiceId(trigger.getData().get("id"));
			if(mBoleto!=null){
				mBoleto.setSituacao(MBoletoStatus.PAGAMENTO_EFETUADO);
				mBoleto.setDataRegistroPagamento(new Date());
				mBoletoDao.merge(mBoleto);
			}
		}
		
		return "Ok";	
    }
	

	
	@POST
    @Path("/invoices")
	@Produces("application/json")
    @Consumes("application/json")
    public String invoices(String entrada) {

		System.out.println(entrada);
		
		return "Ok";	
    }
	
	
    
}