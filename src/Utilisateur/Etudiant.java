package Utilisateur;

import Critere.*;
import Education.*;
import java.util.*;

/**
 * ========================================================
 * ETUDIANT.JAVA - Représente un étudiant dans le système
 * ========================================================
 * 
 * Cette classe HERITE de Personne :
 * public class Etudiant extends Personne
 * 
 * Cela signifie qu'un Etudiant possède :
 * - Tous les attributs de Personne (nom, prénom, email, etc.)
 * - PLUS ses propres attributs spécifiques (âge, moyenne, etc.)
 * 
 * L'héritage représente une relation "EST-UN" :
 * Un Etudiant EST UNE Personne
 * 
 * @author SAE S3 - Groupe
 */
public class Etudiant extends Personne {

    private int idEtudiant;

    private int age;

    private float moyenne;

    private int anneeEtude;

    private boolean estApprenti;

    private boolean estRedoublant;

    /** ("General", "Techno", "Pro") */
    private String typeBac;

    private String sondageRepondu;


    private List<Note> sesNotes;

    private Groupe sonGroupe;

    private Covoiturage sonCovoiturage;

    public Etudiant(String nom, String prenom, String genre, String email,
            String adresse, String telephone,
            int idEtudiant, int age, float moyenne, int anneeEtude,
            boolean estApprenti, boolean estRedoublant, String typeBac) {

        // Appeler le constructeur de la classe parente (Personne)
        // super(...) DOIT être la première instruction du constructeur
        super(nom, prenom, genre, email, adresse, telephone);

        // Initialiser les attributs spécifiques à l'étudiant
        this.idEtudiant = idEtudiant;
        this.age = age;
        this.moyenne = moyenne;
        this.anneeEtude = anneeEtude;
        this.estApprenti = estApprenti;
        this.estRedoublant = estRedoublant;
        this.typeBac = typeBac;

        // Initialiser la liste des notes (vide au départ)
        this.sesNotes = new ArrayList<>();
    }

    /**
     * Constructeur simplifié pour utilisation depuis l'API REST.
     */
    public Etudiant(int idEtudiant, String nom, String prenom, String email, String genre) {
        // Appeler le constructeur parent avec des valeurs par défaut
        super(nom, prenom, genre, email, "", "");

        this.idEtudiant = idEtudiant;
        this.age = 20; // Valeur par défaut
        this.anneeEtude = 2;
        this.estApprenti = false;
        this.estRedoublant = false;
        this.sesNotes = new ArrayList<>();

        // Génération aléatoire pour simulation (à remplacer par vraies données)
        java.util.Random rand = new java.util.Random();
        this.moyenne = (float) (8 + rand.nextDouble() * 10); // Entre 8 et 18
        this.typeBac = rand.nextBoolean() ? "General" : "Techno";
    }

    // =================================================================
    // GETTERS - Accesseurs pour lire les attributs
    // =================================================================

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public int getAge() {
        return age;
    }

    public float getMoyenne() {
        return moyenne;
    }

    public int getAnneeEtude() {
        return anneeEtude;
    }

    public boolean isEstApprenti() {
        return estApprenti;
    }

    public boolean isEstRedoublant() {
        return estRedoublant;
    }

    public String getTypeBac() {
        return typeBac;
    }

    public List<Note> getSesNotes() {
        return sesNotes;
    }

    public Groupe getSonGroupe() {
        return sonGroupe;
    }

    // =================================================================
    // SETTERS - Modificateurs pour changer les attributs
    // =================================================================

    public void setMoyenne(float nouvelle) {
        this.moyenne = nouvelle;
    }

    public void setSonGroupe(Groupe groupe) {
        this.sonGroupe = groupe;
    }

    // =================================================================
    // METHODES METIER
    // =================================================================

    /**
     * Ajoute une note à la liste des notes de l'étudiant.
     * 
     * @param note La note à ajouter
     */
    public void ajouterNote(Note note) {
        this.sesNotes.add(note);
    }

    /**
     * Calcule la moyenne des notes pour un type de matière donné.
     * 
     * Utilise les STREAMS Java (programmation fonctionnelle) :
     * - filter() : garde seulement les notes du bon type
     * - mapToDouble() : transforme chaque note en sa valeur numérique
     * - average() : calcule la moyenne
     * - orElse(0.0) : retourne 0 si aucune note
     * 
     * @param type Le type de matière (ex: "Math", "Info")
     * @return La moyenne des notes de ce type
     */
    public float calculerMoyenneParType(String type) {
        double moyenne = sesNotes.stream() // Créer un flux
                .filter(note -> note.getType().equals(type)) // Garder le bon type
                .mapToDouble(note -> note.getValeur()) // Extraire les valeurs
                .average() // Calculer la moyenne
                .orElse(0.0); // Valeur par défaut

        return (float) moyenne;
    }

    // Alias pour compatibilité avec l'ancien code
    public int getId_etu() {
        return idEtudiant;
    }

    public float getMoyEtu() {
        return moyenne;
    }

    public void addNote(Note n) {
        ajouterNote(n);
    }

    public float calculerMoyenneParMatiere(String type) {
        return calculerMoyenneParType(type);
    }
}