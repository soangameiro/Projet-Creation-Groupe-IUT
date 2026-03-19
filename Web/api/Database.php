<?php
/**
 * Classe Database - Connexion PDO MySQL
 */
class Database
{
    private static $hostname = 'localhost';
    private static $database = 'saes3-mjahier';
    private static $login = 'saes3-mjahier';
    private static $password = 'A60qCz74cFjMQDPY';
    private static $pdo = null;

    public static function getConnection()
    {
        if (self::$pdo === null) {
            try {
                self::$pdo = new PDO(
                    "mysql:host=" . self::$hostname . ";dbname=" . self::$database . ";charset=utf8",
                    self::$login,
                    self::$password,
                    [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
                );
            } catch (PDOException $e) {
                http_response_code(500);
                die(json_encode(['error' => 'Erreur de connexion BDD: ' . $e->getMessage()]));
            }
        }
        return self::$pdo;
    }
}
