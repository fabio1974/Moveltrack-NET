package com.moveltrack.cartaoprograma.rest.api.repository.st500;

import java.util.Date;
import java.util.List;

import com.moveltrack.cartaoprograma.model.Viatura;
import com.moveltrack.cartaoprograma.model.st500.Location;


public interface LocationRepCustom {
	public List<Object> findLocationsByVeiculoInicioFim(Viatura viatura,Date inicio, Date fim);
	public Location getLastLocationFromVeiculo(Viatura viatura) ;
}
