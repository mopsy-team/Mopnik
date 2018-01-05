package mop;

public class MopEquipmentInfo {
    private boolean security;
    private boolean fence;
    private boolean cctv;
    private boolean light;
    private boolean petrolStation;
    private boolean dangerousGoods;
    private boolean restaurant;
    private boolean accomodation;
    private boolean toilet;
    private boolean carWash;
    private boolean carRepairShop;

    public MopEquipmentInfo(boolean security, boolean fence, boolean cctv, boolean light,
                            boolean petrolStation, boolean dangerousGoods, boolean restaurant,
                            boolean accomodation, boolean toilet, boolean carWash,
                            boolean carRepairShop) {
        this.security = security;
        this.fence = fence;
        this.cctv = cctv;
        this.light = light;
        this.petrolStation = petrolStation;
        this.dangerousGoods = dangerousGoods;
        this.restaurant = restaurant;
        this.accomodation = accomodation;
        this.toilet = toilet;
        this.carWash = carWash;
        this.carRepairShop = carRepairShop;
    }

    public boolean isSecurity() {
        return security;
    }

    public boolean isFence() {
        return fence;
    }

    public boolean isCctv() {
        return cctv;
    }

    public boolean isLight() {
        return light;
    }

    public boolean isPetrolStation() {
        return petrolStation;
    }

    public boolean isDangerousGoods() {
        return dangerousGoods;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public boolean isAccomodation() {
        return accomodation;
    }

    public boolean isToilet() {
        return toilet;
    }

    public boolean isCarWash() {
        return carWash;
    }

    public boolean isCarRepairShop() {
        return carRepairShop;
    }
}
