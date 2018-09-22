package com.moveltrack.cartaoprograma.rest.api.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.moveltrack.cartaoprograma.model.GeoEndereco;
import com.moveltrack.cartaoprograma.model.st500.Location;

public class GeoEnderecoRepImpl implements GeoEnderecoRepCustom {
	
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public String getAddressFromLocation(double latitude, double longitude, boolean gc){
		Location location = new Location();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return getAddressFromLocation(location,gc);
	}
	
	@Override
	public String getAddressFromLocation(Location location, boolean gc){
		
		if(location==null)
			return null;

		Query query = entityManager.createQuery("select P.endereco from GeoEndereco P where ABS(P.latitude-:latitude) < 0.0002 and ABS(P.longitude-:longitude) < 0.0002 and P.confiavel=true");
		query.setParameter("latitude",location.getLatitude());
		query.setParameter("longitude",location.getLongitude());
		query.setMaxResults(1);
		List<String> list = query.getResultList();
		if(list!=null && list.size()>0){
			String endereco = list.get(0);
			if(endereco!=null && !endereco.trim().equals("") && endereco.trim().length()>0){
				return endereco;
			}	
		}
		
		if(gc){
			try{
				final Geocoder geocoder = new Geocoder();
				LatLng p = new LatLng(new BigDecimal(location.getLatitude()),new BigDecimal(location.getLongitude()));
				GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(p).getGeocoderRequest();
				GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
				
				
				
				if(geocoderResponse.getStatus()== GeocoderStatus.OK){
					List<GeocoderResult> rs = geocoderResponse.getResults();
					for (GeocoderResult geocoderResult : rs) {
							if(geocoderResult.getFormattedAddress()!=null){
								insertGeoEnderecoConfiavel(geocoderResult.getFormattedAddress(),location.getLatitude(),location.getLongitude());
								return geocoderResult.getFormattedAddress();
							}	
					}
				}
				geocoderResponse = geocoder.geocode(geocoderRequest);
				if(geocoderResponse.getStatus()== GeocoderStatus.OK){
					List<GeocoderResult> rs = geocoderResponse.getResults();
					for (GeocoderResult geocoderResult : rs) {
							if(geocoderResult.getFormattedAddress()!=null){
								insertGeoEnderecoConfiavel(geocoderResult.getFormattedAddress(),location.getLatitude(),location.getLongitude());
								return geocoderResult.getFormattedAddress();
							}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "";
	}	
	
	@Autowired GeoEnderecoRep rep;
	
	private void insertGeoEnderecoConfiavel(String endereco,double latitude, double longitude){
		GeoEndereco gc = new GeoEndereco();
		gc.setLatitude(latitude);
		gc.setLongitude(longitude);
		gc.setEndereco(endereco.replaceAll("[^\\p{InBasic_Latin}\\p{InLatin-1Supplement}]", ""));
		gc.setConfiavel(true);
		rep.save(gc);
	}
	
/*	public Location getLocationFromAddress(String bairro, String cidade, String estadoSigla) {
	    final Geocoder geocoder = new Geocoder();
	    GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(bairro+","+cidade+","+estadoSigla).getGeocoderRequest();
	    GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
	    try {
		    List<GeocoderResult> results = geocoderResponse.getResults();
		    Location location = new Location();
		    location.setLatitude(results.get(0).getGeometry().getLocation().getLat().floatValue());
		    location.setLongitude(results.get(0).getGeometry().getLocation().getLng().floatValue());
		    return location;
		} catch (Exception e) {
			return null;
		}
	}*/
	
	
}
