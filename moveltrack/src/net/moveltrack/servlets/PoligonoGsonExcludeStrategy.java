package net.moveltrack.servlets;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import net.moveltrack.domain.Poligono;

public class PoligonoGsonExcludeStrategy implements ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return ((f.getDeclaringClass() == Poligono.class && f.getName().equals("veiculo")))

			 || (f.getDeclaringClass() == Poligono.class && (f.getName().equals("vertices")));
	}
}