package Critere;

import Utilisateur.Etudiant;
import java.util.ArrayList;
import java.util.List;

/**
 * ========================================================
 * COVOITURAGE.JAVA - Gestion des groupes de covoiturage
 * ========================================================
 * 
 * Cette classe représente un groupe d'étudiants qui partagent
 * le même trajet pour venir à l'université.
 * 
 * C'est un CRITERE pris en compte par l'algorithme de groupes :
 * on essaie de mettre ensemble les étudiants qui font du covoiturage.
 * 
 * ASSOCIATION :
        
    

    n Covoiturage contient plusieu
        s Etudiants (0..*)
    

    
        
    
 * @author SAE S3 - Groupe
 */
public class Covoiturage {
    
    // =================================================================
    // ATTRIBUTS
    // =================================================================
    
    /** Identifiant unique du covoiturage */
    private int idCovoiturage;
    
    /** Description du covoiturage (ex: "Départ Massy 8h") */
    private String libelle;
    
    /** Liste des étudiants qui partagent ce covoiturage */
    private List<Etudiant> sesMembres;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================
    
    /**
     * Crée un nouveau groupe de covoiturage.
     * 
     * @param idCovoiturage L'identifiant unique
     * @param libelle       La description du covoiturage
     */
    public Covoiturage(int idCovoiturage, String libelle) {
        this.idCovoiturage = idCovoiturage;
        this.libelle = libelle;
        this.sesMembres = new ArrayList<>();
    }

    // =================================================================
    // GESTION DES MEMBRES
    // =================================================================
    
    /**
     * Ajoute un étudiant au groupe de covoiturage.
     * 
     * @param etudiant L'étudiant à ajouter
     */
    public void ajouterMembre(Etudiant etudiant) {
        this.sesMembres.add(etudiant);
    }
    
    /**
     * Retire un étudiant du groupe de covoiturage.
     * 
     * @param etudiant L'étudiant à retirer
     */
    public void retirerMembre(Etudiant etudiant) {
        this.sesMembres.remove(etudiant);
    }

    // =================================================================
    // GETTERS
    // =================================================================
    
    public int getIdCovoiturage() { 
        return idCovoiturage; 
    }
    
    public String getLibelle() { 
        return libelle; 
    }
    
    public List<Etudiant> getSesMembres() { 
        return sesMembres; 
    }
    
    // Alias pour compatibilité
    public int getIdCovoit() { return idCovoiturage; }
    public String getLibCovoit() { return libelle; }
}