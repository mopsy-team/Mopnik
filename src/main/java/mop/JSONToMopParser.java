package mop;

import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JSONToMopParser {
    public static HashSet<MopInfo> parseJSON(JSONObject json) {
        HashSet<MopInfo> res = new HashSet<>();
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (json.get(key) instanceof JSONObject) {
                JSONObject mop = (JSONObject) json.get(key);

                int id = mop.getInt("id");
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

                res.add(new MopInfo(id, branch, locality, name, g, road, direction, type, parkingSpacesInfo,
                        equipmentInfo, mileage));
            }
        }
        return res;
    }

    public static JSONObject parseSet(Set<MopInfo> mops) {
        JSONObject res = new JSONObject();
        for (MopInfo mop : mops) {
            String id = String.valueOf(mop.getId());
            res.put(id, parseMopInfo(mop));
        }
        return res;
    }

    public static JSONObject parseMopInfo(MopInfo mop) {
        JSONObject res = new JSONObject();
        res.put("id", mop.getId());
        res.put("title", mop.getName());

        JSONObject coords = new JSONObject();
        coords.put("latitude", mop.getGeoPosition().getLatitude());
        coords.put("longitude", mop.getGeoPosition().getLongitude());
        res.put("coords", coords);

        JSONObject available = new JSONObject();
        available.put("car", mop.getParkingSpacesInfo().getCarSpaces());
        available.put("truck", mop.getParkingSpacesInfo().getTruckSpaces());
        available.put("bus", mop.getParkingSpacesInfo().getBusSpaces());
        res.put("available", available);

        JSONObject facilities = new JSONObject();
        facilities.put("car_wash", mop.getEquipmentInfo().isCarWash());
        facilities.put("dangerous_cargo_places", mop.getEquipmentInfo().isDangerousGoods());
        facilities.put("toilets", mop.getEquipmentInfo().isToilet());
        facilities.put("fence", mop.getEquipmentInfo().isFence());
        facilities.put("monitoring", mop.getEquipmentInfo().isCctv());
        facilities.put("lighting", mop.getEquipmentInfo().isLight());
        facilities.put("security", mop.getEquipmentInfo().isSecurity());
        facilities.put("garage", mop.getEquipmentInfo().isCarRepairShop());
        facilities.put("petrol_station", mop.getEquipmentInfo().isPetrolStation());
        facilities.put("sleeping_places", mop.getEquipmentInfo().isAccommodation());
        facilities.put("restaurant", mop.getEquipmentInfo().isRestaurant());
        res.put("facilities", facilities);

        String chainage = String.valueOf(mop.getMileage()).replace('.', '+');
        res.put("chainage", chainage);
        res.put("direction", mop.getDirection());
        res.put("road_number", mop.getRoad());
        res.put("town", mop.getLocality());
        res.put("department", mop.getBranch());
        res.put("type", mop.getType());
        return res;
    }
}
