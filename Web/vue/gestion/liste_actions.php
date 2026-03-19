<!DOCTYPE html>
<html lang="fr">

<head>
    <?php require_once "vue/commun/head.php"; ?>
    <title>Gestion des Enseignants - IUT</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div class="header-top">
            <h1>Gestion des Enseignants</h1>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
        </div>

        <!-- Barre de recherche -->
        <div style="margin-bottom: 20px;">
            <input type="text" id="search-input" placeholder="🔍 Rechercher un enseignant (nom, prénom, email)..."
                style="width: 100%; padding: 12px; border-radius: 8px; border: 1px solid #ddd; font-size: 1rem;">
        </div>

        <div class="actions-bar">
            <a href="routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire" class="btn-action">
                <span class="icon">➕</span> Ajouter un enseignant
            </a>
        </div>

        <div class="card-table">
            <table class="table-moderne" id="teacher-table">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if (!empty($enseignants)): ?>
                        <?php foreach ($enseignants as $prof): ?>
                            <tr data-nom="<?= strtolower($prof['nom_ens'] ?? $prof['nom']) ?>"
                                data-prenom="<?= strtolower($prof['prenom_ens'] ?? $prof['prenom']) ?>"
                                data-email="<?= strtolower($prof['email_ens'] ?? $prof['email'] ?? '') ?>">
                                <td><?= htmlspecialchars($prof['nom_ens'] ?? $prof['nom']) ?></td>
                                <td><?= htmlspecialchars($prof['prenom_ens'] ?? $prof['prenom']) ?></td>
                                <td><?= htmlspecialchars($prof['email_ens'] ?? $prof['email'] ?? 'Non renseigné') ?></td>
                                <td class="actions-cell">
                                    <a href="routeur.php?controleur=ControleurEnseignant&action=afficherFormulaire&id=<?= $prof['id_ens'] ?>"
                                        class="btn-edit">✏️ Modifier</a>

                                    <a href="routeur.php?controleur=ControleurEnseignant&action=supprimer&id=<?= $prof['id_ens'] ?>"
                                        class="btn-delete"
                                        onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet enseignant ?');">🗑️
                                        Supprimer</a>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <tr id="no-results-row">
                            <td colspan="4" style="text-align:center;">Aucun enseignant trouvé.</td>
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
            const teacherRows = document.querySelectorAll('#teacher-table tbody tr[data-nom]');
            const tbody = document.querySelector('#teacher-table tbody');

            searchInput.addEventListener('input', (e) => {
                const searchTerm = e.target.value.toLowerCase().trim();
                let visibleCount = 0;

                teacherRows.forEach(row => {
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
                if (visibleCount === 0 && teacherRows.length > 0) {
                    if (!noResultsRow) {
                        noResultsRow = document.createElement('tr');
                        noResultsRow.id = 'no-results-search';
                        noResultsRow.innerHTML = '<td colspan="4" style="text-align:center;">Aucun enseignant ne correspond à votre recherche.</td>';
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