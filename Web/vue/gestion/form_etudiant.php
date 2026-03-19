<?php
// Si l'étudiant est invalide ou null, on en crée un vide pour ne pas faire planter la page
if (!isset($etudiant) || $etudiant === false) {
    $etudiant = new Etudiant();
}
?>



<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $pageTitle; ?></title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <link rel="stylesheet" href="assets/css/animations.css">

</head>

<body>
    <div class="form-box">
        <h2><?php echo $pageTitle; ?></h2>

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

        <form action="routeur.php?controleur=ControleurGestion&action=<?php echo $actionForm; ?>" method="post">

            <?php if (isset($etudiant) && $etudiant->getId()) { ?>
                <input type="hidden" name="id" value="<?php echo $etudiant->getId(); ?>">
            <?php } ?>

            <div class="grid-fields">

                <div>
                    <label>Prénom</label>
                    <input type="text" name="prenom" value="<?php echo $etudiant->getPrenom(); ?>" required>
                </div>
                <div>
                    <label>Nom</label>
                    <input type="text" name="nom" value="<?php echo $etudiant->getNom(); ?>" required>
                </div>

                <div class="full-width">
                    <label>Email</label>
                    <input type="email" name="email" value="<?php echo $etudiant->getEmail(); ?>" required>
                </div>

                <div>
                    <label>Téléphone</label>
                    <input type="text" name="tel" value="<?php echo $etudiant->getTel(); ?>" placeholder="0600000000">
                </div>
                <div>
                    <label>Âge</label>
                    <input type="number" name="age" value="<?php echo $etudiant->getAge(); ?>" placeholder="20">
                </div>

                <div class="full-width">
                    <label>Adresse Postale</label>
                    <input type="text" name="adresse" value="<?php echo $etudiant->getAdresse(); ?>"
                        placeholder="Rue, Ville...">
                </div>

                <div>
                    <label>Genre</label>
                    <select name="genre">
                        <option value="H" <?php if ($etudiant->getGenre() == 'H')
                            echo 'selected'; ?>>Homme</option>
                        <option value="F" <?php if ($etudiant->getGenre() == 'F')
                            echo 'selected'; ?>>Femme</option>
                        <option value="A" <?php if ($etudiant->getGenre() == 'A')
                            echo 'selected'; ?>>Autre</option>
                    </select>
                </div>

                <div>
                    <label>Moyenne (/20)</label>
                    <input type="number" step="0.01" name="moyenne" value="<?php echo $etudiant->getMoyenne(); ?>"
                        placeholder="Ex: 14.50">
                </div>

                <div class="full-width">
                    <label>Type de Bac</label>
                    <input type="text" name="bac" value="<?php echo $etudiant->getBac(); ?>"
                        placeholder="Ex: Général Maths, STI2D...">
                </div>

            </div>

            <button type="submit" class="btn-submit">💾 Enregistrer les modifications</button>
            <a href="routeur.php?controleur=ControleurGroupe&action=afficherOutil" class="btn-retour">Annuler</a>
        </form>
    </div>
</body>

</html>