<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Sondages</title>
    <link rel="stylesheet" href="assets/css/main.css">
    <link rel="stylesheet" href="assets/css/sondages.css">
    <link rel="stylesheet" href="assets/css/animations.css">

</head>

<body class="sondages-page">
    <?php require_once "vue/commun/header.php"; ?>

    <div class="sondage-container">
        <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        <h2 class="sondages-titre">Sondages en cours</h2>

        <?php if (isset($_GET['succes']))
            echo "<p style='color:#34d399;'>✅ Réponse enregistrée !</p>"; ?>

        <?php foreach ($lesSondages as $sondage) {
            $idSondage = $sondage->getId();
            $dejaRepondu = isset($reponsesExistantes[$idSondage]);
            ?>
            <div class="sondage-card">
                <div class="sondage-question">
                    <?php echo $sondage->getQuestion(); ?>
                    <?php if ($dejaRepondu) { ?>
                        <span class="badge-repondu">✅ Déjà répondu</span>
                    <?php } ?>
                </div>

                <?php if ($dejaRepondu) { ?>
                    <!-- Affichage de la réponse existante avec option de modification -->
                    <div class="reponse-existante">
                        <strong>Votre réponse :</strong> <?php echo htmlspecialchars($reponsesExistantes[$idSondage]); ?>
                    </div>
                    <button class="btn-modifier" onclick="toggleEdit(<?php echo $idSondage; ?>)">✏️ Modifier ma réponse</button>

                    <!-- Formulaire de modification (masqué par défaut) -->
                    <div id="form-edit-<?php echo $idSondage; ?>" class="form-modifier" style="display: none;">
                        <form action="routeur.php?controleur=ControleurEtudiant&action=traiterReponse" method="post">
                            <input type="hidden" name="id_etu" value="<?php echo $id_etu; ?>">
                            <input type="hidden" name="id_sond" value="<?php echo $sondage->getId(); ?>">

                            <input type="text" name="reponse" placeholder="Nouvelle réponse..."
                                value="<?php echo htmlspecialchars($reponsesExistantes[$idSondage]); ?>" required>
                            <button type="submit">Enregistrer</button>
                            <button type="button" class="btn-annuler"
                                onclick="toggleEdit(<?php echo $idSondage; ?>)">Annuler</button>
                        </form>
                    </div>
                <?php } else { ?>
                    <!-- Formulaire pour répondre au sondage -->
                    <form action="routeur.php?controleur=ControleurEtudiant&action=traiterReponse" method="post">
                        <input type="hidden" name="id_etu" value="<?php echo $id_etu; ?>">
                        <input type="hidden" name="id_sond" value="<?php echo $sondage->getId(); ?>">

                        <input type="text" name="reponse" placeholder="Votre réponse..." required>
                        <button type="submit">Envoyer</button>
                    </form>
                <?php } ?>
            </div>
        <?php } ?>

        <?php if (empty($lesSondages))
            echo "<p>Aucun sondage disponible pour le moment.</p>"; ?>
    </div>

    <script>
        function toggleEdit(sondageId) {
            const formDiv = document.getElementById('form-edit-' + sondageId);
            formDiv.style.display = formDiv.style.display === 'none' ? 'block' : 'none';
        }
    </script>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>