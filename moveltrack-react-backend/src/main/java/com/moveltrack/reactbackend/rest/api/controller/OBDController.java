package com.moveltrack.reactbackend.rest.api.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moveltrack.reactbackend.model.Viatura;
import com.moveltrack.reactbackend.model.st500.PID;
import com.moveltrack.reactbackend.model.st500.ST500ALT;
import com.moveltrack.reactbackend.model.st500.ST500PID;
import com.moveltrack.reactbackend.rest.api.repository.GeoEnderecoRep;
import com.moveltrack.reactbackend.rest.api.repository.ViaturaRepository;
import com.moveltrack.reactbackend.rest.api.repository.st500.ST500ALTRep;
import com.moveltrack.reactbackend.rest.api.repository.st500.ST500PIDRep;



@BasePathAwareController
public class OBDController {
	
	@Autowired ST500PIDRep pdiRep;
	@Autowired ST500ALTRep altRep;
	@Autowired ViaturaRepository viaturaRepository;
	
	  
    
    
	
    @ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "sensores/{placa}")
	public ST500PID sensores(@PathVariable("placa")String placa) {
		
		Viatura viatura = viaturaRepository.findByPlaca(placa);
		String imei = viatura.getRastreador().getImei();
		String serial = String.valueOf(Integer.parseInt(imei));
		ST500PID st500pid = pdiRep.findTopBySerialOrderByDeviceDateDesc(serial);
		
		List<PID> pids = new ArrayList<PID>();
		
		if(st500pid!=null) {
			pids = st500pid.getPids();
			for (PID pid : pids) {
				PID aux = PID.getPid(pid.getHex());
				pid.setDescryption(aux.getDescryption());
				pid.setUnit(aux.getUnit());
				pid.setHexStr(String.format("%02x",pid.getHex()).toUpperCase());
			}
		}
		
		return st500pid;
	}
	
	


	@Autowired GeoEnderecoRep geoEnderecoRep;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@ResponseBody
	@RequestMapping("/alertas/{placa}")
	public List<ST500ALT> alerta(@PathVariable("placa") String placa) {
		Viatura viatura = viaturaRepository.findByPlaca(placa);
		String imei = viatura.getRastreador().getImei();
		String serial = String.valueOf(Integer.parseInt(imei));
		List<ST500ALT> list = altRep.findTop10BySerialOrderByDeviceDateDesc(serial);
		for (ST500ALT st500alt : list) {
			st500alt.setAddress(geoEnderecoRep.getAddressFromLocation(st500alt.getLatitude(),st500alt.getLongitude(),true));
			st500alt.setAlerta();
			st500alt.setData(sdf.format(st500alt.getDeviceDate()));
			
		}
		Collections.reverse(list);
		return list;
	}
	
/*	class Alertas{
		List<ST500ALT> list;
		Viatura viatura;
		public List<ST500ALT> getList() {
			return list;
		}
		public void setList(List<ST500ALT> list) {
			this.list = list;
		}
		public Viatura getViatura() {
			return viatura;
		}
		public void setViatura(Viatura viatura) {
			this.viatura = viatura;
		}
	}
*/

}
