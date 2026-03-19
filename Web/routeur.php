<?php
/**
 * Routeur principal - Toutes les requêtes passent par ici
 */
require_once("config/connexion.php");
require_once("config/session.php");
Connexion::connect();

$controleursPossibles = [
    "ControleurConnexion",
    "ControleurGroupe",
    "ControleurAPI",
    "ControleurGestion",
    "ControleurEtudiant",
    "ControleurStatistiques",
    "ControleurEnseignant",
    "ControleurCovoiturage",
    "ControleurSondage",
    "ControleurCSV"
];

// Contrôleur par défaut = Connexion
$controleur = "ControleurConnexion";

if (isset($_GET["controleur"]) && in_array($_GET["controleur"], $controleursPossibles)) {
    $controleur = $_GET["controleur"];
}

require_once("controleur/" . $controleur . ".php");

$methodes = get_class_methods($controleur);

// Définir l'action par défaut selon le contrôleur
if ($controleur === "ControleurConnexion") {
    $action = "afficherLogin";
} else {
    // Pour les autres contrôleurs, si pas d'action valide, rediriger vers connexion
    $action = null;
}

if (isset($_GET["action"]) && in_array($_GET["action"], $methodes)) {
    $action = $_GET["action"];
}

// Si pas d'action valide pour un contrôleur autre que Connexion, rediriger
if ($action === null) {
    header("Location: routeur.php");
    exit;
}

$controleur::$action();
?>