package net.moveltrack.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.RelatorioUsoIndevido;
import net.moveltrack.domain.RelatorioUsoIndevidoParam;
import net.moveltrack.domain.Veiculo;

@Stateless
@SuppressWarnings("serial")
public class RelatorioUsoIndevidoDao extends DaoBean<RelatorioUsoIndevido>{

	public RelatorioUsoIndevidoDao() { }

	@SuppressWarnings("unchecked")
	public List<RelatorioUsoIndevido> getRelatorio(RelatorioUsoIndevidoParam ruip, Date inicio, Date fim, Veiculo veiculo) {
		
		String filtroVeiculo = veiculo==null?" ":" and v.placa='"+veiculo.getPlaca()+"' ";
		
		
		String sql = "select null as nomeMotorista, null as diaSemana, null as data, year(l.dateLocation) as ano,month(l.dateLocation) as mes, day(l.dateLocation) as dia,hour(l.dateLocation) as hora,v.placa as placa,concat(day(l.dateLocation),hour(l.dateLocation),v.id) as id "+ 
				
		" from location l" +
		" inner join equipamento e on l.imei = e.imei"+
		" inner join veiculo v on v.equipamento_id = e.id"+
	//	" left join viagem vi on vi.veiculo_id = v.id"+
	//	" left join pessoa p2 on vi.motorista_id = p2.id"+
		" inner join contrato c on v.contrato_id = c.id"+
		" inner join pessoa p on c.cliente_id = p.id"+
		
		" where l.velocidade > :velocidade and ("+
			" (weekday(l.dateLocation)=0 and (time(l.dateLocation) < :segundaInicio or time(l.dateLocation) > :segundaFim)) or"+
			" (weekday(l.dateLocation)=1 and (time(l.dateLocation) < :tercaInicio or   time(l.dateLocation) > :tercaFim)) or"+
			" (weekday(l.dateLocation)=2 and (time(l.dateLocation) < :quartaInicio or  time(l.dateLocation) > :quartaFim)) or"+
			" (weekday(l.dateLocation)=3 and (time(l.dateLocation) < :quintaInicio or  time(l.dateLocation) > :quintaFim)) or"+
			" (weekday(l.dateLocation)=4 and (time(l.dateLocation) < :sextaInicio or   time(l.dateLocation) > :sextaFim)) or"+
			" (weekday(l.dateLocation)=5 and (time(l.dateLocation) < :sabadoInicio or  time(l.dateLocation) > :sabadoFim)) or"+					
			" (weekday(l.dateLocation)=6 and (time(l.dateLocation) < :domingoInicio or time(l.dateLocation) > :domingoFim)) 	"+
		" )"+
		" and l.dateLocation > :inicio and l.dateLocation < :fim"+
		filtroVeiculo+
		" and p.id = :clienteId"+
		" group by ano,mes,dia,hora,placa,id"+
		" order by ano,mes,dia,hora,placa,id;";
		
		Query query = getEm().createNativeQuery(sql,RelatorioUsoIndevido.class);
		query.setParameter("velocidade",ruip.getVelocidade());
		
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);
		
		query.setParameter("segundaInicio",new Time(ruip.getSegundaInicio().getTime()));
		query.setParameter("segundaFim",new Time(ruip.getSegundaFim().getTime()));
		query.setParameter("tercaInicio",new Time(ruip.getTercaInicio().getTime()));
		query.setParameter("tercaFim",new Time(ruip.getTercaFim().getTime()));
		query.setParameter("quartaInicio",new Time(ruip.getQuartaInicio().getTime()));
		query.setParameter("quartaFim",new Time(ruip.getQuartaFim().getTime()));
		query.setParameter("quintaInicio",new Time(ruip.getQuintaInicio().getTime()));
		query.setParameter("quintaFim",new Time(ruip.getQuintaFim().getTime()));
		query.setParameter("sextaInicio",new Time(ruip.getSextaInicio().getTime()));
		query.setParameter("sextaFim",new Time(ruip.getSextaFim().getTime()));
		query.setParameter("sabadoInicio",new Time(ruip.getSabadoInicio().getTime()));
		query.setParameter("sabadoFim",new Time(ruip.getSabadoFim().getTime()));
		query.setParameter("domingoInicio",new Time(ruip.getDomingoInicio().getTime()));
		query.setParameter("domingoFim",new Time(ruip.getDomingoFim().getTime()));
		query.setParameter("clienteId",ruip.getCliente().getId());
		
		List<RelatorioUsoIndevido> list;
		
		try{
			list = (List<RelatorioUsoIndevido>)query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
