package Infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ========================================================
 * RESTSERVICE.JAVA - Communication avec l'API REST
 * ========================================================
 * 
 * 
 * - GET = Récupérer des données (lire)
 * - POST = Créer de nouvelles données
 * - PUT = Modifier des données existantes
 * - DELETE = Supprimer des données
 * 
 * @author SAE S3 - Groupe
 */
public class RestService {

    // =================================================================
    // CONSTANTES
    // =================================================================

    /**
     * URL de base de l'API.
     * Toutes les requêtes seront envoyées à cette adresse + un endpoint.
     * 
     * Exemple : BASE_URL + "/etudiants" = "https://.../api/etudiants"
     */
    private static final String URL_BASE = "https://projets.iut-orsay.fr/saes3-mjahier/api";

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    /**
     * Client HTTP pour envoyer les requêtes.
     * Java 11+ fournit HttpClient pour faire des requêtes web facilement.
     */
    private final HttpClient clientHttp;

    /**
     * Token d'authentification (optionnel).
     * Certaines API demandent un token pour prouver qu'on est connecté.
     */
    private String tokenAuth;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée un nouveau service de communication avec l'API.
     */
    public RestService() {
        // Créer un client HTTP avec un timeout de 10 secondes
        // Timeout = si le serveur ne répond pas en 10s, on abandonne
        this.clientHttp = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    // =================================================================
    // CONFIGURATION
    // =================================================================

    /**
     * Définit le token d'authentification.
     * 
     * @param token Le token reçu après connexion
     */
    public void setAuthToken(String token) {
        this.tokenAuth = token;
    }

    // =================================================================
    // METHODES HTTP DE BASE
    // =================================================================

    /**
     * Envoie une requête GET pour LIRE des données.
     * 
     * @param endpoint Le chemin après l'URL de base (ex: "/etudiants")
     * @return La réponse du serveur (JSON), ou null en cas d'erreur
     */
    public String get(String endpoint) {
        try {
            // 1. Construire la requête
            HttpRequest.Builder constructeur = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint)) // L'URL complète
                    .GET() // Méthode GET
                    .header("Content-Type", "application/json"); // On attend du JSON

            // Ajouter le token si on en a un
            if (tokenAuth != null) {
                constructeur.header("Authorization", "Token " + tokenAuth);
            }

            // 2. Envoyer la requête et attendre la réponse
            HttpResponse<String> reponse = clientHttp.send(
                    constructeur.build(),
                    HttpResponse.BodyHandlers.ofString() // On veut la réponse en texte
            );

            // 3. Afficher le résultat dans la console (pour débugger)
            System.out.println("GET " + endpoint + " -> Code: " + reponse.statusCode());

            // 4. Retourner le corps de la réponse
            return reponse.body();

        } catch (Exception e) {
            System.err.println("Erreur GET : " + e.getMessage());
            return null;
        }
    }

    /**
     * Envoie une requête POST pour CREER des données.
     * 
     * @param endpoint Le chemin (ex: "/etudiants")
     * @param json     Les données à envoyer au format JSON
     * @return La réponse du serveur
     */
    public String post(String endpoint, String json) {
        try {
            HttpRequest.Builder constructeur = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Corps de la requête
                    .header("Content-Type", "application/json");

            if (tokenAuth != null) {
                constructeur.header("Authorization", "Token " + tokenAuth);
            }

            HttpResponse<String> reponse = clientHttp.send(
                    constructeur.build(),
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("POST " + endpoint + " -> Code: " + reponse.statusCode());
            return reponse.body();

        } catch (Exception e) {
            System.err.println("Erreur POST : " + e.getMessage());
            return null;
        }
    }

    /**
     * Envoie une requête PUT pour MODIFIER des données.
     * 
     * @param endpoint Le chemin (ex: "/etudiants/5")
     * @param json     Les nouvelles données
     * @return La réponse du serveur
     */
    public String put(String endpoint, String json) {
        try {
            HttpRequest.Builder constructeur = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json");

            if (tokenAuth != null) {
                constructeur.header("Authorization", "Token " + tokenAuth);
            }

            HttpResponse<String> reponse = clientHttp.send(
                    constructeur.build(),
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("PUT " + endpoint + " -> Code: " + reponse.statusCode());
            return reponse.body();

        } catch (Exception e) {
            System.err.println("Erreur PUT : " + e.getMessage());
            return null;
        }
    }

    /**
     * Envoie une requête DELETE pour SUPPRIMER des données.
     * 
     * @param endpoint Le chemin (ex: "/etudiants/5")
     * @return La réponse du serveur
     */
    public String delete(String endpoint) {
        try {
            HttpRequest.Builder constructeur = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint))
                    .DELETE()
                    .header("Content-Type", "application/json");

            if (tokenAuth != null) {
                constructeur.header("Authorization", "Token " + tokenAuth);
            }

            HttpResponse<String> reponse = clientHttp.send(
                    constructeur.build(),
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("DELETE " + endpoint + " -> Code: " + reponse.statusCode());
            return reponse.body();

        } catch (Exception e) {
            System.err.println("Erreur DELETE : " + e.getMessage());
            return null;
        }
    }

    /** Récupère tous les étudiants */
    public String getEtudiants() {
        return get("/etudiants");
    }

    /** Récupère les étudiants d'une promotion spécifique */
    public String getEtudiantsByPromo(int idPromo) {
        return get("/etudiants?promo=" + idPromo);
    }

    /** Récupère tous les enseignants */
    public String getEnseignants() {
        return get("/enseignants");
    }

    /** Récupère toutes les promotions */
    public String getPromotions() {
        return get("/promotions");
    }

    /** Récupère tous les groupes */
    public String getGroupes() {
        return get("/groupes");
    }

    /**
     * Tente de connecter un utilisateur.
     * 
     * @param email    L'email
     * @param password Le mot de passe
     * @return La réponse de l'API (avec token si succès)
     */
    public String login(String email, String password) {
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        return post("/login", json);
    }

    // =================================================================
    // METHODES CRUD POUR LES ETUDIANTS
    // =================================================================
    // CRUD = Create, Read, Update, Delete

    /**
     * Crée un nouvel étudiant dans la base de données.
     */
    public String createEtudiant(String nom, String prenom, int age,
            String genre, String email, int idPromo) {
        // Construire le JSON avec les infos de l'étudiant
        String json = "{" +
                "\"nom\":\"" + nom + "\"," +
                "\"prenom\":\"" + prenom + "\"," +
                "\"age\":" + age + "," +
                "\"genre\":\"" + genre + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"id_promo\":" + idPromo +
                "}";
        return post("/etudiants", json);
    }

    /**
     * Modifie un étudiant existant.
     */
    public String updateEtudiant(int id, String nom, String prenom,
            int age, String genre, String email) {
        String json = "{" +
                "\"nom\":\"" + nom + "\"," +
                "\"prenom\":\"" + prenom + "\"," +
                "\"age\":" + age + "," +
                "\"genre\":\"" + genre + "\"," +
                "\"email\":\"" + email + "\"" +
                "}";
        return put("/etudiants/" + id, json);
    }

    /**
     * Supprime un étudiant.
     */
    public String deleteEtudiant(int id) {
        return delete("/etudiants/" + id);
    }

    // =================================================================
    // METHODES CRUD POUR LES ENSEIGNANTS
    // =================================================================

    /**
     * Crée un nouvel enseignant.
     */
    public String createEnseignant(String nom, String prenom,
            String genre, String email) {
        String json = "{" +
                "\"nom\":\"" + nom + "\"," +
                "\"prenom\":\"" + prenom + "\"," +
                "\"genre\":\"" + genre + "\"," +
                "\"email\":\"" + email + "\"" +
                "}";
        return post("/enseignants", json);
    }

    /**
     * Modifie un enseignant.
     */
    public String updateEnseignant(int id, String nom, String prenom,
            String genre, String email) {
        String json = "{" +
                "\"nom\":\"" + nom + "\"," +
                "\"prenom\":\"" + prenom + "\"," +
                "\"genre\":\"" + genre + "\"," +
                "\"email\":\"" + email + "\"" +
                "}";
        return put("/enseignants/" + id, json);
    }

    /**
     * Supprime un enseignant.
     */
    public String deleteEnseignant(int id) {
        return delete("/enseignants/" + id);
    }
}
