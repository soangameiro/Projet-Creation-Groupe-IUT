<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Espace Étudiant - IUT</title>
</head>

<body>

    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>Tableau de bord - Étudiant</h1>
            <p style="color: var(--scorpion);">
                Bienvenue <?php echo htmlspecialchars($userInfo->getPrenom() . ' ' . $userInfo->getNom()); ?> !
            </p>
        </div>

        <!-- Section Mon espace -->
        <h2>Mon espace</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurEtudiant&action=mesInfos&id=<?php echo $userInfo->getId(); ?>"
                class="big-button">
                <span>👤</span> Mes Infos
            </a>

            <a href="routeur.php?controleur=ControleurEtudiant&action=monGroupe" class="big-button">
                <span>👥</span> Mon Groupe
            </a>
        </div>

        <!-- Section Services -->
        <h2 style="margin-top: 30px;">Services</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurSondage&action=afficherPourEtudiant" class="big-button">
                <span>📝</span> Sondages
            </a>

            <a href="routeur.php?controleur=ControleurCovoiturage&action=afficher" class="big-button">
                <span>🚗</span> Covoiturage
            </a>
        </div>

        <!-- Section Consultation -->
        <h2 style="margin-top: 30px;">Consultation</h2>
        <div class="dashboard-grid">
            <a href="routeur.php?controleur=ControleurEtudiant&action=infosPubliques" class="big-button">
                <span>📚</span> Annuaire
            </a>
        </div>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>