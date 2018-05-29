package way;

import com.opencsv.CSVReader;
import elements.MainFrame;
import methods.MethodResult;
import mop.MopInfo;
import mop.MopParkingSpacesInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class TrafficInfoParser {

    static private void parseFromFile(File file, RoutesMap routesMap) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis,
                    StandardCharsets.UTF_8);
            CSVReader reader = new CSVReader(isr);
            String[] nextLine;
            nextLine = reader.readNext();
            for (String s : nextLine) {
                System.out.print(s + "| ");
            }
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
                routesMap.assignTrafficInfo(name, begin, end, ti);
            }
        } catch (Exception e) {
            return;
        }
        return;
    }

    static public int assignRoutes(MainFrame mainFrame, File file) {
        Collection<MopInfo> mopInfos = mainFrame.getMopInfos();
        parseFromFile(file, mainFrame.getRoutesMap());
        for (MopInfo mop : mopInfos) {
            Route route = mainFrame.getRoutesMap().find(mop.getRoad(), mop.getMileage());
            if (route != null) {
                mop.setRoute(route);
                MopParkingSpacesInfo mopParkingSpacesInfo = mop.getParkingSpacesInfo();
                MethodResult mr = new MethodResult(mopParkingSpacesInfo.getCarSpaces(),
                        mopParkingSpacesInfo.getTruckSpaces(), mopParkingSpacesInfo.getBusSpaces());
                route.addSpacesInfo(mop.getDirection(), mr);
            } else {
                mop.setRoute(new Route());
            }
        }
        mainFrame.repaint();
        return 0;
    }
}
