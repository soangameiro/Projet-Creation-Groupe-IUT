<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Gestion des Sondages</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <style>
        .sondages-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .sondage-card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s, box-shadow 0.2s;
            border-left: 4px solid var(--primary);
        }

        .sondage-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
        }

        .sondage-question {
            font-size: 1.1rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
            line-height: 1.4;
        }

        .sondage-id {
            display: inline-block;
            background: var(--primary);
            color: white;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 0.85rem;
            margin-bottom: 10px;
        }

        .sondage-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .empty-state-icon {
            font-size: 60px;
            margin-bottom: 20px;
        }
    </style>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div
            style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
                <h1>📊 Gestion des Sondages</h1>
                <p style="color: var(--scorpion); margin: 0;">Créez et gérez les sondages pour les étudiants.</p>
            </div>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour Tableau de Bord
            </a>
        </div>

        <?php if (isset($_GET['success'])): ?>
            <div
                style="background: #e8f5e9; padding: 15px; border-radius: 8px; margin-bottom: 20px; border-left: 4px solid #27ae60;">
                ✓ Sondage créé avec succès !
            </div>
        <?php endif; ?>

        <div style="margin-bottom: 30px;">
            <a href="routeur.php?controleur=ControleurSondage&action=formulaireCreer" class="btn btn-primary"
                style="font-size: 1rem; padding: 12px 24px;">
                ➕ Créer un sondage
            </a>
        </div>

        <?php if (empty($sondages)): ?>
            <div class="empty-state">
                <div class="empty-state-icon">📋</div>
                <h2 style="color: #2c3e50; margin-bottom: 10px;">Aucun sondage</h2>
                <p style="color: #666;">Commencez par créer un sondage pour recueillir les avis des étudiants.</p>
                <a href="routeur.php?controleur=ControleurSondage&action=formulaireCreer" class="btn btn-primary"
                    style="margin-top: 20px;">
                    ➕ Créer mon premier sondage
                </a>
            </div>
        <?php else: ?>
            <div class="sondages-grid">
                <?php foreach ($sondages as $s): ?>
                    <div class="sondage-card">
                        <span class="sondage-id">#<?php echo $s->getId(); ?></span>
                        <div class="sondage-question">
                            <?php echo htmlspecialchars($s->getQuestion()); ?>
                        </div>
                        <div class="sondage-actions">
                            <a href="routeur.php?controleur=ControleurSondage&action=resultats&id=<?php echo $s->getId(); ?>"
                                class="btn btn-secondary btn-sm" style="flex: 1;">
                                📊 Résultats
                            </a>
                            <a href="routeur.php?controleur=ControleurSondage&action=supprimer&id=<?php echo $s->getId(); ?>"
                                class="btn btn-danger btn-sm"
                                onclick="return confirm('Supprimer ce sondage et toutes ses réponses ?');">
                                🗑️ Supprimer
                            </a>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>