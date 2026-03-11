package Critere;

/**
 * ========================================================
 * REPONSE.JAVA - Réponse d'un étudiant à un sondage
 * ========================================================
 * 
 * Cette classe représente la réponse donnée par un étudiant
 * à une question de sondage.
 * 
 * ASSOCIATION :
 * - Une Reponse appartient à un Sondage (relation parent)
 * - Un Sondage peut avoir plusieurs Réponses
 * 
        
    
 * @author SAE S3 - Groupe
 */
public class Reponse {
    
    // =================================================================
    // ATTRIBUTS
    // =================================================================
    
    /** Identifiant unique de la réponse */
    private int idReponse;
    
    /** Contenu de la réponse (ex: "Oui", "Non", "Sans avis") */
    private String contenu;
    
    /** Le sondage auquel cette réponse est associée */
    private Sondage sonSondage;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================
    
    /**
     * Crée une nouvelle réponse à un sondage.
     * 
     * @param idReponse L'identifiant unique
     * @param contenu   Le texte de la réponse
     * @param sondage   Le sondage parent
     */
    public Reponse(int idReponse, String contenu, Sondage sondage) {
        this.idReponse = idReponse;
        this.contenu = contenu;
        this.sonSondage = sondage;
    }

    // =================================================================
    // GETTERS
    // =================================================================
    
    public int getIdReponse() { 
        return idReponse; 
    }
    
    public String getContenu() { 
        return contenu; 
    }
    
    public Sondage getSonSondage() { 
        return sonSondage; 
    }
}