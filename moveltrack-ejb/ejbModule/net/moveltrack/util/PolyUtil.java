package net.moveltrack.util;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

import java.util.List;

import net.moveltrack.domain.Location;
import net.moveltrack.domain.Poligono;
import net.moveltrack.domain.Vertice;

public class PolyUtil {

	  /**
     * Computes whether the given point lies inside the specified polygon.
     * The polygon is always cosidered closed, regardless of whether the last point equals
     * the first or not.
     * Inside is defined as not containing the South Pole -- the South Pole is always outside.
     * The polygon is formed of great circle segments if geodesic is true, and of rhumb
     * (loxodromic) segments otherwise.
     */
	
	public static boolean containsLocation(double latitude, double longitude, Poligono poligono, boolean geodesic) {
		Location l = new Location();
		l.setLatitude(latitude);
		l.setLongitude(longitude);
		return containsLocation(l, poligono, geodesic);
	}
	
    public static boolean containsLocation(Location point, Poligono poligono, boolean geodesic) {

    	List<Vertice> vertices = poligono.getVertices();
        
    	final int size = vertices.size();
        if (size == 0) {
            return false;
        }
        double lat3 = toRadians(point.getLatitude());
        double lng3 = toRadians(point.getLongitude());
        Vertice prev = vertices.get(size - 1);
        double lat1 = toRadians(prev.getLat());
        double lng1 = toRadians(prev.getLng());
        int nIntersect = 0;
        for (Vertice point2 : vertices) {
            double dLng3 = wrap(lng3 - lng1, -PI, PI);
            // Special case: point equal to vertex is inside.
            if (lat3 == lat1 && dLng3 == 0) {
                return true;
            }
            double lat2 = toRadians(point2.getLat());
            double lng2 = toRadians(point2.getLng());
            // Offset longitudes by -lng1.
            if (intersects(lat1, lat2, wrap(lng2 - lng1, -PI, PI), lat3, dLng3, geodesic)) {
                ++nIntersect;
            }
            lat1 = lat2;
            lng1 = lng2;
        }
        return (nIntersect & 1) != 0;
    }

    
    /**
     * Wraps the given value into the inclusive-exclusive interval between min and max.
     * @param n   The value to wrap.
     * @param min The minimum.
     * @param max The maximum.
     */
    private static double wrap(double n, double min, double max) {
        return (n >= min && n < max) ? n : (mod(n - min, max - min) + min);
    }
    
    
    /**
     * Computes whether the vertical segment (lat3, lng3) to South Pole intersects the segment
     * (lat1, lng1) to (lat2, lng2).
     * Longitudes are offset by -lng1; the implicit lng1 becomes 0.
     */
    private static boolean intersects(double lat1, double lat2, double lng2,
                                      double lat3, double lng3, boolean geodesic) {
        // Both ends on the same side of lng3.
        if ((lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2)) {
            return false;
        }
        // Point is South Pole.
        if (lat3 <= -PI/2) {
            return false;
        }
        // Any segment end is a pole.
        if (lat1 <= -PI/2 || lat2 <= -PI/2 || lat1 >= PI/2 || lat2 >= PI/2) {
            return false;
        }
        if (lng2 <= -PI) {
            return false;
        }
        double linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2;
        // Northern hemisphere and point under lat-lng line.
        if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
            return false;
        }
        // Southern hemisphere and point above lat-lng line.
        if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
            return true;
        }
        // North Pole.
        if (lat3 >= PI/2) {
            return true;
        }
        // Compare lat3 with latitude on the GC/Rhumb segment corresponding to lng3.
        // Compare through a strictly-increasing function (tan() or mercator()) as convenient.
        return geodesic ?
            Math.tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3) :
            mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3);
    }
    
    /**
     * Returns tan(latitude-at-lng3) on the great circle (lat1, lng1) to (lat2, lng2). lng1==0.
     * See http://williams.best.vwh.net/avform.htm .
     */
    private static double tanLatGC(double lat1, double lat2, double lng2, double lng3) {
        return (Math.tan(lat1) * Math.sin(lng2 - lng3) + Math.tan(lat2) * Math.sin(lng3)) / Math.sin(lng2);
    }

    /**
     * Returns mercator(latitude-at-lng3) on the Rhumb line (lat1, lng1) to (lat2, lng2). lng1==0.
     */
    private static double mercatorLatRhumb(double lat1, double lat2, double lng2, double lng3) {
        return (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2;
    }
    
    private static double mercator(double lat) {
        return Math.log(Math.tan(lat * 0.5 + PI/4));
    }
    
    private static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }
	
}
