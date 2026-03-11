package Critere;

/**
 * ========================================================
 * ACTIVITE.JAVA - Activité extra-scolaire d'un étudiant
 * ========================================================
 * 
 * Cette classe représente une activité pratiquée par un étudiant
 * en dehors des cours (sport, musique, association...).
 * 
 * C'est un CRITERE pouvant être utilisé pour la création de groupes :
 * on peut essayer de regrouper des étudiants avec des centres
 * d'intérêt communs.
 * 
 * @author SAE S3 - Groupe
 */
public class Activite {

    // =================================================================
    // ATTRIBUTS
    // =================================================================
    
    /** Identifiant unique de l'activité */
    private int idActivite;
    
    /** Nom de l'activité (ex: "Football", "Piano", "BDE") */
    private String libelle;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================
    
    /**
     * Crée une nouvelle activité.
     * 
     * @param id      L'identifiant unique
     * @param libelle Le nom de l'activité
     */
    public Activite(int id, String libelle) {
        this.idActivite = id;
        this.libelle = libelle;
    }
    
    /**
     * Constructeur par défaut.
     */
    public Activite() {
    }

    // =================================================================
    // GETTERS ET SETTERS
    // =================================================================
    
    public int getIdActivite() { 
        return idActivite; 
    }
    
    public String getLibelle() { 
        return libelle; 
    }
    
    public void setLibelle(String libelle) { 
        this.libelle = libelle; 
    }
}