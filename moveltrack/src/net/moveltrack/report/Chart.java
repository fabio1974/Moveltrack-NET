package net.moveltrack.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
import net.moveltrack.dao.ReportDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.dao.ReportDao.LitrosValor;
import net.moveltrack.domain.ConsumoPorVeiculo;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.ProdutividadePorVeiculo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.MapaUtil;


@WebServlet("/Chart")
public class Chart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        List<Item> items = getChart(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(items));
		
	}
	
	@Inject	VeiculoDao veiculoDao;
	@Inject	LocationDao locationDao;
	@Inject ReportDao reportDao;
	
	
	private List<Item> getChart(HttpServletRequest request){
		
		List<Item> items = new ArrayList<Item>();
	    String tipo = request.getParameter("tipo");
		String inicioStr = request.getParameter("inicio");
		String fimStr = request.getParameter("fim");
		
		Date inicio = new Date(Long.parseLong(inicioStr));
		Date fim = new Date(Long.parseLong(fimStr));
		
		if(tipo.equals("produtividadeVeiculo")) {
			List<ProdutividadePorVeiculo> list = reportDao.getRelatorioVeiculo(inicio,fim);
			for (ProdutividadePorVeiculo obj : list) {
				Item item = new Item(obj.getPlaca(),""+obj.getQtdViagens());
				items.add(item);
			}
		}else if(tipo.equals("consumoVeiculo")) {

			List<ConsumoPorVeiculo> list = reportDao.getRelatorioConsumoVeiculo(inicio, fim);
			for (ConsumoPorVeiculo consumoPorVeiculo : list) {
				LitrosValor lv = reportDao.getDespesasLitroPorVeiculo(consumoPorVeiculo.getPlaca(),inicio,fim);
				consumoPorVeiculo.setLitros(lv.litros);
				if(consumoPorVeiculo.getLitros()>0)
					consumoPorVeiculo.setKml2(consumoPorVeiculo.getDistanciaHodometro()/consumoPorVeiculo.getLitros());
				Item item = new Item(consumoPorVeiculo.getPlaca(),""+consumoPorVeiculo.getKml2());
				items.add(item);
			}
			
			java.util.Collections.sort(items,new Comparator<Item>() {
				@Override
				public int compare(Item o1, Item o2) {
					if (Double.valueOf(o1.value) > Double.valueOf(o2.value))
						return -1;
					else
						return 1;
				}
			});
			
		}

		return items;
	}
	
	

	
	
	class Item{
		String name;
		String value;
		public Item(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
