package net.moveltrack.action.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.Location2Dao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.GeoEndereco;
import net.moveltrack.domain.LastLocation;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Mapa;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.rest.GsonExcludeStrategy;
import net.moveltrack.util.MapaUtil;


@WebServlet("/PontosFrota")
public class PontosFrota extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	List<Location> pontosMapa = new ArrayList<Location>();
	List<Location> paradas = new ArrayList<Location>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PontosFrota() {
        super();
        // TODO Auto-generated constructor stub
    }


    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        Mapa mapa = getMapa(request);
	        Gson gson  = new GsonBuilder().setExclusionStrategies(new GsonExcludeStrategy()).create();
	        String json = gson.toJson(mapa);
	        response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Inject VeiculoDao veiculoDao;
	@Inject LocationDao locationDao;
	@Inject GeoEnderecoDao geoEnderecoDao;
	
	private Mapa getMapa(HttpServletRequest request){

		Mapa mapa = new Mapa();
		List<Location> locs = null;
		String clienteId = request.getParameter("clienteId");
		if(clienteId!=null){

			locs = locationDao.getLastLocations(Integer.valueOf(clienteId));
			List<LastLocation> lastLocations = new ArrayList<LastLocation>();
			for (Location loc : locs) {
				LastLocation ll = new LastLocation();
				ll.setLocation(loc);
				Veiculo veiculo = veiculoDao.findByEquipamento(loc.getImei());
				ll.setPlaca(veiculo.getPlaca());
				ll.setMarcaModelo(veiculo.getMarcaModelo());
				ll.setVeiculoId(veiculo.getId());
				ll.setVeiculoTipo(veiculo.getTipo().name());
				ll.setModeloRastreador(veiculo.getEquipamento().getModelo().name());
				ll.setEndereco(geoEnderecoDao.getAddressFromLocation(loc, true));
				lastLocations.add(ll);
			}
			
			
		    mapa = MapaUtil.getMapa(locs);
		    mapa.setLastLocations(lastLocations);
		    
		}    
		return mapa;
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
