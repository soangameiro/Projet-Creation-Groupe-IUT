package Vue;

import Controleur.MainController;
import javax.swing.*;
import java.awt.*;

/**
 * Écran 4 : Portail Étudiant avec Consultation et Sondages
 */
public class PortailEtudiantFrame extends JFrame {

    private final MainController controller;
    private final int idEtudiant;
    private JLabel lblNom;
    private JLabel lblPrenom;
    private JLabel lblCovoiturage;
    private DefaultListModel<String> listModelSondages;

    public PortailEtudiantFrame(MainController controller, int idEtudiant) {
        this.controller = controller;
        this.idEtudiant = idEtudiant;
        initComponents();
        loadInfos();
    }

    private void initComponents() {
        setTitle("Mon Portail Étudiant");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel infos personnelles
        JPanel infosPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infosPanel.setBorder(BorderFactory.createTitledBorder("Mes Informations"));

        infosPanel.add(new JLabel("Nom:"));
        lblNom = new JLabel("...");
        infosPanel.add(lblNom);

        infosPanel.add(new JLabel("Prénom:"));
        lblPrenom = new JLabel("...");
        infosPanel.add(lblPrenom);

        infosPanel.add(new JLabel("Covoiturage:"));
        lblCovoiturage = new JLabel("Aucun");
        infosPanel.add(lblCovoiturage);

        add(infosPanel, BorderLayout.NORTH);

        // Panel sondages
        JPanel sondagesPanel = new JPanel(new BorderLayout());
        sondagesPanel.setBorder(BorderFactory.createTitledBorder("Sondages Disponibles"));

        listModelSondages = new DefaultListModel<>();
        JList<String> listSondages = new JList<>(listModelSondages);
        sondagesPanel.add(new JScrollPane(listSondages), BorderLayout.CENTER);

        JButton btnRepondre = new JButton("Répondre au sondage");
        btnRepondre.addActionListener(e -> {
            String selected = listSondages.getSelectedValue();
            if (selected != null) {
                repondreSondage(selected);
            }
        });
        sondagesPanel.add(btnRepondre, BorderLayout.SOUTH);

        add(sondagesPanel, BorderLayout.CENTER);
    }

    private void loadInfos() {
        // TODO: GET /api/etudiants/{id_etu}/infos

        // Simulation
        lblNom.setText("Dupont");
        lblPrenom.setText("Alice");
        lblCovoiturage.setText("Aucun");

        listModelSondages.addElement("Choix de l'option Anglais");
        listModelSondages.addElement("Thème de projet S3");
    }

    private void repondreSondage(String sondage) {
        String reponse = JOptionPane.showInputDialog(this,
                "Réponse pour: " + sondage);

        if (reponse != null) {
            // TODO: POST /api/sondages/{id}/reponses
            JOptionPane.showMessageDialog(this, "Réponse enregistrée!");
        }
    }
}
