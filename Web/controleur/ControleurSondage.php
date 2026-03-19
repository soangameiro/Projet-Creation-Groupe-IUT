<?php
/**
 * Contrôleur Sondage - Création et gestion des sondages
 */
require_once("modele/Sondage.php");
require_once("modele/Etudiant.php");
require_once("config/session.php");

class ControleurSondage
{

    // ========== AFFICHAGE ==========

    /**
     * Affiche la liste des sondages (pour les responsables)
     */
    public static function liste()
    {
        Session::requireResponsable();

        $sondages = Sondage::getAll();
        require_once("vue/responsable/liste_sondages.php");
    }

    /**
     * Affiche le formulaire de création de sondage
     */
    public static function formulaireCreer()
    {
        Session::requireResponsable();
        require_once("vue/responsable/creer_sondage.php");
    }

    /**
     * Affiche les résultats d'un sondage
     */
    public static function resultats()
    {
        Session::requireResponsable();

        $id_sond = intval($_GET['id'] ?? 0);
        $sondage = Sondage::getById($id_sond);

        if (!$sondage) {
            header("Location: routeur.php?controleur=ControleurSondage&action=liste");
            exit;
        }

        $reponses = Sondage::getAllReponses($id_sond);
        $stats = Sondage::getStatistiquesReponses($id_sond);

        require_once("vue/responsable/resultats_sondage.php");
    }

    // ========== ACTIONS ==========

    /**
     * Crée un nouveau sondage
     */
    public static function creer()
    {
        Session::requireResponsable();

        $question = trim($_POST['question'] ?? '');
        $type = $_POST['type'] ?? 'unique';
        $options = $_POST['options'] ?? [];

        if (empty($question)) {
            $error = "La question est obligatoire.";
            require_once("vue/responsable/creer_sondage.php");
            return;
        }

        $id_ens = Session::getUserId();
        Sondage::create($question, $id_ens, $type, $options);

        header("Location: routeur.php?controleur=ControleurSondage&action=liste&success=1");
        exit;
    }

    /**
     * Supprime un sondage
     */
    public static function supprimer()
    {
        Session::requireResponsable();

        $id_sond = intval($_GET['id'] ?? 0);
        if ($id_sond > 0) {
            Sondage::delete($id_sond);
        }

        header("Location: routeur.php?controleur=ControleurSondage&action=liste");
        exit;
    }

    // ========== RÉPONSES ÉTUDIANTS ==========

    /**
     * Affiche les sondages pour un étudiant
     */
    public static function afficherPourEtudiant()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu = Session::getUserId();
        $sondages = Sondage::getAll();
        $mesReponses = Sondage::getReponsesByEtudiant($id_etu);

        require_once("vue/etudiant/sondages.php");
    }

    /**
     * Enregistre la réponse d'un étudiant
     */
    public static function repondre()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu = Session::getUserId();
        $id_sond = intval($_POST['id_sond'] ?? 0);
        $reponse = trim($_POST['reponse'] ?? '');

        if ($id_sond > 0 && !empty($reponse)) {
            Sondage::repondre($id_sond, $id_etu, $reponse);
        }

        header("Location: routeur.php?controleur=ControleurSondage&action=afficherPourEtudiant");
        exit;
    }
}
?>