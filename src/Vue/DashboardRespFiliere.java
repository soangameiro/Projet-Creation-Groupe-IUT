package Vue;

import Controleur.MainController;
import Infrastructure.RestService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dashboard Responsable de Filière - Style moderne avec sidebar
 * CRUD complet : peut gérer étudiants, groupes et sondages
 */
public class DashboardRespFiliere extends JFrame {
    private final MainController controller;
    private final RestService api;

    private DefaultTableModel tableEtudiants;
    private DefaultTableModel tableGroupes;
    private DefaultTableModel tableMembres;
    private DefaultTableModel tableSondages;
    private JTable jtableEtudiants;
    private JLabel lblGroupeStats;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public DashboardRespFiliere(MainController controller) {
        this.controller = controller;
        this.api = new RestService();
        initComponents();
    }

    private void initComponents() {
        setTitle(
                "Responsable de Filiere - " + controller.getCurrentUserPrenom() + " " + controller.getCurrentUserNom());
        setSize(1150, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === SIDEBAR MODERNE ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(270, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel lblTitre = new JLabel("<html>RESPONSABLE<br>FILIERE</html>");
        lblTitre.setFont(Theme.FONT_TITLE);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTitre);
        sidebar.add(Box.createVerticalStrut(10));

        // Nom utilisateur
        JLabel lblUser = new JLabel(controller.getCurrentUserPrenom() + " " + controller.getCurrentUserNom());
        lblUser.setFont(Theme.FONT_SMALL);
        lblUser.setForeground(new Color(180, 180, 180));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createVerticalStrut(30));

        // Menu navigation
        addSidebarButton(sidebar, "Etudiants", "CARD_ETU");
        addSidebarButton(sidebar, "Groupes", "CARD_GRP");
        addSidebarButton(sidebar, "Sondages", "CARD_SOND");
        addSidebarButton(sidebar, "Import / Export", "CARD_IMPORT");

        sidebar.add(Box.createVerticalStrut(20));

        // Bouton algo
        JButton btnAlgo = new JButton("Algorithme Groupes");
        styleSidebarButton(btnAlgo, Theme.PRIMARY);
        btnAlgo.addActionListener(e -> new AlgorithmeGroupesFrame(controller, () -> chargerGroupes()).setVisible(true));
        sidebar.add(btnAlgo);

        sidebar.add(Box.createVerticalGlue());

        // Bouton déconnexion
        JButton btnLogout = new JButton("Deconnexion");
        styleSidebarButton(btnLogout, Theme.DANGER);
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // === CONTENU PRINCIPAL (CardLayout) ===
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.BACKGROUND);

        mainContentPanel.add(createEtudiantsPanel(), "CARD_ETU");
        mainContentPanel.add(createGroupesPanel(), "CARD_GRP");
        mainContentPanel.add(createSondagesPanel(), "CARD_SOND");
        mainContentPanel.add(createImportExportPanel(), "CARD_IMPORT");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void addSidebarButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        styleSidebarButton(btn, new Color(55, 75, 95));
        btn.addActionListener(e -> cardLayout.show(mainContentPanel, cardName));
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private void styleSidebarButton(JButton btn, Color bg) {
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // ==================== GESTION ETUDIANTS (CRUD) ====================
    private JPanel createEtudiantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Gestion des Etudiants");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Theme.BACKGROUND);

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

        chargerEtudiants();
        return panel;
    }

    private void chargerEtudiants() {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
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
        JTextField txtPromo = new JTextField("2024");

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
                        JOptionPane.showMessageDialog(this, "Etudiant ajoute!");
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
            JOptionPane.showMessageDialog(this, "Selectionnez un etudiant");
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
            JOptionPane.showMessageDialog(this, "Selectionnez un etudiant");
            return;
        }

        String id = tableEtudiants.getValueAt(row, 0).toString();
        String nom = tableEtudiants.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer " + nom + " ?", "Confirmer",
                JOptionPane.YES_NO_OPTION);
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

    // ==================== GROUPES ====================
    private JPanel createGroupesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Constitution des Groupes");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(Theme.BACKGROUND);

        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerGroupes());
        toolbar.add(btnRefresh);

        JButton btnCreer = ButtonFactory.ajouter("+ Creer groupe");
        btnCreer.addActionListener(e -> creerGroupe());
        toolbar.add(btnCreer);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setBackground(Theme.BACKGROUND);

        // Gauche
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
                String idGrp = tableGroupes.getValueAt(jtableGroupes.getSelectedRow(), 0).toString();
                String nomGrp = tableGroupes.getValueAt(jtableGroupes.getSelectedRow(), 1).toString();
                chargerMembresGroupe(idGrp, nomGrp);
            }
        });
        leftPanel.add(new JScrollPane(jtableGroupes), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Droite
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Membres du groupe"));
        rightPanel.setBackground(Color.WHITE);

        lblGroupeStats = new JLabel("Selectionnez un groupe");
        lblGroupeStats.setFont(new Font("Arial", Font.BOLD, 12));
        lblGroupeStats.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightPanel.add(lblGroupeStats, BorderLayout.NORTH);

        String[] colsMembres = { "ID", "Nom", "Prenom", "Email", "Genre" };
        tableMembres = new DefaultTableModel(colsMembres, 0);
        JTable jtableMembres = new JTable(tableMembres);
        jtableMembres.setRowHeight(25);
        jtableMembres.getTableHeader().setBackground(Theme.SECONDARY);
        jtableMembres.getTableHeader().setForeground(Color.WHITE);
        rightPanel.add(new JScrollPane(jtableMembres), BorderLayout.CENTER);
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

    private void chargerMembresGroupe(String idGroupe, String nomGroupe) {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    tableMembres.setRowCount(0);
                    int count = 0, h = 0, f = 0;
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        if (idGroupe.equals(getValue(obj, "id_groupe"))) {
                            String genre = getValue(obj, "genre_etu");
                            tableMembres.addRow(new Object[] {
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
                    lblGroupeStats.setText(String.format(
                            "<html><b>%s</b> - %d etudiants (H:%d / F:%d)</html>",
                            nomGroupe, count, h, f));
                });
            }
        }).start();
    }

    private void creerGroupe() {
        JTextField txtNom = new JTextField();
        JTextField txtPromo = new JTextField("2024");

        Object[] fields = { "Nom du groupe:", txtNom, "ID Promo:", txtPromo };
        int result = JOptionPane.showConfirmDialog(this, fields, "Creer un groupe", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                String json = "{\"nom\":\"" + txtNom.getText() + "\",\"id_promo\":" + txtPromo.getText() + "}";
                String response = api.post("/groupes", json);
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Groupe cree!");
                        chargerGroupes();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    // ==================== SONDAGES ====================
    private JPanel createSondagesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Gestion des Sondages");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(Theme.BACKGROUND);

        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerSondages());
        toolbar.add(btnRefresh);

        JButton btnCreer = ButtonFactory.ajouter("+ Creer sondage");
        btnCreer.addActionListener(e -> creerSondage());
        toolbar.add(btnCreer);

        JButton btnVoir = ButtonFactory.principal("Voir reponses");
        btnVoir.addActionListener(e -> voirReponsesSondage());
        toolbar.add(btnVoir);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        String[] cols = { "ID", "Question / Critere", "Createur" };
        tableSondages = new DefaultTableModel(cols, 0);
        JTable jtableSondages = new JTable(tableSondages);
        jtableSondages.setRowHeight(25);
        jtableSondages.getTableHeader().setBackground(Theme.SECONDARY);
        jtableSondages.getTableHeader().setForeground(Color.WHITE);
        centerPanel.add(new JScrollPane(jtableSondages), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        chargerSondages();
        return panel;
    }

    private void chargerSondages() {
        new Thread(() -> {
            String json = api.get("/sondages");
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    tableSondages.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        String createur = getValue(obj, "prenom_ens") + " " + getValue(obj, "nom_ens");
                        tableSondages.addRow(new Object[] {
                                getValue(obj, "id_sond"),
                                getValue(obj, "crit_sond"),
                                createur.trim().isEmpty() ? "Admin" : createur
                        });
                    }
                });
            }
        }).start();
    }

    private void creerSondage() {
        JTextField txtQuestion = new JTextField();
        Object[] fields = { "Question du sondage:", txtQuestion };
        int result = JOptionPane.showConfirmDialog(this, fields, "Creer un sondage", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && !txtQuestion.getText().isEmpty()) {
            new Thread(() -> {
                int idEns = controller.getCurrentUserId();
                if (idEns == 0)
                    idEns = 1;
                String json = "{\"question\":\"" + txtQuestion.getText() + "\",\"id_ens\":" + idEns + "}";
                String response = api.post("/sondages", json);
                SwingUtilities.invokeLater(() -> {
                    if (response != null && response.contains("success")) {
                        JOptionPane.showMessageDialog(this, "Sondage cree!");
                        chargerSondages();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur: " + response, "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        }
    }

    private void voirReponsesSondage() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTATS DU SONDAGE ===\n\n");
        sb.append("(Les resultats detailles seront affiches ici)\n\n");
        sb.append("Cette fonctionnalite necessite une integration API complete.");
        JOptionPane.showMessageDialog(this, sb.toString(), "Resultats", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== IMPORT / EXPORT ====================
    private JPanel createImportExportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        centerPanel.setBackground(Theme.BACKGROUND);

        JButton btnImport = ButtonFactory.principal("Importer des notes (CSV)");
        btnImport.setPreferredSize(new Dimension(350, 60));
        btnImport.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                importerNotes(fc.getSelectedFile());
            }
        });

        JButton btnExport = ButtonFactory.normal("Exporter les donnees");
        btnExport.setPreferredSize(new Dimension(350, 60));
        btnExport.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export en cours de developpement"));

        centerPanel.add(btnImport);
        centerPanel.add(btnExport);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void importerNotes(File file) {
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    if (count == 0 && line.contains("id_etu"))
                        continue;
                    String[] parts = line.split("[;,]");
                    if (parts.length >= 3) {
                        String json = "{\"id_etu\":" + parts[0].trim() + ",\"matiere\":\"" + parts[1].trim()
                                + "\",\"note\":" + parts[2].trim().replace(",", ".") + "}";
                        api.post("/notes", json);
                        count++;
                    }
                }
                final int finalCount = count;
                SwingUtilities.invokeLater(
                        () -> JOptionPane.showMessageDialog(this, "Import termine: " + finalCount + " notes"));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE));
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
