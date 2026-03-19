<?php
/**
 * Gestionnaire de sessions - Démarre et vérifie les sessions utilisateur
 */

// Démarrer la session si pas déjà fait
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

class Session
{

    /**
     * Vérifie si un utilisateur est connecté
     */
    public static function estConnecte()
    {
        return isset($_SESSION['user_id']) && isset($_SESSION['user_type']);
    }

    /**
     * Connecte un étudiant
     */
    public static function connecterEtudiant($etudiant)
    {
        $_SESSION['user_id'] = $etudiant->getId();
        $_SESSION['user_type'] = 'etudiant';
        $_SESSION['user_nom'] = $etudiant->getNom();
        $_SESSION['user_prenom'] = $etudiant->getPrenom();
        $_SESSION['user_email'] = $etudiant->getEmail();
        $_SESSION['user_role'] = 0; // Étudiant = rôle 0
        $_SESSION['id_promo'] = $etudiant->getIdPromo();
    }

    /**
     * Connecte un enseignant
     */
    public static function connecterEnseignant($enseignant)
    {
        $_SESSION['user_id'] = $enseignant->getId();
        $_SESSION['user_type'] = 'enseignant';
        $_SESSION['user_nom'] = $enseignant->getNom();
        $_SESSION['user_prenom'] = $enseignant->getPrenom();
        $_SESSION['user_email'] = $enseignant->getEmail();
        $_SESSION['user_role'] = $enseignant->getRole();
    }

    /**
     * Déconnecte l'utilisateur
     */
    public static function deconnecter()
    {
        session_destroy();
        $_SESSION = [];
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    public static function getUserId()
    {
        return $_SESSION['user_id'] ?? null;
    }

    /**
     * Récupère le type d'utilisateur (etudiant/enseignant)
     */
    public static function getUserType()
    {
        return $_SESSION['user_type'] ?? null;
    }

    /**
     * Récupère le rôle (0=étudiant, 1=enseignant, 2=resp filière, 3=resp formation)
     */
    public static function getUserRole()
    {
        return $_SESSION['user_role'] ?? 0;
    }

    /**
     * Vérifie si l'utilisateur est un étudiant
     */
    public static function estEtudiant()
    {
        return self::getUserType() === 'etudiant';
    }

    /**
     * Vérifie si l'utilisateur est un enseignant (tous types)
     */
    public static function estEnseignant()
    {
        return self::getUserType() === 'enseignant';
    }

    /**
     * Vérifie si l'utilisateur est responsable de filière
     */
    public static function estRespFiliere()
    {
        return self::getUserRole() == 2;
    }

    /**
     * Vérifie si l'utilisateur est responsable de formation/promo (admin)
     */
    public static function estRespFormation()
    {
        return self::getUserRole() == 3;
    }

    /**
     * Vérifie si l'utilisateur a les droits de responsable (filière ou formation)
     */
    public static function estResponsable()
    {
        return self::estRespFiliere() || self::estRespFormation();
    }

    /**
     * Redirige si non connecté
     */
    public static function requireLogin()
    {
        if (!self::estConnecte()) {
            header("Location: routeur.php");
            exit;
        }
    }

    /**
     * Redirige si pas responsable
     */
    public static function requireResponsable()
    {
        self::requireLogin();
        if (!self::estResponsable()) {
            header("Location: routeur.php?error=acces_refuse");
            exit;
        }
    }

    /**
     * Récupère le nom complet de l'utilisateur
     */
    public static function getNomComplet()
    {
        $prenom = $_SESSION['user_prenom'] ?? '';
        $nom = $_SESSION['user_nom'] ?? '';
        return trim("$prenom $nom");
    }

    /**
     * Récupère la promotion de l'étudiant connecté
     */
    public static function getIdPromo()
    {
        return $_SESSION['id_promo'] ?? null;
    }
}
?>