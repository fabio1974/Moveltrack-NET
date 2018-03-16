package net.moveltrack.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

import net.moveltrack.domain.Chip;
import net.moveltrack.domain.GeoEndereco;
import net.moveltrack.domain.Location;

@Stateless
@SuppressWarnings("serial")
public class GeoEnderecoDao extends DaoBean<GeoEndereco>{

	public GeoEnderecoDao() { }

	public List<Chip> findByTerm(String term){
		List<Chip> list = new ArrayList<Chip>();
		Query query = getEm().createQuery("select o from Chip o where o.numero like '%"+term+"%' order by o.numero");
		try{
			list = (List<Chip>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}

	public Chip findByNumero(String numero) {
		Query query = getEm().createQuery("select o from Chip o where o.numero =:numero");
		query.setParameter("numero",numero);
		return (Chip)query.getSingleResult();
	}
	
	
	public String getAddressFromLocation(double latitude, double longitude, boolean gc){
		Location location = new Location();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return getAddressFromLocation(location,gc);
	}
	
	public String getAddressFromLocation(Location location, boolean gc){

		Query query = getEm().createQuery("select P.endereco from GeoEndereco P where ABS(P.latitude-:latitude) < 0.0002 and ABS(P.longitude-:longitude) < 0.0002 and P.confiavel=true");
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
	
	
	@Transactional
	private void insertGeoEnderecoConfiavel(String endereco,double latitude, double longitude){
		GeoEndereco gc = new GeoEndereco();
		gc.setLatitude(latitude);
		gc.setLongitude(longitude);
		gc.setEndereco(endereco);
		gc.setConfiavel(true);
		try{
			salvar(gc);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public Location getLocationFromAddress(String bairro, String cidade, String estadoSigla) {
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
	}

	

}
