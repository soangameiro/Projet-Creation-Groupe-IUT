<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Liste des Étudiants - IUT</title>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div class="header-top">
            <h1>Gestion des Étudiants</h1>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>

        <!-- Barre de recherche -->
        <div style="margin-bottom: 20px;">
            <input type="text" id="search-input" placeholder="🔍 Rechercher un étudiant (nom, prénom, email)..."
                style="width: 100%; padding: 12px; border-radius: 8px; border: 1px solid #ddd; font-size: 1rem;">
        </div>

        <div class="actions-bar">
            <a href="routeur.php?controleur=ControleurGestion&action=afficherFormulaireAjout" class="btn-action">
                <span class="icon">➕</span> Ajouter un étudiant
            </a>
        </div>

        <div class="card-table">
            <table class="table-moderne" id="student-table">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Genre</th>
                        <th>Bac</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (!empty($etudiants)): ?>
                        <?php foreach ($etudiants as $etu): ?>
                            <tr data-nom="<?= strtolower($etu->getNom()) ?>"
                                data-prenom="<?= strtolower($etu->getPrenom()) ?>"
                                data-email="<?= strtolower($etu->getEmail() ?? '') ?>">
                                <td><?= htmlspecialchars($etu->getNom()) ?></td>
                                <td><?= htmlspecialchars($etu->getPrenom()) ?></td>
                                <td><?= htmlspecialchars($etu->getEmail() ?? 'Non renseigné') ?></td>
                                <td><?= $etu->getGenre() == 'H' ? 'Homme' : ($etu->getGenre() == 'F' ? 'Femme' : 'Autre') ?></td>
                                <td><?= $etu->getBacLibelle() ?></td>
                                <td class="actions-cell">
                                    <a href="routeur.php?controleur=ControleurGestion&action=afficherFormulaireModif&id=<?= $etu->getId() ?>"
                                        class="btn-edit">✏️ Modifier</a>

                                    <a href="routeur.php?controleur=ControleurGestion&action=traiterSuppression&id=<?= $etu->getId() ?>"
                                        class="btn-delete"
                                        onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?');">🗑️
                                        Supprimer</a>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <tr id="no-results-row">
                            <td colspan="6" style="text-align:center;">Aucun étudiant trouvé.</td>
                        </tr>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        // Filtrage en temps réel
        document.addEventListener('DOMContentLoaded', () => {
            const searchInput = document.getElementById('search-input');
            const studentRows = document.querySelectorAll('#student-table tbody tr[data-nom]');
            const tbody = document.querySelector('#student-table tbody');

            searchInput.addEventListener('input', (e) => {
                const searchTerm = e.target.value.toLowerCase().trim();
                let visibleCount = 0;

                studentRows.forEach(row => {
                    const nom = row.dataset.nom || '';
                    const prenom = row.dataset.prenom || '';
                    const email = row.dataset.email || '';

                    const matches = nom.includes(searchTerm) ||
                        prenom.includes(searchTerm) ||
                        email.includes(searchTerm);

                    if (matches || searchTerm === '') {
                        row.style.display = '';
                        visibleCount++;
                    } else {
                        row.style.display = 'none';
                    }
                });

                // Afficher un message si aucun résultat
                let noResultsRow = document.getElementById('no-results-search');
                if (visibleCount === 0 && studentRows.length > 0) {
                    if (!noResultsRow) {
                        noResultsRow = document.createElement('tr');
                        noResultsRow.id = 'no-results-search';
                        noResultsRow.innerHTML = '<td colspan="6" style="text-align:center;">Aucun étudiant ne correspond à votre recherche.</td>';
                        tbody.appendChild(noResultsRow);
                    }
                } else if (noResultsRow) {
                    noResultsRow.remove();
                }
            });
        });
    </script>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>
