package way;

import org.jxmapviewer.viewer.GeoPosition;

public class SearchInfo {
    private final Route route;
    private final GeoPosition geoPosition;
    private final double mileage;

    public SearchInfo(Route route, GeoPosition geoPosition, double mileage) {
        this.route = route;
        this.geoPosition = geoPosition;
        this.mileage = mileage;
    }

    public Route getRoute() {
        return route;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public double getMileage() {
        return mileage;
    }
}
