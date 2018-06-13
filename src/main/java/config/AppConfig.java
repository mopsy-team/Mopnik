package config;

import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppConfig {
    private static final String shadowFilename = "config.txt";
    private static String mopXlsxFilename = "MOP-12.2017-final2.xlsx";
    private static String mopJSONFilename = "mop_data.json"; //todo
    private static String mapOsmFilename = "poland-latest6.osm";
    private static String SDRFilename = "kraj.csv";
    private static String mopsUrl = "http://reach.mimuw.edu.pl:8008/mops/?format=json";


    public static void setMopXlsxFilename(String mopXlsxFilename) {
        AppConfig.mopXlsxFilename = mopXlsxFilename;
    }

    public static void setMopJSONFilename(String mopJSONFilename) {
        AppConfig.mopJSONFilename = mopJSONFilename;
    }

    public static void setMapOsmFilename(String mapOsmFilename) {
        AppConfig.mapOsmFilename = mapOsmFilename;
    }

    public static void setMopsUrl(String mopsUrl) {
        AppConfig.mopsUrl = mopsUrl;
    }

    public static void setSDRFilename(String SDRFilename) {
        AppConfig.SDRFilename = SDRFilename;
    }

    public static String getMopXlsxFilename() {
        return mopXlsxFilename;
    }

    public static String getMopJSONFilename() {
        return mopJSONFilename;
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
            mopXlsxFilename = nextLine[0];
            mopJSONFilename = nextLine[1];
            mapOsmFilename = nextLine[2];
            mopsUrl = nextLine[3];
            SDRFilename = nextLine[4];
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(shadowFilename);
            writer.append(String.format("%s,%s,%s,%s,%s", mopXlsxFilename, mopJSONFilename,
                    mapOsmFilename, mopsUrl, SDRFilename));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
