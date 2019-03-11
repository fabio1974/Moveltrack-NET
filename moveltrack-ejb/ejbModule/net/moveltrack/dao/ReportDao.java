package net.moveltrack.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ConsumoPorVeiculo;
import net.moveltrack.domain.DespesaFrotaCategoria;
import net.moveltrack.domain.DespesaFrotaEspecie;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.ProdutividadePorVeiculo;
import net.moveltrack.domain.RelatorioExcessoVelocidade;
import net.moveltrack.domain.RelatorioFrota;
import net.moveltrack.domain.RelatorioMensalDespesa;
import net.moveltrack.domain.RelatorioVeiculo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.domain.Viagem;
import net.moveltrack.util.MapaUtil;
import net.moveltrack.util.Utils;

@Stateless
@SuppressWarnings("serial")
public class ReportDao extends DaoBean<Object>{

	public ReportDao() { }
	
	public List<ProdutividadePorVeiculo> getRelatorioVeiculo(Date inicio,Date fim) {
		return getRelatorioVeiculo(null,inicio,fim);
	}

	@SuppressWarnings("unchecked")
	public List<ProdutividadePorVeiculo> getRelatorioVeiculo(Cliente cliente,Date inicio,Date fim) {
		List<ProdutividadePorVeiculo> list = null;
		String sql = "select v2.id as id,v2.placa as placa,"
						+" count(*) as qtdViagens,"
						+" sum(v.qtdCidades) as qtdCidades,"
						+" sum(v.qtdClientes) as qtdClientes,"
						+" sum(v.valorDaCarga) as valorDaCarga,"
						+" sum(v.pesoDaCarga) as pesoDaCarga,"
						+" sum(v.distanciaPercorrida) as distanciaPercorrida,"
						+" 0 as litros,"
						+" 0 as kml"
						+" from viagem v "
						+ " join veiculo v2 on v.veiculo_id = v2.id"						
						+" where" 
						+ (cliente!=null?" v.cliente_id=:clienteId and":" ")
						+" v.partida >=:inicio and"
						+" v.partida <=:fim "
						+" group by placa,id  order by qtdViagens desc "; 
		
		Query query = getEm().createNativeQuery(sql,ProdutividadePorVeiculo.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		
		try{
			list = (List<ProdutividadePorVeiculo>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public List<ConsumoPorVeiculo> getRelatorioConsumoVeiculo(Date inicio,Date fim) {
		return getRelatorioConsumoVeiculo(null,inicio,fim);
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumoPorVeiculo> getRelatorioConsumoVeiculo(Cliente cliente,Date inicio,Date fim) {
		List<ConsumoPorVeiculo> list = null;
		String sql = "select v2.id as id,v2.placa as placa,v2.marcaModelo as marcaModelo,"
						+" count(*) as qtdViagens,"
						+" sum(v.distanciaPercorrida) as distanciaPercorrida,"
						+" sum(v.distanciaHodometro) as distanciaHodometro,"						
						+" 0 as litros,"
						+" 0 as erro,"
						+" 0 as kml,"
						+" 0 as kml2"						
						+" from viagem v "
						+ " join veiculo v2 on v.veiculo_id = v2.id"						
						+" where" 
						+ (cliente!=null?" v.cliente_id=:clienteId and":" ")
						+" v.partida >=:inicio and"
						+" v.partida <=:fim "
						+" group by placa,id  order by qtdViagens desc "; 
		
		Query query = getEm().createNativeQuery(sql,ConsumoPorVeiculo.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		
		try{
			list = (List<ConsumoPorVeiculo>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	
	public List<Veiculo> getRelatorioVeiculoParado(Date inicio,Date fim) {
		return getRelatorioVeiculoParado(null,inicio,fim);
	}
	
	@SuppressWarnings("unchecked")
	public List<Veiculo> getRelatorioVeiculoParado(Cliente cliente,Date inicio,Date fim) {
		List<Object[]> list = null;
		List<Veiculo> r = new ArrayList<Veiculo>();
		
		String sql = " select v3.placa, v3.marcaModelo"+ 
		" from veiculo v3 "+
		" join contrato c on v3.contrato_id = c.id"+ 
		" where  " +
		(cliente!=null?" c.cliente_id=:clienteId and":" ") +
		" v3.status = 'ATIVO' and "+
		" v3.id not in ("+
		"	select v2.id as id"+
		"	from viagem v join veiculo v2 on v.veiculo_id = v2.id"+ 
		"	where "+
		(cliente!=null?" v.cliente_id=:clienteId and":" ") +
		"	v.partida >=:inicio and"+ 
		"	v.partida <=:fim "+
		" group by id ) order by v3.placa";
		
		Query query = getEm().createNativeQuery(sql);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		
		try{
			list = (List<Object[]>)query.getResultList();
			for (Object[] obj : list) {
				Veiculo v = new Veiculo();
				v.setPlaca((String)obj[0]);
				v.setMarcaModelo((String)obj[1]);
				r.add(v);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return r;
	}


	
	
	
	
	public List<RelatorioFrota> getRelatorioMotorista(Date inicio,Date fim) {
		return getRelatorioMotorista(null,inicio,fim);
	}

	@SuppressWarnings("unchecked")
	public List<RelatorioFrota> getRelatorioMotorista(Cliente cliente,Date inicio,Date fim) {
		List<RelatorioFrota> list = null;
		String sql = "select" 
				+" 0 as ano,"
				+" 0 as mes,"
				+" null as estado,"
				+" null as data,"
				+" ' ' as destino,"
				+" p.id as id,"
				+" p.nome as motorista,"
				+" ' ' as destino,"
				+" count(*) as qtdViagens,"
				+" sum((time_to_sec(timediff(v.chegadaReal,v.partida))/(60*60*24))) as diasViagens," 
				+" sum(v.qtdCidades) as qtdCidades,"
				+" sum(v.qtdClientes) as qtdClientes,"
				+" sum(v.valorDaCarga) as valorDaCarga,"
				+" sum(v.pesoDaCarga) as pesoDaCarga,"
				+" sum(v.distanciaHodometro) as distanciaPercorrida,"
				+" 0 as despesaEstiva,"
				+" 0 as despesaTotal,"
				+" 0 as despesaDiaria,"				
				+" 0 as despesaCombustivel,"
				+" 0 as litros,"
				+" 0 as kml,"				
				+" sum(v.valorDevolucao) as valorDevolucao"

								+" from viagem v join pessoa p on v.motorista_id = p.id"
								+" where" 
								+  (cliente!=null?" v.cliente_id=:clienteId and":" ")
								+" v.partida >=:inicio and"
								+" v.partida <=:fim "
								+" group by p.id order by qtdViagens desc";						

		Query query = getEm().createNativeQuery(sql,RelatorioFrota.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<RelatorioFrota>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	
	public List<RelatorioFrota> getRelatorioMotoristaNovo(Date inicio,Date fim) {
		return getRelatorioMotorista(null,inicio,fim);
	}

	@SuppressWarnings("unchecked")
	public List<RelatorioFrota> getRelatorioMotoristaNovo(Cliente cliente,Date inicio,Date fim) {
		List<RelatorioFrota> list = null;
		String sql = "select" 
				+" 0 as ano,"
				+" 0 as mes,"
				+" null as estado,"
				+" null as data,"
				+" ' ' as destino,"
				+" p.id as id,"
				+" p.nome as motorista,"
				+" ' ' as destino,"
				+" count(*) as qtdViagens,"
				+" sum((time_to_sec(timediff(v.chegadaReal,v.partida))/(60*60*24))) as diasViagens," 
				+" sum(v.qtdCidades) as qtdCidades,"
				+" sum(v.qtdClientes) as qtdClientes,"
				+" sum(v.valorDaCarga) as valorDaCarga,"
				+" sum(v.pesoDaCarga) as pesoDaCarga,"
				+" sum(v.distanciaHodometro) as distanciaPercorrida,"
				+" 0 as despesaEstiva,"
				+" 0 as despesaDiaria,"				
				+" 0 as despesaTotal,"
				+" 0 as despesaCombustivel,"
				+" 0 as litros,"
				+" 0 as kml,"				
				+" sum(v.valorDevolucao) as valorDevolucao"

								+" from viagem v join pessoa p on v.motorista_id = p.id"
								+" where" 
								+  (cliente!=null?" v.cliente_id=:clienteId and":" ")
								+" v.partida >=:inicio and"
								+" v.partida <=:fim "
								+" group by p.id order by qtdViagens desc";						

		Query query = getEm().createNativeQuery(sql,RelatorioFrota.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<RelatorioFrota>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}	
	
	
	
	public List<Viagem> getRelatorioFrequencia(Motorista motorista,Date inicio,Date fim) {
		return getRelatorioFrequencia(null,motorista,inicio,fim);
	}
	
	@SuppressWarnings("unchecked")
	public List<Viagem> getRelatorioFrequencia(Cliente cliente,Motorista motorista,Date inicio,Date fim) {
		List<Viagem> list = new ArrayList<Viagem>();

		String sql = "select v from Viagem v where v.motorista.id ="+motorista.getId() +  " and "
		+  (cliente!=null?" v.cliente.id=:clienteId and":" ")
		+" v.partida >=:inicio and"
		+" v.partida <=:fim "
		+" order by v.partida";						

		Query query = getEm().createQuery(sql);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<Viagem>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;

	}

	
	
	public List<RelatorioFrota> getRelatorioDestino(Date inicio,Date fim) {
		return getRelatorioDestino(null,inicio,fim);
	}
	
	@SuppressWarnings("unchecked")
	public List<RelatorioFrota> getRelatorioDestino(Cliente cliente,Date inicio,Date fim) {
		List<RelatorioFrota> list = null;
		String sql = "select" 
				+" m.codigo as id,"
				+" 0 as ano,"
				+" 0 as mes,"
				+" null as data,"
				+" m.descricao as destino,"
				+" uf.sigla as estado,"
				+" count(*) as qtdViagens,"
				+" sum((time_to_sec(timediff(v.chegadaReal,v.partida))/(60*60*24))) as diasViagens," 
				+" sum(v.qtdCidades) as qtdCidades,"
				+" sum(v.qtdClientes) as qtdClientes,"
				+" sum(v.valorDaCarga) as valorDaCarga,"
				+" sum(v.pesoDaCarga) as pesoDaCarga,"
				+" sum(v.distanciaHodometro) as distanciaPercorrida,"
				+" 0 as despesaEstiva,"
				+" 0 as despesaDiaria,"				
				+" 0 as despesaTotal,"
				+" 0 as despesaCombustivel,"
				+" '' as motorista, "
				+" 0 as litros,"
				+" 0 as kml,"				
				+" sum(v.valorDevolucao) as valorDevolucao"

								+" from viagem v join municipio m on v.cidadeDestino_codigo = m.codigo join uf uf on m.uf_id = uf.id"
								+" where" 
								+  (cliente!=null?" v.cliente_id=:clienteId and":" ")
								+" v.partida >=:inicio and"
								+" v.partida <=:fim "
								+" group by m.codigo order by estado,qtdViagens desc";						

		Query query = getEm().createNativeQuery(sql,RelatorioFrota.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<RelatorioFrota>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	
	public List<RelatorioFrota> getRelatorioEstado(Date inicio,Date fim) {
		return getRelatorioDestino(null,inicio,fim);
	}
	
	@SuppressWarnings("unchecked")
	public List<RelatorioFrota> getRelatorioEstado(Cliente cliente,Date inicio,Date fim) {
		List<RelatorioFrota> list = null;
		String sql = "select" 
				+" uf.id as id,"
				+" 0 as ano,"
				+" 0 as mes,"
				+" null as data,"
				+" '' as destino,"
				+" uf.sigla as estado,"
				+" count(*) as qtdViagens,"
				+" sum((time_to_sec(timediff(v.chegadaReal,v.partida))/(60*60*24))) as diasViagens," 
				+" sum(v.qtdCidades) as qtdCidades,"
				+" sum(v.qtdClientes) as qtdClientes,"
				+" sum(v.valorDaCarga) as valorDaCarga,"
				+" sum(v.pesoDaCarga) as pesoDaCarga,"
				+" sum(v.distanciaHodometro) as distanciaPercorrida,"
				+" 0 as despesaEstiva,"
				+" 0 as despesaDiaria,"				
				+" 0 as despesaTotal,"
				+" 0 as despesaCombustivel,"
				+" '' as motorista, "
				+" 0 as litros,"
				+" 0 as kml,"				
				+" sum(v.valorDevolucao) as valorDevolucao"

								+" from viagem v join municipio m on v.cidadeDestino_codigo = m.codigo join uf uf on m.uf_id = uf.id"
								+" where" 
								+  (cliente!=null?" v.cliente_id=:clienteId and":" ")
								+" v.partida >=:inicio and"
								+" v.partida <=:fim "
								+" group by id order by qtdViagens desc";						

		Query query = getEm().createNativeQuery(sql,RelatorioFrota.class);
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<RelatorioFrota>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	

	


	public class LitrosValor{
		public double litros;
		public double valor;
	}
	
	
	public class DespesaMensal{
		private int ano;
		private int mes;
		private DespesaFrotaEspecie especie;
		private double valor;
		public int getAno() {
			return ano;
		}
		public void setAno(int ano) {
			this.ano = ano;
		}
		public int getMes() {
			return mes;
		}
		public void setMes(int mes) {
			this.mes = mes;
		}
		public DespesaFrotaEspecie getEspecie() {
			return especie;
		}
		public void setEspecie(DespesaFrotaEspecie especie) {
			this.especie = especie;
		}
		public double getValor() {
			return valor;
		}
		public void setValor(double valor) {
			this.valor = valor;
		}
	}

	
	
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
	
	
	public List<RelatorioFrota> getRelatorioMensalOperacional(Date inicio, Date fim) {
		return getRelatorioMensalOperacional(null,inicio,fim);
	}




	public List<RelatorioFrota> getRelatorioMensalOperacional(Cliente cliente, Date inicio, Date fim) {
		List<RelatorioFrota> relatoriosFrota = new ArrayList<RelatorioFrota>();

		Calendar cInicio = Calendar.getInstance();
		cInicio.setTime(inicio);
		
		Calendar cFim = Calendar.getInstance();
		cFim.setTime(fim);

		while(cInicio.before(cFim)){
			RelatorioFrota rf = new RelatorioFrota();
			rf.setAno(cInicio.get(Calendar.YEAR));
			rf.setMes(cInicio.get(Calendar.MONTH)+1);
			
			setRelatorioFrota(rf,cliente,rf.getAno(),rf.getMes());

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,rf.getAno());
			c.set(Calendar.MONTH,rf.getMes()-1);
			rf.setData(c.getTime());
			
			setDespesaCombustivelELitrosDoMes(rf,cliente,rf.getAno(),rf.getMes());
			setDespesaTotalDoMes(rf,cliente,rf.getAno(),rf.getMes());
			
			
			if(rf.getDistanciaPercorrida()>0 && rf.getLitros()>0)
				rf.setKml(rf.getDistanciaPercorrida()/rf.getLitros());
			else
				rf.setKml(0f);
			cInicio.add(Calendar.MONTH,1);

			relatoriosFrota.add(rf);
		}
		return relatoriosFrota;
	}
	

	private void setDespesaCombustivelELitrosDoMes(RelatorioFrota rf, Cliente cliente, int ano, int mes) {
		String sql = "select sum(d.valor),sum(d.litros) from despesafrota d" 
				+" join viagem v on d.viagem_id = v.id "
				+" where"
				+  (cliente!=null?" v.cliente_id=:clienteId and":" ")				
				+" month(v.partida)=:mes and year(v.partida)=:ano and "
				+" d.especie=:especie";

		Query query = getEm().createNativeQuery(sql);
		if(cliente!=null)
			query.setParameter("clienteId",cliente.getId());
		query.setParameter("mes",mes);
		query.setParameter("ano",ano);
		query.setParameter("especie",DespesaFrotaEspecie.COMBUSTIVEL.toString());
		
		Object[] obj=null;
		try{
			obj = (Object[])query.getResultList().get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{rf.setDespesaCombustivel(((Double)obj[0]).doubleValue());}catch(Exception e){}
		try{rf.setLitros(((Double)obj[1]).doubleValue());}catch(Exception e){}
	}
	
	
	
	private void setDespesaTotalDoMes(RelatorioFrota rf, Cliente cliente, int ano, int mes) {
		String sql = "select sum(d.valor) from despesafrota d" 
				+" join viagem v on d.viagem_id = v.id "
				+" where"
				+  (cliente!=null?" v.cliente_id=:clienteId and":" ")				
				+" month(v.partida)=:mes and year(v.partida)=:ano ";

		Query query = getEm().createNativeQuery(sql);
		if(cliente!=null)
			query.setParameter("clienteId",cliente.getId());
		query.setParameter("mes",mes);
		query.setParameter("ano",ano);
		try{
			Object obj = (Object)query.getSingleResult();
			if(obj!=null)
				rf.setDespesaTotal(((Double)obj).doubleValue());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
	
	

	public void setRelatorioFrota(RelatorioFrota rf,Cliente cliente,int ano, int mes) {
		String sql = "select" 
				+" :data as id,"
				+" 0 as despesaCombustivel,"
				+" null as estado,"
				+" 0 as despesaEstiva,"
				+" 0 as despesaDiaria,"
				+" 0 as despesaTotal,"
				+" '' as destino,"
				+" '' as motorista,"
				+" null as data,"
				+" count(*) as qtdViagens,"
				+" sum((time_to_sec(timediff(v.chegadaReal,v.partida))/(60*60*24))) as diasViagens," 
				+" sum(v.qtdCidades) as qtdCidades,"
				+" sum(v.qtdClientes) as qtdClientes,"
				+" sum(v.valorDaCarga) as valorDaCarga,"
				+" sum(v.pesoDaCarga) as pesoDaCarga,"
				+" sum(v.distanciaHodometro) as distanciaPercorrida,"
				+" 0 as litros,"
				+" 0 as ano,"
				+" 0 as mes,"
				+" 0 as kml,"				
				+" sum(v.valorDevolucao) as valorDevolucao"

								+" from viagem v "
								+" where" 
								+" v.partida is not null and "
								+  (cliente!=null?" v.cliente_id=:clienteId and":" ")
								+" month(v.partida)=:mes and year(v.partida)=:ano"
								+" order by qtdViagens desc";

		Query query = getEm().createNativeQuery(sql,RelatorioFrota.class);
		
		
		query.setParameter("data",Integer.valueOf(ano+""+mes));
		if(cliente!=null)
			query.setParameter("clienteId", cliente.getId());
		query.setParameter("mes",mes);
		query.setParameter("ano",ano);
		try{
			RelatorioFrota rfAux = (RelatorioFrota)query.getSingleResult();
			if(rfAux!=null){
 	
					rf.setQtdViagens(rfAux.getQtdViagens());
					rf.setDiasViagens(rfAux.getDiasViagens());
					rf.setQtdCidades(rfAux.getQtdCidades());
					rf.setQtdClientes(rfAux.getQtdClientes());
					rf.setValorDaCarga(rfAux.getValorDaCarga());
					rf.setPesoDaCarga(rfAux.getPesoDaCarga());
					rf.setDistanciaPercorrida(rfAux.getDistanciaPercorrida());
					rf.setValorDevolucao(rfAux.getValorDevolucao());
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	
	
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------
	




	public List<RelatorioMensalDespesa> getRelatorioMensalDespesa(Date inicio,Date fim){
		return getRelatorioMensalDespesa(null,inicio,fim);
	}
	
	
	public List<RelatorioMensalDespesa> getRelatorioMensalDespesa(Cliente cliente,Date inicio,Date fim) {
		
		List<RelatorioMensalDespesa> relatorioDespesas = new ArrayList<RelatorioMensalDespesa>();

		Calendar cInicio = Calendar.getInstance();
		cInicio.setTime(inicio);
		
		Calendar cFim = Calendar.getInstance();
		cFim.setTime(fim);
		
		while(cInicio.before(cFim)){
			RelatorioMensalDespesa rmd = new RelatorioMensalDespesa();
			rmd.setAno(cInicio.get(Calendar.YEAR));
			rmd.setMes(cInicio.get(Calendar.MONTH)+1);
			setDespesasDoMes(rmd,cliente,rmd.getAno(),rmd.getMes());
			setCargasDoMes(rmd,cliente,rmd.getAno(),rmd.getMes());
			cInicio.add(Calendar.MONTH,1);
			relatorioDespesas.add(rmd);
		}
		return relatorioDespesas;
	}
	
	
	private void setCargasDoMes(RelatorioMensalDespesa rmd ,Cliente cliente,int ano,int mes){
		String sql = "select sum(valorDaCarga) from viagem" 
						+" where"
						+  (cliente!=null?" cliente_id=:clienteId and":" ")				
						+" month(partida)=:mes and year(partida)=:ano";

		Query query = getEm().createNativeQuery(sql);
		if(cliente!=null)
			query.setParameter("clienteId",cliente.getId());
		query.setParameter("mes",mes);
		query.setParameter("ano",ano);
		try{
			double valor = 0f;
			Object obj = (Object)query.getSingleResult();
			if(obj!=null)
				valor = ((Double)obj).doubleValue();
			rmd.setCarga(valor);
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	

	private void setDespesasDoMes(RelatorioMensalDespesa rmd ,Cliente cliente,int ano,int mes){
		
		String sql = "select d.especie,sum(d.valor) from despesafrota d"
				  +" join viagem v on d.viagem_id = v.id "
						+" where"
						+  (cliente!=null?" v.cliente_id=:clienteId and":" ")				
						+" month(v.partida)=:mes and year(v.partida)=:ano"
						+" group by d.especie";
		
		String sql2 = "select d.especie,sum(d.valor) from despesafrota d"
						+" where"
						+  (cliente!=null?" d.cliente_id=:clienteId and":" ")				
						+" month(d.dataDaDespesa)=:mes and year(d.dataDaDespesa)=:ano"
						+" group by d.especie";

		

		Query query = getEm().createNativeQuery(sql2);
		if(cliente!=null)
			query.setParameter("clienteId",cliente.getId());
		query.setParameter("mes",mes);
		query.setParameter("ano",ano);
		try{
			@SuppressWarnings("unchecked")
			List<Object[]> objs = (List<Object[]> )query.getResultList();
			if(objs!=null)
				for (Object[] obj : objs) {
					DespesaFrotaEspecie especie = DespesaFrotaEspecie.valueOf((String)obj[0]);
					double valor = ((Double)obj[1]);
					switch (especie) {
					case COMBUSTIVEL:
						rmd.setCombustivel(valor);
						break;
					case DIARIA:
						rmd.setDiarias(valor);
						break;
					case ESTIVA:
						rmd.setEstivas(valor);
						break;
					case IPVA:
						rmd.setIpva(valor);
						break;
					case LICENCIAMENTO:
						rmd.setTransito(rmd.getTransito()+valor);
						break;
					case MANUTENCAO:
						rmd.setManutencao(valor);
						break;
					case MULTA_DE_TRANSITO:
						rmd.setTransito(rmd.getTransito()+valor);
						break;
					case OUTROS:
						rmd.setOutras(valor);
						break;
					case SEGURO_OBRIGATORIO:
						rmd.setTransito(rmd.getTransito()+valor);
						break;
					case TRABALHISTAS:
						rmd.setTrabalhistas(valor);
						break;
					default:
						break;
					}
				}
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
		
	
	
	
	

	public LitrosValor getConsumoCombustivel(Integer motoristaId,Date inicio,Date fim) {
		Object[] obj = null;
		String sql = "select sum(d.litros), sum(d.valor)" 
						+" from viagem v join despesafrota as d on d.viagem_id = v.id"
						+" where" 
						+" v.motorista_id =:motoristaId and" 
						+" d.categoria =:categoria and"
						+" d.especie =:especie and"
						+" v.partida >=:inicio and"
						+" v.partida <=:fim ";
		
		if(motoristaId==12)
			System.out.println("peguei");
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("categoria",DespesaFrotaCategoria.VIAGEM.name());
		query.setParameter("especie",DespesaFrotaEspecie.COMBUSTIVEL.name());
		query.setParameter("motoristaId",motoristaId);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			obj = (Object[])query.getResultList().get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		LitrosValor lv = new LitrosValor();
		
		try{lv.litros = ((Double)obj[0]).doubleValue();}catch(Exception e){}
		try{lv.valor = ((Double)obj[1]).doubleValue();}catch(Exception e){}
		
		return lv;
	}
	
	
	
	public LitrosValor getDespesasLitroPorDestino(Integer cidadeDestinoCodigo,Date inicio,Date fim) {
		Object[] obj = null;
		String sql = "select sum(d.litros), sum(d.valor)" 
						+" from viagem v join despesafrota as d on d.viagem_id = v.id"
						+" where" 
						+" v.cidadeDestino_codigo =:cidadeDestinoCodigo and" 
						+" d.categoria =:categoria and"
						+" v.partida >=:inicio and"
						+" v.partida <=:fim ";
		
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("categoria",DespesaFrotaCategoria.VIAGEM.name());
		query.setParameter("cidadeDestinoCodigo",cidadeDestinoCodigo);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			obj = (Object[])query.getResultList().get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		LitrosValor lv = new LitrosValor();
		
		try{
			lv.litros = ((Double)obj[0]).doubleValue();
		}catch(Exception e){}
		try{
			lv.valor = ((Double)obj[1]).doubleValue();
		}catch(Exception e){}
		
		return lv;
	}
	
	
	public LitrosValor getDespesasLitroPorEstado(Integer ufId,Date inicio,Date fim) {
		Object[] obj = null;
		String sql = "select sum(d.litros), sum(d.valor)" 
						+" from viagem v "
						+ " join despesafrota as d on d.viagem_id = v.id"
						+ " join municipio m on v.cidadeDestino_codigo = m.codigo join uf uf on m.uf_id = uf.id "
						+" where" 
						+" uf.id =:ufId and" 
						+" d.categoria =:categoria and"
						+" v.partida >=:inicio and"
						+" v.partida <=:fim ";
		
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("categoria",DespesaFrotaCategoria.VIAGEM.name());
		query.setParameter("ufId",ufId);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			obj = (Object[])query.getResultList().get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		LitrosValor lv = new LitrosValor();
		
		try{
			lv.litros = ((Double)obj[0]).doubleValue();
		}catch(Exception e){}
		try{
			lv.valor = ((Double)obj[1]).doubleValue();
		}catch(Exception e){}
		
		return lv;
	}	
	
	
	public LitrosValor getDespesasLitroPorVeiculo(String placa,Date inicio,Date fim) {
		
		int m = 0;
		if(placa.equals("HPY-7668"))
			m = m +1;
		
		LitrosValor lvResult = new LitrosValor();
		List<Object[]> list = new ArrayList<Object[]>();

		String sql =     " (select sum(d.litros), sum(d.valor) , v.placa as placa"
						+" from veiculo v join despesafrota as d on d.veiculo_id = v.id"
						+" where placa =:placa"
					    +" and d.dataDaDespesa >=:inicio" 
 						+" and d.dataDaDespesa <=:fim)"
						+" union"
						
						+" (select sum(d.litros), sum(d.valor), ve.placa as placa"
						+" from viagem vi join despesafrota as d on d.viagem_id = vi.id"						
						+" join veiculo as ve on ve.id = vi.veiculo_id"
						+" where placa =:placa"
						+" and vi.partida >=:inicio" 
 						+" and vi.partida <=:fim)"
						 
						+"order by placa";
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("placa",placa);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			list = (List<Object[]>)query.getResultList();
			for (Object[] obj : list) {
				try{
					lvResult.litros += ((Double)obj[0]);
				}catch(Exception e){}
				try{
					lvResult.valor += ((Double)obj[1]);
				}catch(Exception e){}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return lvResult;
	}

	
	private LitrosValor converToLitrosValor(Object[] obj){
		LitrosValor lv = new LitrosValor();
		
		return lv;
	}
	
	
	public double getDespesaEstiva(Integer motoristaId,Date inicio,Date fim) {
		double valor = 0;
		String sql = "select sum(d.valor)" 
						+" from viagem v join despesafrota as d on d.viagem_id = v.id"
						+" where" 
						+" v.motorista_id =:motoristaId and" 
						+" d.categoria =:categoria and"
						+" d.especie =:especie and"
						+" v.partida >=:inicio and"
						+" v.partida <=:fim ";
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("categoria",DespesaFrotaCategoria.VIAGEM.name());
		query.setParameter("especie",DespesaFrotaEspecie.ESTIVA.name());
		query.setParameter("motoristaId",motoristaId);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			valor = ((Double)query.getSingleResult()).doubleValue();
		}catch(Exception e){
			System.err.println("Não trouxe estivas :"+e.getMessage());
		}
		return valor;
	}
	
	
	public double getDespesaDiaria(Integer motoristaId,Date inicio,Date fim) {
		double valor = 0;
		String sql = "select sum(d.valor)" 
						+" from viagem v join despesafrota as d on d.viagem_id = v.id"
						+" where" 
						+" v.motorista_id =:motoristaId and" 
						+" d.categoria =:categoria and"
						+" d.especie =:especie and"
						+" v.partida >=:inicio and"
						+" v.partida <=:fim ";
		
		Query query = getEm().createNativeQuery(sql);
		query.setParameter("categoria",DespesaFrotaCategoria.VIAGEM.name());
		query.setParameter("especie",DespesaFrotaEspecie.DIARIA.name());
		query.setParameter("motoristaId",motoristaId);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		try{
			valor = ((Double)query.getSingleResult()).doubleValue();
		}catch(Exception e){
			System.err.println("Não trouxe estivas :"+e.getMessage());
		}
		return valor;
	}




	@Inject 
	LocationDao locationDao;
	
	@Inject 
	GeoEnderecoDao geoEnderecoDao;
	
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	public List<RelatorioVeiculo> getRelatorioParadas(Veiculo veiculo, Date inicio, Date fim) {
		List<RelatorioVeiculo> lista = new ArrayList<RelatorioVeiculo>();
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationDao.getLocationsFromVeiculo(veiculo,inicio,fim);
		Location previous = locationDao.getPreviousLocation(!pontosCrus.isEmpty()?MapaUtil.getLocationFromObject(pontosCrus.get(0)):null);
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus,inicio,fim,previous,veiculo.getEquipamento());

		int count=0;
		
		for (Location loc : pontosOtimizados) {
			if(Utils.isParada(loc)){
				count++;
				RelatorioVeiculo rv = new RelatorioVeiculo();
				rv.setSituacao("P-"+ String.format("%02d",count));
				rv.setEndereco(geoEnderecoDao.getAddressFromLocation(loc,true));
				rv.setChegada(loc.getDateLocationInicio());
				rv.setSaida(loc.getDateLocation());
				long diff = rv.getSaida().getTime() - rv.getChegada().getTime();
				rv.setPermanencia(Utils.convertMillisecondsToTimeString(diff));
				rv.setLatitude(loc.getLatitude());
				rv.setLongitude(loc.getLongitude());
				lista.add(rv);
			}
		}
		return lista;
	}
	
/*	
	public List<RelatorioVeiculo> getRelatorioParadasSpotTrace2(Veiculo veiculo, Date inicio, Date fim) {
		List<RelatorioVeiculo> lista = new ArrayList<RelatorioVeiculo>();
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationDao.getLocationsFromVeiculo(veiculo,inicio,fim);
		
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus,inicio,fim);

		int count=0;
		Date chegada = null;
		Date saida = null;
		
		Location stopBegin = null;
		Location loc = null;
		
		int index = 0;
		for (Object obj : pontosCrus) {
			
			loc = MapaUtil.getLocationFromObject(obj);
			
			if(loc.getComando().equals("STOP") && stopBegin==null){
				stopBegin = loc;
				
			}else if(loc.getComando().equals("NEWMOVEMENT")){
				if(index ==0 && stopBegin == null) {
					chegada = inicio;
				}else if(stopBegin!=null){
					chegada = stopBegin.getDateLocation();
				}
				saida = loc.getDateLocation();
				count++;
				RelatorioVeiculo rv = buildParada(count, loc, chegada, saida);
				lista.add(rv);
				stopBegin = null;
			}else {
				
				if(stopBegin!=null && !loc.getComando().equals("STOP")) {
					chegada = stopBegin.getDateLocation();
					saida = loc.getDateLocation();
					count++;
					RelatorioVeiculo rv = buildParada(count, loc, chegada, saida);
					lista.add(rv);
					stopBegin = null;
				}
			}
			index++;
		}
		
		if(stopBegin!=null) {
			chegada = loc.getDateLocation();
			Date now = new Date();
			saida = fim.before(now)?fim:now;
			count++;
			RelatorioVeiculo rv = buildParada(count, loc, chegada, saida);
			lista.add(rv);
			stopBegin = null;
		}
		
		return lista;
	}
*/
	
	
	private RelatorioVeiculo buildParada(int count, Location loc, Date chegada, Date saida) {
		RelatorioVeiculo rv = new RelatorioVeiculo();
		rv.setSituacao("P-"+ String.format("%02d",count));
		rv.setEndereco(geoEnderecoDao.getAddressFromLocation(loc,true));
		
		rv.setChegada(chegada);
		rv.setSaida(saida);
		
		long diff = rv.getSaida().getTime() - rv.getChegada().getTime();
		rv.setPermanencia(Utils.convertMillisecondsToTimeString(diff));
		rv.setLatitude(loc.getLatitude());
		rv.setLongitude(loc.getLongitude());
		return rv;
	}
	




	public List<RelatorioVeiculo> getRelatorioPercurso(Veiculo veiculo, Date inicio, Date fim, Integer passo) {
		
		if(veiculo.getEquipamento().getModelo() == ModeloRastreador.SPOT_TRACE)
			passo =  0;
		
		List<RelatorioVeiculo> lista = new ArrayList<RelatorioVeiculo>();
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationDao.getLocationsFromVeiculo(veiculo,inicio,fim);
		Location previous = locationDao.getPreviousLocation(!pontosCrus.isEmpty()?MapaUtil.getLocationFromObject(pontosCrus.get(0)):null);	
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus,inicio,fim,previous,veiculo.getEquipamento());

		int count=0;
		
		Location lastLoc = null;
		
		for (Location loc : pontosOtimizados) {
			
				RelatorioVeiculo rv = new RelatorioVeiculo();
				
				rv.setChegada(loc.getDateLocationInicio());
				rv.setSaida(loc.getDateLocation());
				
				if(Utils.isParada(loc)){
					count++;
					rv.setSituacao("P-"+ String.format("%02d",count));
					long diff = rv.getSaida().getTime() - rv.getChegada().getTime();
					rv.setPermanencia(Utils.convertMillisecondsToTimeString(diff));
					rv.setLatitude(loc.getLatitude());
					rv.setLongitude(loc.getLongitude());
					rv.setEndereco(geoEnderecoDao.getAddressFromLocation(loc,true));
					lista.add(rv);
					lastLoc = loc;

				}else if(loc.getVelocidade()>0){
					
					if(lastLoc!=null){
						long diff = loc.getDateLocationInicio().getTime() - lastLoc.getDateLocation().getTime();
						diff = diff/(60*1000);
						if(diff>passo){
							rv.setSituacao("Movendo");
							rv.setPermanencia("");
							rv.setVelocidade(loc.getVelocidade());
							rv.setLatitude(loc.getLatitude());
							rv.setLongitude(loc.getLongitude());
							rv.setEndereco(geoEnderecoDao.getAddressFromLocation(loc,true));
							lista.add(rv);
							lastLoc = loc;
						}
							
					}else{
						rv.setSituacao("Movendo");
						rv.setPermanencia("");
						rv.setVelocidade(loc.getVelocidade());
						rv.setLatitude(loc.getLatitude());
						rv.setLongitude(loc.getLongitude());
						rv.setEndereco(geoEnderecoDao.getAddressFromLocation(loc,true));
						lista.add(rv);
						lastLoc = loc;
					}
				}
		}
		return lista;
	}

	public List<RelatorioExcessoVelocidade> getRelatorioExcessoVelocidade(Cliente cliente, Integer velocidade, Date inicio, Date fim) {
		List<RelatorioExcessoVelocidade> list  = locationDao.getExcessoVelocidadeList(cliente, velocidade, inicio, fim);
		return list;
	}

	
	
	


	



}
