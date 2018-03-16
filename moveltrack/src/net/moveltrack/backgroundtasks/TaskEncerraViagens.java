package net.moveltrack.backgroundtasks;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.ViagemThreadDao;
import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;
import net.moveltrack.util.Task;



@ApplicationScoped
class TaskEncerraViagens extends Task {
	
	    @Inject
	    ViagemThreadDao viagemThreadDao;
	    
		@Inject
		LocationDao locationDao;
	    
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskEncerraViagens() {
			
		}
	    
	    @Override
	    public void run() {
			try{
				List<Viagem> viagensAtivas = viagemThreadDao.findNaoEncerradas();
				for (Viagem viagem : viagensAtivas) {
					if(viagem.getEntrouNaCerca()!=null){
						viagem.setStatus(ViagemStatus.ENCERRADA);
						viagem.setChegadaReal(viagem.getEntrouNaCerca());
						viagemThreadDao.merge(viagem);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	}