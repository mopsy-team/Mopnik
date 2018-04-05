package layers;


import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.Way;
import com.vividsolutions.jts.geom.Coordinate;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class TrafficMap {

    public TrafficMap() {
    }

    public static List<RoutePainter> painters() {

        Connection c = null;
        Statement way_stmt = null;

        List<RoutePainter> res = new ArrayList<>();

        OSM osm = null;
        try {
            osm = OSMParser.parse(new FileInputStream(new File("../mapy/poland-latest6.osm")));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        for (Way way : osm.getWays()) {
            List<GeoPosition> track = new ArrayList<>();
            for (Coordinate coord : way.getLineString().getCoordinates()) {
                track.add(new GeoPosition(coord.y, coord.x));
            }
            res.add(new RoutePainter(track));
        }

        return res;
    }


}
