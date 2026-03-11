package critere;

import java.util.*;

/**
 * Algorithme Glouton 1 : Score d'utilité (Centré Étudiant).
 * Chaque étudiant est placé dans le groupe qui maximise son utilité.
 * 
 * @author Thomas Grillot
 */
public class Glouton1Thomas {

    public static List<Groupe> repartir(List<Etudiant> etudiants, int nbGroupes, int tailleMax) {
        // Init
        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe(i + 1, tailleMax));
        }

        // TRI STRATEGIQUE : Les plus "Difficiles à placer" en premier
        List<Etudiant> ordre = new ArrayList<>(etudiants);
        for (int i = 0; i < ordre.size() - 1; i++) {
            for (int j = 0; j < ordre.size() - 1 - i; j++) {
                Etudiant e1 = ordre.get(j);
                Etudiant e2 = ordre.get(j + 1);

                // On compare les scores de rareté.
                // Si l'étudiant de droite (e2) est PLUS RARE que celui de gauche (e1),
                // alors ils sont dans le mauvais ordre (on veut décroissant). On échange.
                if (calculerRarete(e2) > calculerRarete(e1)) {
                    // Échange (Swap)
                    ordre.set(j, e2);
                    ordre.set(j + 1, e1);
                }
            }
        }

        // BOUCLE GLOUTONNE
        for (Etudiant e : ordre) {
            Groupe meilleur = null;
            double meilleurScore = Double.MAX_VALUE;

            for (Groupe g : groupes) {
                if (g.estPlein())
                    continue;

                double score = 0;

                // A. Compétences Critiques
                if (e.estLeaderJava()) {
                    if (!g.aUnLeaderJava())
                        score -= 10000;
                    else
                        score += 100;
                }
                if (e.estLeaderBD()) {
                    if (!g.aUnLeaderBD())
                        score -= 10000;
                    else
                        score += 100;
                }

                // B. Mixité Bac
                if (g.getTaille() > 2) {
                    boolean tropDeG = g.nbGeneral() > g.nbTechno() + 1;
                    boolean tropDeT = g.nbTechno() > g.nbGeneral() + 1;

                    if (e.getTypeBac().equals("G") && tropDeG)
                        score += 500;
                    if (e.getTypeBac().equals("T") && tropDeT)
                        score += 500;
                }

                // C. Lissage taille
                score += g.getTaille();

                if (score < meilleurScore) {
                    meilleurScore = score;
                    meilleur = g;
                }
            }

            if (meilleur != null)
                meilleur.ajouter(e);
            else
                System.err.println("Glouton1: Pas de place pour " + e.getNom());
        }

        return groupes;
    }

    private static int calculerRarete(Etudiant e) {
        int r = 0;
        if (e.estLeaderJava())
            r += 10;
        if (e.estLeaderBD())
            r += 10;
        return r;
    }

    
}
