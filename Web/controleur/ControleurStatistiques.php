<?php
require_once 'modele/Etudiant.php';

class ControleurStatistiques
{

    public static function afficher()
    {
        // Récupération des données
        $total = Etudiant::countAll();
        $femmes = Etudiant::countBySexe('F');
        $hommes = Etudiant::countBySexe('H');

        // Calcul simple de pourcentage pour l'affichage
        $pourcentF = ($total > 0) ? round(($femmes / $total) * 100) : 0;
        $pourcentH = ($total > 0) ? round(($hommes / $total) * 100) : 0;

        require_once 'vue/responsable/statistiques.php';
    }
}
?>