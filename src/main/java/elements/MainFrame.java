package elements;

import config.AppConfig;
import methods.CustomMethod;
import methods.Method;
import mop.*;
import mopsim.config_group.MOPSimConfigGroup;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import util.JSONParser;
import way.*;

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
    private CompoundPainter<JXMapViewer> painter;
    private Set<MopInfo> mopInfos;
    private Method method;
    private TrafficMap trafficMap;
    private RoutesMap routesMap = null;
    private RoutesMap addedRoutesMap = new RoutesMap();
    private List<RoutePainter> routePainters = new ArrayList<>();
    private List<RoutePainter> addedRoutePainters = new ArrayList<>();
    private List<MouseListener> listeners;
    private MOPSimConfigGroup mopsimConfig;

    public MOPSimConfigGroup getMopsimConfig() {
        return mopsimConfig;
    }

    public MainFrame() {
        try {
            AppConfig.loadConfig();
        } catch (IOException e) {
            AppConfig.save();
        }
        mopsimConfig = new MOPSimConfigGroup();

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
        painter = new CompoundPainter<>();
        mapViewer.setOverlayPainter(painter);

        mopPoints = new HashSet<>();
        mopInfos = new HashSet<>();
        listeners = new ArrayList<>();
        method = new CustomMethod();
    }

    public void setMopPoints(Set<MopPoint> mopPoints) {
        this.mopPoints = mopPoints;
        repaint();
    }

    public void setMopPointsFromFile(File file) {
        Set<MopInfo> mopInfosTemp;
        String filepath = file.getAbsolutePath();
        if (filepath.endsWith(".json")) {
            try {
                JSONObject json = JSONParser.readJsonFromFile(filepath);
                mopInfosTemp = JSONToMopParser.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
                mopInfosTemp = null;
            }
        }
        // Adding MOPs from xlsx file is currently disabled.
        // It's not crucial for the program:
        // in case of existing MOPs we can relay on data imported from server,
        // in case of added MOPs we can export them to json file to import them later.
        // If for some reason you need that feature, uncomment following lines.
        // Please note, that imported xlsx file should have very specific format
        // (order of columns etc.). For reference, check MOP-12.2017-final2.xlsx file.
        /*
        else if (filepath.endsWith(".xlsx")) {
            XlsToMopParser xlsToMopParser = new XlsToMopParser(file);
            mopInfosTemp = xlsToMopParser.parseMops();
        }
        */
        else {
            System.out.println("Failed to read file " + filepath);
            mopInfosTemp = null;
        }
        if (mopInfosTemp == null) {
            JOptionPane.showMessageDialog(frame,
                    "Wskazany plik nie istnieje lub jest w złym formacie.",
                    "Zły format pliku",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            mopInfos = mopInfosTemp;
            this.mopPoints = mopInfos.stream().map((MopInfo m) ->
                    new MopPoint(m.getName(), m, MopType.EXISTING, this)).collect(Collectors.toSet());
            JOptionPane.showMessageDialog(this.getFrame(),
                    "Poprawnie wczytano plik.");
            AppConfig.setMopFilename(file.getName());
            AppConfig.save();
            routesMap = TrafficInfoParser.assignMopsToRoutes(this);
            repaint();
        }
    }

    public void setMopPointsFromServer() {
        String urlQueryString = AppConfig.getMopsUrl();
        ServerDataHandler serverDataHandler = new ServerDataHandler(urlQueryString);
        try {
            mopInfos = serverDataHandler.parseMops();
            this.mopPoints = mopInfos.stream().map((MopInfo m) -> new MopPoint(m.getName(),
                    m, MopType.EXISTING, this)).collect(Collectors.toSet());
            repaint();
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

    public void setMopPointsFromServerOrFile(File file) {
        String urlQueryString = AppConfig.getMopsUrl();
        ServerDataHandler serverDataHandler = new ServerDataHandler(urlQueryString);
        try {
            mopInfos = serverDataHandler.parseMops();
            this.mopPoints = mopInfos.stream().map((MopInfo m) -> new MopPoint(m.getName(),
                    m, MopType.EXISTING, this)).collect(Collectors.toSet());
            routesMap = TrafficInfoParser.assignMopsToRoutes(this);
            repaint();
        } catch (Exception e) {
            setMopPointsFromFile(file);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public Set<MopInfo> getMopInfos() {
        return mopInfos;
    }

    public void show() throws Exception {

        File mapFile = AppConfig.getFileProd(AppConfig.getMapOsmFilename());
        File mopsFile = AppConfig.getFileProd(AppConfig.getMopFilename());
        File SDRFile = AppConfig.getFileProd(AppConfig.getSDRFilename());

        trafficMap = new TrafficMap(mapFile);

        this.routesMap = TrafficInfoParser.assignRoutes(this, SDRFile);

        setMopPointsFromServerOrFile(mopsFile);

        MainMenu mainMenu = new MainMenu(this);
        frame.pack();
        frame.setVisible(true);

    }

    public void generateRoutesMap(RoutesMap routesMap) {
        trafficMap.addGeopositions(routesMap);
        this.routesMap = routesMap;
    }

    public void changeMethod(Method method) {
        this.method = method;
        routesMap.setSpacesNeeded(method);
        repaint();
    }

    public RoutesMap getRoutesMap() {
        return routesMap;
    }

    public RoutesMap getAddedRoutesMap() {
        return addedRoutesMap;
    }

    public void setRoutesMap(RoutesMap routesMap) {
        this.routesMap = routesMap;
        generateRoutesMap(routesMap);
        repaint();
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
        if (routesMap == null) {
            return;
        }
        routePainters = routesMap.routePainters();
        for (RoutePainter routePainter : routePainters) {
            painter.addPainter(routePainter);
            MouseListener ml = routePainter.mouseListenerOnRoute(mapViewer);
            mapViewer.addMouseListener(ml);
            listeners.add(ml);
        }
        for (RoutePainter routePainter : addedRoutePainters) {
            painter.addPainter(routePainter);
            MouseListener ml = routePainter.mouseListenerOnRoute(mapViewer);
            mapViewer.addMouseListener(ml);
            listeners.add(ml);
        }
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

    public void addRoute(String name, GeoPosition gpBeg, GeoPosition gpEnd,
                         double milBegin, double milEnd, String dir1, String dir2) {
        Route route = new Route(name, milBegin, milEnd,
                gpBeg, gpEnd, new TrafficInfo());
        route.addSpacesInfo(dir1, new MopParkingSpacesInfo(0, 0, 0));
        route.addSpacesInfo(dir2, new MopParkingSpacesInfo(0, 0, 0));
        addedRoutesMap.add(route);
        List<GeoPosition> track = new ArrayList<>();
        track.add(gpBeg);
        track.add(gpEnd);
        addedRoutePainters.add(new RoutePainter(track, route));
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

    public SearchInfo findNearRouteOrNull(Point point) {
        for (RoutePainter routePainter : routePainters) {
            SearchInfo searchInfo = routePainter.isCloseTo(mapViewer, point);
            if (searchInfo != null) {
                return searchInfo;
            }
        }
        for (RoutePainter routePainter : addedRoutePainters) {
            SearchInfo searchInfo = routePainter.isCloseTo(mapViewer, point);
            if (searchInfo != null) {
                return searchInfo;
            }
        }
        return null;
    }

    public void setMapFromFile(File mapFile) {
        try {
            AppConfig.setMapOsmFilename(mapFile.getName());
            this.show();
            JOptionPane.showMessageDialog(this.getFrame(),
                    "Poprawnie wczytano plik.");
            AppConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getFrame(),
                    "Wskazany plik nie istnieje lub jest w złym formacie.",
                    "Zły format pliku",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
