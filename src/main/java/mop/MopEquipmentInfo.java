package mop;

public class MopEquipmentInfo {
    private boolean security;
    private boolean fence;
    private boolean cctv;
    private boolean light;
    private boolean petrolStation;
    private boolean dangerousGoods;
    private boolean restaurant;
    private boolean accommodation;
    private boolean toilet;
    private boolean carWash;
    private boolean carRepairShop;

    public MopEquipmentInfo() {
        this(false, false, false, false, false,
                false, false, false, false, false, false);
    }

    public MopEquipmentInfo(boolean[] booleans){
        this(booleans[0], booleans[1], booleans[2], booleans[3], booleans[4], booleans[5],
                    booleans[6], booleans[7], booleans[8], booleans[9], booleans[10]);
    }

    public MopEquipmentInfo(boolean security, boolean fence, boolean cctv, boolean light,
                            boolean petrolStation, boolean dangerousGoods, boolean restaurant,
                            boolean accommodation, boolean toilet, boolean carWash,
                            boolean carRepairShop) {
        this.security = security;
        this.fence = fence;
        this.cctv = cctv;
        this.light = light;
        this.petrolStation = petrolStation;
        this.dangerousGoods = dangerousGoods;
        this.restaurant = restaurant;
        this.accommodation = accommodation;
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

    public boolean isAccommodation() {
        return accommodation;
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
