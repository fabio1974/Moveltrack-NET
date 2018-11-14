package net.moveltrack.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.DistanciaDiariaDao;
import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.RelatorioDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.DistanciaDiaria;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.pojos.RelatorioDistanciaPercorrida;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.GeoDistanceCalulator;
import net.moveltrack.util.MapaUtil;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@RequestScoped
public class RelatorioDistanciaPercorridaBean extends Report implements Serializable {
	private static final long serialVersionUID = -1;
	
	Date inicio;
	Date fim;
	Veiculo veiculo;
	String orderby;
	List<RelatorioDistanciaPercorrida> list = new ArrayList<RelatorioDistanciaPercorrida>();
	List<Object[]> objs = null;
	Double distanciaTotal = 0d;
	
	@PostConstruct
	public void init() {
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.DAY_OF_MONTH)>1)
			c.add(Calendar.DAY_OF_YEAR,-1);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		setFim(c.getTime());

		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		setInicio(c.getTime());
		
	}
	

	@Inject 
	RelatorioDao relatorioDao;
	
	@Inject
	LoginBean loginBean;
	

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
	
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
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
			atualizaDistanciasDeHojeDoCliente(cliente);
			objs = relatorioDao.getRelatorioDistanciaPercorrida(cliente,veiculo,inicio,fim,orderby);
		}else
			list = null;
	}
	
	@Inject GeoEnderecoDao geoEnderecoDao;

	@Override
	protected void setTotalParameters() {
		RelatorioDistanciaPercorrida rdp = null;
		for (Object[] obj : objs) {
			rdp = new RelatorioDistanciaPercorrida();
			rdp.setPlaca((String)obj[0]);
			rdp.setMarcaModelo((String)obj[1]);
			rdp.setDistancia((Double)obj[2]);
			list.add(rdp);
			distanciaTotal += rdp.getDistancia();
		}
		parameters.put("distanciaTotal",distanciaTotal);
		beanCollectionDataSource = new JRBeanCollectionDataSource(list);
	}
	
	
	@Inject VeiculoDao veiculoDao;
	@Inject LocationDao locationDao;
	@Inject DistanciaDiariaDao distanciaDiariaDao;
	
	private void atualizaDistanciasDeHojeDoCliente(Cliente cliente) {
		
		Calendar inicioHoje = Calendar.getInstance();
		inicioHoje.set(Calendar.HOUR_OF_DAY,0);
		inicioHoje.set(Calendar.MINUTE,0);
		inicioHoje.set(Calendar.SECOND,0);
		
		if(fim.before(inicioHoje.getTime()))
			return;

		
		Calendar fimHoje = Calendar.getInstance();
		fimHoje.set(Calendar.HOUR_OF_DAY,23);
		fimHoje.set(Calendar.MINUTE,59);
		fimHoje.set(Calendar.SECOND,59);
		
		if(veiculo!=null) {
			insereDistanciaDeHojeDoVeiculo(inicioHoje, fimHoje, veiculo);
		}else{

			List<Veiculo> veiculos = veiculoDao.findAtivosByCliente(cliente);
			for (Veiculo veiculo : veiculos) {
				insereDistanciaDeHojeDoVeiculo(inicioHoje, fimHoje, veiculo);
			}
			
		}
	}
	
	

	private void insereDistanciaDeHojeDoVeiculo(Calendar inicioHoje, Calendar fimHoje, Veiculo veiculo) {
		List<Object> objs = locationDao.getLocationsFromVeiculo(veiculo,inicioHoje.getTime(),fimHoje.getTime());
			
		if(!objs.isEmpty()) {
				Location previous = locationDao.getPreviousLocation(MapaUtil.getLocationFromObject(objs.get(0)));
				List<Location> locs = MapaUtil.otimizaPontosDoBanco(objs,inicioHoje.getTime(),fimHoje.getTime(),previous,veiculo.getEquipamento());
				DistanciaDiaria dd = new DistanciaDiaria();
				double distance = getDistance(locs);
				dd.setDataComputada(inicioHoje.getTime());
				dd.setVeiculo(veiculo);
				dd.setDistanciaPercorrida(distance/1.06d);
				
				DistanciaDiaria dd2 = distanciaDiariaDao.findByVeiculoAndDataComputada(veiculo,dd.getDataComputada());
				if(dd2!=null) {
					dd2.setDistanciaPercorrida(dd.getDistanciaPercorrida());
					distanciaDiariaDao.merge(dd2);
				}else
					distanciaDiariaDao.salvar(dd);
		}
	}
	
	
	public double getDistance(List<Location> locs){
		double distance = 0;
		for (int i = 0; i < locs.size()-1; i++) {
			distance = distance + GeoDistanceCalulator.haverSineDistance(locs.get(i+1),locs.get(i)); 
		}
		return distance;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}