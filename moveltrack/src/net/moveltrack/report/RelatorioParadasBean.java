package net.moveltrack.report;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.action.maps.MapaVeiculoBean;
import net.moveltrack.dao.ReportDao;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.RelatorioVeiculo;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioParadasBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<RelatorioVeiculo> list = null;
	
	@Inject
	MapaVeiculoBean mapaVeiculoBean;
	
	@Override
	protected void setDesignPath() {
		designPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/veiculo/"+getFileName()+".jrxml");
	}

	@Override
	protected void setParameters() {
		parameters.putAll(loginBean.getPessoaLogada().toMap());
		parameters.put("endereco",loginBean.getPessoaLogada().getEndereco()+(StringUtils.isNotBlank(loginBean.getPessoaLogada().getNumero())?(", "+loginBean.getPessoaLogada().getNumero()):""));
		parameters.put("inicio",mapaVeiculoBean.getInicio());
		parameters.put("fim",mapaVeiculoBean.getFim());
		parameters.put("logoFile",FacesContext.getCurrentInstance().getExternalContext().getRealPath("/assets/img/moveltrack/"+loginBean.getPessoaLogada().getLogoFile()));
		parameters.put("veiculo",mapaVeiculoBean.getVeiculo().getMarcaModelo()+ ", placa "+ mapaVeiculoBean.getVeiculo().getPlaca());
	}

	@Override
	protected void setJRBeanCollectionDataSource() {
		Equipamento equipamento = mapaVeiculoBean.getVeiculo().getEquipamento();
		if(equipamento.getModelo() == ModeloRastreador.SPOT_TRACE)
			list = reportDao.getRelatorioParadasSpotTrace(mapaVeiculoBean.getVeiculo(),mapaVeiculoBean.getInicio(),mapaVeiculoBean.getFim());
		else
			list = reportDao.getRelatorioParadas(mapaVeiculoBean.getVeiculo(),mapaVeiculoBean.getInicio(),mapaVeiculoBean.getFim());
	}
	


	@Override
	protected void setTotalParameters() {
		
		
		for (RelatorioVeiculo parada : list) {
			
		}
		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		
		

	}

	

}