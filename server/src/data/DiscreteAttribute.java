package data;
import java.io.Serializable;
import java.util.TreeSet;
import java.util.Iterator;

// classe per istanziare concretamente gli attributi (outlook, etc)

public class DiscreteAttribute<T extends Comparable<T>> extends Attribute implements Iterable<T>, Serializable {
    private TreeSet<T> values;

    public DiscreteAttribute(String name, int index, T[] valuesArray) {
        super(name, index);
        this.values = new TreeSet<>();
        for (T value : valuesArray) {
            this.values.add(value);
        }
    }

    public DiscreteAttribute() {
        super(null, -1);
    }

    private int getNumberOfValues() {
        return values.size();
    }

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }
}