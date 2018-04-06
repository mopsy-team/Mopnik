package way;

public class Route {
    private String name;
    private double mileageBegin;
    private double mileageEnd;
    private TrafficInfo trafficInfo;

    public Route(String name, double mileageBegin, double mileageEnd, TrafficInfo trafficInfo) {
        this.name = name;
        this.trafficInfo = trafficInfo;
        this.mileageBegin = mileageBegin;
        this.mileageEnd = mileageEnd;
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

    public TrafficInfo getTrafficInfo() {
        return trafficInfo;
    }
}
