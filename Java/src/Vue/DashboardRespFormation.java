package Vue;

import Controleur.MainController;
import Infrastructure.RestService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardRespFormation extends JFrame {
    private final MainController controller;
    private final RestService api;
    private DefaultTableModel tableEnseignants;
    private DefaultTableModel tableEtudiants;
    private JTable jtableEnseignants;
    private JTable jtableEtudiants;

    // Navigation
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;

    public DashboardRespFormation(MainController controller) {
        this.controller = controller;
        this.api = new RestService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Responsable de Formation - Admin");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === SIDEBAR ===
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel lblTitre = new JLabel("<html>RESPONSABLE<br>FORMATION</html>");
        lblTitre.setFont(Theme.FONT_TITLE);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTitre);
        sidebar.add(Box.createVerticalStrut(40));

        // Menu
        addSidebarButton("Enseignants", "CARD_ENS");
        addSidebarButton("Etudiants", "CARD_ETU");
        addSidebarButton("Groupes", "CARD_GRP");

        sidebar.add(Box.createVerticalStrut(20));
        addSidebarButton("Algo. Groupes", "ACTION_ALGO");

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Déconnexion");
        styleSidebarButton(btnLogout, Theme.DANGER);
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // === MAIN CONTENT (CardLayout) ===
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.BACKGROUND);

        mainContentPanel.add(createEnseignantsPanel(), "CARD_ENS");
        mainContentPanel.add(createEtudiantsPanel(), "CARD_ETU");
        mainContentPanel.add(createGroupesPanel(), "CARD_GRP");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void addSidebarButton(String text, String actionCommand) {
        JButton btn = new JButton(text);
        // Utilisation d'une couleur solide (pas de transparence) pour éviter les soucis
        // de rendu
        Color btnColor = new Color(55, 75, 95); // Un bleu légèrement plus clair que le fond

        if ("ACTION_ALGO".equals(actionCommand)) {
            btnColor = Theme.PRIMARY;
            btn.addActionListener(e -> new AlgorithmeGroupesFrame(controller).setVisible(true));
            // Pour le bouton principal, on le garde tel quel
        } else {
            btn.addActionListener(e -> cardLayout.show(mainContentPanel, actionCommand));
        }

        styleSidebarButton(btn, btnColor);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private void styleSidebarButton(JButton btn, Color bg) {
        btn.setMaximumSize(new Dimension(240, 45));
        // Important: setPreferredSize aide le layout box
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.FONT_BOLD);
        btn.setFocusPainted(false);
        // Bordure plus simple
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // ==================== ENSEIGNANTS ====================
    private JPanel createEnseignantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Gestion des Enseignants");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Theme.BACKGROUND);

        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerEnseignants());

        JButton btnAjouter = ButtonFactory.ajouter("+ Ajouter");
        btnAjouter.addActionListener(e -> ajouterEnseignant());

        JButton btnModifier = ButtonFactory.principal("Modifier");
        btnModifier.addActionListener(e -> modifierEnseignant());

        JButton btnSupprimer = ButtonFactory.supprimer("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerEnseignant());

        JButton btnRole = ButtonFactory.normal("Attribuer Role");
        btnRole.addActionListener(e -> attribuerRole());

        toolbar.add(btnRefresh);
        toolbar.add(btnAjouter);
        toolbar.add(btnModifier);
        toolbar.add(btnSupprimer);
        toolbar.add(btnRole);

        // Center wrapper for Toolbar + Table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        String[] cols = { "ID", "Nom", "Prenom", "Genre", "Email" };
        tableEnseignants = new DefaultTableModel(cols, 0);
        jtableEnseignants = new JTable(tableEnseignants);
        jtableEnseignants.setRowHeight(25);
        jtableEnseignants.getTableHeader().setBackground(Theme.SECONDARY);
        jtableEnseignants.getTableHeader().setForeground(Color.WHITE);

        centerPanel.add(new JScrollPane(jtableEnseignants), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        chargerEnseignants();
        return panel;
    }

    private void chargerEnseignants() {
        new Thread(() -> {
            String json = api.getEnseignants();
            if (json != null) {
                SwingUtilities.invokeLater(() -> {
                    tableEnseignants.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        tableEnseignants.addRow(new Object[] {
                                getValue(obj, "id_ens"),
                                getValue(obj, "nom_ens"),
                                getValue(obj, "prenom_ens"),
                                getValue(obj, "genre_ens"),
                                getValue(obj, "courriel_ens")
                        });
                    }
                });
            }
        }).start();
    }

    private void ajouterEnseignant() {
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> cbGenre = new JComboBox<>(new String[] { "H", "F" });

        Object[] fields = { "Nom:", txtNom, "Prenom:", txtPrenom, "Email:", txtEmail, "Genre:", cbGenre };
        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un enseignant", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                String response = api.createEnseignant(
                        txtNom.getText(), txtPrenom.getText(),
                        (String) cbGenre.getSelectedItem(), txtEmail.getText());
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Enseignant ajoute avec succes!");
                        chargerEnseignants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void modifierEnseignant() {
        int row = jtableEnseignants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionnez un enseignant a modifier");
            return;
        }

        String id = tableEnseignants.getValueAt(row, 0).toString();
        JTextField txtNom = new JTextField(tableEnseignants.getValueAt(row, 1).toString());
        JTextField txtPrenom = new JTextField(tableEnseignants.getValueAt(row, 2).toString());
        JTextField txtEmail = new JTextField(tableEnseignants.getValueAt(row, 4).toString());
        JComboBox<String> cbGenre = new JComboBox<>(new String[] { "H", "F" });
        cbGenre.setSelectedItem(tableEnseignants.getValueAt(row, 3).toString());

        Object[] fields = { "Nom:", txtNom, "Prenom:", txtPrenom, "Email:", txtEmail, "Genre:", cbGenre };
        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier enseignant #" + id,
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                String response = api.updateEnseignant(
                        Integer.parseInt(id), txtNom.getText(), txtPrenom.getText(),
                        (String) cbGenre.getSelectedItem(), txtEmail.getText());
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Enseignant modifie!");
                        chargerEnseignants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void supprimerEnseignant() {
        int row = jtableEnseignants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionnez un enseignant a supprimer");
            return;
        }

        String id = tableEnseignants.getValueAt(row, 0).toString();
        String nom = tableEnseignants.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer l'enseignant " + nom + " (ID: " + id + ") ?",
                "Confirmer suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                String response = api.deleteEnseignant(Integer.parseInt(id));
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Enseignant supprime!");
                        chargerEnseignants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void attribuerRole() {
        int row = jtableEnseignants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionnez un enseignant");
            return;
        }

        String id = tableEnseignants.getValueAt(row, 0).toString();
        String nom = tableEnseignants.getValueAt(row, 1).toString();
        String prenom = tableEnseignants.getValueAt(row, 2).toString();

        String[] roles = { "Enseignant (role=1)", "Responsable Filiere (role=2)", "Responsable Formation (role=3)" };
        JComboBox<String> cbRole = new JComboBox<>(roles);

        int result = JOptionPane.showConfirmDialog(this,
                new Object[] { "Attribuer un role a " + prenom + " " + nom + ":", cbRole },
                "Attribution de role", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int roleId = cbRole.getSelectedIndex() + 1; // 1, 2, ou 3
            new Thread(() -> {
                String json = "{\"id_role\":" + roleId + "}";
                String response = api.put("/enseignants/" + id, json);
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this,
                                "Role attribue: " + roles[roleId - 1]
                                        + "\n\nL'enseignant devra se reconnecter pour voir les changements.");
                        chargerEnseignants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    // ==================== ETUDIANTS ====================
    private JComboBox<String> cbPromoEtudiants;
    private int selectedPromoId = 1; // Default promotion ID

    private JPanel createEtudiantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Gestion des Etudiants");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Theme.BACKGROUND);

        // Promotion selector
        JLabel lblPromo = new JLabel("Promotion: ");
        lblPromo.setFont(Theme.FONT_BOLD);
        toolbar.add(lblPromo);

        cbPromoEtudiants = new JComboBox<>();
        cbPromoEtudiants.setPreferredSize(new Dimension(150, 30));
        cbPromoEtudiants.addActionListener(e -> {
            if (cbPromoEtudiants.getSelectedItem() != null) {
                String selected = cbPromoEtudiants.getSelectedItem().toString();
                // Extract promo ID from "BUT2A (ID:1)" format
                if (selected.contains("ID:")) {
                    String idStr = selected.substring(selected.indexOf("ID:") + 3, selected.indexOf(")"));
                    selectedPromoId = Integer.parseInt(idStr.trim());
                    chargerEtudiants();
                }
            }
        });
        toolbar.add(cbPromoEtudiants);

        // Load promotions in combo box
        chargerPromotionsCombo();

        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerEtudiants());

        JButton btnAjouter = ButtonFactory.ajouter("+ Ajouter");
        btnAjouter.addActionListener(e -> ajouterEtudiant());

        JButton btnModifier = ButtonFactory.principal("Modifier");
        btnModifier.addActionListener(e -> modifierEtudiant());

        JButton btnSupprimer = ButtonFactory.supprimer("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerEtudiant());

        toolbar.add(btnRefresh);
        toolbar.add(btnAjouter);
        toolbar.add(btnModifier);
        toolbar.add(btnSupprimer);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        String[] cols = { "ID", "Nom", "Prenom", "Age", "Genre", "Email" };
        tableEtudiants = new DefaultTableModel(cols, 0);
        jtableEtudiants = new JTable(tableEtudiants);
        jtableEtudiants.setRowHeight(25);
        jtableEtudiants.getTableHeader().setBackground(Theme.SECONDARY);
        jtableEtudiants.getTableHeader().setForeground(Color.WHITE);

        centerPanel.add(new JScrollPane(jtableEtudiants), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void chargerPromotionsCombo() {
        new Thread(() -> {
            String json = api.getPromotions();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    cbPromoEtudiants.removeAllItems();
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    boolean first = true;
                    while (m.find()) {
                        String obj = m.group();
                        String id = getValue(obj, "id_promo");
                        String nom = getValue(obj, "nom_promo");
                        if (nom.isEmpty())
                            nom = getValue(obj, "lib_promo");
                        if (nom.isEmpty())
                            nom = "Promotion " + id;
                        cbPromoEtudiants.addItem(nom + " (ID:" + id + ")");
                        if (first) {
                            selectedPromoId = Integer.parseInt(id);
                            first = false;
                        }
                    }
                    // Charger les étudiants de la première promo
                    chargerEtudiants();
                });
            }
        }).start();
    }

    private void chargerEtudiants() {
        new Thread(() -> {
            String json = api.getEtudiantsByPromo(selectedPromoId);
            if (json != null) {
                SwingUtilities.invokeLater(() -> {
                    tableEtudiants.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        tableEtudiants.addRow(new Object[] {
                                getValue(obj, "id_etu"),
                                getValue(obj, "nom_etu"),
                                getValue(obj, "prenom_etu"),
                                getValue(obj, "age_etu"),
                                getValue(obj, "genre_etu"),
                                getValue(obj, "courriel_etu")
                        });
                    }
                });
            }
        }).start();
    }

    private void ajouterEtudiant() {
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtAge = new JTextField("20");
        JTextField txtEmail = new JTextField();
        JComboBox<String> cbGenre = new JComboBox<>(new String[] { "H", "F" });
        JTextField txtPromo = new JTextField("1");

        Object[] fields = { "Nom:", txtNom, "Prenom:", txtPrenom, "Age:", txtAge, "Email:", txtEmail, "Genre:", cbGenre,
                "ID Promo:", txtPromo };
        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un etudiant", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                String response = api.createEtudiant(
                        txtNom.getText(), txtPrenom.getText(),
                        Integer.parseInt(txtAge.getText()),
                        (String) cbGenre.getSelectedItem(), txtEmail.getText(),
                        Integer.parseInt(txtPromo.getText()));
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Etudiant ajoute avec succes!");
                        chargerEtudiants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void modifierEtudiant() {
        int row = jtableEtudiants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionnez un etudiant a modifier");
            return;
        }

        String id = tableEtudiants.getValueAt(row, 0).toString();
        JTextField txtNom = new JTextField(tableEtudiants.getValueAt(row, 1).toString());
        JTextField txtPrenom = new JTextField(tableEtudiants.getValueAt(row, 2).toString());
        JTextField txtAge = new JTextField(tableEtudiants.getValueAt(row, 3).toString());
        JTextField txtEmail = new JTextField(tableEtudiants.getValueAt(row, 5).toString());
        JComboBox<String> cbGenre = new JComboBox<>(new String[] { "H", "F" });
        cbGenre.setSelectedItem(tableEtudiants.getValueAt(row, 4).toString());

        Object[] fields = { "Nom:", txtNom, "Prenom:", txtPrenom, "Age:", txtAge, "Email:", txtEmail, "Genre:",
                cbGenre };
        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier etudiant #" + id,
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                String response = api.updateEtudiant(
                        Integer.parseInt(id), txtNom.getText(), txtPrenom.getText(),
                        Integer.parseInt(txtAge.getText()),
                        (String) cbGenre.getSelectedItem(), txtEmail.getText());
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Etudiant modifie!");
                        chargerEtudiants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void supprimerEtudiant() {
        int row = jtableEtudiants.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionnez un etudiant a supprimer");
            return;
        }

        String id = tableEtudiants.getValueAt(row, 0).toString();
        String nom = tableEtudiants.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer l'etudiant " + nom + " (ID: " + id + ") ?",
                "Confirmer suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                String response = api.deleteEtudiant(Integer.parseInt(id));
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Etudiant supprime!");
                        chargerEtudiants();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    // ==================== GROUPES (avec gestion) ====================
    private DefaultTableModel tableGroupes;
    private DefaultTableModel tableMembresGroupe;
    private JLabel lblGroupeStats;

    private JPanel createGroupesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Gestion des Groupes");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(Theme.BACKGROUND);
        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerGroupes());
        toolbar.add(btnRefresh);

        JButton btnAlgo = ButtonFactory.ajouter("Lancer Algorithme");
        btnAlgo.addActionListener(e -> new AlgorithmeGroupesFrame(controller).setVisible(true));
        toolbar.add(btnAlgo);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setBackground(Theme.BACKGROUND);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Liste des groupes"));
        leftPanel.setBackground(Color.WHITE);
        String[] cols = { "ID", "Nom du groupe", "Promotion" };
        tableGroupes = new DefaultTableModel(cols, 0);
        JTable jtableGroupes = new JTable(tableGroupes);
        jtableGroupes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtableGroupes.setRowHeight(25);
        jtableGroupes.getTableHeader().setBackground(Theme.SECONDARY);
        jtableGroupes.getTableHeader().setForeground(Color.WHITE);

        jtableGroupes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jtableGroupes.getSelectedRow() >= 0) {
                String idGroupe = tableGroupes.getValueAt(jtableGroupes.getSelectedRow(), 0).toString();
                String nomGroupe = tableGroupes.getValueAt(jtableGroupes.getSelectedRow(), 1).toString();
                chargerMembresGroupeAdmin(idGroupe, nomGroupe);
            }
        });
        leftPanel.add(new JScrollPane(jtableGroupes), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Membres du groupe"));
        rightPanel.setBackground(Color.WHITE);
        lblGroupeStats = new JLabel("Selectionnez un groupe");
        lblGroupeStats.setFont(new Font("Arial", Font.BOLD, 12));
        lblGroupeStats.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightPanel.add(lblGroupeStats, BorderLayout.NORTH);
        String[] colsMembres = { "ID", "Nom", "Prenom", "Email", "Genre" };
        tableMembresGroupe = new DefaultTableModel(colsMembres, 0);

        JTable tableMembres = new JTable(tableMembresGroupe);
        tableMembres.setRowHeight(25);
        // Header style for members too
        tableMembres.getTableHeader().setBackground(Theme.SECONDARY);
        tableMembres.getTableHeader().setForeground(Color.WHITE);

        rightPanel.add(new JScrollPane(tableMembres), BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        centerPanel.add(splitPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        chargerGroupes();
        return panel;
    }

    private void chargerGroupes() {
        new Thread(() -> {
            String json = api.getGroupes();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    tableGroupes.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        tableGroupes.addRow(new Object[] {
                                getValue(obj, "id_groupe"),
                                getValue(obj, "lib_grp"),
                                getValue(obj, "id_promo")
                        });
                    }
                });
            }
        }).start();
    }

    private void chargerMembresGroupeAdmin(String idGroupe, String nomGroupe) {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    tableMembresGroupe.setRowCount(0);
                    int count = 0, h = 0, f = 0;
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        if (idGroupe.equals(getValue(obj, "id_groupe"))) {
                            String genre = getValue(obj, "genre_etu");
                            tableMembresGroupe.addRow(new Object[] {
                                    getValue(obj, "id_etu"),
                                    getValue(obj, "nom_etu"),
                                    getValue(obj, "prenom_etu"),
                                    getValue(obj, "courriel_etu"),
                                    genre
                            });
                            count++;
                            if ("H".equals(genre))
                                h++;
                            else if ("F".equals(genre))
                                f++;
                        }
                    }
                    lblGroupeStats.setText(String.format("<html><b>%s</b> - %d etudiants (H:%d / F:%d)</html>",
                            nomGroupe, count, h, f));
                });
            }
        }).start();
    }

    // Utilitaire JSON
    private String getValue(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\"([^\"]*)\"|([^,}]+))");
        Matcher m = p.matcher(json);
        if (m.find()) {
            String val = m.group(2) != null ? m.group(2) : m.group(3);
            return val != null ? val.trim().replace("null", "") : "";
        }
        return "";
    }
}
