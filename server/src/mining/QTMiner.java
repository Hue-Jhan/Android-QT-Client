package mining;

import data.Data;
import data.Tuple;
import java.io.*;
import java.util.Iterator;

public class QTMiner {
    ClusterSet C;
    double radius;

    public QTMiner(double radius) {
        this.radius = radius;
        C = new ClusterSet();
    }

    public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        ClusterSet temp = (ClusterSet) in.readObject();
        in.close();
        C = new ClusterSet();
        for (Cluster<Integer> c : temp) {
            C.add(c);
        }
    }

    public void salva(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(C);
        out.close();
    }

    public ClusterSet getC() {
        return C;
    }

    public int compute(Data data) throws ClusteringRadiusException, EmptyDatasetException {
        if (data.getNumberOfExamples() == 0) {
            throw new EmptyDatasetException("Dataset is empty!");
        }
        int numclusters = 0;
        boolean[] isClustered = new boolean[data.getNumberOfExamples()];
        for (int i = 0; i < isClustered.length; i++)
            isClustered[i] = false; /* crea un array di booleani grande quanto il numero di righe esempi
                                    per tenere traccia delle righe già clusterizzate */
        int countClustered = 0;
        while (countClustered != data.getNumberOfExamples()) {
            Cluster<Integer> c = buildCandidateCluster(data, isClustered);
            C.add(c); // cluster finali
            numclusters++;

            for (Integer id : c) {
                isClustered[id] = true;
            }
            countClustered += c.getSize();
        }

        if (numclusters == 1) {
            throw new ClusteringRadiusException(data.getNumberOfExamples() + " tuples in one cluster!");
        }

        return numclusters;
    }

    private Cluster<Integer> buildCandidateCluster(Data data, boolean[] isClustered) {
        Cluster<Integer> bestCluster = null;
        int maxSize = -1;

        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            if (!isClustered[i]) {
                Tuple centroid = data.getItemSet(i); // considera ogni tupla come centroide
                Cluster<Integer> candidateCluster = new Cluster<>(centroid);

                for (int j = 0; j < data.getNumberOfExamples(); j++) {
                    if (!isClustered[j]) {
                        Tuple current = data.getItemSet(j);
                        if (centroid.getDistance(current) <= radius) {
                            candidateCluster.addData(j); // se ogni tupla entra nel radius del
                        }                                // centroide viene agguinta nel cluster
                    }
                }
                if (candidateCluster.getSize() > maxSize) { // il cluster appena fatto sara il piu grande
                    bestCluster = candidateCluster;         // quindi itera tutte le tuple per cercare
                    maxSize = candidateCluster.getSize();   // il cluster piu grande in assoluto
                }
            }
        }
        return bestCluster;
    }
}
