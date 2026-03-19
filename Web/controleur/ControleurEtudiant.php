<?php
/**
 * Contrôleur Etudiant - Actions spécifiques aux étudiants
 */
require_once("modele/Etudiant.php");
require_once("modele/Sondage.php");
require_once("modele/Groupe.php");
require_once("config/session.php");

class ControleurEtudiant
{

    /**
     * Affiche "Mes Informations" (Lecture seule)
     */
    public static function mesInfos()
    {
        $id = $_GET['id'] ?? Session::getUserId();
        if (!$id) {
            header("Location: routeur.php");
            exit;
        }
        $etudiant = Etudiant::getById($id);

        // Récupérer le groupe si assigné
        $groupe = null;
        if ($etudiant->getIdGroupe()) {
            $groupe = Groupe::getById($etudiant->getIdGroupe());
        }

        require_once("vue/etudiant/mes_infos.php");
    }

    /**
     * Affiche les Sondages (ancienne méthode, redirige vers le nouveau contrôleur)
     */
    public static function lesSondages()
    {
        $id_etu = $_GET['id'] ?? Session::getUserId();
        $lesSondages = Sondage::getAll();
        $reponsesExistantes = Sondage::getReponsesByEtudiant($id_etu);
        require_once("vue/etudiant/les_sondages.php");
    }

    /**
     * Traite la réponse au sondage
     */
    public static function traiterReponse()
    {
        $id_etu = $_POST['id_etu'] ?? Session::getUserId();
        $id_sond = $_POST['id_sond'];
        $reponse = $_POST['reponse'];

        Sondage::repondre($id_sond, $id_etu, $reponse);

        header("Location: routeur.php?controleur=ControleurEtudiant&action=lesSondages&id=$id_etu&succes=1");
        exit;
    }

    /**
     * Affiche l'annuaire public (Lecture seule)
     */
    public static function infosPubliques()
    {
        $etudiants = Etudiant::getAllEtudiants();
        require_once("vue/etudiant/infos_publiques.php");
    }

    /**
     * Affiche le groupe de l'étudiant
     */
    public static function monGroupe()
    {
        Session::requireLogin();

        if (!Session::estEtudiant()) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $id_etu = Session::getUserId();
        $etudiant = Etudiant::getById($id_etu);

        // Variables pour la vue
        $groupe = null;
        $membres = [];
        $promotion = null;
        $stats = [
            'moyenne' => 0,
            'hommes' => 0,
            'femmes' => 0,
            'bacG' => 0,
            'bacT' => 0
        ];

        // Si l'étudiant a un groupe, charger les données
        if ($etudiant->getIdGroupe()) {
            $id_groupe = $etudiant->getIdGroupe();
            $groupe = Groupe::getById($id_groupe);
            $membres = Groupe::getEtudiants($id_groupe);

            // Récupérer la promotion
            require_once("modele/Promotion.php");
            $promotion = Promotion::getById($groupe->getIdPromo());

            // Calculer les stats manuellement
            $somme = 0;
            foreach ($membres as $m) {
                $somme += $m->getMoyenne() ?? 0;
                if ($m->getGenre() === 'H')
                    $stats['hommes']++;
                else
                    $stats['femmes']++;

                $bac = $m->getBacLibelle();
                if (strpos($bac, 'Général') !== false || strpos($bac, 'General') !== false) {
                    $stats['bacG']++;
                } else {
                    $stats['bacT']++;
                }
            }
            $stats['moyenne'] = count($membres) > 0 ? $somme / count($membres) : 0;
        }

        // Toujours afficher mon_groupe.php (il gère le cas "pas de groupe")
        require_once("vue/etudiant/mon_groupe.php");
    }
}
?>