package Utilisateur;

/**
 * ========================================================
 * RESPONSABLE_FORMATION.JAVA - Responsable de toute la formation
 * ========================================================
 * 
 * Cette classe HERITE de Enseignant.
 * C'est le niveau de privilège le plus élevé dans l'application.
 * 
 * DROITS COMPLETS :
 * - Tout ce qu'un enseignant peut faire
 * - Créer et supprimer des comptes enseignants
 * - Gérer toutes les promotions et filières
 * - Configurer l'application
 * 
 * HERITAGE :
 * Responsable_formation -> Enseignant -> Personne
 * 
 * @author SAE S3 - Groupe
 */
public class Responsable_formation extends Enseignant {

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée un nouveau responsable de formation.
     * 
     * @param nom     Nom de famille
     * @param prenom  Prénom
     * @param genre   Genre ("H" ou "F")
     * @param email   Adresse email
     * @param adresse Adresse postale
     * @param tel     Numéro de téléphone
     * @param id      Identifiant unique
     * @param type    Type (sera "Responsable de formation")
     */
    public Responsable_formation(String nom, String prenom, String genre,
            String email, String adresse, String tel,
            int id, String type) {
        // Appeler le constructeur parent (Enseignant)
        super(nom, prenom, genre, email, adresse, tel, id, type);
    }

    // =================================================================
    // METHODES SPECIFIQUES AU RESPONSABLE DE FORMATION
    // =================================================================

    /**
     * Crée un nouveau compte enseignant dans le système.
     * 
     * Cette méthode est réservée au responsable de formation.
     * Un simple enseignant ne peut pas créer de comptes.
     * 
     * @param nom    Nom de famille du nouvel enseignant
     * @param prenom Prénom du nouvel enseignant
     * @param type   Type ("Enseignant", "Responsable de filiere", etc.)
     */
    public void creerCompteEnseignant(String nom, String prenom, String type) {
        // Cette méthode devrait appeler l'API REST pour créer le compte
        // Pour l'instant, on affiche juste un message de confirmation
        System.out.println("Création du compte enseignant : " + prenom + " " + nom);
        System.out.println("Type : " + type);

        // TODO: Implémenter l'appel API via RestService
        // api.createEnseignant(nom, prenom, "?", email);
    }

    /**
     * Supprime un compte enseignant du système.
     * 
     * @param idEnseignant L'ID de l'enseignant à supprimer
     */
    public void supprimerCompteEnseignant(int idEnseignant) {
        System.out.println("Suppression de l'enseignant ID: " + idEnseignant);
        // TODO: Implémenter l'appel API
    }
}