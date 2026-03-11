package critere;

import java.util.List;

/**
 * Fichier de test des algorithmes de Thomas.
 * Teste : Glouton1Thomas, Glouton2Thomas, ForceBruteThomas
 * 
 * @author Thomas Grillot
 */
public class TestThomas {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("      TESTS ALGORITHMES - THOMAS GRILLOT      ");
        System.out.println("==============================================");
        System.out.println("Mode : Leaders Java/BD + Équilibre Bac");
        System.out.println();

        List<Etudiant> promo = Generateur.genererPromoThomas(100);
        int nbGroupes = 5;
        int tailleMax = 20;

        System.out.println("Étudiants générés : " + promo.size());
        System.out.println("Nombre de groupes : " + nbGroupes);
        System.out.println("Taille max/groupe : " + tailleMax);
        System.out.println();

        // Test Glouton1Thomas
        System.out.println("----------------------------------------------");
        System.out.println("1. GLOUTON 1 (Approche Utilité)");
        System.out.println("----------------------------------------------");
        long d1 = System.nanoTime();
        List<Groupe> res1 = Glouton1Thomas.repartir(promo, nbGroupes, tailleMax);
        long f1 = System.nanoTime();

        double total1 = 0;
        for (Groupe g : res1) {
            double q = g.calculerQualite();
            total1 += q;
            System.out.println("  Grp " + g.getId() + " | Java:" + g.aUnLeaderJava() +
                    " | BD:" + g.aUnLeaderBD() + " | Qualité:" + q);
        }
        System.out.println("  Qualité totale: " + total1 + " | Temps: " + (f1 - d1) / 1000 + " µs");
        System.out.println();

        // Test Glouton2Thomas
        System.out.println("----------------------------------------------");
        System.out.println("2. GLOUTON 2 (Approche Par Vagues)");
        System.out.println("----------------------------------------------");
        long d2 = System.nanoTime();
        List<Groupe> res2 = Glouton2Thomas.repartir(promo, nbGroupes, tailleMax);
        long f2 = System.nanoTime();

        double total2 = 0;
        for (Groupe g : res2) {
            double q = g.calculerQualite();
            total2 += q;
            System.out.println("  Grp " + g.getId() + " | Java:" + g.aUnLeaderJava() +
                    " | BD:" + g.aUnLeaderBD() + " | Qualité:" + q);
        }
        System.out.println("  Qualité totale: " + total2 + " | Temps: " + (f2 - d2) / 1000 + " µs");
        System.out.println();


        // Résumé
        System.out.println("==============================================");
        System.out.println("                   RÉSUMÉ                     ");
        System.out.println("==============================================");
        System.out.println("| Algorithme      | Qualité Totale |");
        System.out.println("|-----------------|----------------|");
        System.out.println("| Glouton 1       | " + String.format("%14.0f", total1) + " |");
        System.out.println("| Glouton 2       | " + String.format("%14.0f", total2) + " |");
        System.out.println("==============================================");
    }
}
