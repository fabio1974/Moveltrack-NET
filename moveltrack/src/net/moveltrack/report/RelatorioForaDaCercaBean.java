package net.moveltrack.report;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.LastLocation;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.GeoDistanceCalulator;
import net.moveltrack.util.Utils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@SessionScoped
public class RelatorioForaDaCercaBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	private Cliente cliente;
	private Cerca cerca;
	private boolean fora = true;
	private List<LastLocation> list = new ArrayList<LastLocation>();
	private List<LastLocation> listIn = null;
	private List<LastLocation> listOut = null;

	@Inject 
	LocationDao locationDao;
	
	@Inject 
	GeoEnderecoDao geoEnderecoDao;
	
	@Inject
	LoginBean loginBean;



	
	@PostConstruct
	public void init() {
		
		Calendar c = Calendar.getInstance();
		setFim(c.getTime());
		
		c.add(Calendar.DAY_OF_YEAR,-15);
		setInicio(c.getTime());
		
		if(loginBean.getPessoaLogada() instanceof Cliente){
			this.cliente  = (Cliente)loginBean.getPessoaLogada();
		}
		
		cerca = new Cerca();
		cerca.setLat1(cliente.getCerca().getLat1());
		cerca.setLon1(cliente.getCerca().getLon1());
		cerca.setRaio(120);
		
		
	}
	
	
	public void enableFields(){
		setDisabled(false);
	}
	

	public void pdf(boolean fora) throws JRException, IOException {
			setFora(fora);
			super.pdf("PDF");
	}
	
	
	
	@Override
	protected void setDesignPath() {
		designPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/relatorios/frota/"+getFileName()+".jrxml");
	}

	@Override
	protected void setParameters() {
		parameters.putAll(loginBean.getPessoaLogada().toMap());
		parameters.put("endereco",loginBean.getPessoaLogada().getEndereco()+(StringUtils.isNotBlank(loginBean.getPessoaLogada().getNumero())?(", "+loginBean.getPessoaLogada().getNumero()):""));
		parameters.put("logoFile",FacesContext.getCurrentInstance().getExternalContext().getRealPath("/assets/img/moveltrack/"+loginBean.getPessoaLogada().getLogoFile()));
	}

	@Override
	protected void setJRBeanCollectionDataSource() {
		listIn = new ArrayList<LastLocation>();
		listOut = new ArrayList<LastLocation>();
		for (LastLocation ll : list) {
			ll.setEndereco(geoEnderecoDao.getAddressFromLocation(ll.getLocation(),true));
			double distancia = GeoDistanceCalulator.haverSineDistance(cerca.getLat1(),cerca.getLon1(),ll.getLocation().getLatitude(),ll.getLocation().getLongitude());		// TODO Auto-generated method stub
			ll.setDistancia(distancia);
			if(distancia <= cerca.getRaio())
				listIn.add(ll);
			else
				listOut.add(ll);
		}
	}
	
		
	@Override
	protected void setTotalParameters() {
		if(isFora()){
			parameters.put("escopo","Fora");
			beanCollectionDataSource = new JRBeanCollectionDataSource(listOut);
		}else{
			parameters.put("escopo","Dentro");
			beanCollectionDataSource = new JRBeanCollectionDataSource(listIn);
		}	
	}

	public String weekDay(Calendar cal) {
	    return new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
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

	public List<LastLocation> getList() {
		return list;
	}

	public void setList(List<LastLocation> list) {
		this.list = list;
	}

	public boolean isFora() {
		return fora;
	}


	public void setFora(boolean fora) {
		this.fora = fora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cerca getCerca() {
		return cerca;
	}

	public void setCerca(Cerca cerca) {
		this.cerca = cerca;
	}


}