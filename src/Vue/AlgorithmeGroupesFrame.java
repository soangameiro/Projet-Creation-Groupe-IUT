package Vue;

import Controleur.MainController;
import Infrastructure.JsonParser;
import Infrastructure.RestService;
import Utilisateur.Etudiant;
import Education.Groupe; // Classe Groupe enrichie
import Education.Note;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * ========================================================
 * ALGORITHMEGROUPESFRAME.JAVA - Version Finale Autonome
 * ========================================================
 * Contient l'interface et l'implémentation directe des 5 algorithmes.
 */
public class AlgorithmeGroupesFrame extends JFrame {

    private final MainController controleur;
    private final RestService api;
    private JSpinner selecteurNombreGroupes;
    private JComboBox<String> listePromotions;
    private JComboBox<String> listeMethodes;
    private DefaultTableModel modeleResultat;
    private JLabel labelScore;
    private JLabel labelStatus;

    private List<Etudiant> etudiantsCharges;
    private List<Groupe> groupesCrees;
    private String algorithmeChoisi; // Pour stocker le nom de l'algorithme optimal

    private final Runnable callbackMiseAJour;

    public AlgorithmeGroupesFrame(MainController controleur, Runnable callbackMiseAJour) {
        this.controleur = controleur;
        this.api = new RestService();
        this.callbackMiseAJour = callbackMiseAJour;
        this.etudiantsCharges = new ArrayList<>();
        this.groupesCrees = new ArrayList<>();
        initialiserComposants();
    }

    public AlgorithmeGroupesFrame(MainController controleur) {
        this(controleur, () -> {
        });
    }

    // =================================================================
    // INITIALISATION UI (Sidebar Moderne)
    // =================================================================

    private void initialiserComposants() {
        setTitle("Algorithme de Creation de Groupes - Final");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(320, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("PARAMETRES");
        titleLabel.setFont(Theme.FONT_SUBTITLE);
        titleLabel.setForeground(new Color(200, 200, 200));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(20));

        JPanel configPanel = creerPanneauConfigurationSidebar();
        configPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(configPanel);

        sidebar.add(Box.createVerticalStrut(20));

        JPanel contraintesPanel = creerPanneauContraintesSidebar();
        contraintesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(contraintesPanel);

        sidebar.add(Box.createVerticalStrut(30));

        // BOUTONS
        addSidebarButton(sidebar, "1. Charger Etudiants", Theme.ACCENT, e -> chargerEtudiants());
        sidebar.add(Box.createVerticalStrut(10));
        addSidebarButton(sidebar, "2. LANCER ALGO", Theme.PRIMARY, e -> lancerAlgorithme());
        sidebar.add(Box.createVerticalStrut(10));
        addSidebarButton(sidebar, "3. Sauvegarder BDD", Theme.SUCCESS, e -> sauvegarderGroupes());
        sidebar.add(Box.createVerticalStrut(10));
        addSidebarButton(sidebar, "4. Voir Statistiques", Theme.PRIMARY, e -> afficherStatistiques());

        sidebar.add(Box.createVerticalGlue());

        labelScore = new JLabel("Score: ---");
        labelScore.setFont(Theme.FONT_TITLE);
        labelScore.setForeground(Color.WHITE);
        labelScore.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(labelScore);

        sidebar.add(Box.createVerticalStrut(20));
        addSidebarButton(sidebar, "Tout Effacer en BDD", Theme.DANGER, e -> reinitialiserGroupes());

        add(sidebar, BorderLayout.WEST);

        // --- CONTENU ---
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(Theme.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(Theme.BACKGROUND);
        JLabel mainTitle = new JLabel("Résultats de la répartition");
        mainTitle.setFont(Theme.FONT_TITLE);
        mainTitle.setForeground(Theme.SECONDARY);
        headerContent.add(mainTitle, BorderLayout.WEST);

        JButton btnRetour = new JButton("Retour Tableau de Bord");
        styleFlatButton(btnRetour, Color.LIGHT_GRAY, Color.BLACK);
        btnRetour.addActionListener(e -> dispose());
        headerContent.add(btnRetour, BorderLayout.EAST);
        contentPanel.add(headerContent, BorderLayout.NORTH);

        String[] colonnes = { "Groupe", "ID", "Nom", "Prénom", "Bac", "Moyenne", "Java", "BD" };
        modeleResultat = new DefaultTableModel(colonnes, 0);
        JTable tableauResultat = new JTable(modeleResultat);
        tableauResultat.setFillsViewportHeight(true);
        tableauResultat.setRowHeight(30);
        tableauResultat.setFont(Theme.FONT_REGULAR);
        tableauResultat.getTableHeader().setBackground(Theme.SECONDARY);
        tableauResultat.getTableHeader().setForeground(Color.WHITE);
        tableauResultat.getTableHeader().setFont(Theme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(tableauResultat);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        labelStatus = new JLabel("Prêt.");
        labelStatus.setForeground(Color.GRAY);
        contentPanel.add(labelStatus, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void addSidebarButton(JPanel sidebar, String text, Color color, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        styleFlatButton(btn, color, Color.WHITE);
        btn.setMaximumSize(new Dimension(280, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(action);
        sidebar.add(btn);
    }

    private void styleFlatButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(Theme.FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel creerPanneauConfigurationSidebar() {
        JPanel p = new JPanel(new GridLayout(6, 1, 5, 5));
        p.setOpaque(false);
        p.add(labelWhite("Nombre de groupes :"));
        selecteurNombreGroupes = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        p.add(selecteurNombreGroupes);
        p.add(labelWhite("Promotion :"));
        listePromotions = new JComboBox<>(new String[] { "2024 - BUT2", "2025 - BUT1", "2023 - BUT3" });
        p.add(listePromotions);
        p.add(labelWhite("Méthode :"));
        listeMethodes = new JComboBox<>(new String[] {
                "Glouton Moyenne (Base)",
                "Glouton Bac (Base)",
                "Aléatoire (Base)",
                "Glouton 1 - Utilité (Expert)",
                "Glouton 2 - Vagues (Expert)",
                "🎯 Algorithme Optimal (Auto)"
        });
        p.add(listeMethodes);
        return p;
    }

    private JPanel creerPanneauContraintesSidebar() {
        JPanel p = new JPanel(new GridLayout(5, 1, 5, 5));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
                "Contraintes", 0, 0, Theme.FONT_SMALL, Color.LIGHT_GRAY));
        p.add(checkWhite("Equil. Moyennes"));
        p.add(checkWhite("Equil. Mixité H/F"));
        p.add(labelWhite("Glouton 1 : Rareté"));
        p.add(labelWhite("Glouton 2 : Vagues"));
        return p;
    }

    private JLabel labelWhite(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    private JCheckBox checkWhite(String text) {
        JCheckBox c = new JCheckBox(text, true);
        c.setForeground(Color.WHITE);
        c.setOpaque(false);
        return c;
    }

    // =================================================================
    // 1. CHARGEMENT ET SIMULATION
    // =================================================================

    private void chargerEtudiants() {
        labelStatus.setText("Chargement en cours...");

        // Extraire l'ID de promotion depuis la combobox
        int idPromo = 2024; // Default BUT2
        String promoSelection = (String) listePromotions.getSelectedItem();
        if (promoSelection != null) {
            // Format attendu: "2024 - BUT2" ou "BUT2A (ID:2024)"
            if (promoSelection.contains("ID:")) {
                String idStr = promoSelection.substring(promoSelection.indexOf("ID:") + 3, promoSelection.indexOf(")"));
                idPromo = Integer.parseInt(idStr.trim());
            } else if (promoSelection.startsWith("2024")) {
                idPromo = 2024; // BUT2
            } else if (promoSelection.startsWith("2025")) {
                idPromo = 2025; // BUT1
            } else if (promoSelection.startsWith("2023")) {
                idPromo = 2023; // BUT3
            }
        }

        final int finalIdPromo = idPromo;
        new Thread(() -> {
            String json = api.getEtudiantsByPromo(finalIdPromo);
            if (json != null && json.startsWith("[")) {
                etudiantsCharges.clear();
                List<String> objets = JsonParser.extraireObjets(json);
                Random rand = new Random();

                for (String obj : objets) {
                    try {
                        int id = Integer.parseInt(JsonParser.extraireValeur(obj, "id_etu"));
                        String nom = JsonParser.extraireValeur(obj, "nom_etu");
                        String prenom = JsonParser.extraireValeur(obj, "prenom_etu");
                        String email = JsonParser.extraireValeur(obj, "courriel_etu");
                        String genre = JsonParser.extraireValeur(obj, "genre_etu");

                        // Parse Moyenne
                        float moy = 10.0f;
                        String moyStr = JsonParser.extraireValeur(obj, "moyenne");
                        if (!moyStr.isEmpty())
                            try {
                                moy = Float.parseFloat(moyStr);
                            } catch (Exception e) {
                            }
                        else
                            moy = (float) (8 + Math.random() * 8);

                        // --- SIMULATION POUR GLOUTON 1 & 2 ---
                        String typeBac = rand.nextInt(100) < 60 ? "General" : "Techno";
                        boolean redoublant = rand.nextInt(100) < 10;
                        boolean apprenti = rand.nextInt(100) < 15;

                        float noteJava, noteBD;
                        int profil = rand.nextInt(100);
                        if (profil < 15) {
                            noteJava = genererNote(14, 20);
                            noteBD = genererNote(8, 14);
                        } // Dev
                        else if (profil < 30) {
                            noteJava = genererNote(8, 14);
                            noteBD = genererNote(14, 20);
                        } // Data
                        else if (profil < 40) {
                            noteJava = genererNote(15, 20);
                            noteBD = genererNote(15, 20);
                        } // Fort
                        else {
                            noteJava = genererNote(6, 14);
                            noteBD = genererNote(6, 14);
                        } // Std

                        Etudiant e = new Etudiant(nom, prenom, genre, email, "Inconnu", "000",
                                id, 20, moy, 2, apprenti, redoublant, typeBac);

                        e.ajouterNote(new Note(0, "Java", noteJava));
                        e.ajouterNote(new Note(0, "BD", noteBD));

                        etudiantsCharges.add(e);
                    } catch (Exception ex) {
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    labelStatus.setText(etudiantsCharges.size() + " étudiants chargés (Simulés).");
                    afficherResultats();
                });
            } else {
                SwingUtilities.invokeLater(() -> labelStatus.setText("Erreur chargement."));
            }
        }).start();
    }

    // =================================================================
    // 2. ORCHESTRATION DES ALGOS
    // =================================================================

    private void lancerAlgorithme() {
        if (etudiantsCharges.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chargez d'abord les étudiants !");
            return;
        }
        labelStatus.setText("Calcul...");
        int nbGroupes = (Integer) selecteurNombreGroupes.getValue();
        int methode = listeMethodes.getSelectedIndex();
        int tailleMax = (int) Math.ceil((double) etudiantsCharges.size() / nbGroupes) + 2;

        // Si mode optimal, déterminer le meilleur algorithme
        if (methode == 5) {
            labelStatus.setText("Analyse des algorithmes...");
            methode = determinerAlgorithmeOptimal(nbGroupes, tailleMax);
            String[] nomsAlgos = {
                    "Glouton Moyenne (Base)",
                    "Glouton Bac (Base)",
                    "Aléatoire (Base)",
                    "Glouton 1 - Utilité (Expert)",
                    "Glouton 2 - Vagues (Expert)"
            };
            algorithmeChoisi = nomsAlgos[methode];
        } else {
            algorithmeChoisi = null;
        }

        groupesCrees.clear();
        // Init Groupes
        for (int i = 0; i < nbGroupes; i++) {
            groupesCrees.add(new Groupe(i + 1, "Groupe " + (i + 1), null));
        }

        long start = System.currentTimeMillis();

        switch (methode) {
            case 0:
                appliquerGloutonMoyenne(nbGroupes);
                break;
            case 1:
                appliquerGloutonBac(nbGroupes);
                break;
            case 2:
                appliquerAleatoire(nbGroupes);
                break;
            case 3:
                appliquerGlouton1(nbGroupes, tailleMax);
                break;
            case 4:
                appliquerGlouton2(nbGroupes, tailleMax);
                break;
        }

        long end = System.currentTimeMillis();

        // Score pour l'interface
        double score = 0;
        for (Groupe g : groupesCrees) {
            if (methode >= 3) {
                if (g.aUnLeaderJava())
                    score += 10;
                if (g.aUnLeaderBD())
                    score += 10;
            } else {
                score += g.calculerMoyenneGroupe();
            }
        }
        if (methode < 3)
            score /= nbGroupes; // Moyenne pour les algos simples

        labelScore.setText(String.format("Score: %.1f", score));

        // Afficher quel algorithme a été choisi en mode optimal
        String statusMsg = "Terminé en " + (end - start) + "ms";
        if (algorithmeChoisi != null) {
            statusMsg = "✓ Algorithme optimal sélectionné: " + algorithmeChoisi + " (" + (end - start) + "ms)";
        }
        labelStatus.setText(statusMsg);
        afficherResultats();
    }

    // =================================================================
    // 3. IMPLEMENTATION DES 5 ALGORITHMES
    // =================================================================

    // --- Base : Moyenne ---
    private void appliquerGloutonMoyenne(int nbGroupes) {
        List<Etudiant> tries = new ArrayList<>(etudiantsCharges);
        tries.sort((a, b) -> Double.compare(b.getMoyenne(), a.getMoyenne()));
        for (Etudiant e : tries) {
            int meilleurGroupe = 0;
            int tailleMin = Integer.MAX_VALUE;
            double moyMin = Double.MAX_VALUE;
            for (int i = 0; i < nbGroupes; i++) {
                Groupe g = groupesCrees.get(i);
                if (g.getNombreMembres() < tailleMin
                        || (g.getNombreMembres() == tailleMin && g.calculerMoyenneGroupe() < moyMin)) {
                    tailleMin = g.getNombreMembres();
                    moyMin = g.calculerMoyenneGroupe();
                    meilleurGroupe = i;
                }
            }
            groupesCrees.get(meilleurGroupe).ajouterMembre(e);
        }
    }

    // --- Base : Bac ---
    private void appliquerGloutonBac(int nbGroupes) {
        List<Etudiant> generaux = new ArrayList<>();
        List<Etudiant> technos = new ArrayList<>();
        for (Etudiant e : etudiantsCharges) {
            if ("General".equals(e.getTypeBac()))
                generaux.add(e);
            else
                technos.add(e);
        }
        int index = 0;
        for (Etudiant e : generaux) {
            groupesCrees.get(index % nbGroupes).ajouterMembre(e);
            index++;
        }
        for (Etudiant e : technos) {
            groupesCrees.get(index % nbGroupes).ajouterMembre(e);
            index++;
        }
    }

    // --- Base : Aléatoire ---
    private void appliquerAleatoire(int nbGroupes) {
        List<Etudiant> melange = new ArrayList<>(etudiantsCharges);
        Collections.shuffle(melange);
        int i = 0;
        for (Etudiant e : melange) {
            groupesCrees.get(i % nbGroupes).ajouterMembre(e);
            i++;
        }
    }

    // --- Expert : Glouton 1 (Utilité/Rareté) ---
    private void appliquerGlouton1(int nbGroupes, int tailleMax) {
        List<Etudiant> ordre = new ArrayList<>(etudiantsCharges);
        ordre.sort((e1, e2) -> calculerRaret(e2) - calculerRaret(e1));

        for (Etudiant e : ordre) {
            Groupe meilleurG = null;
            double meilleurScore = Double.MAX_VALUE;

            for (Groupe g : groupesCrees) {
                if (g.getNombreMembres() >= tailleMax)
                    continue;
                double score = 0;
                if (estLeaderJava(e))
                    score += (!g.aUnLeaderJava()) ? -10000 : 100;
                if (estLeaderBD(e))
                    score += (!g.aUnLeaderBD()) ? -10000 : 100;

                boolean isGen = "General".equals(e.getTypeBac());
                if (isGen && g.nbGeneral() > g.nbTechno() + 1)
                    score += 500;
                if (!isGen && g.nbTechno() > g.nbGeneral() + 1)
                    score += 500;

                score += g.getNombreMembres() * 10;
                if (score < meilleurScore) {
                    meilleurScore = score;
                    meilleurG = g;
                }
            }
            if (meilleurG == null)
                for (Groupe g : groupesCrees)
                    if (g.getNombreMembres() < tailleMax + 2) {
                        meilleurG = g;
                        break;
                    }
            if (meilleurG != null)
                meilleurG.ajouterMembre(e);
        }
    }

    // --- Expert : Glouton 2 (Vagues) ---
    private void appliquerGlouton2(int nbGroupes, int tailleMax) {
        LinkedList<Etudiant> expertsJava = new LinkedList<>();
        LinkedList<Etudiant> expertsBD = new LinkedList<>();
        LinkedList<Etudiant> autres = new LinkedList<>();

        for (Etudiant e : etudiantsCharges) {
            if (estLeaderJava(e))
                expertsJava.add(e);
            else if (estLeaderBD(e))
                expertsBD.add(e);
            else
                autres.add(e);
        }

        int index = 0;
        while (!expertsJava.isEmpty()) {
            Groupe g = groupesCrees.get(index % nbGroupes);
            if (g.getNombreMembres() < tailleMax)
                g.ajouterMembre(expertsJava.poll());
            index++;
        }

        groupesCrees.sort((g1, g2) -> Boolean.compare(g1.aUnLeaderBD(), g2.aUnLeaderBD()));
        for (Groupe g : groupesCrees) {
            if (!expertsBD.isEmpty() && g.getNombreMembres() < tailleMax && !g.aUnLeaderBD())
                g.ajouterMembre(expertsBD.poll());
        }
        while (!expertsBD.isEmpty()) {
            Groupe g = groupesCrees.get(index % nbGroupes);
            if (g.getNombreMembres() < tailleMax)
                g.ajouterMembre(expertsBD.poll());
            index++;
        }
        groupesCrees.sort(Comparator.comparingInt(Groupe::getIdGroupe));

        for (Etudiant e : autres) {
            Groupe meilleurG = null;
            double meilleurScore = Double.MAX_VALUE;
            for (Groupe g : groupesCrees) {
                if (g.getNombreMembres() >= tailleMax)
                    continue;
                double score = g.getNombreMembres();
                boolean isGen = "General".equals(e.getTypeBac());
                if (isGen && g.nbGeneral() > g.nbTechno())
                    score += 5;
                if (!isGen && g.nbTechno() > g.nbGeneral())
                    score += 5;
                if (score < meilleurScore) {
                    meilleurScore = score;
                    meilleurG = g;
                }
            }
            if (meilleurG == null)
                for (Groupe g : groupesCrees)
                    if (g.getNombreMembres() < tailleMax + 2) {
                        meilleurG = g;
                        break;
                    }
            if (meilleurG != null)
                meilleurG.ajouterMembre(e);
        }
    }

    // =================================================================
    // 4. AFFICHAGE & SAUVEGARDE
    // =================================================================

    private void afficherResultats() {
        modeleResultat.setRowCount(0);

        List<Etudiant> source = groupesCrees.isEmpty() ? etudiantsCharges : null;
        if (source != null && !source.isEmpty() && groupesCrees.isEmpty()) {
            for (Etudiant e : source)
                ajouterLigneTable("-", e);
        } else {
            for (Groupe g : groupesCrees) {
                for (Etudiant e : g.getSesMembres()) {
                    ajouterLigneTable(g.getLibelle(), e);
                }
            }
        }
    }

    private void ajouterLigneTable(String grpName, Etudiant e) {
        modeleResultat.addRow(new Object[] {
                grpName, e.getId_etu(), e.getNom(), e.getPrenom(),
                e.getTypeBac(), String.format("%.2f", e.getMoyenne()),
                String.format("%.1f", getNote(e, "Java")),
                String.format("%.1f", getNote(e, "BD"))
        });
    }

    private void afficherStatistiques() {
        if (groupesCrees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lancez l'algorithme d'abord.");
            return;
        }
        int methode = listeMethodes.getSelectedIndex();
        boolean modeAvance = (methode >= 3);

        StringBuilder sb = new StringBuilder();
        sb.append("=== STATISTIQUES DÉTAILLÉES ===\n\n");

        double totalScore = 0;
        for (Groupe g : groupesCrees) {
            sb.append("■ ").append(g.getLibelle()).append(" (").append(g.getNombreMembres()).append(" étu.)\n");

            int nbG = g.nbGeneral();
            int nbT = g.nbTechno();
            int nbF = g.getNbFilles();
            float moy = g.calculerMoyenneGroupe();

            sb.append("   - Moyenne : ").append(String.format("%.2f", moy)).append("/20\n");
            sb.append("   - Bacs    : ").append(nbG).append(" G / ").append(nbT).append(" T\n");
            sb.append("   - Femmes  : ").append(nbF).append("\n");

            if (modeAvance) {
                boolean hJ = g.aUnLeaderJava();
                boolean hB = g.aUnLeaderBD();
                sb.append("   - Java    : ").append(hJ ? "OUI (+10)" : "NON").append("\n");
                sb.append("   - BD      : ").append(hB ? "OUI (+10)" : "NON").append("\n");
                if (hJ)
                    totalScore += 10;
                if (hB)
                    totalScore += 10;
            }
            sb.append("---------------------------------\n");
        }
        if (modeAvance)
            sb.append("\nScore Technique Total : ").append(totalScore);

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane p = new JScrollPane(textArea);
        p.setPreferredSize(new Dimension(400, 500));
        JOptionPane.showMessageDialog(this, p, "Détails", JOptionPane.INFORMATION_MESSAGE);
    }

    private void sauvegarderGroupes() {
        if (groupesCrees.isEmpty())
            return;
        labelStatus.setText("Sauvegarde...");

        final String ts = String.valueOf(System.currentTimeMillis() % 10000);
        // Copie des groupes pour le thread
        final List<Groupe> snapshot = new ArrayList<>(groupesCrees);

        new Thread(() -> {
            int created = 0;
            int assigned = 0;
            Map<Integer, Integer> mapId = new HashMap<>();

            for (int i = 0; i < snapshot.size(); i++) {
                String nom = "G" + (i + 1) + "-" + ts;
                String json = "{\"nom\":\"" + nom + "\",\"id_promo\":2024}";
                String resp = api.post("/groupes", json);
                if (resp != null && resp.contains("success")) {
                    String idStr = JsonParser.extraireValeur(resp, "id");
                    if (!idStr.isEmpty()) {
                        mapId.put(i, Integer.parseInt(idStr));
                        created++;
                    }
                }
            }

            for (int i = 0; i < snapshot.size(); i++) {
                Integer idG = mapId.get(i);
                if (idG == null)
                    continue;
                for (Etudiant e : snapshot.get(i).getSesMembres()) {
                    api.put("/etudiants/" + e.getId_etu(), "{\"id_groupe\":" + idG + "}");
                    assigned++;
                }
            }

            final int c = created;
            final int a = assigned;
            SwingUtilities.invokeLater(() -> {
                labelStatus.setText("Sauvegardé.");
                JOptionPane.showMessageDialog(this, "Sauvegarde : " + c + " groupes, " + a + " assignations.");
            });
        }).start();
    }

    private void reinitialiserGroupes() {
        int choix = JOptionPane.showConfirmDialog(this, "Tout supprimer ?", "Attention", JOptionPane.YES_NO_OPTION);
        if (choix != JOptionPane.YES_OPTION)
            return;

        new Thread(() -> {
            try {
                // Utiliser la promo sélectionnée
                int idPromo = 2024;
                String promoSelection = (String) listePromotions.getSelectedItem();
                if (promoSelection != null && promoSelection.startsWith("2024"))
                    idPromo = 2024;
                else if (promoSelection != null && promoSelection.startsWith("2025"))
                    idPromo = 2025;
                else if (promoSelection != null && promoSelection.startsWith("2023"))
                    idPromo = 2023;

                String jsonEtudiants = api.getEtudiantsByPromo(idPromo);
                List<String> etudiants = JsonParser.extraireObjets(jsonEtudiants);
                for (String etuJson : etudiants) {
                    String id = JsonParser.extraireValeur(etuJson, "id_etu");
                    String idGrp = JsonParser.extraireValeur(etuJson, "id_groupe");
                    if (!id.isEmpty() && !idGrp.isEmpty() && !idGrp.equals("0") && !idGrp.equals("null"))
                        api.put("/etudiants/" + id, "{\"id_groupe\":null}");
                }
                String jsonGroupes = api.getGroupes();
                List<String> groupes = JsonParser.extraireObjets(jsonGroupes);
                for (String grpJson : groupes) {
                    String id = JsonParser.extraireValeur(grpJson, "id_groupe");
                    if (!id.isEmpty())
                        api.delete("/groupes/" + id);
                }
                SwingUtilities.invokeLater(() -> {
                    groupesCrees.clear();
                    modeleResultat.setRowCount(0);
                    labelStatus.setText("Tout effacé.");
                    JOptionPane.showMessageDialog(this, "Base nettoyée.");
                });
            } catch (Exception e) {
            }
        }).start();
    }

    // =================================================================
    // HELPERS INTERNES
    // =================================================================

    private double getNote(Etudiant e, String type) {
        if (e.getSesNotes() == null)
            return 0.0;
        for (Note n : e.getSesNotes())
            if (n.getType().equalsIgnoreCase(type))
                return n.getValeur();
        return 0.0;
    }

    private float genererNote(int min, int max) {
        return (float) ((min + new Random().nextDouble() * (max - min) + min + new Random().nextDouble() * (max - min))
                / 2.0);
    }

    private boolean estLeaderJava(Etudiant e) {
        return getNote(e, "Java") >= 14;
    }

    private boolean estLeaderBD(Etudiant e) {
        return getNote(e, "BD") >= 14;
    }

    private int calculerRaret(Etudiant e) {
        int r = 0;
        if (estLeaderJava(e))
            r += 10;
        if (estLeaderBD(e))
            r += 10;
        return r;
    }

    // =================================================================
    // DETERMINATION ALGORITHME OPTIMAL
    // =================================================================

    /**
     * Détermine quel algorithme donne les meilleurs résultats sur un échantillon
     * 
     * @param nbGroupes Nombre de groupes souhaités
     * @param tailleMax Taille maximale d'un groupe
     * @return L'index de l'algorithme optimal (0-4)
     */
    private int determinerAlgorithmeOptimal(int nbGroupes, int tailleMax) {
        // Créer un échantillon (30% des étudiants, minimum 20)
        int tailleEchantillon = Math.max(20, (int) (etudiantsCharges.size() * 0.3));
        tailleEchantillon = Math.min(tailleEchantillon, etudiantsCharges.size());

        List<Etudiant> echantillon = new ArrayList<>(etudiantsCharges.subList(0, tailleEchantillon));

        double meilleurScore = -1;
        int meilleurAlgo = 0;

        // Tester chaque algorithme
        for (int algo = 0; algo <= 4; algo++) {
            double score = testerAlgorithme(algo, echantillon, nbGroupes, tailleMax);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurAlgo = algo;
            }
        }

        return meilleurAlgo;
    }

    /**
     * Teste un algorithme sur un échantillon et retourne son score
     * 
     * @param algo      Index de l'algorithme (0-4)
     * @param etudiants Liste des étudiants
     * @param nbGroupes Nombre de groupes
     * @param tailleMax Taille max d'un groupe
     * @return Score de l'algorithme (plus grand = meilleur)
     */
    private double testerAlgorithme(int algo, List<Etudiant> etudiants, int nbGroupes, int tailleMax) {
        // Créer des groupes temporaires
        List<Groupe> groupesTemp = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupesTemp.add(new Groupe(i + 1, "Groupe " + (i + 1), null));
        }

        // Sauvegarder l'état actuel et utiliser l'échantillon temporairement
        List<Etudiant> sauvegarde = etudiantsCharges;
        List<Groupe> sauvegardeGroupes = groupesCrees;

        etudiantsCharges = new ArrayList<>(etudiants);
        groupesCrees = groupesTemp;

        // Appliquer l'algorithme
        switch (algo) {
            case 0:
                appliquerGloutonMoyenne(nbGroupes);
                break;
            case 1:
                appliquerGloutonBac(nbGroupes);
                break;
            case 2:
                appliquerAleatoire(nbGroupes);
                break;
            case 3:
                appliquerGlouton1(nbGroupes, tailleMax);
                break;
            case 4:
                appliquerGlouton2(nbGroupes, tailleMax);
                break;
        }

        // Calculer le score
        double score = calculerScoreAlgorithme(algo, groupesTemp);

        // Restaurer l'état
        etudiantsCharges = sauvegarde;
        groupesCrees = sauvegardeGroupes;

        return score;
    }

    /**
     * Calcule le score d'un algorithme selon son type
     * 
     * @param algo    Index de l'algorithme
     * @param groupes Liste des groupes créés
     * @return Score (plus grand = meilleur)
     */
    private double calculerScoreAlgorithme(int algo, List<Groupe> groupes) {
        double score = 0;

        if (algo <= 2) {
            // Algorithmes base : maximiser la moyenne
            for (Groupe g : groupes) {
                score += g.calculerMoyenneGroupe();
            }
            score /= groupes.size();
        } else {
            // Algorithmes expert : maximiser la couverture des leaders
            for (Groupe g : groupes) {
                if (g.aUnLeaderJava())
                    score += 10;
                if (g.aUnLeaderBD())
                    score += 10;
            }
        }

        return score;
    }
}