package Vue;

import javax.swing.*;
import java.awt.*;

/**
 * ========================================================
 * BUTTONFACTORY.JAVA - Fabrique de boutons stylisés
 * ========================================================
 * 
 * Cette classe utilise le DESIGN PATTERN "Factory" (Fabrique).
 * 
 * PRINCIPE :
 * Au lieu de créer des boutons manuellement à chaque fois :
 *     JButton btn = new JButton("Texte");
 *     btn.setBackground(...);
 *     btn.setForeground(...);
 *     ... (10 lignes de configuration)
 * 
 * On utilise une méthode qui fait tout ça pour nous :
 *     JButton btn = ButtonFactory.principal("Texte");
 * 
 * AVANTAGES :
 * - Code plus court et lisible
 * - Tous les boutons ont le même style (cohérence visuelle)
 * - Pour modifier l'apparence, on change UNE SEULE FOIS ici
 * 
 * @author SAE S3 - Groupe
 */
public class ButtonFactory {

    /**
     * Crée un bouton avec des coins arrondis et un effet de survol.
     * 
     * Cette méthode est la BASE pour créer tous les boutons stylisés.
     * Elle utilise une classe anonyme (JButton() { ... }) pour
     * personnaliser le dessin du bouton.
     * 
     * @param texte       Le texte affiché sur le bouton
     * @param couleurFond Couleur de fond normale
     * @param couleurHover Couleur quand la souris survole le bouton
     * @param couleurTexte Couleur du texte
     * @return Un JButton configuré et prêt à utiliser
     */
    public static JButton creerBoutonArrondi(String texte, Color couleurFond, 
                                              Color couleurHover, Color couleurTexte) {
        
        // Création d'une CLASSE ANONYME qui hérite de JButton
        // On redéfinit paintComponent pour dessiner le bouton nous-mêmes
        JButton bouton = new JButton(texte) {
            
            /**
             * Cette méthode est appelée automatiquement par Java
             * chaque fois que le bouton doit être redessiné.
             */
            @Override
            protected void paintComponent(Graphics g) {
                // Graphics2D offre plus de fonctionnalités que Graphics de base
                Graphics2D g2 = (Graphics2D) g.create();
                
                // Activer l'antialiasing = bords lisses (pas de pixels visibles)
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Choisir la couleur selon l'état du bouton
                if (getModel().isRollover()) {
                    // isRollover() = true quand la souris survole le bouton
                    g2.setColor(couleurHover);
                } else {
                    g2.setColor(getBackground());
                }
                
                // Dessiner un rectangle arrondi (15 pixels de rayon pour les coins)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Appeler la méthode parente pour dessiner le texte
                super.paintComponent(g2);
                
                // Libérer les ressources graphiques
                g2.dispose();
            }
        };
        
        // Configuration du bouton
        bouton.setBackground(couleurFond);
        bouton.setForeground(couleurTexte);
        bouton.setFocusPainted(false);      // Pas de rectangle de focus visible
        bouton.setBorderPainted(false);     // Pas de bordure
        bouton.setContentAreaFilled(false); // On dessine le fond nous-mêmes
        bouton.setOpaque(false);            // Bouton transparent (on gère le dessin)
        bouton.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 13f));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur "main" au survol

        // Marge interne (haut, gauche, bas, droite)
        bouton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return bouton;
    }

    // =================================================================
    // METHODES RACCOURCIS - Boutons prédéfinis avec les bonnes couleurs
    // =================================================================

    /**
     * Bouton PRINCIPAL - Pour les actions importantes.
     * Couleur : Bordeaux (PAPRIKA)
     * 
     * Exemples : "SE CONNECTER", "VALIDER", "ENREGISTRER"
     */
    public static JButton principal(String texte) {
        return creerBoutonArrondi(texte, 
                                   Theme.PAPRIKA,   // Fond bordeaux
                                   Theme.CADILLAC,  // Survol rose foncé
                                   Theme.BON_JOUR); // Texte blanc/clair
    }

    /**
     * Bouton NORMAL - Pour les actions secondaires.
     * Style : Contour gris, fond clair
     * 
     * Exemples : "Actualiser", "Annuler", "Fermer"
     */
    public static JButton normal(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setForeground(Theme.COD_GRAY);   // Texte noir
        bouton.setBackground(Theme.BON_JOUR);   // Fond clair
        bouton.setFont(Theme.BODY_FONT);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bordure composée = bordure extérieure + marge intérieure
        bouton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.MOBSTER, 1), // Bordure grise 1px
            BorderFactory.createEmptyBorder(5, 10, 5, 10)     // Marge interne
        ));
        
        return bouton;
    }

    /**
     * Bouton AJOUTER - Pour les actions de création.
     * Couleur : Vert (succès)
     * 
     * Exemples : "+ Ajouter", "Créer un groupe", "Nouvel étudiant"
     */
    public static JButton ajouter(String texte) {
        // La couleur au survol est un vert plus foncé (20, 108, 67)
        return creerBoutonArrondi(texte, 
                                   Theme.SUCCESS,           // Vert
                                   new Color(20, 108, 67),  // Vert foncé au survol
                                   Color.WHITE);            // Texte blanc
    }

    /**
     * Bouton SUPPRIMER - Pour les actions dangereuses.
     * Couleur : Rouge (danger)
     * 
     * Exemples : "Supprimer", "REINITIALISER", "Annuler tout"
     */
    public static JButton supprimer(String texte) {
        return creerBoutonArrondi(texte, 
                                   Theme.DANGER,            // Rouge
                                   new Color(187, 45, 59),  // Rouge foncé au survol
                                   Color.WHITE);            // Texte blanc
    }
    
    // =================================================================
    // ANCIENNE METHODE - Conservée pour compatibilité
    // =================================================================
    
    /**
     * Alias de creerBoutonArrondi pour compatibilité avec l'ancien code.
     * @deprecated Utiliser creerBoutonArrondi() à la place
     */
    public static JButton createRounded(String text, Color bgColor, 
                                         Color hoverColor, Color textColor) {
        return creerBoutonArrondi(text, bgColor, hoverColor, textColor);
    }
}
