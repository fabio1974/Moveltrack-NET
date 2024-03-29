package net.moveltrack.action.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import net.moveltrack.dao.GeoEnderecoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.MapaUtil;


@WebServlet("/PontosTacografo")
public class PontosTacografo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	List<Location> pontosMapa = new ArrayList<Location>();
	List<Location> paradas = new ArrayList<Location>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PontosTacografo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        List<Location> list = getPontosTacografo(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(list));
		
	}
	
	@Inject
	VeiculoDao veiculoDao;
	
	@Inject 
	LocationDao locationDao;
	
	@Inject
	MapaBean mapaBean;
	@Inject GeoEnderecoDao geoEnderecoDao;
	
	private List<Location> getPontosTacografo(HttpServletRequest request){
		
		List<Location> pontosTacografo = new ArrayList<Location>();
	    String imei = request.getParameter("imei");
		String inicioStr = request.getParameter("inicio");
		String fimStr = request.getParameter("fim");

		if(StringUtils.isNoneBlank(imei,inicioStr)){
			Date inicio = new Date(Long.parseLong(inicioStr));
			Date fim = null;
			if(StringUtils.isBlank(fimStr))
				fim = new Date();  
			else
				fim = new Date(Long.parseLong(fimStr)); 
			//for debug only. Comment it for production!
			//fim = new Date();
			//Date contaInicio = new Date();

			try{
				Veiculo veiculo = veiculoDao.findByEquipamento(imei);
				List<Object> pontosCrus = locationDao.getLocationsFromVeiculo(veiculo,inicio,fim);
				
				if(pontosCrus==null || pontosCrus.size()==0){
					pontosCrus = new ArrayList<Object>();
					pontosCrus.add(locationDao.getLastLocationFromVeiculo(veiculo));
				}	
				
				for (Object object : pontosCrus) {
					pontosTacografo.add(MapaUtil.getLocationFromObject(object));
				}
				
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		return pontosTacografo;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
