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

import net.moveltrack.util.Task;




@ApplicationScoped
public class BackgroundTaskManager implements ServletContextListener {

    private static final int MAXIMUM_CONCURRENT = 6;
    public static int countEmails = 1;
    private ScheduledThreadPoolExecutor executor = null;

	@Inject TaskCobranca taskCobranca;
	@Inject TaskLembrete taskLembrete;
	@Inject TaskChecaRastreamento taskChecaRastreamento;
	@Inject TaskTeste taskTeste;

	@PostConstruct
	public void init() {
		System.out.println(this.getClass().getName() + " ...init!");
	}
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		executor = new ScheduledThreadPoolExecutor(MAXIMUM_CONCURRENT);
		List<Task> tasks = new ArrayList<Task>();
		
		//taskLembrete.setIntervalo(10);
		taskLembrete.setInicio("08:00:00");
		taskLembrete.setPeriodo(86400);
		taskLembrete.setServletContext(sce.getServletContext());
		tasks.add(taskLembrete);
		
		//taskCobranca.setIntervalo(10);
		taskCobranca.setInicio("12:00:00");
		taskCobranca.setPeriodo(86400);
		taskCobranca.setServletContext(sce.getServletContext());
		tasks.add(taskCobranca);
		
		taskChecaRastreamento.setIntervalo(10);
		taskChecaRastreamento.setPeriodo(120);
		taskChecaRastreamento.setServletContext(sce.getServletContext());
		tasks.add(taskChecaRastreamento);

		
		for (Task task : tasks) {
			executor.scheduleAtFixedRate(task,task.getIntervalo(),task.getPeriodo(),TimeUnit.SECONDS);	
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		executor.shutdown();		
	}
}