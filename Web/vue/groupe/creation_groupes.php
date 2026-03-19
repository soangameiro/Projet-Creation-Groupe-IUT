<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Création de Groupes</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/pages.css">
    <style>
        /* Layout sidebar + contenu */
        .algo-layout {
            display: grid;
            grid-template-columns: 320px 1fr;
            gap: 20px;
            margin-top: 20px;
        }

        /* Sidebar */
        .algo-sidebar {
            background: var(--secondary, #2c3e50);
            padding: 20px;
            border-radius: 10px;
            color: white;
            height: fit-content;
            position: sticky;
            top: 20px;
        }

        .algo-sidebar h3 {
            color: #ecf0f1;
            font-size: 14px;
            letter-spacing: 1px;
            margin-bottom: 20px;
            text-transform: uppercase;
        }

        .param-box {
            background: rgba(255, 255, 255, 0.05);
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 12px;
        }

        .param-box label {
            display: block;
            color: #bdc3c7;
            margin-bottom: 6px;
            font-size: 13px;
        }

        .param-box select,
        .param-box input {
            width: 100%;
            padding: 8px;
            border: none;
            border-radius: 5px;
        }

        .algo-btn {
            width: 100%;
            padding: 12px;
            margin-bottom: 8px;
            border: none;
            border-radius: 5px;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s;
        }

        .algo-btn:hover {
            transform: translateY(-2px);
        }

        .score-box {
            background: rgba(255, 255, 255, 0.1);
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            margin: 15px 0;
        }

        .score-box h3 {
            font-size: 20px;
            color: #f39c12;
            margin: 0;
        }

        /* Tableau résultats */
        .result-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        .result-table thead {
            background: var(--secondary, #2c3e50);
            color: white;
        }

        .result-table th {
            padding: 12px;
            text-align: left;
            font-weight: 600;
        }

        .result-table td {
            padding: 10px 12px;
            border-bottom: 1px solid #ecf0f1;
        }

        .result-table tbody tr:hover {
            background: #f8f9fa;
        }

        .groupe-badge {
            display: inline-block;
            padding: 4px 10px;
            background: var(--primary, #3498db);
            color: white;
            border-radius: 12px;
            font-size: 12px;
            font-weight: bold;
        }

        /* Modal statistiques */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
        }

        .modal-content {
            background: white;
            margin: 5% auto;
            padding: 30px;
            border-radius: 10px;
            width: 80%;
            max-width: 600px;
            max-height: 80vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #ecf0f1;
        }

        .close-modal {
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            color: #95a5a6;
        }

        .stat-group {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .stat-group h4 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .stat-line {
            display: flex;
            justify-content: space-between;
            padding: 5px 0;
            font-size: 14px;
        }

        @media (max-width: 900px) {
            .algo-layout {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>
    <?php require_once "vue/commun/header.php"; ?>

    <div class="container">
        <div
            style="border-bottom: 1px solid var(--bon-jour); padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-start;">
            <div>
                <h1>🎓 Création de Groupes - Algorithmes Avancés</h1>
                <p style="color: var(--scorpion); margin: 0;">Gérez les groupes TD/TP de votre promotion avec 6
                    algorithmes
                    intelligents.</p>
            </div>
            <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary">
                ← Retour Tableau de Bord
            </a>
        </div>

        <div class="algo-layout">
            <!-- SIDEBAR -->
            <div class="algo-sidebar">
                <h3>PARAMÈTRES</h3>

                <div class="param-box">
                    <label for="promotion">Promotion :</label>
                    <select id="promotion" onchange="chargerEtudiants()">
                        <?php foreach ($promotions as $p): ?>
                            <option value="<?php echo $p->getId(); ?>" <?php echo ($p->getId() == $id_promo) ? 'selected' : ''; ?>>
                                <?php echo htmlspecialchars($p->getNom()); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>

                <div class="param-box">
                    <label for="nb-groupes">Nombre de groupes :</label>
                    <input type="number" id="nb-groupes" value="4" min="1" max="20">
                </div>

                <div class="param-box">
                    <label for="methode">Méthode :</label>
                    <select id="methode">
                        <option value="glouton_moyenne">Glouton Moyenne (Base)</option>
                        <option value="glouton_bac">Glouton Bac (Base)</option>
                        <option value="aleatoire">Aléatoire (Base)</option>
                        <option value="glouton1">Glouton 1 - Utilité (Expert)</option>
                        <option value="glouton2">Glouton 2 - Vagues (Expert)</option>
                        <option value="optimal">🎯 Algorithme Optimal (Auto)</option>
                    </select>
                </div>

                <div style="margin: 25px 0;">
                    <button class="algo-btn btn-primary" style="background: var(--primary);"
                        onclick="lancerAlgorithme()">1. LANCER ALGO</button>
                    <button class="algo-btn btn-success" onclick="sauvegarderGroupes()">2. Sauvegarder BDD</button>
                    <button class="algo-btn btn-primary" onclick="afficherStatistiques()">3. Voir Statistiques</button>
                </div>

                <div class="score-box">
                    <h3 id="score-label">Score: ---</h3>
                </div>

                <button class="algo-btn btn-danger" onclick="toutEffacer()">🗑️ Tout Effacer en BDD</button>

                <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.1);">
                    <a href="routeur.php?controleur=ControleurConnexion&action=dashboard" class="btn btn-secondary"
                        style="width: 100%; display: block; text-align: center; color: white; text-decoration: none; padding: 10px;">
                        ← Retour Tableau de Bord
                    </a>
                </div>
            </div>

            <!-- CONTENU PRINCIPAL -->
            <div>
                <div id="status-message" class="card"
                    style="display: none; margin-bottom: 20px; padding: 15px; border-left: 4px solid var(--primary);">
                    Prêt.
                </div>

                <div class="card">
                    <h2>Résultats de la Répartition</h2>
                    <table class="result-table" id="result-table">
                        <thead>
                            <tr>
                                <th>Groupe</th>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Prénom</th>
                                <th>Bac</th>
                                <th>Moyenne</th>
                                <th>Genre</th>
                            </tr>
                        </thead>
                        <tbody id="result-tbody">
                            <tr>
                                <td colspan="7" style="text-align: center; padding: 40px; color: #95a5a6;">
                                    Chargez les étudiants et lancez l'algorithme pour voir les résultats.
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Statistiques -->
    <div id="stats-modal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Statistiques Détaillées</h2>
                <span class="close-modal" onclick="fermerStats()">&times;</span>
            </div>
            <div id="stats-content"></div>
        </div>
    </div>

    <script>
        let etudiantsCharges = [];
        let groupesCrees = [];
        let algorithmeChoisi = null;

        // Charger automatiquement au démarrage
        document.addEventListener('DOMContentLoaded', () => {
            chargerEtudiants();
        });

        function showStatus(message, success = false) {
            const statusEl = document.getElementById('status-message');
            statusEl.textContent = message;
            statusEl.style.display = 'block';
            statusEl.style.borderLeftColor = success ? 'var(--success, #27ae60)' : 'var(--primary, #3498db)';
            statusEl.style.background = success ? '#d5f4e6' : 'white';
        }

        function chargerEtudiants() {
            showStatus('Chargement en cours...');
            const promoId = document.getElementById('promotion').value;

            fetch(`routeur.php?controleur=ControleurAPI&action=listeEtudiantsJSON&promo=${promoId}`)
                .then(res => res.json())
                .then(data => {
                    etudiantsCharges = data;
                    showStatus(`${data.length} étudiants chargés.`, true);
                    afficherEtudiantsSansGroupe();
                })
                .catch(err => {
                    showStatus('Erreur lors du chargement.', false);
                    console.error(err);
                });
        }

        function afficherEtudiantsSansGroupe() {
            const tbody = document.getElementById('result-tbody');
            tbody.innerHTML = '';

            if (etudiantsCharges.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 40px; color: #95a5a6;">Aucun étudiant chargé.</td></tr>';
                return;
            }

            etudiantsCharges.forEach(etu => {
                const row = `
                    <tr>
                        <td><span class="groupe-badge" style="background: #95a5a6;">-</span></td>
                        <td>${etu.id}</td>
                        <td>${etu.nom}</td>
                        <td>${etu.prenom}</td>
                        <td>${etu.bac || 'N/A'}</td>
                        <td>${etu.moyenne ? parseFloat(etu.moyenne).toFixed(2) : 'N/A'}</td>
                        <td>${etu.genre === 'F' ? 'Femme' : 'Homme'}</td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });
        }

        function lancerAlgorithme() {
            if (etudiantsCharges.length === 0) {
                alert('Chargez d\'abord les étudiants !');
                return;
            }

            showStatus('Calcul en cours...');
            const nbGroupes = document.getElementById('nb-groupes').value;
            const methode = document.getElementById('methode').value;
            const promoId = document.getElementById('promotion').value;

            const formData = new FormData();
            formData.append('id_promo', promoId);
            formData.append('nb_groupes', nbGroupes);
            formData.append('methode', methode);

            fetch('routeur.php?controleur=ControleurGroupe&action=executerAlgorithme', {
                method: 'POST',
                body: formData
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        groupesCrees = data.groupes;
                        algorithmeChoisi = data.algorithmeChoisi;
                        afficherResultats(data.groupes);
                        document.getElementById('score-label').textContent = `Score: ${data.score.toFixed(1)}`;

                        let msg = `Terminé en ${data.temps}ms`;
                        if (algorithmeChoisi) {
                            msg = `✓ Algorithme optimal sélectionné: ${algorithmeChoisi} (${data.temps}ms)`;
                        }
                        showStatus(msg, true);
                    } else {
                        showStatus('Erreur: ' + (data.error || 'Erreur inconnue'), false);
                    }
                })
                .catch(err => {
                    showStatus('Erreur lors de l\'exécution.', false);
                    console.error(err);
                });
        }

        function afficherResultats(groupes) {
            const tbody = document.getElementById('result-tbody');
            tbody.innerHTML = '';

            const colors = ['#3498db', '#e74c3c', '#f39c12', '#9b59b6', '#1abc9c', '#34495e', '#e67e22', '#16a085'];

            groupes.forEach((groupe, idx) => {
                groupe.etudiants.forEach(etu => {
                    const row = `
                        <tr>
                            <td><span class="groupe-badge" style="background: ${colors[idx % colors.length]};">${groupe.nom}</span></td>
                            <td>${etu.id}</td>
                            <td>${etu.nom}</td>
                            <td>${etu.prenom}</td>
                            <td>${etu.bac || 'N/A'}</td>
                            <td>${etu.moyenne ? parseFloat(etu.moyenne).toFixed(2) : 'N/A'}</td>
                            <td>${etu.genre === 'F' ? 'Femme' : 'Homme'}</td>
                        </tr>
                    `;
                    tbody.innerHTML += row;
                });
            });
        }

        function sauvegarderGroupes() {
            if (groupesCrees.length === 0) {
                alert('Lancez d\'abord l\'algorithme !');
                return;
            }

            showStatus('Sauvegarde en cours...');
            const promoId = document.getElementById('promotion').value;

            const formData = new FormData();
            formData.append('id_promo', promoId);
            formData.append('groupes', JSON.stringify(groupesCrees));

            fetch('routeur.php?controleur=ControleurGroupe&action=sauvegarderResultats', {
                method: 'POST',
                body: formData
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        showStatus(`✓ Sauvegardé : ${data.nbGroupes} groupes, ${data.nbAffectations} affectations`, true);
                    } else {
                        showStatus('Erreur lors de la sauvegarde.', false);
                    }
                })
                .catch(err => {
                    showStatus('Erreur lors de la sauvegarde.', false);
                    console.error(err);
                });
        }

        function afficherStatistiques() {
            if (groupesCrees.length === 0) {
                alert('Lancez d\'abord l\'algorithme !');
                return;
            }

            const statsContent = document.getElementById('stats-content');
            statsContent.innerHTML = '<h3>Statistiques par Groupe</h3>';

            groupesCrees.forEach(groupe => {
                const stats = calculerStats(groupe);
                const html = `
                    <div class="stat-group">
                        <h4>${groupe.nom} (${groupe.etudiants.length} étudiants)</h4>
                        <div class="stat-line"><span>Moyenne :</span><span>${stats.moyenne.toFixed(2)}/20</span></div>
                        <div class="stat-line"><span>Hommes :</span><span>${stats.hommes}</span></div>
                        <div class="stat-line"><span>Femmes :</span><span>${stats.femmes}</span></div>
                        <div class="stat-line"><span>Bac Général :</span><span>${stats.bacGeneral}</span></div>
                        <div class="stat-line"><span>Bac Techno :</span><span>${stats.bacTechno}</span></div>
                    </div>
                `;
                statsContent.innerHTML += html;
            });

            document.getElementById('stats-modal').style.display = 'block';
        }

        function calculerStats(groupe) {
            let somme = 0, hommes = 0, femmes = 0, bacG = 0, bacT = 0;
            groupe.etudiants.forEach(etu => {
                somme += parseFloat(etu.moyenne || 0);
                if (etu.genre === 'H') hommes++;
                else femmes++;
                if (etu.bac === 'General') bacG++;
                else bacT++;
            });
            return {
                moyenne: groupe.etudiants.length > 0 ? somme / groupe.etudiants.length : 0,
                hommes,
                femmes,
                bacGeneral: bacG,
                bacTechno: bacT
            };
        }

        function fermerStats() {
            document.getElementById('stats-modal').style.display = 'none';
        }

        function toutEffacer() {
            if (!confirm('Voulez-vous vraiment tout effacer en BDD ?')) return;

            showStatus('Suppression en cours...');
            const promoId = document.getElementById('promotion').value;

            fetch(`routeur.php?controleur=ControleurGroupe&action=reinitialiser&promo=${promoId}`)
                .then(() => {
                    showStatus('Tout a été effacé.', true);
                    groupesCrees = [];
                    document.getElementById('score-label').textContent = 'Score: ---';
                    afficherEtudiantsSansGroupe();
                })
                .catch(err => {
                    showStatus('Erreur lors de la suppression.', false);
                    console.error(err);
                });
        }

        // Fermer modal en cliquant en dehors
        window.onclick = function (event) {
            const modal = document.getElementById('stats-modal');
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    </script>

    <?php require_once "vue/commun/footer.php"; ?>
</body>

</html>