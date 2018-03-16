package net.moveltrack.gateway.utils;

import java.text.SimpleDateFormat;
import java.util.Properties;

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

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.VeiculoTipo;
import net.moveltrack.gateway.persistence.EquipamentoDAO;

@Named
public class GerenciaAlarmes
{

	static String SOS = "SOS!";
	static String BATERIA_FRACA = "BATERIA FRACA!"; 
	static String DESCONECTANDO_BATERIA = "DESCONECTANDO BATERIA!";
	static String PARADA_BRUSCA = "PARADA BRUSCA!";
	static String ALARME_NEUTRO = "ALARME NEUTRO!";
	static String ENTROU_CERCA = "ENTROU NA CERCA VIRTUAL!";
	static String SAIU_CERCA = "SAIU DA CERCA VIRTUAL!";
	
	@Inject 
	static EquipamentoDao equipamentoDao;

	public static void gerenciaAlarme(Location alarme)
	{
		Equipamento equipamento =  equipamentoDao.findByImei(alarme.getImei());
		Veiculo veiculo = EquipamentoDAO.getInstance().getVeiculoByEquipamento(equipamento.getId());
		Cliente cliente =  EquipamentoDAO.getInstance().getClienteByVeiculo(veiculo);

		if(veiculo.getTipo()==VeiculoTipo.MOTOCICLETA && (getAlarmeByCodigo(alarme).equals(DESCONECTANDO_BATERIA)||getAlarmeByCodigo(alarme).equals(BATERIA_FRACA)))
			sendEmail(alarme,equipamento,cliente,veiculo,"moveltrack@gmail.com");
		else	
			sendEmail(alarme,equipamento,cliente,veiculo,cliente.getEmailAlarme());  
	} 	  

	public static void sendEmail(Location alarme,Equipamento equipamento,Cliente cliente,Veiculo veiculo,String to){

		if(to==null)
			to = "fabio.barros1974@gmail.com";	   

		String from = "alarme@moveltrack.com.br";
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "mail.moveltrack.com.br");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		//props.put("mail.debug", "true");
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("alarme@moveltrack.com.br","mvt17547");
			}
		});
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject(getAlarmeByCodigo(alarme));

			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(getConteudo(alarme,cliente,equipamento), "text/html; charset=ISO-8859-1");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart );
			Transport.send(message);
			Utils.log(true,"Email de ALARME enviado para "+cliente.getNome()+ " e-mail:"+cliente.getEmailAlarme());
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}




	private static String getConteudo(Location alarme, Pessoa cliente, Equipamento equipamento) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		StringBuffer mensagem = new StringBuffer("<p>Prezado cliente, um alarme foi enviado nesse momento do veículo "+ EquipamentoDAO.getInstance().getVeiculoByEquipamento(equipamento.getId())+":</p>");
		mensagem.append("<br>");
		mensagem.append("<p>Tipo do Alarme: "+getAlarmeByCodigo(alarme).toUpperCase()+"</p>");
		mensagem.append("<p>Data: "+sdf.format(alarme.getDateLocation())+"h</p>");
		mensagem.append("<p>Latitude: "+alarme.getLatitude()+"</p>");
		mensagem.append("<p>Longitude: "+alarme.getLongitude()+"</p>");       	
		mensagem.append("<p>Velocidade: "+alarme.getVelocidade()+"</p>");       	
		mensagem.append("<p>Cliente: "+cliente.getNome()+"</p>");
		mensagem.append("<p>Este e-mail é automático e não recebe respostas.</b>.</p>");
		mensagem.append("<p>Por favor, em caso de dúvida, ligue para <b>4105-0145</b>.</p>");
		mensagem.append("<p><b>Moveltrack Rastreamento.</b></p>");	
		return mensagem.toString();
	}



	private static String getAlarmeByCodigo(Location loc) {
		if(loc!=null && loc.getAlarmType()!=null){
			if(loc.getAlarmType().equals("100")){
				return SOS;
			}	
			else if(loc.getAlarmType().equals("011")){
				return BATERIA_FRACA;
			}	
			else if(loc.getAlarmType().equals("010")){
				return DESCONECTANDO_BATERIA;
			}	
			else if(loc.getAlarmType().equals("001")){
				return  PARADA_BRUSCA;
			}	
			else if(loc.getAlarmType().equals("000")){
				return  ALARME_NEUTRO;
			}
			else if(loc.getAlarmType().equals("110")){
				return  ENTROU_CERCA;
			}	
			else if(loc.getAlarmType().equals("111")){
				return  SAIU_CERCA;
			}	

		}
		return null;
	}









	/*   private static void attach(Multipart multipart, byte[] pdfFile, String fileName) {
	   BodyPart messageBodyPart = new MimeBodyPart();
       DataSource source = new ByteArrayDataSource(pdfFile, "application/pdf");
       try {
		messageBodyPart.setDataHandler(new DataHandler(source));
	       messageBodyPart.setFileName(fileName);
	       multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
   }*/
}