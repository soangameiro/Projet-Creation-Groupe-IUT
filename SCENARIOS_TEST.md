# Scénarios de Test - Grille d'Évaluation

Documentation complète des scénarios de test pour démonstration de soutenance.


## Scénarios de Test Détaillés

### Scénario 1 : Gestion Complète Responsable Formation
**Objectif :** Démontrer tous les accès du responsable de formation  
**Compte :** `kilian.mbacle@univ.fr` / `Mbacle123!`  
**Points testés :** Critères 1, 5, 6, 8, 9

#### Étapes Web

1. **Connexion**
   ```
   - URL: http://localhost/saes3-mjahier
   - Email: kilian.mbacle@univ.fr
   - Password: Mbacle123!
   - ✅ Vérifier: Dashboard Responsable Formation affiché
   ```

2. **Gestion des Enseignants** (Critère 1)
   ```
   - Cliquer: "Gestion" → "Liste Enseignants"
   - Action: Créer un nouvel enseignant
     * Nom: TestEns
     * Prénom: Demo
     * Email: test.ens@univ.fr
    
   - ✅ Vérifier: Enseignant créé et visible dans la liste
   - Action: Modifier le type → Responsable Filière
   - ✅ Vérifier: Type mis à jour
   -verifier connexion marche
   ```

3. **Consultation Groupes** (Critère 5)
   ```
   - Cliquer: "Gestion Groupes"
   - Sélectionner: Promotion BUT2A
   - ✅ Vérifier: Liste des groupes affichée
   - ✅ Vérifier: Statistiques par groupe (moyenne, H/F, bac)
   ```

4. **Gestion Étudiants** (Critère 6)
   ```
   - Cliquer: "Gestion" → "Liste Étudiants"
   - Action: Rechercher "Martin"
   - ✅ Vérifier: Résultats en temps réel
   - Action: Modifier l'étudiant Martin
     * Changer l'adresse univ
   - ✅ Vérifier: Modification enregistrée
   ```

5. **Création de Groupes** (Critères 8, 9)
   ```
   - Cliquer: "Création Groupes"
   - Sélectionner: Promotion BUT2A
   - ✅ Vérifier: Étudiants chargés automatiquement
   - Configurer:
     * Nombre de groupes: 4
     * Méthode: Algorithme Optimal
   - Cliquer: "1. LANCER ALGO"
   - ✅ Vérifier: Tableau coloré avec résultats
   - ✅ Vérifier: Statistiques affichées (moyenne, H/F, bac)
   - ✅ Vérifier: Score de performance affiché
   - Cliquer: "2. Sauvegarder BDD"
   - ✅ Vérifier: Message de confirmation
   ```

6. **Tester Autres Algorithmes**
   ```
   - Méthode: Glouton Moyenne → Lancer → Observer résultats
   - Méthode: Glouton Bac → Lancer → Observer résultats
   - Méthode: Aléatoire → Lancer → Observer résultats
   - ✅ Comparer les scores entre algorithmes
   ```

#### Étapes Java (Application COO)

1. **Connexion**
   ```
   - Lancer: LoginFrame
   - Email: djed.spence@univ.fr
   - Password: Spence123!
   - ✅ Vérifier: DashboardRespFormation s'ouvre
   ```

2. **Création de Groupes**
   ```
   - Cliquer: "Gestion Groupes"
   - ✅ Vérifier: AlgorithmeGroupesFrame s'ouvre
   - Sélectionner: Promotion
   - Configurer: Nombre de groupes
   - Cliquer: "Lancer Algorithme"
   - ✅ Vérifier: Résultats affichés
   ```

3. **Gestion Enseignants**
   ```
   - Cliquer: "Gestion Enseignants"
   - ✅ Vérifier: GestionEnseignantsFrame s'ouvre
   - ✅ Vérifier: Liste des enseignants chargée depuis API
   ```

---

### Scénario 2 : Gestion Sondages
**Objectif :** Démontrer création et réponse sondages  
**Comptes :**
- Responsable Filière: `darwin.nunez@univ.fr` / `Nunez123!`
- Étudiant: `lea.martin@etu.univ.fr` / `Martin123!`  
**Points testés :** Critère 4

#### Partie 1 : Création (Responsable Filière)

1. **Connexion Responsable**
   ```
   - Email: darwin.nunez@univ.fr
   - Password: Nunez123!
   - Aller: "Sondages" → "Créer un sondage"
   ```

2. **Créer Sondage**
   ```
   - Question: "Préférez-vous les cours en présentiel ou distanciel ?"
   - Cliquer: "Créer le sondage"
   - ✅ Vérifier: Sondage créé et visible dans liste
   - ✅ Vérifier: Design moderne avec cards
   ```

3. **Vérifier Liste**
   ```
   - Retour: Liste sondages
   - ✅ Vérifier: Nouveau sondage affiché
   - ✅ Vérifier: Boutons "Résultats" et "Supprimer"
   ```

#### Partie 2 : Réponse (Étudiant)

1. **Connexion Étudiant**
   ```
   - Se déconnecter
   - Email: lea.martin@etu.univ.fr
   - Password: Martin123!
   - Cliquer: Dashboard → "Sondages"
   ```

2. **Répondre au Sondage**
   ```
   - ✅ Vérifier: Nouveau sondage visible
   - Réponse: "Présentiel"
   - Cliquer: "Envoyer"
   - ✅ Vérifier: Message "Réponse enregistrée"
   - ✅ Vérifier: Badge "✅ Déjà répondu"
   ```

3. **Modifier Réponse**
   ```
   - Cliquer: "✏️ Modifier ma réponse"
   - Changer: "Distanciel"
   - Enregistrer
   - ✅ Vérifier: Modification enregistrée
   ```

#### Partie 3 : Consulter Résultats (Responsable)

1. **Retour Responsable**
   ```
   - Se reconnecter: darwin.nunez@univ.fr
   - Sondages → Résultats
   ```

2. **Vérifier Statistiques**
   ```
   - ✅ Vérifier: Réponses affichées
   - ✅ Vérifier: Graphique en barres
   - ✅ Vérifier: Pourcentages calculés
   - ✅ Vérifier: Liste détaillée avec noms étudiants
   ```

---

### Scénario 3 : Consultation Étudiant
**Objectif :** Démontrer accès étudiant complet  
**Compte :** `lea.martin@etu.univ.fr` / `Martin123!`  
**Points testés :** Critères 2, 3

#### Navigation Dashboard

1. **Mon Profil** (Critère 3)
   ```
   - Cliquer: "Mes Infos"
   - ✅ Vérifier: Profil affiché avec:
     * Nom, Prénom
     * Email universitaire
     * Genre
     * Type de Bac (libellé: "Général" ou "Technologique")
     * Moyenne générale
     * Adresse
   - ✅ Vérifier: Design moderne avec carte profil
   ```

2. **Mon Groupe** (Critère 3)
   ```
   - Retour Dashboard
   - Cliquer: "Mon Groupe"
   - ✅ Vérifier: Informations groupe:
     * Nom du groupe
     * Promotion
     * Nombre de membres
     * Moyenne du groupe
     * Répartition H/F
     * Répartition Bac Général/Technologique
   - ✅ Vérifier: Liste des coéquipiers avec:
     * Nom, Prénom
     * Email
     * Type de bac
     * Moyenne
     * Badge "(Moi)" sur son profil
   ```

3. **Annuaire** (Critère 3)
   ```
   - Retour Dashboard
   - Cliquer: "Annuaire"
   - ✅ Vérifier: Affichage de TOUS (étudiants + enseignants)
   - Tester recherche: "Martin"
   - ✅ Vérifier: Filtrage en temps réel
   - Tester tri: "Trier par Nom"
   - ✅ Vérifier: Liste triée
   - ✅ Vérifier: Badges différents pour:
     * 🎓 Étudiant - Général/Technologique
     * 👨‍🏫 Enseignant
     * 👔 Responsable Filière
     * 🎯 Responsable Formation
   ```

4. **Covoiturage** (Critère 2)
   ```
   - Retour Dashboard
   - Cliquer: "Covoiturage"
   - ✅ Vérifier: Page accessible
   - ✅ Vérifier: Interface de gestion covoiturage
   - Note: Fonctionnalité de base implémentée
   ```

---

### Scénario 4 : Consultation Enseignant Classique
**Objectif :** Vérifier accès lecture seule  
**Compte :** `djed.spence@univ.fr` / `Spence123!`  
**Points testés :** Critère 5

#### Tests d'Accès

1. **Connexion**
   ```
   - Email: djed.spence@univ.fr
   - Password: Spence123!
   - ✅ Vérifier: Dashboard Enseignant
   ```

2. **Consultation Groupes** (Critère 5)
   ```
   - Cliquer: Lien vers groupes
   - ✅ Vérifier: Accès autorisé (lecture seule)
   - ✅ Vérifier: Pas de boutons modification/suppression
   - ✅ Vérifier: Statistiques visibles
   ```

3. **Annuaire**
   ```
   - Accéder: Annuaire
   - ✅ Vérifier: Liste complète visible
   - ✅ Vérifier: Recherche et tri fonctionnels
   ```

4. **Restrictions**
   ```
   - ✅ Vérifier: Pas d'accès "Création Groupes"
   - ✅ Vérifier: Pas d'accès "Gestion Étudiants"
   - ✅ Vérifier: Pas d'accès "Gestion Enseignants"
   ```

---

### Scénario 5 : Import CSV Étudiants
**Objectif :** Démontrer import en masse  
**Compte :** `darwin.nunez@univ.fr` / `Nunez123!`  
**Points testés :** Critères 6, 7

#### Préparation Fichier CSV

1. **Créer fichier test** `etudiants_test.csv`
   ```csv
   nom,prenom,email,bac,moyenne,genre
   Dupont,Jean,jean.dupont@etu.univ.fr,1,14.5,H
   Martin,Sophie,sophie.martin@etu.univ.fr,2,15.2,F
   Bernard,Thomas,thomas.bernard@etu.univ.fr,1,13.8,H
   ```

2. **Import**
   ```
   - Connexion: darwin.nunez@univ.fr
   - Aller: Gestion → Import CSV
   - Sélectionner: etudiants_test.csv
   - Cliquer: "Importer"
   - ✅ Vérifier: Message de succès
   - ✅ Vérifier: Nouveaux étudiants dans liste
   ```

3. **Vérification**
   ```
   - Liste étudiants
   - Rechercher: "Dupont"
   - ✅ Vérifier: Jean Dupont présent
   - ✅ Vérifier: Bac affiché comme "Général" (pas "1")
   - ✅ Vérifier: Moyenne correcte
   ```

---

### Scénario 6 : Communication API REST
**Objectif :** Démontrer communication entre composants  
**Points testés :** Critère 10

#### Test API avec Application Java

1. **Lancer Application Java**
   ```
   - Exécuter: LoginFrame.java
   - ✅ Vérifier: Logs console montrent:
     "Tentative de connexion : kilian.mbacle@univ.fr"
   ```

2. **Observer Communication**
   ```
   - Connexion avec identifiants valides
   - ✅ Vérifier logs console:
     "Réponse de l'API : {\"success\":true,\"role\":\"...\"}"
     "Connexion réussie - Role : responsable_formation, ID : 101"
   ```

3. **Charger Données**
   ```
   - Ouvrir: Gestion Étudiants
   - ✅ Vérifier logs:
     "GET /etudiants"
     "Réponse : [{...}]"
   - ✅ Vérifier: Données affichées dans interface
   ```



**Ordre recommandé :**

1. **Introduction (2 min)**
   - Présenter architecture (Web + Java + API)
   - Montrer diagramme architecture

2. **Démo Web - Responsable Formation (5 min)**
   - Connexion
   - Gestion enseignants (création)
   - Configuration et création groupes (algorithme optimal)
   - Montrer statistiques

3. **Démo Web - Sondages (3 min)**
   - Connexion Responsable Filière
   - Créer sondage
   - Connexion Étudiant
   - Répondre et modifier

4. **Démo Web - Étudiant (3 min)**
   - Mon profil
   - Mon groupe
   - Annuaire avec recherche/tri

5. **Démo Java - Application COO (4 min)**
   - Connexion
   - Montrer dashboard
   - Création groupes
   - Montrer logs API

6. **API REST (2 min)**
   - Montrer code RestService.java
   - Montrer endpoints dans ControleurAPI.php
   - Exemple requête/réponse

7. **Questions (6 min)**

---

## Script de Présentation Suggéré

### Introduction
> "Notre projet implémente un système complet de gestion des groupes d'étudiants avec deux interfaces : une application web en PHP et une application desktop en Java, communiquant via une API REST."

### Démonstration Technique
> "Je vais vous montrer comment un responsable de formation peut créer des groupes optimisés en quelques clics..."

> "L'algorithme optimal teste automatiquement 6 algorithmes différents et sélectionne le meilleur selon des critères pondérés : équilibre des moyennes, mixité de genre, et diversité des parcours."

### Communication API
> "Côté technique, quand l'application Java se connecte, elle envoie une requête POST à notre API PHP. Vous pouvez voir dans la console les échanges JSON..."

---

## Conseils pour la Soutenance

### Points Forts à Mettre en Avant

1. **6 Algorithmes de Répartition**
   - Dont un algorithme optimal qui sélectionne automatiquement le meilleur
   - Visualisation temps réel des résultats
   - Score de performance pour chaque algorithme

2. **Architecture MVC Complète**
   - Séparation claire Modèle-Vue-Contrôleur
   - Code bien documenté et commenté
   - Design patterns appliqués

3. **Interface Moderne**
   - Design system "Liquid Glass" cohérent
   - Animations et transitions fluides
   - Responsive design

4. **API REST Fonctionnelle**
   - Communication bidirectionnelle Java ↔ PHP
   - Format JSON standard
   - Gestion des erreurs

5. **Gestion Complète des Rôles**
   - 4 types d'utilisateurs différents
   - Permissions granulaires
   - Sécurité implémentée

### Démonstration Fluid

- **Naviguer rapidement** entre les fonctionnalités
- **Montrer les logs** pour prouver la communication API
- **Comparer les algorithmes** pour montrer l'intérêt de l'optimal
- **Avoir des données de test** préparées

### Si Problème Technique

- **Plan B** : Vidéo de démonstration pré-enregistrée
- **Screenshots** des fonctionnalités clés
- **Code prêt à montrer** pour expliquer le fonctionnement

---

## Résumé Points Évaluation

| Critère | Implémenté | Testable | Points |
|---------|------------|----------|--------|
| Accès enseignants | ✅ Oui | ✅ Oui | 1/1 |
| Covoiturage | ✅ Oui | ✅ Oui | 1/1 |
| Consultation étudiants | ✅ Oui | ✅ Oui | 1/1 |
| Sondages | ✅ Oui | ✅ Oui | 2/2 |
| Consultation enseignants | ✅ Oui | ✅ Oui | 1/1 |
| Gestion étudiants | ✅ Oui | ✅ Oui | 1/1 |
| Import notes | ⚠️ CSV | ✅ Oui | 0.5/1 |
| Configuration groupes | ✅ Oui | ✅ Oui | 2/2 |
| Algorithmes (Java+Web) | ✅ Oui | ✅ Oui | 2/2 |
| API REST | ✅ Oui | ✅ Oui | 2/2 |

**Total estimé : 13.5/14 points** (~96%)

---

## Conclusion

Tous les critères sont implémentés et testables. Les scénarios ci-dessus permettent de démontrer chaque fonctionnalité de manière claire et structurée.

**Temps total de démonstration : ~20 minutes** (parfait pour une soutenance de 25 minutes avec questions)
