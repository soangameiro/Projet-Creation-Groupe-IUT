<?php
/**
 * Modèle Sondage - Gestion des sondages et réponses
 */
require_once("config/connexion.php");

class Sondage
{
    private $id_sond;
    private $crit_sond;
    private $id_ens;
    private $type_sond;      // 'unique' ou 'ordonne'
    private $options_sond;   // Options séparées par |

    public function __construct()
    {
    }

    // ========== LECTURE ==========

    /**
     * Récupère tous les sondages
     */
    public static function getAll()
    {
        $sql = "SELECT * FROM Sondage ORDER BY id_sond DESC";
        $req = Connexion::pdo()->query($sql);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Sondage');
        return $req->fetchAll();
    }

    /**
     * Récupère un sondage par son ID
     */
    public static function getById($id)
    {
        $sql = "SELECT * FROM Sondage WHERE id_sond = :id";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Sondage');
        return $req->fetch();
    }

    /**
     * Récupère les sondages créés par un enseignant
     */
    public static function getByEnseignant($id_ens)
    {
        $sql = "SELECT * FROM Sondage WHERE id_ens = :id ORDER BY id_sond DESC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['id' => $id_ens]);
        $req->setFetchMode(PDO::FETCH_CLASS, 'Sondage');
        return $req->fetchAll();
    }

    // ========== CRÉATION ==========

    /**
     * Crée un nouveau sondage
     * @param string $question La question du sondage
     * @param int $id_ens ID de l'enseignant créateur
     * @param string $type 'unique' ou 'ordonne'
     * @param array $options Liste des options possibles
     */
    public static function create($question, $id_ens, $type = 'unique', $options = [])
    {
        $sqlId = "SELECT MAX(id_sond) as max_id FROM Sondage";
        $resId = Connexion::pdo()->query($sqlId)->fetch();
        $nextId = ($resId['max_id'] ?? 0) + 1;

        $sql = "INSERT INTO Sondage (id_sond, crit_sond, id_ens) VALUES (:id, :crit, :ens)";
        $req = Connexion::pdo()->prepare($sql);
        return $req->execute([
            'id' => $nextId,
            'crit' => $question,
            'ens' => $id_ens
        ]);
    }

    // ========== RÉPONSES ==========

    /**
     * Enregistre ou met à jour une réponse
     */
    public static function repondre($id_sond, $id_etu, $reponse)
    {
        // Vérifier si l'étudiant a déjà répondu
        $sqlCheck = "SELECT COUNT(*) FROM repondre WHERE id_sond = :s AND id_etu = :e";
        $check = Connexion::pdo()->prepare($sqlCheck);
        $check->execute(['s' => $id_sond, 'e' => $id_etu]);

        if ($check->fetchColumn() > 0) {
            // Mise à jour
            $sql = "UPDATE repondre SET response = :r WHERE id_sond = :s AND id_etu = :e";
        } else {
            // Insertion
            $sql = "INSERT INTO repondre (id_sond, id_etu, response) VALUES (:s, :e, :r)";
        }

        $req = Connexion::pdo()->prepare($sql);
        return $req->execute(['s' => $id_sond, 'e' => $id_etu, 'r' => $reponse]);
    }

    /**
     * Récupère la réponse d'un étudiant à un sondage
     */
    public static function getReponse($id_sond, $id_etu)
    {
        $sql = "SELECT response FROM repondre WHERE id_sond = :s AND id_etu = :e";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['s' => $id_sond, 'e' => $id_etu]);
        $res = $req->fetch();
        return $res ? $res['response'] : null;
    }

    /**
     * Récupère toutes les réponses d'un étudiant
     */
    public static function getReponsesByEtudiant($id_etu)
    {
        $sql = "SELECT id_sond, response FROM repondre WHERE id_etu = :e";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['e' => $id_etu]);

        $reponses = [];
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            $reponses[$row['id_sond']] = $row['response'];
        }
        return $reponses;
    }

    /**
     * Récupère toutes les réponses à un sondage
     */
    public static function getAllReponses($id_sond)
    {
        $sql = "SELECT r.*, e.nom_etu, e.prenom_etu 
                FROM repondre r 
                JOIN Etudiant e ON r.id_etu = e.id_etu 
                WHERE r.id_sond = :s
                ORDER BY e.nom_etu ASC";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['s' => $id_sond]);
        return $req->fetchAll(PDO::FETCH_ASSOC);
    }

    /**
     * Compte les réponses par valeur pour un sondage
     */
    public static function getStatistiquesReponses($id_sond)
    {
        $sql = "SELECT response, COUNT(*) as nb FROM repondre WHERE id_sond = :s GROUP BY response";
        $req = Connexion::pdo()->prepare($sql);
        $req->execute(['s' => $id_sond]);

        $stats = [];
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            $stats[$row['response']] = $row['nb'];
        }
        return $stats;
    }

    // ========== SUPPRESSION ==========

    /**
     * Supprime un sondage et ses réponses
     */
    public static function delete($id)
    {
        // Supprimer les réponses
        $sql1 = "DELETE FROM repondre WHERE id_sond = :id";
        Connexion::pdo()->prepare($sql1)->execute(['id' => $id]);

        // Supprimer le sondage
        $sql2 = "DELETE FROM Sondage WHERE id_sond = :id";
        return Connexion::pdo()->prepare($sql2)->execute(['id' => $id]);
    }

    // ========== GETTERS ==========

    public function getId()
    {
        return $this->id_sond;
    }
    public function getQuestion()
    {
        return $this->crit_sond;
    }
    public function getIdEnseignant()
    {
        return $this->id_ens;
    }
    public function getType()
    {
        return $this->type_sond ?? 'unique';
    }

    public function getOptions()
    {
        if (empty($this->options_sond)) {
            return ['Oui', 'Non', 'Sans avis'];
        }
        return explode('|', $this->options_sond);
    }
}
?>