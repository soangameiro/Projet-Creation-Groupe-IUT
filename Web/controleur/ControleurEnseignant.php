<?php
require_once 'modele/ModeleEnseignant.php';
require_once 'config/session.php';

class ControleurEnseignant
{

    // 1. Afficher la liste
    public static function liste()
    {
        $enseignants = ModeleEnseignant::getAll();
        require_once 'vue/enseignant/liste.php';
    }

    // 2. Afficher le formulaire (Ajout ou Modif)
    public static function afficherFormulaire()
    {
        // Vérification des permissions : seul Resp Formation peut modifier
        if (!Session::estRespFormation()) {
            header("Location: routeur.php?error=" . urlencode("Accès non autorisé"));
            exit();
        }

        $error = isset($_GET['error']) ? $_GET['error'] : null;
        $success = isset($_GET['success']) ? $_GET['success'] : null;

        if (isset($_GET['id'])) {
            // Si on a un ID, on récupère les infos pour pré-remplir
            $enseignant = ModeleEnseignant::getOne($_GET['id']);
        }
        require_once 'vue/enseignant/form_enseignant.php';
    }

    // 3. Enregistrer en base de données
    public static function enregistrer()
    {
        // Vérification des permissions
        if (!Session::estRespFormation()) {
            header("Location: routeur.php?error=" . urlencode("Accès non autorisé"));
            exit();
        }

        // Validation des données
        $errors = [];

        if (empty($_POST['nom']) || trim($_POST['nom']) === '') {
            $errors[] = "Le nom est obligatoire";
        }

        if (empty($_POST['prenom']) || trim($_POST['prenom']) === '') {
            $errors[] = "Le prénom est obligatoire";
        }

        if (empty($_POST['email']) || trim($_POST['email']) === '') {
            $errors[] = "L'email est obligatoire";
        } elseif (!filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)) {
            $errors[] = "L'email n'est pas valide";
        }

        // Validation du mot de passe (seulement pour la création)
        if (!isset($_POST['id_ens']) || empty($_POST['id_ens'])) {
            if (empty($_POST['password']) || trim($_POST['password']) === '') {
                $errors[] = "Le mot de passe est obligatoire";
            }
        }

        // Si erreurs, rediriger avec message
        if (!empty($errors)) {
            $errorMsg = implode(", ", $errors);
            header("Location: routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire&error=" . urlencode($errorMsg));
            exit();
        }

        // Récupération des données nettoyées
        $nom = trim($_POST['nom']);
        $prenom = trim($_POST['prenom']);
        $email = trim($_POST['email']);
        $password = isset($_POST['password']) ? trim($_POST['password']) : null;

        try {
            if (isset($_POST['id_ens']) && !empty($_POST['id_ens'])) {
                // Modification
                ModeleEnseignant::update($_POST['id_ens'], $nom, $prenom, $email, $password);
                $successMsg = "Enseignant modifié avec succès !";
            } else {
                // Création (mot de passe obligatoire)
                ModeleEnseignant::create($nom, $prenom, $email, $password);
                $successMsg = "Enseignant créé avec succès !";
            }

            // Retour à la liste avec message de succès
            header("Location: routeur.php?controleur=ControleurEnseignant&action=liste&success=" . urlencode($successMsg));
            exit();

        } catch (Exception $e) {
            // En cas d'erreur SQL
            $errorMsg = "Erreur lors de l'enregistrement : " . $e->getMessage();
            header("Location: routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire&error=" . urlencode($errorMsg));
            exit();
        }
    }

    // 4. Supprimer
    public static function supprimer()
    {
        // Vérification des permissions
        if (!Session::estRespFormation()) {
            header("Location: routeur.php?error=" . urlencode("Accès non autorisé"));
            exit();
        }

        if (isset($_GET['id'])) {
            try {
                ModeleEnseignant::delete($_GET['id']);
                $successMsg = "Enseignant supprimé avec succès !";
                header("Location: routeur.php?controleur=ControleurEnseignant&action=liste&success=" . urlencode($successMsg));
            } catch (Exception $e) {
                $errorMsg = "Erreur lors de la suppression";
                header("Location: routeur.php?controleur=ControleurEnseignant&action=liste&error=" . urlencode($errorMsg));
            }
        } else {
            header("Location: routeur.php?controleur=ControleurEnseignant&action=liste");
        }
        exit();
    }
}
?>