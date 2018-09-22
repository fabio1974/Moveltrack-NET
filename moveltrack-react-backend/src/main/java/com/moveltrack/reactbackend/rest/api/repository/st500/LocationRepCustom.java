package com.moveltrack.reactbackend.rest.api.repository.st500;

import java.util.Date;
import java.util.List;

import com.moveltrack.reactbackend.model.Veiculo;
import com.moveltrack.reactbackend.model.st500.Location;


public interface LocationRepCustom {
	public List<Object> findLocationsByVeiculoInicioFim(Veiculo veiculo,Date inicio, Date fim);
	public Location getLastLocationFromVeiculo(Veiculo veiculo) ;
}
