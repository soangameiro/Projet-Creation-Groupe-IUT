package Vue;

import Controleur.MainController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Écran 1 : Gestion des Enseignants (Responsable de Formation)
 */
public class GestionEnseignantsFrame extends JFrame {

    private final MainController controller;
    private JTable tableEnseignants;
    private DefaultTableModel tableModel;

    public GestionEnseignantsFrame(MainController controller) {
        this.controller = controller;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Gestion des Enseignants");
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterEnseignant());
        btnModifier.addActionListener(e -> modifierEnseignant());
        btnSupprimer.addActionListener(e -> supprimerEnseignant());

        toolbar.add(btnAjouter);
        toolbar.add(btnModifier);
        toolbar.add(btnSupprimer);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Nom", "Prénom", "Genre", "Email", "Type" };
        tableModel = new DefaultTableModel(cols, 0);
        tableEnseignants = new JTable(tableModel);
        add(new JScrollPane(tableEnseignants), BorderLayout.CENTER);
    }

    private void loadData() {
        // TODO: Appeler GET /api/enseignants
        tableModel.setRowCount(0);

        // Données de test
        tableModel.addRow(new Object[] { 1, "Férey", "Nicolas", "M", "nicolas.ferey@univ.fr", "Responsable" });
        tableModel.addRow(new Object[] { 2, "Hellouin", "Benjamin", "M", "benjamin.hdm@univ.fr", "Enseignant" });
    }

    private void ajouterEnseignant() {
        // TODO: Afficher formulaire d'ajout
        String nom = JOptionPane.showInputDialog(this, "Nom:");
        String prenom = JOptionPane.showInputDialog(this, "Prénom:");

        if (nom != null && prenom != null) {
            // TODO: POST /api/enseignants
            JOptionPane.showMessageDialog(this, "Enseignant ajouté (simulation)");
            loadData();
        }
    }

    private void modifierEnseignant() {
        int row = tableEnseignants.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant");
            return;
        }
        // TODO: PUT /api/enseignants/{id}
        JOptionPane.showMessageDialog(this, "Modification (simulation)");
    }

    private void supprimerEnseignant() {
        int row = tableEnseignants.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression?");
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: DELETE /api/enseignants/{id}
            tableModel.removeRow(row);
        }
    }
}
