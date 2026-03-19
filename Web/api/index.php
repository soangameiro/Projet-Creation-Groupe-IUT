<?php
/**
 * API REST - Gestion des Groupes
 * CRUD complet avec gestion manuelle des IDs
 */

header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    header('Access-Control-Allow-Headers: Authorization, Content-Type');
    header('Access-Control-Allow-Methods: OPTIONS, GET, POST, PUT, DELETE');
    exit;
}

require_once __DIR__ . '/../config/connexion.php';
Connexion::connect();

$uri = $_SERVER['REQUEST_URI'];
$parts = explode('/', trim(parse_url($uri, PHP_URL_PATH), '/'));
$apiIdx = array_search('api', $parts);
$resource = $parts[$apiIdx + 1] ?? '';
$id = $parts[$apiIdx + 2] ?? null;
$method = $_SERVER['REQUEST_METHOD'];

switch ($resource) {
    case 'login':
        handleLogin($method);
        break;
    case 'enseignants':
        handleEnseignants($method, $id);
        break;
    case 'etudiants':
        handleEtudiants($method, $id);
        break;
    case 'promotions':
        handlePromotions($method, $id);
        break;
    case 'groupes':
        handleGroupes($method, $id);
        break;
    case 'sondages':
        handleSondages($method, $id, $parts[$apiIdx + 2] ?? null, $parts[$apiIdx + 3] ?? null);
        break;
    case 'covoiturage':
        handleCovoiturage($method);
        break;
    case 'notes':
        handleNotes($method);
        break;
    default:
        echo json_encode(['api' => 'Gestion Groupes v1.0', 'status' => 'ok']);
}

function handleLogin($method)
{
    if ($method === 'POST') {
        $pdo = Connexion::pdo();
        $body = json_decode(file_get_contents('php://input'), true);
        $email = $body['email'] ?? '';
        $password = $body['password'] ?? '';

        // 1. Chercher dans Enseignant
        $stmt = $pdo->prepare("SELECT e.*, r.nom_role FROM Enseignant e LEFT JOIN Role r ON e.id_role = r.id_role WHERE courriel_ens = ? AND mdp_ens = ?");
        $stmt->execute([$email, $password]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            // Déterminer le rôle selon nom_role ou id_role
            $role = 'enseignant';
            $nomRole = strtolower($user['nom_role'] ?? '');
            if (strpos($nomRole, 'formation') !== false || $user['id_role'] == 3) {
                $role = 'responsable_formation';
            } elseif (strpos($nomRole, 'filiere') !== false || $user['id_role'] == 2) {
                $role = 'responsable_filiere';
            }

            echo json_encode([
                'success' => true,
                'token' => bin2hex(random_bytes(16)),
                'role' => $role,
                'user' => [
                    'id' => $user['id_ens'],
                    'nom' => $user['nom_ens'],
                    'prenom' => $user['prenom_ens'],
                    'email' => $user['courriel_ens']
                ]
            ]);
            return;
        }

        // 2. Chercher dans Etudiant
        $stmt = $pdo->prepare("SELECT * FROM Etudiant WHERE courriel_etu = ? AND mdp_etu = ?");
        $stmt->execute([$email, $password]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            echo json_encode([
                'success' => true,
                'token' => bin2hex(random_bytes(16)),
                'role' => 'etudiant',
                'user' => [
                    'id' => $user['id_etu'],
                    'nom' => $user['nom_etu'],
                    'prenom' => $user['prenom_etu'],
                    'email' => $user['courriel_etu']
                ]
            ]);
            return;
        }

        // Aucun utilisateur trouvé
        http_response_code(401);
        echo json_encode(['error' => 'Email ou mot de passe incorrect']);
    } else {
        echo json_encode(['status' => 'API Login ok']);
    }
}

function handleEnseignants($method, $id)
{
    $pdo = Connexion::pdo();

    switch ($method) {
        case 'GET':
            if ($id) {
                $stmt = $pdo->prepare("SELECT id_ens, nom_ens, prenom_ens, genre_ens, courriel_ens FROM Enseignant WHERE id_ens = ?");
                $stmt->execute([$id]);
                echo json_encode($stmt->fetch(PDO::FETCH_ASSOC) ?: []);
            } else {
                echo json_encode($pdo->query("SELECT id_ens, nom_ens, prenom_ens, genre_ens, courriel_ens FROM Enseignant")->fetchAll(PDO::FETCH_ASSOC));
            }
            break;

        case 'POST':
            $body = json_decode(file_get_contents('php://input'), true);
            // Calculer le prochain ID disponible
            $nextId = $pdo->query("SELECT COALESCE(MAX(id_ens), 100) + 1 FROM Enseignant")->fetchColumn();
            $stmt = $pdo->prepare("INSERT INTO Enseignant (id_ens, nom_ens, prenom_ens, genre_ens, courriel_ens, mdp_ens, adresse_ens, tel_ens, id_role) VALUES (?,?,?,?,?,?,?,?,?)");
            $stmt->execute([
                $nextId,
                $body['nom'] ?? '',
                $body['prenom'] ?? '',
                $body['genre'] ?? 'H',
                $body['email'] ?? '',
                $body['password'] ?? 'default123',
                $body['adresse'] ?? '',
                $body['tel'] ?? '',
                $body['role'] ?? 1
            ]);
            http_response_code(201);
            echo json_encode(['success' => true, 'id' => $nextId]);
            break;

        case 'PUT':
            if (!$id) {
                http_response_code(400);
                echo json_encode(['error' => 'ID requis']);
                return;
            }
            $body = json_decode(file_get_contents('php://input'), true);

            // 1. Récupérer l'existant pour ne pas écraser avec du vide
            $stmt = $pdo->prepare("SELECT * FROM Enseignant WHERE id_ens = ?");
            $stmt->execute([$id]);
            $current = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$current) {
                http_response_code(404);
                echo json_encode(['error' => 'Enseignant non trouvé']);
                return;
            }

            // 2. Fusionner les données (priorité au body, sinon existant)
            $nom = $body['nom'] ?? $current['nom_ens'];
            $prenom = $body['prenom'] ?? $current['prenom_ens'];
            $genre = $body['genre'] ?? $current['genre_ens'];
            $email = $body['email'] ?? $current['courriel_ens'];
            $id_role = $body['id_role'] ?? $current['id_role'];

            // 3. Update incluant id_role
            $stmt = $pdo->prepare("UPDATE Enseignant SET nom_ens=?, prenom_ens=?, genre_ens=?, courriel_ens=?, id_role=? WHERE id_ens=?");
            try {
                $stmt->execute([$nom, $prenom, $genre, $email, $id_role, $id]);
                echo json_encode(['success' => true, 'updated' => $stmt->rowCount()]);
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(['error' => $e->getMessage()]);
            }
            break;

        case 'DELETE':
            if (!$id) {
                http_response_code(400);
                echo json_encode(['error' => 'ID requis']);
                return;
            }
            $stmt = $pdo->prepare("DELETE FROM Enseignant WHERE id_ens = ?");
            $stmt->execute([$id]);
            echo json_encode(['success' => true, 'deleted' => $stmt->rowCount()]);
            break;
    }
}

function handleEtudiants($method, $id)
{
    $pdo = Connexion::pdo();

    switch ($method) {
        case 'GET':
            $promo = $_GET['promo'] ?? null;
            if ($id) {
                $stmt = $pdo->prepare("SELECT * FROM Etudiant WHERE id_etu = ?");
                $stmt->execute([$id]);
                echo json_encode($stmt->fetch(PDO::FETCH_ASSOC) ?: []);
            } elseif ($promo) {
                $stmt = $pdo->prepare("SELECT * FROM Etudiant WHERE id_promo = ?");
                $stmt->execute([$promo]);
                echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
            } else {
                echo json_encode($pdo->query("SELECT * FROM Etudiant")->fetchAll(PDO::FETCH_ASSOC));
            }
            break;

        case 'POST':
            $body = json_decode(file_get_contents('php://input'), true);
            // Calculer le prochain ID disponible
            $nextId = $pdo->query("SELECT COALESCE(MAX(id_etu), 0) + 1 FROM Etudiant")->fetchColumn();
            $stmt = $pdo->prepare("INSERT INTO Etudiant (id_etu, nom_etu, prenom_etu, age_etu, genre_etu, courriel_etu, mdp_etu, adresse_etu, tel_etu, annee_etud, id_promo) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            $stmt->execute([
                $nextId,
                $body['nom'] ?? '',
                $body['prenom'] ?? '',
                $body['age'] ?? 20,
                $body['genre'] ?? 'H',
                $body['email'] ?? '',
                'etu123',
                $body['adresse'] ?? '',
                $body['tel'] ?? '',
                2024,
                $body['id_promo'] ?? 1
            ]);
            http_response_code(201);
            echo json_encode(['success' => true, 'id' => $nextId]);
            break;

        case 'PUT':
            if (!$id) {
                http_response_code(400);
                echo json_encode(['error' => 'ID requis']);
                return;
            }
            $body = json_decode(file_get_contents('php://input'), true);

            // Si seulement id_groupe est fourni (même si null), c'est une assignation
            if (array_key_exists('id_groupe', $body) && count($body) == 1) {
                $stmt = $pdo->prepare("UPDATE Etudiant SET id_groupe=? WHERE id_etu=?");
                $stmt->execute([$body['id_groupe'], $id]);
                echo json_encode(['success' => true, 'updated' => $stmt->rowCount()]);
            } else {
                // Mise à jour complète ou partielle (similaire à Enseignant)
                $stmt = $pdo->prepare("SELECT * FROM Etudiant WHERE id_etu = ?");
                $stmt->execute([$id]);
                $current = $stmt->fetch(PDO::FETCH_ASSOC);

                if (!$current) {
                    http_response_code(404);
                    echo json_encode(['error' => 'Etudiant non trouve']);
                    return;
                }

                $stmt = $pdo->prepare("UPDATE Etudiant SET nom_etu=?, prenom_etu=?, age_etu=?, genre_etu=?, courriel_etu=?, id_groupe=? WHERE id_etu=?");
                $stmt->execute([
                    $body['nom'] ?? $current['nom_etu'],
                    $body['prenom'] ?? $current['prenom_etu'],
                    $body['age'] ?? $current['age_etu'],
                    $body['genre'] ?? $current['genre_etu'],
                    $body['email'] ?? $current['courriel_etu'],
                    array_key_exists('id_groupe', $body) ? $body['id_groupe'] : $current['id_groupe'],
                    $id
                ]);
                echo json_encode(['success' => true, 'updated' => $stmt->rowCount()]);
            }
            break;

        case 'DELETE':
            if (!$id) {
                http_response_code(400);
                echo json_encode(['error' => 'ID requis']);
                return;
            }
            $stmt = $pdo->prepare("DELETE FROM Etudiant WHERE id_etu = ?");
            try {
                $stmt->execute([$id]);
                echo json_encode(['success' => true, 'deleted' => $stmt->rowCount()]);
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(['error' => $e->getMessage()]);
            }
            break;
    }
}

function handlePromotions($method, $id)
{
    $pdo = Connexion::pdo();
    if ($id) {
        $stmt = $pdo->prepare("SELECT * FROM Promotion WHERE id_promo = ?");
        $stmt->execute([$id]);
        echo json_encode($stmt->fetch(PDO::FETCH_ASSOC) ?: []);
    } else {
        echo json_encode($pdo->query("SELECT * FROM Promotion")->fetchAll(PDO::FETCH_ASSOC));
    }
}

function handleGroupes($method, $id)
{
    $pdo = Connexion::pdo();
    $promo = $_GET['promo'] ?? null;

    if ($method === 'GET') {
        if ($promo) {
            $stmt = $pdo->prepare("SELECT * FROM Groupe WHERE id_promo = ?");
            $stmt->execute([$promo]);
        } else {
            $stmt = $pdo->query("SELECT * FROM Groupe");
        }
        echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
    } elseif ($method === 'POST') {
        $body = json_decode(file_get_contents('php://input'), true);
        $nextId = $pdo->query("SELECT COALESCE(MAX(id_groupe), 0) + 1 FROM Groupe")->fetchColumn();
        $stmt = $pdo->prepare("INSERT INTO Groupe (id_groupe, lib_grp, id_promo) VALUES (?,?,?)");
        $stmt->execute([$nextId, $body['nom'] ?? '', $body['id_promo'] ?? 2024]);
        http_response_code(201);
        echo json_encode(['success' => true, 'id' => $nextId]);
    } elseif ($method === 'DELETE') {
        if (!$id) {
            http_response_code(400);
            echo json_encode(['error' => 'ID requis']);
            return;
        }
        try {
            // D'abord, désassigner tous les étudiants de ce groupe
            $stmtUnassign = $pdo->prepare("UPDATE Etudiant SET id_groupe = NULL WHERE id_groupe = ?");
            $stmtUnassign->execute([$id]);
            $unassigned = $stmtUnassign->rowCount();

            // Ensuite, supprimer le groupe
            $stmt = $pdo->prepare("DELETE FROM Groupe WHERE id_groupe = ?");
            $stmt->execute([$id]);
            echo json_encode(['success' => true, 'deleted' => $stmt->rowCount(), 'etudiants_desassignes' => $unassigned]);
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(['error' => "Erreur suppression: " . $e->getMessage()]);
        }
    }
}

function handleSondages($method, $id, $subResource = null, $subId = null)
{
    $pdo = Connexion::pdo();

    // Gestion des réponses aux sondages (/sondages/reponse)
    if ($id === 'reponse' || $subResource === 'reponse') {
        if ($method === 'POST') {
            $body = json_decode(file_get_contents('php://input'), true);

            $pdo->exec("CREATE TABLE IF NOT EXISTS ReponseSondage (
                id_rep INT PRIMARY KEY,
                id_sond INT,
                id_etu INT,
                reponse TEXT
            )");

            $nextId = $pdo->query("SELECT COALESCE(MAX(id_rep), 0) + 1 FROM ReponseSondage")->fetchColumn();
            $stmt = $pdo->prepare("INSERT INTO ReponseSondage (id_rep, id_sond, id_etu, reponse) VALUES (?,?,?,?)");
            $stmt->execute([
                $nextId,
                $body['id_sond'],
                $body['id_etu'],
                $body['reponse']
            ]);
            http_response_code(201);
            echo json_encode(['success' => true, 'id' => $nextId]);
            return;
        }
    }

    if ($method === 'GET') {
        if ($id) {
            $stmt = $pdo->prepare("SELECT s.*, e.nom_ens, e.prenom_ens FROM Sondage s LEFT JOIN Enseignant e ON s.id_ens = e.id_ens WHERE s.id_sond = ?");
            $stmt->execute([$id]);
            echo json_encode($stmt->fetch(PDO::FETCH_ASSOC) ?: []);
        } else {
            $stmt = $pdo->query("SELECT s.*, e.nom_ens, e.prenom_ens FROM Sondage s LEFT JOIN Enseignant e ON s.id_ens = e.id_ens");
            echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
        }
    } elseif ($method === 'POST') {
        $body = json_decode(file_get_contents('php://input'), true);
        $nextId = $pdo->query("SELECT COALESCE(MAX(id_sond), 0) + 1 FROM Sondage")->fetchColumn();
        $stmt = $pdo->prepare("INSERT INTO Sondage (id_sond, crit_sond, id_ens) VALUES (?,?,?)");
        $stmt->execute([$nextId, $body['question'] ?? '', $body['id_ens'] ?? 1]);
        http_response_code(201);
        echo json_encode(['success' => true, 'id' => $nextId]);
    } elseif ($method === 'DELETE') {
        if (!$id) {
            http_response_code(400);
            echo json_encode(['error' => 'ID requis']);
            return;
        }
        $stmt = $pdo->prepare("DELETE FROM Sondage WHERE id_sond = ?");
        $stmt->execute([$id]);
        echo json_encode(['success' => true, 'deleted' => $stmt->rowCount()]);
    }
}

function handleCovoiturage($method)
{
    $pdo = Connexion::pdo();
    if ($method === 'POST') {
        $body = json_decode(file_get_contents('php://input'), true);

        // Simuler la table si elle n'existe pas (CREATE TABLE IF NOT EXISTS Covoiturage ...)
        $pdo->exec("CREATE TABLE IF NOT EXISTS Covoiturage (
            id_covoit INT PRIMARY KEY, 
            id_demandeur INT, 
            id_souhaite INT
        )");

        $nextId = $pdo->query("SELECT COALESCE(MAX(id_covoit), 0) + 1 FROM Covoiturage")->fetchColumn();
        $stmt = $pdo->prepare("INSERT INTO Covoiturage (id_covoit, id_demandeur, id_souhaite) VALUES (?,?,?)");
        $stmt->execute([$nextId, $body['id_demandeur'], $body['id_souhaite']]);

        echo json_encode(['success' => true, 'id' => $nextId]);
    } elseif ($method === 'GET') {
        $pdo->exec("CREATE TABLE IF NOT EXISTS Covoiturage (
            id_covoit INT PRIMARY KEY, 
            id_demandeur INT, 
            id_souhaite INT
        )");
        $stmt = $pdo->query("SELECT c.*, e1.nom_etu as nom_d, e2.nom_etu as nom_s FROM Covoiturage c 
                             JOIN Etudiant e1 ON c.id_demandeur = e1.id_etu
                             JOIN Etudiant e2 ON c.id_souhaite = e2.id_etu");
        echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
    }
}

function handleNotes($method)
{
    $pdo = Connexion::pdo();
    if ($method === 'POST') {
        $body = json_decode(file_get_contents('php://input'), true);

        $pdo->exec("CREATE TABLE IF NOT EXISTS Note (
            id_note INT PRIMARY KEY, 
            id_etu INT, 
            matiere VARCHAR(50), 
            note DECIMAL(4,2)
        )");

        $nextId = $pdo->query("SELECT COALESCE(MAX(id_note), 0) + 1 FROM Note")->fetchColumn();
        $stmt = $pdo->prepare("INSERT INTO Note (id_note, id_etu, matiere, note) VALUES (?,?,?,?)");
        $stmt->execute([$nextId, $body['id_etu'], $body['matiere'], $body['note']]);

        echo json_encode(['success' => true, 'id' => $nextId]);
    }
}
