<?php
/**
 * Modèle Promotion - Gestion des promotions
 */
class Promotion
{
    private $id_promo;
    private $nom_promo;
    private $nb_eleves;
    private $nb_grp;

    public function __construct()
    {
    }

    // ========== LECTURE ==========

    /**
     * Récupère toutes les promotions
     */
    public static function getAll()
    {
        $sql = "SELECT * FROM Promotion ORDER BY nom_promo ASC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Promotion');
        return $req->fetchAll();
    }

    /**
     * Récupère une promotion par son ID
     */
    public static function getById($id)
    {
        $sql = "SELECT * FROM Promotion WHERE id_promo = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Promotion');
        return $req->fetch();
    }

    /**
     * Récupère les étudiants d'une promotion
     */
    public static function getEtudiants($id_promo)
    {
        $sql = "SELECT * FROM Etudiant WHERE id_promo = :promo ORDER BY nom_etu ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['promo' => $id_promo]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetchAll();
    }

    /**
     * Récupère les étudiants non affectés à un groupe
     */
    public static function getEtudiantsSansGroupe($id_promo)
    {
        $sql = "SELECT * FROM Etudiant WHERE id_promo = :promo AND id_groupe IS NULL ORDER BY nom_etu ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['promo' => $id_promo]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetchAll();
    }

    /**
     * Compte les étudiants d'une promotion
     */
    public static function countEtudiants($id_promo)
    {
        $sql = "SELECT COUNT(*) as nb FROM Etudiant WHERE id_promo = :promo";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['promo' => $id_promo]);
        $res = $req->fetch();
        return $res['nb'] ?? 0;
    }

    // ========== CRÉATION ==========

    /**
     * Crée une nouvelle promotion
     */
    public static function create($nom, $nb_eleves = 0, $nb_grp = 0)
    {
        $sqlId = "SELECT MAX(id_promo) as max_id FROM Promotion";
        $resId = Connexion::pdo()->query($sqlId)->fetch();
        $nextId = ($resId['max_id'] ?? 0) + 1;

        $sql = "INSERT INTO Promotion (id_promo, nom_promo, nb_eleves, nb_grp) VALUES (:id, :nom, :eleves, :grp)";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['id' => $nextId, 'nom' => $nom, 'eleves' => $nb_eleves, 'grp' => $nb_grp]);
    }

    // ========== MODIFICATION ==========

    /**
     * Met à jour les compteurs d'une promotion
     */
    public static function updateCompteurs($id_promo)
    {
        $nb_eleves = self::countEtudiants($id_promo);

        $sqlGrp = "SELECT COUNT(*) as nb FROM Groupe WHERE id_promo = :promo";
        $reqGrp = Connexion::pdo()->prepare($sqlGrp);
        $reqGrp->execute(['promo' => $id_promo]);
        $nb_grp = $reqGrp->fetch()['nb'] ?? 0;

        $sql = "UPDATE Promotion SET nb_eleves = :eleves, nb_grp = :grp WHERE id_promo = :id";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['eleves' => $nb_eleves, 'grp' => $nb_grp, 'id' => $id_promo]);
    }

    // ========== GETTERS ==========

    public function getId()
    {
        return $this->id_promo;
    }
    public function getNom()
    {
        return $this->nom_promo;
    }
    public function getNbEleves()
    {
        return $this->nb_eleves;
    }
    public function getNbGroupes()
    {
        return $this->nb_grp;
    }
}
?>