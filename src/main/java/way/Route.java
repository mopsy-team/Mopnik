package way;

import exceptions.ValidationError;
import methods.CustomMethod;
import methods.Method;
import methods.MethodResult;
import mop.MopParkingSpacesInfo;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.*;

import static util.Validator.makeRoadNumber;

public class Route {
    private String name = "";
    private double mileageBegin;
    private double mileageEnd;
    private GeoPosition geoPositionBegin;
    private GeoPosition geoPositionEnd;
    private TreeMap<Double, GeoPosition> geoPositions = new TreeMap<>();
    private TrafficInfo trafficInfo;
    private Map<String, MopParkingSpacesInfo> spacesByDirection;
    private MethodResult spacesNeeded;
    private boolean directionsSet = false;


    public static Route newRouteForComparing(double mileageBegin) {
        return new Route(mileageBegin);
    }

    public Route(double mileageBegin) {
        this.mileageBegin = mileageBegin;
    }

    public Route(String name, double mileageBegin, double mileageEnd, TreeMap<Double, GeoPosition> geoPositions,
                 TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        this.geoPositions = geoPositions;
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
        if (mileageBegin > mileageEnd) {
            swap();
        }
        this.geoPositionBegin = geoPositions.firstEntry().getValue();
        this.geoPositionEnd = geoPositions.lastEntry().getValue();
    }

    public Route(String name, double mileageBegin, double mileageEnd, GeoPosition geoPositionBegin,
                 GeoPosition geoPositionEnd, TrafficInfo trafficInfo) {
        this(name, mileageBegin, mileageEnd, trafficInfo);
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
        this.geoPositionBegin = geoPositionBegin;
        this.geoPositionEnd = geoPositionEnd;
        if (mileageBegin > mileageEnd) {
            swap();
        }
        geoPositions.put(this.mileageBegin, this.geoPositionBegin);
        double latBegin = this.geoPositionBegin.getLatitude(), lonBegin = this.geoPositionBegin.getLongitude();
        double latDiff = (this.geoPositionEnd.getLatitude() - this.geoPositionBegin.getLatitude()) / (this.mileageEnd - this.mileageBegin);
        double lonDiff = (this.geoPositionEnd.getLongitude() - this.geoPositionBegin.getLongitude()) / (this.mileageEnd - this.mileageBegin);

        for (int i = 1; this.mileageBegin + i < this.mileageEnd; ++i) {
            geoPositions.put(this.mileageBegin + i,
                    new GeoPosition(latBegin + i * latDiff, lonBegin + i * lonDiff));
        }
    }

    public Route(String name, Double mileageBegin, Double mileageEnd, TrafficInfo trafficInfo) {
        this.name = name;
        this.trafficInfo = trafficInfo;
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
        if (mileageBegin > mileageEnd) {
            swap();
        }
        this.spacesByDirection = new HashMap<>();
        String dir1;
        String dir2;
        int number = 0;
        try {
            number = makeRoadNumber(name);
        } catch (ValidationError e) {
            e.printStackTrace();
        }
        if (number % 2 == 0) {
            dir1 = "Wschód";
            dir2 = "Zachód";
        } else {
            dir1 = "Północ";
            dir2 = "Południe";
        }
        this.spacesByDirection.put(dir1, new MopParkingSpacesInfo(0, 0, 0));
        this.spacesByDirection.put(dir2, new MopParkingSpacesInfo(0, 0, 0));
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
        if (!directionsSet) {
            spacesByDirection = new HashMap<>();
        }
        directionsSet = true;
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

    public SearchInfo searchInfo(GeoPosition geoPosition) {
        double diff = Double.MAX_VALUE;
        double mileageRes = 0.;
        GeoPosition geoPositionRes = null;
        for (Map.Entry<Double, GeoPosition> entry : geoPositions.entrySet()) {
            double newDiff = computeDiff(geoPosition, entry.getValue());
            if (newDiff < diff) {
                mileageRes = entry.getKey();
                geoPositionRes = entry.getValue();
                diff = newDiff;
            }
        }
        return new SearchInfo(this, geoPositionRes, mileageRes);
    }

    public GeoPosition getGeoPositionBegin() {
        return geoPositionBegin;
    }

    public GeoPosition getGeoPositionEnd() {
        return geoPositionEnd;
    }

    private double computeDiff(GeoPosition g1, GeoPosition g2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(g1.getLatitude() - g2.getLatitude());
        double lonDistance = Math.toRadians(g1.getLongitude() - g2.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(g1.getLatitude())) * Math.cos(Math.toRadians(g2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

    private void swap () {
        double temp = mileageBegin;
        mileageBegin = mileageEnd;
        mileageEnd = temp;
        GeoPosition temp2 = geoPositionBegin;
        geoPositionBegin = geoPositionEnd;
        geoPositionEnd = temp2;
    }

}
