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

    public int getCarSpaces() {
        return carSpaces;
    }

    public int getTruckSpaces() {
        return truckSpaces;
    }

    public int getBusSpaces() {
        return busSpaces;
    }
}
