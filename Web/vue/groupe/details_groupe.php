<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Détails du Groupe</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>👥
                <?php echo htmlspecialchars($groupe->getLibelle()); ?>
            </h1>
            <p style="color: var(--scorpion);">Liste des membres du groupe.</p>
        </div>

        <!-- Statistiques du groupe -->
        <div class="card" style="margin-bottom: 30px;">
            <h2>Statistiques</h2>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: 15px;">
                <div style="text-align: center; padding: 15px; background: #e3f2fd; border-radius: 10px;">
                    <div style="font-size: 24px; font-weight: bold;">
                        <?php echo $stats['total']; ?>
                    </div>
                    <div>Étudiants</div>
                </div>
                <div style="text-align: center; padding: 15px; background: #e8f5e9; border-radius: 10px;">
                    <div style="font-size: 24px; font-weight: bold;">
                        <?php echo $stats['moyenne']; ?>
                    </div>
                    <div>Moyenne</div>
                </div>
                <div style="text-align: center; padding: 15px; background: #fff3e0; border-radius: 10px;">
                    <div style="font-size: 24px; font-weight: bold;">
                        <?php echo $stats['hommes']; ?>H /
                        <?php echo $stats['femmes']; ?>F
                    </div>
                    <div>Genre</div>
                </div>
                <div style="text-align: center; padding: 15px; background: #f3e5f5; border-radius: 10px;">
                    <div style="font-size: 24px; font-weight: bold;">
                        <?php echo $stats['bac_general']; ?> /
                        <?php echo $stats['bac_techno']; ?>
                    </div>
                    <div>Gén. / Tech.</div>
                </div>
            </div>
        </div>

        <!-- Liste des étudiants -->
        <div class="card">
            <h2>Membres du groupe</h2>

            <?php if (empty($etudiants)): ?>
                <p>Aucun étudiant dans ce groupe.</p>
            <?php else: ?>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Genre</th>
                            <th>Email</th>
                            <th>Moyenne</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($etudiants as $e): ?>
                            <tr>
                                <td>
                                    <?php echo htmlspecialchars($e->getNom()); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($e->getPrenom()); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($e->getGenre()); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($e->getEmail()); ?>
                                </td>
                                <td>
                                    <?php echo $e->getMoyenne() ?? '-'; ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            <?php endif; ?>
        </div>

        <div style="margin-top: 30px;">
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour au tableau de bord
            </a>
            <?php if (Session::estResponsable()): ?>
                <a href="routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=<?php echo $groupe->getIdPromo(); ?>"
                    class="btn btn-primary">
                    ✏️ Gérer les groupes
                </a>
            <?php endif; ?>
        </div>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>