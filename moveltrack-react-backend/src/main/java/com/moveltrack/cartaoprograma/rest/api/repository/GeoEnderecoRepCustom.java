package com.moveltrack.cartaoprograma.rest.api.repository;

import com.moveltrack.cartaoprograma.model.st500.Location;

public interface GeoEnderecoRepCustom {
	public String getAddressFromLocation(double latitude, double longitude, boolean gc);
	public String getAddressFromLocation(Location location, boolean gc);
}
