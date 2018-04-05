package way;

public class TrafficInfo {
    private int sum;
    private int motorcycle;
    private int car;
    private int van;
    private int truckNoTrail;
    private int truckTrail;
    private int bus;
    private int tractor;
    private int bicycle;

    public TrafficInfo(int sum, int motorcycle, int car, int van, int truckNoTrail,
                       int truckTrail, int bus, int tractor, int bicycle) {
        this.sum = sum;
        this.motorcycle = motorcycle;
        this.car = car;
        this.van = van;
        this.truckNoTrail = truckNoTrail;
        this.truckTrail = truckTrail;
        this.bus = bus;
        this.tractor = tractor;
        this.bicycle = bicycle;
    }

    public TrafficInfo() {
        new TrafficInfo(-1, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    public Integer getCar() {
        return car + van;
    }

    public Integer getTruck() {
        return truckNoTrail + truckTrail;
    }

    public Integer getBus() {
        return bus;
    }


}
