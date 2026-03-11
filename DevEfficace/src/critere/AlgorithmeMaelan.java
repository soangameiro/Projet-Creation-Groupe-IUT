package critere;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Algorithmes de création de groupes - Mode Maelan Jahier.
 * Mode : Équilibre des bacs et des moyennes.
 * 
 * CONTRAINTES :
 * - Chaque groupe doit contenir entre 15 et 18 étudiants
 * - Entre 10% et 20% de filles par groupe
 * 
 * CRITÈRES À OPTIMISER :
 * - Minimiser l'écart maximal de moyenne entre les groupes
 * - Minimiser l'écart du nombre d'étudiants Général/Techno entre les groupes
 * 
 * SCORE : EcartMoyenneMax + EcartBacMax (à minimiser)
 * 
 * @author Maelan Jahier
 */
public class AlgorithmeMaelan {

    // =========================================================================
    // CONSTANTES
    // =========================================================================

    public static final int TAILLE_MIN_GROUPE = 15;
    public static final int TAILLE_MAX_GROUPE = 18;
    public static final double POURCENTAGE_FILLES_MIN = 0.10;
    public static final double POURCENTAGE_FILLES_MAX = 0.20;

    // =========================================================================
    // CALCUL DU SCORE
    // =========================================================================

    /**
     * Calcule le score d'une répartition de groupes.
     * Score = EcartMoyenneMax + EcartBacMax (à minimiser)
     */
    public static double calculerScore(List<Groupe> groupes) {
        if (groupes == null || groupes.size() < 2) {
            return Double.MAX_VALUE;
        }

        double moyenneMin = Double.MAX_VALUE;
        double moyenneMax = Double.MIN_VALUE;
        int bacGeneralMin = Integer.MAX_VALUE;
        int bacGeneralMax = Integer.MIN_VALUE;

        for (Groupe g : groupes) {
            double moy = g.getMoyenne();
            int nbGeneral = g.getNbBacGeneral();

            if (moy < moyenneMin)
                moyenneMin = moy;
            if (moy > moyenneMax)
                moyenneMax = moy;
            if (nbGeneral < bacGeneralMin)
                bacGeneralMin = nbGeneral;
            if (nbGeneral > bacGeneralMax)
                bacGeneralMax = nbGeneral;
        }

        double ecartMoyenne = moyenneMax - moyenneMin;
        int ecartBac = bacGeneralMax - bacGeneralMin;

        return ecartMoyenne + ecartBac;
    }

    /**
     * Vérifie si tous les groupes respectent les contraintes.
     */
    public static boolean tousGroupesValides(List<Groupe> groupes) {
        for (Groupe g : groupes) {
            if (!g.estValide()) {
                return false;
            }
        }
        return true;
    }

    // =========================================================================
    // ALGORITHME GLOUTON 1 : TRI PAR MOYENNE
    // =========================================================================

    /**
     * Algorithme glouton qui trie les étudiants par moyenne décroissante
     * et les répartit en round-robin dans les groupes.
     */
    public static List<Groupe> gloutonParMoyenne(List<Etudiant> etudiants, int nbGroupes) {
        if (etudiants == null || etudiants.isEmpty()) {
            throw new IllegalArgumentException("La liste d'étudiants ne peut pas être vide");
        }
        if (nbGroupes <= 0) {
            throw new IllegalArgumentException("Le nombre de groupes doit être positif");
        }

        List<Etudiant> listeTriee = new ArrayList<>(etudiants);
        Collections.sort(listeTriee, (a, b) -> Double.compare(b.getMoyenne(), a.getMoyenne()));

        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe("Groupe " + (i + 1)));
        }

        int indexGroupe = 0;
        for (Etudiant e : listeTriee) {
            groupes.get(indexGroupe).ajouterEtudiant(e);
            indexGroupe = (indexGroupe + 1) % nbGroupes;
        }

        return groupes;
    }

    // =========================================================================
    // ALGORITHME GLOUTON 2 : TRI PAR TYPE DE BAC
    // =========================================================================

    /**
     * Algorithme glouton qui équilibre d'abord les types de bac,
     * puis distribue par moyenne au sein de chaque catégorie.
     */
    public static List<Groupe> gloutonParBac(List<Etudiant> etudiants, int nbGroupes) {
        if (etudiants == null || etudiants.isEmpty()) {
            throw new IllegalArgumentException("La liste d'étudiants ne peut pas être vide");
        }
        if (nbGroupes <= 0) {
            throw new IllegalArgumentException("Le nombre de groupes doit être positif");
        }

        List<Etudiant> bacGeneral = new ArrayList<>();
        List<Etudiant> bacTechno = new ArrayList<>();

        for (Etudiant e : etudiants) {
            if (e.estBacGeneral()) {
                bacGeneral.add(e);
            } else {
                bacTechno.add(e);
            }
        }

        Comparator<Etudiant> parMoyenne = (a, b) -> Double.compare(b.getMoyenne(), a.getMoyenne());
        Collections.sort(bacGeneral, parMoyenne);
        Collections.sort(bacTechno, parMoyenne);

        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe("Groupe " + (i + 1)));
        }

        int indexGroupe = 0;
        for (Etudiant e : bacGeneral) {
            groupes.get(indexGroupe).ajouterEtudiant(e);
            indexGroupe = (indexGroupe + 1) % nbGroupes;
        }

        indexGroupe = 0;
        for (Etudiant e : bacTechno) {
            groupes.get(indexGroupe).ajouterEtudiant(e);
            indexGroupe = (indexGroupe + 1) % nbGroupes;
        }

        return groupes;
    }

    // =========================================================================
    // ALGORITHME FORCE BRUTE 
    // =========================================================================

    /**
     * Algorithme de force brute qui teste plusieurs permutations aléatoires
     * et garde la meilleure solution trouvée.
     */
    public static List<Groupe> forceBrute(List<Etudiant> etudiants, int nbGroupes, int nbEssais) {
        if (etudiants == null || etudiants.isEmpty()) {
            throw new IllegalArgumentException("La liste d'étudiants ne peut pas être vide");
        }
        if (nbGroupes <= 0) {
            throw new IllegalArgumentException("Le nombre de groupes doit être positif");
        }
        if (nbEssais <= 0) {
            nbEssais = 1000;
        }

        List<Groupe> meilleureSolution = null;
        double meilleurScore = Double.MAX_VALUE;

        Random random = new Random();

        for (int essai = 0; essai < nbEssais; essai++) {
            List<Etudiant> listeAleatoire = new ArrayList<>(etudiants);
            Collections.shuffle(listeAleatoire, random);

            List<Groupe> groupes = new ArrayList<>();
            for (int i = 0; i < nbGroupes; i++) {
                groupes.add(new Groupe("Groupe " + (i + 1)));
            }

            int indexGroupe = 0;
            for (Etudiant e : listeAleatoire) {
                groupes.get(indexGroupe).ajouterEtudiant(e);
                indexGroupe = (indexGroupe + 1) % nbGroupes;
            }

            double score = calculerScore(groupes);

            if (score < meilleurScore) {
                meilleurScore = score;
                meilleureSolution = groupes;
            }
        }

        return meilleureSolution;
    }

    // =========================================================================
    // MÉTHODES UTILITAIRES
    // =========================================================================

    /**
     * Affiche les statistiques d'une liste de groupes.
     */
    public static void afficherStatistiques(List<Groupe> groupes) {
        System.out.println("\n========== STATISTIQUES DES GROUPES ==========");
        System.out.println("Score total : " + String.format("%.2f", calculerScore(groupes)));
        System.out.println();

        for (Groupe g : groupes) {
            System.out.println("--- " + g.getNom() + " ---");
            System.out.println("  Taille : " + g.getTaille() + " étudiants");
            System.out.println("  Moyenne : " + String.format("%.2f", g.getMoyenne()));
            System.out.println("  Bac Général : " + g.getNbBacGeneral());
            System.out.println("  Bac Techno : " + g.getNbBacTechno());
            System.out.println("  Filles : " + g.getNbFilles() +
                    " (" + String.format("%.0f", g.getPourcentageFilles() * 100) + "%)");
            System.out.println("  Valide : " + (g.estValide() ? "OUI" : "NON"));
            System.out.println();
        }
    }



    
}
