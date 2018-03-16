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
public class TaskCobranca extends Task {
	
	    @Inject MBoletoDao mBoletoDao;
	    @Inject ClienteDao clienteDao;
	    @Inject SendEmailLembretes sendEmailLembretes;
	    @Inject SendEmailCobrancas sendEmailCobrancas;

		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskCobranca() {
			
		}
	    

	    @Override
	    public void run() {
			try{
				if(OSValidator.isUnix())
					cobrar();
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	    
	    
		public void cobrar() throws Exception{
			List<Cliente> clientes = clienteDao.findParaCobranca();
			System.out.println("Trazendo "+clientes.size()+" clientes para e-mail de cobrança!");
			List<MBoleto> boletosVencidos,boletosCobraveis=null;
			int count = 0;
			
			for (Cliente cliente : clientes) {
				if(count<148){
					boletosVencidos = mBoletoDao.findByClienteStatus(cliente,MBoletoStatus.VENCIDO);
					if(boletosVencidos!=null && boletosVencidos.size()>0) {
						boletosCobraveis = getBoletosCobraveis(cliente,boletosVencidos);

						//Envia cobrança de faturas por email
						if(!Utils.isDomingo() && boletosCobraveis!=null && boletosCobraveis.size()>0 && cliente.getEmail()!=null && Utils.validEmail(cliente.getEmail())){ 
							if(sendEmailCobrancas.sendEmail(boletosCobraveis,maiorAtraso,this.getServletContext())){
								count++;
							}
						}	
						//Envia SMS de cobrança de faturas 
						//if(Sessao.VERSAO==Sessao.PRODUCAO && !Utils.isDomingo() && boletosCobraveis!=null && boletosCobraveis.size()>0)
						//enviarSmsParaCelular(boletosCobraveis);
					}
				}
			}
			System.out.println(count+" clientes receberam e-mail de cobrança!");
			fc.sendNotification("fabio", count+" clientes receberam e-mail de cobrança!", clientes.size() + " é o total de clientes do banco dados");
			
		}
		
		@Inject FirebaseController fc;
	    
		long maiorAtraso;

		private List<MBoleto> getBoletosCobraveis(Cliente cliente,List<MBoleto> boletosVencidos) {
			List<MBoleto> cobraveis  = new LinkedList<MBoleto>();
			Calendar hoje = Calendar.getInstance();
			maiorAtraso = -1;
			for (MBoleto boletoVencido : boletosVencidos) {
				Calendar vencimento = Calendar.getInstance();
				vencimento.setTime(boletoVencido.getDataVencimento());
				long atraso = hoje.getTimeInMillis() - vencimento.getTimeInMillis();
				atraso = atraso/(86400000);
				if(atraso>maiorAtraso)
					maiorAtraso=atraso;
				if(atraso>3 && atraso%2==1)
					cobraveis.add(boletoVencido);
			}
			return cobraveis;
		}
		
	
	}