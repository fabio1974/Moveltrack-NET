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
public class RelatorioFrequenciaBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	private Motorista motorista;
	
	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	
	

	List<Viagem> list = null;
	List<Frequencia> frequencias = null;
	
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
			list = reportDao.getRelatorioFrequencia(cliente,motorista,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioFrequencia(null,motorista,grb.getInicio(),grb.getFim());
		
		
	}
	
	class ValorLitros {
		double litros;
		double valor;
	}

	@Override
	protected void setTotalParameters() {
		double tHoras=0;
		int tDias=0;
		frequencias = new ArrayList<Frequencia>();
		for (Viagem viagem : list) {
			Frequencia frequencia = new Frequencia();
			frequencia.setViagem(viagem);
			Date now = new Date();
			Date inicio = viagem.getPartida();
			Date fim = viagem.getChegadaReal()==null?now:viagem.getChegadaReal();
			if(fim.after(grb.getFim()))
				fim = grb.getFim();
			
			double horas = Utils.getTimeDiffInHours(fim,viagem.getPartida());
			int dias = (int)Math.ceil(Utils.getTimeDiffInDays(fim,viagem.getPartida()));
			
			
			tHoras+=horas;
			tDias+=dias;
			frequencia.setHoras(horas);
			frequencia.setDias(dias);
			frequencias.add(frequencia);
		}
		beanCollectionDataSource = new JRBeanCollectionDataSource(frequencias);
		parameters.put("tDias",tDias);
		parameters.put("tHoras",tHoras);
	}

	

}