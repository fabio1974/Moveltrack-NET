package net.moveltrack.backgroundtasks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.iugu.exceptions.IuguException;
import com.iugu.model.Address;
import com.iugu.model.Invoice;
import com.iugu.model.Item;
import com.iugu.model.Payer;
import com.iugu.responses.InvoiceResponse;
import com.iugu.services.InvoiceService;

import net.moveltrack.dao.IuguDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Iugu;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskCorrigeIugu extends Task  {
	
	@PostConstruct
	public void init() {
	}
	
	@Inject MBoletoDao mBoletoDao;
	@Inject IuguDao iuguDao;
	@Inject InvoiceService invoiceService;

	
	@Override
    public void run() {

			Calendar c = Calendar.getInstance();
			c.set(2017,2,20);
			
			List<MBoleto> mBoletos = mBoletoDao.findByStatusInicioFim(MBoletoStatus.PAGAMENTO_EFETUADO,c.getTime(),new Date());
			for (MBoleto mBoleto : mBoletos) {
				if(mBoleto.getIugu()!=null){
					try {
						InvoiceResponse response = invoiceService.find(mBoleto.getIugu().getInvoiceId());
						if(!response.getStatus().equals("paid")){
							invoiceService.cancel(mBoleto.getIugu().getInvoiceId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	
		
	/*	List<Iugu> iugus = iuguDao.findAll();
		for (Iugu iugu : iugus) {
			
			if(!iugu.getCodigoBarras().equals("00000000000000000000000000000000000000000000000")){
				invoiceService.cancel(iugu.getInvoiceId());
				System.out.println(iugu.getInvoiceId() + "-"+ iugu.getCodigoBarras());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		
			
    
	
	
	private Iugu createIuguInvoice(MBoleto mBoleto) {
		int count=0;
		InvoiceResponse response = null;
		while((response==null || StringUtils.isEmpty(response.getId())) && count<3){
			response = createIuguInvoce(mBoleto);
			count++;
		}
		if(response!=null && !StringUtils.isEmpty(response.getId())){
			Iugu iugu = new Iugu();
			iugu.setInvoiceId(response.getId());
			iugu.setCodigoBarras(response.getBankSlip().getDigitableLine());
			iugu.setCodigoBarrasImagem(getBarCodeInBytes(response.getBankSlip().getBarcode()));
			return iugu;
		}
		return null;
	}
	
    private static byte[] getBarCodeInBytes(String url){
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	try {
    		URL u = new URL(url);
    		byte[] chunk = new byte[4096];
    		int bytesRead;
    		InputStream stream = u.openStream();

    		while ((bytesRead = stream.read(chunk)) > 0) {
    			outputStream.write(chunk, 0, bytesRead);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    	return outputStream.toByteArray();
    }

	
	private InvoiceResponse createIuguInvoce(MBoleto mBoleto){
		try {
			Cliente cliente = mBoleto.getContrato().getCliente();
			int valor = (int)(mBoleto.getValor()*100);
			
			Invoice invoice = new Invoice("suporte@moveltrack.com.br",mBoleto.getDataVencimento(), new Item(StringUtils.isEmpty(mBoleto.getMensagem34())?"Valor a ser pago.":mBoleto.getMensagem34(),1,valor));
	    	Address address = new Address(cliente.getEndereco(),cliente.getNumero(),cliente.getMunicipio()==null?"Fortaleza":cliente.getMunicipio().getDescricao(),cliente.getMunicipio()==null?"CE":cliente.getMunicipio().getUf().getSigla(),"Brasil",cliente.getCep()==null?"60000-000":cliente.getCep(),cliente.getBairro());
	    	String ddd = cliente.getCelular1()!=null?cliente.getCelular1().substring(1,3):"";
	    	String celular = cliente.getCelular1()!=null?cliente.getCelular1().substring(4):"";
	    	invoice.setPayer(new Payer(cliente.getNome(),"suporte@moveltrack.com.br",cliente.getCpf()!=null?cliente.getCpf():cliente.getCnpj(),ddd,celular,address));
	    	InvoiceResponse response = invoiceService.create(invoice);
			return response;	
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}