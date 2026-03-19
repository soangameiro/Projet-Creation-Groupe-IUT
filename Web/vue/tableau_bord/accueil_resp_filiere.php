<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Espace Responsable de Filière - IUT</title>
</head>

<body>

    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">

        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>Tableau de bord - Responsable de Filière</h1>
            <p style="color: var(--scorpion);">
                Bienvenue <?php echo htmlspecialchars($userInfo->getPrenom() . ' ' . $userInfo->getNom()); ?> !
                <br>Gérez votre promotion et vos étudiants.
            </p>
        </div>

        <!-- Section Gestion des utilisateurs -->
        <h2>Gestion des utilisateurs</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurGestion&action=listeEtudiants" class="big-button">
                <span>👥</span> Gérer les Étudiants
            </a>
        </div>

        <!-- Section Gestion pédagogique -->
        <h2 style="margin-top: 30px;">Gestion pédagogique</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurGroupe&action=afficherOutil" class="big-button">
                <span>🎓</span> Créer des Groupes
            </a>

            <a href="routeur.php?controleur=ControleurSondage&action=liste" class="big-button">
                <span>📊</span> Sondages
            </a>

            <a href="routeur.php?controleur=ControleurCSV&action=formulaireImport" class="big-button">
                <span>📤</span> Import / Export CSV
            </a>

            <a href="routeur.php?controleur=ControleurStatistiques&action=afficher" class="big-button">
                <span>📈</span> Statistiques
            </a>
        </div>

        <!-- Section Consultation -->
        <h2 style="margin-top: 30px;">Consultation</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurGroupe&action=afficher" class="big-button">
                <span>📋</span> Voir les Groupes
            </a>

            <a href="routeur.php?controleur=ControleurEtudiant&action=infosPubliques" class="big-button">
                <span>📚</span> Annuaire
            </a>
        </div>

    </div>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>