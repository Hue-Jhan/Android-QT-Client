package data;

import java.io.Serializable;

public class ContinuousItem extends Item implements Serializable {

    public ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    @Override
    double distance(Object a) {
        if (!(a instanceof Number)) {
            throw new IllegalArgumentException("Expected a numeric value");
        }
        double currentValue = ((Number) getValue()).doubleValue();
        double otherValue = ((Number) a).doubleValue();

        ContinuousAttribute attr = (ContinuousAttribute) getAttribute();
        double scaledCurrent = attr.getScaledValue(currentValue);
        double scaledOther = attr.getScaledValue(otherValue);

        return Math.abs(scaledCurrent - scaledOther);
    }

}