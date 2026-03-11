package Education;

import java.util.*;
import Utilisateur.*;

/**
 * ========================================================
 * PROMOTION.JAVA - Représente une promotion d'étudiants
 * ========================================================
 * 
 * Une promotion regroupe tous les étudiants d'une même année.
 * Exemple : "BUT2 Informatique 2024-2025"
 * 
 * Une promotion peut être divisée en plusieurs GROUPES
 * pour les TD et TP.
 * 
 * RELATION avec d'autres classes :
 * - Une Promotion contient plusieurs Etudiants (1..*)
 * - Une Promotion contient plusieurs Groupes (0..*)
 * - Une Promotion appartient à une Filière (1)
 * 
 * @author SAE S3 - Groupe
 */
public class Promotion {

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    /** Identifiant unique de la promotion */
    private int idPromo;

    /** Nom de la promotion (ex: "BUT2 Info 2024") */
    private String nomPromo;

    /** Nombre d'étudiants (calculé automatiquement) */
    private int nombreEleves;

    /** Nombre de groupes souhaités (défini par le responsable) */
    private int nombreGroupes;

    // =================================================================
    // ASSOCIATIONS
    // =================================================================

    /** Liste de tous les étudiants de cette promotion */
    private List<Etudiant> sesEtudiants;

    /** Liste de tous les groupes de cette promotion */
    private List<Groupe> sesGroupes;

    /** La filière à laquelle appartient cette promotion */
    private Filiere saFiliere;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée une nouvelle promotion.
     * 
     * @param id  L'identifiant unique
     * @param nom Le nom de la promotion
     */
    public Promotion(int id, String nom) {
        this.idPromo = id;
        this.nomPromo = nom;
        this.sesEtudiants = new ArrayList<>();
        this.sesGroupes = new ArrayList<>();
    }

    // =================================================================
    // GESTION DES ETUDIANTS
    // =================================================================

    /**
     * Ajoute un étudiant à la promotion.
     * Met à jour automatiquement le compteur.
     * 
     * @param etudiant L'étudiant à ajouter
     */
    public void ajouterEtudiant(Etudiant etudiant) {
        this.sesEtudiants.add(etudiant);
        this.nombreEleves = sesEtudiants.size();
    }

    /**
     * Retire un étudiant de la promotion.
     * 
     * @param etudiant L'étudiant à retirer
     */
    public void retirerEtudiant(Etudiant etudiant) {
        this.sesEtudiants.remove(etudiant);
        this.nombreEleves = sesEtudiants.size();
    }

    // =================================================================
    // GESTION DES GROUPES
    // =================================================================

    /**
     * Définit le nombre de groupes à créer.
     * Cette valeur est utilisée par l'algorithme de création de groupes.
     * 
     * @param nombre Le nombre de groupes souhaités
     */
    public void setNombreGroupes(int nombre) {
        this.nombreGroupes = nombre;
    }

    /**
     * Crée les groupes vides en fonction du nombre demandé.
     * 
     * Cette méthode est appelée AVANT l'algorithme de répartition.
     * Elle crée des groupes vides qui seront ensuite remplis.
     */
    public void initialiserGroupes() {
        // Vider la liste des groupes existants
        this.sesGroupes.clear();

        // Créer le nombre de groupes demandés
        for (int i = 1; i <= nombreGroupes; i++) {
            Groupe nouveauGroupe = new Groupe(i, "Groupe " + i, this);
            sesGroupes.add(nouveauGroupe);
        }
    }

    // =================================================================
    // GETTERS
    // =================================================================

    public int getIdPromo() {
        return idPromo;
    }

    public String getNomPromo() {
        return nomPromo;
    }

    public int getNombreEleves() {
        return nombreEleves;
    }

    public int getNombreGroupes() {
        return nombreGroupes;
    }

    public List<Etudiant> getSesEtudiants() {
        return sesEtudiants;
    }

    public List<Groupe> getSesGroupes() {
        return sesGroupes;
    }

    public Filiere getSaFiliere() {
        return saFiliere;
    }

    // =================================================================
    // SETTERS
    // =================================================================

    public void setNomPromo(String nom) {
        this.nomPromo = nom;
    }

    public void setSaFiliere(Filiere filiere) {
        this.saFiliere = filiere;
    }

    // Alias pour compatibilité
    public void setNbGroupe(int nb) {
        setNombreGroupes(nb);
    }
}