package net.moveltrack.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.Veiculo;


@WebServlet("/GetLastLocation")
public class GetLastLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetLastLocation() {
        super();
    }


    @Inject LocationDao locationDao;
    @Inject VeiculoDao veiculoDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String placa = request.getParameter("placa");
			Veiculo veiculo = veiculoDao.findByPlaca(placa);
			Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Location2 location2 = locationDao.getLastLocation2FromVeiculo(veiculo);
			if(location2!=null)
				response.getWriter().write(gson.toJson(location2));
			else{
				Location location = locationDao.getLastLocationFromVeiculo(veiculo);
				response.getWriter().write(gson.toJson(location));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{\"retorno\":\"null\"}");
		}
		
	}
	
	
	
	
	
}
