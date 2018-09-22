package com.moveltrack.reactbackend.rest.api.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Viatura;
import com.moveltrack.reactbackend.model.st500.Location;
import com.moveltrack.reactbackend.model.st500.Location2;
import com.moveltrack.reactbackend.rest.api.controller.relatorios.MapaUtil;
import com.moveltrack.reactbackend.rest.api.controller.relatorios.Parada;
import com.moveltrack.reactbackend.rest.api.repository.GeoEnderecoRep;
import com.moveltrack.reactbackend.rest.api.repository.ViaturaRepository;
import com.moveltrack.reactbackend.rest.api.repository.st500.LocationRep;

@BasePathAwareController
public class LocationController {

	@Autowired
	LocationRep locationRep;
	@Autowired
	ViaturaRepository viaturaRepository;
	@Autowired
	GeoEnderecoRep geoEnderecoRep;

	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm'h'");
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "locations/{placa}/{inicio}/{fim}")
	public List<Object> getLocations(@PathVariable(name = "placa") String placa,
			@PathVariable(name = "inicio") String inicio, @PathVariable(name = "fim") String fim) {

		Viatura viatura = viaturaRepository.findByPlaca(placa);

		return locationRep.findLocationsByVeiculoInicioFim(viatura, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));

	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "paradas/{placa}/{inicio}/{fim}")
	public List<Parada> getParadas(@PathVariable(name = "placa") String placa,
			@PathVariable(name = "inicio") String inicio, @PathVariable(name = "fim") String fim) {

		Viatura viatura = viaturaRepository.findByPlaca(placa);
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationRep.findLocationsByVeiculoInicioFim(viatura, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));
		if (pontosCrus == null || pontosCrus.size() == 0) {
			pontosCrus = new ArrayList<Object>();
			pontosCrus.add(locationRep.getLastLocationFromVeiculo(viatura));
		}
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));

		int count = 1;
		List<Parada> paradas = new ArrayList<Parada>();
		for (Location loc : pontosOtimizados) {
			if (MapaUtil.isParada(loc)) {

				Parada p = new Parada();
				p.setId(count);
				p.setTag("P-" + String.format("%02d", count));
				p.setEndereco(geoEnderecoRep.getAddressFromLocation(loc, true));
				p.setDataInicio(sdf2.format(loc.getDateLocationInicio()));
				p.setDataFim(sdf2.format(loc.getDateLocation()));

				long diff = loc.getDateLocation().getTime() - loc.getDateLocationInicio().getTime();

				p.setIntervalo(MapaUtil.convertMillisecondsToTimeString(diff));
				p.setVelocidade("" + loc.getVelocidade());
				p.setLatitude("" + loc.getLatitude());
				p.setLongitude("" + loc.getLongitude());

				paradas.add(p);
				count++;
			}
		}
		return paradas;
	}

	Location lastLoc = null;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "percurso/{placa}/{inicio}/{fim}")
	public List<Parada> getPercurso(@PathVariable(name = "placa") String placa,
			@PathVariable(name = "inicio") String inicio, @PathVariable(name = "fim") String fim) {

		long passo = 1l;

		Viatura viatura = viaturaRepository.findByPlaca(placa);
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationRep.findLocationsByVeiculoInicioFim(viatura, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));
		if (pontosCrus == null || pontosCrus.size() == 0) {
			pontosCrus = new ArrayList<Object>();
			pontosCrus.add(locationRep.getLastLocationFromVeiculo(viatura));
		}
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));

		int count = 1;
		List<Parada> paradas = new ArrayList<Parada>();
		for (Location loc : pontosOtimizados) {
			
			Parada p = new Parada();
			
			if (MapaUtil.isParada(loc)) {

				
				p.setId(count);
				p.setTag("P-" + String.format("%02d", count));
				p.setEndereco(geoEnderecoRep.getAddressFromLocation(loc, true));
				p.setDataInicio(sdf2.format(loc.getDateLocationInicio()));
				p.setDataFim(sdf2.format(loc.getDateLocation()));

				long diff = loc.getDateLocation().getTime() - loc.getDateLocationInicio().getTime();

				p.setIntervalo(MapaUtil.convertMillisecondsToTimeString(diff));
				p.setVelocidade("" + loc.getVelocidade());
				p.setLatitude("" + loc.getLatitude());
				p.setLongitude("" + loc.getLongitude());

				paradas.add(p);
				count++;

			} else if (loc.getVelocidade() > 0) {

				if (lastLoc != null) {
					long diff = loc.getDateLocationInicio().getTime() - lastLoc.getDateLocation().getTime();
					diff = diff / (60 * 1000);
					if (Math.abs(diff) > passo) {

						
						p.setId(count);
						p.setTag("movendo...");
						p.setEndereco(geoEnderecoRep.getAddressFromLocation(loc, true));
						p.setDataInicio(sdf2.format(loc.getDateLocationInicio()));
						p.setDataFim(sdf2.format(loc.getDateLocation()));

						p.setIntervalo("");
						p.setVelocidade("" + loc.getVelocidade());
						p.setLatitude("" + loc.getLatitude());
						p.setLongitude("" + loc.getLongitude());

						paradas.add(p);

						lastLoc = loc;
					}

				} else {
					
					p.setId(count);
					p.setTag("movendo...");
					p.setEndereco(geoEnderecoRep.getAddressFromLocation(loc, true));
					p.setDataInicio(sdf2.format(loc.getDateLocationInicio()));
					p.setDataFim(sdf2.format(loc.getDateLocation()));

					long diff = loc.getDateLocation().getTime() - loc.getDateLocationInicio().getTime();

					p.setIntervalo(MapaUtil.convertMillisecondsToTimeString(diff));
					p.setVelocidade("" + loc.getVelocidade());
					p.setLatitude("" + loc.getLatitude());
					p.setLongitude("" + loc.getLongitude());

					paradas.add(p);
					lastLoc = loc;
				}
			}
		}
		return paradas;
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "tacografo/{placa}/{inicio}/{fim}")
	public List<PontoTacografo> getTacografo(@PathVariable(name = "placa") String placa, @PathVariable(name = "inicio") String inicio, @PathVariable(name = "fim") String fim) {

		List<PontoTacografo> pts = new ArrayList<PontoTacografo>();

		
		Viatura viatura = viaturaRepository.findByPlaca(placa);
		List<Location> pontosOtimizados = new ArrayList<Location>();
		List<Object> pontosCrus = locationRep.findLocationsByVeiculoInicioFim(viatura, new Date(Long.parseLong(inicio)),
				new Date(Long.parseLong(fim)));
		if (pontosCrus == null || pontosCrus.size() == 0) {
			pontosCrus = new ArrayList<Object>();
			pontosCrus.add(locationRep.getLastLocationFromVeiculo(viatura));
		}
		
		pontosOtimizados = MapaUtil.otimizaPontosDoBanco(pontosCrus, new Date(Long.parseLong(inicio)),new Date(Long.parseLong(fim)));
		
		for (Object obj : pontosCrus) {
			PontoTacografo pt = new PontoTacografo();
			if(obj instanceof Location) {
				Location loc = (Location)obj;
				pt.setTempo(loc.getDateLocation().getTime());
				pt.setVelocidade(loc.getVelocidade());
			}else if(obj instanceof Location2) {
				Location2 loc = (Location2)obj;
				pt.setTempo(loc.getDateLocation().getTime());
				pt.setVelocidade(loc.getVelocidade());
			}
			pts.add(pt);
		}
		
		return pts;
	}
	
	
	class PontoTacografo{
		private Long tempo;
		private Double velocidade;
		public Long getTempo() {
			return tempo;
		}
		public void setTempo(Long tempo) {
			this.tempo = tempo;
		}
		public Double getVelocidade() {
			return velocidade;
		}
		public void setVelocidade(Double velocidade) {
			this.velocidade = velocidade;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}