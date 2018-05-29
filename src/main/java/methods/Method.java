package methods;

import way.Route;

import java.util.Collection;

public interface Method {
    Collection<MethodValue> getFields();

    void setFields(Collection<MethodValue> fields);

    MethodResult compute(Route route);

    boolean isSet();
}
