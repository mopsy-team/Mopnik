package mop;

import org.jxmapviewer.viewer.GeoPosition;
import way.TrafficInfo;
import way.Way;

public class MopInfo {

    // General information about MOP
    private final String branch;
    private final String locality;
    private final String name;
    private final GeoPosition geoPosition;
    private final String road;
    private final String direction;
    private final int type;
    private final double mileage;
    private final MopParkingSpacesInfo parkingSpacesInfo;
    private final MopEquipmentInfo equipmentInfo;
    private Way way = new Way();

    public MopInfo(String branch, String locality, String name, GeoPosition geoPosition,
                   String road, String direction, int type,
                   MopParkingSpacesInfo parkingSpacesInfo, MopEquipmentInfo equipmentInfo, double mileage) {
        this.branch = branch;
        this.locality = locality;
        this.name = name;
        this.geoPosition = geoPosition;
        this.road = road;
        this.direction = direction;
        this.type = type;
        this.parkingSpacesInfo = parkingSpacesInfo;
        this.equipmentInfo = equipmentInfo;
        this.mileage = mileage;
    }

    public String getBranch() {
        return branch;
    }

    public String getLocality() {
        return locality;
    }

    public String getName() {
        return name;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public String getRoad() {
        return road;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getType() {
        return type;
    }

    public MopParkingSpacesInfo getParkingSpacesInfo() {
        return parkingSpacesInfo;
    }

    public MopEquipmentInfo getEquipmentInfo() {
        return equipmentInfo;
    }

    public double getMileage() {
        return mileage;
    }

    public Way getWay() {
        return way;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public TrafficInfo getTrafficInfo() {
        return way.getTrafficInfo();
    }
}
