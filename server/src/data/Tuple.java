package data;


import java.io.Serializable;
import java.util.Set;

public class Tuple implements Serializable {
    private Item [] tuple;

    public Tuple(int size) {
        tuple = new Item[size];
        // System.out.println("Tuple constructed");
    }

    public int getLength() {
        return tuple.length;
    }

    public Item get(int i) {
        return tuple[i];
    }

    public void add(Item c, int i) {
        tuple[i] = c;
    }

    public double getDistance(Tuple obj) {
        double dis = 0.0;
        for (int i = 0; i < tuple.length; i++) {
            dis += tuple[i].distance(obj.get(i).getValue());
            /* prende una tupla, itera per ogni tupla, e per ognuna di esse controlla se i valori (getvalue)
             sono uguali, se non sono uguali la distanza aumenta di 1 per ogni valore diverso */
        }
        return dis;
    }

    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double sumD = 0.0;
        for (int id : clusteredData) {
            double d = getDistance(data.getItemSet(id));  // Ottieni la distanza dalla tupla
            sumD += d;  // Somma le distanze
        }
        return sumD / clusteredData.size();  // Calcola la distanza media
    }

}
