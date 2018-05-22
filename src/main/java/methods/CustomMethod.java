package methods;

import way.Route;
import way.TrafficInfo;

import java.util.ArrayList;

public class CustomMethod extends AbstractMethod {
    public CustomMethod() {
        ArrayList<MethodValue> fields = new ArrayList<MethodValue>();
        fields.add(new MethodValue("indicator", "Wskaźnik przeliczeniowy", 0.0036, 0.0039, 0.0039));
        fields.add(new MethodValue("seasonal indicator", "Wskaźnik sezonowy", 1.7, 1.5, 1.5));
        this.fields = fields;
    }

    @Override
    public MethodResult compute(Route route) {
        TrafficInfo trafficInfo = route.getTrafficInfo();
        Double distPer15km = route.getDistance() / 15;
        MethodValue ind = getValueOrNull("indicator");
        MethodValue sind = getValueOrNull("seasonal indicator");
        if (ind == null || sind == null) {
            return new MethodResult(0, 0, 0);
        }
        long car = Math.round(ind.getCar() * trafficInfo.getCar()/2 * sind.getCar() * distPer15km);
        long truck = Math.round(ind.getTruck() * trafficInfo.getTruck()/2 * sind.getTruck() * distPer15km);
        long bus = Math.round(ind.getBus() * trafficInfo.getBus()/2 * sind.getBus() * distPer15km);
        return new MethodResult(car, truck, bus);
    }

    @Override
    public String toString() {
        return "Metodyka domyślna";
    }
}
