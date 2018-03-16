package net.moveltrack.servlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.moveltrack.dao.MarcadorDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Marcador;
import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Veiculo;


@WebServlet("/SalvaMarcadores")
public class ServletSalvaMarcadores extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletSalvaMarcadores() {
        super();
    }

    @Inject VeiculoDao veiculoDao;
    @Inject MarcadorDao marcadorDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String jsonString = request.getParameter("marcadores");
			String placa = request.getParameter("placa");
			Gson gson = new Gson();

			Marcador[] marcadoresArray = gson.fromJson(jsonString, Marcador[].class);
			List<Marcador> marcadores = Arrays.asList(marcadoresArray);
			
			Veiculo veiculo = veiculoDao.findByPlaca(placa);
			
			marcadorDao.removeMarcadoresByVeiculo(veiculo);
			if(marcadores!=null && marcadores.size()>0)
				marcadorDao.salvaMarcadores(marcadores,veiculo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class Position implements Serializable{
		double lat;
		double lng;
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*try {
			String placa = request.getParameter("placa");
			poligonoDao.removePoligonoByVeiculo(veiculoDao.findByPlaca(placa));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{\"retorno\":\"null\"}");
		}*/
		
	}
	
	
	
	
	
}
