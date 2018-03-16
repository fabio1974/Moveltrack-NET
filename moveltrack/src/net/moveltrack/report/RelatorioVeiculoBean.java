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
import net.moveltrack.dao.ReportDao.LitrosValor;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ProdutividadePorVeiculo;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioVeiculoBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	

	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<ProdutividadePorVeiculo> list = null;
	
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
			list = reportDao.getRelatorioVeiculo(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioVeiculo(grb.getInicio(),grb.getFim());

	}

	@Override
	protected void setTotalParameters() {
		int tQtdViagens=0;
		int tQtdCidades=0;
		int tQtdClientes=0;
		double tValorDaCarga=0;
		int tPesoDaCarga=0;
		double tDistanciaPercorrida = 0;
		double tLitros = 0;

		
		for (ProdutividadePorVeiculo produtividadePorVeiculo : list) {
			tQtdViagens += produtividadePorVeiculo.getQtdViagens();
			tQtdCidades += produtividadePorVeiculo.getQtdCidades();
			tQtdClientes += produtividadePorVeiculo.getQtdClientes();
			tValorDaCarga += produtividadePorVeiculo.getValorDaCarga();
			tPesoDaCarga += produtividadePorVeiculo.getPesoDaCarga();
			tDistanciaPercorrida += produtividadePorVeiculo.getDistanciaPercorrida();
			
			LitrosValor lv = reportDao.getDespesasLitroPorVeiculo(produtividadePorVeiculo.getPlaca(),grb.getInicio(),grb.getFim());
			produtividadePorVeiculo.setLitros(lv.litros);
			//produtividadePorVeiculo.setDespesaCombustivel(lv.valor);
			
			tLitros += produtividadePorVeiculo.getLitros();
			//tDespesaCombustivel += produtividadePorVeiculo.getDespesaCombustivel();

			tDistanciaPercorrida +=produtividadePorVeiculo.getDistanciaPercorrida();
			
			if(produtividadePorVeiculo.getLitros()>0)
				produtividadePorVeiculo.setKml(
						produtividadePorVeiculo.getDistanciaPercorrida()
					/produtividadePorVeiculo.getLitros());
		}
		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		
		parameters.put("tQtdViagens",tQtdViagens);
		parameters.put("tQtdCidades",tQtdCidades);
		parameters.put("tQtdClientes",tQtdClientes);
		parameters.put("tValorDaCarga",tValorDaCarga);
		parameters.put("tPesoDaCarga",tPesoDaCarga);
		parameters.put("tLitros",tLitros);
		parameters.put("tDistanciaPercorrida",tDistanciaPercorrida);

	}
	
	
	


	

}