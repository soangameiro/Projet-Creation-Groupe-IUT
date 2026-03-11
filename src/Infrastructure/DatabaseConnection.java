package Infrastructure;

import java.sql.*;

/**
 * ========================================================
 * DATABASECONNECTION.JAVA - Connexion à la base de données
 * ========================================================
 * 
 * Cette classe gère la CONNEXION DIRECTE à la base de données MySQL.
 * 
 * NOTE : Dans ce projet, on utilise plutôt l'API REST via RestService.
 * Cette classe est disponible pour des besoins particuliers
 * (requêtes complexes, performances, etc.)
 * 
 * QU'EST-CE QU'UNE BASE DE DONNEES ?
 * - Un logiciel qui stocke des données de manière structurée
 * - MySQL = un système de gestion de base de données populaire
 * - On interagit avec via des requêtes SQL
 * 
 * PRINCIPE DU SINGLETON :
 * - On ne veut qu'UNE SEULE connexion à la base de données
 * - La connexion est stockée dans une variable statique
 * - getConnection() retourne toujours la même instance
 * 
 * @author SAE S3 - Groupe
 */
public class DatabaseConnection {

    // =================================================================
    // CONFIGURATION DE LA BASE DE DONNEES
    // =================================================================
    // Ces constantes définissent comment se connecter à MySQL

    /** Adresse du serveur de base de données */
    private static final String SERVEUR = "localhost";

    /** Nom de la base de données */
    private static final String NOM_BDD = "saes3-mjahier";

    /** Nom d'utilisateur pour la connexion */
    private static final String UTILISATEUR = "saes3-mjahier";

    /** Mot de passe pour la connexion */
    private static final String MOT_DE_PASSE = "A60qCz74cFjMQDPY";

    /**
     * URL de connexion JDBC.
     * Format : jdbc:mysql://serveur/base?options
     * 
     * Options utilisées :
     * - useSSL=false : pas de connection sécurisée (pour dev)
     * - serverTimezone=UTC : fuseau horaire par défaut
     */
    private static final String URL_CONNEXION = "jdbc:mysql://" + SERVEUR + "/" + NOM_BDD +
            "?useSSL=false&serverTimezone=UTC";

    // =================================================================
    // SINGLETON - Instance unique de la connexion
    // =================================================================

    /** La connexion unique (pattern Singleton) */
    private static Connection connexion = null;

    // =================================================================
    // METHODE PRINCIPALE : OBTENIR LA CONNEXION
    // =================================================================

    /**
     * Retourne la connexion à la base de données.
     * 
     * Si la connexion n'existe pas encore, elle est créée.
     * Si elle existe déjà, elle est réutilisée.
     * 
     * @return L'objet Connection pour exécuter des requêtes SQL
     * @throws SQLException Si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        // Vérifier si on a déjà une connexion valide
        if (connexion == null || connexion.isClosed()) {
            try {
                // 1. Charger le driver MySQL
                // Cette ligne dit à Java comment communiquer avec MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // 2. Créer la connexion
                connexion = DriverManager.getConnection(
                        URL_CONNEXION, UTILISATEUR, MOT_DE_PASSE);

                System.out.println("✓ Connexion BDD réussie: " + NOM_BDD);

            } catch (ClassNotFoundException erreur) {
                // Le driver MySQL n'est pas installé
                System.err.println("✗ Driver MySQL introuvable!");
                System.err.println("Téléchargez-le sur: https://dev.mysql.com/downloads/connector/j/");
                throw new SQLException(erreur);
            }
        }
        return connexion;
    }

    // =================================================================
    // FERMETURE DE LA CONNEXION
    // =================================================================

    /**
     * Ferme proprement la connexion à la base de données.
     * A appeler quand l'application se termine.
     */
    public static void fermerConnexion() {
        if (connexion != null) {
            try {
                connexion.close();
                System.out.println("Connexion à la BDD fermée");
            } catch (SQLException erreur) {
                System.err.println("Erreur fermeture connexion: " + erreur.getMessage());
            }
        }
    }

    // =================================================================
    // PROGRAMME DE TEST
    // =================================================================

    /**
     * Méthode main pour tester la connexion.
     * Lancez cette classe directement pour vérifier que la BDD est accessible.
     */
    public static void main(String[] args) {
        try {
            // Tester la connexion
            Connection conn = getConnection();
            System.out.println("TEST: Connexion OK!");

            // Tester une requête simple
            Statement statement = conn.createStatement();
            ResultSet resultat = statement.executeQuery(
                    "SELECT COUNT(*) as nb FROM Enseignant");

            if (resultat.next()) {
                int nombreEnseignants = resultat.getInt("nb");
                System.out.println("Nombre d'enseignants en BDD: " + nombreEnseignants);
            }

            // Fermer proprement
            resultat.close();
            statement.close();
            fermerConnexion();

        } catch (SQLException erreur) {
            System.err.println("ERREUR: " + erreur.getMessage());
            erreur.printStackTrace();
        }
    }

    // Alias pour compatibilité avec l'ancien code
    public static void closeConnection() {
        fermerConnexion();
    }
}
