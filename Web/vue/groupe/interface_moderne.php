<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Gestion des Étudiants</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <link rel="stylesheet" href="assets/css/animations.css">

    <style>
        /* CSS Spécifique animation reste ici */
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .card-anim {
            animation: slideIn 0.4s ease-out forwards;
            opacity: 0;
        }

        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid var(--bon-jour);
        }

        .sort-select {
            padding: 10px;
            border-radius: 8px;
            border: 1px solid var(--mobster);
        }
    </style>
</head>

<body>

    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">

        <div class="toolbar">
            <div>
                <h1>Gestion des Étudiants</h1>
                <p style="color: var(--scorpion); margin: 0;">Promotion BUT Informatique</p>
            </div>

            <div style="display: flex; gap: 15px; align-items: center;">
                <!-- Barre de recherche -->
                <div>
                    <input type="text" id="search-input" placeholder="🔍 Rechercher..."
                        style="padding: 10px; border-radius: 8px; border: 1px solid var(--mobster); min-width: 250px;">
                </div>
                <div>
                    <select id="tri-select" class="sort-select">
                        <option value="nom">Nom (A-Z)</option>
                        <option value="moyenne_desc">Moyenne (Décroissant)</option>
                        <option value="moyenne_asc">Moyenne (Croissant)</option>
                    </select>
                </div>
                <a href="routeur.php?controleur=ControleurGestion&action=afficherFormulaireAjout" class="btn-action">
                    <span>➕</span> Ajouter
                </a>
                <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn-logout">
                    Retour
                </a>
            </div>
        </div>

        <div id="liste-etudiants" class="grid-etudiants">
            <p>Chargement...</p>
        </div>

    </div>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const container = document.getElementById('liste-etudiants');
            const selectTri = document.getElementById('tri-select');
            const searchInput = document.getElementById('search-input');
            let etudiantsData = [];
            let filteredData = [];

            fetch('routeur.php?controleur=ControleurAPI&action=listeEtudiantsJSON')
                .then(res => res.json())
                .then(data => {
                    etudiantsData = data;
                    filteredData = data;
                    renderEtudiants(filteredData);
                });

            // Filtrage par recherche
            searchInput.addEventListener('input', (e) => {
                const searchTerm = e.target.value.toLowerCase().trim();

                if (searchTerm === '') {
                    filteredData = etudiantsData;
                } else {
                    filteredData = etudiantsData.filter(etu => {
                        const nom = (etu.nom || '').toLowerCase();
                        const prenom = (etu.prenom || '').toLowerCase();
                        const email = (etu.email || '').toLowerCase();

                        return nom.includes(searchTerm) ||
                            prenom.includes(searchTerm) ||
                            email.includes(searchTerm);
                    });
                }

                // Appliquer le tri actuel
                applySortAndRender();
            });

            // Tri
            selectTri.addEventListener('change', () => {
                applySortAndRender();
            });

            function applySortAndRender() {
                const c = selectTri.value;
                let sorted = [...filteredData];
                if (c === 'nom') sorted.sort((a, b) => a.nom.localeCompare(b.nom));
                if (c === 'moyenne_desc') sorted.sort((a, b) => (b.moyenne || 0) - (a.moyenne || 0));
                if (c === 'moyenne_asc') sorted.sort((a, b) => (a.moyenne || 0) - (b.moyenne || 0));
                renderEtudiants(sorted);
            }
            function renderEtudiants(liste) {
                container.innerHTML = '';
                liste.forEach((etu, i) => {
                    const moy = parseFloat(etu.moyenne || 0);
                    let gradeClass = moy >= 12 ? 'grade-good' : (moy < 10 ? 'grade-bad' : 'grade-avg');
                    const initial = etu.prenom ? etu.prenom.charAt(0) : '?';

                    const html = `
                <div class="card card-anim" style="animation-delay: ${i * 0.05}s">
                    <div class="grade-badge ${gradeClass}">${moy.toFixed(2)}/20</div>
                    <div class="card-header">
                        <div class="avatar-circle">${initial}</div>
                        <div class="card-info">
                            <h3>${etu.prenom} ${etu.nom}</h3>
                            <span class="email">${etu.email}</span>
                        </div>
                    </div>
                    <div class="tags">
                        <span class="tag">${etu.bac || '-'}</span>
                        <span class="tag">${etu.genre === 'F' ? 'Femme' : 'Homme'}</span>
                    </div>
                    <div class="card-actions">
                        <a href="routeur.php?controleur=ControleurGestion&action=afficherFormulaireModif&id=${etu.id}" class="btn-card btn-edit">✏️ Éditer</a>
                        <a href="routeur.php?controleur=ControleurGestion&action=traiterSuppression&id=${etu.id}" class="btn-card btn-del" onclick="return confirm('Sûr ?');">🗑️ Supprimer</a>
                    </div>
                </div>`;
                    container.innerHTML += html;
                });
            }
        });
    </script>

    <?php require_once "vue/commun/footer.php"; ?>

</body>

</html>