package net.moveltrack.backgroundtasks;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.action.BloqueioBean;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.PoligonoDao;
import net.moveltrack.dao.SmsDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.dao.VerticeDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsTipo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Vertice;
import net.moveltrack.financeiro.mail.SendEmailCliente;
import net.moveltrack.firebase.FirebaseController;
import net.moveltrack.util.PolyUtil;
import net.moveltrack.util.Task;
import net.moveltrack.util.Utils;


@ApplicationScoped
public class TaskCerca extends Task {
	
		public static double PI = 3.14159265;
		public static double TWOPI = 2*PI;
		Map<String,Location2> previousLocation = new HashMap<String,Location2>();
		Map<String,Integer> previousId = new HashMap<String,Integer>();
	
		@Inject LocationDao locationDao;
		@Inject PoligonoDao poligonoDao;
		@Inject VeiculoDao veiculoDao;
		@Inject SendEmailCliente sendEmailCliente;
		@Inject BloqueioBean bloqueioBean;
		@Inject VerticeDao verticeDao;

	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskCerca() {
			
		}
	    
	    //int count = 0;
	    
	    @Override
	    public void run() {
			List<Poligono> cercasAtivas = poligonoDao.findAtivos();
			Location2 locationCorrente = null;
			for (Poligono cerca : cercasAtivas) {
				try{
						Location2 locationAnterior = previousLocation.get(cerca.getVeiculo().getPlaca());
						Integer idCercaAnterior = previousId.get(cerca.getVeiculo().getPlaca());
						
						locationCorrente = locationDao.getLastLocation2FromVeiculo(cerca.getVeiculo());
						if(locationCorrente!=null){

							List<Vertice> vertices = verticeDao.findByPoligono(cerca);
							cerca.setVertices(vertices);
							boolean inside = PolyUtil.containsLocation(locationCorrente.getLatitude(),locationCorrente.getLongitude(),cerca,true);
							locationCorrente.setAlarmType(inside?"dentro":"fora");
							System.out.println(locationCorrente.getAlarmType()+" "+cerca.getVeiculo().getPlaca()+" "+cerca.getVeiculo().getMarcaModelo());

							Cliente cliente = cerca.getVeiculo().getContrato().getCliente();

							/*if(count<10){
								sendNotification(cliente, "Saiu da Cerca Virtual"+count,"Veículo "+cerca.getVeiculo().getMarcaModelo()+", de placa "+ cerca.getVeiculo().getPlaca());
								count++;
							}	*/


							//Saiu da cerca!
							if(idCercaAnterior!=null && idCercaAnterior.intValue()==cerca.getId().intValue() && locationAnterior!=null && locationAnterior.getAlarmType().equals("dentro") && locationCorrente.getAlarmType().equals("fora")){
								if(cerca.isEnviaEmail()){
									firebaseController.sendNotification(cliente, "Saiu da Cerca Virtual!","Veiculo "+cerca.getVeiculo().getMarcaModelo()+", de placa "+ cerca.getVeiculo().getPlaca());
								}if(cerca.isBloqueia()){
									bloquear(cerca.getVeiculo());
									firebaseController.sendNotification(cliente, "Bloqueando veículo!","Veiculo "+cerca.getVeiculo().getMarcaModelo()+", de placa "+ cerca.getVeiculo().getPlaca());
								}
							}

							//Entrou na cerca!
							if(idCercaAnterior!=null && idCercaAnterior.intValue()==cerca.getId().intValue() && locationAnterior!=null && locationAnterior.getAlarmType().equals("fora") && locationCorrente.getAlarmType().equals("dentro")){
								if(cerca.isEnviaEmail()){
									firebaseController.sendNotification(cliente, "Entrou na Cerca Virtual!","Veículo "+cerca.getVeiculo().getMarcaModelo()+", de placa "+ cerca.getVeiculo().getPlaca());
								}/*if(cerca.isBloqueia()){
									bloquear(cerca.getVeiculo());
									firebaseController.sendNotification(cliente, "Bloqueando veículo!","Veículo "+cerca.getVeiculo().getMarcaModelo()+", de placa "+ cerca.getVeiculo().getPlaca());
								}*/
							}
							previousLocation.put(cerca.getVeiculo().getPlaca(),locationCorrente);
							previousId.put(cerca.getVeiculo().getPlaca(),cerca.getId());
						}	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	    }
	    
	    

		@Inject SmsDao smsDao;

	    public void bloquear(Veiculo veiculo){
	    	try {
	    	    Sms sms = new Sms();
			    sms.setCelular("+55"+veiculo.getEquipamento().getChip().getNumero().replaceAll("[^0-9]",""));
			    sms.setImei(veiculo.getEquipamento().getImei());
			    sms.setMensagem(Utils.getComandoBloqueia(veiculo.getEquipamento()));
			    sms.setTipo(SmsTipo.BLOQUEIO);
			    sms.setStatus(Utils.getStatusParaBloqueio(veiculo.getEquipamento()));
			    sms.setDataUltimaAtualizacao(new Date());
			    smsDao.salvar(sms);				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }


		@Inject FirebaseController firebaseController;
	/*    public void sendNotification(Cliente cliente,String title, String text){
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
	    }*/

	    
		//sendEmailCliente.sendEmail(cliente,"MOVELTRACK: OCORRÊNCIA PARA VEICULO DE PLACA "+cerca.getVeiculo().getPlaca()+"!",getConteudoEmail("entrado na",cerca.getVeiculo(),cliente,cerca.isBloqueia()));	    
		//sendEmailCliente.sendEmail(cliente,"MOVELTRACK: OCORRÊNCIA PARA VEICULO DE PLACA "+cerca.getVeiculo().getPlaca()+"!",getConteudoEmail("saído da",cerca.getVeiculo(),cliente,cerca.isBloqueia()));
/*	    private String getConteudoEmail(String movimento,Veiculo veiculo, Cliente cliente, boolean isBloqueio) {
	    	String recado = "acabou de ser bloqueado por ter "+movimento+" cerca virtual";
	    	if(!isBloqueio)
	    		recado = "acabou de ter "+movimento+" cerca virtual";
	     	StringBuffer mensagem = new StringBuffer("<p>Prezado(a) "+cliente+", o veículo <b>"+veiculo.getMarcaModelo()+"</b>, de placa <b>"+ veiculo.getPlaca() +"</b>,  "+ recado+".</p>");
	     	mensagem.append("<p>Modifique as configurações da sua cerca, caso deseje que isso não ocorra novamente.</p>");
			return mensagem.toString();
		}
*/

  
		
	}