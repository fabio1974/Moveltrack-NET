package net.moveltrack.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.ReportDao;
import net.moveltrack.dao.ReportDao.LitrosValor;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ConsumoPorVeiculo;
import net.moveltrack.domain.ProdutividadePorVeiculo;
import net.moveltrack.domain.RelatorioFrota;
import net.moveltrack.domain.RelatorioMensalDespesa;
import net.moveltrack.security.LoginBean;


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
	@Inject	LoginBean loginBean;
	
	
	private List<Item> getChart(HttpServletRequest request){
		
		Cliente cliente  = (Cliente)loginBean.getPessoaLogada();
		
		List<Item> items = new ArrayList<Item>();
	    String tipo = request.getParameter("tipo");
		String inicioStr = request.getParameter("inicio");
		String fimStr = request.getParameter("fim");
		
		Date inicio = new Date(Long.parseLong(inicioStr));
		Date fim = new Date(Long.parseLong(fimStr));
		
		if(tipo.equals("produtividadeVeiculo")) {
			List<ProdutividadePorVeiculo> list = reportDao.getRelatorioVeiculo(cliente,inicio,fim);
			for (ProdutividadePorVeiculo obj : list) {
				Item item = new Item(obj.getPlaca(),""+obj.getQtdViagens());
				items.add(item);
			}
			orderList(items);
		}else if(tipo.equals("consumoVeiculo")) {

			List<ConsumoPorVeiculo> list = reportDao.getRelatorioConsumoVeiculo(cliente,inicio, fim);
			for (ConsumoPorVeiculo consumoPorVeiculo : list) {
				LitrosValor lv = reportDao.getDespesasLitroPorVeiculo(consumoPorVeiculo.getPlaca(),inicio,fim);
				consumoPorVeiculo.setLitros(lv.litros);
				if(consumoPorVeiculo.getLitros()>0)
					consumoPorVeiculo.setKml2(consumoPorVeiculo.getDistanciaHodometro()/consumoPorVeiculo.getLitros());
				Item item = new Item(consumoPorVeiculo.getPlaca(),""+consumoPorVeiculo.getKml2());
				items.add(item);
			}
			orderList(items);
			
			
			
		}else if(tipo.equals("produtividadeMotorista")) {
			List<RelatorioFrota> list = reportDao.getRelatorioMotorista(cliente, inicio, fim);
			for (RelatorioFrota obj : list) {
				Item item = new Item(obj.getMotorista(),""+obj.getDiasViagens());
				items.add(item);
			}
			orderList(items);
		}
		
		else if(tipo.equals("consumoMotorista")) {
			List<RelatorioFrota> list = reportDao.getRelatorioMotoristaNovo(cliente, inicio, fim);
			for (RelatorioFrota obj : list) {
				
				LitrosValor lv = reportDao.getConsumoCombustivel(obj.getId(),inicio, fim);
				obj.setLitros(lv.litros);
				if(obj.getLitros()>0)
					obj.setKml(obj.getDistanciaPercorrida()/obj.getLitros());
				
				Item item = new Item(obj.getMotorista(),""+obj.getKml());
				items.add(item);
			}
			orderList(items);
	
		
	}else if(tipo.equals("produtividadeDestino")) {
		List<RelatorioFrota> list = reportDao.getRelatorioDestino(cliente, inicio, fim);
		for (RelatorioFrota obj : list) {
			Item item = new Item(obj.getDestino(),""+obj.getValorDaCarga());
			items.add(item);
		}
		orderList(items);
		
	}else if(tipo.equals("produtividadeEstado")) {
		List<RelatorioFrota> list = reportDao.getRelatorioEstado(cliente, inicio, fim);
		for (RelatorioFrota obj : list) {
			Item item = new Item(obj.getEstado(),""+obj.getValorDaCarga());
			items.add(item);
		}
		orderList(items);
	}		
		
	else if(tipo.equals("produtividadeMes")) {
		DateFormat df = new SimpleDateFormat("YYYY - MMMM", new Locale("pt","BR"));
		List<RelatorioFrota> list = reportDao.getRelatorioMensalOperacional(cliente, inicio, fim);
		for (RelatorioFrota obj : list) {
			Item item = new Item(df.format(obj.getData()),""+obj.getValorDaCarga());
			items.add(item);
		}
	}
		
	else if(tipo.equals("despesasMes")) {
		List<RelatorioMensalDespesa> list = reportDao.getRelatorioMensalDespesa(cliente, inicio, fim);
		for (RelatorioMensalDespesa obj : list) {
			double despesas = obj.getCombustivel() + obj.getDiarias() + obj.getEstivas()  + obj.getManutencao() + obj.getIpva() + obj.getOutras() + obj.getTrabalhistas() + obj.getTransito();
			Item item = new Item(formatMonth(obj.getMes(), new Locale("pt","BR")),""+despesas);
			items.add(item);
		}
	}	
		
		

		return items;
	}
	
	public String formatMonth(int month, Locale locale) {
	    DateFormatSymbols symbols = new DateFormatSymbols(locale);
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month - 1];
	}
	
	private void orderList(List<Item> items) {
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
