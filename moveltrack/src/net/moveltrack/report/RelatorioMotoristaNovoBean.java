package net.moveltrack.report;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.ReportDao;
import net.moveltrack.dao.ReportDao.LitrosValor;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ConsumoPorVeiculo;
import net.moveltrack.domain.RelatorioFrota;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioMotoristaNovoBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<RelatorioFrota> list = null;
	
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
			list = reportDao.getRelatorioMotoristaNovo(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioMotoristaNovo(null,grb.getInicio(),grb.getFim());
		
		
	}
	
	class ValorLitros {
		double litros;
		double valor;
	}

	@Override
	protected void setTotalParameters() {
		
		int tQtdViagens=0;
		double tDiasViagens=0;
		int tQtdCidades=0;
		int tQtdClientes=0;
		double tValorDaCarga=0;
		int tPesoDaCarga=0;
		double tValorDevolucao = 0;
		double tLitros = 0;
		double tDespesaCombustivel=0;
		double tDespesaEstiva=0;
		double tDespesaDiaria=0;
		double tDistanciaPercorrida=0;
		
		for (RelatorioFrota relatorioFrota : list) {
			tQtdViagens += relatorioFrota.getQtdViagens();
			tQtdCidades += relatorioFrota.getQtdCidades();
			tQtdClientes += relatorioFrota.getQtdClientes();
			tValorDaCarga += relatorioFrota.getValorDaCarga();
			tPesoDaCarga += relatorioFrota.getPesoDaCarga();
			tValorDevolucao += relatorioFrota.getValorDevolucao();
			
			LitrosValor lv = reportDao.getConsumoCombustivel(relatorioFrota.getId(),grb.getInicio(),grb.getFim());
			relatorioFrota.setLitros(lv.litros);
			relatorioFrota.setDespesaCombustivel(lv.valor);
			relatorioFrota.setDespesaEstiva(reportDao.getDespesaEstiva(relatorioFrota.getId(),grb.getInicio(),grb.getFim()));  //id do motorista
			relatorioFrota.setDespesaDiaria(reportDao.getDespesaDiaria(relatorioFrota.getId(),grb.getInicio(),grb.getFim()));  //id do motorista
			
			tLitros += relatorioFrota.getLitros();
			tDespesaCombustivel += relatorioFrota.getDespesaCombustivel();
			tDespesaEstiva += relatorioFrota.getDespesaEstiva();
			tDespesaDiaria += relatorioFrota.getDespesaDiaria();
			tDiasViagens +=relatorioFrota.getDiasViagens();
			tDistanciaPercorrida +=relatorioFrota.getDistanciaPercorrida();
			
			if(relatorioFrota.getLitros()>0)
				relatorioFrota.setKml(
					relatorioFrota.getDistanciaPercorrida()
					/relatorioFrota.getLitros());
			
		}
		
		java.util.Collections.sort(list,new Comparator<RelatorioFrota>() {
			@Override
			public int compare(RelatorioFrota o1, RelatorioFrota o2) {
				if (o1.getKml() > o2.getKml())
					return 1;
				else
					return -1;
			}
		});

		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		parameters.put("tQtdViagens",tQtdViagens);
		parameters.put("tDiasViagens",tDiasViagens);
		parameters.put("tQtdCidades",tQtdCidades);
		parameters.put("tQtdClientes",tQtdClientes);
		parameters.put("tValorDaCarga",tValorDaCarga);
		parameters.put("tPesoDaCarga",tPesoDaCarga);
		parameters.put("tValorDevolucao",tValorDevolucao);
		parameters.put("tDistanciaPercorrida",tDistanciaPercorrida);
		parameters.put("tLitros",tLitros);
		parameters.put("tDespesaCombustivel",tDespesaCombustivel);
		parameters.put("tDespesaEstiva",tDespesaEstiva);
		parameters.put("tDespesaDiaria",tDespesaDiaria);
		
		

	}

	

}