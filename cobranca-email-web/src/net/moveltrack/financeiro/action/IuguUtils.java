package net.moveltrack.financeiro.action;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

//import com.iugu.exceptions.IuguException;
import com.iugu.model.Address;
import com.iugu.model.Invoice;
import com.iugu.model.Item;
import com.iugu.model.Payer;
import com.iugu.responses.InvoiceResponse;
import com.iugu.services.InvoiceService;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Iugu;
import net.moveltrack.domain.MBoleto;

/**
 * Hello world!
 *
 */
@Named
@RequestScoped
public class IuguUtils implements Serializable
{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 2650053210971881236L;
	@Inject InvoiceService invoiceService;


	public Iugu createIuguInvoice(MBoleto mBoleto) {
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
	
	

	public void cancelIuguInvoice(MBoleto mBoleto) {
		if(mBoleto.getIugu()!=null)
			invoiceService.cancel(mBoleto.getIugu().getInvoiceId());
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

    
    public InvoiceResponse findById(String id){
    	try {
			return invoiceService.find(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	private InvoiceResponse createIuguInvoce(MBoleto mBoleto){
		try {
			Cliente cliente = mBoleto.getContrato().getCliente();
			int valor = (int)(mBoleto.getValor()*100);
			
			Invoice invoice = new Invoice("suporte@moveltrack.com.br",mBoleto.getDataVencimento(), new Item(StringUtils.isEmpty(mBoleto.getMensagem34())?"Valor a ser pago.":mBoleto.getMensagem34(),1,valor));
	    	Address address = new Address(cliente.getEndereco(),cliente.getNumero(),cliente.getMunicipio().getDescricao(),cliente.getMunicipio().getUf().getSigla(),"Brasil",cliente.getCep(),cliente.getBairro());
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

    
	public static void start()
    {
/*        InvoiceResponse response;
        try {
        	
        	Calendar c = Calendar.getInstance();
        	c.add(Calendar.DAY_OF_YEAR,5);
        	
        	Invoice invoice = new Invoice("suporte@moveltrack.com.br",c.getTime(), new Item("Teste 2",1,598));
        	Address address = new Address("Rua Lauro Maia","291","Fortaleza", "CE","BRASIL","60055-234");
        	invoice.setPayer(new Payer("Antonio Ivanildo","fabio.barros1974@gmail.com","28272738880","85","98765-8976",address));
        	response = new InvoiceService(iuguConfiguration).create(invoice);

        	System.out.println(response.getId());
        }
        catch (Exception e) {
        	e.printStackTrace();
        }*/
        
    }
}
