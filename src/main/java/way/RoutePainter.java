package way;

import javafx.util.Pair;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Paints a route
 *
 * @author Martin Steiger
 */
public class RoutePainter implements Painter<JXMapViewer> {
    private final boolean antiAlias = false;
    private Color color;
    private boolean first;
    private List<Pair<Line2D, Route>> lines;
    private Route route;
    private JXMapViewer map;
    private ArrayList<GeoPosition> track;

    /**
     * @param track the track
     */
    public RoutePainter(List<GeoPosition> track, Route route) {
        // copy the list so that changes in the
        // original list do not have an effect here
        /*
        Color[] colors = { Color.GREEN, Color.ORANGE, Color.RED, Color.YELLOW};
        this.track = new ArrayList<>(track);
        Random r = new Random();
        this.color = colors[r.nextInt(colors.length)]; */
        this.first = true;
        this.lines = new ArrayList<>();
        this.route = route;
        this.track = new ArrayList<>(track);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        Point2D pt = map.getTileFactory().geoToPixel(track.get(0), map.getZoom());
 /*       if (!map.getViewportBounds().contains((int) pt.getX(), (int) pt.getY()))
            return;*/

        g = (Graphics2D) g.create();

        this.map = map;

        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (route != null) {
            NSpaces nSpaces = route.nSpaces();
            if (nSpaces == NSpaces.LARGE) {
                color = Color.green;
            } else if (nSpaces == NSpaces.SUFFICIENT) {
                color = Color.yellow;
            } else if (nSpaces == NSpaces.LOW) {
                color = Color.orange;
            } else if (nSpaces == NSpaces.VERY_LOW) {
                color = Color.red;
            } else {
                color = Color.black;
            }
        }

        g.setColor(color);
        g.setStroke(new BasicStroke(4));

        drawRoute(g, map);

        g.dispose();

    }

    /**
     * @param g   the graphics object
     * @param map the map
     */
    private void drawRoute(Graphics2D g, JXMapViewer map) {
        if (route == null) {
            return;
        }
        TileFactory tf = map.getTileFactory();
        int zoom = map.getZoom();
        lines = new ArrayList<>();
        for (int i = 0; i < track.size() - 1; i++) {
            double x1 = tf.geoToPixel(track.get(i), zoom).getX();
            double y1 = tf.geoToPixel(track.get(i), zoom).getY();
            double x2 = tf.geoToPixel(track.get(i + 1), zoom).getX();
            double y2 = tf.geoToPixel(track.get(i + 1), zoom).getY();
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
            g.draw(line);
            lines.add(new Pair<>(line, route)); // TODO(MG)
        }
    }

    public MouseListener mouseListenerOnRoute(JXMapViewer map) {
        return (new MouseAdapter() {
            final int S = 10;

            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                Rectangle r = map.getViewportBounds();
                int x = p.x + r.x;
                int y = p.y + r.y;
                Rectangle sensor = new Rectangle(S, S);
                sensor.setFrameFromCenter(x, y, x + S / 2, y + S / 2);
                for (Pair<Line2D, Route> line : lines) {
                    if (line.getKey().intersects(sensor)) {
                        new TrafficInfoDialog(line.getValue());
                        break;
                    }
                }
            }
        });
    }

    public SearchInfo isCloseTo (JXMapViewer map, Point p) {
        final int S = 10;
        Rectangle r = map.getViewportBounds();
        int x = p.x + r.x;
        int y = p.y + r.y;
        Rectangle sensor = new Rectangle(S, S);
        sensor.setFrameFromCenter(x, y, x + S / 2, y + S / 2);
        for (Pair<Line2D, Route> line : lines) {
            if (line.getKey().intersects(sensor)) {
                return line.getValue().searchInfo(map.convertPointToGeoPosition(p));
            }
        }
        return null;
    }
}

