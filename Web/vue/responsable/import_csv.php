<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Import CSV</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>📤 Import CSV</h1>
            <p style="color: var(--scorpion);">Importez un fichier CSV pour mettre à jour les notes des étudiants.</p>
        </div>

        <?php if (isset($error)): ?>
            <div style="background: #ffebee; padding: 15px; border-radius: 8px; margin-bottom: 20px; color: #c62828;">
                ⚠️
                <?php echo htmlspecialchars($error); ?>
            </div>
        <?php endif; ?>

        <?php if (isset($success)): ?>
            <div style="background: #e8f5e9; padding: 15px; border-radius: 8px; margin-bottom: 20px; color: #2e7d32;">
                ✓
                <?php echo htmlspecialchars($success); ?>
            </div>
        <?php endif; ?>

        <div class="card" style="margin-bottom: 30px;">
            <h2>Format attendu du fichier CSV</h2>
            <p>Le fichier doit contenir les colonnes suivantes séparées par des <strong>points-virgules (;)</strong> :
            </p>

            <div
                style="background: #f5f5f5; padding: 15px; border-radius: 8px; font-family: monospace; margin: 15px 0;">
                num_etudiant;nom;prenom;note1;note2;note3;...<br>
                1;Dupont;Jean;14.5;12;16<br>
                2;Martin;Marie;15;13.5;14<br>
                ...
            </div>

            <ul>
                <li>La première ligne doit être l'en-tête</li>
                <li>Les 3 premières colonnes sont obligatoires (ID, nom, prénom)</li>
                <li>Les colonnes suivantes sont les notes (la moyenne sera calculée)</li>
                <li>Utilisez des virgules ou points pour les décimales</li>
            </ul>
        </div>

        <div class="card">
            <h2>Importer un fichier</h2>
            <form action="routeur.php?controleur=ControleurCSV&action=importerNotes" method="POST"
                enctype="multipart/form-data">
                <div class="form-group">
                    <label for="fichier">Fichier CSV :</label>
                    <input type="file" name="fichier" id="fichier" class="form-control" accept=".csv,.txt" required>
                </div>

                <button type="submit" class="btn btn-primary">📤 Importer</button>
            </form>
        </div>

        <div class="card" style="margin-top: 30px;">
            <h2>Exporter une promotion</h2>
            <form action="routeur.php" method="GET">
                <input type="hidden" name="controleur" value="ControleurCSV">
                <input type="hidden" name="action" value="exporterPromotion">

                <div class="form-group">
                    <label for="promo">Promotion :</label>
                    <select name="promo" id="promo" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <?php foreach ($promotions as $p): ?>
                            <option value="<?php echo $p->getId(); ?>">
                                <?php echo htmlspecialchars($p->getNom()); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>

                <button type="submit" class="btn btn-secondary">📥 Exporter (simple)</button>
            </form>

            <form action="routeur.php" method="GET" style="margin-top: 10px;">
                <input type="hidden" name="controleur" value="ControleurCSV">
                <input type="hidden" name="action" value="exporterComplet">

                <div class="form-group">
                    <label for="promo2">Export complet (avec données personnelles) :</label>
                    <select name="promo" id="promo2" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <?php foreach ($promotions as $p): ?>
                            <option value="<?php echo $p->getId(); ?>">
                                <?php echo htmlspecialchars($p->getNom()); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>

                <button type="submit" class="btn btn-secondary">📥 Exporter (complet)</button>
            </form>
        </div>

        <div style="margin-top: 30px;">
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour au tableau de bord
            </a>
        </div>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>