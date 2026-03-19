# Guide d'Installation et Configuration

Documentation complète pour installer et faire fonctionner le projet SAE S3.

---

## Prérequis

### Logiciels Requis

| Logiciel | Version | Téléchargement |
|----------|---------|----------------|
| **XAMPP** (Apache + MySQL + PHP) | 8.0+ | [apachefriends.org](https://www.apachefriends.org/) |
| **Java JDK** | 11+ | [oracle.com/java](https://www.oracle.com/java/technologies/downloads/) |
| **Eclipse IDE** (ou IntelliJ) | 2023+ | [eclipse.org](https://www.eclipse.org/downloads/) |

---

## Structure du Projet

```
s3-sae-3dg3/
├── Web/                    ← Application PHP (serveur web)
├── COO/                    ← Application Java Swing (client)
├── DevEfficace/            ← Algorithmes de répartition
└── GUIDE_INSTALLATION.md   ← Ce fichier
```


## Installation - Application Java (COO)

### Étape 1 : Importer dans Eclipse

1. Ouvrir **Eclipse IDE**
2. `File` → `Open Projects from File System`
3. Sélectionner le dossier `COO/`
4. Cliquer **Finish**

### Étape 2 : Configurer Gson

1. Clic droit sur le projet → `Build Path` → `Configure Build Path`
2. Onglet `Libraries` → `Add External JARs`
3. Sélectionner `COO/lib/gson-2.8.9.jar`
4. Cliquer **Apply and Close**

### Étape 3 : Configurer l'URL de l'API

Éditer `COO/src/Infrastructure/RestService.java` :

```java
public class RestService {
    // Modifier cette URL selon votre configuration
    private static final String BASE_URL = "http://localhost/saes3-mjahier";
    
    // ...
}
```

### Étape 4 : Lancer l'application

1. Dans Eclipse, ouvrir `Vue/LoginFrame.java`
2. Clic droit → `Run As` → `Java Application`
3. Fenêtre de connexion doit s'afficher ✅

---

## Comptes de Test

### Enseignants

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| `kilian.mbacle@univ.fr` | `Mbacle123!` | Responsable Formation |
| `darwin.nunez@univ.fr` | `Nunez123!` | Responsable Filière |
| `djed.spence@univ.fr` | `Spence123!` | Enseignant |

### Étudiants

| Email | Mot de passe |
|-------|--------------|
| `lea.martin@etu.univ.fr` | `Martin123!` |

---



---

## Fonctionnalités Principales

Une fois installé, vous pourrez :

### Application Web
- Se connecter selon son rôle
- Créer des groupes avec différents algorithmes
- Consulter l'annuaire (étudiants + enseignants)
- Créer et répondre aux sondages
- Importer/exporter des données CSV

### Application Java
- Se connecter via l'API REST
- Gérer les étudiants et enseignants
- Créer des groupes
- Consulter les données



**Projet académique - IUT d'Orsay 2025-2026**
