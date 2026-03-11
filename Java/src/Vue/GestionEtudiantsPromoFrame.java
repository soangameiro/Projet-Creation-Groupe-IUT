package Vue;

import Controleur.MainController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Écran 2 : Gestion des Étudiants par Promotion
 */
public class GestionEtudiantsPromoFrame extends JFrame {

    private final MainController controller;
    private JComboBox<String> comboPromo;
    private JTable tableEtudiants;
    private DefaultTableModel tableModel;

    public GestionEtudiantsPromoFrame(MainController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestion des Étudiants par Promotion");
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel top avec sélecteur de promo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Promotion:"));

        comboPromo = new JComboBox<>(new String[] { "BUT2 Info 2024", "BUT3 Alterne 2024" });
        comboPromo.addActionListener(e -> loadEtudiants());
        topPanel.add(comboPromo);

        JButton btnRefresh = new JButton("Actualiser");
        btnRefresh.addActionListener(e -> loadEtudiants());
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // Table étudiants
        String[] cols = { "ID", "Nom", "Prénom", "Âge", "Moyenne", "Apprenti", "Redoublant", "Bac" };
        tableModel = new DefaultTableModel(cols, 0);
        tableEtudiants = new JTable(tableModel);
        add(new JScrollPane(tableEtudiants), BorderLayout.CENTER);

        loadEtudiants();
    }

    private void loadEtudiants() {
        tableModel.setRowCount(0);

        // TODO: GET /api/promotions/{id_promo}/etudiants

        // Données de test
        tableModel.addRow(new Object[] { 2023001, "Dupont", "Alice", 20, 14.5, "Non", "Non", "Général" });
        tableModel.addRow(new Object[] { 2023002, "Martin", "Bob", 21, 11.2, "Oui", "Oui", "STI2D" });
    }
}
