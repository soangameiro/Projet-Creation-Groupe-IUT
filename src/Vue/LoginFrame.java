package Vue;

import Controleur.MainController;
import javax.swing.*;
import java.awt.*;

/**
 * ========================================================
 * LOGINFRAME.JAVA - Fenêtre de connexion
 * ========================================================
 * 
 * C'est la PREMIERE fenêtre que l'utilisateur voit.
 * Elle permet de :
 * - Saisir son email et mot de passe
 * - Se connecter à l'application
 * - Accéder au tableau de bord correspondant à son rôle
 * 
 * STRUCTURE DE SWING (bibliothèque graphique Java) :
 * - JFrame : la fenêtre principale
 * - JPanel : un conteneur pour regrouper des composants
 * - JLabel : du texte affiché
 * - JTextField : une zone de saisie
 * - JButton : un bouton cliquable
 * 
 * @author SAE S3 - Groupe
 */
public class LoginFrame extends JFrame {

    // =================================================================
    // ATTRIBUTS
    // =================================================================
    
    /** Le contrôleur qui gère la logique de connexion */
    private final MainController controleur;
    
    /** Champ de saisie pour l'email */
    private JTextField champEmail;
    
    /** Champ de saisie pour le mot de passe (masqué avec des points) */
    private JPasswordField champMotDePasse;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================
    
    /**
     * Crée la fenêtre de connexion.
     */
    public LoginFrame() {
        this.controleur = new MainController();
        initialiserComposants();
    }

    // =================================================================
    // INITIALISATION DE L'INTERFACE
    // =================================================================
    
    /**
     * Configure tous les éléments visuels de la fenêtre.
     */
    private void initialiserComposants() {
        // --- Configuration de la fenêtre ---
        setTitle("Gestion des Groupes - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer = quitter l'appli
        setSize(450, 500);                              // Largeur x Hauteur en pixels
        setLocationRelativeTo(null);                    // Centrer sur l'écran
        setResizable(false);                            // Empêcher le redimensionnement

        // --- Panneau principal ---
        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS)); // Empiler verticalement
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));   // Marges
        panneauPrincipal.setBackground(Theme.BON_JOUR);

        // --- Logo ou Titre ---
        ajouterLogoOuTitre(panneauPrincipal);
        
        // --- Sous-titre ---
        JLabel sousTitre = new JLabel("Authentification");
        sousTitre.setFont(Theme.TITLE_FONT.deriveFont(20f)); // Taille 20 pixels
        sousTitre.setForeground(Theme.COD_GRAY);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauPrincipal.add(sousTitre);
        
        // Espace vertical
        panneauPrincipal.add(Box.createVerticalStrut(30)); // 30 pixels d'espace

        // --- Formulaire de connexion ---
        JPanel panneauFormulaire = creerFormulaireConnexion();
        panneauPrincipal.add(panneauFormulaire);
        
        panneauPrincipal.add(Box.createVerticalStrut(30));

        // --- Bouton de connexion ---
        JButton boutonConnexion = ButtonFactory.principal("SE CONNECTER");
        boutonConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonConnexion.setPreferredSize(new Dimension(200, 45));
        boutonConnexion.setMaximumSize(new Dimension(200, 45));
        
        // Que faire quand on clique sur le bouton ?
        // -> Appeler la méthode gererConnexion()
        boutonConnexion.addActionListener(evenement -> gererConnexion());
        
        panneauPrincipal.add(boutonConnexion);

        // Ajouter le panneau à la fenêtre
        add(panneauPrincipal);
    }
    
    /**
     * Ajoute le logo de l'application ou un titre de remplacement.
     */
    private void ajouterLogoOuTitre(JPanel panneau) {
        try {
            // Chercher le fichier logo.png dans le même dossier que cette classe
            java.net.URL urlLogo = getClass().getResource("logo.png");
            
            if (urlLogo != null) {
                // Charger et redimensionner l'image
                ImageIcon iconeOriginale = new ImageIcon(urlLogo);
                Image image = iconeOriginale.getImage();
                Image imageRedim = image.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
                
                JLabel labelLogo = new JLabel(new ImageIcon(imageRedim));
                labelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
                panneau.add(labelLogo);
                panneau.add(Box.createVerticalStrut(20));
            } else {
                // Si pas de logo, afficher un titre texte
                JLabel titre = new JLabel("IUT GESTION");
                titre.setFont(Theme.TITLE_FONT);
                titre.setForeground(Theme.PAPRIKA);
                titre.setAlignmentX(Component.CENTER_ALIGNMENT);
                panneau.add(titre);
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement logo : " + e.getMessage());
        }
    }
    
    /**
     * Crée le formulaire avec les champs email et mot de passe.
     */
    private JPanel creerFormulaireConnexion() {
        JPanel panneau = new JPanel();
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
        panneau.setBackground(Theme.BON_JOUR);
        panneau.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- Champ Email ---
        JLabel labelEmail = new JLabel("Email Universitaire");
        labelEmail.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 12f));
        labelEmail.setForeground(Theme.SCORPION);
        labelEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        panneau.add(labelEmail);
        
        panneau.add(Box.createVerticalStrut(5));
        
        champEmail = new JTextField();
        champEmail.setFont(Theme.BODY_FONT);
        champEmail.setPreferredSize(new Dimension(300, 35));
        champEmail.setMaximumSize(new Dimension(300, 35));
        champEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        panneau.add(champEmail);
        
        panneau.add(Box.createVerticalStrut(15));
        
        // --- Champ Mot de passe ---
        JLabel labelMdp = new JLabel("Mot de passe");
        labelMdp.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 12f));
        labelMdp.setForeground(Theme.SCORPION);
        labelMdp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panneau.add(labelMdp);
        
        panneau.add(Box.createVerticalStrut(5));
        
        champMotDePasse = new JPasswordField();
        champMotDePasse.setPreferredSize(new Dimension(300, 35));
        champMotDePasse.setMaximumSize(new Dimension(300, 35));
        champMotDePasse.setAlignmentX(Component.LEFT_ALIGNMENT);
        panneau.add(champMotDePasse);
        
        return panneau;
    }

    // =================================================================
    // LOGIQUE DE CONNEXION
    // =================================================================
    
    /**
     * Gère le clic sur le bouton "SE CONNECTER".
     */
    private void gererConnexion() {
        // Récupérer les valeurs saisies
        String email = champEmail.getText().trim(); // trim() enlève les espaces
        String motDePasse = new String(champMotDePasse.getPassword());

        // Vérifier que les champs ne sont pas vides
        if (email.isEmpty() || motDePasse.isEmpty()) {
            // Afficher un message d'erreur
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs.", 
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return; // Sortir de la méthode
        }

        // Tenter la connexion via le contrôleur
        if (controleur.login(email, motDePasse)) {
            // Connexion réussie !
            // SwingUtilities.invokeLater() = exécuter dans le thread graphique
            SwingUtilities.invokeLater(() -> {
                ouvrirTableauDeBord();
                this.dispose(); // Fermer cette fenêtre
            });
        } else {
            // Echec de connexion
            JOptionPane.showMessageDialog(this,
                "Identifiants incorrects.",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            champMotDePasse.setText(""); // Vider le champ mot de passe
        }
    }
    
    /**
     * Ouvre le tableau de bord correspondant au rôle de l'utilisateur.
     */
    private void ouvrirTableauDeBord() {
        String role = controleur.getCurrentUserRole();

        // Selon le rôle, ouvrir une fenêtre différente
        switch (role) {
            case MainController.ROLE_ETUDIANT:
                new DashboardEtudiant(controleur).setVisible(true);
                break;
                
            case MainController.ROLE_ENSEIGNANT:
                new DashboardEnseignant(controleur).setVisible(true);
                break;
                
            case MainController.ROLE_RESP_FILIERE:
                new DashboardRespFiliere(controleur).setVisible(true);
                break;
                
            case MainController.ROLE_RESP_FORMATION:
                new DashboardRespFormation(controleur).setVisible(true);
                break;
                
            default:
                System.out.println("Rôle inconnu : " + role);
        }
    }

    // =================================================================
    // POINT D'ENTREE DE L'APPLICATION
    // =================================================================
    
    /**
     * Méthode main : c'est ici que l'application démarre.
     */
    public static void main(String[] args) {
        // 1. Appliquer le thème visuel
        Theme.setup();
        
        // 2. Créer et afficher la fenêtre de connexion
        // invokeLater() = respecter les règles de Swing pour les threads
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
