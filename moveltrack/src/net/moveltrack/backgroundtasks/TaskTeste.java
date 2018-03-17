package net.moveltrack.backgroundtasks;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.firebase.FirebaseController;
import net.moveltrack.util.IuguUtils;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskTeste extends Task  {
	
	@PostConstruct
	public void init() {
	}
	
	@Inject	LocationDao locationDao;
	@Inject FirebaseController firebaseController;
	@Inject ClienteDao clienteDao;
	
	@Override
    public void run() {
		
		Cliente cliente = clienteDao.findByNome("Demo");
		firebaseController.sendNotification(cliente, "Bloqueando veículo!","Veículo Lifan , de placa HXX4356www");
		
    }
}