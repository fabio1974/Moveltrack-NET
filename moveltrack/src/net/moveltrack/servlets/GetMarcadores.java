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

import net.moveltrack.dao.MarcadorDao;
import net.moveltrack.dao.PoligonoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Marcador;


@WebServlet("/GetMarcadores")
public class GetMarcadores extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetMarcadores() {
        super();
    }

    @Inject VeiculoDao veiculoDao;
    @Inject PoligonoDao poligonoDao;
    @Inject MarcadorDao marcadorDao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String placa = request.getParameter("placa");
			List<Marcador> marcadores = marcadorDao.findByVeiculo(veiculoDao.findByPlaca(placa));
			if(marcadores==null)
				response.getWriter().write("{\"retorno\":\"null\"}");
			else{
				Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				
				String json = gson.toJson(marcadores);
				
				response.getWriter().write(json);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("{\"retorno\":\"null\"}");
		}
		
	}
	
	
	
	
	
}
