<?php
require_once("modele/Etudiant.php");
require_once("modele/Enseignant.php");

class ControleurGestion
{

    // Liste des étudiants - affiche la liste complète
    public static function listeEtudiants()
    {
        require_once("modele/Etudiant.php");
        require_once("config/session.php");
        Session::requireLogin();
        
        $etudiants = Etudiant::getAllEtudiants();
        $userInfo = Session::estEtudiant() 
            ? Etudiant::getById(Session::getUserId()) 
            : Enseignant::getById(Session::getUserId());
        
        require_once("vue/gestion/liste_etudiants.php");
    }

    public static function afficherFormulaireAjout()
    {
        $pageTitle = "Ajouter un étudiant complet";
        $actionForm = "traiterAjout";
        $etudiant = new Etudiant();
        $error = isset($_GET['error']) ? $_GET['error'] : null;
        $success = isset($_GET['success']) ? $_GET['success'] : null;
        require_once("vue/gestion/form_etudiant.php");
    }

    public static function traiterAjout()
    {
        // Validation des champs obligatoires
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
        if (empty($_POST['genre'])) {
            $errors[] = "Le genre est obligatoire";
        }

        // Si erreurs, rediriger
        if (!empty($errors)) {
            $errorMsg = implode(", ", $errors);
            header("Location: routeur.php?controleur=ControleurGestion&action=afficherFormulaireAjout&error=" . urlencode($errorMsg));
            exit();
        }

        try {
            // Gestion sécurisée des champs optionnels
            $bac = isset($_POST['bac']) && !empty(trim($_POST['bac'])) ? trim($_POST['bac']) : null;
            $moyenne = isset($_POST['moyenne']) && !empty($_POST['moyenne']) && is_numeric($_POST['moyenne']) ? floatval($_POST['moyenne']) : null;
            $adresse = isset($_POST['adresse']) && !empty(trim($_POST['adresse'])) ? trim($_POST['adresse']) : null;
            $tel = isset($_POST['tel']) && !empty(trim($_POST['tel'])) ? trim($_POST['tel']) : null;
            $age = isset($_POST['age']) && !empty($_POST['age']) && is_numeric($_POST['age']) ? intval($_POST['age']) : null;

            Etudiant::createComplet(
                trim($_POST['nom']),
                trim($_POST['prenom']),
                trim($_POST['email']),
                $_POST['genre'],
                $bac,
                $moyenne,
                $adresse,
                $tel,
                $age
            );
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&success=" . urlencode("Étudiant créé avec succès !"));
        } catch (Exception $e) {
            header("Location: routeur.php?controleur=ControleurGestion&action=afficherFormulaireAjout&error=" . urlencode("Erreur : " . $e->getMessage()));
        }
        exit();
    }

    public static function afficherFormulaireModif()
    {
        $id = $_GET['id'];
        $etudiant = Etudiant::getById($id);
        $pageTitle = "Modifier toutes les infos";
        $actionForm = "traiterModif";
        $error = isset($_GET['error']) ? $_GET['error'] : null;
        $success = isset($_GET['success']) ? $_GET['success'] : null;
        require_once("vue/gestion/form_etudiant.php");
    }

    public static function traiterModif()
    {
        // Validation
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

        if (!empty($errors)) {
            $errorMsg = implode(", ", $errors);
            header("Location: routeur.php?controleur=ControleurGestion&action=afficherFormulaireModif&id=" . $_POST['id'] . "&error=" . urlencode($errorMsg));
            exit();
        }

        try {
            // Gestion sécurisée des champs optionnels
            $bac = isset($_POST['bac']) && !empty(trim($_POST['bac'])) ? trim($_POST['bac']) : null;
            $moyenne = isset($_POST['moyenne']) && !empty($_POST['moyenne']) && is_numeric($_POST['moyenne']) ? floatval($_POST['moyenne']) : null;
            $adresse = isset($_POST['adresse']) && !empty(trim($_POST['adresse'])) ? trim($_POST['adresse']) : null;
            $tel = isset($_POST['tel']) && !empty(trim($_POST['tel'])) ? trim($_POST['tel']) : null;
            $age = isset($_POST['age']) && !empty($_POST['age']) && is_numeric($_POST['age']) ? intval($_POST['age']) : null;

            Etudiant::updateComplet(
                $_POST['id'],
                trim($_POST['nom']),
                trim($_POST['prenom']),
                trim($_POST['email']),
                $_POST['genre'],
                $bac,
                $moyenne,
                $adresse,
                $tel,
                $age
            );
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&success=" . urlencode("Étudiant modifié avec succès !"));
        } catch (PDOException $e) {
            // Traduire les erreurs SQL en messages compréhensibles
            $errorMsg = "Erreur lors de la modification";

            if (strpos($e->getMessage(), 'Duplicate entry') !== false) {
                $errorMsg = "Cet email est déjà utilisé par un autre étudiant";
            } elseif (strpos($e->getMessage(), 'cannot be null') !== false) {
                $errorMsg = "Un champ obligatoire est manquant";
            } elseif (strpos($e->getMessage(), 'Data too long') !== false) {
                $errorMsg = "Une des valeurs saisies est trop longue";
            }

            header("Location: routeur.php?controleur=ControleurGestion&action=afficherFormulaireModif&id=" . $_POST['id'] . "&error=" . urlencode($errorMsg));
        } catch (Exception $e) {
            header("Location: routeur.php?controleur=ControleurGestion&action=afficherFormulaireModif&id=" . $_POST['id'] . "&error=" . urlencode("Erreur inattendue : veuillez réessayer"));
        }
        exit();
    }

    // Suppression avec gestion d'erreur
    public static function traiterSuppression()
    {
        if (!isset($_GET['id']) || empty($_GET['id'])) {
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&error=" . urlencode("ID manquant pour la suppression"));
            exit();
        }

        $id = $_GET['id'];
        try {
            Etudiant::delete($id);
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&success=" . urlencode("Étudiant supprimé avec succès !"));
        } catch (Exception $e) {
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&error=" . urlencode("Erreur lors de la suppression : " . $e->getMessage()));
        }
        exit();
    }
}
?>