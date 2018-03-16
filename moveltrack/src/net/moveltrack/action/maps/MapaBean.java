package net.moveltrack.action.maps;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.action.ConversationBaseAction;
import net.moveltrack.dao.Location2Dao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class MapaBean extends ConversationBaseAction implements Serializable{
	
	@Inject
	private Conversation conversation;
	
	@Inject
	Location2Dao location2Dao;
	
	@Inject
	LoginBean loginBean;
	
	Cerca cerca = null;
	
	private Point2D.Double center = new Double(-51.82,-13.34);
	private Veiculo veiculo; 
	private String erroData;
	private boolean atualiza = true;
	//private boolean mostraStatus = true;
	private boolean reloadMapa = false;
	
	
	private Date inicio;
	private Date fim;
	
	
	public MapaBean() {
		System.out.println("MapaBean Constructor ... ");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		inicio = c.getTime();
		
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.HOUR_OF_DAY,23);
		c1.set(Calendar.MINUTE,59);
		c1.set(Calendar.SECOND,59);
		c1.set(Calendar.MILLISECOND,0);
		fim = c1.getTime();
		//Location2 lastLocation = location2Dao.getLastLocation2FromImei(veiculo.getEquipamento().getImei());
		//if(lastLocation!=null)
			//center.setLocation(lastLocation.getLongitude(),lastLocation.getLatitude());
		
		Pessoa pessoa = loginBean.getPessoaLogada();
		if(pessoa instanceof Cliente){
			Cliente cliente =(Cliente)pessoa;
			cerca = cliente.getCerca();
		}else{
			cerca = new Cerca();
			cerca.setLat1(-3.773901d);
			cerca.setLon1(-38.52177);
			cerca.setRaio(3000);
		}
					
		
	}
	
	
	

	public boolean isReloadMapa() {
		return reloadMapa;
	}

	public void setReloadMapa(boolean reloadMapa) {
		this.reloadMapa = reloadMapa;
	}

	public boolean isMostraStatus() {
		try{
			return veiculo.getEquipamento().getModelo()==ModeloRastreador.GT06 ||
					veiculo.getEquipamento().getModelo()==ModeloRastreador.GT06N ||
							veiculo.getEquipamento().getModelo()==ModeloRastreador.TR02 ||
									veiculo.getEquipamento().getModelo()==ModeloRastreador.CRX1 ||
											veiculo.getEquipamento().getModelo()==ModeloRastreador.CRXN ||
												veiculo.getEquipamento().getModelo()==ModeloRastreador.JV200 ||
													veiculo.getEquipamento().getModelo()==ModeloRastreador.TK06;
		}catch(Exception e){
			return false;
		}
	}

/*	public void setMostraStatus(boolean mostraStatus) {
		this.mostraStatus = mostraStatus;
	}
*/
	
	
	
	public Date getInicio() {
		return inicio;
	}

	public Cerca getCerca() {
		return cerca;
	}

	public void setCerca(Cerca cerca) {
		this.cerca = cerca;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public void setFim(Date fim) {
		setReloadMapa(fim.after(new Date()));
		this.fim = fim;
	}
	
	public String getFimStr() {
		return  fim.getTime()+"";
	}

	public String getInicioStr() {
		return  inicio.getTime()+"";
	}
	
	public String getErroData() {
		if(!inicio.before(fim))
			return "Data de início deve ser anterior a data final do período!";
		if(fim.getTime() - inicio.getTime() > 120*60*60*1000)
			return "O período não pode ser supeioro a 5 dias (120 horas)"; 
		return erroData;
	}

	public void setErroData(String erroData) {
		this.erroData = erroData;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Date getFim() {
		return fim;
	}

	public Point2D.Double getCenter() {
		return center;
	}

	public void setCenter(Point2D.Double center) {
		this.center = center;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}





	public boolean isAtualiza() {
		return atualiza;
	}

	public void setAtualiza(boolean atualiza) {
		this.atualiza = atualiza;
	}


	

	
	
	
	
}
