package mop;

import org.json.JSONException;
import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;
import util.JSONParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class ServerDataHandler {

    private String url;

    public ServerDataHandler(String url) {
        this.url = url;
    }

    public HashSet<MopInfo> parseMops() throws IOException {

        HashSet<MopInfo> res = new HashSet<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.readJsonFromUrl(url);
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();

            if (json.get(key) instanceof JSONObject) {
                JSONObject mop = (JSONObject) json.get(key);

                String branch = mop.getString("department");
                String locality = mop.getString("town");
                String name = mop.getString("title");
                String road = mop.getString("road_number");
                JSONObject coords = mop.getJSONObject("coords");
                Double x = coords.getDouble("latitude");
                Double y = coords.getDouble("longitude");
                GeoPosition g = new GeoPosition(x, y);

                String mileageString = mop.getString("chainage").replace('+', '.');
                double mileage;
                try {
                    mileage = Double.parseDouble(mileageString);
                } catch (NumberFormatException e) {
                    mileage = -1.;
                }
                int type = mop.getInt("type");

                JSONObject places = mop.getJSONObject("available");
                int bus = places.getInt("bus");
                int car = places.getInt("car");
                int truck = places.getInt("truck");
                MopParkingSpacesInfo parkingSpacesInfo = new MopParkingSpacesInfo(car, truck, bus);
                JSONObject facilities = (JSONObject) mop.getJSONObject("facilities");
                boolean monitoring = facilities.getBoolean("monitoring");
                boolean garage = facilities.getBoolean("garage");
                boolean toilets = facilities.getBoolean("toilets");
                boolean petrol_station = facilities.getBoolean("petrol_station");
                boolean dangerous_cargo_places = facilities.getBoolean("dangerous_cargo_places");
                boolean fence = facilities.getBoolean("fence");
                boolean restaurant = facilities.getBoolean("restaurant");
                boolean sleeping_places = facilities.getBoolean("sleeping_places");
                boolean car_wash = facilities.getBoolean("car_wash");
                boolean lighting = facilities.getBoolean("lighting");
                boolean security = facilities.getBoolean("security");
                MopEquipmentInfo equipmentInfo = new MopEquipmentInfo(security, fence, monitoring,
                        lighting, petrol_station, dangerous_cargo_places, restaurant,
                        sleeping_places, toilets, car_wash, garage);

                String direction = mop.getString("direction");

                res.add(new MopInfo(branch, locality, name, g, road, direction, type, parkingSpacesInfo,
                        equipmentInfo, mileage));
            }
        }
        System.out.println(res.size());
        if (res.size() == 0) {
            throw new JSONException("Data on server contain no valid MOPs");
        }
        return res;
    }
}
