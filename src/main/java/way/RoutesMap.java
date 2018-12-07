package way;

import methods.Method;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.*;

public class RoutesMap {
    public Map<String, TreeSet<Route>> getRoutes() {
        return routes;
    }

    private Map<String, TreeSet<Route>> routes;
    private Comparator<Route> routesComparator = Comparator.comparingDouble(Route::getMileageBegin);

    public RoutesMap() {
        routes = new HashMap<>();
    }

    public void add(Route w) {
        if (!routes.containsKey(w.getName())) {
            routes.put(w.getName(), new TreeSet<>(routesComparator));
        }
        routes.get(w.getName()).add(w);
    }

    // Finds way given a name of motorway and point (mileage).
    // Returns null if there is no such way.
    Route find(String name, double mileage) {
        if (!routes.containsKey(name)) {
            return null;
        }
        Route w = routes.get(name).floor(Route.newRouteForComparing(mileage));
        if (w == null) {
            return null;
        }
        if (w.getMileageEnd() < mileage) {
            return null;
        }
        return w;
    }

//    Route findAndRemove(String name, double mileage) {
//        Route res = find(name, mileage);
//        if (res == null) {
//            return null;
//        }
//        remove(res);
//        return res;
//    }

    public void addGeoposition(String name, double mileageBeg, GeoPosition geoPosition) {
        Route r = find(name, mileageBeg);
        if (r != null) {
            r.addGeoposition(mileageBeg, geoPosition);
        }
    }

    public Route findRouteByGeoPosition(GeoPosition geoPosition) {
        Route res = null;
        double diff = Double.MAX_VALUE;
        for (Map.Entry<String, TreeSet<Route>> entry : routes.entrySet()) {
            for (Route r : entry.getValue()) {
                double d = r.getDistanceFromGeoPosition(geoPosition);
                if (d < diff) {
                    diff = d;
                    res = r;
                }
            }
        }
        return res;
    }

    public List<RoutePainter> routePainters() {
        List<RoutePainter> routePainters = new ArrayList<>();
        for (Map.Entry<String, TreeSet<Route>> entry : routes.entrySet()) {
            for (Route route : entry.getValue()) {
                RoutePainter rp = route.getRoutePainter();
                if (rp != null) {
                    routePainters.add(route.getRoutePainter());
                }
            }
        }
        return routePainters;
    }

    public void setSpacesNeeded(Method method) {
        for (Map.Entry<String, TreeSet<Route>> entry : routes.entrySet()) {
            for (Route r : entry.getValue()) {
                r.computeSpacesNeeded(method);
            }
        }
    }

//    private void remove(Route route) {
//        routes.get(route.getName()).remove(route);
//    }
}
