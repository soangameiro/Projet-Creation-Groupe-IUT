<?php
/**
 * Contrôleur CSV - Import/Export de fichiers CSV
 */
require_once("modele/Etudiant.php");
require_once("modele/Promotion.php");
require_once("modele/Groupe.php");
require_once("config/session.php");

class ControleurCSV
{

    // ========== IMPORT ==========

    /**
     * Affiche le formulaire d'import
     */
    public static function formulaireImport()
    {
        Session::requireResponsable();
        $promotions = Promotion::getAll();
        require_once("vue/responsable/import_csv.php");
    }

    /**
     * Importe un fichier CSV de notes
     * Format attendu: num_etudiant;nom;prenom;note1;note2;...
     */
    public static function importerNotes()
    {
        Session::requireResponsable();

        if (!isset($_FILES['fichier']) || $_FILES['fichier']['error'] != UPLOAD_ERR_OK) {
            $error = "Erreur lors du téléchargement du fichier.";
            $promotions = Promotion::getAll();
            require_once("vue/responsable/import_csv.php");
            return;
        }

        $fichier = $_FILES['fichier']['tmp_name'];
        $handle = fopen($fichier, 'r');

        if (!$handle) {
            $error = "Impossible d'ouvrir le fichier.";
            $promotions = Promotion::getAll();
            require_once("vue/responsable/import_csv.php");
            return;
        }

        // Lire l'en-tête
        $entete = fgetcsv($handle, 0, ';');
        if (!$entete || count($entete) < 4) {
            $error = "Format de fichier invalide. Attendu: num_etudiant;nom;prenom;note1;...";
            fclose($handle);
            $promotions = Promotion::getAll();
            require_once("vue/responsable/import_csv.php");
            return;
        }

        // Types de notes pour chaque colonne (note1 = DS1, note2 = TP1, note3 = DS2, etc.)
        $typesNotes = ['DS1', 'TP1', 'DS2', 'TP2', 'Oral', 'DS3', 'TP3', 'Projet', 'DS4', 'TP4'];

        $lignesImportees = 0;
        $notesCreees = 0;
        $erreurs = [];

        // ID de matière par défaut (101 = première matière trouvée dans votre BDD)
        $id_matiere_defaut = 101;

        while (($ligne = fgetcsv($handle, 0, ';')) !== false) {
            if (count($ligne) < 4)
                continue;

            $id_etu = intval($ligne[0]);
            $nom = $ligne[1];
            $prenom = $ligne[2];

            // Vérifier que l'étudiant existe
            $etudiant = Etudiant::getById($id_etu);
            if (!$etudiant || !$etudiant->getId()) {
                $erreurs[] = "Étudiant $id_etu ($nom $prenom) introuvable.";
                continue;
            }

            // Récupérer les notes à partir de la 4ème colonne
            $notes = array_slice($ligne, 3);
            $somme = 0;
            $count = 0;
            $noteIndex = 0;

            foreach ($notes as $noteValeur) {
                $val = floatval(str_replace(',', '.', $noteValeur));

                if ($val >= 0 && $val <= 20) {
                    // Créer la note dans la base de données
                    $typeNote = $typesNotes[$noteIndex] ?? 'Note' . ($noteIndex + 1);

                    try {
                        // Récupérer le prochain ID
                        $sqlMaxId = "SELECT COALESCE(MAX(id_note), 0) + 1 as next_id FROM Note";
                        $reqMax = Connexion::pdo()->query($sqlMaxId);
                        $nextId = $reqMax->fetch()['next_id'];

                        $sqlInsert = "INSERT INTO Note (id_note, type_note, valeur_note, id_etu, id_mat) 
                                      VALUES (:id, :type, :valeur, :id_etu, :id_mat)";
                        $reqInsert = Connexion::pdo()->prepare($sqlInsert);
                        $reqInsert->execute([
                            'id' => $nextId,
                            'type' => $typeNote,
                            'valeur' => $val,
                            'id_etu' => $id_etu,
                            'id_mat' => $id_matiere_defaut
                        ]);
                        $notesCreees++;
                    } catch (Exception $e) {
                        // Capturer l'erreur pour debugging
                        $erreurs[] = "Note étudiant $id_etu ($typeNote): " . $e->getMessage();
                    }

                    $somme += $val;
                    $count++;
                }
                $noteIndex++;
            }

            if ($count > 0) {
                $moyenne = $somme / $count;
                // Mettre à jour la moyenne de l'étudiant
                $sql = "UPDATE Etudiant SET moy_etu = :moy WHERE id_etu = :id";
                $req = Connexion::pdo()->prepare($sql);
                $req->execute(['moy' => round($moyenne, 2), 'id' => $id_etu]);
                $lignesImportees++;
            }
        }

        fclose($handle);

        $success = "Import terminé: $lignesImportees étudiants mis à jour, $notesCreees notes créées.";
        if (!empty($erreurs)) {
            $error = "Erreurs rencontrées: " . implode(" | ", array_slice($erreurs, 0, 5));
            if (count($erreurs) > 5) {
                $error .= " (et " . (count($erreurs) - 5) . " autres...)";
            }
        }

        $promotions = Promotion::getAll();
        require_once("vue/responsable/import_csv.php");
    }

    // ========== EXPORT ==========

    /**
     * Exporte les étudiants d'une promotion en CSV
     */
    public static function exporterPromotion()
    {
        Session::requireResponsable();

        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_promo <= 0) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $promo = Promotion::getById($id_promo);
        $etudiants = Promotion::getEtudiants($id_promo);

        // Headers pour le téléchargement
        $nomFichier = "promotion_" . $id_promo . "_" . date('Y-m-d') . ".csv";
        header('Content-Type: text/csv; charset=utf-8');
        header('Content-Disposition: attachment; filename="' . $nomFichier . '"');

        $output = fopen('php://output', 'w');

        // BOM UTF-8 pour Excel
        fprintf($output, chr(0xEF) . chr(0xBB) . chr(0xBF));

        // En-tête
        fputcsv($output, [
            'ID',
            'Nom',
            'Prénom',
            'Genre',
            'Email',
            'Type Bac',
            'Moyenne',
            'Groupe',
            'Redoublant',
            'Apprenti'
        ], ';');

        // Données
        foreach ($etudiants as $etu) {
            $groupe = $etu->getIdGroupe() ? Groupe::getById($etu->getIdGroupe()) : null;

            fputcsv($output, [
                $etu->getId(),
                $etu->getNom(),
                $etu->getPrenom(),
                $etu->getGenre(),
                $etu->getEmail(),
                $etu->getBac(),
                $etu->getMoyenne(),
                $groupe ? $groupe->getLibelle() : '',
                $etu->isRedoublant() ? 'Oui' : 'Non',
                $etu->isApprenti() ? 'Oui' : 'Non'
            ], ';');
        }

        fclose($output);
        exit;
    }

    /**
     * Export complet avec données personnelles (responsable filière uniquement)
     */
    public static function exporterComplet()
    {
        Session::requireResponsable();

        $id_promo = intval($_GET['promo'] ?? 0);

        if ($id_promo <= 0) {
            header("Location: routeur.php?controleur=ControleurConnexion&action=dashboard");
            exit;
        }

        $promo = Promotion::getById($id_promo);
        $etudiants = Promotion::getEtudiants($id_promo);

        // Headers pour le téléchargement
        $nomFichier = "export_complet_" . $id_promo . "_" . date('Y-m-d') . ".csv";
        header('Content-Type: text/csv; charset=utf-8');
        header('Content-Disposition: attachment; filename="' . $nomFichier . '"');

        $output = fopen('php://output', 'w');

        // BOM UTF-8 pour Excel
        fprintf($output, chr(0xEF) . chr(0xBB) . chr(0xBF));

        // En-tête complet
        fputcsv($output, [
            'ID',
            'Nom',
            'Prénom',
            'Age',
            'Genre',
            'Email',
            'Téléphone',
            'Adresse',
            'Type Bac',
            'Moyenne',
            'Année',
            'Groupe',
            'Covoiturage',
            'Redoublant',
            'Apprenti'
        ], ';');

        // Données
        foreach ($etudiants as $etu) {
            $groupe = $etu->getIdGroupe() ? Groupe::getById($etu->getIdGroupe()) : null;
            $covoit = $etu->getIdCovoit() ? Covoiturage::getById($etu->getIdCovoit()) : null;

            fputcsv($output, [
                $etu->getId(),
                $etu->getNom(),
                $etu->getPrenom(),
                $etu->getAge(),
                $etu->getGenre(),
                $etu->getEmail(),
                $etu->getTel(),
                $etu->getAdresse(),
                $etu->getBac(),
                $etu->getMoyenne(),
                $etu->getAnnee(),
                $groupe ? $groupe->getLibelle() : '',
                $covoit ? $covoit->getLibelle() : '',
                $etu->isRedoublant() ? 'Oui' : 'Non',
                $etu->isApprenti() ? 'Oui' : 'Non'
            ], ';');
        }

        fclose($output);
        exit;
    }
}
?>