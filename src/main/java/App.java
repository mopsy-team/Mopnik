import mop.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        final JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition mim = new GeoPosition(52.211798, 20.982224);

        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(mim);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        final JFrame frame = new JFrame("MIM");
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        XlsToMopParser xlsToMopParser = new XlsToMopParser("MOP-12.2017-final2.xlsx");
        Set<MopInfo> mopInfos = xlsToMopParser.parseMops();
        Set<MopPoint> mopPoints = mopInfos.stream().map((MopInfo m) -> new MopPoint(m.getName(),
                Color.red, m, frame)).collect(Collectors.toSet());
        WaypointPainter<MopPoint> waypointPainter = new MopPointPainter();
        waypointPainter.setWaypoints(mopPoints);
        mapViewer.setOverlayPainter(waypointPainter);

        // Add the JButtons to the map viewer
        for (MopPoint w : mopPoints) {
            mapViewer.add(w.getButton());
            // mapViewer.add(w.getPanel(), BorderLayout.CENTER);
        }

        // Display the viewer in a JFrame
        frame.getContentPane().add(mapViewer);
        frame.pack();
        frame.setVisible(true);


        mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateWindowTitle(frame, mapViewer);
            }
        });

        mapViewer.addPropertyChangeListener("center", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateWindowTitle(frame, mapViewer);
            }
        });

        updateWindowTitle(frame, mapViewer);


    }

    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer) {
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();
    }


}
