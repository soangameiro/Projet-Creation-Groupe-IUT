package Vue;

import Controleur.MainController;
import Infrastructure.JsonParser;
import Infrastructure.RestService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dashboard Enseignant Classique - Style moderne avec sidebar
 * LECTURE SEULE : peut voir les données mais pas les modifier
 */
public class DashboardEnseignant extends JFrame {

    private final MainController controleur;
    private final RestService api;

    private DefaultTableModel modeleEtudiants;
    private DefaultTableModel modeleGroupes;
    private DefaultTableModel modeleMembres;
    private JLabel labelStatsGroupe;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public DashboardEnseignant(MainController controleur) {
        this.controleur = controleur;
        this.api = new RestService();
        initialiserComposants();
    }

    private void initialiserComposants() {
        setTitle("Enseignant - " + controleur.getCurrentUserPrenom() + " " + controleur.getCurrentUserNom());
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === SIDEBAR MODERNE ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel lblTitre = new JLabel("<html>ENSEIGNANT<br><small>(Lecture seule)</small></html>");
        lblTitre.setFont(Theme.FONT_TITLE);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTitre);
        sidebar.add(Box.createVerticalStrut(10));

        // Nom utilisateur
        JLabel lblUser = new JLabel(controleur.getCurrentUserPrenom() + " " + controleur.getCurrentUserNom());
        lblUser.setFont(Theme.FONT_SMALL);
        lblUser.setForeground(new Color(180, 180, 180));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createVerticalStrut(30));

        // Menu navigation
        addSidebarButton(sidebar, "Etudiants", "CARD_ETU");
        addSidebarButton(sidebar, "Groupes", "CARD_GRP");
        addSidebarButton(sidebar, "Promotions", "CARD_PROMO");

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

        mainContentPanel.add(creerPanneauEtudiants(), "CARD_ETU");
        mainContentPanel.add(creerPanneauGroupes(), "CARD_GRP");
        mainContentPanel.add(creerPanneauPromotions(), "CARD_PROMO");

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
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // ==================== ETUDIANTS (lecture seule) ====================
    private JPanel creerPanneauEtudiants() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Liste des Etudiants");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);

        JLabel lblReadOnly = new JLabel("(Lecture seule)");
        lblReadOnly.setForeground(Color.GRAY);
        header.add(lblReadOnly, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Theme.BACKGROUND);
        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerEtudiants());
        toolbar.add(btnRefresh);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        String[] cols = { "ID", "Nom", "Prenom", "Age", "Genre", "Email" };
        modeleEtudiants = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(modeleEtudiants);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(Theme.SECONDARY);
        table.getTableHeader().setForeground(Color.WHITE);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        chargerEtudiants();
        return panel;
    }

    private void chargerEtudiants() {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleEtudiants.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        modeleEtudiants.addRow(new Object[] {
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

    // ==================== GROUPES (avec détails) ====================
    private JPanel creerPanneauGroupes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Consultation des Groupes");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setBackground(Theme.BACKGROUND);

        // Gauche : liste groupes
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Liste des groupes"));
        leftPanel.setBackground(Color.WHITE);

        String[] colsGrp = { "ID", "Nom du groupe", "Promotion" };
        modeleGroupes = new DefaultTableModel(colsGrp, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tableGroupes = new JTable(modeleGroupes);
        tableGroupes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableGroupes.setRowHeight(25);
        tableGroupes.getTableHeader().setBackground(Theme.SECONDARY);
        tableGroupes.getTableHeader().setForeground(Color.WHITE);

        tableGroupes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableGroupes.getSelectedRow() >= 0) {
                String idGrp = modeleGroupes.getValueAt(tableGroupes.getSelectedRow(), 0).toString();
                String nomGrp = modeleGroupes.getValueAt(tableGroupes.getSelectedRow(), 1).toString();
                chargerMembresGroupe(idGrp, nomGrp);
            }
        });
        leftPanel.add(new JScrollPane(tableGroupes), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Droite : membres
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Membres du groupe"));
        rightPanel.setBackground(Color.WHITE);

        labelStatsGroupe = new JLabel("Selectionnez un groupe");
        labelStatsGroupe.setFont(new Font("Arial", Font.BOLD, 12));
        labelStatsGroupe.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightPanel.add(labelStatsGroupe, BorderLayout.NORTH);

        String[] colsMembres = { "ID", "Nom", "Prenom", "Email", "Genre" };
        modeleMembres = new DefaultTableModel(colsMembres, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tableMembres = new JTable(modeleMembres);
        tableMembres.setRowHeight(25);
        tableMembres.getTableHeader().setBackground(Theme.SECONDARY);
        tableMembres.getTableHeader().setForeground(Color.WHITE);
        rightPanel.add(new JScrollPane(tableMembres), BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        panel.add(splitPane, BorderLayout.CENTER);
        chargerGroupes();
        return panel;
    }

    private void chargerGroupes() {
        new Thread(() -> {
            String json = api.getGroupes();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleGroupes.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        modeleGroupes.addRow(new Object[] {
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
                    modeleMembres.setRowCount(0);
                    int total = 0, h = 0, f = 0;
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        if (idGroupe.equals(getValue(obj, "id_groupe"))) {
                            String genre = getValue(obj, "genre_etu");
                            modeleMembres.addRow(new Object[] {
                                    getValue(obj, "id_etu"),
                                    getValue(obj, "nom_etu"),
                                    getValue(obj, "prenom_etu"),
                                    getValue(obj, "courriel_etu"),
                                    genre
                            });
                            total++;
                            if ("H".equals(genre))
                                h++;
                            else if ("F".equals(genre))
                                f++;
                        }
                    }
                    labelStatsGroupe.setText(String.format(
                            "<html><b>%s</b> - %d etudiants (H:%d / F:%d)</html>",
                            nomGroupe, total, h, f));
                });
            }
        }).start();
    }

    // ==================== PROMOTIONS ====================
    private DefaultTableModel modelePromotions;

    private JPanel creerPanneauPromotions() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel title = new JLabel("Liste des Promotions");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.SECONDARY);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Theme.BACKGROUND);
        JButton btnRefresh = ButtonFactory.normal("Actualiser");
        btnRefresh.addActionListener(e -> chargerPromotions());
        toolbar.add(btnRefresh);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(toolbar, BorderLayout.NORTH);

        String[] cols = { "ID", "Nom", "Nb Eleves", "Nb Groupes" };
        modelePromotions = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(modelePromotions);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(Theme.SECONDARY);
        table.getTableHeader().setForeground(Color.WHITE);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        chargerPromotions();
        return panel;
    }

    private void chargerPromotions() {
        new Thread(() -> {
            String json = api.getPromotions();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modelePromotions.setRowCount(0);
                    Pattern p = Pattern.compile("\\{[^}]+\\}");
                    Matcher m = p.matcher(json);
                    while (m.find()) {
                        String obj = m.group();
                        modelePromotions.addRow(new Object[] {
                                getValue(obj, "id_promo"),
                                getValue(obj, "nom_promo"),
                                getValue(obj, "nb_eleves"),
                                getValue(obj, "nb_grp")
                        });
                    }
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
