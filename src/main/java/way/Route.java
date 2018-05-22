package way;

import methods.CustomMethod;
import methods.MethodResult;
import methods.MethodValue;

import java.util.HashMap;
import java.util.Map;

public class Route {
    private String name;
    private double mileageBegin;
    private double mileageEnd;
    private TrafficInfo trafficInfo;
    private Map<String, MethodResult> spacesByDirection;
    private MethodResult spacesNeeded;

    public Route(String name, double mileageBegin, double mileageEnd, TrafficInfo trafficInfo) {
        this.name = name;
        this.trafficInfo = trafficInfo;
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
        this.spacesByDirection = new HashMap<>();
        this.spacesByDirection.put(" ", new MethodResult(0, 0, 0));
        this.spacesByDirection.put("", new MethodResult(0, 0, 0));
        if (trafficInfo != null)
            this.spacesNeeded = new CustomMethod().compute(this);
    }

    public Route() {
        new Route("", 0, 0, new TrafficInfo());
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

    public Map<String, MethodResult> getSpacesByDirection() {
        return spacesByDirection;
    }

    public NSpaces nSpaces() {
        int lacks = 0;
        int toomany = 0;
        for (MethodResult mr: spacesByDirection.values()) {
            lacks += (mr.getCar() < spacesNeeded.getCar() ? 1 : 0)
                    + (mr.getTruck() < spacesNeeded.getTruck() ? 1 : 0);
            toomany += (mr.getCar() > spacesNeeded.getCar() * 1.5 ? 1 : 0)
                    + (mr.getTruck() > spacesNeeded.getTruck() * 1.5 ? 1 : 0);
        }
        if (lacks > 2){
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

    public void addSpacesInfo(String direction, MethodResult methodResult) {
        if (spacesByDirection.size() == 2 && spacesByDirection.containsKey(" ")){
            spacesByDirection = new HashMap<>();
        }
        if (direction.equals("") || direction.equals(" ")) { return; }
        if (spacesByDirection.containsKey(direction)) {
            spacesByDirection.get(direction).add(methodResult);
        } else {
            spacesByDirection.put(direction, methodResult);
        }
    }

    public Route add(Route route){
        String name = this.name;
        double milbeg = Math.min(mileageBegin, route.mileageBegin);
        double milend = Math.max(mileageEnd, route.mileageEnd);
        TrafficInfo tinfo = trafficInfo.add(route.getTrafficInfo());
        Route res = new Route(name, milbeg, milend, tinfo);
        for (Map.Entry<String, MethodResult> entry : route.getSpacesByDirection().entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, MethodResult> entry : this.spacesByDirection.entrySet()) {
            res.addSpacesInfo(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public void setSpacesNeeded(MethodResult mr) {
        spacesNeeded = mr;
    }
}
