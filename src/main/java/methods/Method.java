package methods;

import way.TrafficInfo;

import java.util.Collection;

public interface Method {
    Collection<MethodValue> getFields();

    void setFields(Collection<MethodValue> fields);

    MethodResult compute(TrafficInfo trafficInfo);

    boolean isSet();
}
