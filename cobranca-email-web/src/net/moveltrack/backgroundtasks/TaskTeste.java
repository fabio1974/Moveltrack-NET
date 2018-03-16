package net.moveltrack.backgroundtasks;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.financeiro.mail.MyEmail;
import net.moveltrack.financeiro.mail.SendEmailLembretes;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskTeste extends Task  {
	
	@PostConstruct
	public void init() {
	}
	
	@Inject
	LocationDao locationDao;
	 @Inject SendEmailLembretes sendEmailLembretes;
	
	@Inject ClienteDao clienteDao;
	
	@Override
    public void run() {
		
		
		try {
			sendEmail(getServletContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
	
	static Properties mailServerProperties;
	
	
	public boolean sendEmail(ServletContext servletContext)
	{
		String to = "josefabiosousabarros@gmail.com";

		String from = "suporte@moveltrack.com.br";
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.debug", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("suporte@moveltrack.com.br","jcja1903");
			}
		});
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from,"Lembrete Moveltrack"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.addRecipient(Message.RecipientType.BCC,new InternetAddress("josefabiosousabarros@gmail.com"));
			message.setSubject("Envio de Boleto TESTE - Moveltrack Segurança e Tecnologia");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(getConteudo(), "text/html; charset=ISO-8859-1");
			multipart.addBodyPart(messageBodyPart);


			message.setContent(multipart );
			Transport.send(message);
			System.out.println("Email de teste enviado para e-mail:");
			return true;
		}catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}


	private static String getConteudo() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer mensagem = new StringBuffer("<p>Prezado(a) , para sua comodidade, segue boleto para pagamento, com vencimento em:</p>");

			mensagem = new StringBuffer("<p>Prezado(a)  para sua comodidade, seguem os boletos para pagamento, com vencimentos em:</p>");
		mensagem.append("<br>");

			mensagem.append("<p>Estes boletos podem ser pagos em toda a rede bancária até o vencimento, ou por meio eletrônico.</b></p>");

			mensagem.append("<p>Este boleto pode ser pago em toda a rede bancária até o vencimento, ou por meio eletrônico.</b></p>");

			mensagem.append("<p>Caso este boleto já tenha sido pago, desconsidere esta mensagem.</p>");

		mensagem.append("<p>Este e-mail é automático e não recebe respostas.</b></p>");
		mensagem.append("<p>Por favor, em caso de duvida, ligue para <b>4105-0145</b>.</p>");
		mensagem.append("<p>Cordialmente, Dep. Financeiro</b>.</p>");
		mensagem.append("<p><b>Moveltrack Segurança & Tecnologia.</b></p>");	
		return mensagem.toString();
	}
}