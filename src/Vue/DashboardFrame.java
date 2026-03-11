package Vue;

import Controleur.MainController;
import Utilisateur.Etudiant;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ========================================================
 * DASHBOARDFRAME.JAVA - Tableau de bord générique
 * ========================================================
 * 
 * Cette classe est un TABLEAU DE BORD de démonstration.
 * Elle sert de base pour comprendre comment créer un dashboard.
 * 
 * En pratique, on utilise plutôt les dashboards spécifiques :
 * - DashboardEtudiant : pour les étudiants
 * - DashboardEnseignant : pour les enseignants
 * - DashboardRespFiliere : pour les responsables de filière
 * - DashboardRespFormation : pour les responsables de formation
 * 
 * STRUCTURE D'UN DASHBOARD :
 * - Barre de menu (déconnexion, navigation)
 * - Panneau supérieur (bienvenue, sélecteurs)
 * - Tableau central (données)
 * - Panneau inférieur (statistiques)
 * 
 * @author SAE S3 - Groupe
 */
public class DashboardFrame extends JFrame {

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    /** Le contrôleur principal */
    private final MainController controleur;

    /** Nom de l'utilisateur connecté */
    private final String nomUtilisateur;

    /** Tableau affichant les données */
    private JTable tableauEtudiants;

    /** Modèle de données du tableau */
    private DefaultTableModel modeleTableau;

    /** Sélecteur de promotion */
    private JComboBox<String> listePromotions;

    /** Label pour les statistiques */
    private JLabel labelStatistiques;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    public DashboardFrame(MainController controleur, String nomUtilisateur) {
        this.controleur = controleur;
        this.nomUtilisateur = nomUtilisateur;
        initialiserComposants();
        chargerDonnees();
    }

    // =================================================================
    // INITIALISATION
    // =================================================================

    private void initialiserComposants() {
        setTitle("Gestion des Groupes - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // --- Barre de menu ---
        JMenuBar barreMenu = new JMenuBar();

        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemDeconnexion = new JMenuItem("Déconnexion");
        itemDeconnexion.addActionListener(e -> deconnexion());
        menuFichier.add(itemDeconnexion);
        barreMenu.add(menuFichier);

        // Menu Gestion
        JMenu menuGestion = new JMenu("Gestion");
        JMenuItem itemEnseignants = new JMenuItem("Enseignants");
        itemEnseignants.addActionListener(e -> {
            new GestionEnseignantsFrame(controleur).setVisible(true);
        });
        menuGestion.add(itemEnseignants);

        JMenuItem itemEtudiants = new JMenuItem("Étudiants par Promo");
        itemEtudiants.addActionListener(e -> {
            new GestionEtudiantsPromoFrame(controleur).setVisible(true);
        });
        menuGestion.add(itemEtudiants);
        barreMenu.add(menuGestion);

        // Menu Outils
        JMenu menuOutils = new JMenu("Outils");
        JMenuItem itemAlgo = new JMenuItem("Algorithme Groupes");
        itemAlgo.addActionListener(e -> {
            new AlgorithmeGroupesFrame(controleur).setVisible(true);
        });
        menuOutils.add(itemAlgo);

        JMenuItem itemPortail = new JMenuItem("Portail Étudiant");
        itemPortail.addActionListener(e -> {
            new PortailEtudiantFrame(controleur, 2023001).setVisible(true);
        });
        menuOutils.add(itemPortail);
        barreMenu.add(menuOutils);

        setJMenuBar(barreMenu);

        // --- Panneau supérieur : Message de bienvenue + sélecteur ---
        JPanel panneauHaut = new JPanel(new BorderLayout());
        panneauHaut.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelBienvenue = new JLabel("Bienvenue, " + nomUtilisateur);
        labelBienvenue.setFont(new Font("Arial", Font.BOLD, 14));
        panneauHaut.add(labelBienvenue, BorderLayout.WEST);

        // Sélecteur de promotion
        JPanel panneauPromo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panneauPromo.add(new JLabel("Promotion:"));
        listePromotions = new JComboBox<>(new String[] {
                "BUT2 Info 2024-2025",
                "BUT3 Alterne 2024-2025"
        });
        listePromotions.addActionListener(e -> chargerDonnees());
        panneauPromo.add(listePromotions);
        panneauHaut.add(panneauPromo, BorderLayout.EAST);

        add(panneauHaut, BorderLayout.NORTH);

        // --- Tableau central : Liste des étudiants ---
        String[] colonnes = { "ID", "Nom", "Prénom", "Email", "Moyenne", "Genre" };
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Lecture seule
            }
        };
        tableauEtudiants = new JTable(modeleTableau);
        tableauEtudiants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableauEtudiants.setRowHeight(25);
        add(new JScrollPane(tableauEtudiants), BorderLayout.CENTER);

        // --- Panneau inférieur : Statistiques ---
        JPanel panneauBas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panneauBas.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        labelStatistiques = new JLabel("Chargement...");
        panneauBas.add(labelStatistiques);
        add(panneauBas, BorderLayout.SOUTH);
    }

    // =================================================================
    // CHARGEMENT DES DONNEES
    // =================================================================

    /**
     * Charge les données à afficher dans le tableau.
     * 
     * NOTE : Pour l'instant, utilise des données de test.
     * En production, il faudrait appeler l'API via le contrôleur.
     */
    private void chargerDonnees() {
        modeleTableau.setRowCount(0);

        // Données de test (à remplacer par un appel API)
        Object[][] donneesTest = {
                { 1001, "Dupont", "Jean", "jean.dupont@univ.fr", 14.5, "M" },
                { 1002, "Martin", "Alice", "alice.martin@univ.fr", 16.2, "F" },
                { 1003, "Bernard", "Lucas", "lucas.bernard@univ.fr", 11.0, "M" }
        };

        for (Object[] ligne : donneesTest) {
            modeleTableau.addRow(ligne);
        }

        labelStatistiques.setText("Nombre d'étudiants: " + modeleTableau.getRowCount());
    }

    // =================================================================
    // DECONNEXION
    // =================================================================

    /**
     * Gère la déconnexion de l'utilisateur.
     */
    private void deconnexion() {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
                this.dispose();
            });
        }
    }
}
