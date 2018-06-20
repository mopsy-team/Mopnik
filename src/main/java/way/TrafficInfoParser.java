package way;

import com.opencsv.CSVReader;
import elements.MainFrame;
import mop.MopInfo;
import mop.MopParkingSpacesInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class TrafficInfoParser {

    static private RoutesMap parseFromFile(File file) {
        RoutesMap routesMap = new RoutesMap();
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis,
                    StandardCharsets.UTF_8);
            CSVReader reader = new CSVReader(isr);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String name = nextLine[1];
                double begin = Double.parseDouble(nextLine[3]) + Double.parseDouble(nextLine[4]) / 1000;
                double end = Double.parseDouble(nextLine[5]) + Double.parseDouble(nextLine[6]) / 1000;
                int sum = Integer.parseInt(nextLine[10]);
                int motorcycle = Integer.parseInt(nextLine[11]);
                int car = Integer.parseInt(nextLine[12]);
                int van = Integer.parseInt(nextLine[13]);
                int truckNoTrail = Integer.parseInt(nextLine[14]);
                int truckWithTrail = Integer.parseInt(nextLine[15]);
                int bus = Integer.parseInt(nextLine[16]);
                int tractor = Integer.parseInt(nextLine[17]);
                int bicycle = Integer.parseInt(nextLine[18]);
                TrafficInfo ti = new TrafficInfo(sum, motorcycle, car, van, truckNoTrail, truckWithTrail, bus, tractor, bicycle);
                routesMap.add(new Route(name, begin, end, ti));
            }
        } catch (Exception e) {
            return null;
        }
        return routesMap;
    }

    static public RoutesMap assignRoutes(MainFrame mainFrame, File file) throws Exception {
        RoutesMap routesMap = parseFromFile(file);
        if (routesMap == null) {
            throw new Exception("Wrong SDR file");
        }
        mainFrame.generateRoutesMap(routesMap);
        return assignMopsToRoutes(mainFrame);
    }

    static public RoutesMap assignMopsToRoutes(MainFrame mainFrame) {
        Collection<MopInfo> mopInfos = mainFrame.getMopInfos();
        RoutesMap routesMap = mainFrame.getRoutesMap();
        for (MopInfo mop : mopInfos) {
            Route route = routesMap.findRouteByGeoPosition(mop.getGeoPosition());
            if (route == null ||
                    !route.getName().contains(mop.getRoad())
                    || mop.getMileage() > route.getMileageEnd()
                    || mop.getMileage() < route.getMileageBegin()) {
                route = routesMap.find(mop.getRoad(), mop.getMileage());
            }
            if (route != null) {
                mop.setRoute(route);
                MopParkingSpacesInfo mopParkingSpacesInfo = mop.getParkingSpacesInfo();
                route.addSpacesInfo(mop.getDirection(), mopParkingSpacesInfo);
            }
        }
        return routesMap;

    }
}
