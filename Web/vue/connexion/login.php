<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - IUT d'Orsay</title>
    <link rel="icon" type="image/png" href="assets/logo.png">
    <link rel="apple-touch-icon" href="assets/logo.png">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <link rel="stylesheet" href="assets/css/animations.css">

</head>

<body>

    <div class="login-wrapper">
        <div class="login-box">
            <img src="assets/logo.png" alt="Logo IUT" class="logo-img">

            <h2>Bienvenue</h2>
            <p class="subtitle" style="margin-bottom: 30px;">Espace de gestion S301</p>

            <?php if (isset($error))
                echo "<div style='background:#fee2e2; color:#b91c1c; padding:15px; border-radius:12px; margin-bottom:25px; font-weight:600;'>⚠️ $error</div>"; ?>

            <form action="routeur.php?controleur=ControleurConnexion&action=connecter" method="post">

                <div class="input-group">
                    <label>Identifiant Universitaire</label>
                    <input type="email" name="email" placeholder="prenom.nom@univ.fr" required>
                </div>

                <div class="input-group">
                    <label>Mot de passe</label>
                    <input type="password" name="password" placeholder="Mot de passe" required>
                </div>

                <button type="submit" style="width: 100%; font-size: 1.1rem; padding: 15px;">Se connecter</button>
            </form>
        </div>
    </div>

</body>

</html>