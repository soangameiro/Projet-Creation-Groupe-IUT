package Utilisateur;

import Education.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ========================================================
 * RESPONSABLE_FILIERE.JAVA - Responsable d'une filière
 * ========================================================
 * 
 * Cette classe HERITE de Enseignant.
 * C'est un enseignant avec des DROITS SUPPLEMENTAIRES :
 * - Gérer les étudiants de sa filière (ajouter/supprimer)
 * - Créer et modifier les groupes
 * - Attribuer des notes
 * 
 * HERITAGE MULTIPLE :
 * ResponsableFiliere -> Enseignant -> Personne
 * (hérite des attributs et méthodes des deux classes parentes)
 * 
 * @author SAE S3 - Groupe
 */
public class Responsable_filiere extends Enseignant {

    // =================================================================
    // ATTRIBUTS SPECIFIQUES
    // =================================================================

    /** Liste des promotions gérées par ce responsable */
    private List<Promotion> sesPromotions;

    /** La filière dont il est responsable (optionnel) */
    private Matiere saFiliere;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée un nouveau responsable de filière.
     * 
     * @param nom     Nom de famille
     * @param prenom  Prénom
     * @param genre   Genre ("H" ou "F")
     * @param email   Adresse email
     * @param adresse Adresse postale
     * @param tel     Numéro de téléphone
     * @param id      Identifiant unique
     * @param type    Type (sera "Responsable de filière")
     */
    public Responsable_filiere(String nom, String prenom, String genre,
            String email, String adresse, String tel,
            int id, String type) {
        // Appeler le constructeur de la classe parente (Enseignant)
        super(nom, prenom, genre, email, adresse, tel, id, type);

        this.sesPromotions = new ArrayList<>();
    }

    // =================================================================
    // METHODES SPECIFIQUES AU RESPONSABLE DE FILIERE
    // =================================================================

    /**
     * Ajoute un étudiant à une promotion.
     * 
     * Un responsable de filière a le DROIT de gérer les étudiants,
     * contrairement à un enseignant classique (lecture seule).
     * 
     * @param etudiant  L'étudiant à ajouter
     * @param promotion La promotion cible
     */
    public void ajouterEtudiantAPromotion(Etudiant etudiant, Promotion promotion) {
        promotion.ajouterEtudiant(etudiant);
    }

    /**
     * Retire un étudiant d'une promotion.
     * 
     * @param etudiant  L'étudiant à retirer
     * @param promotion La promotion source
     */
    public void retirerEtudiantDePromotion(Etudiant etudiant, Promotion promotion) {
        promotion.retirerEtudiant(etudiant);
    }

    // =================================================================
    // GETTERS
    // =================================================================

    public List<Promotion> getSesPromotions() {
        return sesPromotions;
    }

    public Matiere getSaFiliere() {
        return saFiliere;
    }

    // Alias pour compatibilité
    public void ajouterEtudiantAPromo(Etudiant e, Promotion p) {
        ajouterEtudiantAPromotion(e, p);
    }
}