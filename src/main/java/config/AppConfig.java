package config;

import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppConfig {
    private static final String shadowFilename = "config.txt";
    private static String mopFilename = "MOP-12.2017-final2.xlsx";
    private static String mapOsmFilename = "poland-extract.osm";
    private static String SDRFilename = "kraj.csv";
    private static String mopsUrl = "http://reach.mimuw.edu.pl:8008/mops/?format=json";


    public static void setMopFilename(String mopFilename) {
        AppConfig.mopFilename = mopFilename;
    }

    public static void setMapOsmFilename(String mapOsmFilename) {
        AppConfig.mapOsmFilename = mapOsmFilename;
    }

    public static void setSDRFilename(String SDRFilename) {
        AppConfig.SDRFilename = SDRFilename;
    }

    public static void setMopsUrl(String mopsUrl) {
        AppConfig.mopsUrl = mopsUrl;
    }

    public static String getMopFilename() {
        return mopFilename;
    }

    public static String getSDRFilename() {
        return SDRFilename;
    }

    public static String getMopsUrl() {
        return mopsUrl;
    }

    public static String getMapOsmFilename() {
        return mapOsmFilename;
    }

    public static String getPath(String filename) {
        String path;
        try {
            path = AppConfig.class.getClassLoader().getResource(filename).getPath();
        } catch (NullPointerException e) {
            path = null;
        }
        return path;
    }


    public static File getFile(String filename) {
        File file;
        try {
            file = new File(AppConfig.class.getClassLoader().getResource(filename).getFile());
        } catch (NullPointerException e) {
            file = null;
        }
        return file;
    }


    public static void loadConfig() throws IOException {
        FileInputStream fis = new FileInputStream(shadowFilename);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(isr);
        String[] nextLine;
        if ((nextLine = reader.readNext()) != null) {
            mopFilename = nextLine[0];
            mapOsmFilename = nextLine[1];
            mopsUrl = nextLine[2];
            SDRFilename = nextLine[3];
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(shadowFilename);
            writer.append(String.format("%s,%s,%s,%s", mopFilename,
                    mapOsmFilename, mopsUrl, SDRFilename));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
