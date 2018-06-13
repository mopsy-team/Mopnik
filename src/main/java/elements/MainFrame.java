package elements;

import config.AppConfig;
import way.*;
import methods.Method;
import mop.*;
import org.json.JSONException;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
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
        try {
            AppConfig.loadConfig();
        } catch (IOException e) {
            AppConfig.save();
        }

        frame = new JFrame("Mopnik");
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
        trafficMap.setRoutesMap(this);
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
        this.mopPoints = mopInfos.stream().map((MopInfo m) ->
                new MopPoint(m.getName(), m, MopType.EXISTING, this)).collect(Collectors.toSet());
        repaint();
    }

    public void setMopPointsFromServer() {
        String urlQueryString = AppConfig.getMopsUrl();
        ServerDataHandler serverDataHandler = new ServerDataHandler(urlQueryString);
        try {
            mopInfos = serverDataHandler.parseMops();
            this.mopPoints = mopInfos.stream().map((MopInfo m) -> new MopPoint(m.getName(),
                    m, MopType.EXISTING, this)).collect(Collectors.toSet());
            JOptionPane.showMessageDialog(frame,
                    "Poprawnie załadowano dane.");
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(frame,
                    "Niepoprawne dane na serwerze.",
                    "Nie udało się załadować danych",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                    "Brak możliwości połączenia z serwerem lub niepoprawny URL.",
                    "Nie udało się załadować danych",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public Set<MopInfo> getMopInfos() {
        return mopInfos;
    }

    public void show() {
        File mopsFile = AppConfig.getFile(AppConfig.getMopXlsxFilename());

        setMopPointsFromFile(mopsFile);

        File matrixFile = AppConfig.getFile(AppConfig.getSDRFilename());
        if (TrafficInfoParser.assignRoutes(this, matrixFile) == -1) {
            JOptionPane.showMessageDialog(getFrame(),
                    "Wskazany plik nie istnieje lub jest w złym formacie.",
                    "Zły format pliku",
                    JOptionPane.WARNING_MESSAGE);
        }

        repaint();

        MainMenu mainMenu = new MainMenu(this);
        frame.pack();
        frame.setVisible(true);

    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public RoutesMap getRoutesMap() {
        return routesMap;
    }

    public void setRoutesMap(RoutesMap routesMap) {
        this.routesMap = routesMap;
    }

    public JXMapViewer getMapViewer() {
        return mapViewer;
    }

    public void repaint() {
        mapViewer.removeAll();
        WaypointPainter<MopPoint> waypointPainter = new MopPointPainter();
        waypointPainter.setWaypoints(mopPoints);
        painter.addPainter(waypointPainter);
        for (MouseListener listener : listeners) {
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

    public void addMop(String name, GeoPosition geoPosition, Route route, String direction) {
        String road = "";
        double mileage = 0;
        if (route != null) {
            road = route.getName();
            mileage = (route.getMileageBegin() + route.getMileageEnd()) / 2;
        }
        int id = mopPoints.size();
        MopInfo mopInfo = new MopInfo(id, "", "", "", geoPosition, road, direction, 0,
                new MopParkingSpacesInfo(), new MopEquipmentInfo(), mileage);
        mopInfo.setRoute(route);
        mopInfos.add(mopInfo);
        mopPoints.add(new MopPoint(name, mopInfo, MopType.ADDED, this));
        new AddedMopInfoDialog(mopInfo, this);
        repaint();
    }

    public void removeMop(MopInfo mopInfo) {
        for (MopPoint mopPoint : mopPoints) {
            if (mopPoint.getMopInfo() == mopInfo) {
                mopPoints.remove(mopPoint);
                break;
            }
        }
        mopInfos.remove(mopInfo);
        repaint();
    }
}
