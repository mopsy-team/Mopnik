package config;

import java.io.File;

public class AppConfig {
    private static final String mopXlsxFilename = "MOP-12.2017-final2.xlsx";
    private static final String mopCSVFilename = "mop_data_switz.csv"; //todo
    private static final String mapOsmFilename = "poland-latest6.osm";
    private static final String mapXmlFilename = "poland_network.xml";
    private static final String carMatrixFilename = "car_matrix.csv";
    private static final String truckMatrixFilename = "truck_matrix.csv";
    private static final String busMatrixFilename = "bus_matrix.csv";
    private static final String mopsUrl = "http://reach.mimuw.edu.pl:8008/mops/?format=json";
    private static final String sumMatrixFilename = "kraj.csv";

    public static String getMopXlsxFilename() {
        return mopXlsxFilename;
    }

    public static String getMopCSVFilename() {
        return mopCSVFilename;
    }

    public static String getSumMatrixFilename() {
        return sumMatrixFilename;
    }

    public static String getCarMatrixFilename() {
        return carMatrixFilename;
    }

    public static String getTruckMatrixFilename() {
        return truckMatrixFilename;
    }

    public static String getBusMatrixFilename() {
        return busMatrixFilename;
    }

    public static String getMopsUrl() {
        return mopsUrl;
    }

    public static String getMapOsmFilename() {
        return mapOsmFilename;
    }

    public static String getMapXmlFilename() {
        return mapXmlFilename;
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

}
