package net.moveltrack.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.ReportDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Frequencia;
import net.moveltrack.domain.RelatorioExcessoVelocidade;
import net.moveltrack.domain.Viagem;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.Utils;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioExcessoVelocidadeBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	protected Date inicio;
	protected Date fim;
	
	@PostConstruct
	public void init() {
		Calendar c = Calendar.getInstance();
		setFim(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		//c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		setInicio(c.getTime());
		setVelocidade(110);
		
		
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	private Integer velocidade;

	public Integer getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(Integer velocidade) {
		this.velocidade = velocidade;
	}
	
	

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	List<Integer> velocidades = null;
	List<RelatorioExcessoVelocidade> list = null;
	
	@Override
	protected void setDesignPath() {
		designPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/frota/"+getFileName()+".jrxml");
	}

	@Override
	protected void setParameters() {
		parameters.putAll(loginBean.getPessoaLogada().toMap());
		parameters.put("endereco",loginBean.getPessoaLogada().getEndereco()+(StringUtils.isNotBlank(loginBean.getPessoaLogada().getNumero())?(", "+loginBean.getPessoaLogada().getNumero()):""));
		parameters.put("inicio",inicio);
		parameters.put("fim",fim);
		parameters.put("logoFile",FacesContext.getCurrentInstance().getExternalContext().getRealPath("/assets/img/moveltrack/"+loginBean.getPessoaLogada().getLogoFile()));
	}

	@Override
	protected void setJRBeanCollectionDataSource() {
	
		if(loginBean.getPessoaLogada() instanceof Cliente) {
			Cliente cliente  = (Cliente)loginBean.getPessoaLogada();
			list = reportDao.getRelatorioExcessoVelocidade(cliente,velocidade,inicio,fim);
		}else
			list = null;// reportDao.getRelatorioExcessoVelocidadeFrequencia(null,motorista,grb.getInicio(),grb.getFim());
	}
	
	@Inject GeoEnderecoDao geoEnderecoDao;

	@Override
	protected void setTotalParameters() {
		int i = 0;
		for (RelatorioExcessoVelocidade rev : list) {
			i++;
			if(i<1000)
				rev.setEndereco(geoEnderecoDao.getAddressFromLocation(rev.getLatitude(),rev.getLongitude(),true));
		}
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
	}
}