package data;


import java.io.Serializable;

abstract public class Item<T> implements Serializable {
    private Attribute attribute;
    private T Value;

    public Item(Attribute attribute, T value) {
        this.attribute = attribute;
        this.Value = value;
        // System.out.println("Inizializzazione item");
    }

    protected Attribute getAttribute() {
        return this.attribute;
    }

    public T getValue() {
        return this.Value;
    }

    public String toString() {
        return Value.toString();
    }

    abstract double distance(Object a);


}
