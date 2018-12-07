package mop;

public class MopParkingSpacesInfo {

    private int carSpaces;
    private int truckSpaces;
    private int busSpaces;

    public MopParkingSpacesInfo() {
        new MopParkingSpacesInfo(0, 0, 0);
    }

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

    public void add(MopParkingSpacesInfo mr) {
        this.carSpaces += mr.getCarSpaces();
        this.truckSpaces += mr.getTruckSpaces();
        this.busSpaces += mr.getBusSpaces();
    }

    public void clear() {
        this.carSpaces = 0;
        this.truckSpaces = 0;
        this.busSpaces = 0;
    }
}
