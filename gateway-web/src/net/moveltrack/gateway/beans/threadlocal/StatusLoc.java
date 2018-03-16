package net.moveltrack.gateway.beans.threadlocal;

import net.moveltrack.domain.Location;

public class StatusLoc {

	public static final ThreadLocal<Location> instance = new ThreadLocal<Location>();
	
	public static void setStatusLoc(Location statusLoc) {
		instance.set(statusLoc);
	}

	public static Location getStatusLoc() {
		return instance.get();
	}

}