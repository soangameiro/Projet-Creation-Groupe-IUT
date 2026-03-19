<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Créer un Sondage</title>
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
                <h1>➕ Créer un Sondage</h1>
                <p style="color: var(--scorpion); margin: 0;">Définissez une question pour les étudiants.</p>
            </div>
            <a href="routeur.php?controleur=ControleurSondage&action=liste" class="btn btn-secondary">
                ← Retour Liste Sondages
            </a>
        </div>

        <?php if (isset($error)): ?>
            <div style="background: #ffebee; padding: 15px; border-radius: 8px; margin-bottom: 20px; color: #c62828;">
                ⚠️
                <?php echo htmlspecialchars($error); ?>
            </div>
        <?php endif; ?>

        <div class="card">
            <form action="routeur.php?controleur=ControleurSondage&action=creer" method="POST">
                <div class="form-group">
                    <label for="question">Question / Critère :</label>
                    <input type="text" name="question" id="question" class="form-control"
                        placeholder="Ex: Souhaitez-vous avoir des cours en anglais ?" required>
                </div>

                <div class="form-group">
                    <label for="type">Type de réponse :</label>
                    <select name="type" id="type" class="form-control">
                        <option value="unique">Réponse unique</option>
                        <option value="ordonne">Réponses ordonnées (préférences)</option>
                    </select>
                </div>

                <p style="color: #888; font-size: 14px;">
                    Les options par défaut sont : Oui, Non, Sans avis.<br>
                    Pour personnaliser les options, modifiez le sondage après création.
                </p>

                <button type="submit" class="btn btn-primary">Créer le sondage</button>
                <a href="routeur.php?controleur=ControleurSondage&action=liste" class="btn btn-secondary">Annuler</a>
            </form>
        </div>


    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>