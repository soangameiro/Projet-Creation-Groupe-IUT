package critere;

import java.util.List;

/**
 * Fichier de test des algorithmes de Soan.
 * Teste : GloutonScoreSoan, GloutonTriSoan, ForceBruteSoan
 * 
 * @author Soan Gameiro
 */
public class TestSoan {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       TESTS ALGORITHMES - SOAN GAMEIRO       ");
        System.out.println("==============================================");
        System.out.println("Mode : Parcours + Covoiturage + Mixité");
        System.out.println();

        List<Etudiant> promo = Generateur.genererPromoSoan(80);
        int nbGroupes = 4;

        System.out.println("Étudiants générés : " + promo.size());
        System.out.println("Nombre de groupes : " + nbGroupes);
        System.out.println();

        // Test GloutonScoreSoan
        System.out.println("----------------------------------------------");
        System.out.println("1. GLOUTON SCORE (Best Fit)");
        System.out.println("----------------------------------------------");
        GloutonScoreSoan algoScore = new GloutonScoreSoan();
        long d1 = System.nanoTime();
        List<Groupe> res1 = algoScore.resoudre(promo, nbGroupes);
        long f1 = System.nanoTime();

        int total1 = 0;
        for (Groupe g : res1) {
            int s = g.calculerScore();
            total1 += s;
            System.out.println("  " + g + " | Score: " + s);
        }
        System.out.println("  TOTAL: " + total1 + " | Temps: " + (f1 - d1) / 1000 + " µs");
        System.out.println();

        // Test GloutonTriSoan
        System.out.println("----------------------------------------------");
        System.out.println("2. GLOUTON TRI (Stratégique)");
        System.out.println("----------------------------------------------");
        GloutonTriSoan algoTri = new GloutonTriSoan();
        long d2 = System.nanoTime();
        List<Groupe> res2 = algoTri.resoudre(promo, nbGroupes);
        long f2 = System.nanoTime();

        int total2 = 0;
        for (Groupe g : res2) {
            int s = g.calculerScore();
            total2 += s;
            System.out.println("  " + g + " | Score: " + s);
        }
        System.out.println("  TOTAL: " + total2 + " | Temps: " + (f2 - d2) / 1000 + " µs");
        System.out.println();

        // Test ForceBruteSoan (petit échantillon)
        System.out.println("----------------------------------------------");
        System.out.println("3. FORCE BRUTE (Backtracking)");
        System.out.println("----------------------------------------------");
        List<Etudiant> petitePromo = Generateur.genererPromoSoan(15);
        ForceBruteSoan algoFB = new ForceBruteSoan();
        long d3 = System.currentTimeMillis();
        List<Groupe> res3 = algoFB.resoudre(petitePromo, 1);
        long f3 = System.currentTimeMillis();

        int total3 = 0;
        for (Groupe g : res3) {
            int s = g.calculerScore();
            total3 += s;
            System.out.println("  " + g + " | Score: " + s);
        }
        System.out.println("  TOTAL: " + total3 + " | Temps: " + (f3 - d3) + " ms");
        System.out.println();

        // Résumé
        System.out.println("==============================================");
        System.out.println("                   RÉSUMÉ                     ");
        System.out.println("==============================================");
        System.out.println("| Algorithme      | Score Total |");
        System.out.println("|-----------------|-------------|");
        System.out.println("| Glouton Score   | " + String.format("%11d", total1) + " |");
        System.out.println("| Glouton Tri     | " + String.format("%11d", total2) + " |");
        System.out.println("| Force Brute     | " + String.format("%11d", total3) + " |");
        System.out.println("==============================================");
    }
}
