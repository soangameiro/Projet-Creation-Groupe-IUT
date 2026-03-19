<?php
/**
 * Modèle Covoiturage - Gestion des groupes de covoiturage
 */
class Covoiturage
{
    private $id_covoit;
    private $lib_covoiturage;

    public function __construct()
    {
    }

    // ========== LECTURE ==========

    /**
     * Récupère tous les groupes de covoiturage
     */
    public static function getAll()
    {
        $sql = "SELECT * FROM Covoiturage ORDER BY id_covoit ASC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Covoiturage');
        return $req->fetchAll();
    }

    /**
     * Récupère un covoiturage par son ID
     */
    public static function getById($id)
    {
        $sql = "SELECT * FROM Covoiturage WHERE id_covoit = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Covoiturage');
        return $req->fetch();
    }

    /**
     * Récupère les membres d'un groupe de covoiturage
     */
    public static function getMembres($id_covoit)
    {
        $sql = "SELECT * FROM Etudiant WHERE id_covoit = :id ORDER BY nom_etu ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_covoit]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetchAll();
    }

    /**
     * Compte les membres d'un covoiturage
     */
    public static function countMembres($id_covoit)
    {
        $sql = "SELECT COUNT(*) as nb FROM Etudiant WHERE id_covoit = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_covoit]);
        $res = $req->fetch();
        return $res['nb'] ?? 0;
    }

    /**
     * Récupère le covoiturage d'un étudiant
     */
    public static function getByEtudiant($id_etu)
    {
        $sql = "SELECT c.* FROM Covoiturage c 
                JOIN Etudiant e ON e.id_covoit = c.id_covoit 
                WHERE e.id_etu = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_etu]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Covoiturage');
        return $req->fetch();
    }

    // ========== CRÉATION ==========

    /**
     * Crée un nouveau groupe de covoiturage
     */
    public static function create($libelle = null)
    {
        // Calcul ID automatique
        $sqlId = "SELECT MAX(id_covoit) as max_id FROM Covoiturage";
        $resId = Connexion::pdo()->query($sqlId)->fetch();
        $nextId = ($resId['max_id'] ?? 0) + 1;

        if (empty($libelle)) {
            $libelle = "Covoiturage " . $nextId;
        }

        $sql = "INSERT INTO Covoiturage (id_covoit, lib_covoiturage) VALUES (:id, :lib)";
        $req = Connexion::pdo()->prepare($sql);
        $result = $req->execute(['id' => $nextId, 'lib' => $libelle]);

        return $result ? $nextId : false;
    }

    // ========== GESTION MEMBRES ==========

    /**
     * Ajoute un étudiant au covoiturage
     * Limite par défaut : 4 membres max
     */
    public static function ajouterMembre($id_covoit, $id_etu, $maxMembres = 4)
    {
        // Vérifier si le covoiturage n'est pas plein
        $nbActuel = self::countMembres($id_covoit);
        if ($nbActuel >= $maxMembres) {
            return false; // Covoiturage plein
        }

        // Retirer l'étudiant de son ancien covoiturage s'il en avait un
        self::retirerMembre($id_etu);

        // Ajouter au nouveau covoiturage
        $sql = "UPDATE Etudiant SET id_covoit = :covoit WHERE id_etu = :etu";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['covoit' => $id_covoit, 'etu' => $id_etu]);
    }

    /**
     * Retire un étudiant de son covoiturage
     */
    public static function retirerMembre($id_etu)
    {
        $sql = "UPDATE Etudiant SET id_covoit = NULL WHERE id_etu = :etu";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['etu' => $id_etu]);
    }

    /**
     * Crée un covoiturage et y ajoute des étudiants
     */
    public static function creerAvecMembres($libelle, $ids_etudiants)
    {
        $id_covoit = self::create($libelle);
        if (!$id_covoit)
            return false;

        foreach ($ids_etudiants as $id_etu) {
            self::ajouterMembre($id_covoit, $id_etu);
        }

        return $id_covoit;
    }

    // ========== SUPPRESSION ==========

    /**
     * Supprime un covoiturage (retire d'abord les membres)
     */
    public static function delete($id)
    {
        // Retirer les membres
        $sql1 = "UPDATE Etudiant SET id_covoit = NULL WHERE id_covoit = :id";
        Connexion::pdo()->prepare($sql1)->execute(['id' => $id]);

        // Supprimer le covoiturage
        $sql2 = "DELETE FROM Covoiturage WHERE id_covoit = :id";
        return Connexion::pdo()->prepare($sql2)->execute(['id' => $id]);
    }

    // ========== GETTERS ==========

    public function getId()
    {
        return $this->id_covoit;
    }
    public function getLibelle()
    {
        return $this->lib_covoiturage;
    }
}
?>