<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Gestion des Enseignants</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/animations.css">

    <?php echo "\n    <link rel=\"stylesheet\" href=\"assets/css/components.css\">\n    <link rel=\"stylesheet\" href=\"assets/css/pages.css\">"; ?>
    <style>
        /* Style spécifique pour copier l'affichage "Etudiants" */
        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
            padding: 20px 0;
        }

        .card-enseignant {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
            text-align: center;
            transition: transform 0.2s;
            border: 1px solid #eee;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .card-enseignant:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .avatar-circle {
            width: 80px;
            height: 80px;
            background-color: #800020;
            /* Couleur IUT */
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .card-name {
            font-size: 1.2rem;
            font-weight: bold;
            color: #333;
            margin: 0;
        }

        .card-email {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 15px;
        }

        .card-actions {
            margin-top: auto;
            /* Pousse les boutons vers le bas */
            display: flex;
            gap: 10px;
            width: 100%;
            justify-content: center;
        }

        .btn-small {
            padding: 8px 15px;
            border-radius: 20px;
            text-decoration: none;
            font-size: 0.85rem;
            font-weight: 500;
            transition: background 0.2s;
        }

        .btn-edit-card {
            background: #f0f0f0;
            color: #333;
        }

        .btn-edit-card:hover {
            background: #e0e0e0;
        }

        .btn-delete-card {
            background: #ffebee;
            color: #d32f2f;
        }

        .btn-delete-card:hover {
            background: #ffcdd2;
        }
    </style>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div class="header-top">
            <h1>Annuaire des Enseignants</h1>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>

        <div style="margin-bottom: 20px;">
            <input type="text" id="search-input" placeholder="🔍 Rechercher un enseignant (nom, prénom, email)..."
                style="width: 100%; padding: 12px; border-radius: 8px; border: 1px solid #ddd; font-size: 1rem;">
        </div>

        <div style="text-align: right; margin-bottom: 20px;">
            <a href="routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire" class="btn-action">
                ➕ Ajouter un enseignant
            </a>
        </div>

        <div class="grid-container" id="teacher-grid">
            <?php if (!empty($enseignants)): ?>
                <?php foreach ($enseignants as $prof): ?>
                    <div class="card-enseignant" data-nom="<?= strtolower($prof['nom_ens']) ?>"
                        data-prenom="<?= strtolower($prof['prenom_ens']) ?>"
                        data-email="<?= strtolower($prof['email_ens'] ?? $prof['courriel_ens'] ?? '') ?>">
                        <div class="avatar-circle">
                            <?= strtoupper(substr($prof['prenom_ens'], 0, 1)) ?>
                        </div>

                        <h3 class="card-name"><?= htmlspecialchars($prof['prenom_ens'] . ' ' . $prof['nom_ens']) ?></h3>
                        <p class="card-email"><?= htmlspecialchars($prof['email_ens'] ?? $prof['courriel_ens'] ?? '') ?></p>

                        <?php if (Session::estRespFormation()): ?>
                            <div class="card-actions">
                                <a href="routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire&id=<?= $prof['id_ens'] ?>"
                                    class="btn-small btn-edit-card">
                                    ✏️ Modifier
                                </a>
                                <a href="routeur.php?controleur=ControleurEnseignant&action=supprimer&id=<?= $prof['id_ens'] ?>"
                                    class="btn-small btn-delete-card"
                                    onclick="return confirm('Êtes-vous sûr de vouloir supprimer <?= addslashes($prof['nom_ens']) ?> ?');">
                                    🗑️ Supprimer
                                </a>
                            </div>
                        <?php endif; ?>
                    </div>
                <?php endforeach; ?>
            <?php else: ?>
                <p id="no-results">Aucun enseignant trouvé.</p>
            <?php endif; ?>
        </div>
    </div>

    <script>
        // Filtrage en temps réel
        document.addEventListener('DOMContentLoaded', () => {
            const searchInput = document.getElementById('search-input');
            const teacherCards = document.querySelectorAll('.card-enseignant');
            const grid = document.getElementById('teacher-grid');

            searchInput.addEventListener('input', (e) => {
                const searchTerm = e.target.value.toLowerCase().trim();
                let visibleCount = 0;

                teacherCards.forEach(card => {
                    const nom = card.dataset.nom || '';
                    const prenom = card.dataset.prenom || '';
                    const email = card.dataset.email || '';

                    const matches = nom.includes(searchTerm) ||
                        prenom.includes(searchTerm) ||
                        email.includes(searchTerm);

                    if (matches || searchTerm === '') {
                        card.style.display = '';
                        visibleCount++;
                    } else {
                        card.style.display = 'none';
                    }
                });

                // Afficher un message si aucun résultat
                let noResultsMsg = document.getElementById('no-results-search');
                if (visibleCount === 0 && teacherCards.length > 0) {
                    if (!noResultsMsg) {
                        noResultsMsg = document.createElement('p');
                        noResultsMsg.id = 'no-results-search';
                        noResultsMsg.textContent = 'Aucun enseignant ne correspond à votre recherche.';
                        grid.appendChild(noResultsMsg);
                    }
                } else if (noResultsMsg) {
                    noResultsMsg.remove();
                }
            });
        });
    </script>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>