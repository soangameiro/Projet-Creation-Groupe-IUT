package Vue;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * Classe utilitaire définissant la charte graphique de l'application.
 * Combine les anciens styles (compatibilité) et le nouveau design.
 */
public class Theme {
    // =================================================================
    // NOUVELLES COULEURS (Modern Design)
    // =================================================================
    public static final Color BACKGROUND = new Color(245, 247, 250); // Blanc cassé
    public static final Color PRIMARY = new Color(167, 25, 48); // Shiraz (Rouge IUT)
    public static final Color SECONDARY = new Color(44, 62, 80); // Bleu nuit
    public static final Color ACCENT = new Color(52, 152, 219); // Bleu clair
    public static final Color SUCCESS = new Color(40, 167, 69); // Vert
    public static final Color WARNING = new Color(255, 193, 7); // Jaune/Orange
    public static final Color DANGER = new Color(220, 53, 69); // Rouge erreur

    public static final Color TEXT_DARK = new Color(33, 37, 41);
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color GRID_LINE = new Color(220, 220, 220);
    public static final Color PANEL_BORDER = new Color(200, 200, 200);

    // =================================================================
    // ALIAS DE COMPATIBILITE (Pour LoginFrame et ButtonFactory existants)
    // =================================================================
    public static final Color BON_JOUR = BACKGROUND; // Fond
    public static final Color PAPRIKA = PRIMARY; // Rouge
    public static final Color COD_GRAY = TEXT_DARK; // Gris foncé
    public static final Color SCORPION = new Color(80, 80, 80); // Gris moyen

    // Alias manquants pour ButtonFactory
    public static final Color CADILLAC = PRIMARY.brighter(); // Rose foncé (Survol Paprika)
    public static final Color MOBSTER = new Color(127, 127, 127); // Gris bordure

    // =================================================================
    // POLICES
    // =================================================================
    public static final Font FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    public static final Font FONT_REGULAR = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    // Alias polices
    public static final Font TITLE_FONT = FONT_TITLE;
    public static final Font BODY_FONT = FONT_REGULAR;

    /**
     * Configure le LookAndFeel global de l'application.
     */
    public static void setup() {
        try {
            // Essayer d'utiliser le LookAndFeel système ou Nimbus
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

            // Appliquer les polices partout
            setGlobalFont(new FontUIResource("Segoe UI", Font.PLAIN, 14));

            // Couleurs Nimbus custom (si Nimbus utilisé)
            UIManager.put("nimbusBase", SECONDARY);
            UIManager.put("nimbusBlueGrey", BACKGROUND);
            UIManager.put("control", BACKGROUND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applique une police globale à tous les composants Swing.
     */
    private static void setGlobalFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
