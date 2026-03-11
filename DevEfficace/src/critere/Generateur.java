package critere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Classe unifiée pour la génération d'étudiants aléatoires.
 * Supporte les différents modes de création de groupes.
 * 
 * @author Maelan Jahier, Thomas Grillot, Soan Gameiro
 */
public class Generateur {

    private static final String[] PRENOMS_MASCULINS = {
            "Thomas", "Lucas", "Hugo", "Jules", "Arthur", "Louis",
            "Nathan", "Léo", "Gabriel", "Raphaël", "Paul", "Adam"
    };

    private static final String[] PRENOMS_FEMININS = {
            "Emma", "Léa", "Chloé", "Manon", "Jade", "Louise",
            "Alice", "Lina", "Sarah", "Camille", "Anna", "Marie"
    };

    private static final String[] NOMS = {
            "Martin", "Bernard", "Dubois", "Thomas", "Robert",
            "Richard", "Petit", "Durand", "Leroy", "Moreau"
    };

    private static final String[] PARCOURS = { "Dev", "Reseau", "Cyber" };

    // ========== MODE SOAN (Parcours/Covoiturage) ==========

    /**
     * Génère une promo aléatoire compatible avec le mode Soan.
     * Inclut : nom, moyenne, typeBac, sexe, parcours, idCovoiturage.
     * 
     * @param n Nombre d'étudiants à générer
     * @return Une liste d'étudiants
     */
    public static List<Etudiant> genererPromoSoan(int n) {
        List<Etudiant> promo = new ArrayList<>();
        Random rand = new Random();
        int currentCovoitId = 1;

        for (int i = 0; i < n; i++) {
            String nom = "Etu" + i;
            String bac = rand.nextBoolean() ? "G" : "T";
            double moyenne = 10 + rand.nextDouble() * 10;

            // Sexe : ~40% de filles pour garantir la contrainte (min 4/groupe)
            char sexe;
            if (i < (n * 2) / 5) {
                sexe = 'F';
            } else {
                sexe = (rand.nextDouble() < 0.25) ? 'F' : 'M';
            }

            // Parcours aléatoire
            String parcours = PARCOURS[rand.nextInt(PARCOURS.length)];

            // Covoiturage
            int idCovoit = -1;
            if (i > 0 && rand.nextDouble() < 0.3) {
                if (rand.nextBoolean()) {
                    idCovoit = currentCovoitId;
                    if (!promo.isEmpty()) {
                        parcours = promo.get(promo.size() - 1).getParcours();
                    }
                } else {
                    currentCovoitId++;
                    idCovoit = currentCovoitId;
                }
            }

            promo.add(new Etudiant(nom, moyenne, bac, sexe, parcours, idCovoit));
        }

        return promo;
    }

    // ========== MODE THOMAS (Notes/Compétences) ==========

    /**
     * Génère une promo aléatoire compatible avec le mode Thomas.
     * Inclut : nom, typeBac, redoublant, apprentissage, noteJava, noteBD.
     * 
     * @param n Nombre d'étudiants à générer
     * @return Une liste d'étudiants
     */
    public static List<Etudiant> genererPromoThomas(int n) {
        List<Etudiant> promo = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            String bac = rand.nextBoolean() ? "G" : "T";
            double noteJava = rand.nextInt(20);
            double noteBD = rand.nextInt(20);

            // On force quelques experts pour le test
            if (i < n / 5) {
                noteJava = 15 + rand.nextInt(5);
                noteBD = 15 + rand.nextInt(5);
            }

            promo.add(new Etudiant(
                    "Etu" + i,
                    bac,
                    false,
                    Etudiant.StatutApprentissage.AUCUN,
                    noteJava,
                    noteBD));
        }

        return promo;
    }

    // ========== MODE MAELAN (Bac/Moyenne) ==========

    /**
     * Génère une promo aléatoire compatible avec le mode Maelan.
     * Respect des contraintes : 10-20% de filles.
     * 
     * @param nombre Nombre d'étudiants à générer
     * @return Liste d'étudiants générés
     */
    public static List<Etudiant> genererPromoMaelan(int nombre) {
        if (nombre <= 0) {
            throw new IllegalArgumentException("Le nombre d'étudiants doit être positif");
        }

        List<Etudiant> etudiants = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= nombre; i++) {
            boolean estFille = random.nextBoolean();
            String genre = estFille ? "F" : "H";

            String prenom = estFille
                    ? PRENOMS_FEMININS[random.nextInt(PRENOMS_FEMININS.length)]
                    : PRENOMS_MASCULINS[random.nextInt(PRENOMS_MASCULINS.length)];

            String nom = NOMS[random.nextInt(NOMS.length)];
            double moyenne = 5 + random.nextDouble() * 15;
            moyenne = Math.round(moyenne * 100) / 100.0;
            int typeBac = random.nextDouble() < 0.6 ? 1 : 2; // 1 = Général, 2 = Techno

            etudiants.add(new Etudiant(i, nom, prenom, moyenne, typeBac, genre));
        }

        return etudiants;
    }

    /**
     * Génère une promo simplifiée pour les tests rapides.
     * 
     * @param nb Nombre d'étudiants
     * @return Liste d'étudiants
     */
    public static List<Etudiant> genererEtudiants(int nb) {
        Random rand = new Random();
        List<Etudiant> liste = new ArrayList<>();

        double tauxFilles = 0.10 + rand.nextDouble() * 0.10;
        int nbFilles = (int) (nb * tauxFilles);

        for (int i = 0; i < nb; i++) {
            String nom = "Etu" + (i + 1);
            double moy = 8 + rand.nextDouble() * 10;
            String bac = rand.nextBoolean() ? "General" : "Techno";
            String sexe = (i < nbFilles) ? "F" : "M";
            liste.add(new Etudiant(nom, moy, bac, sexe));
        }

        Collections.shuffle(liste);
        return liste;
    }
}
