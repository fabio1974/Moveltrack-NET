package net.moveltrack.report;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.ReportDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioVeiculoParadoBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	

	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<Veiculo> list = null;
	
	@Override
	protected void setDesignPath() {
		designPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/frota/"+getFileName()+".jrxml");
	}

	@Override
	protected void setParameters() {
		parameters.putAll(loginBean.getPessoaLogada().toMap());
		parameters.put("endereco",loginBean.getPessoaLogada().getEndereco()+(StringUtils.isNotBlank(loginBean.getPessoaLogada().getNumero())?(", "+loginBean.getPessoaLogada().getNumero()):""));
		parameters.put("inicio",grb.getInicio());
		parameters.put("fim",grb.getFim());
		parameters.put("logoFile",FacesContext.getCurrentInstance().getExternalContext().getRealPath("/assets/img/moveltrack/"+loginBean.getPessoaLogada().getLogoFile()));
	}

	@Override
	protected void setJRBeanCollectionDataSource() {
		if(loginBean.getPessoaLogada() instanceof Cliente) {
			Cliente cliente  = (Cliente)loginBean.getPessoaLogada();
			list = reportDao.getRelatorioVeiculoParado(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioVeiculoParado(grb.getInicio(),grb.getFim());
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);

	}

	@Override
	protected void setTotalParameters() {
	}
	
	
	


	

}