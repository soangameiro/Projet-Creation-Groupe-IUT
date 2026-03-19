<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Espace Enseignant - IUT</title>
</head>

<body>

    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">

        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>Tableau de bord - Enseignant</h1>
            <p style="color: var(--scorpion);">
                Bienvenue <?php echo htmlspecialchars($userInfo->getPrenom() . ' ' . $userInfo->getNom()); ?> !
                <br>Consultez les informations pédagogiques.
            </p>
        </div>

        <!-- Section Consultation -->
        <h2>Consultation</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurGestion&action=listeEtudiants" class="big-button">
                <span>👥</span> Liste des Étudiants
            </a>

            <a href="routeur.php?controleur=ControleurGroupe&action=afficher" class="big-button">
                <span>📋</span> Voir les Groupes
            </a>

            <a href="routeur.php?controleur=ControleurEtudiant&action=infosPubliques" class="big-button">
                <span>📚</span> Annuaire
            </a>

            <a href="routeur.php?controleur=ControleurStatistiques&action=afficher" class="big-button">
                <span>📈</span> Statistiques
            </a>
        </div>

    </div>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>