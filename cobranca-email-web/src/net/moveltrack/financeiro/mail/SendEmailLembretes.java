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
public class SendEmailLembretes
{

	@Inject BoletoUtils boletoUtils; 

	public boolean sendEmail(List<MBoleto> boletos, ServletContext servletContext)
	{
		Cliente cliente = boletos.get(0).getContrato().getCliente(); 
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
			message.setFrom(new InternetAddress(from,"Lembrete da Moveltrack"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
			message.setSubject("Envio de Boleto - Moveltrack SeguranÃ§a e Tecnologia");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(getConteudo(boletos), "text/html; charset=ISO-8859-1");
			multipart.addBodyPart(messageBodyPart);

			for (MBoleto mBoleto : boletos) {
				byte[] bs = boletoUtils.getBoletoIuguInBytes(mBoleto,servletContext);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String vencimento = sdf.format(mBoleto.getDataVencimento());
				String fileName = mBoleto.getContrato().getCliente().getNome()+"_Venc-"+vencimento+".pdf";
				attach(multipart,bs,StringUtils.replace(fileName," ",""));
			}
			message.setContent(multipart );
			Transport.send(message);
			System.out.println("Email de lembrete enviado para "+cliente.getNome()+ " e-mail:"+cliente.getEmail()+ " nÂº de boletos:"+boletos.size());
			return true;
		}catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}


	private static String getConteudo(List<MBoleto> boletos) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String cliente = boletos.get(0).getContrato().getCliente().getNome();
		StringBuffer mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", para sua comodidade, segue boleto para pagamento, com vencimento em:</p>");
		if(boletos.size()>1)
			mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", para sua comodidade, seguem os boletos para pagamento, com vencimentos em:</p>");
		for (MBoleto boleto : boletos){
			String valor = Moeda.mascaraDinheiro(boleto.getValor(),Moeda.DINHEIRO_REAL);
			mensagem.append("<p><b>"+sdf.format(boleto.getDataVencimento())+"</b>, no valor de <b>"+valor+"</b>;</p>");
		}	
		mensagem.append("<br>");
		if(boletos.size()>1)
			mensagem.append("<p>Estes boletos podem ser pagos em toda a rede bancária até o vencimento, ou por meio eletrônico.</b></p>");
		else
			mensagem.append("<p>Este boleto pode ser pago em toda a rede bancária até o vencimento, ou por meio eletrônico.</b></p>");

		if(boletos.size()>1)
			mensagem.append("<p>Caso estes boletos já tenham sido pagos, desconsidere esta mensagem.</p>");
		else	
			mensagem.append("<p>Caso este boleto já tenha sido pago, desconsidere esta mensagem.</p>");

		mensagem.append("<p>Este e-mail é automático e não recebe respostas.</b></p>");
		mensagem.append("<p>Por favor, em caso de dúvida, ligue para <b>4105-0145</b>.</p>");
		mensagem.append("<p>Cordialmente, Dep. Financeiro</b>.</p>");
		mensagem.append("<p><b>Moveltrack Segurança & Tecnologia.</b></p>");	
		return mensagem.toString();
	}


	/*	public static byte[] getPdfBytesFromFatura(Boleto boleto){
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