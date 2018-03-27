package way;

import com.opencsv.CSVReader;
import mop.MopInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class TrafficInfoParser {

    static private WaysMap parseFromFile(Collection<MopInfo> mops, File file) {
        FileInputStream fis;
        WaysMap map;
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis,
                    StandardCharsets.UTF_8);
            CSVReader reader = new CSVReader(isr);
            String[] nextLine;
            map = new WaysMap();
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
                map.add(new Way(name, begin, end, ti));
            }
        } catch (Exception e) {
            return null;
        }
        return map;
    }

    static public int assignWays(Collection<MopInfo> mopInfos, File file) {
        WaysMap waysMap = parseFromFile(mopInfos, file);
        if (waysMap == null) {
            return -1;
        }
        for (MopInfo mop : mopInfos) {
            Way way = waysMap.find(mop.getRoad(), mop.getMileage());
            if (way != null) {
                mop.setWay(way);
            } else {
                mop.setWay(new Way());
            }
        }
        return 0;
    }
}
