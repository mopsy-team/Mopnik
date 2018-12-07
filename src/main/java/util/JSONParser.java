package util;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONParser {
    private static String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static JSONObject readJsonFromUrl(String _url) throws IOException {
        String dataFromUrl = readUrl(_url);
        return new JSONObject(dataFromUrl);
    }

    public static JSONObject readJsonFromFile(String filepath) throws IOException {
        String dataFromFile = readFile(filepath, StandardCharsets.UTF_8);
        return new JSONObject(dataFromFile);
    }

    public static void writeJsonToFile(JSONObject jsonObject, String filepath) throws IOException {
        String jsonString = jsonObject.toString();
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filepath), StandardCharsets.UTF_8))) {
            out.write(jsonString);
        }
    }
}
