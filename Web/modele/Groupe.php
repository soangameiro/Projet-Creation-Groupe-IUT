<?php
/**
 * Modèle Groupe - Gestion des groupes TD/TP
 */
class Groupe
{
    private $id_groupe;
    private $lib_grp;
    private $id_promo;

    public function __construct()
    {
    }

    // ========== LECTURE ==========

    /**
     * Récupère tous les groupes
     */
    public static function getAll()
    {
        $sql = "SELECT * FROM Groupe ORDER BY 
                CAST(REGEXP_SUBSTR(lib_grp, '[0-9]+') AS UNSIGNED), lib_grp ASC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Groupe');
        return $req->fetchAll();
    }

    /**
     * Récupère les groupes d'une promotion
     */
    public static function getByPromo($id_promo)
    {
        $sql = "SELECT * FROM Groupe WHERE id_promo = :promo ORDER BY 
                CAST(REGEXP_SUBSTR(lib_grp, '[0-9]+') AS UNSIGNED), lib_grp ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['promo' => $id_promo]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Groupe');
        return $req->fetchAll();
    }

    /**
     * Récupère un groupe par son ID
     */
    public static function getById($id)
    {
        $sql = "SELECT * FROM Groupe WHERE id_groupe = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Groupe');
        return $req->fetch();
    }

    /**
     * Récupère les étudiants d'un groupe
     */
    public static function getEtudiants($id_groupe)
    {
        $sql = "SELECT * FROM Etudiant WHERE id_groupe = :id ORDER BY nom_etu ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_groupe]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetchAll();
    }

    /**
     * Compte les étudiants dans un groupe
     */
    public static function countEtudiants($id_groupe)
    {
        $sql = "SELECT COUNT(*) as nb FROM Etudiant WHERE id_groupe = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_groupe]);
        $res = $req->fetch();
        return $res['nb'] ?? 0;
    }

    // ========== CRÉATION ==========

    /**
     * Crée un nouveau groupe
     */
    public static function create($lib_grp, $id_promo)
    {
        try {
            // Calcul ID automatique
            $sqlId = "SELECT MAX(id_groupe) as max_id FROM Groupe";
            $resId = Connexion::pdo()->query($sqlId)->fetch();
            $nextId = ($resId['max_id'] ?? 0) + 1;

            $sql = "INSERT INTO Groupe (id_groupe, lib_grp, id_promo) VALUES (:id, :lib, :promo)";
            $req = Connexion::pdo()->prepare($sql);
            $result = $req->execute(['id' => $nextId, 'lib' => $lib_grp, 'promo' => $id_promo]);

            return $result ? $nextId : false;
        } catch (PDOException $e) {
            // Gestion de l'erreur duplicate entry (code 23000)
            if ($e->getCode() == 23000) {
                // Ajouter un suffix unique pour éviter le doublon
                $lib_grp_unique = $lib_grp . "_" . substr(uniqid(), -4);
                return self::create($lib_grp_unique, $id_promo);
            }
            throw $e;
        }
    }

    /**
     * Crée plusieurs groupes pour une promotion
     */
    public static function creerGroupesPourPromo($id_promo, $nb_groupes, $prefixe = "Groupe")
    {
        $groupes_crees = [];
        for ($i = 1; $i <= $nb_groupes; $i++) {
            $lib = $prefixe . " " . $i;
            $id = self::create($lib, $id_promo);
            if ($id) {
                $groupes_crees[] = $id;
            }
        }
        return $groupes_crees;
    }

    // ========== MODIFICATION ==========

    /**
     * Modifie le libellé d'un groupe
     */
    public static function update($id, $lib_grp)
    {
        $sql = "UPDATE Groupe SET lib_grp = :lib WHERE id_groupe = :id";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['lib' => $lib_grp, 'id' => $id]);
    }

    /**
     * Ajoute un étudiant à un groupe
     */
    public static function ajouterEtudiant($id_groupe, $id_etu)
    {
        $sql = "UPDATE Etudiant SET id_groupe = :groupe WHERE id_etu = :etu";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['groupe' => $id_groupe, 'etu' => $id_etu]);
    }

    /**
     * Retire un étudiant d'un groupe
     */
    public static function retirerEtudiant($id_etu)
    {
        $sql = "UPDATE Etudiant SET id_groupe = NULL WHERE id_etu = :etu";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['etu' => $id_etu]);
    }

    /**
     * Vide un groupe (retire tous les étudiants)
     */
    public static function viderGroupe($id_groupe)
    {
        $sql = "UPDATE Etudiant SET id_groupe = NULL WHERE id_groupe = :groupe";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['groupe' => $id_groupe]);
    }

    // ========== SUPPRESSION ==========

    /**
     * Supprime un groupe (retire d'abord les étudiants)
     */
    public static function delete($id)
    {
        self::viderGroupe($id);
        $sql = "DELETE FROM Groupe WHERE id_groupe = :id";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['id' => $id]);
    }

    // ========== STATISTIQUES ==========

    /**
     * Calcule les statistiques d'un groupe
     */
    public static function getStatistiques($id_groupe)
    {
        $etudiants = self::getEtudiants($id_groupe);

        $stats = [
            'total' => count($etudiants),
            'hommes' => 0,
            'femmes' => 0,
            'bac_general' => 0,
            'bac_techno' => 0,
            'moyenne' => 0,
            'apprentis' => 0,
            'redoublants' => 0
        ];

        $somme_moy = 0;
        $nb_avec_moy = 0;

        foreach ($etudiants as $etu) {
            if ($etu->getGenre() == 'H')
                $stats['hommes']++;
            else if ($etu->getGenre() == 'F')
                $stats['femmes']++;

            if ($etu->getBac() == 1)
                $stats['bac_general']++;
            else if ($etu->getBac() == 2)
                $stats['bac_techno']++;

            if ($etu->getMoyenne() > 0) {
                $somme_moy += $etu->getMoyenne();
                $nb_avec_moy++;
            }

            if ($etu->isApprenti())
                $stats['apprentis']++;
            if ($etu->isRedoublant())
                $stats['redoublants']++;
        }

        $stats['moyenne'] = $nb_avec_moy > 0 ? round($somme_moy / $nb_avec_moy, 2) : 0;

        return $stats;
    }

    // ========== GETTERS ==========

    public function getId()
    {
        return $this->id_groupe;
    }
    public function getLibelle()
    {
        return $this->lib_grp;
    }
    public function getIdPromo()
    {
        return $this->id_promo;
    }
}
?>