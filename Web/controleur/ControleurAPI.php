<?php
require_once("modele/Etudiant.php");
require_once("modele/Promotion.php");
require_once("modele/Enseignant.php");

class ControleurAPI
{
    public static function listeEtudiantsJSON()
    {
        header('Content-Type: application/json');

        try {
            $promo = intval($_GET['promo'] ?? 1);
            $etudiants = Promotion::getEtudiants($promo);

            // Utiliser jsonSerialize des étudiants directement
            echo json_encode($etudiants);
        } catch (Exception $e) {
            echo json_encode(['error' => $e->getMessage()]);
        }
        exit;
    }

    /**
     * Liste pour l'annuaire : tous les étudiants ET enseignants
     */
    public static function listeAnnuaireJSON()
    {
        header('Content-Type: application/json');

        try {
            $result = [];

            // Récupérer TOUS les étudiants
            $etudiants = Etudiant::getAllEtudiants();
            foreach ($etudiants as $e) {
                $result[] = [
                    'id' => $e->getId(),
                    'nom' => $e->getNom(),
                    'prenom' => $e->getPrenom(),
                    'email' => $e->getEmail(),
                    'type' => 'Étudiant',
                    'typeDetail' => $e->getBacLibelle(),
                    'genre' => $e->getGenre(),
                    'moyenne' => $e->getMoyenne()
                ];
            }

            // Récupérer TOUS les enseignants
            $enseignants = Enseignant::getAll();
            foreach ($enseignants as $ens) {
                // Déterminer le type d'enseignant
                $typeDetail = 'Enseignant';
                if (method_exists($ens, 'getTypeResponsable')) {
                    $typeResp = $ens->getTypeResponsable();
                    if ($typeResp === 'Responsable Filière') {
                        $typeDetail = 'Responsable Filière';
                    } elseif ($typeResp === 'Responsable Promotion') {
                        $typeDetail = 'Responsable Promotion';
                    }
                }

                $result[] = [
                    'id' => 'ENS_' . $ens->getId(),
                    'nom' => $ens->getNom(),
                    'prenom' => $ens->getPrenom(),
                    'email' => $ens->getEmail(),
                    'type' => 'Enseignant',
                    'typeDetail' => $typeDetail,
                    'genre' => $ens->getGenre() ?? 'H',
                    'moyenne' => null
                ];
            }

            echo json_encode($result);
        } catch (Exception $e) {
            echo json_encode(['error' => $e->getMessage()]);
        }
        exit;
    }
}
?>