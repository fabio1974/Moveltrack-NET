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
import net.moveltrack.domain.RelatorioFrota;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioDestinoBean extends Report implements Serializable {
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
			list = reportDao.getRelatorioDestino(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioDestino(grb.getInicio(),grb.getFim());
		
		
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
		double tCargaLiquida=0;
		double tDistanciaPercorrida=0;
		
		for (RelatorioFrota relatorioFrota : list) {
			tQtdViagens += relatorioFrota.getQtdViagens();
			tQtdCidades += relatorioFrota.getQtdCidades();
			tQtdClientes += relatorioFrota.getQtdClientes();
			tValorDaCarga += relatorioFrota.getValorDaCarga();
			tPesoDaCarga += relatorioFrota.getPesoDaCarga();
			tValorDevolucao += relatorioFrota.getValorDevolucao();
			tCargaLiquida += relatorioFrota.getMediaCargaLiquida();
			
			LitrosValor lv = reportDao.getDespesasLitroPorDestino(relatorioFrota.getId(),grb.getInicio(),grb.getFim());
			relatorioFrota.setLitros(lv.litros);
			relatorioFrota.setDespesaCombustivel(lv.valor);
			relatorioFrota.setDiasViagensMedia(relatorioFrota.getDiasViagens()/relatorioFrota.getQtdViagens());
			relatorioFrota.setQtdCidadesMedia(relatorioFrota.getQtdCidades()/relatorioFrota.getQtdViagens());
			relatorioFrota.setMediaCargaLiquida((relatorioFrota.getValorDaCarga()-relatorioFrota.getDespesaCombustivel())/relatorioFrota.getQtdViagens());

			
			//relatorioFrota.setDespesaEstiva(reportDao.getDespesaEstiva(relatorioFrota.getId(),grb.getInicio(),grb.getFim()));  //id do motorista
			
			tLitros += relatorioFrota.getLitros();
			tDespesaCombustivel += relatorioFrota.getDespesaCombustivel();
			//tDespesaEstiva += relatorioFrota.getDespesaEstiva();
			try{
				tDiasViagens +=relatorioFrota.getDiasViagens();
			}catch(Exception e){
				e.printStackTrace();
			}
			tDistanciaPercorrida +=relatorioFrota.getDistanciaPercorrida();
			
			if(relatorioFrota.getLitros()>0)
				relatorioFrota.setKml(
					relatorioFrota.getDistanciaPercorrida()
					/relatorioFrota.getLitros());
			
		}
		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		parameters.put("tQtdViagens",tQtdViagens);
		parameters.put("tDiasViagens",tDiasViagens);
		parameters.put("tQtdCidades",tQtdCidades);
		parameters.put("tQtdClientes",tQtdClientes);
		parameters.put("tValorDaCarga",tValorDaCarga);
		parameters.put("tCargaLiquida",tCargaLiquida);
		parameters.put("tPesoDaCarga",tPesoDaCarga);
		parameters.put("tValorDevolucao",tValorDevolucao);
		parameters.put("tDistanciaPercorrida",tDistanciaPercorrida);
		parameters.put("tLitros",tLitros);
		parameters.put("tDespesaCombustivel",tDespesaCombustivel);
		//parameters.put("tDespesaEstiva",tDespesaEstiva);
		
		

	}

	
}