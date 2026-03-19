<?php
class Etudiant implements JsonSerializable
{
    // Propriétés correspondant au nouveau schéma SQL
    private $id_etu;
    private $mdp_etu;
    private $nom_etu;
    private $prenom_etu;
    private $age_etu;
    private $tel_etu;
    private $genre_etu;
    private $courriel_etu;
    private $adresse_etu;
    private $moy_etu;
    private $annee_etud;
    private $etat_apprent_etu;
    private $redouble_etu;
    private $id_groupe;
    private $id_covoit;
    private $id_promo;
    private $id_bac;

    public function __construct()
    {
    }

    // Transformation pour JSON (API)
    public function jsonSerialize(): mixed
    {
        return [
            'id' => $this->id_etu,
            'nom' => $this->nom_etu,
            'prenom' => $this->prenom_etu,
            'email' => $this->courriel_etu,
            'genre' => $this->genre_etu,
            'age' => $this->age_etu,
            'tel' => $this->tel_etu,
            'adresse' => $this->adresse_etu,
            'moyenne' => $this->moy_etu,
            'annee' => $this->annee_etud,
            'apprenti' => $this->etat_apprent_etu,
            'redoublant' => $this->redouble_etu,
            'id_groupe' => $this->id_groupe,
            'id_covoit' => $this->id_covoit,
            'id_promo' => $this->id_promo,
            'id_bac' => $this->id_bac
        ];
    }

    // Lecture
    public static function getAllEtudiants()
    {
        $sql = "SELECT * FROM Etudiant ORDER BY nom_etu ASC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetchAll();
    }

    public static function getById($id)
    {
        $sql = "SELECT * FROM Etudiant WHERE id_etu = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        $res = $req->fetch();
        // Sécurité : si l'ID n'existe pas, on renvoie un objet vide pour éviter le crash "bool"
        return $res ? $res : new Etudiant();
    }

    // Ajout Complet avec tous les nouveaux champs (paramètres optionnels pour faciliter l'utilisation)
    public static function createComplet(
        $nom,
        $prenom,
        $email,
        $genre,
        $id_bac = null,
        $moyenne = null,
        $adresse = '',
        $tel = '',
        $age = null,
        $mdp = null,
        $id_promo = null,
        $annee_etud = 1,
        $apprenti = 0,
        $redoublant = 0
    ) {
        // Calcul ID automatique
        $sqlId = "SELECT MAX(id_etu) as max_id FROM Etudiant";
        $resId = Connexion::pdo()->query($sqlId)->fetch();
        $nextId = ($resId['max_id'] ?? 0) + 1;

        // Générer un mot de passe par défaut si non fourni
        if (empty($mdp)) {
            $mdp = 'etudiant' . $nextId; // Mot de passe par défaut
        }

        // Valeur par défaut pour l'âge si non fourni
        if (empty($age) || $age === null) {
            $age = 18; // Âge par défaut
        }

        // Valeur par défaut pour id_promo si non fourni
        if (empty($id_promo) || $id_promo === null) {
            // Récupérer la première promotion disponible
            $sqlPromo = "SELECT id_promo FROM Promotion LIMIT 1";
            $resPromo = Connexion::pdo()->query($sqlPromo)->fetch();
            $id_promo = $resPromo ? $resPromo['id_promo'] : 1; // Par défaut 1 si aucune promo
        }

        // Hash du mot de passe
        $mdp_hash = password_hash($mdp, PASSWORD_DEFAULT);

        $sql = "INSERT INTO Etudiant (id_etu, mdp_etu, nom_etu, prenom_etu, courriel_etu, genre_etu, 
                id_bac, moy_etu, adresse_etu, tel_etu, age_etu, id_promo, annee_etud, 
                etat_apprent_etu, redouble_etu) 
                VALUES (:id, :mdp, :nom, :prenom, :email, :genre, :bac, :moyenne, :adresse, :tel, 
                :age, :promo, :annee, :apprenti, :redoublant)";

        $req = Connexion::pdo()->prepare($sql);
        return $req->execute([
            'id' => $nextId,
            'mdp' => $mdp_hash,
            'nom' => $nom,
            'prenom' => $prenom,
            'email' => $email,
            'genre' => $genre,
            'bac' => $id_bac,
            'moyenne' => $moyenne,
            'adresse' => $adresse,
            'tel' => $tel,
            'age' => $age,
            'promo' => $id_promo,
            'annee' => $annee_etud,
            'apprenti' => $apprenti,
            'redoublant' => $redoublant
        ]);
    }

    // Modification Complète
    public static function updateComplet($id, $nom, $prenom, $email, $genre, $id_bac, $moyenne, $adresse, $tel, $age, $apprenti = null, $redoublant = null)
    {
        $sql = "UPDATE Etudiant SET nom_etu = :n, prenom_etu = :p, courriel_etu = :e, genre_etu = :g, 
                id_bac = :b, moy_etu = :m, adresse_etu = :a, tel_etu = :t, age_etu = :ag";

        $params = [
            'n' => $nom,
            'p' => $prenom,
            'e' => $email,
            'g' => $genre,
            'b' => $id_bac,
            'm' => $moyenne,
            'a' => $adresse,
            't' => $tel,
            'ag' => $age,
            'id' => $id
        ];

        if ($apprenti !== null) {
            $sql .= ", etat_apprent_etu = :apprenti";
            $params['apprenti'] = $apprenti;
        }
        if ($redoublant !== null) {
            $sql .= ", redouble_etu = :redoublant";
            $params['redoublant'] = $redoublant;
        }

        $sql .= " WHERE id_etu = :id";

        $req = Connexion::pdo()->prepare($sql);
        return $req->execute($params);
    }

    // Suppression
    public static function delete($id)
    {
        $sql1 = "DELETE FROM Note WHERE id_etu = :id";
        Connexion::pdo()->prepare($sql1)->execute(['id' => $id]);
        $sql2 = "DELETE FROM Etudiant WHERE id_etu = :id";
        return Connexion::pdo()->prepare($sql2)->execute(['id' => $id]);
    }

    // Login
    public static function getByEmail($email)
    {
        $sql = "SELECT * FROM Etudiant WHERE courriel_etu = :email";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['email' => $email]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Etudiant');
        return $req->fetch();
    }

    // Getters
    public function getId()
    {
        return $this->id_etu;
    }
    public function getNom()
    {
        return $this->nom_etu;
    }
    public function getPrenom()
    {
        return $this->prenom_etu;
    }
    public function getEmail()
    {
        return $this->courriel_etu;
    }
    public function getGenre()
    {
        return $this->genre_etu;
    }
    public function getBac()
    {
        return $this->id_bac;
    }

    // Vérifier le mot de passe
    public function verifyPassword($mdp)
    {
        return $this->mdp_etu === $mdp; // Simple comparaison pour l'instant
    }
    public function getMoyenne()
    {
        return $this->moy_etu;
    }
    public function getAdresse()
    {
        return $this->adresse_etu;
    }
    public function getTel()
    {
        return $this->tel_etu;
    }
    public function getAge()
    {
        return $this->age_etu;
    }

    public function getAnnee()
    {
        return $this->annee_etud;
    }

    public function isApprenti()
    {
        return $this->etat_apprent_etu;
    }

    public function isRedoublant()
    {
        return $this->redouble_etu;
    }

    public function getIdGroupe()
    {
        return $this->id_groupe;
    }

    public function getIdCovoit()
    {
        return $this->id_covoit;
    }

    public function getIdPromo()
    {
        return $this->id_promo;
    }

    public function getIdBac()
    {
        return $this->id_bac;
    }

    /**
     * Retourne le libellé du bac depuis la table TypeBac
     */
    public function getBacLibelle()
    {
        if (empty($this->id_bac)) {
            return "Non renseigné";
        }
        
        try {
            $sql = "SELECT lib_bac FROM TypeBac WHERE id_bac = :id";
            $req = Connexion::pdo()->prepare($sql);
            $req->execute(['id' => $this->id_bac]);
            $res = $req->fetch();
            
            if ($res && isset($res['lib_bac'])) {
                return $res['lib_bac'];
            }
            return "Bac " . $this->id_bac;
        } catch (Exception $e) {
            // Fallback si la table n'existe pas
            switch ($this->id_bac) {
                case 1: return "Général";
                case 2: return "Technologique";
                default: return "Bac " . $this->id_bac;
            }
        }
    }

    // Dans ModeleEtudiant.php

    public static function countAll()
    {
        $sql = "SELECT COUNT(*) as total FROM Etudiant";
        $req = Connexion::pdo()->query($sql);
        $res = $req->fetch();
        return $res['total'];
    }

    public static function countBySexe($sexe)
    {
        // Supposons que la colonne s'appelle 'sexe' ou 'genre'
        $sql = "SELECT COUNT(*) as total FROM Etudiant WHERE genre_etu = :genre";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['genre' => $sexe]);
        $res = $req->fetch();
        return $res['total'];
    }
}
?>