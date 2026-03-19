<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Liste des Groupes</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 40px;">
            <h1>📋 Liste des Groupes</h1>
            <p style="color: var(--scorpion);">Consultez les groupes de la promotion.</p>
        </div>

        <!-- Sélection de la promotion -->
        <div class="card" style="margin-bottom: 20px;">
            <form action="routeur.php" method="GET" style="display: flex; gap: 10px; align-items: center;">
                <input type="hidden" name="controleur" value="ControleurGroupe">
                <input type="hidden" name="action" value="afficher">
                <label for="promo"><strong>Promotion :</strong></label>
                <select name="promo" id="promo" class="form-control" style="width: auto;" onchange="this.form.submit()">
                    <?php foreach ($promotions as $p): ?>
                        <option value="<?php echo $p->getId(); ?>" <?php echo ($p->getId() == $id_promo) ? 'selected' : ''; ?>>
                            <?php echo htmlspecialchars($p->getNom()); ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </form>
        </div>

        <?php if (empty($groupes)): ?>
            <div class="card">
                <p>Aucun groupe créé pour cette promotion.</p>
                <?php if (Session::estResponsable()): ?>
                    <a href="routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=<?php echo $id_promo; ?>" class="btn btn-primary">
                        ➕ Créer des groupes
                    </a>
                <?php endif; ?>
            </div>
        <?php else: ?>
            <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px;">
                <?php foreach ($groupes as $g): 
                    $stats = $statsGroupes[$g->getId()];
                ?>
                    <div class="card">
                        <h3><?php echo htmlspecialchars($g->getLibelle()); ?></h3>
                        <p><strong><?php echo $stats['total']; ?></strong> étudiants</p>
                        <p>Moyenne: <strong><?php echo $stats['moyenne']; ?></strong></p>
                        <p>H: <?php echo $stats['hommes']; ?> | F: <?php echo $stats['femmes']; ?></p>
                        <p>Gén: <?php echo $stats['bac_general']; ?> | Tech: <?php echo $stats['bac_techno']; ?></p>
                        <a href="routeur.php?controleur=ControleurGroupe&action=details&id=<?php echo $g->getId(); ?>" class="btn btn-secondary">
                            Voir les membres
                        </a>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>

        <div style="margin-top: 30px;">
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour au tableau de bord
            </a>
            <?php if (Session::estResponsable()): ?>
                <a href="routeur.php?controleur=ControleurGroupe&action=afficherOutil&promo=<?php echo $id_promo; ?>" class="btn btn-primary">
                    ✏️ Gérer les groupes
                </a>
            <?php endif; ?>
        </div>
    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>
