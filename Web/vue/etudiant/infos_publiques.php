<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Informations Publiques</title>
    <link rel="stylesheet" href="assets/css/animations.css">
    <link rel="stylesheet" href="assets/css/animations.css">

    <link rel="stylesheet" href="assets/css/style.css">
    <?php echo "\n    <link rel=\"stylesheet\" href=\"assets/css/components.css\">\n    <link rel=\"stylesheet\" href=\"assets/css/pages.css\">"; ?>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="top-bar">
        <h1>Annuaire de la Promotion</h1>
        <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-retour">⬅ Retour</a>
    </div>

    <div class="container">
        <!-- Barre de tri et recherche -->
        <div style="display: flex; gap: 15px; margin-bottom: 20px; flex-wrap: wrap;">
            <select id="sort-select"
                style="padding: 12px; border-radius: 8px; border: 1px solid #ddd; font-size: 1rem; width: 220px;">
                <option value="nom">Trier par Nom</option>
                <option value="prenom">Trier par Prénom</option>
                <option value="type">Trier par Type</option>
                <option value="email">Trier par Email</option>
            </select>
            <input type="text" id="search-input" placeholder="🔍 Rechercher une personne (nom, prénom, email, type)..."
                style="flex: 1; min-width: 350px; padding: 12px; border-radius: 8px; border: 1px solid #ddd; font-size: 1rem;">
        </div>

        <div id="liste-etudiants" class="grid-etudiants">
            <p>Chargement...</p>
        </div>
    </div>

    <script>
        // Afficher TOUS les utilisateurs (étudiants + enseignants)
        document.addEventListener('DOMContentLoaded', () => {
            const container = document.getElementById('liste-etudiants');
            const searchInput = document.getElementById('search-input');
            const urlAPI = 'routeur.php?controleur=ControleurAPI&action=listeAnnuaireJSON';
            let allUsers = [];

            fetch(urlAPI)
                .then(res => res.json())
                .then(data => {
                    if (data.error) {
                        container.innerHTML = '<p>Erreur: ' + data.error + '</p>';
                        return;
                    }
                    allUsers = data;
                    renderUsers(allUsers);
                });

            // Fonction pour afficher les utilisateurs
            function renderUsers(users) {
                if (!users || users.length === 0) {
                    container.innerHTML = '<p>Aucune personne trouvée.</p>';
                    return;
                }

                container.innerHTML = users.map(user => {
                    const typeColor = user.type === 'Enseignant' ? '#9b59b6' : '#3498db';
                    const typeIcon = user.type === 'Enseignant' ? '👨‍🏫' : '🎓';

                    return `
                    <div class="card">
                        <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 10px;">
                            <div style="font-size: 40px;">${typeIcon}</div>
                            <div style="flex: 1;">
                                <h3 style="margin: 0;">${user.prenom} ${user.nom}</h3>
                                <span style="display: inline-block; background: ${typeColor}; color: white; padding: 3px 10px; border-radius: 12px; font-size: 0.75rem; margin-top: 5px;">
                                    ${user.typeDetail}
                                </span>
                            </div>
                        </div>
                        <p style="margin: 5px 0; color: #555;">📧 ${user.email}</p>
                        <div style="margin-top:10px; font-size:0.8rem; color:#aaa;">
                            ${user.genre === 'F' ? 'Femme' : 'Homme'}
                            ${user.moyenne ? ' • Moyenne: ' + parseFloat(user.moyenne).toFixed(2) + '/20' : ''}
                        </div>
                    </div>`;
                }).join('');
            }

            // Filtrage en temps réel
            searchInput.addEventListener('input', (e) => {
                const searchTerm = e.target.value.toLowerCase().trim();

                if (searchTerm === '') {
                    renderUsers(allUsers);
                    return;
                }

                const filtered = allUsers.filter(user => {
                    const nom = (user.nom || '').toLowerCase();
                    const prenom = (user.prenom || '').toLowerCase();
                    const email = (user.email || '').toLowerCase();
                    const type = (user.typeDetail || '').toLowerCase();

                    return nom.includes(searchTerm) ||
                        prenom.includes(searchTerm) ||
                        email.includes(searchTerm) ||
                        type.includes(searchTerm);
                });

                renderUsers(filtered);
            });

            // Tri
            const sortSelect = document.getElementById('sort-select');
            sortSelect.addEventListener('change', (e) => {
                const sortBy = e.target.value;
                const sorted = [...allUsers].sort((a, b) => {
                    const valA = (a[sortBy] || '').toString().toLowerCase();
                    const valB = (b[sortBy] || '').toString().toLowerCase();
                    return valA.localeCompare(valB);
                });
                allUsers = sorted;
                renderUsers(allUsers);
            });
        });
    </script>
    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>