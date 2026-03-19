<?php
/**
 * Contrôleur Connexion - Authentification des utilisateurs
 */
require_once("modele/Etudiant.php");
require_once("modele/Enseignant.php");
require_once("config/session.php");

class ControleurConnexion
{

    /**
     * Affiche la page de login
     */
    public static function afficherLogin()
    {
        $error = $_GET['error'] ?? null;
        require_once("vue/connexion/login.php");
    }

    /**
     * Connecte l'utilisateur (détection automatique étudiant ou enseignant)
     */
    public static function connecter()
    {
        $email = trim($_POST['email'] ?? '');
        $password = $_POST['password'] ?? '';

        if (empty($email)) {
            self::afficherErreur("Veuillez entrer votre email.");
            return;
        }

        // Essayer d'abord de trouver un enseignant avec cet email
        $enseignant = Enseignant::getByEmail($email);
        
        if ($enseignant && $enseignant->getId()) {
            // C'est un enseignant
            Session::connecterEnseignant($enseignant);
            $userInfo = $enseignant;

            // Redirection selon le rôle
            if ($enseignant->estRespFiliere()) {
                require_once("vue/tableau_bord/accueil_resp_filiere.php");
            } elseif ($enseignant->estRespPromo()) {
                require_once("vue/tableau_bord/accueil_resp_promo.php");
            } else {
                require_once("vue/tableau_bord/accueil_enseignant.php");
            }
            return;
        }

        // Sinon, chercher un étudiant
        $etudiant = Etudiant::getByEmail($email);
        
        if ($etudiant && $etudiant->getId()) {
            // C'est un étudiant
            Session::connecterEtudiant($etudiant);
            $userInfo = $etudiant;
            require_once("vue/tableau_bord/accueil_etudiant.php");
            return;
        }

        // Aucun utilisateur trouvé
        self::afficherErreur("Email introuvable. Vérifiez votre identifiant.");
    }

    /**
     * Affiche le tableau de bord selon le rôle de l'utilisateur connecté
     */
    public static function dashboard()
    {
        Session::requireLogin();

        if (Session::estEtudiant()) {
            $userInfo = Etudiant::getById(Session::getUserId());
            require_once("vue/tableau_bord/accueil_etudiant.php");
        } else {
            $userInfo = Enseignant::getById(Session::getUserId());
            if (Session::estRespFiliere()) {
                require_once("vue/tableau_bord/accueil_resp_filiere.php");
            } elseif (Session::estRespFormation()) {
                require_once("vue/tableau_bord/accueil_resp_promo.php");
            } else {
                require_once("vue/tableau_bord/accueil_enseignant.php");
            }
        }
    }

    /**
     * Déconnecte l'utilisateur
     */
    public static function deconnecter()
    {
        Session::deconnecter();
        header("Location: routeur.php");
        exit;
    }

    /**
     * Affiche une erreur sur la page de login
     */
    private static function afficherErreur($msg)
    {
        $error = $msg;
        require_once("vue/connexion/login.php");
    }
}
?>