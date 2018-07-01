package way;


import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import org.apache.commons.lang3.math.NumberUtils;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.File;
import java.io.FileInputStream;


public class TrafficMap {
    private OSM osm;
    private static final String tag = "distance";

    public TrafficMap(File osmFile) throws Exception {
        osm = OSMParser.parse(new FileInputStream(osmFile.getAbsolutePath()));
    }

    public void addGeopositions(RoutesMap routesMap) {
        for (OSMNode node : osm.getNodes()) {
            if (node.getAllTags().containsKey("ref")) {
                double distance = getDistance(node);
                if (distance == -1) {
                    continue;
                }
                String refs[] = NameAdjuster.getWayNames(node);
                for (String ref : refs) {
                    if ((ref.contains("A") || ref.contains("S") && !ref.contains("S3"))) {
                        routesMap.addGeoposition(ref, distance,
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon)));
                    }
                }
            }
        }

    }

    private double getDistance(OSMNode node) {
        if (node.getAllTags().containsKey(tag)) {
            if (NumberUtils.isCreatable(node.getAllTags().get(tag))) {
                return Double.parseDouble(node.getAllTags().get(tag).replaceAll(",", "."));
            }
        }
        return -1.;
    }

}
