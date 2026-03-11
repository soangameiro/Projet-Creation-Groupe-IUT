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
import java.util.Map;
import java.util.HashMap;

public class DashboardEtudiant extends JFrame {

    private final MainController controleur;
    private final RestService api;
    private DefaultTableModel modeleTableauPromotion;
    private DefaultListModel<String> modeleListeCovoiturage;

    // Navigation
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;

    public DashboardEtudiant(MainController controleur) {
        this.controleur = controleur;
        this.api = new RestService();
        initialiserComposants();
    }

    private void initialiserComposants() {
        String titre = "Espace Etudiant - " + controleur.getCurrentUserPrenom() + " " + controleur.getCurrentUserNom();
        setTitle(titre);
        setSize(1100, 750);
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
        JLabel lblTitre = new JLabel("<html>ESPACE<br>ETUDIANT</html>");
        lblTitre.setFont(Theme.FONT_TITLE);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblTitre);
        sidebar.add(Box.createVerticalStrut(40));

        // Menu
        addSidebarButton("Mes Informations", "CARD_INFOS");
        addSidebarButton("Ma Promotion", "CARD_PROMO");
        addSidebarButton("Mes Groupes", "CARD_GRP");
        addSidebarButton("Covoiturage", "CARD_COVOIT");
        addSidebarButton("Sondages", "CARD_SOND");

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Déconnexion");
        styleSidebarButton(btnLogout, Theme.DANGER);
        btnLogout.addActionListener(e -> deconnexion());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // === MAIN CONTENT ===
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(Theme.BACKGROUND);

        mainContentPanel.add(creerPanneauInfos(), "CARD_INFOS");
        mainContentPanel.add(creerPanneauPromotion(), "CARD_PROMO");
        mainContentPanel.add(creerPanneauGroupes(), "CARD_GRP");
        mainContentPanel.add(creerPanneauCovoiturage(), "CARD_COVOIT");
        mainContentPanel.add(creerPanneauSondages(), "CARD_SOND");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void addSidebarButton(String text, String actionCommand) {
        JButton btn = new JButton(text);
        // Couleur solide pour éviter le blanc sur blanc
        Color btnColor = new Color(55, 75, 95);
        styleSidebarButton(btn, btnColor);
        btn.addActionListener(e -> cardLayout.show(mainContentPanel, actionCommand));
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private void styleSidebarButton(JButton btn, Color bg) {
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // ==================== PANNEAUX ====================

    private JPanel createContentPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BACKGROUND);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        JLabel lbl = new JLabel(title);
        lbl.setFont(Theme.FONT_SUBTITLE);
        lbl.setForeground(Theme.SECONDARY);
        header.add(lbl, BorderLayout.WEST);

        p.add(header, BorderLayout.NORTH);
        return p;
    }

    private JPanel creerPanneauInfos() {
        JPanel panneau = createContentPanel("Mes Informations");

        JPanel cardInfo = new JPanel(new GridLayout(7, 2, 10, 10));
        cardInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.PANEL_BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        cardInfo.setBackground(Color.WHITE);

        cardInfo.add(new JLabel("Nom:"));
        cardInfo.add(new JLabel(controleur.getCurrentUserNom()));
        cardInfo.add(new JLabel("Prenom:"));
        cardInfo.add(new JLabel(controleur.getCurrentUserPrenom()));
        cardInfo.add(new JLabel("Email:"));
        cardInfo.add(new JLabel(controleur.getCurrentUserEmail()));
        cardInfo.add(new JLabel("Role:"));
        cardInfo.add(new JLabel(controleur.getCurrentUserRole()));
        cardInfo.add(new JLabel("Groupe:"));
        cardInfo.add(new JLabel("A definir"));
        cardInfo.add(new JLabel("Promotion:"));
        cardInfo.add(new JLabel("BUT Informatique 2024-2025"));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Theme.BACKGROUND);
        centerWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        centerWrapper.add(cardInfo, BorderLayout.NORTH); // Align top

        JButton boutonActualiser = ButtonFactory.normal("Actualiser mes informations");
        boutonActualiser.addActionListener(e -> chargerMesInfos());
        centerWrapper.add(boutonActualiser, BorderLayout.SOUTH);

        panneau.add(centerWrapper, BorderLayout.CENTER);
        return panneau;
    }

    private JPanel creerPanneauPromotion() {
        JPanel panneau = createContentPanel("Ma Promotion");

        JPanel barreOutils = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barreOutils.setBackground(Theme.BACKGROUND);
        JButton boutonCharger = ButtonFactory.normal("Charger promotion");
        boutonCharger.addActionListener(e -> chargerPromotion());
        barreOutils.add(boutonCharger);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(barreOutils, BorderLayout.NORTH);

        String[] cols = { "ID", "Nom", "Prenom", "Genre", "Email", "Age" };
        modeleTableauPromotion = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tableau = new JTable(modeleTableauPromotion);
        tableau.setRowHeight(25);
        tableau.getTableHeader().setBackground(Theme.SECONDARY);
        tableau.getTableHeader().setForeground(Color.WHITE);

        centerPanel.add(new JScrollPane(tableau), BorderLayout.CENTER);
        panneau.add(centerPanel, BorderLayout.CENTER);

        chargerPromotion();
        return panneau;
    }

    private DefaultTableModel modeleTableauGroupes;
    private DefaultTableModel modeleTableauMembres;

    private JPanel creerPanneauGroupes() {
        JPanel panneau = createContentPanel("Mes Groupes");

        JPanel barreOutils = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barreOutils.setBackground(Theme.BACKGROUND);
        JButton boutonActualiser = ButtonFactory.normal("Actualiser les groupes");
        boutonActualiser.addActionListener(e -> chargerGroupes());
        barreOutils.add(boutonActualiser);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(barreOutils, BorderLayout.NORTH);

        JSplitPane panneauDivise = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panneauDivise.setDividerLocation(350);
        panneauDivise.setBackground(Theme.BACKGROUND);

        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Liste des groupes"));
        panneauGauche.setBackground(Color.WHITE);

        String[] colonnesGroupes = { "ID", "Nom du groupe", "Promotion" };
        modeleTableauGroupes = new DefaultTableModel(colonnesGroupes, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tableauGroupes = new JTable(modeleTableauGroupes);
        tableauGroupes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableauGroupes.setRowHeight(25);
        tableauGroupes.getTableHeader().setBackground(Theme.SECONDARY);
        tableauGroupes.getTableHeader().setForeground(Color.WHITE);

        tableauGroupes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableauGroupes.getSelectedRow() >= 0) {
                String idGroupe = modeleTableauGroupes.getValueAt(
                        tableauGroupes.getSelectedRow(), 0).toString();
                chargerMembresGroupe(idGroupe);
            }
        });

        panneauGauche.add(new JScrollPane(tableauGroupes), BorderLayout.CENTER);
        panneauDivise.setLeftComponent(panneauGauche);

        JPanel panneauDroite = new JPanel(new BorderLayout());
        panneauDroite.setBorder(BorderFactory.createTitledBorder("Membres du groupe"));
        panneauDroite.setBackground(Color.WHITE);

        String[] colsM = { "Nom", "Prenom", "Email", "Genre" };
        modeleTableauMembres = new DefaultTableModel(colsM, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tableM = new JTable(modeleTableauMembres);
        tableM.setRowHeight(25);
        tableM.getTableHeader().setBackground(Theme.SECONDARY);
        tableM.getTableHeader().setForeground(Color.WHITE);

        panneauDroite.add(new JScrollPane(tableM), BorderLayout.CENTER);
        panneauDivise.setRightComponent(panneauDroite);

        centerPanel.add(panneauDivise, BorderLayout.CENTER);
        panneau.add(centerPanel, BorderLayout.CENTER);
        chargerGroupes();
        return panneau;
    }

    private Map<String, Integer> mapEmailVersId = new HashMap<>();

    private JPanel creerPanneauCovoiturage() {
        JPanel panneau = createContentPanel("Covoiturage");

        JPanel outil = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outil.setBackground(Theme.BACKGROUND);
        outil.add(new JLabel("Choisissez vos collègues (max 3): "));
        JButton boutonCharger = ButtonFactory.normal("Charger liste");
        boutonCharger.addActionListener(e -> chargerEtudiantsPourCovoiturage());
        outil.add(boutonCharger);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(outil, BorderLayout.NORTH);

        modeleListeCovoiturage = new DefaultListModel<>();
        JList<String> listeEtudiants = new JList<>(modeleListeCovoiturage);
        listeEtudiants.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        centerPanel.add(new JScrollPane(listeEtudiants), BorderLayout.CENTER);

        JButton boutonValider = ButtonFactory.principal("VALIDER MA SELECTION");
        boutonValider.addActionListener(e -> {
            int[] selectionnes = listeEtudiants.getSelectedIndices();
            if (selectionnes.length > 3) {
                JOptionPane.showMessageDialog(this, "Maximum 3 personnes!", "Erreur", JOptionPane.WARNING_MESSAGE);
            } else if (selectionnes.length == 0) {
                JOptionPane.showMessageDialog(this, "Sélectionnez au moins une personne", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                validerChoixCovoiturage(selectionnes, listeEtudiants);
            }
        });
        centerPanel.add(boutonValider, BorderLayout.SOUTH);
        panneau.add(centerPanel, BorderLayout.CENTER);

        chargerEtudiantsPourCovoiturage();
        return panneau;
    }

    private JPanel conteneurSondages;
    private Map<Integer, ButtonGroup> reponsesSondages = new HashMap<>();

    private JPanel creerPanneauSondages() {
        JPanel panneau = createContentPanel("Sondages");

        JPanel outils = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outils.setBackground(Theme.BACKGROUND);
        JButton boutonCharger = ButtonFactory.normal("Charger sondages");
        boutonCharger.addActionListener(e -> chargerSondages());
        outils.add(boutonCharger);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Theme.BACKGROUND);
        centerPanel.add(outils, BorderLayout.NORTH);

        conteneurSondages = new JPanel();
        conteneurSondages.setLayout(new BoxLayout(conteneurSondages, BoxLayout.Y_AXIS));
        conteneurSondages.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(conteneurSondages);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scroll, BorderLayout.CENTER);

        JButton boutonEnvoyer = ButtonFactory.principal("ENVOYER MES REPONSES");
        boutonEnvoyer.addActionListener(e -> envoyerReponsesSondage());
        centerPanel.add(boutonEnvoyer, BorderLayout.SOUTH);

        panneau.add(centerPanel, BorderLayout.CENTER);
        chargerSondages();
        return panneau;
    }

    // ==================== LOGIQUE METIER (Similaire à avant) ====================

    private void chargerMesInfos() {
        new Thread(() -> {
            int id = controleur.getCurrentUserId();
            String nom = controleur.getCurrentUserNom();
            String prenom = controleur.getCurrentUserPrenom();
            String email = controleur.getCurrentUserEmail();
            String groupe = "Aucun";

            String jsonEtudiant = api.get("/etudiants/" + id);
            String idGroupe = JsonParser.extraireValeur(jsonEtudiant, "id_groupe");

            if (!idGroupe.isEmpty() && !idGroupe.equals("0")) {
                String jsonGroupes = api.getGroupes();
                if (jsonGroupes != null) {
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(jsonGroupes);
                    while (matcher.find()) {
                        String objetGroupe = matcher.group();
                        if (JsonParser.extraireValeur(objetGroupe, "id_groupe").equals(idGroupe)) {
                            groupe = JsonParser.extraireValeur(objetGroupe, "lib_grp");
                            break;
                        }
                    }
                }
            }
            String groupeFinal = groupe;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Informations:\nNom: " + nom + "\nPrenom: " + prenom + "\nGroupe: " + groupeFinal);
            });
        }).start();
    }

    private void chargerPromotion() {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleTableauPromotion.setRowCount(0);
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(json);
                    while (matcher.find()) {
                        String obj = matcher.group();
                        modeleTableauPromotion.addRow(new Object[] {
                                JsonParser.extraireValeur(obj, "id_etu"),
                                JsonParser.extraireValeur(obj, "nom_etu"),
                                JsonParser.extraireValeur(obj, "prenom_etu"),
                                JsonParser.extraireValeur(obj, "genre_etu"),
                                JsonParser.extraireValeur(obj, "courriel_etu"),
                                JsonParser.extraireValeur(obj, "age_etu")
                        });
                    }
                });
            }
        }).start();
    }

    private void chargerGroupes() {
        new Thread(() -> {
            String json = api.getGroupes();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleTableauGroupes.setRowCount(0);
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(json);
                    while (matcher.find()) {
                        String obj = matcher.group();
                        modeleTableauGroupes.addRow(new Object[] {
                                JsonParser.extraireValeur(obj, "id_groupe"),
                                JsonParser.extraireValeur(obj, "lib_grp"),
                                JsonParser.extraireValeur(obj, "id_promo")
                        });
                    }
                });
            }
        }).start();
    }

    private void chargerMembresGroupe(String idGroupe) {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleTableauMembres.setRowCount(0);
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(json);
                    while (matcher.find()) {
                        String obj = matcher.group();
                        if (idGroupe.equals(JsonParser.extraireValeur(obj, "id_groupe"))) {
                            modeleTableauMembres.addRow(new Object[] {
                                    JsonParser.extraireValeur(obj, "nom_etu"),
                                    JsonParser.extraireValeur(obj, "prenom_etu"),
                                    JsonParser.extraireValeur(obj, "courriel_etu"),
                                    JsonParser.extraireValeur(obj, "genre_etu")
                            });
                        }
                    }
                });
            }
        }).start();
    }

    private void chargerEtudiantsPourCovoiturage() {
        new Thread(() -> {
            String json = api.getEtudiants();
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    modeleListeCovoiturage.clear();
                    mapEmailVersId.clear();
                    String monEmail = controleur.getCurrentUserEmail();
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(json);
                    while (matcher.find()) {
                        String obj = matcher.group();
                        String email = JsonParser.extraireValeur(obj, "courriel_etu");
                        if (!email.equals(monEmail)) {
                            String nom = JsonParser.extraireValeur(obj, "nom_etu");
                            String prenom = JsonParser.extraireValeur(obj, "prenom_etu");
                            String idTexte = JsonParser.extraireValeur(obj, "id_etu");
                            modeleListeCovoiturage.addElement(prenom + " " + nom + " - " + email);
                            if (!idTexte.isEmpty())
                                mapEmailVersId.put(email, Integer.parseInt(idTexte));
                        }
                    }
                });
            }
        }).start();
    }

    private void validerChoixCovoiturage(int[] indices, JList<String> liste) {
        new Thread(() -> {
            int monId = controleur.getCurrentUserId();
            for (int indice : indices) {
                String selection = modeleListeCovoiturage.get(indice);
                String email = selection.substring(selection.lastIndexOf(" - ") + 3);
                Integer idSouhaite = mapEmailVersId.get(email);
                if (idSouhaite != null) {
                    String json = "{\"id_demandeur\":" + monId + ",\"id_souhaite\":" + idSouhaite + "}";
                    api.post("/covoiturage", json);
                }
            }
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Choix enregistrés !"));
        }).start();
    }

    private void chargerSondages() {
        new Thread(() -> {
            String json = api.get("/sondages");
            if (json != null && json.startsWith("[")) {
                SwingUtilities.invokeLater(() -> {
                    conteneurSondages.removeAll();
                    reponsesSondages.clear();
                    Pattern pattern = Pattern.compile("\\{[^}]+\\}");
                    Matcher matcher = pattern.matcher(json);
                    while (matcher.find()) {
                        String obj = matcher.group();
                        String idTexte = JsonParser.extraireValeur(obj, "id_sond");
                        String question = JsonParser.extraireValeur(obj, "crit_sond");
                        if (!idTexte.isEmpty()) {
                            int idSondage = Integer.parseInt(idTexte);
                            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            p.setBackground(Color.WHITE);
                            p.setBorder(BorderFactory.createTitledBorder(question));
                            ButtonGroup g = new ButtonGroup();
                            JRadioButton r1 = new JRadioButton("Oui");
                            r1.setActionCommand("Oui");
                            r1.setOpaque(false);
                            JRadioButton r2 = new JRadioButton("Non");
                            r2.setActionCommand("Non");
                            r2.setOpaque(false);
                            JRadioButton r3 = new JRadioButton("Sans avis");
                            r3.setActionCommand("Sans avis");
                            r3.setOpaque(false);
                            g.add(r1);
                            g.add(r2);
                            g.add(r3);
                            p.add(r1);
                            p.add(r2);
                            p.add(r3);
                            conteneurSondages.add(p);
                            reponsesSondages.put(idSondage, g);
                        }
                    }
                    conteneurSondages.revalidate();
                    conteneurSondages.repaint();
                });
            }
        }).start();
    }

    private void envoyerReponsesSondage() {
        int monId = controleur.getCurrentUserId();
        new Thread(() -> {
            int count = 0;
            for (Map.Entry<Integer, ButtonGroup> ent : reponsesSondages.entrySet()) {
                if (ent.getValue().getSelection() != null) {
                    String r = ent.getValue().getSelection().getActionCommand();
                    api.post("/sondages/reponse",
                            "{\"id_sond\":" + ent.getKey() + ",\"id_etu\":" + monId + ",\"reponse\":\"" + r + "\"}");
                    count++;
                }
            }
            final int c = count;
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, c + " réponses envoyées !"));
        }).start();
    }

    private void deconnexion() {
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}
