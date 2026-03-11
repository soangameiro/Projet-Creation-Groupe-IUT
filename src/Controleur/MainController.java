package Controleur;

import Infrastructure.JsonParser;
import Infrastructure.RestService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ========================================================
 * MAINCONTROLLER.JAVA - Contrôleur principal de l'application
 * ========================================================
 * 
 * Ce fichier suit le pattern MVC (Modèle-Vue-Contrôleur) :
 * - MODELE : Les données (Etudiant, Enseignant, Groupe...)
 * - VUE : L'interface graphique (LoginFrame, DashboardEtudiant...)
 * - CONTROLEUR : La logique qui fait le lien entre les deux (ce fichier)
 * 
 * ROLE DU CONTROLEUR :
 * - Gérer l'authentification (connexion/déconnexion)
 * - Stocker les infos de l'utilisateur connecté
 * - Communiquer avec l'API REST (via RestService)
 * 
 * @author SAE S3 - Groupe
 */
public class MainController {

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    /**
     * Service pour communiquer avec l'API REST.
     * "final" = ne peut pas être modifié après initialisation.
     */
    private final RestService serviceApi;

    // Informations sur l'utilisateur actuellement connecté
    private String emailUtilisateur; // Email de connexion
    private String roleUtilisateur; // "etudiant", "enseignant", etc.
    private String nomUtilisateur; // Nom de famille
    private String prenomUtilisateur; // Prénom
    private int idUtilisateur; // ID dans la base de données

    // =================================================================
    // CONSTANTES DE ROLE
    // =================================================================
    // "public static final" = constante accessible partout :
    // MainController.ROLE_ETUDIANT

    /** Role : Etudiant (peut voir ses infos, répondre aux sondages) */
    public static final String ROLE_ETUDIANT = "etudiant";

    /** Role : Enseignant (peut voir les étudiants et groupes) */
    public static final String ROLE_ENSEIGNANT = "enseignant";

    /** Role : Responsable de Formation (peut tout faire) */
    public static final String ROLE_RESP_FORMATION = "responsable_formation";

    /** Role : Responsable de Filière (peut gérer étudiants et groupes) */
    public static final String ROLE_RESP_FILIERE = "responsable_filiere";

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée un nouveau contrôleur.
     * Initialise le service API pour communiquer avec le serveur.
     */
    public MainController() {
        this.serviceApi = new RestService();
    }

    // =================================================================
    // METHODE D'AUTHENTIFICATION
    // =================================================================

    /**
     * Tente de connecter un utilisateur avec son email et mot de passe.
     * 
     * FONCTIONNEMENT :
     * 1. Envoie une requête POST à l'API /login
     * 2. L'API vérifie les identifiants dans la base de données
     * 3. Si OK, l'API renvoie les infos de l'utilisateur en JSON
     * 4. On extrait ces infos et on les stocke
     * 
     * @param email      L'email de l'utilisateur
     * @param motDePasse Le mot de passe
     * @return true si la connexion réussit, false sinon
     */
    public boolean login(String email, String motDePasse) {
        System.out.println("Tentative de connexion : " + email);

        // Construire le JSON à envoyer à l'API
        // Format : {"email":"...", "password":"..."}
        String jsonAEnvoyer = "{\"email\":\"" + email + "\",\"password\":\"" + motDePasse + "\"}";

        // Envoyer la requête POST et récupérer la réponse
        String reponseApi = serviceApi.post("/login", jsonAEnvoyer);
        System.out.println("Réponse de l'API : " + reponseApi);

        // Vérifier si la connexion a réussi
        // L'API renvoie "success":true si les identifiants sont corrects
        if (reponseApi != null && reponseApi.contains("\"success\":true")) {

            // Extraire les informations de la réponse JSON
            emailUtilisateur = email;
            roleUtilisateur = JsonParser.extraireValeur(reponseApi, "role");
            nomUtilisateur = JsonParser.extraireValeur(reponseApi, "nom");
            prenomUtilisateur = JsonParser.extraireValeur(reponseApi, "prenom");

            // Extraire l'ID (c'est un nombre, pas une chaîne)
            try {
                String idTexte = JsonParser.extraireValeur(reponseApi, "id");

                // Si extraireValeur n'a pas trouvé l'ID, essayer autrement
                if (idTexte.isEmpty()) {
                    // Pattern pour trouver "id": suivi d'un nombre
                    Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
                    Matcher matcher = pattern.matcher(reponseApi);
                    if (matcher.find()) {
                        idTexte = matcher.group(1); // group(1) = le nombre capturé
                    }
                }
                idUtilisateur = Integer.parseInt(idTexte);

            } catch (Exception e) {
                System.out.println("Erreur lors de l'extraction de l'ID : " + e.getMessage());
                idUtilisateur = 0;
            }

            System.out.println("Connexion réussie - Role : " + roleUtilisateur + ", ID : " + idUtilisateur);
            return true;
        }

        System.out.println("Echec de la connexion");
        return false;
    }

    // NOTE: Utilise désormais Infrastructure.JsonParser pour le parsing JSON

    // =================================================================
    // METHODES D'ACCES A L'API
    // =================================================================

    /**
     * Récupère la liste de tous les enseignants depuis l'API.
     * 
     * @return Le JSON contenant tous les enseignants
     */
    public String getEnseignants() {
        return serviceApi.get("/enseignants");
    }

    /**
     * Récupère la liste de tous les étudiants depuis l'API.
     * 
     * @return Le JSON contenant tous les étudiants
     */
    public String getEtudiants() {
        return serviceApi.get("/etudiants");
    }

    /**
     * Récupère la liste de toutes les promotions depuis l'API.
     * 
     * @return Le JSON contenant toutes les promotions
     */
    public String getPromotions() {
        return serviceApi.get("/promotions");
    }

    // =================================================================
    // GETTERS - Accès aux infos de l'utilisateur connecté
    // =================================================================

    /** @return Le rôle de l'utilisateur connecté */
    public String getCurrentUserRole() {
        return roleUtilisateur;
    }

    /** @return Le nom de famille de l'utilisateur connecté */
    public String getCurrentUserNom() {
        return nomUtilisateur;
    }

    /** @return Le prénom de l'utilisateur connecté */
    public String getCurrentUserPrenom() {
        return prenomUtilisateur;
    }

    /** @return L'ID de l'utilisateur connecté */
    public int getCurrentUserId() {
        return idUtilisateur;
    }

    /** @return L'email de l'utilisateur connecté */
    public String getCurrentUserEmail() {
        return emailUtilisateur;
    }
}
