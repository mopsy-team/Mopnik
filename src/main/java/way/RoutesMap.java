package way;

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

    Route findNear(String name, double mileage, double precision) {
        return find(name, mileage + precision);
    }

    int size() {
        return routes.size();
    }
}
