package net.moveltrack.gateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.SmsDao;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Sms;
import net.moveltrack.gateway.utils.Constantes;
import net.moveltrack.gateway.utils.Utils;


@ApplicationScoped
public class StartService implements ServletContextListener   {
	
	
	@Inject SmsDao smsDao;
	@Inject EquipamentoDao equipamentoDao;
	CommandHandler chCRX1,chGT06, chJV200,chTK106,chCRXN,chCRX3;
	boolean DEBUG_SERVICE = false;
	
	public StartService() {
		
	}

	@PostConstruct
	public void init() {
		
		Utils.log(true,"Iniciando leitura dos rastreadores");

		(new ThreadReading(Constantes.PORT_CRX1,  new ReadingProtocolHandlerCRX1() )).start();
		(new ThreadReading(Constantes.PORT_CRX3,  new ReadingProtocolHandlerCRX3() )).start();
		(new ThreadReading(Constantes.PORT_CRXN,  new ReadingProtocolHandlerCRXN() )).start();
		(new ThreadReading(Constantes.PORT_GT06,  new ReadingProtocolHandlerGT06() )).start();
		(new ThreadReading(Constantes.PORT_TR02,  new ReadingProtocolHandlerTR02() )).start();
		(new ThreadReading(Constantes.PORT_TK103, new ReadingProtocolHandlerTK103())).start();
		(new ThreadReading(Constantes.PORT_GT06_B,new ReadingProtocolHandlerGT06B())).start();
		(new ThreadReading(Constantes.PORT_XT009, new ReadingProtocolHandlerXT009())).start();
		(new ThreadReading(Constantes.PORT_JV200, new ReadingProtocolHandlerJV200())).start();
		(new ThreadReading(Constantes.PORT_TK06, new ReadingProtocolHandlerTK06())).start();
				
		chCRX1 = new CommandHandlerCRX1();
		chCRX3 = new CommandHandlerCRX3();
		chCRXN = new CommandHandlerCRXN();
		chGT06 = new CommandHandlerGT06();
		chJV200 = new CommandHandlerJV200();
		//chTK106 = new CommandHandlerTK106(); protocolo do TK106 não é igual ao do CRX1, pelo menos no 
		//que diz respeito ao comando GPRS de bloqueio/desbloqueio
		
		ThreadCommnad threadCommnad = new ThreadCommnad();
		threadCommnad.start();
		
		Utils.log(true,"Serviço Iniciado");
	}
	
	
	private class ThreadReading extends Thread{
		ReadingProtocolHandler ph;
		int port;
		
		public ThreadReading(int port,ReadingProtocolHandler ph){
			this.port = port;
			this.ph = ph;
		}
	    @Override
		public void run() {
	    	ServerSocket serverSocket = null;
			try{
				serverSocket = new ServerSocket(port);
				while(true){
					new ThreadForTrackerReading(ph,serverSocket.accept()).start();  //Uma thread iniciada para cada rastreador
				}	
			}catch (Exception e) {
				Utils.log(DEBUG_SERVICE,port+ " "+e.getMessage());
	            try {
					serverSocket.close();
				} catch (IOException e1) {
					Utils.log(DEBUG_SERVICE,e1.getMessage());
				}
			}
		}
	}	


	private class ThreadCommnad extends Thread {

		public ThreadCommnad() {
		}

		@Override
		public void run() {
			while(true) {
				try {
					
					
					List<Sms> commands = smsDao.getWaitingPowerCommands();
					if(commands!=null && commands.size()>0) {
						for (Sms sms : commands) {
							ModeloRastreador mr = equipamentoDao.findByImei(sms.getImei()).getModelo();
							switch (mr) {
							case CRX1:
								chCRX1.sendCommandToTracker(sms);
								break;
								
							case CRX3:
								chCRX3.sendCommandToTracker(sms);
								break;
								
							case CRXN:
								chCRXN.sendCommandToTracker(sms);
								break;
								
								
							case TK06:  //Por enquanto, TK06 pegando o protocolo de comando do CRX para ver se funciona
								chTK106.sendCommandToTracker(sms);
								break;	

							case GT06:
								chGT06.sendCommandToTracker(sms);
								break;

							case GT06N:
								chGT06.sendCommandToTracker(sms);
								break;
								
							case JV200:
								chJV200.sendCommandToTracker(sms);
								break;

							default:
								break;
							}
						}
					}
					Thread.sleep(5*1000);
				} catch (Exception e) {
					if(DEBUG_SERVICE)
						e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}		
}
