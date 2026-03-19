<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Mon Covoiturage - IUT</title>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div class="header-top">
            <h1>🚗 Mon Covoiturage</h1>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>
        <p style="color: var(--scorpion); margin-bottom: 30px;">Gérez votre groupe de covoiturage (max 4 personnes).</p>

        <?php if ($monCovoiturage): ?>
            <!-- Mon groupe de covoiturage actuel -->
            <div class="card" style="margin-bottom: 30px;">
                <h2>
                    <?php echo htmlspecialchars($monCovoiturage->getLibelle()); ?>
                </h2>
                <p><strong>Membres (
                        <?php echo count($membres); ?>/4):
                    </strong></p>

                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Email</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($membres as $m): ?>
                            <tr>
                                <td>
                                    <?php echo htmlspecialchars($m->getNom()); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($m->getPrenom()); ?>
                                </td>
                                <td>
                                    <?php echo htmlspecialchars($m->getEmail()); ?>
                                </td>
                                <td>
                                    <?php if ($m->getId() != $id_etu): ?>
                                        <a href="routeur.php?controleur=ControleurCovoiturage&action=retirerMembre&id=<?php echo $m->getId(); ?>"
                                            class="btn btn-danger btn-sm" onclick="return confirm('Retirer ce membre ?');">
                                            ✕ Retirer
                                        </a>
                                    <?php else: ?>
                                        <span style="color: #888;">(Vous)</span>
                                    <?php endif; ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>

                <div style="margin-top: 20px;">
                    <a href="routeur.php?controleur=ControleurCovoiturage&action=quitter" class="btn btn-danger"
                        onclick="return confirm('Voulez-vous vraiment quitter ce covoiturage ?');">
                        🚪 Quitter le covoiturage
                    </a>
                </div>
            </div>

            <?php if (count($membres) < 4): ?>
                <!-- Ajouter un membre -->
                <div class="card">
                    <h3>➕ Ajouter un membre</h3>
                    <form action="routeur.php?controleur=ControleurCovoiturage&action=ajouterMembre" method="POST">
                        <div class="form-group">
                            <label for="id_etu">Choisir un étudiant :</label>
                            <select name="id_etu" id="id_etu" class="form-control" required>
                                <option value="">-- Sélectionner --</option>
                                <?php foreach ($etudiantsDisponibles as $e): ?>
                                    <option value="<?php echo $e->getId(); ?>">
                                        <?php echo htmlspecialchars($e->getNom() . ' ' . $e->getPrenom()); ?>
                                    </option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Ajouter</button>
                    </form>
                </div>
            <?php endif; ?>

        <?php else: ?>
            <!-- Pas de covoiturage -->
            <div class="card">
                <h2>Vous n'avez pas encore de groupe de covoiturage</h2>
                <p>Créez un groupe ou attendez qu'un autre étudiant vous ajoute à son groupe.</p>

                <a href="routeur.php?controleur=ControleurCovoiturage&action=creer" class="btn btn-primary">
                    ➕ Créer mon groupe de covoiturage
                </a>
            </div>
        <?php endif; ?>


    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>