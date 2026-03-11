package critere;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * Algorithme Glouton basé sur le score (Best Fit).
 * À chaque étape, on place le cluster dans le groupe qui maximise le gain de
 * score.
 * 
 * @author Soan Gameiro
 */
public class GloutonScoreSoan {

    public List<Groupe> resoudre(List<Etudiant> promo, int nbGroupes) {
        List<Cluster> clusters = ClusterUtils.creerClusters(promo);

        // Tri : plus gros clusters d'abord
        clusters.sort((c1, c2) -> c2.size() - c1.size());

        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe(i));
        }

        for (int i = 0; i < clusters.size(); i++) {
            Cluster c = clusters.get(i);
            Groupe meilleurGroupe = null;
            int meilleurGain = -999999;

            for (int j = 0; j < groupes.size(); j++) {
                Groupe g = groupes.get(j);
                if (g.getPlaceRestante() >= c.size()) {
                    int scoreAvant = scoreTemporaire(g);
                    for (Etudiant e : c.getMembres())
                        g.ajouterEtudiant(e);
                    int scoreApres = scoreTemporaire(g);
                    for (Etudiant e : c.getMembres())
                        g.retirerEtudiant(e);

                    int gain = scoreApres - scoreAvant;
                    if (gain > meilleurGain) {
                        meilleurGain = gain;
                        meilleurGroupe = g;
                    }
                }
            }

            if (meilleurGroupe != null) {
                for (Etudiant e : c.getMembres()) {
                    meilleurGroupe.ajouterEtudiant(e);
                }
            }
        }

        return groupes;
    }

    /**
     * Score simplifié pour la construction (ignore la pénalité de taille minimale).
     */
    private int scoreTemporaire(Groupe g) {
        List<Etudiant> etudiants = g.getEtudiants();
        if (etudiants.isEmpty())
            return 0;

        int score = 0;

        // Parcours
        int maxM = 0;
        for (int i = 0; i < etudiants.size(); i++) {
            String p = etudiants.get(i).getParcours();
            int count = 0;
            for (int j = 0; j < etudiants.size(); j++) {
                if (etudiants.get(j).getParcours().equals(p))
                    count++;
            }
            if (count > maxM)
                maxM = count;
        }
        score += maxM;

        // Filles
        int f = g.getNbFilles();
        if (f >= 4)
            score += 10;
        else if (f > 0)
            score -= 50;

        // Covoiturage
        for (Etudiant e : etudiants) {
            if (e.getIdCovoiturage() != -1)
                score += 1;
        }

        return score;
    }

   
}