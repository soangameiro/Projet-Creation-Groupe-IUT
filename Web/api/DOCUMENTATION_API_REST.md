# Documentation API REST

Documentation des requêtes HTTP et réponses JSON pour l'intégration Java ↔ PHP.

---

## URL de Base

```
http://localhost/saes3-mjahier/routeur.php
```

---

## 1. Authentification (Login)

### Requête

```http
POST /routeur.php?controleur=ControleurConnexion&action=loginAPI
Content-Type: application/json

{
  "email": "kilian.mbacle@univ.fr",
  "password": "Mbacle123!"
}
```

### Réponse - Succès

```json
{
  "success": true,
  "id": 101,
  "nom": "Mbacle",
  "prenom": "Kilian",
  "email": "kilian.mbacle@univ.fr",
  "role": "responsable_formation"
}
```

### Réponse - Échec

```json
{
  "success": false,
  "message": "Identifiants incorrects"
}
```

### Rôles possibles

| Valeur | Description |
|--------|-------------|
| `etudiant` | Étudiant standard |
| `enseignant` | Enseignant classique |
| `responsable_filiere` | Responsable de filière |
| `responsable_formation` | Responsable de formation (promotion) |

---

## 2. Liste des Étudiants d'une Promotion

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listeEtudiantsJSON&promo=1
```

### Paramètres

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `promo` | int | Non (défaut: 1) | ID de la promotion |

### Réponse

```json
[
  {
    "id": 1,
    "nom": "Dupont",
    "prenom": "Thomas",
    "email": "thomas.dupont@etu.univ.fr",
    "genre": "H",
    "bac": 1,
    "bacLibelle": "Bac Général",
    "moyenne": 15.50,
    "age": 20,
    "redoublant": false,
    "apprenti": false,
    "id_groupe": 2,
    "id_promo": 1
  },
  {
    "id": 2,
    "nom": "Martin",
    "prenom": "Léa",
    "email": "lea.martin@etu.univ.fr",
    "genre": "F",
    "bac": 2,
    "bacLibelle": "Bac Technologique",
    "moyenne": 14.25,
    "age": 21,
    "redoublant": false,
    "apprenti": true,
    "id_groupe": 1,
    "id_promo": 1
  }
]
```

---

## 3. Annuaire Complet (Étudiants + Enseignants)

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listeAnnuaireJSON
```

### Réponse

```json
[
  {
    "id": 1,
    "nom": "Dupont",
    "prenom": "Thomas",
    "email": "thomas.dupont@etu.univ.fr",
    "type": "Étudiant",
    "typeDetail": "Bac Général",
    "genre": "H",
    "moyenne": 15.50
  },
  {
    "id": 2,
    "nom": "Martin",
    "prenom": "Léa",
    "email": "lea.martin@etu.univ.fr",
    "type": "Étudiant",
    "typeDetail": "Bac Technologique",
    "genre": "F",
    "moyenne": 14.25
  },
  {
    "id": "ENS_101",
    "nom": "Mbacle",
    "prenom": "Kilian",
    "email": "kilian.mbacle@univ.fr",
    "type": "Enseignant",
    "typeDetail": "Responsable Promotion",
    "genre": "H",
    "moyenne": null
  },
  {
    "id": "ENS_102",
    "nom": "Nunez",
    "prenom": "Darwin",
    "email": "darwin.nunez@univ.fr",
    "type": "Enseignant",
    "typeDetail": "Responsable Filière",
    "genre": "H",
    "moyenne": null
  }
]
```

---

## 4. Liste des Promotions

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listePromotionsJSON
```

### Réponse

```json
[
  {
    "id": 1,
    "nom": "BUT2A",
    "annee": "2025-2026",
    "nb_etudiants": 45
  },
  {
    "id": 2,
    "nom": "BUT2B",
    "annee": "2025-2026",
    "nb_etudiants": 42
  }
]
```

---

## 5. Liste des Groupes d'une Promotion

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listeGroupesJSON&promo=1
```

### Paramètres

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `promo` | int | Oui | ID de la promotion |

### Réponse

```json
[
  {
    "id": 1,
    "libelle": "Groupe A",
    "nb_membres": 12,
    "id_promo": 1,
    "statistiques": {
      "moyenne": 14.85,
      "hommes": 7,
      "femmes": 5,
      "bacGeneral": 8,
      "bacTechno": 4
    }
  },
  {
    "id": 2,
    "libelle": "Groupe B",
    "nb_membres": 11,
    "id_promo": 1,
    "statistiques": {
      "moyenne": 14.92,
      "hommes": 6,
      "femmes": 5,
      "bacGeneral": 7,
      "bacTechno": 4
    }
  }
]
```

---

## 6. Liste des Enseignants

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listeEnseignantsJSON
```

### Réponse

```json
[
  {
    "id": 101,
    "nom": "Mbacle",
    "prenom": "Kilian",
    "email": "kilian.mbacle@univ.fr",
    "type": "responsable_formation",
    "genre": "H"
  },
  {
    "id": 102,
    "nom": "Nunez",
    "prenom": "Darwin",
    "email": "darwin.nunez@univ.fr",
    "type": "responsable_filiere",
    "genre": "H"
  },
  {
    "id": 103,
    "nom": "Spence",
    "prenom": "Djed",
    "email": "djed.spence@univ.fr",
    "type": "enseignant",
    "genre": "H"
  }
]
```

---

## 7. Sauvegarde des Groupes

### Requête

```http
POST /routeur.php?controleur=ControleurGroupe&action=sauvegarderGroupesAPI
Content-Type: application/json

{
  "promo": 1,
  "groupes": [
    {
      "libelle": "Groupe A",
      "etudiants": [1, 2, 3, 4, 5]
    },
    {
      "libelle": "Groupe B",
      "etudiants": [6, 7, 8, 9, 10]
    },
    {
      "libelle": "Groupe C",
      "etudiants": [11, 12, 13, 14, 15]
    }
  ]
}
```

### Réponse - Succès

```json
{
  "success": true,
  "message": "3 groupes créés avec succès",
  "groupes_crees": [
    {"id": 1, "libelle": "Groupe A", "nb_etudiants": 5},
    {"id": 2, "libelle": "Groupe B", "nb_etudiants": 5},
    {"id": 3, "libelle": "Groupe C", "nb_etudiants": 5}
  ]
}
```

### Réponse - Erreur

```json
{
  "success": false,
  "message": "Erreur lors de la création des groupes",
  "error": "Détail de l'erreur SQL"
}
```

---

## 8. Sondages - Liste

### Requête

```http
GET /routeur.php?controleur=ControleurAPI&action=listeSondagesJSON
```

### Réponse

```json
[
  {
    "id": 1,
    "question": "Préférez-vous les cours en présentiel ou distanciel ?",
    "date_creation": "2026-01-07",
    "nb_reponses": 15,
    "actif": true
  },
  {
    "id": 2,
    "question": "Quel créneau préférez-vous pour les TP ?",
    "date_creation": "2026-01-05",
    "nb_reponses": 23,
    "actif": true
  }
]
```

---

## 9. Sondages - Répondre

### Requête

```http
POST /routeur.php?controleur=ControleurAPI&action=repondreSondageAPI
Content-Type: application/json

{
  "id_sondage": 1,
  "id_etudiant": 2,
  "reponse": "Présentiel"
}
```

### Réponse

```json
{
  "success": true,
  "message": "Réponse enregistrée"
}
```

---

## Codes d'Erreur HTTP

| Code | Signification |
|------|---------------|
| 200 | Succès |
| 400 | Requête invalide (paramètres manquants) |
| 401 | Non authentifié |
| 403 | Accès refusé (permissions insuffisantes) |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

---

## Diagramme de Séquence - Authentification

```
┌─────────────┐          ┌─────────────┐          ┌─────────────┐
│  Java App   │          │  API PHP    │          │   MySQL     │
└──────┬──────┘          └──────┬──────┘          └──────┬──────┘
       │                        │                        │
       │  POST /login           │                        │
       │  {email, password}     │                        │
       │───────────────────────>│                        │
       │                        │                        │
       │                        │  SELECT * FROM         │
       │                        │  Enseignant/Etudiant   │
       │                        │───────────────────────>│
       │                        │                        │
       │                        │  Résultat              │
       │                        │<───────────────────────│
       │                        │                        │
       │  {success, role, id}   │                        │
       │<───────────────────────│                        │
       │                        │                        │
```

---

## Diagramme de Séquence - Chargement Étudiants

```
┌─────────────┐          ┌─────────────┐          ┌─────────────┐
│  Java App   │          │  API PHP    │          │   MySQL     │
└──────┬──────┘          └──────┬──────┘          └──────┬──────┘
       │                        │                        │
       │  GET /etudiants?promo=1│                        │
       │───────────────────────>│                        │
       │                        │                        │
       │                        │  SELECT * FROM         │
       │                        │  Etudiant WHERE promo  │
       │                        │───────────────────────>│
       │                        │                        │
       │                        │  Liste étudiants       │
       │                        │<───────────────────────│
       │                        │                        │
       │  [{id, nom, prenom...}]│                        │
       │<───────────────────────│                        │
       │                        │                        │
```

---

## Données de Test

### Comptes Enseignants

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| `kilian.mbacle@univ.fr` | `Mbacle123!` | Responsable Formation |
| `darwin.nunez@univ.fr` | `Nunez123!` | Responsable Filière |
| `djed.spence@univ.fr` | `Spence123!` | Enseignant |

### Comptes Étudiants

| Email | Mot de passe |
|-------|--------------|
| `lea.martin@etu.univ.fr` | `Martin123!` |

---

## Exemple d'Utilisation en Java

```java
// Connexion à l'API
public boolean login(String email, String password) {
    String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
    String response = restService.post("/login", json);
    
    if (response.contains("\"success\":true")) {
        // Extraire les informations
        this.role = JsonParser.extraireValeur(response, "role");
        this.id = Integer.parseInt(JsonParser.extraireValeur(response, "id"));
        return true;
    }
    return false;
}

// Récupérer les étudiants
public String getEtudiants(int promoId) {
    return restService.get("/etudiants?promo=" + promoId);
}
```

---

## Structure des Fichiers

```
Web/
├── routeur.php              ← Point d'entrée unique
├── controleur/
│   ├── ControleurAPI.php    ← Endpoints JSON
│   ├── ControleurConnexion.php
│   └── ControleurGroupe.php
└── modele/
    ├── Etudiant.php         ← Modèle avec jsonSerialize()
    ├── Enseignant.php
    └── Groupe.php
```
