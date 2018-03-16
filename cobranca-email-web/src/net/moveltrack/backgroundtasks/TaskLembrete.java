package net.moveltrack.backgroundtasks;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.financeiro.mail.SendEmailCobrancas;
import net.moveltrack.financeiro.mail.SendEmailLembretes;
import net.moveltrack.firebase.FirebaseController;
import net.moveltrack.util.OSValidator;
import net.moveltrack.util.Task;
import net.moveltrack.util.Utils;

@ApplicationScoped
public class TaskLembrete extends Task {
	
	    @Inject MBoletoDao mBoletoDao;
	    @Inject ClienteDao clienteDao;
	    @Inject SendEmailLembretes sendEmailLembretes;
	    @Inject SendEmailCobrancas sendEmailCobrancas;

		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskLembrete() {
			
		}
	    

	    @Override
	    public void run() {
			try{
				if(OSValidator.isUnix())
					lembrar();
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	    
	    
	    public void lembrar() throws Exception{

	    	List<Cliente> clientes = clienteDao.findParaLembrete();
	    	System.out.println("Trazendo "+clientes.size()+" clientes para e-mail de lembrete!");
	    	
	    	List<MBoleto> boletosAPagar,boletosALembrar=null;
	    	
	    	int count = 0;
	    	
	    	
	    	/*select distinct (p.email) from mboleto m
	    	inner join contrato c on m.contrato_id = c.id
	    	inner join pessoa p on c.cliente_id = p.id
	    	where 
	    	((m.dataVencimento >= '2017-04-20 00:00:00' and m.dataVencimento < '2017-04-21 00:00:00' and SITUACAO = 'EMITIDO') ||
	    	(m.dataVencimento >= '2017-04-25 00:00:00' and m.dataVencimento < '2017-04-26 00:00:00' and SITUACAO = 'EMITIDO'))
	    	and
	    	p.email is not null*/


	    	for (Cliente cliente : clientes) {

	    			boletosAPagar = mBoletoDao.getBoletosAPagar(cliente);
	    			if(boletosAPagar!=null && boletosAPagar.size()>0) {
	    				boletosALembrar = getBoletosALembrar(boletosAPagar);
	    				if(boletosALembrar!=null && boletosALembrar.size()>0 && cliente.getEmail()!=null && Utils.validEmail(cliente.getEmail())){ 
	    					if(sendEmailLembretes.sendEmail(boletosALembrar, getServletContext())){ 		//Envia emails de aviso de faturas emitidas a pagar em 6 dias, e 1 dia, da data atual.
		    					count++;
	    					}
	    				}
	    			}
	    	}
	    	
	    	System.out.println(count+" clientes receberam e-mail de lembrete!");
	    	fc.sendNotification("fabio", count+" clientes receberam e-mail de lembrete!", clientes.size() + " é o total de clientes do banco dados");
	    }
		
	    @Inject FirebaseController fc;
	  
		private List<MBoleto> getBoletosALembrar(List<MBoleto> faturasAPagar) {
			List<MBoleto> aLembrar  = new LinkedList<MBoleto>();
			
			for (MBoleto faturaAPagar : faturasAPagar) {
				if(isVencimentoAmanha(faturaAPagar.getDataVencimento()) || isVencimentoEmSeisDias(faturaAPagar.getDataVencimento()))
					aLembrar.add(faturaAPagar);
			}
			return aLembrar;
		}
		
		

		private boolean isVencimentoEmSeisDias(Date dataVencimento) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR,6);
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			c.set(Calendar.MILLISECOND,0);
			Date inicio = c.getTime();
			
			c.add(Calendar.DAY_OF_YEAR,1);
			c.add(Calendar.MILLISECOND,-1);
			
			Date fim = c.getTime();
			
			return inicio.getTime() <= dataVencimento.getTime() && dataVencimento.getTime() <=fim.getTime() ;
		}

		private boolean isVencimentoAmanha(Date dataVencimento) {

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR,1);
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			c.set(Calendar.MILLISECOND,0);
			Date inicio = c.getTime();
			
			c.add(Calendar.DAY_OF_YEAR,1);
			c.add(Calendar.MILLISECOND,-1);
			
			Date fim = c.getTime();
			
			return inicio.getTime() <= dataVencimento.getTime() && dataVencimento.getTime() <=fim.getTime() ;
		}
	}