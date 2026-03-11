package Education;

/**
 * ========================================================
 * COURS.JAVA - Représente un cours enseigné
 * ========================================================
 * 
 * Un cours est lié à une matière et peut être dispensé
 * par un ou plusieurs enseignants.
 * 
 * Exemples de cours :
 * - "TD de Programmation Java"
 * - "TP Base de Données"
 * - "CM Algorithmique"
 * 
 * @author SAE S3 - Groupe
 */
public class Cours {

    // =================================================================
    // ATTRIBUTS
    // =================================================================
    
    /** Identifiant unique du cours */
    private int idCours;
    
    /** Nom du cours (ex: "TP Java") */
    private String nomCours;
    
    /** La matière à laquelle appartient ce cours */
    private Matiere saMatiere;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================
    
    /**
     * Crée un nouveau cours.
     * 
     * @param id      L'identifiant unique
     * @param nom     Le nom du cours
     * @param matiere La matière parente
     */
    public Cours(int id, String nom, Matiere matiere) {
        this.idCours = id;
        this.nomCours = nom;
        this.saMatiere = matiere;
    }
    
    /**
     * Constructeur par défaut.
     */
    public Cours() {
    }

    // =================================================================
    // GETTERS ET SETTERS
    // =================================================================
    
    public int getIdCours() { 
        return idCours; 
    }
    
    public String getNomCours() { 
        return nomCours; 
    }
    
    public Matiere getSaMatiere() { 
        return saMatiere; 
    }
    
    public void setNomCours(String nom) { 
        this.nomCours = nom; 
    }
    
    public void setSaMatiere(Matiere matiere) { 
        this.saMatiere = matiere; 
    }
}