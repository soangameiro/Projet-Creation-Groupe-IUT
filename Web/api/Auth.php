<?php
/**
 * Classe Auth - Authentification et Tokens
 */
class Auth
{

    // Génère un token unique
    public static function generateToken()
    {
        return bin2hex(random_bytes(32));
    }

    // Récupère le header Authorization
    public static function getAuthHeader()
    {
        $headers = getallheaders();
        foreach ($headers as $name => $value) {
            if (strtolower($name) === 'authorization') {
                return $value;
            }
        }
        return null;
    }

    // Récupère les credentials (Basic Auth)
    public static function getCredentials()
    {
        $auth = self::getAuthHeader();
        if ($auth === null)
            return null;

        $parts = explode(' ', $auth);
        if ($parts[0] !== 'Basic')
            return null;

        $decoded = base64_decode($parts[1]);
        $credentials = explode(':', $decoded);

        return [
            'email' => $credentials[0],
            'password' => $credentials[1] ?? ''
        ];
    }

    // Récupère le token (Token ou Bearer)
    public static function getToken()
    {
        $auth = self::getAuthHeader();
        if ($auth === null)
            return null;

        $parts = explode(' ', $auth);
        if ($parts[0] !== 'Token' && $parts[0] !== 'Bearer')
            return null;

        return $parts[1] ?? null;
    }

    // Vérifie l'authentification
    public static function authenticate()
    {
        $token = self::getToken();
        if ($token) {
            // Pour la démo, on accepte tout token non vide
            return ['authenticated' => true];
        }

        $credentials = self::getCredentials();
        if ($credentials) {
            $pdo = Database::getConnection();
            $stmt = $pdo->prepare("SELECT * FROM Enseignant WHERE courriel_ens = ? AND mdp_ens = ?");
            $stmt->execute([$credentials['email'], $credentials['password']]);
            return $stmt->fetch(PDO::FETCH_ASSOC);
        }

        return null;
    }

    // Requiert une authentification valide
    public static function requireAuth()
    {
        $user = self::authenticate();
        if (!$user) {
            http_response_code(401);
            echo json_encode(['error' => 'Authentification requise', 'help' => 'Ajouter header Authorization: Token <votre_token>']);
            exit;
        }
        return $user;
    }
}
