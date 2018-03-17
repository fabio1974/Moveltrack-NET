package net.moveltrack.firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;

import net.moveltrack.dao.FirebaseDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Firebase;
import net.moveltrack.financeiro.mail.SendEmailCliente;

@Named
@Stateless
public class FirebaseController {
  
	private static final String SERVER_KEY = "AIzaSyBanvEhbNRkg1WvEo7pmrTW2gjNAHuLac4";

	public static String sendMessage(FirebaseMessage message) {
		try {
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestMethod("POST");
			urlc.setDoOutput(true);
			urlc.addRequestProperty("Content-Type", "application/json");
			urlc.addRequestProperty("Authorization", "key=" + SERVER_KEY);
			String jsonMessage =  new Gson().toJson(message);
			System.out.println(jsonMessage);
			urlc.getOutputStream().write(jsonMessage.getBytes("UTF-8"));
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
			while (br.ready())
				System.out.println(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Inject FirebaseDao firebaseDao;
	@Inject SendEmailCliente sendEmailCliente;
	
	public void sendNotification(Cliente cliente,String title, String text){
		
		
		
    	Firebase firebase = firebaseDao.findByCliente(cliente);
    	if(firebase!=null){
    		System.out.println("Enviando notificação para "+cliente.getNome());
    		FirebaseNotification notification = new FirebaseNotification(title,text);
    		FirebaseMessage message = new FirebaseMessage();
    		message.setNotification(notification);
    		message.setTo(firebase.getToken());
    		message.setPriority("high");
    		FirebaseController.sendMessage(message);
    	}
    	
    	sendEmailCliente.sendNotification(cliente,title,text);  //Aproveita e ,manda um e-mail para o cliente.
    	
    }
	
	
	@Inject UsuarioDao usuarioDao;
	
	public void sendNotification(String nomeUsuario,String title, String text){
    	Firebase firebase = firebaseDao.findByUsuario(usuarioDao.findByNomeUsuario(nomeUsuario));
    	if(firebase!=null){
    		System.out.println("Enviando notificação para "+nomeUsuario);
    		FirebaseNotification notification = new FirebaseNotification(title,text);
    		FirebaseMessage message = new FirebaseMessage();
    		message.setNotification(notification);
    		message.setTo(firebase.getToken());
    		message.setPriority("high");
    		FirebaseController.sendMessage(message);
    	}	
    }

	
 
	
	
}