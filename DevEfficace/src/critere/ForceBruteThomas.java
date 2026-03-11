package critere;

import java.util.*;

/**
 * Algorithme de Force Brute (Version Récursive).
 * Teste toutes les combinaisons et garde la meilleure.
 * 
 * @author Thomas Grillot
 */
public class ForceBruteThomas {

    private static List<Groupe> meilleureRepartion = null;
    private static double meilleurScore = -1.0;

    public static List<Groupe> lancerRecherche(List<Etudiant> etudiants, int nbGroupes, int tailleMax) {
        List<Groupe> groupesVides = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupesVides.add(new Groupe(i + 1, tailleMax));
        }

        meilleureRepartion = null;
        meilleurScore = -1.0;
        testerToutesLesSolutions(etudiants, 0, groupesVides);

        return meilleureRepartion;
    }

    private static void testerToutesLesSolutions(List<Etudiant> etudiants, int index, List<Groupe> groupes) {
        if (index == etudiants.size()) {
            double scoreActuel = getScoreGlobal(groupes);

            if (scoreActuel > meilleurScore) {
                meilleurScore = scoreActuel;
                meilleureRepartion = copierListe(groupes);
            }
            return;
        }

        Etudiant e = etudiants.get(index);

        for (Groupe g : groupes) {
            if (!g.estPlein()) {
                g.ajouter(e);
                testerToutesLesSolutions(etudiants, index + 1, groupes);
                g.getEtudiants().remove(g.getEtudiants().size() - 1);
            }
        }
    }

    private static double getScoreGlobal(List<Groupe> groupes) {
        double total = 0;
        for (Groupe g : groupes) {
            total += g.calculerQualite();
        }
        return total;
    }

    private static List<Groupe> copierListe(List<Groupe> source) {
        List<Groupe> copie = new ArrayList<>();
        for (Groupe g : source) {
            Groupe nouveau = new Groupe(g.getId(), g.getCapaciteMax());
            for (Etudiant e : g.getEtudiants()) {
                nouveau.ajouter(e);
            }
            copie.add(nouveau);
        }
        return copie;
    }

    
}