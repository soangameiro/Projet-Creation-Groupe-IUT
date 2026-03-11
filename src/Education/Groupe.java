package Education;

import Utilisateur.Etudiant;
import java.util.*;

/**
 * ========================================================
 * GROUPE.JAVA - Représente un groupe de TD/TP
 * ========================================================
 * Enrichi avec des méthodes d'analyse pour les algorithmes gloutons.
 */
public class Groupe {

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    private int idGroupe;
    private String libelle;
    private Promotion saPromotion;
    private List<Etudiant> sesMembres;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    public Groupe(int id, String libelle, Promotion promo) {
        this.idGroupe = id;
        this.libelle = libelle;
        this.saPromotion = promo;
        this.sesMembres = new ArrayList<>();
    }

    // =================================================================
    // METHODES DE GESTION
    // =================================================================

    public void ajouterMembre(Etudiant etudiant) {
        this.sesMembres.add(etudiant);
    }

    public void retirerMembre(Etudiant etudiant) {
        this.sesMembres.remove(etudiant);
    }

    public int getNombreMembres() {
        return sesMembres.size();
    }

    public List<Etudiant> getSesMembres() {
        return sesMembres;
    }

    // =================================================================
    // METHODES ANALYTIQUES (POUR LES ALGORITHMES)
    // =================================================================

    /**
     * Calcule la moyenne générale du groupe.
     */
    public float calculerMoyenneGroupe() {
        if (sesMembres.isEmpty()) return 0;
        float somme = 0;
        for (Etudiant e : sesMembres) somme += e.getMoyenne();
        return somme / sesMembres.size();
    }

    /**
     * Récupère la note d'un étudiant pour une matière donnée.
     * @param type "Java", "BD", etc.
     */
    private float getNoteValeur(Etudiant e, String type) {
        if (e.getSesNotes() == null) return 0.0f;
        for (Note n : e.getSesNotes()) {
            if (n.getType().equalsIgnoreCase(type)) return n.getValeur();
        }
        return 0.0f;
    }

    // --- Indicateurs de Compétences (Glouton 1 & 2) ---

    public boolean aUnLeaderJava() {
        for (Etudiant e : sesMembres) {
            if (getNoteValeur(e, "Java") >= 14.0) return true;
        }
        return false;
    }

    public boolean aUnLeaderBD() {
        for (Etudiant e : sesMembres) {
            if (getNoteValeur(e, "BD") >= 14.0) return true;
        }
        return false;
    }

    // --- Indicateurs de Diversité ---

    public int nbGeneral() {
        int c = 0;
        for (Etudiant e : sesMembres) if ("General".equalsIgnoreCase(e.getTypeBac())) c++;
        return c;
    }

    public int nbTechno() {
        int c = 0;
        for (Etudiant e : sesMembres) if ("Techno".equalsIgnoreCase(e.getTypeBac())) c++;
        return c;
    }

    public int getNbFilles() {
        int c = 0;
        for (Etudiant e : sesMembres) if ("F".equalsIgnoreCase(e.getGenre())) c++;
        return c;
    }

    public int nbRedoublants() {
        int c = 0;
        for (Etudiant e : sesMembres) if (e.isEstRedoublant()) c++;
        return c;
    }

    // =================================================================
    // GETTERS / SETTERS
    // =================================================================

    public int getIdGroupe() { return idGroupe; }
    public String getLibelle() { return libelle; }
    public Promotion getSaPromotion() { return saPromotion; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}