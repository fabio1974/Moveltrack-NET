package net.moveltrack.backgroundtasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.CarneDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoGeraCarne;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.util.Task;


@ApplicationScoped
public class TaskSetaPendenciasDeCarnes extends Task  {
	
	public TaskSetaPendenciasDeCarnes() {
		
	}

	@Inject 
	CarneDao carneDao;
	
	@Inject 
	ContratoDao contratoDao;
	
	@Inject
	MBoletoDao mBoletoDao;
	
	@Override
    public void run() {
		
		System.out.println(this.getClass().getName()+" RUNNING AT " + new Date());
		List<Contrato> contratos = contratoDao.findAtivos();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH,1);
		Date daquiHaUmMes = c.getTime();
		
		for (Contrato contrato: contratos) {
			if(!contrato.isPagamentoAnual() && contrato.getMensalidade()>0){
				Date dataBase = carneDao.getDataBase(contrato);
				if(dataBase.before(daquiHaUmMes)){
					contrato.setContratoGeraCarne(ContratoGeraCarne.PENDENTE);
					contratoDao.merge(contrato);
				}
			}
		}
		System.out.println(this.getClass().getName()+" HAS FINISHED AT " + new Date());
    }
    
    
}