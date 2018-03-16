package net.moveltrack.servlets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.moveltrack.dao.PoligonoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Vertice;
import net.moveltrack.util.MapFeatures;
import net.moveltrack.util.MapaUtil;


@WebServlet("/SalvaPoligono")
public class ServletSalvaPoligono extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletSalvaPoligono() {
        super();
    }

    @Inject VeiculoDao veiculoDao;
    @Inject PoligonoDao poligonoDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String poligonoStr = request.getParameter("poligono");
			Gson gson = new Gson();
			Poligono poligono = gson.fromJson(poligonoStr,Poligono.class);
			poligono.setVeiculo(veiculoDao.findByPlaca(poligono.getPlaca()));
			
			MapFeatures mp = MapaUtil.getPoligonFeatures(poligono.getVertices());
			
			poligono.setCentroLat(mp.center.y);
			poligono.setCentroLng(mp.center.x);
			poligono.setZoom(mp.zoom);
			
			poligonoDao.removePoligonoByVeiculo(poligono.getVeiculo());
			poligonoDao.salvaPoligono(poligono);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String placa = request.getParameter("placa");
			poligonoDao.removePoligonoByVeiculo(veiculoDao.findByPlaca(placa));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{\"retorno\":\"null\"}");
		}
		
	}
	
	
	
	
	
}
