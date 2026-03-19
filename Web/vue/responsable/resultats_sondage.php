<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Résultats du Sondage</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div
            style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
                <h1>📊 Résultats du Sondage</h1>
                <p style="color: var(--scorpion); margin: 0;">
                    <?php echo htmlspecialchars($sondage->getQuestion()); ?>
                </p>
            </div>
            <a href="routeur.php?controleur=ControleurSondage&action=liste" class="btn btn-secondary">
                ← Retour Liste Sondages
            </a>
        </div>

        <!-- Statistiques -->
        <div class="card" style="margin-bottom: 30px;">
            <h2>Statistiques</h2>

            <?php if (empty($stats)): ?>
                <p>Aucune réponse pour le moment.</p>
            <?php else: ?>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Réponse</th>
                            <th>Nombre</th>
                            <th>Pourcentage</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        $total = array_sum($stats);
                        foreach ($stats as $reponse => $nb):
                            $pct = $total > 0 ? round(($nb / $total) * 100, 1) : 0;
                            ?>
                            <tr>
                                <td><strong>
                                        <?php echo htmlspecialchars($reponse); ?>
                                    </strong></td>
                                <td>
                                    <?php echo $nb; ?>
                                </td>
                                <td>
                                    <div style="background: #e0e0e0; border-radius: 10px; height: 20px; width: 200px;">
                                        <div
                                            style="background: var(--primary); border-radius: 10px; height: 100%; width: <?php echo $pct; ?>%;">
                                        </div>
                                    </div>
                                    <?php echo $pct; ?>%
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
                <p><strong>Total :
                        <?php echo $total; ?> réponse(s)
                    </strong></p>
            <?php endif; ?>
        </div>

        <!-- Liste des réponses -->
        <div class="card">
            <h2>Détail des réponses</h2>

            <?php if (empty($reponses)): ?>
                <p>Aucune réponse pour le moment.</p>
            <?php else: ?>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Étudiant</th>
                            <th>Réponse</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($reponses as $r): ?>
                            <tr>
                                <td>
                                    <?php echo htmlspecialchars($r['nom_etu'] . ' ' . $r['prenom_etu']); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($r['response']); ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            <?php endif; ?>
        </div>


    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>