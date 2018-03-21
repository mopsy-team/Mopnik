package methods;

import way.TrafficInfo;

import java.util.Collection;

public abstract class AbstractMethod implements Method {
    protected Collection<MethodValue> fields;
    protected boolean isSet = false;

    @Override
    public Collection<MethodValue> getFields() {
        return fields;
    }

    @Override
    public void setFields(Collection<MethodValue> fields) {
        this.fields = fields;
        this.isSet = true;
    }

    @Override
    public abstract MethodResult compute(TrafficInfo trafficInfo);

    @Override
    public boolean isSet() {
        return isSet;
    }

    protected MethodValue getValueOrNull(String key) {
        for (MethodValue methodValue : fields) {
            if (methodValue.getName().equals(key)) {
                return methodValue;
            }
        }
        return null;
    }
}
