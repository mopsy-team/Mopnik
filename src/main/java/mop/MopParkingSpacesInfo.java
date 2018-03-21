package mop;

public class MopParkingSpacesInfo {

    private int carSpaces;
    private int truckSpaces;
    private int busSpaces;

    public MopParkingSpacesInfo(int carSpaces, int truckSpaces, int busSpaces) {
        this.carSpaces = carSpaces;
        this.truckSpaces = truckSpaces;
        this.busSpaces = busSpaces;
    }

    public Integer getCarSpaces() {
        return carSpaces;
    }

    public Integer getTruckSpaces() {
        return truckSpaces;
    }

    public Integer getBusSpaces() {
        return busSpaces;
    }
}
