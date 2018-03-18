package net.moveltrack.report;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.action.Action;
import net.moveltrack.dao.RelatorioUsoIndevidoDao;
import net.moveltrack.dao.RelatorioUsoIndevidoParamDao;
import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.RelatorioUsoIndevido;
import net.moveltrack.domain.RelatorioUsoIndevidoParam;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioUsoIndevidoBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	Cliente cliente;
	Veiculo veiculo;
	String ordem;
	
	@PostConstruct
	public void init() {
		
		Calendar c = Calendar.getInstance();
		setFim(c.getTime());
		
		//c.add(Calendar.DAY_OF_YEAR,-15);
		c.set(Calendar.DAY_OF_MONTH, 1);
		setInicio(c.getTime());
		
		if(loginBean.getPessoaLogada() instanceof Cliente){
			this.cliente  = (Cliente)loginBean.getPessoaLogada();
			ruip = relatorioUsoIndevidoParamDao.findByCliente(cliente);
			if(ruip ==null){
				buildNewRuip();
			}	
		}
	}

	

	private void buildNewRuip() {
		ruip = new RelatorioUsoIndevidoParam();
		Calendar c = Calendar.getInstance();
		c.set(0,0,0,7,0,0);
		Time inicio = new Time(c.getTimeInMillis());
		c.set(0,0,0,18,0,0);
		Time fim = new Time(c.getTimeInMillis());
		
		ruip.setCliente(cliente);

		ruip.setSegundaInicio(inicio);
		ruip.setTercaInicio(inicio);
		ruip.setQuartaInicio(inicio);
		ruip.setQuintaInicio(inicio);
		ruip.setSextaInicio(inicio);
		ruip.setSabadoInicio(inicio);
		ruip.setDomingoInicio(inicio);
		
		ruip.setSegundaFim(fim);
		ruip.setTercaFim(fim);
		ruip.setQuartaFim(fim);
		ruip.setQuintaFim(fim);
		ruip.setSextaFim(fim);
		ruip.setSabadoFim(fim);
		ruip.setDomingoFim(fim);
	}


	@Inject 
	RelatorioUsoIndevidoParamDao relatorioUsoIndevidoParamDao;
	
	@Inject 
	RelatorioUsoIndevidoDao relatorioUsoIndevidoDao;

	
	
	@Inject
	LoginBean loginBean;
	
	private RelatorioUsoIndevidoParam ruip;
	
	private Date inicio;
	private Date fim;
	
	private Boolean disabled = true;
	
	List<RelatorioUsoIndevido> list = null;
	private String action = Action.INSERT; 
	
	
	@Transactional
	public void salvar() {
		if (validaGravacao()) {
			if(ruip.getId()==null)
				relatorioUsoIndevidoParamDao.salvar(ruip);
			else 
				relatorioUsoIndevidoParamDao.merge(ruip);
			operacaoSucesso();
			setDisabled(true);
		}
	}
	
	public void enableFields(){
		setDisabled(false);
	}
	
	@Override
	public void pdf(String tipo) throws JRException, IOException {
		
		if(!disabled){
			operacaoErro("error.salvar.expedientes","none",false);
			return;
		}

		if(inicio.after(fim)){
			operacaoErro("error.ruip.inicio.maior.que.fim","inicio",false);
			return;
		}
			super.pdf(tipo);
	}
	
	private Boolean validaGravacao() {
		
		if (ruip.getDomingoInicio().after(ruip.getDomingoFim())) {
			operacaoErro("error.ruip.domingo.inicio.maior.que.fim","domingoInicio",false);
			return false;
		}
		
		if (ruip.getSegundaInicio().after(ruip.getSegundaFim())) {
			operacaoErro("error.ruip.segunda.inicio.maior.que.fim","segundaInicio",false);
			return false;
		}

		if (ruip.getTercaInicio().after(ruip.getTercaFim())) {
			operacaoErro("error.ruip.terca.inicio.maior.que.fim","tercaInicio",false);
			return false;
		}

		if (ruip.getQuartaInicio().after(ruip.getQuartaFim())) {
			operacaoErro("error.ruip.quarta.inicio.maior.que.fim","quartaInicio",false);
			return false;
		}

		if (ruip.getQuintaInicio().after(ruip.getQuintaFim())) {
			operacaoErro("error.ruip.quinta.inicio.maior.que.fim","quintaInicio",false);
			return false;
		}

		if (ruip.getSextaInicio().after(ruip.getSextaFim())) {
			operacaoErro("error.ruip.sexta.inicio.sexta.que.fim","sextaInicio",false);
			return false;
		}

		if (ruip.getSabadoInicio().after(ruip.getSabadoFim())) {
			operacaoErro("error.ruip.sabado.inicio.maior.que.fim","sabadoInicio",false);
			return false;
		}
		

		
		return true;
	}	
	
	
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
			ruip.setCliente(cliente);
			ruip.setVelocidade(10);
			list = relatorioUsoIndevidoDao.getRelatorio(ruip,inicio,fim,veiculo,ordem);
		}
	}
	
	
	@Inject 
	ViagemDao viagemDao;
		
	@Override
	protected void setTotalParameters() {
		
		for (RelatorioUsoIndevido rui : list) {
			Viagem viagem = viagemDao.findByRui(rui.getPlaca(),rui.getAno(),rui.getMes(),rui.getDia(),rui.getHora());
			if(viagem!=null)
				rui.setNomeMotorista(viagem.getMotorista().getNome());
			Calendar c = Calendar.getInstance();
			c.set(rui.getAno(),rui.getMes()-1,rui.getDia());
			rui.setData(c.getTime());
			rui.setDiaSemana(weekDay(c));
			
		}
		
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
	}

	public String weekDay(Calendar cal) {
	    return new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
	}
	

	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

	

	public Veiculo getVeiculo() {
		return veiculo;
	}



	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
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

	public RelatorioUsoIndevidoParam getRuip() {
		return ruip;
	}

	public void setRuip(RelatorioUsoIndevidoParam ruip) {
		this.ruip = ruip;
	}

	public List<RelatorioUsoIndevido> getList() {
		return list;
	}

	public void setList(List<RelatorioUsoIndevido> list) {
		this.list = list;
	}


	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}



	public String getOrdem() {
		return ordem;
	}



	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
	


}