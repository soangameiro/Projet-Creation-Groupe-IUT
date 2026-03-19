<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Statistiques Promotion</title>
    <link rel="stylesheet" href="assets/css/animations.css">

    <link rel="stylesheet" href="assets/css/style.css">
    <?php echo "\n    <link rel=\"stylesheet\" href=\"assets/css/components.css\">\n    <link rel=\"stylesheet\" href=\"assets/css/pages.css\">"; ?>
    <style>
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            padding: 20px;
        }

        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .stat-number {
            font-size: 2.5rem;
            font-weight: bold;
            color: #800020;
            /* Votre couleur bordeaux */
            margin: 10px 0;
        }

        .stat-label {
            color: #666;
            text-transform: uppercase;
            font-size: 0.9rem;
            letter-spacing: 1px;
        }
    </style>
</head>

<body>
    <div class="top-bar">
        <h1>Tableau de bord Statistiques</h1>
        <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
    </div>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-label">Total Étudiants</div>
            <div class="stat-number"><?= $total ?></div>
        </div>

        <div class="stat-card">
            <div class="stat-label">Femmes</div>
            <div class="stat-number"><?= $femmes ?></div>
            <p><?= $pourcentF ?>% de la promo</p>
        </div>

        <div class="stat-card">
            <div class="stat-label">Hommes</div>
            <div class="stat-number"><?= $hommes ?></div>
            <p><?= $pourcentH ?>% de la promo</p>
        </div>
    </div>
</body>

</html>