package net.moveltrack.financeiro.mail;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.financeiro.action.BoletoUtils;
import net.moveltrack.util.Moeda;


@Named
@Stateless
public class SendEmailCobrancas
{
	
	@Inject BoletoUtils boletoUtils;
	
   public boolean sendEmail(List<MBoleto> mBoletos,long atraso, ServletContext servletContext)
   {
	   
	  Cliente cliente = mBoletos.get(0).getContrato().getCliente(); 
	   
      String to = cliente.getEmail();
		if(to == null && cliente.getUsuario()!=null)
			to = cliente.getUsuario().getEmail();
        //to = "josefabiosousabarros@gmail.com";
      
		String from = "suporte@moveltrack.com.br";
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		//props.put("mail.debug", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("suporte@moveltrack.com.br","moveltrack02");
			}
		});
		
	try{
		 MimeMessage message = new MimeMessage(session);
		 message.setFrom(new InternetAddress(from,"Cobrança Moveltrack"));
			
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
         message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
         message.setSubject("Cobrança - Moveltrack Rastreamento");
         
         Multipart multipart = new MimeMultipart();
         BodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setContent(getConteudo(mBoletos), "text/html; charset=ISO-8859-1");
         
         multipart.addBodyPart(messageBodyPart);
         

			
         for (MBoleto boleto : mBoletos) {
			byte[] bs =  boletoUtils.getBoletoIuguInBytes(boleto,servletContext);
			if(bs!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String vencimento = sdf.format(boleto.getDataVencimento());
				String fileName = boleto.getContrato().getCliente().getNome()+"_Venc-"+vencimento+".pdf";
				attach(multipart,bs,StringUtils.replace(fileName," ",""));
			}
		 }
         
         message.setContent(multipart );
         
         Transport.send(message);
         System.out.println("Email enviado para "+cliente.getNome()+ " e-mail:"+cliente.getEmail()+ " numero de boletos:"+mBoletos.size() +" - atraso: "+atraso+" dias");
         return true;
      }catch (MessagingException mex) {
         mex.printStackTrace();
         return false;
      }catch(Exception e){
    	  e.printStackTrace();
    	  return false;
      }
   }
   
   
	private static String getConteudo(List<MBoleto> mBoletos) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String cliente = mBoletos.get(0).getContrato().getCliente().getNome();
        StringBuffer mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", consta no nosso sistema que não foi pago o boleto com vencimento em:</p>");
        if(mBoletos.size()>1)
        	mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", consta no nosso sistema que não foram pagos os boletos com vencimento em:</p>");
        
        
        for (MBoleto mBoleto: mBoletos){
        	String valor = Moeda.mascaraDinheiro(mBoleto.getValor(),Moeda.DINHEIRO_REAL);
        	mensagem.append("<p><b>"+sdf.format(mBoleto.getDataVencimento())+"</b>, no valor de <b>"+valor+"</b>;</p>");
        }
        
        
        mensagem.append("<br>");

        if(mBoletos.size()>1)
        	mensagem.append("<p>Caso estes boletos já tenham sido pagos, desconsidere esta mensagem.</p>");
        else	
        	mensagem.append("<p>Caso este boleto já tenha sido pago, desconsidere esta mensagem.</p>");
        
        mensagem.append("<p>Este e-mail é automático e não recebe respostas.</b>.</p>");
        mensagem.append("<p>Por favor, em caso de duvida, ligue para <b>4105-0145</b>.</p>");
        mensagem.append("<p>Cordialmente, Dep. Financeiro</b>.</p>");
        mensagem.append("<p><b>Moveltrack Segurança & Tecnologia.</b></p>");	
		return mensagem.toString();
	}


/*	public static byte[] getPdfBytesFromFatura(Fatura fatura){
		List<Boleto> boletos = new ArrayList<Boleto>();
	    MeuBoleto mb = new MeuBoleto();
		Boleto b = mb.gera2aVia(fatura);
		boletos.add(b);
		File templateFile = new File(ClassLoaders.getResource("/templates/BoletoTemplateSemSacadorAvalista.pdf").getFile());
		byte[] bytes = BoletoViewer.groupInOnePdfWithTemplate(boletos, templateFile);
		return bytes;
	}
*/
	
	

   private static void attach(Multipart multipart, byte[] pdfFile, String fileName) {
	   BodyPart messageBodyPart = new MimeBodyPart();
       DataSource source = new ByteArrayDataSource(pdfFile, "application/pdf");
       try {
		messageBodyPart.setDataHandler(new DataHandler(source));
	       messageBodyPart.setFileName(fileName);
	       multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
   }
}