<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Formulaire Enseignant</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <link rel="stylesheet" href="assets/css/animations.css">

</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>
    <div class="container">
        <h1><?= isset($enseignant) ? "Modifier" : "Ajouter" ?> un enseignant</h1>

        <?php if (isset($error)): ?>
            <div class="message-box error-box">
                <span class="message-icon">⚠️</span>
                <div class="message-content">
                    <strong>Erreur</strong>
                    <p><?= htmlspecialchars($error) ?></p>
                </div>
            </div>
        <?php endif; ?>

        <?php if (isset($success)): ?>
            <div class="message-box success-box">
                <span class="message-icon">✅</span>
                <div class="message-content">
                    <strong>Succès</strong>
                    <p><?= htmlspecialchars($success) ?></p>
                </div>
            </div>
        <?php endif; ?>

        <form action="routeur.php?controleur=ControleurEnseignant&action=enregistrer" method="post"
            class="form-moderne">

            <?php if (isset($enseignant)): ?>
                <input type="hidden" name="id_ens" value="<?= $enseignant['id_ens'] ?>">
            <?php endif; ?>

            <div class="form-group">
                <label>Nom :</label>
                <input type="text" name="nom" required value="<?= $enseignant['nom_ens'] ?? '' ?>">
            </div>

            <div class="form-group">
                <label>Prénom :</label>
                <input type="text" name="prenom" required value="<?= $enseignant['prenom_ens'] ?? '' ?>">
            </div>

            <div class="form-group">
                <label>Email :</label>
                <input type="email" name="email" required value="<?= $enseignant['courriel_ens'] ?? '' ?>">
            </div>

            <div class="form-group">
                <label>Mot de passe :</label>
                <input type="password" name="password" <?= isset($enseignant) ? '' : 'required' ?> 
                       placeholder="<?= isset($enseignant) ? 'Laisser vide pour ne pas changer' : 'Mot de passe' ?>">
                <?php if (isset($enseignant)): ?>
                    <small style="color: var(--scorpion); font-size: 0.85rem;">Laissez vide si vous ne souhaitez pas modifier le mot de passe</small>
                <?php endif; ?>
            </div>

            <div style="display: flex; gap: 12px; margin-top: 20px;">
                <button type="submit" class="btn-action">💾 Enregistrer</button>
                <a href="routeur.php?controleur=ControleurEnseignant&action=liste" class="btn-retour">Retour</a>
            </div>
        </form>
    </div>
    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>