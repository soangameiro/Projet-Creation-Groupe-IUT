
<header class="site-header">
    <div class="container header-inner">
        <?php
        // Le logo redirige vers le dashboard (qui affiche le bon accueil selon le rôle)
        $accueilUrl = "routeur.php?controleur=ControleurConnexion&action=dashboard";
        ?>
        <a href="<?php echo $accueilUrl; ?>" class="header-brand">
            <img src="assets/logo.png" alt="Logo IUT" class="header-logo">
            <div>
                <div class="header-title">IUT d'Orsay</div>
                <span class="header-subtitle">Gestion des Groupes S301</span>
            </div>
        </a>

        <?php if (isset($userInfo)): ?>
            <div class="header-user-info">
                <div class="user-name">
                    <?php echo $userInfo->getPrenom() . ' ' . $userInfo->getNom(); ?>
                    <span class="user-role">
                        <?php
                        // Détecter si c'est un Enseignant ou un Étudiant
                        if (get_class($userInfo) === 'Enseignant') {
                            $role = $userInfo->getRole();
                            if ($role == 2) {
                                echo "RESPONSABLE DE FILIÈRE";
                            } elseif ($role == 3) {
                                echo "RESPONSABLE DE FORMATION";
                            } else {
                                echo "ENSEIGNANT";
                            }
                        } else {
                            echo "ÉTUDIANT";
                        }
                        ?>
                    </span>
                </div>
                <a href="routeur.php?controleur=ControleurConnexion&action=deconnecter" class="btn-logout">
                    Déconnexion
                </a>
            </div>
        <?php endif; ?>
    </div>
</header>

<!-- Premium scroll behavior script -->
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const header = document.querySelector('.site-header');
        let lastScroll = 0;

        window.addEventListener('scroll', () => {
            const currentScroll = window.pageYOffset;

            if (currentScroll > 50) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }

            lastScroll = currentScroll;
        });
    });
</script>

<main class="page-content">