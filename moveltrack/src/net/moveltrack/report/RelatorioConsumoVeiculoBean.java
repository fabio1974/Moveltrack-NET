package net.moveltrack.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import net.moveltrack.dao.ReportDao;
import net.moveltrack.dao.ReportDao.LitrosValor;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ConsumoPorVeiculo;
import net.moveltrack.domain.ProdutividadePorVeiculo;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioConsumoVeiculoBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	

	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<ConsumoPorVeiculo> list = null;
	
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
			list = reportDao.getRelatorioConsumoVeiculo(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioConsumoVeiculo(grb.getInicio(),grb.getFim());

	}

	@Override
	protected void setTotalParameters() {
		int tQtdViagens=0;
		double tDistanciaPercorrida = 0;
		double tDistanciaHodometro = 0;
		double tLitros = 0;
		double tKml = 0;
		double tKml1 = 0;
		double tErro = 0;

		
		for (ConsumoPorVeiculo consumoPorVeiculo : list) {
			tQtdViagens += consumoPorVeiculo.getQtdViagens();
			tDistanciaPercorrida += consumoPorVeiculo.getDistanciaPercorrida();
			tDistanciaHodometro += consumoPorVeiculo.getDistanciaHodometro();
			
			int h = 0;
			if(consumoPorVeiculo.getPlaca().equals("OIY-8990")){
				h++;
			}
			
			LitrosValor lv = reportDao.getDespesasLitroPorVeiculo(consumoPorVeiculo.getPlaca(),grb.getInicio(),grb.getFim());
			consumoPorVeiculo.setLitros(lv.litros);
			tLitros += consumoPorVeiculo.getLitros();

			
			if(consumoPorVeiculo.getLitros()>0){
				consumoPorVeiculo.setKml(consumoPorVeiculo.getDistanciaPercorrida()/consumoPorVeiculo.getLitros());
				consumoPorVeiculo.setKml2(consumoPorVeiculo.getDistanciaHodometro()/consumoPorVeiculo.getLitros());
			}
			
			if(consumoPorVeiculo.getDistanciaHodometro()>0)
				consumoPorVeiculo.setErro(
						(consumoPorVeiculo.getDistanciaHodometro()-consumoPorVeiculo.getDistanciaPercorrida())/consumoPorVeiculo.getDistanciaHodometro()
				);
			
		}
		
		java.util.Collections.sort(list,new Comparator<ConsumoPorVeiculo>() {
			@Override
			public int compare(ConsumoPorVeiculo o1, ConsumoPorVeiculo o2) {
				if (o1.getKml2() > o2.getKml2())
					return 1;
				else
					return -1;
			}
		});

		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		if(tLitros>0){
			tKml = tDistanciaPercorrida/tLitros;
			tKml1 = tDistanciaHodometro/tLitros;
		}	
		tErro = (tDistanciaHodometro-tDistanciaPercorrida)/tDistanciaHodometro;
		
		parameters.put("tQtdViagens",tQtdViagens);
		parameters.put("tLitros",tLitros);
		parameters.put("tDistanciaPercorrida",tDistanciaPercorrida);
		parameters.put("tDistanciaHodometro",tDistanciaHodometro);
		
		parameters.put("tKml",tKml);
		parameters.put("tKml1",tKml1);
		parameters.put("tErro",tErro);
		

	}
	
	
	


	

}