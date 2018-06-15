package way;

import methods.CustomMethod;
import methods.Method;
import methods.MethodResult;
import mop.MopParkingSpacesInfo;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.*;

import static util.DistanceCounter.computeDiff;

public class Route {
    private String name;
    private double mileageBegin;
    private double mileageEnd;
    private Map<Double, GeoPosition> geoPositions = new TreeMap<>();
    private TrafficInfo trafficInfo;
    private Map<String, MopParkingSpacesInfo> spacesByDirection;
    private MethodResult spacesNeeded;

    public Route(String name, double mileageBegin, double mileageEnd, Map<Double, GeoPosition> geoPositions,
                 TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        this.geoPositions = geoPositions;
    }

    public Route(String name, double mileageBegin, double mileageEnd, GeoPosition geoPositionBegin,
                 GeoPosition geoPositionEnd, TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        geoPositions.put(mileageBegin, geoPositionBegin);
        double latBegin = geoPositionBegin.getLatitude(), lonBegin = geoPositionBegin.getLongitude();
        double latDiff = (geoPositionEnd.getLatitude() - geoPositionBegin.getLatitude()) / (mileageEnd - mileageBegin);
        double lonDiff = (geoPositionEnd.getLongitude() - geoPositionBegin.getLongitude()) / (mileageEnd - mileageBegin);

        for (int i = 1; mileageBegin + i < mileageEnd; ++i) {
            geoPositions.put(mileageBegin + i,
                    new GeoPosition(latBegin + i * latDiff, lonBegin + i * lonDiff));
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
        this("", 0., 0., new TrafficInfo());
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
        int tooMany = 0;
        if (spacesNeeded == null || spacesByDirection == null) {
            return NSpaces.NO_INFO;
        }
        for (MopParkingSpacesInfo mr : spacesByDirection.values()) {
            lacks += (mr.getCarSpaces() < spacesNeeded.getCar() ? 1 : 0)
                    + (mr.getTruckSpaces() < spacesNeeded.getTruck() ? 1 : 0);
            tooMany += (mr.getCarSpaces() > spacesNeeded.getCar() * 1.5 ? 1 : 0)
                    + (mr.getTruckSpaces() > spacesNeeded.getTruck() * 1.5 ? 1 : 0);
        }
        if (lacks > 2) {
            return NSpaces.VERY_LOW;
        }
        if (lacks > 0) {
            return NSpaces.LOW;
        }
        if (tooMany < 4) {
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

    public Collection<GeoPosition> getGeoPositions() {
        return geoPositions.values();
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
        double milBeg = first.mileageBegin;
        double milEnd = second.mileageEnd;
        first.geoPositions.putAll(second.geoPositions);
        TrafficInfo tInfo = trafficInfo.add(route.getTrafficInfo());
        Route res = new Route(name, milBeg, milEnd, first.geoPositions, tInfo);
        for (Map.Entry<String, MopParkingSpacesInfo> entry : route.getSpacesByDirection().entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, MopParkingSpacesInfo> entry : this.spacesByDirection.entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public RoutePainter getRoutePainter() {
        if (geoPositions.size() > 0) {
            return new RoutePainter(new LinkedList<>(geoPositions.values()), this);
        } else {
            return null;
        }
    }

    public void addGeoposition(Double mileageBegin, GeoPosition geoPosition) {
        geoPositions.put(mileageBegin, geoPosition);
    }

    public MethodResult getSpacesNeeded() {
        return spacesNeeded;
    }

    public void setSpacesNeeded(MethodResult mr) {
        spacesNeeded = mr;
    }

    public void computeSpacesNeeded(Method method) {
        spacesNeeded = method.compute(this);
    }

    public double getDistanceFromGeoPosition(GeoPosition geoPosition) {
        double diff = Double.MAX_VALUE;
        for (GeoPosition gp : geoPositions.values()) {
            double newDiff = computeDiff(geoPosition, gp);
            if (newDiff < diff) {
                diff = newDiff;
            }
        }
        return diff;
    }

    public double findMileage(GeoPosition gp) {
        double diff = Double.MAX_VALUE;
        double mileage = 0.;
        for (double key : geoPositions.keySet()) {
            double newDiff = computeDiff(geoPositions.get(key), gp);
            if (newDiff < diff) {
                diff = newDiff;
                mileage = key;
            }
        }
        return mileage;
    }
}
