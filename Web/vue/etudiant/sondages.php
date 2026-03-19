<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Mes Sondages - IUT</title>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div class="header-top">
            <h1>📝 Sondages</h1>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>
        <p style="color: var(--scorpion); margin-bottom: 30px;">Répondez aux sondages de votre formation.</p>

        <?php if (empty($sondages)): ?>
            <div class="card">
                <p>Aucun sondage disponible pour le moment.</p>
            </div>
        <?php else: ?>
            <?php foreach ($sondages as $sondage): ?>
                <div class="card" style="margin-bottom: 20px;">
                    <h3><?php echo htmlspecialchars($sondage->getQuestion()); ?></h3>
                    
                    <?php 
                    $maReponse = $mesReponses[$sondage->getId()] ?? null;
                    $options = $sondage->getOptions();
                    ?>
                    
                    <?php if ($maReponse): ?>
                        <div style="background: #e8f5e9; padding: 15px; border-radius: 8px; margin-top: 10px;">
                            <strong>✓ Votre réponse :</strong> <?php echo htmlspecialchars($maReponse); ?>
                        </div>
                        
                        <!-- Modifier la réponse -->
                        <form action="routeur.php?controleur=ControleurSondage&action=repondre" method="POST" style="margin-top: 15px;">
                            <input type="hidden" name="id_sond" value="<?php echo $sondage->getId(); ?>">
                            <div class="form-group">
                                <label>Modifier votre réponse :</label>
                                <select name="reponse" class="form-control" style="display: inline-block; width: auto; margin-left: 10px;">
                                    <?php foreach ($options as $opt): ?>
                                        <option value="<?php echo htmlspecialchars($opt); ?>" 
                                                <?php echo ($opt == $maReponse) ? 'selected' : ''; ?>>
                                            <?php echo htmlspecialchars($opt); ?>
                                        </option>
                                    <?php endforeach; ?>
                                </select>
                                <button type="submit" class="btn btn-secondary btn-sm">Modifier</button>
                            </div>
                        </form>
                    <?php else: ?>
                        <form action="routeur.php?controleur=ControleurSondage&action=repondre" method="POST">
                            <input type="hidden" name="id_sond" value="<?php echo $sondage->getId(); ?>">
                            <div class="form-group">
                                <label>Votre réponse :</label>
                                <?php foreach ($options as $opt): ?>
                                    <label class="sondage-option">
                                        <input type="radio" name="reponse" value="<?php echo htmlspecialchars($opt); ?>" required>
                                        <span class="option-text"><?php echo htmlspecialchars($opt); ?></span>
                                    </label>
                                <?php endforeach; ?>
                            </div>
                            <button type="submit" class="btn btn-primary">Répondre</button>
                        </form>
                        
                        <style>
                            .sondage-option {
                                display: block;
                                margin: 8px 0;
                                padding: 12px 15px;
                                background: #f8f9fa;
                                border: 2px solid #e9ecef;
                                border-radius: 8px;
                                cursor: pointer;
                                transition: all 0.2s ease;
                            }
                            .sondage-option:hover {
                                background: #e9ecef;
                                border-color: #adb5bd;
                            }
                            .sondage-option input[type="radio"] {
                                margin-right: 10px;
                            }
                            .sondage-option:has(input:checked) {
                                background: #d4edda;
                                border-color: var(--shiraz, #a71930);
                            }
                            .sondage-option .option-text {
                                font-weight: 500;
                            }
                        </style>
                    <?php endif; ?>
                </div>
            <?php endforeach; ?>
        <?php endif; ?>


    </div>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>
