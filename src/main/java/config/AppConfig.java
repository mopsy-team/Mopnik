package config;

import java.io.File;

public class AppConfig {
    private static final String mopFilename = "MOP-12.2017-final2.xlsx";
    private static final String mapFilename = "poland-latest6.osm";
    private static final String matrixFilename = "kraj.csv";
    private static final String mopsUrl = "http://reach.mimuw.edu.pl:8008/mops/?format=json";

    public static String getMopFilename() {
        return mopFilename;
    }

    public static String getMapFilename() {
        return mapFilename;
    }

    public static String getMatrixFilename() {
        return matrixFilename;
    }

    public static String getPath(String filename) {
        String path;
        try {
            path = AppConfig.class.getClassLoader().getResource(filename).getPath();
        }
        catch (NullPointerException e) {
            path = null;
        }
        return path;
    }


    public static File getFile(String filename) {
        File file;
        try {
            file = new File(AppConfig.class.getClassLoader().getResource(filename).getFile());
        }
        catch (NullPointerException e) {
            file = null;
        }
        return file;
    }

    public static String getMopsUrl() {
        return mopsUrl;
    }
}
