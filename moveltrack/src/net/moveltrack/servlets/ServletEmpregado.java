package net.moveltrack.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.moveltrack.dao.EmpregadoDao;

@WebServlet("/Empregados")
public class ServletEmpregado extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletEmpregado() {
        super();
    }

     @Inject
     EmpregadoDao empregadoDao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			String term = request.getParameter("term");
			System.out.println("Data from ajax call " + term);
			response.getWriter().write(new Gson().toJson(empregadoDao.findByTerm(term)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
