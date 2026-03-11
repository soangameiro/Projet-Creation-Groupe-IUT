package critere;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * Algorithme de Force Brute avec backtracking.
 * Teste toutes les combinaisons possibles et garde la meilleure.
 * 
 * @author Soan Gameiro
 */
public class ForceBruteSoan {

    private int maxScoreGlobal = -999999;
    private List<Groupe> meilleurResultat = new ArrayList<>();

    public List<Groupe> resoudre(List<Etudiant> promo, int nbGroupes) {
        List<Cluster> clusters = ClusterUtils.creerClusters(promo);

        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe(i));
        }

        maxScoreGlobal = -999999;
        backtracking(clusters, 0, groupes);

        return meilleurResultat;
    }

    private void backtracking(List<Cluster> clusters, int index, List<Groupe> groupes) {
        if (index == clusters.size()) {
            int scoreTotal = 0;
            for (Groupe g : groupes) {
                scoreTotal += g.calculerScore();
            }

            if (scoreTotal > maxScoreGlobal) {
                maxScoreGlobal = scoreTotal;
                meilleurResultat = copierGroupes(groupes);
            }
            return;
        }

        Cluster c = clusters.get(index);
        for (Groupe g : groupes) {
            if (g.getPlaceRestante() >= c.size()) {
                for (Etudiant e : c.getMembres()) {
                    g.ajouterEtudiant(e);
                }

                backtracking(clusters, index + 1, groupes);

                for (Etudiant e : c.getMembres()) {
                    g.retirerEtudiant(e);
                }
            }
        }
    }

    private List<Groupe> copierGroupes(List<Groupe> source) {
        List<Groupe> copie = new ArrayList<>();
        for (Groupe g : source) {
            Groupe ng = new Groupe(g.getId());
            for (Etudiant e : g.getEtudiants()) {
                ng.ajouterEtudiant(e);
            }
            copie.add(ng);
        }
        return copie;
    }

    
}