package Utilisateur;

/**
 * ========================================================
 * PERSONNE.JAVA - Classe de base pour tous les utilisateurs
 * ========================================================
 * 
 * Cette classe est ABSTRAITE, ce qui signifie :
 * - On NE PEUT PAS créer d'objet Personne directement : new Personne(...)
 * INTERDIT
 * - Elle sert de MODELE pour les classes qui en héritent (Etudiant, Enseignant)
 * 
 * POURQUOI faire ça ?
 * Etudiant et Enseignant ont des attributs en commun (nom, prénom, email...).
 * Au lieu de les écrire 2 fois, on les met dans Personne et on HERITE.
 * 
 * HERITAGE en Java :
 * public class Etudiant extends Personne { ... }
 * -> Etudiant "est une" Personne, il a tous ses attributs + les siens
 * 
 * @author SAE S3 - Groupe
 */
public abstract class Personne {

    // =================================================================
    // ATTRIBUTS - Les informations stockées pour chaque personne
    // =================================================================
    // "private" = accessible UNIQUEMENT dans cette classe (encapsulation)

    private String nom; // Ex: "Dupont"
    private String prenom; // Ex: "Jean"
    private String genre; // "H" pour Homme, "F" pour Femme
    private String courriel; // Ex: "jean.dupont@univ.fr"
    private String adresse; // Adresse postale
    private String telephone; // Numéro de téléphone

    // =================================================================
    // CONSTRUCTEUR - Méthode appelée lors de la création d'un objet
    // =================================================================

    /**
     * Constructeur de la classe Personne.
     * 
     * Comme Personne est abstraite, ce constructeur sera appelé
     * par les classes enfants via super(...).
     * 
     * Exemple dans Etudiant :
     * public Etudiant(...) {
     * super(nom, prenom, genre, mail, adresse, tel); // Appelle ce constructeur
     * // Puis initialise les attributs spécifiques à Etudiant
     * }
     * 
     * @param nom       Le nom de famille
     * @param prenom    Le prénom
     * @param genre     Le genre ("H" ou "F")
     * @param courriel  L'adresse email
     * @param adresse   L'adresse postale
     * @param telephone Le numéro de téléphone
     */
    public Personne(String nom, String prenom, String genre,
            String courriel, String adresse, String telephone) {
        // "this" fait référence à l'objet actuel
        // Permet de différencier l'attribut "nom" du paramètre "nom"
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.courriel = courriel;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    // =================================================================
    // GETTERS - Méthodes pour LIRE les attributs privés
    // =================================================================
    // Convention : getNomAttribut() pour récupérer la valeur

    /**
     * Retourne le nom de famille.
     * 
     * @return Le nom (ex: "Dupont")
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prénom.
     * 
     * @return Le prénom (ex: "Jean")
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Retourne l'adresse email.
     * 
     * @return L'email (ex: "jean.dupont@univ.fr")
     */
    public String getCourriel() {
        return courriel;
    }

    /**
     * Retourne le genre.
     * 
     * @return "H" ou "F"
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Retourne l'adresse postale.
     * 
     * @return L'adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Retourne le numéro de téléphone.
     * 
     * @return Le téléphone
     */
    public String getTelephone() {
        return telephone;
    }

    // =================================================================
    // SETTERS - Méthodes pour MODIFIER les attributs privés
    // =================================================================
    // Convention : setNomAttribut(nouvelleValeur)

    /**
     * Modifie le nom de famille.
     * 
     * @param nom Le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie le prénom.
     * 
     * @param prenom Le nouveau prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Modifie l'adresse email.
     * 
     * @param courriel Le nouvel email
     */
    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }
}