document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('liste-etudiants');
    
    // Fonction pour créer une carte HTML
    const createCard = (etudiant, index) => {
        // Calcul couleur moyenne
        const colorMoyenne = etudiant.moyenne >= 10 ? '#34d399' : '#f87171';
        
        // Délai animation (effet cascade)
        const delay = index * 100; 

        return `
            <div class="card" style="animation-delay: ${delay}ms">
                <div class="card-header">
                    <div class="avatar">${etudiant.nom.charAt(0)}</div>
                    <div class="moyenne-badge" style="color: ${colorMoyenne}; border: 1px solid ${colorMoyenne}40">
                        ${etudiant.moyenne}/20
                    </div>
                </div>
                <h3>${etudiant.prenom} ${etudiant.nom}</h3>
                <p>${etudiant.email}</p>
                
                <div class="tags">
                    <span class="tag">🎓 ${etudiant.bac || 'Non renseigné'}</span>
                    <span class="tag">${etudiant.genre === 'F' ? '👩 Femme' : '👨 Homme'}</span>
                </div>
            </div>
        `;
    };

    // Appel AJAX vers ton PHP
    fetch('routeur.php?controleur=ControleurAPI&action=listeEtudiantsJSON')
        .then(response => response.json())
        .then(data => {
            if(data.length === 0) {
                container.innerHTML = '<p style="color:white">Aucun étudiant trouvé.</p>';
                return;
            }
            
            // Génération du HTML
            container.innerHTML = data.map((etu, i) => createCard(etu, i)).join('');
        })
        .catch(error => {
            console.error('Erreur:', error);
            container.innerHTML = '<p style="color:red">Erreur de chargement des données.</p>';
        });
});