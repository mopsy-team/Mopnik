package util;

import org.jxmapviewer.viewer.GeoPosition;

public class DistanceCounter {
    public static double computeDiff(GeoPosition g1, GeoPosition g2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(g1.getLatitude() - g2.getLatitude());
        double lonDistance = Math.toRadians(g1.getLongitude() - g2.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(g1.getLatitude())) * Math.cos(Math.toRadians(g2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }
}
