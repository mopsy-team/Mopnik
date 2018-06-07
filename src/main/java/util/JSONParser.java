package util;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONParser {
    private static String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
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
        String dataFromUrl = readFile(filepath, Charset.defaultCharset());
        return new JSONObject(dataFromUrl);
    }

    public static void writeJsonToFile(JSONObject jsonObject, String filepath) throws FileNotFoundException {
        String jsonString = jsonObject.toString();
        PrintWriter out = new PrintWriter(filepath);
        out.print(jsonString);
        out.close();
    }
}
