package way;


import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Way;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.painter.Painter;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;


public class TrafficMap {
    private OSM osm = null;
    private final String path = "../mapy/new/poland-latest6.osm";

    public TrafficMap() {
        try {
            osm = OSMParser.parse(new FileInputStream(new File(path)));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public List<Painter<JXMapViewer>> painters() {

        Connection c = null;
        Statement way_stmt = null;

        List<Painter<JXMapViewer>> res = new ArrayList<>();

        /*
        for (Route way : osm.getWays()) {
            List<GeoPosition> track = new ArrayList<>();
            for (Coordinate coord : way.getLineString().getCoordinates()) {
                track.add(new GeoPosition(coord.y, coord.x));
            }
            res.add(new RoutePainter(track));
        }
        */

        return res;
    }

    public Set<Waypoint> mileages() {
        Set<Waypoint> waypointSet = new HashSet<>();
        WaypointPainter<Waypoint> p = new WaypointPainter<Waypoint>();
        for (OSMNode node: osm.getNodes()) {
            if (node.getAllTags().containsKey("milestone")) {
                waypointSet.add(
                        new DefaultWaypoint(
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon))));
            }
        }
        return waypointSet;
    }

    public List<RoutePainter> routes(RoutesMap routesMap) {
        Map<String, Set<OSMNode>> nodes = new HashMap<>();
        Comparator<OSMNode> osmNodeComparator =
                Comparator.comparingDouble(n -> Integer.parseInt(n.getAllTags().get("milestone")));
        for (Way way : osm.getWays()){
            for (OSMNode node : way.nodes) {
                if (node.getAllTags().containsKey("milestone") && way.getAllTags().containsKey("ref")) {
                    String refs[] = way.getAllTags().get("ref").replaceAll("\\s+","").split(";");
                    for (String ref : refs) {
                    //String ref = way.getAllTags().get("ref").replaceAll("\\s+", "");
                        if (nodes.containsKey(ref)) {
                            nodes.get(ref).add(node);
                        } else {
                            nodes.put(ref, new TreeSet<>(osmNodeComparator));
                        }
                    }
                }
            }
        }
        List<RoutePainter> res = new ArrayList<>();
        for (Map.Entry<String, Set<OSMNode>> entry : nodes.entrySet()){
            if (!entry.getKey().equals("S8")) {
                System.out.println(entry.getKey());
                boolean first = true;
                OSMNode last = null;
                double lastMilestone = 0.0;
                for (OSMNode node : entry.getValue()) {
                    double newMilestone =  Double.parseDouble(node.getAllTags().get("milestone"));
                    System.out.println(node.getAllTags().get("milestone"));
                    if (!first && Integer.parseInt(last.getAllTags().get("milestone")) <
                            Integer.parseInt(node.getAllTags().get("milestone")) - 2) {
                        List<GeoPosition> track = Arrays.asList(
                                new GeoPosition(Double.parseDouble(last.lat), Double.parseDouble(last.lon)),
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon)));
                        Route route = null;
                        if (routesMap != null) {
                            route = routesMap.findAllAndReplace(entry.getKey(), lastMilestone, newMilestone);
                        }
                        res.add(new RoutePainter(track, route));
                    }
                    first = false;
                    last = node;
                    lastMilestone =  newMilestone;
                }
            }
        }
        return res;
    }


}
