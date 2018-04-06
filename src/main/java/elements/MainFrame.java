package elements;

import way.RoutePainter;
import way.TrafficMap;
import methods.Method;
import mop.MopInfo;
import mop.MopPoint;
import mop.MopPointPainter;
import mop.XlsToMopParser;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;
import way.RoutesMap;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainFrame {

    private final JFrame frame;
    private final JXMapViewer mapViewer;
    private Set<MopPoint> mopPoints;
    private Set<Waypoint> mileages;
    private CompoundPainter<JXMapViewer> painter;
    private Set<MopInfo> mopInfos;
    private Set<Method> methods;
    private TrafficMap trafficMap;
    private RoutesMap routesMap = null;
    private boolean first = true;
    private List<MouseListener> listeners;

    public MainFrame() {
        frame = new JFrame("MOPY");
        mapViewer = new JXMapViewer();
        mapViewer.setName("MapViewer");

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

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

        frame.setPreferredSize(new Dimension(1600, 1200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().add(mapViewer);
        painter = new CompoundPainter<JXMapViewer>();
        mapViewer.setOverlayPainter(painter);

        mopPoints = new HashSet<>();
        mopInfos = new HashSet<>();
        methods = new HashSet<>();
        listeners = new ArrayList<>();
        trafficMap = new TrafficMap();
        mileages = trafficMap.mileages();
    }

    public void setMopPoints(Set<MopPoint> mopPoints) {
        this.mopPoints = mopPoints;
        repaint();
    }

    public void setMopPointsFromFile(File file) {
        XlsToMopParser xlsToMopParser = new XlsToMopParser(file);
        Set<MopInfo> mopInfosTemp = xlsToMopParser.parseMops();
        if (mopInfosTemp == null) {
            JOptionPane.showMessageDialog(frame,
                    "Wskazany plik nie istnieje lub jest w złym formacie.",
                    "Zły format pliku",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            mopInfos = mopInfosTemp;
        }
        this.mopPoints = mopInfos.stream().map((MopInfo m) -> new MopPoint(m.getName(),
                Color.red, m, this)).collect(Collectors.toSet());
        repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public Set<MopInfo> getMopInfos() {
        return mopInfos;
    }

    public void show() {
        setMopPointsFromFile(
                new File(getClass().getClassLoader().getResource("MOP-12.2017-final2.xlsx").getFile()));

        repaint();

        MainMenu.create(this);
        frame.pack();
        frame.setVisible(true);

    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public Set<Method> getMethods() {
        System.out.println(methods.size());
        return methods;
    }

    public void setRoutesMap(RoutesMap routesMap) {
        this.routesMap = routesMap;
        System.out.println("RoutesMap set");
    }

    public void repaint() {
        mapViewer.removeAll();
        WaypointPainter<MopPoint> waypointPainter = new MopPointPainter();
        waypointPainter.setWaypoints(mopPoints);
        painter.addPainter(waypointPainter);
        // WaypointPainter<Waypoint> mils = new WaypointPainter<>();
        // mils.setWaypoints(mileages);
        // painter.addPainter(mils);
        for (MouseListener listener: listeners) {
            mapViewer.removeMouseListener(listener);
        }
        listeners = new ArrayList<>();
        for (RoutePainter routePainter : trafficMap.routes(routesMap)) {
            painter.addPainter(routePainter);
            MouseListener ml = routePainter.mouseListenerOnRoute(mapViewer);
            mapViewer.addMouseListener(ml);
            listeners.add(ml);
        }
        first = false;
        for (MopPoint w : mopPoints) {
            mapViewer.add(w.getButton());
        }
        frame.revalidate();
    }
}
