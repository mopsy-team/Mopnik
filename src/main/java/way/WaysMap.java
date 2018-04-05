package way;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class WaysMap {
    private Map<String, TreeSet<Way>> ways;
    private Comparator<Way> waysComparator = new Comparator<Way>() {
        @Override
        public int compare(Way w1, Way w2) {
            return Double.compare(w1.getMileageBegin(), w2.getMileageBegin());
        }
    };

    public WaysMap() {
        ways = new HashMap<>();
    }

    public void add(Way w) {
        if (!ways.containsKey(w.getName())) {
            ways.put(w.getName(), new TreeSet<Way>(waysComparator));
        }
        ways.get(w.getName()).add(w);
    }

    // Finds way given a name of motorway and point (mileage).
    // Returns null if there is no such way.
    public Way find(String name, double mileage) {
        if (!ways.containsKey(name)) {
            return null;
        }
        Way w = ways.get(name).floor(new Way("", mileage, mileage, null));
        if (w == null) {
            return null;
        }
        if (w.getMileageEnd() < mileage) {
            return null;
        }
        return w;
    }
}
