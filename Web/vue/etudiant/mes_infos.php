<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Mes Informations</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/animations.css">

    <?php echo "\n    <link rel=\"stylesheet\" href=\"assets/css/components.css\">\n    <link rel=\"stylesheet\" href=\"assets/css/pages.css\">"; ?>
    <style>
        /* Styles spécifiques pour la fiche profil */
        .profile-card {
            background: var(--white);
            border-radius: 16px;
            box-shadow: var(--shadow-hover);
            padding: 40px;
            max-width: 800px;
            margin: 0 auto;
            /* Centré */
            border: 1px solid rgba(150, 3, 60, 0.1);
            position: relative;
            overflow: hidden;
        }

        /* Bandeau décoratif en haut de la carte */
        .profile-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 6px;
            background: linear-gradient(90deg, var(--paprika), var(--cadillac));
        }

        .profile-header {
            display: flex;
            align-items: center;
            gap: 25px;
            margin-bottom: 40px;
            padding-bottom: 25px;
            border-bottom: 2px solid var(--bon-jour);
        }

        .profile-avatar {
            width: 90px;
            height: 90px;
            background-color: #fff0f5;
            /* Rose très pâle */
            color: var(--paprika);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            font-weight: 700;
            border: 3px solid var(--blossom);
            box-shadow: var(--shadow-soft);
        }

        /* Grille des informations */
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
        }

        /* Chaque bloc d'info */
        .info-item {
            background: var(--bon-jour);
            padding: 15px 20px;
            border-radius: 12px;
            border: 1px solid transparent;
            transition: 0.3s;
        }

        .info-item:hover {
            background: var(--white);
            border-color: var(--blossom);
            box-shadow: var(--shadow-soft);
            transform: translateY(-2px);
        }

        .info-label {
            display: block;
            font-size: 0.8rem;
            color: var(--scorpion);
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 8px;
            font-weight: 700;
        }

        .info-value {
            font-size: 1.2rem;
            color: var(--cod-gray);
            font-weight: 500;
            font-family: 'Playfair Display', serif;
            /* Touche élégante pour les données */
            word-break: break-word;
        }
    </style>
</head>

<body>

    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">

        <div style="margin-bottom: 30px;">
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>

        <div class="profile-card">

            <div class="profile-header">
                <div class="profile-avatar">
                    <?php echo substr($etudiant->getPrenom(), 0, 1); ?>
                </div>
                <div>
                    <h1 style="margin: 0; font-size: 2.2rem;">Mon Profil</h1>
                    <span style="color: var(--cadillac); font-weight: 500; font-size: 1.1rem;">
                        <?php echo $etudiant->getPrenom() . ' ' . $etudiant->getNom(); ?>
                    </span>
                </div>
            </div>

            <div class="info-grid">

                <div class="info-item">
                    <span class="info-label">Email Universitaire</span>
                    <span class="info-value"><?php echo $etudiant->getEmail(); ?></span>
                </div>

                <div class="info-item">
                    <span class="info-label">Genre</span>
                    <span class="info-value">
                        <?php echo ($etudiant->getGenre() == 'F') ? 'Femme' : 'Homme'; ?>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Type de Bac</span>
                    <span class="info-value"><?php echo $etudiant->getBacLibelle(); ?></span>
                </div>

                <div class="info-item">
                    <span class="info-label">Moyenne Générale</span>
                    <span class="info-value" style="color: var(--paprika); font-weight: 700;">
                        <?php echo $etudiant->getMoyenne(); ?>/20
                    </span>
                </div>

                <div class="info-item" style="grid-column: 1 / -1;">
                    <span class="info-label">Adresse Postale</span>
                    <span class="info-value">
                        <?php echo $etudiant->getAdresse() ? $etudiant->getAdresse() : 'Non renseignée'; ?>
                    </span>
                </div>

            </div>

        </div>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>