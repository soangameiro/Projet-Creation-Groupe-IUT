package Utilisateur;

import Critere.*;
import Education.*;
import java.util.*;

/**
 * ========================================================
 * ENSEIGNANT.JAVA - Représente un enseignant
 * ========================================================
 * 
 * Cette classe HERITE de Personne.
 * Un Enseignant possède tous les attributs d'une Personne
 * plus ses propres attributs et associations.
 * 
 * ROLES POSSIBLES :
 * - Enseignant classique : peut consulter les étudiants/groupes
 * - Responsable de filière : peut gérer les étudiants d'une filière
 * - Responsable de formation : peut tout faire
 * 
 * ASSOCIATIONS :
 * - Enseigne des Cours (0..*)
 * - Constitue des Groupes (0..*)
 * - Encadre des Promotions (0..*)
 * - Attribue des Notes (0..*)
 * - Crée des Sondages (0..*)
 * - Gère des Etudiants (0..*)
 * 
 * @author SAE S3 - Groupe
 */
public class Enseignant extends Personne {

    // =================================================================
    // ATTRIBUTS SPECIFIQUES
    // =================================================================

    /** Identifiant unique dans la base de données */
    private int idEnseignant;

    /** Type d'enseignant (ex: "Responsable de formation", "Enseignant") */
    private String typeEnseignant;

    // =================================================================
    // ASSOCIATIONS VERS D'AUTRES CLASSES
    // =================================================================
    // Ces listes représentent les relations "1 vers plusieurs"
    // du diagramme de classes UML

    /** Liste des cours enseignés */
    private List<Cours> sesCours;

    /** Liste des groupes constitués par cet enseignant */
    private List<Groupe> groupesConstitues;

    /** Liste des promotions encadrées */
    private List<Promotion> promotionsEncadrees;

    /** Liste des notes attribuées aux étudiants */
    private List<Note> notesAttribuees;

    /** Liste des sondages créés */
    private List<Sondage> sondagesCrees;

    /** Liste des étudiants gérés */
    private List<Etudiant> etudiantsGeres;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée un nouvel enseignant.
     * 
     * @param nom     Nom de famille
     * @param prenom  Prénom
     * @param genre   Genre ("H" ou "F")
     * @param email   Adresse email
     * @param adresse Adresse postale
     * @param tel     Numéro de téléphone
     * @param id      Identifiant unique
     * @param type    Type d'enseignant
     */
    public Enseignant(String nom, String prenom, String genre, String email,
            String adresse, String tel, int id, String type) {
        // Appeler le constructeur parent
        super(nom, prenom, genre, email, adresse, tel);

        // Initialiser les attributs spécifiques
        this.idEnseignant = id;
        this.typeEnseignant = type;

        // Initialiser toutes les listes (vides)
        // Cela évite les NullPointerException quand on appelle getSesCours()
        this.sesCours = new ArrayList<>();
        this.groupesConstitues = new ArrayList<>();
        this.promotionsEncadrees = new ArrayList<>();
        this.notesAttribuees = new ArrayList<>();
        this.sondagesCrees = new ArrayList<>();
        this.etudiantsGeres = new ArrayList<>();
    }

    // =================================================================
    // METHODES DE GESTION
    // =================================================================

    /**
     * Ajoute une promotion à la liste des promotions encadrées.
     * Vérifie que la promotion n'est pas déjà présente.
     * 
     * @param promotion La promotion à ajouter
     */
    public void ajouterPromotion(Promotion promotion) {
        if (!this.promotionsEncadrees.contains(promotion)) {
            this.promotionsEncadrees.add(promotion);
        }
    }

    /**
     * Attribue une note à un étudiant.
     * 
     * @param note La note à attribuer
     */
    public void attribuerNote(Note note) {
        this.notesAttribuees.add(note);
    }

    /**
     * Crée un nouveau sondage.
     * 
     * @param sondage Le sondage à créer
     */
    public void creerSondage(Sondage sondage) {
        this.sondagesCrees.add(sondage);
    }

    // =================================================================
    // GETTERS
    // =================================================================

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public String getTypeEnseignant() {
        return typeEnseignant;
    }

    public List<Cours> getSesCours() {
        return sesCours;
    }

    public List<Groupe> getGroupesConstitues() {
        return groupesConstitues;
    }

    public List<Promotion> getPromotionsEncadrees() {
        return promotionsEncadrees;
    }

    public List<Note> getNotesAttribuees() {
        return notesAttribuees;
    }

    public List<Sondage> getSondagesCrees() {
        return sondagesCrees;
    }

    public List<Etudiant> getEtudiantsGeres() {
        return etudiantsGeres;
    }

    // Alias pour compatibilité avec l'ancien code
    public int getIdEns() {
        return idEnseignant;
    }

    public String getTypeEns() {
        return typeEnseignant;
    }

    public List<Groupe> getGroupeConstitues() {
        return groupesConstitues;
    }

    public List<Note> getNoteAttribuees() {
        return notesAttribuees;
    }
}