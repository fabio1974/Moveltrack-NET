package net.moveltrack.backgroundtasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.moveltrack.util.OSValidator;
import net.moveltrack.util.Task;




@ApplicationScoped
public class BackgroundTaskManager implements ServletContextListener {

    private static final int MAXIMUM_CONCURRENT = 20;
    private ScheduledThreadPoolExecutor executor = null;

	@Inject TaskAtualizaViagem calculaDistanciasDeViagens;
	@Inject TaskAtualizaDistanciaDeViagensEncerradas taskAtualizaDistanciaDeViagensEncerradas;
	@Inject TaskEncerraViagens taskEncerraViagens;
	@Inject TaskVenceBoletos taskVenceBoletos;
	@Inject TaskSetaPendenciasDeCarnes taskSetaPendenciasDeCarnes;
	@Inject TaskDeletaLocations taskDeletaLocations;
	@Inject TaskCerca taskCerca;
	@Inject TaskAlarmeBateria taskAlarmeBateria;
	@Inject TaskAlarmeVelocidade taskAlarmeVelocidade;
	@Inject TaskTeste taskTeste;
	@Inject TaskGeraBoletoNoIugu taskGeraBoletoNoIugu;
	@Inject TaskCorrigeIugu taskCorrigeIugu;
	@Inject TaskDistanciaDiaria taskDistanciaDiaria;

	@PostConstruct
	public void init() {
		System.out.println(this.getClass().getName() + " ...init!");
	}
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		executor = new ScheduledThreadPoolExecutor(MAXIMUM_CONCURRENT);
		List<Task> tasks = new ArrayList<Task>();
		
		//executor.schedule(taskDistanciaDiaria,5,TimeUnit.SECONDS);
		
		taskCorrigeIugu.setInicio("01:30:00");
		taskCorrigeIugu.setPeriodo(604800);
		tasks.add(taskCorrigeIugu);
		
		taskDeletaLocations.setInicio("03:30:00");
		taskDeletaLocations.setPeriodo(86400);
		tasks.add(taskDeletaLocations);
		
		calculaDistanciasDeViagens.setIntervalo(30);  
		calculaDistanciasDeViagens.setPeriodo(300);
		tasks.add(calculaDistanciasDeViagens);
		
		taskCerca.setIntervalo(30);  
		taskCerca.setPeriodo(20);
		tasks.add(taskCerca);
		
		taskAlarmeVelocidade.setIntervalo(10);  
		taskAlarmeVelocidade.setPeriodo(30 * 60);
		tasks.add(taskAlarmeVelocidade);

		taskAlarmeBateria.setIntervalo(15*60);  
		taskAlarmeBateria.setPeriodo(30*60);
		tasks.add(taskAlarmeBateria);
		
		taskVenceBoletos.setInicio("00:01:00");
		taskVenceBoletos.setPeriodo(86400);
		tasks.add(taskVenceBoletos);
		
		taskSetaPendenciasDeCarnes.setInicio("00:05:00");
		taskSetaPendenciasDeCarnes.setPeriodo(86400);
		tasks.add(taskSetaPendenciasDeCarnes);
		
		taskEncerraViagens.setInicio("00:10:00");
		taskEncerraViagens.setPeriodo(86400);
		tasks.add(taskEncerraViagens);
		
		taskDistanciaDiaria.setInicio("00:15:00");
		taskDistanciaDiaria.setPeriodo(86400);
		tasks.add(taskDistanciaDiaria);
		
		taskGeraBoletoNoIugu.setIntervalo(20);
		taskGeraBoletoNoIugu.setPeriodo(5);
		tasks.add(taskGeraBoletoNoIugu);
		
		if(OSValidator.isUnix()){
			for (Task task : tasks) {
				executor.scheduleAtFixedRate(task,task.getIntervalo(),task.getPeriodo(),TimeUnit.SECONDS);	
			}
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		executor.shutdown();		
	}
}