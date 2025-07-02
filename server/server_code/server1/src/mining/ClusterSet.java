package mining;

import data.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ClusterSet implements Iterable<Cluster<Integer>>, Serializable {
    private Set<Cluster<Integer>> C = new TreeSet<>();

    ClusterSet(){
    }

    void add(Cluster<Integer> newCluster) {
        C.add(newCluster);
    }

    @Override
    public Iterator<Cluster<Integer>> iterator() {
        return C.iterator();
    }

    public String toString() {
        String str = "\n";
        Iterator<Cluster<Integer>> it = this.iterator();
        int i = 0;
        while (it.hasNext()) {
            Cluster<Integer> cluster = it.next();
            str += "Cluster " + i++ + ": " + cluster.toString() + "\n";
        }
        return str;
    }

    public String toString(Data data) {
        String str = "\n";
        Iterator<Cluster<Integer>> it = this.iterator();
        int i = 1;
        while (it.hasNext()) {
            Cluster<Integer> cluster = it.next();
            str += i++ + ": " + cluster.toString(data) + "\n";
        }
        return str;
    }
}