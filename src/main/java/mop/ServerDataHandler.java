package mop;

import org.json.JSONException;
import org.json.JSONObject;
import util.JSONParser;

import java.io.IOException;
import java.util.HashSet;

public class ServerDataHandler {

    private String url;

    public ServerDataHandler(String url) {
        this.url = url;
    }

    public HashSet<MopInfo> parseMops() throws IOException {
        JSONObject json = JSONParser.readJsonFromUrl(url);
        HashSet<MopInfo> res = JSONToMopParser.parseJSON(json);
        if (res.size() == 0) {
            throw new JSONException("Data on server contain no valid MOPs");
        }
        return res;
    }
}
