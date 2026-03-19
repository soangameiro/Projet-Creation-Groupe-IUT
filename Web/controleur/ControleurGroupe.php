<?php
/**
 * Contrôleur Groupe - Création et gestion des groupes TD/TP
 */
require_once("modele/Groupe.php");
require_once("modele/Etudiant.php");
require_once("modele/Promotion.php");
require_once("config/session.php");

class ControleurGroupe
{

    // ========== AFFICHAGE ==========

    /**
     * Affiche la liste des groupes d'une promotion
     */
    public static function afficher()
    {
        Session::requireLogin(); // Tous les enseignants peuvent consulter

        $id_promo = intval($_GET['promo'] ?? 1);
        $promotions = Promotion::getAll();
        $promo = Promotion::getById($id_promo);
        $groupes = Groupe::getByPromo($id_promo);

        // Statistiques par groupe
        $statsGroupes = [];
        foreach ($groupes as $g) {
            $statsGroupes[$g->getId()] = Groupe::getStatistiques($g->getId());
        }

        $etudiantsSansGroupe = Promotion::getEtudiantsSansGroupe($id_promo);

        require_once("vue/groupe/liste_groupes.php");
    }

    /**
     * Affiche l'outil de création de groupes
     */
    public static function afficherOutil()
    {
        Session::requireResponsable();

        $id_promo = intval($_GET['promo'] ?? 1);
        $promotions = Promotion::getAll();
        $promo = Promotion::getById($id_promo);
        $groupes = Groupe::getByPromo($id_promo);
        $etudiants = Promotion::getEtudiants($id_promo);
        $etudiantsSansGroupe = Promotion::getEtudiantsSansGroupe($id_promo);

        // Statistiques par groupe
        $statsGroupes = [];
        foreach ($groupes as $g) {
            $statsGroupes[$g->getId()] = Groupe::getStatistiques($g->getId());
        }

        require_once("vue/groupe/creation_groupes.php");
    }

    /**
     * Affiche la page d'algorithmes avancés
     */
    public static function afficherAlgorithmes()
    {
        Session::requireResponsable();

        $id_promo = intval($_GET['promo'] ?? 1);
        $promotions = Promotion::getAll();

        require_once("vue/groupe/algorithmes_groupes.php");
    }

    /**
     * Affiche les détails d'un groupe
     */
    public static function details()
    {
        Session::requireLogin();

        $id_groupe = intval($_GET['id'] ?? 0);
        $groupe = Groupe::getById($id_groupe);

        if (!$groupe) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $etudiants = Groupe::getEtudiants($id_groupe);
        $stats = Groupe::getStatistiques($id_groupe);

        require_once("vue/groupe/details_groupe.php");
    }

    // ========== CRÉATION ==========

    /**
     * Crée un nouveau groupe
     */
    public static function creer()
    {
        Session::requireResponsable();

        $libelle = trim($_POST['libelle'] ?? '');
        $id_promo = intval($_POST['id_promo'] ?? 0);

        if (!empty($libelle) && $id_promo > 0) {
            Groupe::create($libelle, $id_promo);
            Promotion::updateCompteurs($id_promo);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    /**
     * Crée plusieurs groupes d'un coup
     */
    public static function creerMultiples()
    {
        Session::requireResponsable();

        $nb_groupes = intval($_POST['nb_groupes'] ?? 0);
        $id_promo = intval($_POST['id_promo'] ?? 0);
        $prefixe = trim($_POST['prefixe'] ?? 'Groupe');

        if ($nb_groupes > 0 && $id_promo > 0) {
            Groupe::creerGroupesPourPromo($id_promo, $nb_groupes, $prefixe);
            Promotion::updateCompteurs($id_promo);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    // ========== GESTION ÉTUDIANTS ==========

    /**
     * Ajoute un étudiant à un groupe
     */
    public static function ajouterEtudiant()
    {
        Session::requireResponsable();

        $id_groupe = intval($_POST['id_groupe'] ?? 0);
        $id_etu = intval($_POST['id_etu'] ?? 0);
        $id_promo = intval($_POST['id_promo'] ?? 0);

        if ($id_groupe > 0 && $id_etu > 0) {
            Groupe::ajouterEtudiant($id_groupe, $id_etu);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    /**
     * Retire un étudiant d'un groupe
     */
    public static function retirerEtudiant()
    {
        Session::requireResponsable();

        $id_etu = intval($_GET['id'] ?? 0);
        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_etu > 0) {
            Groupe::retirerEtudiant($id_etu);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    /**
     * Déplace un étudiant vers un autre groupe
     */
    public static function deplacerEtudiant()
    {
        Session::requireResponsable();

        $id_etu = intval($_POST['id_etu'] ?? 0);
        $id_groupe = intval($_POST['id_groupe'] ?? 0);
        $id_promo = intval($_POST['id_promo'] ?? 0);

        if ($id_etu > 0 && $id_groupe > 0) {
            Groupe::ajouterEtudiant($id_groupe, $id_etu);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    // ========== RÉPARTITION AUTOMATIQUE ==========

    /**
     * Répartit automatiquement les étudiants dans les groupes
     */
    public static function repartirAuto()
    {
        Session::requireResponsable();

        $id_promo = intval($_POST['id_promo'] ?? 0);
        $methode = $_POST['methode'] ?? 'equilibre'; // equilibre, aleatoire, moyenne

        if ($id_promo <= 0) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $groupes = Groupe::getByPromo($id_promo);
        $etudiants = Promotion::getEtudiantsSansGroupe($id_promo);

        if (empty($groupes) || empty($etudiants)) {
            header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
            exit;
        }

        // Convertir en tableau d'IDs de groupes
        $ids_groupes = [];
        foreach ($groupes as $g) {
            $ids_groupes[] = $g->getId();
        }

        switch ($methode) {
            case 'aleatoire':
                self::repartirAleatoire($etudiants, $ids_groupes);
                break;
            case 'moyenne':
                self::repartirParMoyenne($etudiants, $ids_groupes);
                break;
            case 'equilibre':
            default:
                self::repartirEquilibre($etudiants, $ids_groupes);
                break;
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo . "&success=repartition");
        exit;
    }

    /**
     * Exécute un algorithme et retourne les résultats en JSON
     */
    public static function executerAlgorithme()
    {
        Session::requireResponsable();
        header('Content-Type: application/json');

        $id_promo = intval($_POST['id_promo'] ?? 0);
        $nb_groupes = intval($_POST['nb_groupes'] ?? 4);
        $methode = $_POST['methode'] ?? 'equilibre';

        $startTime = microtime(true);

        // Récupérer les étudiants
        $etudiants = Promotion::getEtudiants($id_promo);

        if (empty($etudiants)) {
            echo json_encode(['success' => false, 'error' => 'Aucun étudiant trouvé']);
            exit;
        }

        // Créer les groupes temporaires
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = [
                'id' => $i + 1,
                'nom' => 'Groupe ' . ($i + 1),
                'etudiants' => []
            ];
        }

        $algorithmeChoisi = null;

        // Exécuter l'algorithme
        if ($methode === 'optimal') {
            $meilleur = self::determinerAlgorithmeOptimal($etudiants, $nb_groupes);
            $methode = $meilleur['methode'];
            $nomsAlgos = [
                'glouton_moyenne' => 'Glouton Moyenne (Base)',
                'glouton_bac' => 'Glouton Bac (Base)',
                'aleatoire' => 'Aléatoire (Base)',
                'glouton1' => 'Glouton 1 - Utilité (Expert)',
                'glouton2' => 'Glouton 2 - Vagues (Expert)'
            ];
            $algorithmeChoisi = $nomsAlgos[$methode] ?? $methode;
        }

        switch ($methode) {
            case 'glouton_moyenne':
                $groupes = self::repartirGloutonMoyenneArray($etudiants, $nb_groupes);
                break;
            case 'glouton_bac':
                $groupes = self::repartirGloutonBacArray($etudiants, $nb_groupes);
                break;
            case 'aleatoire':
                $groupes = self::repartirAleatoireArray($etudiants, $nb_groupes);
                break;
            case 'glouton1':
                $groupes = self::repartirGlouton1Array($etudiants, $nb_groupes);
                break;
            case 'glouton2':
                $groupes = self::repartirGlouton2Array($etudiants, $nb_groupes);
                break;
            default:
                $groupes = self::repartirGloutonMoyenneArray($etudiants, $nb_groupes);
        }

        // Calculer le score
        $score = self::calculerScore($groupes, $methode);

        $endTime = microtime(true);
        $temps = round(($endTime - $startTime) * 1000);

        echo json_encode([
            'success' => true,
            'groupes' => $groupes,
            'score' => $score,
            'temps' => $temps,
            'algorithmeChoisi' => $algorithmeChoisi
        ]);
        exit;
    }

    /**
     * Sauvegarde les résultats d'un algorithme en BDD
     */
    public static function sauvegarderResultats()
    {
        Session::requireResponsable();
        header('Content-Type: application/json');

        $id_promo = intval($_POST['id_promo'] ?? 0);
        $groupesJSON = $_POST['groupes'] ?? '[]';
        $groupes = json_decode($groupesJSON, true);

        // D'abord vider tous les groupes existants
        $groupesExistants = Groupe::getByPromo($id_promo);
        foreach ($groupesExistants as $g) {
            Groupe::delete($g->getId());
        }

        $nbGroupes = 0;
        $nbAffectations = 0;

        // Créer les nouveaux groupes et affecter les étudiants
        foreach ($groupes as $grp) {
            $idGroupe = Groupe::create($grp['nom'], $id_promo);
            $nbGroupes++;

            foreach ($grp['etudiants'] as $etu) {
                Groupe::ajouterEtudiant($idGroupe, $etu['id']);
                $nbAffectations++;
            }
        }

        echo json_encode([
            'success' => true,
            'nbGroupes' => $nbGroupes,
            'nbAffectations' => $nbAffectations
        ]);
        exit;
    }

    /**
     * Répartition aléatoire
     */
    private static function repartirAleatoire($etudiants, $ids_groupes)
    {
        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = $e->getId();
        }
        shuffle($etudiantsArray);

        $nbGroupes = count($ids_groupes);
        $i = 0;
        foreach ($etudiantsArray as $id_etu) {
            Groupe::ajouterEtudiant($ids_groupes[$i % $nbGroupes], $id_etu);
            $i++;
        }
    }

    /**
     * Répartition équilibrée (Round-robin avec tri par moyenne)
     */
    private static function repartirEquilibre($etudiants, $ids_groupes)
    {
        // Trier par moyenne décroissante
        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = ['id' => $e->getId(), 'moy' => $e->getMoyenne() ?? 0];
        }
        usort($etudiantsArray, function ($a, $b) {
            return $b['moy'] <=> $a['moy'];
        });

        // Round-robin
        $nbGroupes = count($ids_groupes);
        $i = 0;
        foreach ($etudiantsArray as $etu) {
            Groupe::ajouterEtudiant($ids_groupes[$i % $nbGroupes], $etu['id']);
            $i++;
        }
    }

    /**
     * Répartition par moyenne (groupe 1 = meilleures moyennes)
     */
    private static function repartirParMoyenne($etudiants, $ids_groupes)
    {
        // Trier par moyenne décroissante
        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = ['id' => $e->getId(), 'moy' => $e->getMoyenne() ?? 0];
        }
        usort($etudiantsArray, function ($a, $b) {
            return $b['moy'] <=> $a['moy'];
        });

        // Répartir en tranches
        $nbGroupes = count($ids_groupes);
        $tailleGroupe = ceil(count($etudiantsArray) / $nbGroupes);

        $groupeIndex = 0;
        $count = 0;
        foreach ($etudiantsArray as $etu) {
            if ($count >= $tailleGroupe && $groupeIndex < $nbGroupes - 1) {
                $groupeIndex++;
                $count = 0;
            }
            Groupe::ajouterEtudiant($ids_groupes[$groupeIndex], $etu['id']);
            $count++;
        }
    }

    // ========== SUPPRESSION ==========

    /**
     * Vide un groupe
     */
    public static function vider()
    {
        Session::requireResponsable();

        $id_groupe = intval($_GET['id'] ?? 0);
        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_groupe > 0) {
            Groupe::viderGroupe($id_groupe);
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    /**
     * Supprime un groupe
     */
    public static function supprimer()
    {
        Session::requireResponsable();

        $id_groupe = intval($_GET['id'] ?? 0);
        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_groupe > 0) {
            Groupe::delete($id_groupe);
            if ($id_promo > 0) {
                Promotion::updateCompteurs($id_promo);
            }
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    /**
     * Réinitialise tous les groupes (supprime tous les groupes)
     */
    public static function reinitialiser()
    {
        Session::requireResponsable();

        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_promo > 0) {
            $groupes = Groupe::getByPromo($id_promo);
            foreach ($groupes as $g) {
                Groupe::delete($g->getId());
            }
        }

        header("Location: routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=" . $id_promo);
        exit;
    }

    // ========== ALGORITHMES AVANCÉS (POUR API) ==========

    /**
     * Glouton par moyenne - version array
     */
    private static function repartirGloutonMoyenneArray($etudiants, $nb_groupes)
    {
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = ['id' => $i + 1, 'nom' => 'Groupe ' . ($i + 1), 'etudiants' => []];
        }

        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = [
                'id' => $e->getId(),
                'nom' => $e->getNom(),
                'prenom' => $e->getPrenom(),
                'moyenne' => $e->getMoyenne() ?? 10,
                'bac' => $e->getBac() ?? 'General',
                'genre' => $e->getGenre()
            ];
        }

        usort($etudiantsArray, function ($a, $b) {
            return $b['moyenne'] <=> $a['moyenne'];
        });

        $index = 0;
        foreach ($etudiantsArray as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }

        return $groupes;
    }

    /**
     * Glouton par bac - version array
     */
    private static function repartirGloutonBacArray($etudiants, $nb_groupes)
    {
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = ['id' => $i + 1, 'nom' => 'Groupe ' . ($i + 1), 'etudiants' => []];
        }

        $bacG = [];
        $bacT = [];

        foreach ($etudiants as $e) {
            $etu = [
                'id' => $e->getId(),
                'nom' => $e->getNom(),
                'prenom' => $e->getPrenom(),
                'moyenne' => $e->getMoyenne() ?? 10,
                'bac' => $e->getBac() ?? 'General',
                'genre' => $e->getGenre()
            ];
            if ($etu['bac'] === 'General') {
                $bacG[] = $etu;
            } else {
                $bacT[] = $etu;
            }
        }

        $index = 0;
        foreach ($bacG as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }
        foreach ($bacT as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }

        return $groupes;
    }

    /**
     * Aléatoire - version array
     */
    private static function repartirAleatoireArray($etudiants, $nb_groupes)
    {
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = ['id' => $i + 1, 'nom' => 'Groupe ' . ($i + 1), 'etudiants' => []];
        }

        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = [
                'id' => $e->getId(),
                'nom' => $e->getNom(),
                'prenom' => $e->getPrenom(),
                'moyenne' => $e->getMoyenne() ?? 10,
                'bac' => $e->getBac() ?? 'General',
                'genre' => $e->getGenre()
            ];
        }

        shuffle($etudiantsArray);

        $index = 0;
        foreach ($etudiantsArray as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }

        return $groupes;
    }

    /**
     * Glouton 1 - Utilité (basé sur la rareté des profils)
     */
    private static function repartirGlouton1Array($etudiants, $nb_groupes)
    {
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = ['id' => $i + 1, 'nom' => 'Groupe ' . ($i + 1), 'etudiants' => []];
        }

        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = [
                'id' => $e->getId(),
                'nom' => $e->getNom(),
                'prenom' => $e->getPrenom(),
                'moyenne' => $e->getMoyenne() ?? 10,
                'bac' => $e->getBac() ?? 'General',
                'genre' => $e->getGenre()
            ];
        }

        // Note: Simplifié - dans la vraie version on utiliserait les notes Java/BD
        // Ici on trie par moyenne décroissante comme approximation
        usort($etudiantsArray, function ($a, $b) {
            return $b['moyenne'] <=> $a['moyenne'];
        });

        // Distribution équilibrée
        $index = 0;
        foreach ($etudiantsArray as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }

        return $groupes;
    }

    /**
     * Glouton 2 - Par vagues
     */
    private static function repartirGlouton2Array($etudiants, $nb_groupes)
    {
        $groupes = [];
        for ($i = 0; $i < $nb_groupes; $i++) {
            $groupes[] = ['id' => $i + 1, 'nom' => 'Groupe ' . ($i + 1), 'etudiants' => []];
        }

        // Séparer par niveau (top 30%, middle 40%, bottom 30%)
        $etudiantsArray = [];
        foreach ($etudiants as $e) {
            $etudiantsArray[] = [
                'id' => $e->getId(),
                'nom' => $e->getNom(),
                'prenom' => $e->getPrenom(),
                'moyenne' => $e->getMoyenne() ?? 10,
                'bac' => $e->getBac() ?? 'General',
                'genre' => $e->getGenre()
            ];
        }

        usort($etudiantsArray, function ($a, $b) {
            return $b['moyenne'] <=> $a['moyenne'];
        });

        $total = count($etudiantsArray);
        $vague1 = array_slice($etudiantsArray, 0, (int) ($total * 0.3));
        $vague2 = array_slice($etudiantsArray, (int) ($total * 0.3), (int) ($total * 0.4));
        $vague3 = array_slice($etudiantsArray, (int) ($total * 0.7));

        // Distribuer vague par vague
        $index = 0;
        foreach ($vague1 as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }
        foreach ($vague2 as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }
        foreach ($vague3 as $etu) {
            $groupes[$index % $nb_groupes]['etudiants'][] = $etu;
            $index++;
        }

        return $groupes;
    }

    /**
     * Détermine l'algorithme optimal
     */
    private static function determinerAlgorithmeOptimal($etudiants, $nb_groupes)
    {
        // Échantillon de 30%
        $tailleEchantillon = max(20, (int) (count($etudiants) * 0.3));
        $echantillon = array_slice($etudiants, 0, min($tailleEchantillon, count($etudiants)));

        $algorithmes = [
            'glouton_moyenne',
            'glouton_bac',
            'aleatoire',
            'glouton1',
            'glouton2'
        ];

        $meilleurScore = -1;
        $meilleur = 'glouton_moyenne';

        foreach ($algorithmes as $algo) {
            $groupes = null;
            switch ($algo) {
                case 'glouton_moyenne':
                    $groupes = self::repartirGloutonMoyenneArray($echantillon, $nb_groupes);
                    break;
                case 'glouton_bac':
                    $groupes = self::repartirGloutonBacArray($echantillon, $nb_groupes);
                    break;
                case 'aleatoire':
                    $groupes = self::repartirAleatoireArray($echantillon, $nb_groupes);
                    break;
                case 'glouton1':
                    $groupes = self::repartirGlouton1Array($echantillon, $nb_groupes);
                    break;
                case 'glouton2':
                    $groupes = self::repartirGlouton2Array($echantillon, $nb_groupes);
                    break;
            }

            $score = self::calculerScore($groupes, $algo);
            if ($score > $meilleurScore) {
                $meilleurScore = $score;
                $meilleur = $algo;
            }
        }

        return ['methode' => $meilleur, 'score' => $meilleurScore];
    }

    /**
     * Calcule le score d'une répartition
     */
    private static function calculerScore($groupes, $methode)
    {
        $score = 0;

        foreach ($groupes as $grp) {
            $somme = 0;
            foreach ($grp['etudiants'] as $etu) {
                $somme += $etu['moyenne'];
            }
            $moyenne = count($grp['etudiants']) > 0 ? $somme / count($grp['etudiants']) : 0;
            $score += $moyenne;
        }

        return count($groupes) > 0 ? $score / count($groupes) : 0;
    }
}
?>