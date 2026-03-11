package critere;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour les opérations sur les clusters.
 * Factorise la méthode creerClusters() qui était dupliquée.
 * 
 * @author Soan Gameiro
 */
public class ClusterUtils {

    /**
     * Regroupe les étudiants qui font du covoiturage ensemble.
     * Les étudiants avec le même idCovoiturage sont regroupés dans le même cluster.
     * 
     * @param promo Liste des étudiants
     * @return Liste des clusters
     */
    public static List<Cluster> creerClusters(List<Etudiant> promo) {
        List<Cluster> clusters = new ArrayList<>();

        for (int i = 0; i < promo.size(); i++) {
            Etudiant e = promo.get(i);
            boolean ajoute = false;

            // Si l'étudiant fait du covoiturage, chercher son cluster
            if (e.getIdCovoiturage() != -1) {
                for (int j = 0; j < clusters.size(); j++) {
                    Cluster c = clusters.get(j);
                    if (c.size() > 0 && c.getMembres().get(0).getIdCovoiturage() == e.getIdCovoiturage()) {
                        c.add(e);
                        ajoute = true;
                        break;
                    }
                }
            }

            // Si pas trouvé ou pas de covoiturage, créer un nouveau cluster
            if (!ajoute) {
                Cluster newC = new Cluster();
                newC.add(e);
                clusters.add(newC);
            }
        }

        return clusters;
    }
}
