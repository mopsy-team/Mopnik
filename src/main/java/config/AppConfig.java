package config;

public class AppConfig {
    private static String mopsUrl = "http://reach.mimuw.edu.pl:8008/mops/?format=json";

    public static String getMopsUrl() {
        return mopsUrl;
    }
}
