package net.moveltrack.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import net.moveltrack.domain.Carne;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.MBoletoTipo;
import net.moveltrack.domain.TipoDeCobranca;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.Utils;





@Stateless
@SuppressWarnings("serial")
public class CarneDao extends DaoBean<Carne>{


	public CarneDao() { }

	public Date getDataBase(Contrato contrato) {
		
		Date dataBase = null;
		Query query = getEm().createQuery("select max(o.dataVencimento) from MBoleto o where o.tipo=:tipo and o.contrato.id=:contratoId");
		query.setParameter("tipo",MBoletoTipo.MENSAL);
		query.setParameter("contratoId",contrato.getId());
		try{
			dataBase = (Date)query.getSingleResult();
			if(dataBase==null)
				dataBase = contrato.getInicio(); 
		}catch(Exception e){
			dataBase = contrato.getInicio();
			System.out.println(e.getMessage());
		}
		return dataBase;
	}
	
	
	public Date getPrimeiroVencimento(Contrato contrato) {
		Date dataBase = getDataBase(contrato);
		Date primeiroVencimento;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataBase);
		c1.set(Calendar.DAY_OF_MONTH,contrato.getDiaVencimento());
		primeiroVencimento = c1.getTime();
		
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DAY_OF_YEAR,15);
		Date daquiHaQuinzeDias = c2.getTime();
		
		//primeiro vencimento tem que ser maior que a data base e maior que o dia de hoje + 15 dias
		while(!primeiroVencimento.after(daquiHaQuinzeDias) || !primeiroVencimento.after(dataBase)){
			c1.add(Calendar.MONTH,1);
			primeiroVencimento = c1.getTime();
		}
		return primeiroVencimento;
	}
	
	
	@Inject
	MBoletoDao mBoletoDao;
	
	@Inject
	RemessaDao remessaDao;
	
	@Inject 
	VeiculoDao veiculoDao;
	
	@Inject ConfigDao configDao;
	
	@Inject CarneDao carneDao;



	public Date geraBoletos(Carne carne, Empregado emissor) {
		Calendar c = Calendar.getInstance();
		c.setTime(carne.getDataVencimentoInicio());
		long qtdeVeiculosAtivos = veiculoDao.getVeiculosAtivosCount(carne.getContrato());
		Date vencimento = null;
		for(int i=0; i < carne.getQuantidadeBoleto();i++){
			vencimento = c.getTime();
			criaMBoleto(i,vencimento,carne,emissor,qtdeVeiculosAtivos);
			c.add(Calendar.MONTH,1);
		}
		return vencimento;
	}


	private void criaMBoleto(int i,Date vencimento, Carne carne, Empregado emissor, long qtdeVeiculosAtivos) {
		MBoleto mBoleto = new MBoleto();
		mBoleto.setContrato(carne.getContrato());
		mBoleto.setDataEmissao(new Date());
		mBoleto.setTipoDeCobranca(carne.getTipoDeCobranca());
		mBoleto.setDataVencimento(vencimento);
		mBoleto.setEmissor(emissor);
		mBoleto.setCarne(carne);
		mBoleto.setMensagem34("referente ao rastreamento mensal de "+ qtdeVeiculosAtivos +" veículo"+(qtdeVeiculosAtivos>1?"s":""));
		
		double valor = carne.getContrato().getMensalidade() * qtdeVeiculosAtivos;
		if(i==0){ //no primeiro boleto, é feito o cálculo do proporcional
			
			Date dataBase = getDataBase(carne.getContrato());
			
			Calendar v = Calendar.getInstance();
			v.setTime(vencimento);
			Calendar b = Calendar.getInstance();
			b.setTime(dataBase);
			b.add(Calendar.MONTH,1);
			
			if(b.get(Calendar.DAY_OF_YEAR)!=v.get(Calendar.DAY_OF_YEAR)){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
				double days = Utils.getTimeDiffInDays(vencimento,dataBase);
				valor = valor * (days/31);
				mBoleto.setMensagem34("Proporcional de "+Moeda.mascaraDinheiro(valor,Moeda.DINHEIRO_REAL)
				+": "+(int)days+" dia(s) de rastreamento (" +sdf.format(dataBase)+" a "+sdf.format(vencimento)+")");
			}else
				mBoleto.setMensagem34("referente ao rastreamento mensal de "+ qtdeVeiculosAtivos +" veículo"+(qtdeVeiculosAtivos>1?"s":""));
		}
			
		mBoleto.setValor(valor);
		
		mBoleto.setMulta(configDao.findById(1).getMulta());
		mBoleto.setJuros(configDao.findById(1).getJuros());
		mBoleto.setNossoNumero(mBoletoDao.getProximoNossoNumero(mBoleto.getTipoDeCobranca()));
		mBoleto.setRemessa(mBoleto.getTipoDeCobranca()==TipoDeCobranca.SEM_REGISTRO?null:remessaDao.getRemessaAtual());
		mBoleto.setSituacao(MBoletoStatus.EMITIDO);
		mBoleto.setTipo(MBoletoTipo.MENSAL);
		mBoletoDao.salvar(mBoleto);
	}


}
