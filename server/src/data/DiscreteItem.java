package data;


import java.io.Serializable;

public class DiscreteItem<T extends Comparable<T>> extends Item<T> implements Serializable {

    public DiscreteItem(DiscreteAttribute<T> attribute, T value) {
        super(attribute, value);
    }

    public DiscreteItem() {
        super(null, null);
    }

    public double distance(Object a) {
        if (getValue().equals(a)){
            return 0;
        } else {
            return 1;
        }
    }

}
