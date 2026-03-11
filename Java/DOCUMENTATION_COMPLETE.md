# 📚 Documentation Complète - SAE S3 Gestion des Groupes

> **Projet** : Gestion des Groupes TD/TP  
> **Auteurs** : Équipe SAE S3  
> **Version** : 1.0  

---

## 📋 Table des matières

1. [Architecture Globale](#1-architecture-globale)
2. [Partie WEB - API REST PHP](#2-partie-web---api-rest-php)
3. [Partie COO - Application Java Swing](#3-partie-coo---application-java-swing)
4. [Algorithmes de Répartition](#4-algorithmes-de-répartition)
5. [Base de Données](#5-base-de-données)
6. [Flux de Données Complets](#6-flux-de-données-complets)

---

## 1. Architecture Globale

### 1.1 Vue d'ensemble du projet

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           PROJET SAE S3                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌─────────────┐    ┌─────────────┐    ┌─────────────┐                 │
│   │    Web/     │    │    COO/     │    │    Algo/    │                 │
│   │  (PHP/API)  │    │   (Java)    │    │   (Java)    │                 │
│   └──────┬──────┘    └──────┬──────┘    └─────────────┘                 │
│          │                  │                                            │
│          │    HTTP/JSON     │                                            │
│          └────────┬─────────┘                                            │
│                   ▼                                                      │
│          ┌───────────────┐                                               │
│          │  Base MySQL   │                                               │
│          └───────────────┘                                               │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.2 Structure des dossiers

```
s3-sae-3dg3/
├── Web/                    # Application web PHP
│   ├── api/               # API REST (point d'entrée pour Java)
│   │   ├── index.php      # Routeur API REST
│   │   └── Database.php   # Connexion BDD pour API
│   ├── config/            # Configuration (connexion.php)
│   ├── controleur/        # Contrôleurs MVC
│   ├── modele/            # Modèles (Etudiant, Enseignant, etc.)
│   ├── vue/               # Vues HTML/PHP
│   └── routeur.php        # Routeur principal web
│
├── COO/                    # Application Java Swing
│   └── src/
│       ├── Infrastructure/  # Couche technique
│       │   ├── RestService.java      # Client HTTP
│       │   └── DatabaseConnection.java
│       ├── Controleur/     # Logique métier
│       │   └── MainController.java
│       ├── Vue/            # Interfaces graphiques
│       │   ├── LoginFrame.java
│       │   ├── DashboardEtudiant.java
│       │   ├── DashboardEnseignant.java
│       │   ├── DashboardRespFiliere.java
│       │   └── DashboardRespFormation.java
│       └── Utilisateur/    # Modèles utilisateur
│
├── Algo/                   # Algorithmes de répartition
│   ├── GroupeMaelan.java   # Algo équilibre bacs/moyennes
│   ├── RepartitionBacs.java # Algo répartition par bacs
│   └── Etudiant.java       # Classe métier
│
└── bd/                     # Scripts SQL
```

---

## 2. Partie WEB - API REST PHP

### 2.1 Fonctionnement de l'API REST

L'API REST est le **pont de communication** entre l'application Java et la base de données MySQL.

#### Point d'entrée : `Web/api/index.php`

```
┌─────────────────────────────────────────────────────────────────┐
│                        REQUÊTE HTTP                              │
│   GET/POST/PUT/DELETE https://serveur/api/{ressource}/{id}      │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                      index.php (Routeur)                         │
│                                                                  │
│  1. Parse l'URL → extrait ressource et ID                       │
│  2. Switch sur la ressource                                      │
│  3. Appelle la fonction handler appropriée                       │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│               HANDLERS (fonctions spécialisées)                  │
│  • handleLogin()       → authentification                        │
│  • handleEnseignants() → CRUD enseignants                        │
│  • handleEtudiants()   → CRUD étudiants                          │
│  • handlePromotions()  → liste promotions                        │
│  • handleGroupes()     → CRUD groupes                            │
│  • handleSondages()    → gestion sondages                        │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BASE DE DONNÉES MySQL                         │
│                     (via PDO préparé)                            │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Endpoints disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/login` | Authentification |
| GET | `/api/enseignants` | Liste tous les enseignants |
| GET | `/api/enseignants/{id}` | Un enseignant par ID |
| POST | `/api/enseignants` | Créer un enseignant |
| PUT | `/api/enseignants/{id}` | Modifier un enseignant |
| DELETE | `/api/enseignants/{id}` | Supprimer un enseignant |
| GET | `/api/etudiants` | Liste tous les étudiants |
| GET | `/api/etudiants?promo=X` | Étudiants d'une promo |
| POST | `/api/etudiants` | Créer un étudiant |
| PUT | `/api/etudiants/{id}` | Modifier un étudiant |
| DELETE | `/api/etudiants/{id}` | Supprimer un étudiant |
| GET | `/api/promotions` | Liste des promotions |
| GET | `/api/groupes` | Liste des groupes |
| POST | `/api/groupes` | Créer un groupe |
| DELETE | `/api/groupes/{id}` | Supprimer un groupe |

### 2.3 Exemple détaillé : handleLogin()

```php
function handleLogin($method) {
    if ($method === 'POST') {
        $pdo = Connexion::pdo();
        
        // 1. Récupérer le JSON envoyé
        $body = json_decode(file_get_contents('php://input'), true);
        $email = $body['email'] ?? '';
        $password = $body['password'] ?? '';

        // 2. Chercher dans la table Enseignant
        $stmt = $pdo->prepare("SELECT e.*, r.nom_role 
                               FROM Enseignant e 
                               LEFT JOIN Role r ON e.id_role = r.id_role 
                               WHERE courriel_ens = ? AND mdp_ens = ?");
        $stmt->execute([$email, $password]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            // 3. Déterminer le rôle
            $role = 'enseignant';
            if ($user['id_role'] == 3) $role = 'responsable_formation';
            if ($user['id_role'] == 2) $role = 'responsable_filiere';

            // 4. Retourner le JSON de succès
            echo json_encode([
                'success' => true,
                'token' => bin2hex(random_bytes(16)),
                'role' => $role,
                'user' => [
                    'id' => $user['id_ens'],
                    'nom' => $user['nom_ens'],
                    'prenom' => $user['prenom_ens']
                ]
            ]);
            return;
        }

        // 5. Sinon chercher dans Etudiant...
        // 6. Si toujours rien → erreur 401
    }
}
```

### 2.4 Schéma du flux CRUD complet

```
┌──────────┐     POST /api/etudiants      ┌─────────────┐
│  Client  │ ──────────────────────────►  │   index.php │
│  (Java)  │     {"nom":"Dupont",...}     │             │
└──────────┘                              └──────┬──────┘
                                                 │
                                                 ▼
                                    ┌────────────────────────┐
                                    │  handleEtudiants()     │
                                    │                        │
                                    │  1. Calcule nextId     │
                                    │  2. INSERT INTO...     │
                                    │  3. return JSON        │
                                    └────────────┬───────────┘
                                                 │
                                                 ▼
┌──────────┐    {"success":true,"id":72}  ┌─────────────┐
│  Client  │ ◄────────────────────────────│   MySQL     │
│  (Java)  │                              │   Database  │
└──────────┘                              └─────────────┘
```

---

## 3. Partie COO - Application Java Swing

### 3.1 Architecture MVC

```
┌───────────────────────────────────────────────────────────────────┐
│                     APPLICATION JAVA SWING                         │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                          VUE                                 │  │
│  │  LoginFrame | DashboardEtudiant | DashboardEnseignant | ... │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │                                      │
│                             ▼                                      │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                      CONTROLEUR                              │  │
│  │                   MainController.java                        │  │
│  │   • login(email, password)                                   │  │
│  │   • getCurrentUserRole()                                     │  │
│  │   • getEtudiants(), getEnseignants()                        │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │                                      │
│                             ▼                                      │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                   INFRASTRUCTURE                             │  │
│  │                    RestService.java                          │  │
│  │   • get(endpoint) → HTTP GET                                 │  │
│  │   • post(endpoint, json) → HTTP POST                         │  │
│  │   • put(endpoint, json) → HTTP PUT                           │  │
│  │   • delete(endpoint) → HTTP DELETE                           │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │                                      │
└─────────────────────────────┼──────────────────────────────────────┘
                              │ HTTP/HTTPS
                              ▼
                    ┌───────────────────┐
                    │   API REST PHP    │
                    │  (serveur web)    │
                    └───────────────────┘
```

### 3.2 RestService.java - Le client HTTP

C'est le **cœur de la communication** avec l'API.

```java
public class RestService {
    // URL de base de l'API
    private static final String URL_BASE = 
        "https://projets.iut-orsay.fr/saes3-mjahier/api";
    
    // Client HTTP Java 11+
    private final HttpClient clientHttp;
    
    // Token d'authentification (optionnel)
    private String tokenAuth;

    public RestService() {
        this.clientHttp = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    // ===== MÉTHODE GET =====
    public String get(String endpoint) {
        try {
            HttpRequest.Builder constructeur = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint))
                    .GET()
                    .header("Content-Type", "application/json");

            if (tokenAuth != null) {
                constructeur.header("Authorization", "Token " + tokenAuth);
            }

            HttpResponse<String> reponse = clientHttp.send(
                    constructeur.build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            return reponse.body();

        } catch (Exception e) {
            System.err.println("Erreur GET : " + e.getMessage());
            return null;
        }
    }

    // ===== MÉTHODE POST =====
    public String post(String endpoint, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> reponse = clientHttp.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            return reponse.body();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Raccourcis métier
    public String getEtudiants() { return get("/etudiants"); }
    public String getEnseignants() { return get("/enseignants"); }
    public String getGroupes() { return get("/groupes"); }
}
```

### 3.3 Appels asynchrones (Thread séparé)

Pour ne pas bloquer l'interface graphique pendant les appels API :

```java
private void chargerEtudiants() {
    // Thread séparé pour l'appel réseau
    new Thread(() -> {
        String json = api.getEtudiants();  // Appel API (peut être lent)
        
        // Retour dans le thread Swing pour mise à jour UI
        SwingUtilities.invokeLater(() -> {
            modeleTableau.setRowCount(0);
            
            // Parser le JSON avec des regex
            Pattern pattern = Pattern.compile("\\{[^}]+\\}");
            Matcher matcher = pattern.matcher(json);
            
            while (matcher.find()) {
                String obj = matcher.group();
                modeleTableau.addRow(new Object[]{
                    extraireValeur(obj, "id_etu"),
                    extraireValeur(obj, "nom_etu"),
                    extraireValeur(obj, "prenom_etu")
                });
            }
        });
    }).start();
}
```

### 3.4 Parsing JSON sans bibliothèque

```java
private String extraireValeur(String json, String cle) {
    // Regex pour trouver "cle": "valeur" ou "cle": nombre
    Pattern pattern = Pattern.compile(
        "\"" + cle + "\"\\s*:\\s*(\"([^\"]*)\"|([^,}]+))");
    Matcher matcher = pattern.matcher(json);
    
    if (matcher.find()) {
        // group(2) = valeur entre guillemets
        // group(3) = valeur sans guillemets (nombre)
        String val = matcher.group(2) != null 
            ? matcher.group(2) 
            : matcher.group(3);
        return val != null ? val.trim() : "";
    }
    return "";
}
```

---

## 4. Algorithmes de Répartition

### 4.1 Vue d'ensemble

Deux algorithmes principaux pour répartir les étudiants en groupes équilibrés :

```
┌─────────────────────────────────────────────────────────────────┐
│                    CRITÈRES D'ÉQUILIBRE                          │
├─────────────────────────────────────────────────────────────────┤
│  • Équilibre des MOYENNES entre groupes                         │
│  • Équilibre des TYPES DE BAC (Général/Techno)                   │
│  • Contrainte : 10-20% de filles par groupe                      │
│  • Taille : 15-18 étudiants par groupe                          │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 Fonction de Score

Le score mesure la **qualité** d'une répartition (plus bas = meilleur) :

```
Score = EcartMoyenne + EcartBacGénéral + EcartBacTechno

Où :
  • EcartMoyenne = MoyenneGroupeMax - MoyenneGroupeMin
  • EcartBacGénéral = NbGénéralMax - NbGénéralMin
  • EcartBacTechno = NbTechnoMax - NbTechnoMin
```

```java
public static double calculerScore(List<List<Etudiant>> groupes) {
    double maxMoy = 0, minMoy = 20;
    int maxGen = 0, minGen = 999;
    int maxTech = 0, minTech = 999;

    for (List<Etudiant> g : groupes) {
        if (g.isEmpty()) continue;

        double somme = 0;
        int gen = 0, tech = 0;
        
        for (Etudiant e : g) {
            somme += e.moyenne;
            if (e.typeBac.equals("General")) gen++;
            else tech++;
        }

        double moy = somme / g.size();
        
        // Mise à jour min/max
        maxMoy = Math.max(maxMoy, moy);
        minMoy = Math.min(minMoy, moy);
        maxGen = Math.max(maxGen, gen);
        minGen = Math.min(minGen, gen);
        maxTech = Math.max(maxTech, tech);
        minTech = Math.min(minTech, tech);
    }

    return (maxMoy - minMoy) + (maxGen - minGen) + (maxTech - minTech);
}
```

### 4.3 Algorithme Glouton par Moyenne

**Principe** : Pour chaque étudiant (du plus fort au plus faible), l'ajouter au groupe ayant la moyenne la plus basse.

```
ENTRÉE : Liste d'étudiants, nombre de groupes
SORTIE : Groupes équilibrés en moyenne

1. Trier étudiants par moyenne DÉCROISSANTE
2. Créer N groupes vides
3. Pour chaque étudiant E :
   a. Trouver le groupe G avec la moyenne la plus BASSE
   b. Ajouter E à G
4. Retourner les groupes
```

```
Exemple avec 6 étudiants et 2 groupes :

Étudiants triés : [18, 16, 14, 12, 10, 8]

Étape 1: 18 → G1 (moy=18)    G2 (moy=0)
Étape 2: 16 → G1 (moy=18)    G2 (moy=16) ← plus basse
Étape 3: 14 → G1 (moy=18)    G2 (moy=15) ← plus basse
Étape 4: 12 → G1 (moy=15)    G2 (moy=15) ← G1 car égalité
Étape 5: 10 → G1 (moy=...)   G2 (moy=12.3) ← plus basse
Étape 6:  8 → ...

Résultat : Groupes avec moyennes proches !
```

### 4.4 Algorithme Glouton par Type de Bac

**Principe** : Répartir les bacs Généraux équitablement, puis les Technos.

```
1. Séparer : Généraux vs Technos
2. Trier chaque liste par moyenne décroissante
3. Distribution Round-Robin :
   - Généraux : G1, G2, G3, G1, G2, G3, ...
   - Technos  : continue le round-robin
```

```java
public static List<List<Etudiant>> gloutonBac(List<Etudiant> etudiants, int nbGroupes) {
    // Séparer par type de bac
    List<Etudiant> generaux = new ArrayList<>();
    List<Etudiant> technos = new ArrayList<>();
    
    for (Etudiant e : etudiants) {
        if (e.typeBac.equals("General")) generaux.add(e);
        else technos.add(e);
    }
    
    // Trier par moyenne
    generaux.sort((a, b) -> Double.compare(b.moyenne, a.moyenne));
    technos.sort((a, b) -> Double.compare(b.moyenne, a.moyenne));
    
    // Créer groupes vides
    List<List<Etudiant>> groupes = new ArrayList<>();
    for (int i = 0; i < nbGroupes; i++) groupes.add(new ArrayList<>());
    
    // Round-robin
    int index = 0;
    for (Etudiant e : generaux) {
        groupes.get(index % nbGroupes).add(e);
        index++;
    }
    for (Etudiant e : technos) {
        groupes.get(index % nbGroupes).add(e);
        index++;
    }
    
    return groupes;
}
```

### 4.5 Force Brute (petits ensembles uniquement)

**Principe** : Tester TOUTES les combinaisons possibles et garder la meilleure.

```
Complexité : O(nbGroupes^nbEtudiants)
  • 10 étudiants, 3 groupes = 3^10 = 59 049 combinaisons ✓
  • 20 étudiants, 3 groupes = 3^20 = 3.4 milliards ✗
```

```
┌────────────────────────────────────────────────────────────────┐
│                     FORCE BRUTE                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Affectation = [0, 0, 0]  →  Tous dans groupe 0                │
│  Affectation = [0, 0, 1]  →  Etu3 dans groupe 1                │
│  Affectation = [0, 0, 2]  →  Etu3 dans groupe 2                │
│  Affectation = [0, 1, 0]  →  Etu2 dans groupe 1                │
│  ...                                                            │
│  Affectation = [2, 2, 2]  →  Tous dans groupe 2                │
│                                                                 │
│  → Pour chaque affectation, calculer le score                  │
│  → Garder l'affectation avec le score le plus bas              │
└────────────────────────────────────────────────────────────────┘
```

### 4.6 Comparaison des algorithmes

| Algorithme | Complexité | Qualité | Utilisation |
|------------|------------|---------|-------------|
| Glouton Moyenne | O(n × g) | Bonne | Production |
| Glouton Bac | O(n × g) | Bonne | Production |
| Force Brute | O(g^n) | Optimale | Tests (n ≤ 10) |

---

## 5. Base de Données

### 5.1 Schéma relationnel

```
┌─────────────────┐       ┌─────────────────┐
│    Promotion    │       │      Role       │
├─────────────────┤       ├─────────────────┤
│ id_promo (PK)   │       │ id_role (PK)    │
│ lib_promo       │       │ nom_role        │
└────────┬────────┘       └────────┬────────┘
         │                         │
         │ 1:N                     │ 1:N
         ▼                         ▼
┌─────────────────┐       ┌─────────────────┐
│    Etudiant     │       │   Enseignant    │
├─────────────────┤       ├─────────────────┤
│ id_etu (PK)     │       │ id_ens (PK)     │
│ nom_etu         │       │ nom_ens         │
│ prenom_etu      │       │ prenom_ens      │
│ age_etu         │       │ genre_ens       │
│ genre_etu       │       │ courriel_ens    │
│ courriel_etu    │       │ mdp_ens         │
│ mdp_etu         │       │ id_role (FK)    │
│ id_promo (FK)   │       └─────────────────┘
│ id_groupe (FK)  │
└────────┬────────┘
         │
         │ N:1
         ▼
┌─────────────────┐
│     Groupe      │
├─────────────────┤
│ id_groupe (PK)  │
│ lib_grp         │
│ id_promo (FK)   │
└─────────────────┘
```

### 5.2 Rôles utilisateurs

| id_role | nom_role | Droits |
|---------|----------|--------|
| 1 | Enseignant | Lecture seule |
| 2 | Responsable Filière | Gestion groupes étudiants |
| 3 | Responsable Formation | CRUD complet |

---

## 6. Flux de Données Complets

### 6.1 Connexion utilisateur

```
┌──────────────┐                              ┌───────────────┐
│  LoginFrame  │                              │  index.php    │
│    (Java)    │                              │   /api/login  │
└──────┬───────┘                              └───────┬───────┘
       │                                               │
       │  1. POST /api/login                          │
       │     {"email":"...", "password":"..."}        │
       │ ─────────────────────────────────────────►   │
       │                                               │
       │                                    2. SELECT * FROM Enseignant
       │                                       WHERE courriel_ens = ?
       │                                       AND mdp_ens = ?
       │                                               │
       │  3. {"success":true, "role":"enseignant",    │
       │      "user":{"id":5, "nom":"Martin",...}}    │
       │ ◄─────────────────────────────────────────   │
       │                                               │
       │  4. Ouverture du bon Dashboard               │
       │     selon le rôle                            │
       ▼                                               
┌────────────────────────┐
│  DashboardEnseignant   │
│  ou DashboardResp...   │
└────────────────────────┘
```

### 6.2 Chargement et affichage des données

```
┌───────────────────────────────────────────────────────────────┐
│                    DASHBOARD JAVA                              │
└───────────────────────────────────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
   Thread 1           Thread 2           Thread 3
   getEtudiants()     getGroupes()       getPromotions()
        │                  │                  │
        ▼                  ▼                  ▼
   GET /etudiants     GET /groupes      GET /promotions
        │                  │                  │
        └──────────────────┼──────────────────┘
                           ▼
                    ┌─────────────┐
                    │   MySQL DB  │
                    └──────┬──────┘
                           │
                    JSON responses
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
   SwingUtilities.invokeLater(() -> { ... })
                           │
                    Mise à jour UI
                    (JTable, JList, etc.)
```

---

## 📝 Résumé des technologies

| Composant | Technologie | Rôle |
|-----------|-------------|------|
| **API REST** | PHP 8 + PDO | Communication Java ↔ BDD |
| **Client HTTP** | java.net.http (Java 11+) | Appels API depuis Java |
| **Interface** | Java Swing | UI graphique desktop |
| **Base de données** | MySQL | Stockage persistant |
| **Parsing JSON** | Regex (Pattern/Matcher) | Extraction de données |
| **Algorithmes** | Java pur | Répartition des groupes |

---

*Documentation générée le 5 janvier 2026*
