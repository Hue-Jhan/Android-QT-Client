package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

class Cluster<T extends Integer> implements Iterable<Integer>, Comparable<Cluster<T>>, Serializable {
    private Tuple centroid;
    private HashSet<Integer> clusteredData;

	/*Cluster(){
	}*/

    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<>();
    }

    Tuple getCentroid() {
        return centroid;
    }

    boolean addData(T id) {
        return clusteredData.add(id);
    }

    private boolean contain(T id) {
        return clusteredData.contains(id);
    }

    private void removeTuple(T id) {
        clusteredData.remove(id);
    }

    int getSize() {
        return clusteredData.size();
    }


    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    @Override
    public int compareTo(Cluster other) {
        int sizeComparison = Integer.compare(this.getSize(), other.getSize());
        if (sizeComparison != 0)
            return sizeComparison;

        for (int i = 0; i < this.centroid.getLength(); i++) {
            Object val1 = this.centroid.get(i);
            Object val2 = other.centroid.get(i);

            int cmp = val1.toString().compareTo(val2.toString());
            if (cmp != 0)
                return cmp;
        }
        return 0;
    }

    public String toString() {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i);
        str += ")";
        return str;
    }

    String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";

        str += ")\nExamples:\n";

        Iterator<Integer> it = clusteredData.iterator();
        while (it.hasNext()) {
            int id = it.next();
            str += "[";
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                str += data.getValue(id, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(id)) + "\n";
        }

        str += "AvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        str += " \n";
        return str;
    }

}
