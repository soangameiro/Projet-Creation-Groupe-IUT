<?php
/**
 * Modèle Enseignant - Gestion des enseignants
 */
class Enseignant
{
    private $id_ens;
    private $mdp_ens;
    private $nom_ens;
    private $prenom_ens;
    private $genre_ens;
    private $courriel_ens;
    private $adresse_ens;
    private $tel_ens;
    private $id_mat;
    private $id_role;

    public function __construct()
    {
    }

    /**
     * Récupère un enseignant par son email
     */
    public static function getByEmail($email)
    {
        $sql = "SELECT * FROM Enseignant WHERE courriel_ens = :email";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['email' => $email]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Enseignant');
        return $req->fetch();
    }

    /**
     * Récupère tous les enseignants
     */
    public static function getAll()
    {
        $sql = "SELECT * FROM Enseignant ORDER BY nom_ens ASC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Enseignant');
        return $req->fetchAll();
    }

    /**
     * Récupère un enseignant par son ID
     */
    public static function getById($id)
    {
        $sql = "SELECT * FROM Enseignant WHERE id_ens = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Enseignant');
        return $req->fetch();
    }

    // Méthodes de détection basées sur id_role
    // 1=Enseignant, 2=Resp. filière, 3=Resp. formation

    public function estRespFiliere()
    {
        return $this->id_role == 2;
    }

    public function estRespPromo()
    {
        return $this->id_role == 3;
    }

    public function estResponsable()
    {
        return $this->id_role == 2 || $this->id_role == 3;
    }

    // Vérifier le mot de passe
    public function verifyPassword($mdp)
    {
        // Comparaison simple pour l'instant
        return $this->mdp_ens === $mdp;
    }

    // Getters
    public function getId()
    {
        return $this->id_ens;
    }
    public function getNom()
    {
        return $this->nom_ens;
    }
    public function getPrenom()
    {
        return $this->prenom_ens;
    }
    public function getGenre()
    {
        return $this->genre_ens;
    }
    public function getEmail()
    {
        return $this->courriel_ens;
    }
    public function getAdresse()
    {
        return $this->adresse_ens;
    }
    public function getTel()
    {
        return $this->tel_ens;
    }
    public function getRole()
    {
        return $this->id_role;
    }
    public function getIdMat()
    {
        return $this->id_mat;
    }

    /**
     * Retourne le libellé du rôle
     */
    public function getRoleLibelle()
    {
        switch ($this->id_role) {
            case 1:
                return "Enseignant";
            case 2:
                return "Responsable de Filière";
            case 3:
                return "Responsable de Formation";
            default:
                return "Inconnu";
        }
    }
}
?>