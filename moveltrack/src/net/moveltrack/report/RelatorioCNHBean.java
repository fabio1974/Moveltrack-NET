package net.moveltrack.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.dao.ReportDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Frequencia;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Viagem;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.Utils;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioCNHBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject	ReportDao reportDao;
	@Inject MotoristaDao motoristaDao;
	@Inject	LoginBean loginBean;

	List<Motorista> list = null;

	
	@Override
	protected void setDesignPath() {
		designPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/frota/"+getFileName()+".jrxml");
	}

	@Override
	protected void setParameters() {
		parameters.putAll(loginBean.getPessoaLogada().toMap());
		parameters.put("endereco",loginBean.getPessoaLogada().getEndereco()+(StringUtils.isNotBlank(loginBean.getPessoaLogada().getNumero())?(", "+loginBean.getPessoaLogada().getNumero()):""));
		parameters.put("fimExpiracao",grb.getFimExpiracao());
		parameters.put("logoFile",FacesContext.getCurrentInstance().getExternalContext().getRealPath("/assets/img/moveltrack/"+loginBean.getPessoaLogada().getLogoFile()));
	}

	
	
	
	@Override
	protected void setJRBeanCollectionDataSource() {
		if(loginBean.getPessoaLogada() instanceof Cliente) {
			Cliente cliente  = (Cliente)loginBean.getPessoaLogada();
			list = motoristaDao.getRelatorioCNH(cliente,grb.getFimExpiracao());
		}
	}
	

	@Override
	protected void setTotalParameters() {
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
	}

	

}