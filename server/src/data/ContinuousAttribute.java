package data;


import java.io.Serializable;

public class ContinuousAttribute extends Attribute implements Serializable {
    private double min;
    private double max;

    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    protected double getScaledValue(double v) {
        double v1 = ((v-min)/(max-min));
        return v1;
    }

}
