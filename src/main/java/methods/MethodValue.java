package methods;

public class MethodValue {
    private final String name;
    private final String description;
    private Double car;
    private Double truck;
    private Double bus;

    public MethodValue(String name, String description, Double car, Double truck, Double bus) {
        this.name = name;
        this.description = description;
        this.car = car;
        this.truck = truck;
        this.bus = bus;
    }

    public MethodValue(String name, String description) {
        this.name = name;
        this.description = description;
        this.car = 0.;
        this.truck = 0.;
        this.bus = 0.;
    }

    public String getDescription() {
        return description;
    }

    public Double getBus() {
        return bus;
    }

    public void setBus(Double bus) {
        this.bus = bus;
    }

    public Double getCar() {
        return car;
    }

    public void setCar(Double car) {
        this.car = car;
    }

    public Double getTruck() {
        return truck;
    }

    public void setTruck(Double truck) {
        this.truck = truck;
    }

    public String getName() {
        return name;
    }
}