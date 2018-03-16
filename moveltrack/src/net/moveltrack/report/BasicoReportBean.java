package net.moveltrack.report;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Veiculo;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
public class BasicoReportBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject 
	VeiculoDao veiculoDao;
	
	@Inject
	ClienteDao clienteDao;

	@Override
	protected void setParameters() {
		
		Cliente cliente = clienteDao.findByTerm("SINCOPLEMA").get(0);
		parameters.putAll(cliente.toMap());
		parameters.put("endereco",cliente.getEndereco()+(StringUtils.isNotBlank(cliente.getNumero())?(", "+cliente.getNumero()):""));
		
		Calendar c= Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);

		parameters.put("inicio",c.getTime());
		parameters.put("fim",new Date());
		
		List<Veiculo> veiculos = veiculoDao.findAll();
		beanCollectionDataSource = new JRBeanCollectionDataSource(veiculos);
		
		//reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/viagens/basico.jrxml");
		fileName = "viagem_basico.pdf";
	}

	@Override
	protected void setDesignPath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setJRBeanCollectionDataSource() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setTotalParameters() {
		// TODO Auto-generated method stub
		
	}
	

}