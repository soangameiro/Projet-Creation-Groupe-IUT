package critere;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * Algorithme Glouton basé sur le tri stratégique.
 * Trie les clusters par nombre de filles puis par parcours.
 * 
 * @author Soan Gameiro
 */
public class GloutonTriSoan {

    public List<Groupe> resoudre(List<Etudiant> promo, int nbGroupes) {
        List<Cluster> clusters = ClusterUtils.creerClusters(promo);

        // TRI STRATEGIQUE : Filles d'abord, puis Parcours
        clusters.sort((c1, c2) -> {
            int f = c2.getNbFilles() - c1.getNbFilles();
            if (f != 0)
                return f;
            return c1.getParcours().compareTo(c2.getParcours());
        });

        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe(i));
        }

        int indexGroupe = 0;
        for (int i = 0; i < clusters.size(); i++) {
            Cluster c = clusters.get(i);
            Groupe gCourant = groupes.get(indexGroupe);

            if (gCourant.getPlaceRestante() >= c.size()) {
                for (Etudiant e : c.getMembres()) {
                    gCourant.ajouterEtudiant(e);
                }

                // Rotation si le groupe est bien rempli
                if (gCourant.getTaille() >= 18) {
                    indexGroupe = (indexGroupe + 1) % nbGroupes;
                }
            } else {
                // Recherche d'une place ailleurs
                for (int j = 0; j < nbGroupes; j++) {
                    Groupe autre = groupes.get(j);
                    if (autre.getPlaceRestante() >= c.size()) {
                        for (Etudiant e : c.getMembres()) {
                            autre.ajouterEtudiant(e);
                        }
                        break;
                    }
                }
            }
        }

        return groupes;
    }

}