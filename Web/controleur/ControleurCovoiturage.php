<?php
/**
 * Contrôleur Covoiturage - Gestion des groupes de covoiturage pour les étudiants
 */
require_once("modele/Covoiturage.php");
require_once("modele/Etudiant.php");
require_once("modele/Promotion.php");
require_once("config/session.php");

class ControleurCovoiturage
{

    /**
     * Affiche la page de gestion du covoiturage pour un étudiant
     */
    public static function afficher()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $id_etu = Session::getUserId();
        $etudiant = Etudiant::getById($id_etu);
        $monCovoiturage = Covoiturage::getByEtudiant($id_etu);
        $membres = [];

        if ($monCovoiturage) {
            $membres = Covoiturage::getMembres($monCovoiturage->getId());
        }

        // Récupérer les étudiants de la même promo pour les ajouter
        $promo = $etudiant->getIdPromo();
        $etudiantsPromo = Promotion::getEtudiants($promo);

        // Filtrer ceux qui ne sont pas déjà dans le covoiturage
        $etudiantsDisponibles = [];
        foreach ($etudiantsPromo as $e) {
            if ($e->getId() != $id_etu && $e->getIdCovoit() != ($monCovoiturage ? $monCovoiturage->getId() : -1)) {
                $etudiantsDisponibles[] = $e;
            }
        }

        require_once("vue/etudiant/covoiturage.php");
    }

    /**
     * Crée un nouveau groupe de covoiturage avec l'étudiant connecté
     */
    public static function creer()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu = Session::getUserId();
        $etudiant = Etudiant::getById($id_etu);

        // Créer le covoiturage
        $libelle = "Covoit. de " . $etudiant->getPrenom();
        $id_covoit = Covoiturage::create($libelle);

        if ($id_covoit) {
            // Ajouter l'étudiant au covoiturage
            Covoiturage::ajouterMembre($id_covoit, $id_etu);
        }

        header("Location: routeur.php?controleur=ControleurCovoiturage&action=afficher");
        exit;
    }

    /**
     * Ajoute un membre au covoiturage
     */
    public static function ajouterMembre()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu_ajouter = intval($_POST['id_etu'] ?? 0);
        $id_etu = Session::getUserId();

        // Récupérer mon covoiturage
        $monCovoiturage = Covoiturage::getByEtudiant($id_etu);

        if (!$monCovoiturage) {
            // Créer d'abord un covoiturage
            $etudiant = Etudiant::getById($id_etu);
            $libelle = "Covoit. de " . $etudiant->getPrenom();
            $id_covoit = Covoiturage::create($libelle);
            Covoiturage::ajouterMembre($id_covoit, $id_etu);
        } else {
            $id_covoit = $monCovoiturage->getId();
        }

        if ($id_etu_ajouter > 0) {
            Covoiturage::ajouterMembre($id_covoit, $id_etu_ajouter, 4); // Max 4 membres
        }

        header("Location: routeur.php?controleur=ControleurCovoiturage&action=afficher");
        exit;
    }

    /**
     * Retire un membre du covoiturage (ou se retire soi-même)
     */
    public static function retirerMembre()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu_retirer = intval($_GET['id'] ?? 0);
        $id_etu = Session::getUserId();

        // On peut retirer quelqu'un seulement si on est dans le même covoiturage
        $monCovoiturage = Covoiturage::getByEtudiant($id_etu);
        $sonCovoiturage = Covoiturage::getByEtudiant($id_etu_retirer);

        if ($monCovoiturage && $sonCovoiturage && $monCovoiturage->getId() == $sonCovoiturage->getId()) {
            Covoiturage::retirerMembre($id_etu_retirer);

            // Si le covoiturage est vide, le supprimer
            if (Covoiturage::countMembres($monCovoiturage->getId()) == 0) {
                Covoiturage::delete($monCovoiturage->getId());
            }
        }

        header("Location: routeur.php?controleur=ControleurCovoiturage&action=afficher");
        exit;
    }

    /**
     * Quitte le covoiturage
     */
    public static function quitter()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php");
            exit;
        }

        $id_etu = Session::getUserId();
        $monCovoiturage = Covoiturage::getByEtudiant($id_etu);

        if ($monCovoiturage) {
            Covoiturage::retirerMembre($id_etu);

            // Si le covoiturage est vide, le supprimer
            if (Covoiturage::countMembres($monCovoiturage->getId()) == 0) {
                Covoiturage::delete($monCovoiturage->getId());
            }
        }

        header("Location: routeur.php?controleur=ControleurCovoiturage&action=afficher");
        exit;
    }
}
?>