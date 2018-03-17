package net.moveltrack.action.maps;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;


@Named
@SessionScoped
public class MapaVeiculoBean extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	
	@Inject
	PessoaDao pessoaDao;

	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	LoginBean loginBean;
	

	private Veiculo veiculo;
	private Date inicio;
	private Date fim;
	
	@Inject
	private Sinal sinal;
	
	private boolean reloadMapa = false;
	private boolean reportDisabled = false;
	private String erroData;
	private String ultimoSinal;
	private boolean displayInfo = false;
	private boolean displayMenu = false;

 

	public MapaVeiculoBean() {

	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... "+this.getClass().getName());
		System.out.println("Init ... ");
		Calendar c = Calendar.getInstance();
		//c.set(Calendar.MONTH, 3);
		//c.set(Calendar.DAY_OF_MONTH, 16);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		inicio = c.getTime();
		
		Calendar c1 = Calendar.getInstance();
		//c1.set(Calendar.MONTH, 3);
		//c1.set(Calendar.DAY_OF_MONTH, 16);
		c1.set(Calendar.HOUR_OF_DAY,23);
		c1.set(Calendar.MINUTE,59);
		c1.set(Calendar.SECOND,59);
		c1.set(Calendar.MILLISECOND,0);
		fim = c1.getTime();
		
		setReloadMapa(fim.after(new Date()));
	}
	
	
	private Location center;


	@Inject LocationDao locationDao;
	@Inject GeoEnderecoDao geoEnderecoDao;

	public Location getCenter() {
		center = locationDao.getLastLocationFromVeiculo(veiculo);
		if(center == null){
			Cliente cliente = veiculo.getContrato().getCliente();
			if(cliente!=null)
				center = geoEnderecoDao.getLocationFromAddress(cliente.getBairro(),cliente.getMunicipio().getDescricao(),cliente.getMunicipio().getUf().getSigla());
		}
		if(center==null){
			center = new Location();
			center.setLatitude(-3.742117); 
			center.setLongitude(-38.523537);
		}	
		return center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}
	
	
	
	public void disableReports(){
		setReportDisabled(true);
	}
	
	public String atualizaMapa(){
		if(erroData==null){
			setReportDisabled(false);
			return "mapaVeiculoCompleto";
		}
		return null;
	}
	
	public String atualizaMapaSpot(){
		if(erroData==null){
			setReportDisabled(false);
			return "mapaVeiculoSpot";
		}
		return null;
	}

	
	


	public boolean isDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(boolean displayInfo) {
		this.displayInfo = displayInfo;
	}

	public boolean isDisplayMenu() {
		return displayMenu;
	}

	public void setDisplayMenu(boolean displayMenu) {
		this.displayMenu = displayMenu;
	}

	public boolean isReloadMapa() {
		return reloadMapa;
	}

	public void setReloadMapa(boolean reloadMapa) {
		this.reloadMapa = reloadMapa;
	}

	public String getErroData() {
		if(!inicio.before(fim))
			erroData = "Data de início deve ser anterior a data final do período!";
		else if(fim.getTime() - inicio.getTime() > 120*60*60*1000)
			erroData = "O período não pode ser supeioro a 5 dias (120 horas)";
		else
			erroData = null;
		return erroData;
	}
	
	public void setErroData(String erroData) {
		this.erroData = erroData;
	}

	
	
	public Date getNow(){
		return new Date();
	}

	public Date getInicio() {
		return inicio;
	}
	

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		setReloadMapa(fim.after(new Date()));
		this.fim = fim;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}


	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	 
	public void setVeiculoById(Integer id) {
		if(id!=null) {
			Veiculo vei = veiculoDao.findById(id);
			setVeiculo(vei);
		}
	}


	public Sinal getSinal() {
		return sinal;
	}

	public void setSinal(Sinal sinal) {
		this.sinal = sinal;
	}

	public String getUltimoSinal() {
		return ultimoSinal;
	}

	public void setUltimoSinal(String ultimoSinal) {
		this.ultimoSinal = ultimoSinal;
	}

	public boolean isReportDisabled() {
		return reportDisabled;
	}

	public void setReportDisabled(boolean reportDisabled) {
		this.reportDisabled = reportDisabled;
	}

	
	public void toggleInfo(){
		setDisplayInfo(!displayInfo);
	}
	
	public void toggleMenu(){
		setDisplayMenu(!displayMenu);
	}	
	


}