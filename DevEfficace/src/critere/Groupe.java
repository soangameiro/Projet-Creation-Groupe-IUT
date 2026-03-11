package critere;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe unifiée représentant un groupe d'étudiants avec toutes les méthodes
 * nécessaires pour les différents modes de création de groupes.
 * 
 * @author Maelan Jahier, Thomas Grillot, Soan Gameiro
 */
public class Groupe {

    // ========== CONSTANTES ==========

    public static final int CAPACITE_MAX_DEFAUT = 20;
    public static final int CAPACITE_MIN_DEFAUT = 15;

    // ========== ATTRIBUTS ==========

    private int id;
    private String nom;
    private int capaciteMax;
    private int capaciteMin;
    private List<Etudiant> etudiants;

    // ========== CONSTRUCTEURS ==========

    /**
     * Constructeur avec id uniquement (style Soan).
     */
    public Groupe(int id) {
        this.id = id;
        this.nom = "Groupe " + id;
        this.capaciteMax = CAPACITE_MAX_DEFAUT;
        this.capaciteMin = CAPACITE_MIN_DEFAUT;
        this.etudiants = new ArrayList<>();
    }

    /**
     * Constructeur avec id et taille max (style Thomas).
     */
    public Groupe(int id, int capaciteMax) {
        this.id = id;
        this.nom = "Groupe " + id;
        this.capaciteMax = capaciteMax;
        this.capaciteMin = 0;
        this.etudiants = new ArrayList<>();
    }

    /**
     * Constructeur avec nom (style Maelan).
     */
    public Groupe(String nom) {
        this.id = 0;
        this.nom = nom;
        this.capaciteMax = CAPACITE_MAX_DEFAUT;
        this.capaciteMin = CAPACITE_MIN_DEFAUT;
        this.etudiants = new ArrayList<>();
    }

    // ========== MÉTHODES COMMUNES ==========

    /**
     * Ajoute un étudiant au groupe.
     * 
     * @return true si ajouté, false si groupe plein
     */
    public boolean ajouterEtudiant(Etudiant e) {
        if (etudiants.size() < capaciteMax) {
            etudiants.add(e);
            return true;
        }
        return false;
    }

    /**
     * Ajoute un étudiant sans vérifier la capacité (style Thomas).
     */
    public void ajouter(Etudiant e) {
        etudiants.add(e);
    }

    /**
     * Retire un étudiant du groupe.
     */
    public void retirerEtudiant(Etudiant e) {
        etudiants.remove(e);
    }

    /**
     * Vérifie si le groupe est plein.
     */
    public boolean estPlein() {
        return etudiants.size() >= capaciteMax;
    }

    /**
     * Retourne le nombre de places restantes.
     */
    public int getPlaceRestante() {
        return capaciteMax - etudiants.size();
    }

    // ========== GETTERS ==========

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getTaille() {
        return etudiants.size();
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    // ========== MÉTHODES MODE MAELAN (bac/moyenne) ==========

    /**
     * Calcule la moyenne du groupe.
     */
    public double getMoyenne() {
        if (etudiants.isEmpty())
            return 0;
        double somme = 0;
        for (Etudiant e : etudiants) {
            somme += e.getMoyenne();
        }
        return somme / etudiants.size();
    }

    /**
     * Compte le nombre d'étudiants avec un bac Général.
     */
    public int getNbBacGeneral() {
        int count = 0;
        for (Etudiant e : etudiants) {
            if (e.estBacGeneral())
                count++;
        }
        return count;
    }

    /**
     * Compte le nombre d'étudiants avec un bac Technologique.
     */
    public int getNbBacTechno() {
        return etudiants.size() - getNbBacGeneral();
    }

    // Alias pour compatibilité Thomas
    public int nbGeneral() {
        return getNbBacGeneral();
    }

    public int nbTechno() {
        return getNbBacTechno();
    }

    /**
     * Compte le nombre de filles dans le groupe.
     */
    public int getNbFilles() {
        int count = 0;
        for (Etudiant e : etudiants) {
            if (e.estFille())
                count++;
        }
        return count;
    }

    /**
     * Calcule le pourcentage de filles.
     */
    public double getPourcentageFilles() {
        if (etudiants.isEmpty())
            return 0;
        return (double) getNbFilles() / etudiants.size();
    }

    /**
     * Vérifie si le groupe respecte les contraintes mode Maelan.
     */
    public boolean estValide() {
        int taille = etudiants.size();
        double pctFilles = getPourcentageFilles();
        return taille >= capaciteMin
                && taille <= capaciteMax
                && pctFilles >= 0.10
                && pctFilles <= 0.20;
    }

    // ========== MÉTHODES MODE SOAN (parcours/covoiturage) ==========

    /**
     * Calcule le score du groupe selon les critères Soan.
     * Prend en compte : taille, mixité, parcours, covoiturage.
     */
    public int calculerScore() {
        if (etudiants.isEmpty())
            return 0;

        int score = 0;
        int nb = etudiants.size();

        // Vérification des contraintes "dures"
        boolean tailleValide = (nb >= capaciteMin && nb <= capaciteMax);
        int nbFilles = getNbFilles();
        boolean mixiteValide = (nbFilles == 0 || nbFilles >= 4);

        if (!tailleValide || !mixiteValide) {
            return -10000;
        }

        // Parcours : points pour le parcours majoritaire
        int maxMemeParcours = 0;
        for (int i = 0; i < etudiants.size(); i++) {
            String parcoursCourant = etudiants.get(i).getParcours();
            int compteur = 0;
            for (int j = 0; j < etudiants.size(); j++) {
                if (etudiants.get(j).getParcours().equals(parcoursCourant)) {
                    compteur++;
                }
            }
            if (compteur > maxMemeParcours) {
                maxMemeParcours = compteur;
            }
        }
        score += maxMemeParcours;

        // Bonus mixité
        if (nbFilles >= 4) {
            score += 10;
        }

        // Covoiturage
        for (Etudiant e : etudiants) {
            if (e.getIdCovoiturage() != -1) {
                score += 1;
            }
        }

        return score;
    }

    // ========== MÉTHODES MODE THOMAS (notes/compétences) ==========

    /**
     * Vérifie s'il y a un "leader Java" dans le groupe.
     */
    public boolean aUnLeaderJava() {
        for (Etudiant e : etudiants) {
            if (e.estLeaderJava())
                return true;
        }
        return false;
    }

    /**
     * Vérifie s'il y a un "leader BD" dans le groupe.
     */
    public boolean aUnLeaderBD() {
        for (Etudiant e : etudiants) {
            if (e.estLeaderBD())
                return true;
        }
        return false;
    }

    /**
     * Compte le nombre de redoublants.
     */
    public int nbRedoublants() {
        int c = 0;
        for (Etudiant e : etudiants) {
            if (e.isRedoublant())
                c++;
        }
        return c;
    }

    /**
     * Compte le nombre d'étudiants en recherche d'apprentissage.
     */
    public int nbRecherche() {
        int c = 0;
        for (Etudiant e : etudiants) {
            if (e.getApprentissage() == Etudiant.StatutApprentissage.RECHERCHE)
                c++;
        }
        return c;
    }

    // Alias pour compatibilité
    public int nbApprentis() {
        return nbRecherche();
    }

    /**
     * Calcule la qualité du groupe selon les critères Thomas.
     */
    public int calculerQualite() {
        int score = 100;
        if (!aUnLeaderJava())
            score -= 40;
        if (!aUnLeaderBD())
            score -= 40;
        int ecart = Math.abs(nbGeneral() - nbTechno());
        if (ecart > 1)
            score -= (ecart * 10);
        return Math.max(0, score);
    }

    @Override
    public String toString() {
        return nom + " (" + etudiants.size() + " étudiants)";
    }
}
