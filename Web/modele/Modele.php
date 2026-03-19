<?php
// On inclut votre fichier de configuration
require_once __DIR__ . '/../config/connexion.php';

class Modele {

    // Cette fonction sert d'intermédiaire pour récupérer votre connexion existante
    public static function getPdo() {
        // Si la connexion n'est pas encore faite, on la lance
        if (Connexion::pdo() == null) {
            Connexion::connect();
        }
        // On retourne l'objet PDO de votre classe Connexion
        return Connexion::pdo();
    }
}
?>