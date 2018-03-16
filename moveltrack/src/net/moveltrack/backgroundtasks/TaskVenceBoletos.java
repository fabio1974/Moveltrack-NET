package net.moveltrack.backgroundtasks;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskVenceBoletos extends Task {
	
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskVenceBoletos() {
			
		}
	    
	    @Inject MBoletoDao mBoletoDao;

	    @Override
	    public void run() {
			try{
				mBoletoDao.venceBoletos();
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	}