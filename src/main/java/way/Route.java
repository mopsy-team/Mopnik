package way;

import methods.CustomMethod;
import methods.Method;
import methods.MethodResult;
import mop.MopParkingSpacesInfo;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
    private String name;
    private double mileageBegin;
    private double mileageEnd;
    private List<GeoPosition> geoPositions = new ArrayList<>();
    private TrafficInfo trafficInfo;
    private Map<String, MopParkingSpacesInfo> spacesByDirection;
    private MethodResult spacesNeeded;

    public Route(String name, double mileageBegin, double mileageEnd, List<GeoPosition> geoPositions,
                 TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        this.geoPositions = geoPositions;
    }

    public Route(String name, double mileageBegin, double mileageEnd, GeoPosition geoPositionBegin,
                 GeoPosition geoPositionEnd, TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        geoPositions.add(geoPositionBegin);
        double latBegin = geoPositionBegin.getLatitude(), lonBegin = geoPositionBegin.getLongitude();
        double latDiff = (geoPositionEnd.getLatitude() - geoPositionBegin.getLatitude()) / (mileageEnd - mileageBegin);
        double lonDiff = (geoPositionEnd.getLongitude() - geoPositionBegin.getLongitude()) / (mileageEnd - mileageBegin);
        for (int i = 1; mileageBegin * i < mileageEnd; ++i) {
            geoPositions.add(new GeoPosition(latBegin + i * latDiff, lonBegin + i * lonDiff));
        }
    }

    public Route(String name, double mileageBegin, double mileageEnd, TrafficInfo trafficInfo) {
        this.name = name;
        this.trafficInfo = trafficInfo;
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
        this.spacesByDirection = new HashMap<>();
        this.spacesByDirection.put("1", new MopParkingSpacesInfo(0, 0, 0));
        this.spacesByDirection.put("2", new MopParkingSpacesInfo(0, 0, 0));
        if (trafficInfo != null) {
            this.spacesNeeded = new CustomMethod().compute(this);
        }
    }

    public Route() {
        this("", 0, 0, new TrafficInfo());
    }

    public String getName() {
        return name;
    }

    public double getMileageBegin() {
        return mileageBegin;
    }

    public double getMileageEnd() {
        return mileageEnd;
    }

    public double getDistance() {
        return mileageEnd - mileageBegin;
    }

    public Map<String, MopParkingSpacesInfo> getSpacesByDirection() {
        return spacesByDirection;
    }

    public NSpaces nSpaces() {
        int lacks = 0;
        int toomany = 0;
        for (MopParkingSpacesInfo mr : spacesByDirection.values()) {
            lacks += (mr.getCarSpaces() < spacesNeeded.getCar() ? 1 : 0)
                    + (mr.getTruckSpaces() < spacesNeeded.getTruck() ? 1 : 0);
            toomany += (mr.getCarSpaces() > spacesNeeded.getCar() * 1.5 ? 1 : 0)
                    + (mr.getTruckSpaces() > spacesNeeded.getTruck() * 1.5 ? 1 : 0);
        }
        if (lacks > 2) {
            return NSpaces.VERY_LOW;
        }
        if (lacks > 0) {
            return NSpaces.LOW;
        }
        if (toomany < 4) {
            return NSpaces.SUFFICIENT;
        }
        return NSpaces.LARGE;
    }

    public TrafficInfo getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(TrafficInfo trafficInfo) {
        this.trafficInfo = trafficInfo;
        this.spacesNeeded = new CustomMethod().compute(this);
    }

    public List<GeoPosition> getGeoPositions() {
        return geoPositions;
    }

    public void addSpacesInfo(String direction, MopParkingSpacesInfo mopParkingSpacesInfo) {
        if (spacesByDirection.size() == 2 && spacesByDirection.containsKey("1")) {
            spacesByDirection = new HashMap<>();
        }
        if (direction.equals("1") || direction.equals("2")) {
            return;
        }
        if (spacesByDirection.containsKey(direction)) {
            spacesByDirection.get(direction).add(mopParkingSpacesInfo);
        } else {
            spacesByDirection.put(direction, mopParkingSpacesInfo);
        }
    }

    public void removeSpacesInfo(String direction, MopParkingSpacesInfo mopParkingSpacesInfo) {
        MopParkingSpacesInfo mr = new MopParkingSpacesInfo(-mopParkingSpacesInfo.getCarSpaces(),
                -mopParkingSpacesInfo.getTruckSpaces(),
                -mopParkingSpacesInfo.getBusSpaces());
        addSpacesInfo(direction, mr);
    }

    public Route add(Route route) {
        String name = this.name;
        Route first, second;
        if (mileageBegin < route.mileageBegin) {
            first = this;
            second = route;
        } else {
            first = this;
            second = route;
        }
        double milbeg = first.mileageBegin;
        double milend = second.mileageEnd;
        first.geoPositions.addAll(second.geoPositions);
        TrafficInfo tinfo = trafficInfo.add(route.getTrafficInfo());
        Route res = new Route(name, milbeg, milend, first.geoPositions, tinfo);
        for (Map.Entry<String, MopParkingSpacesInfo> entry : route.getSpacesByDirection().entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, MopParkingSpacesInfo> entry : this.spacesByDirection.entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public void setSpacesNeeded(MethodResult mr) {
        spacesNeeded = mr;
    }
}
