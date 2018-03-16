package net.moveltrack.report;

import java.io.Serializable;
import java.util.Calendar;
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
import net.moveltrack.domain.RelatorioMensalDespesa;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioMensalDespesaBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	@PostConstruct
	public void init() {
	}

	@Inject 
	ReportDao reportDao;
	
	@Inject
	LoginBean loginBean;
	
	List<RelatorioMensalDespesa> list = null;
	
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
			list = reportDao.getRelatorioMensalDespesa(cliente,grb.getInicio(),grb.getFim());
		}else
			list = reportDao.getRelatorioMensalDespesa(grb.getInicio(),grb.getFim());
		
		
	}
	
	class ValorLitros {
		double litros;
		double valor;
	}

	@Override
	protected void setTotalParameters() {
		
		double tCarga=0,tCombustivel=0,tManutencao=0,tEstivas=0,tDiarias=0,tIpva=0,tTransito=0,tTrabalhistas=0,tOutras=0,tDespesa=0;
        double pCombustivel=0,pManutencao=0,pEstivas=0,pDiarias=0,pIpva=0,pTransito=0,pTrabalhistas=0,pOutras=0,pDespesa=0;
        
        
		
		for (RelatorioMensalDespesa relatorioMensalDespesa : list) {
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,relatorioMensalDespesa.getAno());
			c.set(Calendar.MONTH,relatorioMensalDespesa.getMes()-1);
			
			relatorioMensalDespesa.setData(c.getTime());
			
			
			tCarga += relatorioMensalDespesa.getCarga();
			tCombustivel += relatorioMensalDespesa.getCombustivel();
			tManutencao += relatorioMensalDespesa.getManutencao();
			tEstivas += relatorioMensalDespesa.getEstivas();
			tDiarias += relatorioMensalDespesa.getDiarias();
			tIpva += relatorioMensalDespesa.getIpva();
			tTransito += relatorioMensalDespesa.getTransito();
			tTrabalhistas += relatorioMensalDespesa.getTrabalhistas();
			tOutras += relatorioMensalDespesa.getOutras();
			
			relatorioMensalDespesa.setDespesa(
					relatorioMensalDespesa.getCombustivel()+
					relatorioMensalDespesa.getManutencao()+
					relatorioMensalDespesa.getEstivas()+
					relatorioMensalDespesa.getDiarias()+
					relatorioMensalDespesa.getIpva()+
					relatorioMensalDespesa.getTransito()+
					relatorioMensalDespesa.getTrabalhistas()+
					relatorioMensalDespesa.getOutras());
			tDespesa += relatorioMensalDespesa.getDespesa();
		}
		
		if(tCarga!=0){
			pCombustivel = tCombustivel/tCarga;
			pManutencao = tManutencao/tCarga;
			pEstivas = tEstivas/tCarga;
			pDiarias = tDiarias/tCarga;
			pIpva = tIpva/tCarga;
			pTransito = tTransito/tCarga;
			pTrabalhistas = tTrabalhistas/tCarga;
			pOutras = tOutras/tCarga;
			pDespesa = tDespesa/tCarga;
		}	
		
		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
		
		parameters.put("tCarga",tCarga);
		parameters.put("tCombustivel",tCombustivel);
		parameters.put("tManutencao",tManutencao);
		parameters.put("tEstivas",tEstivas);
		parameters.put("tDiarias",tDiarias);
		parameters.put("tIpva",tIpva);
		parameters.put("tTransito",tTransito);
		parameters.put("tTrabalhistas",tTrabalhistas);
		parameters.put("tOutras",tOutras);
		parameters.put("tDespesa",tDespesa);
		
		
		parameters.put("pCombustivel",pCombustivel);
		parameters.put("pManutencao",pManutencao);
		parameters.put("pEstivas",pEstivas);
		parameters.put("pDiarias",pDiarias);
		parameters.put("pIpva",pIpva);
		parameters.put("pTransito",pTransito);
		parameters.put("pTrabalhistas",pTrabalhistas);
		parameters.put("pOutras",pOutras);
		parameters.put("pDespesa",pDespesa);

	}

}