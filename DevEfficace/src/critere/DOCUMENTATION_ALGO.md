# Documentation des Algorithmes de Répartition

Ce document détaille le fonctionnement structurel et algorithmique du projet de répartition des étudiants. Le projet unifie trois approches différentes (Soan, Thomas, Maelan) au sein d'une architecture commune.

---

## 🏗️ Architecture Unifiée

Le projet repose sur des classes communes situées dans le package `critere` (anciennement `commun`).

### 1. Classe `Etudiant`

Représente un étudiant avec l'ensemble des attributs nécessaires pour les 3 modes :

- **Identité** : Nom, Prénom.
- **Académique** : Moyenne, Type de Bac (Général/Technologique), Notes Spécifiques (Java, BD).
- **Social** : Sexe (H/F), Parcours (Dev/Reseau/Cyber), ID Covoiturage.
- **Statut** : Redoublant, Recherche d'apprentissage.

### 2. Classe `Groupe`

Représente un groupe d'étudiants et contient les logiques de validation pour chaque mode :

- **Mode Soan** : `calculerScore()` -> Retourne un score basé sur les bonus (covoiturage, parcours) et malus (contraintes non respectées).
- **Mode Thomas** : `calculerQualite()` -> Retourne une qualité (0-100) basée sur la présence de leaders et l'équilibre des bacs.
- **Mode Maelan** : `estValide()` -> Vérifie les contraintes strictes (taille, % filles).

### 3. Classe `Generateur`

Permet de générer des promotions d'étudiants adaptées aux critères de test :

- `genererPromoSoan(n)` : Génère parcours, covoiturage et ~40% de filles.
- `genererPromoThomas(n)` : Génère notes Java/BD et statuts (redoublant/apprenti).
- `genererPromoMaelan(n)` : Génère moyennes et types de bac équilibrés.

---

## 🧠 Algorithmes par Auteur

### 👤 1. Mode Soan (Parcours & Cohésion)

**Objectif :** Maximiser la cohésion sociale et académique.

#### Critères de Score :

- **Contraintes Dures** (Score = -10000 si non respecté) :
  - Taille du groupe (min/max).
  - Mixité : Minimum 4 filles par groupe (si le groupe contient des filles).
- **Bonus** :
  - **+1 pt** par paire d'étudiants en covoiturage.
  - **+1 pt** par étudiant ayant le même parcours que la majorité.
  - **+10 pts** si la mixité est respectée.

#### Algorithmes :

1.  **GloutonScoreSoan (Best Fit)** : Place chaque étudiant dans le groupe où il augmente le plus le score instantané.
2.  **GloutonTriSoan (Stratégique)** : Trie les étudiants par "clusters" de covoiturage, puis les répartit pour maximiser les regroupements.
3.  **ForceBruteSoan (Backtracking)** : Teste toutes les combinaisons possibles (utilisable uniquement sur de petits effectifs < 20).

---

### 👤 2. Mode Thomas (Compétences & Équilibre)

**Objectif :** Garantir des têtes de classe et l'hétérogénéité.

#### Critères de Qualité (sur 100) :

- **Départ** : 100 points.
- **Pénalités** :
  - **-40 pts** si aucun Leader Java (>15/20).
  - **-40 pts** si aucun Leader BD (>15/20).
  - **-10 pts** par point d'écart entre le nombre de Bac G et Bac T.

#### Algorithmes :

1.  **Glouton1Thomas (Utilité)** : Calcule un "coût" d'ajout pour chaque étudiant et minimise ce coût global.
2.  **Glouton2Thomas (Par Vagues)** : Répartit d'abord les Leaders Java, puis les Leaders BD, puis complète avec les autres pour équilibrer.
3.  **ForceBruteThomas (Récursif)** : Explore récursivement les affectations pour trouver le maximum global.

---

### 👤 3. Mode Maelan (Moyenne & Bacs)

**Objectif :** Lisser les niveaux et les profils scolaires.

#### Score Global (à minimiser) :

- **Score = ÉcartMoyenneMax + ÉcartBacMax**
- _ÉcartMoyenneMax_ : Différence entre la meilleure et la pire moyenne de groupe.
- _ÉcartBacMax_ : Différence du nombre de Bac Généraux entre le groupe qui en a le plus et celui qui en a le moins.

#### Algorithmes :

1.  **AlgorithmeMaelan.gloutonParMoyenne** : Trie les étudiants par moyenne décroissante et distribue en "serpentin" (1,2,3,4,4,3,2,1...) ou circulaire.
2.  **AlgorithmeMaelan.gloutonParBac** : Sépare Bac G et Bac T, puis distribue équitablement chaque sous-liste.
3.  **AlgorithmeMaelan.forceBrute** : Méthode de Monte-Carlo (tente N permutations aléatoires et garde la meilleure).

---

## 🧪 Tests Unitaires

Chaque auteur dispose de son propre fichier de test pour valider ses algorithmes :

- `TestSoan.java` : Vérifie que le score n'est pas de -10000 (contraintes respectées).
- `TestThomas.java` : Affiche la qualité de chaque groupe (objectif 100).
- `TestMaelan.java` : Compare les écarts de moyenne (objectif 0).

**Exécution :**
Compilez et lancez via Eclipse ou en ligne de commande :

```bash
javac critere/*.java
java -cp . critere.TestSoan
```
