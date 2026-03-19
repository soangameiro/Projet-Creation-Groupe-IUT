# Système de Gestion de Groupes S301 - IUT d'Orsay

Application web complète de gestion des groupes d'étudiants pour l'IUT d'Orsay, développée dans le cadre de la SAE S3.

## Table des Matières

- [Vue d'ensemble](#vue-densemble)
- [Fonctionnalités](#fonctionnalités)
- [Types d'utilisateurs](#types-dutilisateurs)
- [Comptes de test](#comptes-de-test)
- [Architecture technique](#architecture-technique)
- [Installation](#installation)

---

##  Vue d'ensemble

Cette application permet de gérer efficacement les groupes d'étudiants en combinant:
- **6 algorithmes de répartition** (dont un algorithme optimal automatique)
- **Gestion complète** des étudiants, enseignants et promotions
- **Sondages personnalisables** pour recueillir les avis
- **Annuaire complet** de tous les utilisateurs
- **Interface moderne** avec design "Liquid Glass"

---
##  Utilisation
   se rendre sur https://projets.iut-orsay.fr/saes3-mjahier/routeur.php

##  Fonctionnalités

###  Gestion des Groupes

#### Algorithmes de Répartition

L'application propose **6 algorithmes** différents pour la création de groupes:

1. **Glouton Moyenne (Base)** - Équilibre les groupes selon la moyenne générale
2. **Glouton Bac (Base)** - Équilibre selon le type de bac (Général/Technologique)
3. **Aléatoire (Base)** - Distribution aléatoire
4. **Glouton 1 - Utilité (Expert)** - Optimise selon plusieurs critères de diversité
5. **Glouton 2 - Vagues (Expert)** - Répartition par vagues successives
6. **Algorithme Optimal (Auto)** - Teste tous les algorithmes et sélectionne le meilleur

#### Fonctionnalités de Création

- **Chargement automatique** des étudiants au changement de promotion
- **Paramètres ajustables** : nombre de groupes, promotion, méthode
- **Visualisation en temps réel** des résultats avec tableau coloré
- **Statistiques détaillées** par groupe (moyenne, répartition H/F, bac)
- **Score de performance** pour chaque algorithme
- **Sauvegarde en base de données** des groupes créés

###  Gestion des Sondages

#### Pour les Responsables
- **Création de sondages** avec questions personnalisées
- **Visualisation des résultats** avec statistiques et graphiques
- **Gestion complète** (création, suppression, consultation)
- **Interface moderne** avec cards et layout épuré

#### Pour les Étudiants
- **Réponse aux sondages** en cours
- **Modification des réponses** déjà soumises
- **Indication visuelle** des sondages déjà répondus
- **Interface intuitive** et claire

###  Gestion des Étudiants

#### Fonctionnalités Responsable
- **CRUD complet** : Création, lecture, modification, suppression
- **Recherche en temps réel** par nom, prénom, email
- **Import CSV** pour ajout en masse
- **Affichage du type de bac** (Général/Technologique) et non du numéro
- **Gestion des promotions** et affectation

#### Profil Étudiant
- **Consultation** de ses informations personnelles
- **Visualisation de son groupe** avec ses coéquipiers
- **Statistiques du groupe** (moyenne, composition H/F, bacs)
- **Design élégant** avec carte profil moderne

###  Gestion des Enseignants

- **CRUD complet** pour les responsables
- **Types d'enseignants** : Enseignant classique, Responsable Filière, Responsable Promotion
- **Consultation en lecture seule** pour les enseignants classiques
- **Interface cohérente** avec la gestion étudiants

###  Annuaire Complet

- **Affichage de tous** : étudiants ET enseignants
- **Recherche avancée** par nom, prénom, email, type
- **Tri dynamique** par nom, prénom, type, email
- **Badges visuels** avec icônes pour différencier étudiants/enseignants
- **Indication du type** d'enseignant (Responsable Filière, etc.)
- **Design responsive** avec cards modernes

###  Statistiques

- **Tableaux de bord** personnalisés par rôle
- **Statistiques globales** sur les promotions
- **Métriques par groupe** (moyennes, répartitions)
- **Visualisations graphiques** des données

---

##  Types d'utilisateurs

###  Étudiant

**Accès :**
- ✅ Consultation de son profil
- ✅ Visualisation de son groupe et coéquipiers
- ✅ Réponse aux sondages
- ✅ Consultation de l'annuaire complet
- ✅ Accès au covoiturage

**Restrictions :**
- ❌ Pas de modification des groupes
- ❌ Pas de gestion des utilisateurs
- ❌ Pas de création de sondages

###  Enseignant Classique

**Accès :**
- ✅ Consultation des groupes (lecture seule)
- ✅ Consultation de l'annuaire
- ✅ Accès aux statistiques

**Restrictions :**
- ❌ Pas de création/modification de groupes
- ❌ Pas de gestion des étudiants
- ❌ Pas de sondages

###  Responsable Filière

**Accès :**
- ✅ Toutes les fonctionnalités enseignant
- ✅ Gestion complète des étudiants
- ✅ Gestion complète des enseignants
- ✅ Création et gestion des sondages

**Restrictions :**
- ❌ Pas de création de groupes (réservé au Responsable Promotion)

###  Responsable Promotion

**Accès :**
- ✅ **TOUS les accès** du Responsable Filière
- ✅ **Création et gestion des groupes** avec tous les algorithmes
- ✅ **Accès à l'outil de répartition automatique**
- ✅ **Suppression des groupes**

**Pouvoir complet sur :**
- ✅ Groupes
- ✅ Étudiants
- Groupes
- Étudiants
- Enseignants
- Sondages
- Promotions

---

## Comptes de Test

### Enseignants

| Email | Nom | Mdp | Type | ID |
|-------|-----|--------|------|-----|
| `kilian.mbacle@univ.fr` | Mbacle | Mbacle123! | Responsable Promotion | 101 |
| `darwin.nunez@univ.fr` | Nunez | Nunez123! | Responsable Filière | 102 |
| `djed.spence@univ.fr` | Spence | Spence123! | Enseignant Classique | 103 |

### Étudiants

| Email | Nom | Mdp | Âge |
|-------|-----|--------|-----|
| `lea.martin@etu.univ.fr` | Martin | Martin123! | 21 |

---

## Architecture Technique

### Technologies Utilisées

**Backend :**
- PHP 7.4+
- Architecture MVC (Modèle-Vue-Contrôleur)
- PDO pour la base de données
- Sessions sécurisées

**Frontend :**
- HTML5 / CSS3
- JavaScript Vanilla (ES6+)
- Design System personnalisé "Liquid Glass"
- Responsive Design

**Base de Données :**
- MySQL 


### Structure du Projet

```
s3-sae-3dg3/
├── Web/                # Application web PHP
│   ├── controleur/     # Contrôleurs MVC
│   ├── modele/         # Modèles de données
│   ├── vue/            # Vues (templates)
│   ├── assets/         # Ressources CSS/JS
│   └── config/         # Configuration
├── COO/                # Application Java COO
├── DevEfficace/        # Algorithmes de répartition
└── README.md           # Ce fichier
```

### Algorithmes de Répartition

Les algorithmes sont basés sur différents critères :

1. **Diversité des compétences** (moyennes générales)
2. **Équilibre des parcours** (types de bac)
3. **Mixité de genre** (répartition H/F)
4. **Optimisation multi-critères** (algorithmes experts)

L'**algorithme optimal** teste automatiquement tous les algorithmes et sélectionne celui qui produit le meilleur score selon des critères pondérés.

---


## Design System

L'application utilise un design system cohérent inspiré du "Liquid Glass" avec :

### Palette de Couleurs
- **Primaires** : Paprika (#96033C), Cadillac (#B04968)
- **Secondaires** : Blossom (rose pâle), Bon Jour (gris clair)
- **Neutres** : Cod Gray (noir), Scorpion (gris foncé)

### Composants UI
- **Cards modernes** avec ombres subtiles et bordures arrondies
- **Boutons cohérents** avec états hover et animations
- **Badges colorés** pour les statuts et types
- **Tableaux stylisés** avec alternance de lignes
- **Formulaires élégants** avec validation visuelle

### Responsive Design
- Grilles adaptatives CSS Grid
- Breakpoints optimisés pour mobile/tablette/desktop
- Navigation mobile-friendly

---

##  Sécurité

### Mesures Implémentées

- ✅ **Échappement HTML** systématique (`htmlspecialchars`)
- ✅ **Requêtes préparées PDO** (protection SQL injection)
- ✅ **Gestion des sessions** sécurisée avec régénération d'ID
- ✅ **Contrôle d'accès** par rôle (RBAC)
- ✅ **Validation des données** côté serveur
- ✅ **Protection CSRF** sur les formulaires critiques
- ✅ **Hashage des mots de passe** (bcrypt/password_hash)

### Bonnes Pratiques

- Séparation des responsabilités (MVC)
- Principe du moindre privilège
- Validation entrée/sortie
- Logs des actions sensibles

---

## Guide d'Utilisation

### Pour les Responsables Promotion

1. **Créer des groupes**
   - Aller dans "Gestion Groupes"
   - Sélectionner une promotion
   - Choisir nombre de groupes et algorithme
   - Cliquer sur "Lancer Algo"
   - Vérifier les résultats et statistiques
   - Sauvegarder en BDD

2. **Gérer les étudiants**
   - "Gestion" → "Liste Étudiants"
   - Ajouter/Modifier/Supprimer
   - Utiliser la recherche pour filtrer

3. **Créer des sondages**
   - "Sondages" → "Créer un sondage"
   - Définir la question
   - Consulter les résultats en temps réel

### Pour les Étudiants

1. **Consulter son groupe**
   - Dashboard → "Mon Groupe"
   - Voir tous les coéquipiers
   - Statistiques du groupe

2. **Répondre aux sondages**
   - Dashboard → "Sondages"
   - Répondre ou modifier ses réponses

3. **Consulter l'annuaire**
   - "Annuaire"
   - Rechercher par nom/type
   - Utiliser les filtres de tri

---



## Contribution

Développé par l'équipe SAE S3.01 - IUT d'Orsay



**Technologies :**
- Backend: PHP/MySQL
- Frontend: HTML/CSS/JavaScript

---

## Licence

Projet académique - IUT d'Orsay 2025-2026

© Tous droits réservés
