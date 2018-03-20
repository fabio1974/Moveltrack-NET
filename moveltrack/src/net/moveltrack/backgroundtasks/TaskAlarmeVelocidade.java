package net.moveltrack.backgroundtasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.action.BloqueioBean;
import net.moveltrack.dao.AlarmesDao;
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.PoligonoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.dao.VerticeDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Location2;
import net.moveltrack.financeiro.mail.SendEmailCliente;
import net.moveltrack.util.Task;


@ApplicationScoped
public class TaskAlarmeVelocidade extends Task {
	
		public static double PI = 3.14159265;
		public static double TWOPI = 2*PI;
		Map<String,Location2> previousLocation = new HashMap<String,Location2>();
		Map<String,Integer> previousId = new HashMap<String,Integer>();
	
		@Inject AlarmesDao alarmesDao;
		@Inject PoligonoDao poligonoDao;
		@Inject VeiculoDao veiculoDao;
		@Inject SendEmailCliente sendEmailCliente;
		@Inject BloqueioBean bloqueioBean;
		@Inject VerticeDao verticeDao;
		@Inject ClienteDao clienteDao;

	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskAlarmeVelocidade() {
			
		}
	    
	    //int count = 0;
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    
	    @Override
	    public void run() {
	    	
	    	List<Cliente> clientes = clienteDao.findAlarmeVelocidade();
	    	for (Cliente cliente : clientes) {
				
	    		System.out.println("RODANDO ALARME DE VELOCIDADE! - "+cliente.getNome());
				
				List<Object[]> arrays = alarmesDao.getExcessoDeVelocidade(cliente);
				if(!arrays.isEmpty()) {
					StringBuffer mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", detectamos alguns excessos de velocidade dos seus veículos agora ha pouco:</p>");
					for (Object[] obj : arrays) {
						
						Date data = (Date)obj[0];
						
						Double velocidade= (Double)obj[1];
						Double latitude = (Double)obj[2];
						Double longitude = (Double)obj[3];
						String placa = (String)obj[4];
						String marcaModelo = (String)obj[5];
						
						mensagem.append("<p>Data:"+ sdf.format(data)+" - Placa: "+placa+" - Marca/Modelo:"+ marcaModelo +" - Lat/Long:"+Double.toString(latitude)+","+Double.toString(longitude)+" - <b>Velocidade em Excesso:"+Double.toString(velocidade)+"km/h</b>.</p>");	
					}
					sendEmailCliente.sendEmail(cliente,"EXCESSO DE VELOCIDADE", mensagem.toString());
				}
				
				
			}
	    	
	    	
	    }
	    
	    

  
		
	}