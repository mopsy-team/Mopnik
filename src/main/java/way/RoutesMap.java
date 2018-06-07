package way;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class RoutesMap {
    private Map<String, TreeSet<Route>> routes;
    private Comparator<Route> routesComparator = Comparator.comparingDouble(Route::getMileageBegin);

    RoutesMap() {
        routes = new HashMap<>();
    }

    public void add(Route w) {
        if (!routes.containsKey(w.getName())) {
            routes.put(w.getName(), new TreeSet<Route>(routesComparator));
        }
        routes.get(w.getName()).add(w);
    }

    // Finds way given a name of motorway and point (mileage).
    // Returns null if there is no such way.
    Route find(String name, double mileage) {
        if (!routes.containsKey(name)) {
            return null;
        }
        Route w = routes.get(name).floor(new Route("", mileage, mileage, null));
        if (w == null) {
            return null;
        }
        if (w.getMileageEnd() < mileage) {
            return null;
        }
        return w;
    }

    Route findAndRemove(String name, double mileage) {
        Route res = find(name, mileage);
        if (res == null) {
            return null;
        }
        remove(res);
        return res;
    }


    Route findAllAndReplace(String name, double mileageBegin, double mileageEnd) {
        Route res = findAndRemove(name, mileageBegin + 1.);
        if (res == null) {
            return null;
        }
        mileageBegin = res.getMileageEnd();
        while (mileageBegin < mileageEnd) {
            Route r = findAndRemove(name, mileageBegin + 1.);
            if (r == null) {
                add(res);
                return res;
            }
            res = res.add(r);
            mileageBegin = r.getMileageEnd();
        }
        add(res);
        return res;
    }

    public void assignTrafficInfo(String name, double mileageBegin, double mileageEnd, TrafficInfo trafficInfo) {
        Route route = find(name, mileageBegin + 2);
        if (route != null)
            route.setTrafficInfo(trafficInfo);
    }

    int size() {
        return routes.size();
    }

    public Route findRouteByGeoPosition(GeoPosition geoPosition) {

        Route res = null;
        double diff = Double.MAX_VALUE;
        for (Map.Entry<String, TreeSet<Route>> entry : routes.entrySet()) {
            for (Route r : entry.getValue()) {
                for (GeoPosition gp: r.getGeoPositions()) {
                    double newDiff = computeDiff(geoPosition, gp);
                    if (newDiff < diff) {
                        diff = newDiff;
                        res = r;
                    }
                }
            }
        }
        return res;
    }

    private double computeDiff (GeoPosition g1, GeoPosition g2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(g1.getLatitude()-g2.getLatitude());
        double lonDistance = Math.toRadians(g1.getLongitude()-g2.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(g1.getLatitude())) * Math.cos(Math.toRadians(g2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

    private void remove(Route route) {
        routes.get(route.getName()).remove(route);
    }
}
