package methods;

public class MethodResult {
    private long car;
    private long truck;
    private long bus;

    public MethodResult(long car, long truck, long bus) {
        this.car = car;
        this.truck = truck;
        this.bus = bus;
    }

    public Long getCar() {
        return car;
    }

    public Long getBus() {
        return bus;
    }

    public Long getTruck() {
        return truck;
    }

}
