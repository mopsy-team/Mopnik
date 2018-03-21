package methods;

import way.TrafficInfo;

import java.util.ArrayList;

public class CustomMethod extends AbstractMethod {
    public CustomMethod() {
        ArrayList<MethodValue> fields = new ArrayList<MethodValue>();
        fields.add(new MethodValue("indicator", "Wskaźnik przeliczeniowy"));
        this.fields = fields;
    }

    @Override
    public MethodResult compute(TrafficInfo trafficInfo) {
        MethodValue ind = getValueOrNull("indicator");
        // TODO(MG) seasonal indicator
        if (ind == null) {
            System.out.println("Nie ma");
            return new MethodResult(0, 0, 0);
        }
        System.out.println(ind.getCar());
        System.out.println(trafficInfo.getCar());
        long car = Math.round(ind.getCar() * trafficInfo.getCar());
        long truck = Math.round(ind.getTruck() * trafficInfo.getTruck());
        long bus = Math.round(ind.getBus() * trafficInfo.getBus());
        return new MethodResult(car, truck, bus);
    }

    @Override
    public String toString() {
        return "Metodyka opracowana";
    }
}
