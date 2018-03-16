package net.moveltrack.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import net.moveltrack.domain.Carne;
import net.moveltrack.domain.CarneConteudo;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.MBoletoTipo;
import net.moveltrack.util.Moeda;
import net.moveltrack.util.Utils;





@Stateless
@SuppressWarnings("serial")
public class CarneConteudoDao extends DaoBean<CarneConteudo>{


	public CarneConteudoDao() { }



}
