<?php
require_once __DIR__ . '/Modele.php';

class ModeleEnseignant extends Modele
{

    // Récupérer tous les enseignants
    public static function getAll()
    {
        $sql = "SELECT * FROM Enseignant ORDER BY nom_ens ASC";
        $req = self::getPdo()->query($sql);
        return $req->fetchAll();
    }

    // Récupérer UN enseignant par son ID (pour la modification)
    public static function getOne($id)
    {
        $sql = "SELECT * FROM Enseignant WHERE id_ens = :id";
        $req = self::getPdo()->prepare($sql);
        $req->execute(['id' => $id]);
        return $req->fetch();
    }

    // Supprimer un enseignant (avec gestion des contraintes FK)
    public static function delete($id)
    {
        try {
            // D'abord, mettre à NULL les références dans les tables liées si nécessaire
            // Par exemple, si l'enseignant est référencé quelque part
            $pdo = self::getPdo();
            
            // Supprimer l'enseignant
            $sql = "DELETE FROM Enseignant WHERE id_ens = :id";
            $req = $pdo->prepare($sql);
            $result = $req->execute(['id' => $id]);
            
            if (!$result) {
                throw new Exception("Erreur lors de la suppression");
            }
            
            return true;
        } catch (PDOException $e) {
            // Si erreur de contrainte FK, essayer de détacher d'abord
            if (strpos($e->getMessage(), 'foreign key constraint') !== false ||
                strpos($e->getMessage(), 'FOREIGN KEY') !== false) {
                throw new Exception("Impossible de supprimer : cet enseignant est référencé ailleurs");
            }
            throw $e;
        }
    }

    // Ajouter un enseignant avec mot de passe
    public static function create($nom, $prenom, $email, $password, $genre = 'H', $adresse = '', $tel = '', $id_mat = null, $id_role = 1)
    {
        // Générer un nouvel ID
        $sqlId = "SELECT MAX(id_ens) as max_id FROM Enseignant";
        $resId = self::getPdo()->query($sqlId)->fetch();
        $nextId = ($resId['max_id'] ?? 0) + 1;

        // Hasher le mot de passe
        $mdp_hash = password_hash($password, PASSWORD_DEFAULT);

        $sql = "INSERT INTO Enseignant (id_ens, mdp_ens, nom_ens, prenom_ens, courriel_ens, genre_ens, adresse_ens, tel_ens, id_mat, id_role) 
                VALUES (:id, :mdp, :nom, :prenom, :email, :genre, :adresse, :tel, :id_mat, :id_role)";
        $req = self::getPdo()->prepare($sql);
        return $req->execute([
            'id' => $nextId,
            'mdp' => $mdp_hash,
            'nom' => $nom,
            'prenom' => $prenom,
            'email' => $email,
            'genre' => $genre,
            'adresse' => $adresse,
            'tel' => $tel,
            'id_mat' => $id_mat,
            'id_role' => $id_role
        ]);
    }

    // Mettre à jour un enseignant (avec mot de passe optionnel)
    public static function update($id, $nom, $prenom, $email, $password = null)
    {
        if ($password && !empty(trim($password))) {
            // Si un nouveau mot de passe est fourni, le hasher et l'inclure
            $mdp_hash = password_hash($password, PASSWORD_DEFAULT);
            $sql = "UPDATE Enseignant SET nom_ens = :nom, prenom_ens = :prenom, courriel_ens = :email, mdp_ens = :mdp WHERE id_ens = :id";
            $req = self::getPdo()->prepare($sql);
            return $req->execute(['nom' => $nom, 'prenom' => $prenom, 'email' => $email, 'mdp' => $mdp_hash, 'id' => $id]);
        } else {
            // Sinon, ne pas toucher au mot de passe
            $sql = "UPDATE Enseignant SET nom_ens = :nom, prenom_ens = :prenom, courriel_ens = :email WHERE id_ens = :id";
            $req = self::getPdo()->prepare($sql);
            return $req->execute(['nom' => $nom, 'prenom' => $prenom, 'email' => $email, 'id' => $id]);
        }
    }
}
?>