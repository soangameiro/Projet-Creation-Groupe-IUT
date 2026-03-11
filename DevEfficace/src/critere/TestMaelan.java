package critere;

import java.util.List;

/**
 * Fichier de test des algorithmes de Maelan.
 * Teste : AlgorithmeMaelan (gloutonParMoyenne, gloutonParBac, forceBrute)
 * 
 * @author Maelan Jahier
 */
public class TestMaelan {

    private static final int NB_TESTS = 20;
    private static final int NB_ETUDIANTS =110;
    private static final int NB_GROUPES = 6;
    private static final int NB_ESSAIS_FORCE_BRUTE = 1000;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       TESTS ALGORITHMES - MAELAN JAHIER      ");
        System.out.println("==============================================");
        System.out.println("Mode : Équilibre Moyenne + Bac");
        System.out.println();

        System.out.println("Paramètres :");
        System.out.println("  - Nombre de tests : " + NB_TESTS);
        System.out.println("  - Étudiants/test : " + NB_ETUDIANTS);
        System.out.println("  - Groupes/test : " + NB_GROUPES);
        System.out.println();

        double totalScoreGlouton1 = 0, totalScoreGlouton2 = 0, totalScoreForceBrute = 0;
        int victoiresG1 = 0, victoiresG2 = 0, victoiresFB = 0;

        System.out.println("Test en cours...");

        for (int i = 0; i < NB_TESTS; i++) {
            List<Etudiant> etudiants = Generateur.genererPromoMaelan(NB_ETUDIANTS);

            List<Groupe> g1 = AlgorithmeMaelan.gloutonParMoyenne(etudiants, NB_GROUPES);
            List<Groupe> g2 = AlgorithmeMaelan.gloutonParBac(etudiants, NB_GROUPES);
            List<Groupe> g3 = AlgorithmeMaelan.forceBrute(etudiants, NB_GROUPES, NB_ESSAIS_FORCE_BRUTE);

            double s1 = AlgorithmeMaelan.calculerScore(g1);
            double s2 = AlgorithmeMaelan.calculerScore(g2);
            double s3 = AlgorithmeMaelan.calculerScore(g3);

            totalScoreGlouton1 += s1;
            totalScoreGlouton2 += s2;
            totalScoreForceBrute += s3;

            double minScore = Math.min(s1, Math.min(s2, s3));
            if (s1 == minScore)
                victoiresG1++;
            if (s2 == minScore)
                victoiresG2++;
            if (s3 == minScore)
                victoiresFB++;

            if ((i + 1) % 5 == 0) {
                System.out.println("  " + (i + 1) + "/" + NB_TESTS + " tests effectués");
            }
        }

        double moyG1 = totalScoreGlouton1 / NB_TESTS;
        double moyG2 = totalScoreGlouton2 / NB_TESTS;
        double moyFB = totalScoreForceBrute / NB_TESTS;

        System.out.println();
        System.out.println("==============================================");
        System.out.println("                   RÉSULTATS                  ");
        System.out.println("==============================================");
        System.out.println();

        System.out.println("SCORES MOYENS (plus petit = meilleur)");
        System.out.println("----------------------------------------------");
        System.out.printf("| %-20s | %10s |%n", "Algorithme", "Score Moy.");
        System.out.println("----------------------------------------------");
        System.out.printf("| %-20s | %10.2f |%n", "Glouton Moyenne", moyG1);
        System.out.printf("| %-20s | %10.2f |%n", "Glouton Bac", moyG2);
        System.out.printf("| %-20s | %10.2f |%n", "Force Brute", moyFB);
        System.out.println("----------------------------------------------");

        System.out.println();
        System.out.println("NOMBRE DE VICTOIRES");
        System.out.println("----------------------------------------------");
        System.out.printf("| %-20s | %5d fois |%n", "Glouton Moyenne", victoiresG1);
        System.out.printf("| %-20s | %5d fois |%n", "Glouton Bac", victoiresG2);
        System.out.printf("| %-20s | %5d fois |%n", "Force Brute", victoiresFB);
        System.out.println("----------------------------------------------");

        System.out.println();
        System.out.println("CONCLUSION");
        System.out.println("----------------------------------------------");

        String meilleur;
        if (moyG1 <= moyG2 && moyG1 <= moyFB)
            meilleur = "Glouton Moyenne";
        else if (moyG2 <= moyFB)
            meilleur = "Glouton Bac";
        else
            meilleur = "Force Brute";

        System.out.println("Meilleur algorithme en moyenne : " + meilleur);
        System.out.println("==============================================");
    }
}
