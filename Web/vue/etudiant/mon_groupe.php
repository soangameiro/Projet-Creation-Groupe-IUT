<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Mon Groupe</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <style>
        .groupe-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            text-align: center;
        }

        .membres-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .membre-card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.2s;
        }

        .membre-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .membre-avatar {
            width: 60px;
            height: 60px;
            background: #3498db;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            font-weight: bold;
            margin: 0 auto 15px;
        }

        .stats-bar {
            display: flex;
            justify-content: space-around;
            background: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
        }

        .stat-item {
            text-align: center;
        }

        .stat-value {
            font-size: 32px;
            font-weight: bold;
            color: var(--primary);
        }

        .stat-label {
            color: #666;
            font-size: 14px;
            margin-top: 5px;
        }
    </style>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div
            style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
                <h1>🎓 Mon Groupe</h1>
                <p style="color: var(--scorpion); margin: 0;">Consultez votre groupe et vos coéquipiers.</p>
            </div>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour Tableau de Bord
            </a>
        </div>

        <?php if ($groupe): ?>
            <!-- Informations du groupe -->
            <div class="groupe-info">
                <h2 style="margin: 0; font-size: 2.5rem;">
                    <?php echo htmlspecialchars($groupe->getLibelle()); ?>
                </h2>
                <p style="margin: 10px 0 0; opacity: 0.9;">Promotion:
                    <?php echo htmlspecialchars($promotion->getNom()); ?>
                </p>
            </div>

            <!-- Statistiques -->
            <div class="stats-bar">
                <div class="stat-item">
                    <div class="stat-value">
                        <?php echo count($membres); ?>
                    </div>
                    <div class="stat-label">Membres</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">
                        <?php echo number_format($stats['moyenne'], 2); ?>
                    </div>
                    <div class="stat-label">Moyenne Groupe</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">
                        <?php echo $stats['bacG']; ?> /
                        <?php echo $stats['bacT']; ?>
                    </div>
                    <div class="stat-label">Bac Gén / Tech</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">
                        <?php echo $stats['hommes']; ?> /
                        <?php echo $stats['femmes']; ?>
                    </div>
                    <div class="stat-label">H / F</div>
                </div>
            </div>

            <!-- Liste des membres -->
            <div class="card">
                <h2>Mes Coéquipiers</h2>
                <div class="membres-grid">
                    <?php foreach ($membres as $membre): ?>
                        <div class="membre-card <?php echo ($membre->getId() == $etudiant->getId()) ? 'moi' : ''; ?>"
                            style="<?php echo ($membre->getId() == $etudiant->getId()) ? 'border: 3px solid var(--primary);' : ''; ?>">
                            <div class="membre-avatar"
                                style="background: <?php echo ($membre->getId() == $etudiant->getId()) ? 'var(--primary)' : '#3498db'; ?>;">
                                <?php echo strtoupper(substr($membre->getPrenom(), 0, 1)); ?>
                            </div>
                            <h3 style="margin: 0 0 5px; font-size: 1.1rem;">
                                <?php echo htmlspecialchars($membre->getPrenom() . ' ' . $membre->getNom()); ?>
                                <?php if ($membre->getId() == $etudiant->getId()): ?>
                                    <span style="color: var(--primary); font-size: 0.9rem;">😊 (Moi)</span>
                                <?php endif; ?>
                            </h3>
                            <p style="margin: 5px 0; color: #666; font-size: 0.9rem;">
                                📧
                                <?php echo htmlspecialchars($membre->getEmail()); ?>
                            </p>
                            <div style="margin-top: 10px; font-size: 0.85rem; color: #999;">
                                <?php echo $membre->getBacLibelle(); ?> •
                                <?php echo $membre->getGenre() === 'F' ? 'Femme' : 'Homme'; ?>
                                <?php if ($membre->getId() == $etudiant->getId()): ?>
                                    • Moy: <?php echo number_format($membre->getMoyenne(), 2); ?>/20
                                <?php endif; ?>
                            </div>
                        </div>
                    <?php endforeach; ?>
                </div>
            </div>

        <?php else: ?>
            <div class="card" style="text-align: center; padding: 60px;">
                <div style="font-size: 60px; margin-bottom: 20px;">🤷</div>
                <h2>Vous n'êtes pas encore dans un groupe</h2>
                <p style="color: #666;">Les groupes seront créés prochainement par votre responsable.</p>
            </div>
        <?php endif; ?>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>