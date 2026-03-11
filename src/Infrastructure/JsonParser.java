package Infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ========================================================
 * JSONPARSER.JAVA - Utilitaire de parsing JSON
 * ========================================================
 * 
 * Classe  pour parser le JSON sans bibliothèque externe.
 * Évite la duplication de la méthode extraireValeur() dans chaque fichier.
 * 
 * UTILISATION :
 * - JsonParser.extraireValeur(json, "nom") → retourne la valeur de "nom"
 * - JsonParser.extraireObjets(jsonArray) → retourne une liste des objets {...}
 * 
 * @author SAE S3 - Groupe
 */
public class JsonParser {

    // =================================================================
    // PATTERNS REGEX PRECOMPILES (performance)
    // =================================================================

    /** Pattern pour trouver les objets JSON {...} dans un tableau */
    private static final Pattern PATTERN_OBJET = Pattern.compile("\\{[^}]+\\}");

    // =================================================================
    // METHODES STATIQUES
    // =================================================================

    /**
     * Extrait une valeur d'une chaîne JSON.
     * 
     * Exemple : dans {"nom":"Dupont","age":20}
     * - extraireValeur(json, "nom") retourne "Dupont"
     * - extraireValeur(json, "age") retourne "20"
     * 
     * Gère les valeurs entre guillemets et les valeurs numériques.
     * 
     * @param json Le texte JSON complet
     * @param cle  La clé dont on veut la valeur
     * @return La valeur trouvée, ou "" si non trouvée
     */
    public static String extraireValeur(String json, String cle) {
        if (json == null || cle == null)
            return "";

        // Pattern pour trouver "cle": "valeur" ou "cle": nombre
        // group(2) = valeur entre guillemets
        // group(3) = valeur sans guillemets (nombre, null, boolean)
        Pattern pattern = Pattern.compile(
                "\"" + cle + "\"\\s*:\\s*(\"([^\"]*)\"|([^,}]+))");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String valeur = matcher.group(2) != null
                    ? matcher.group(2)
                    : matcher.group(3);
            return valeur != null ? valeur.trim().replace("null", "") : "";
        }
        return "";
    }

    /**
     * Extrait une valeur numérique entière d'une chaîne JSON.
     * 
     * @param json Le texte JSON
     * @param cle  La clé dont on veut la valeur
     * @return La valeur entière, ou 0 si non trouvée ou invalide
     */
    public static int extraireInt(String json, String cle) {
        String valeur = extraireValeur(json, cle);
        if (valeur.isEmpty())
            return 0;
        try {
            return Integer.parseInt(valeur);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Extrait une valeur numérique décimale d'une chaîne JSON.
     * 
     * @param json Le texte JSON
     * @param cle  La clé dont on veut la valeur
     * @return La valeur décimale, ou 0.0 si non trouvée ou invalide
     */
    public static double extraireDouble(String json, String cle) {
        String valeur = extraireValeur(json, cle);
        if (valeur.isEmpty())
            return 0.0;
        try {
            return Double.parseDouble(valeur);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Extrait tous les objets JSON {...} d'un tableau JSON.
     * 
     * Exemple : [{"id":1},{"id":2}] retourne une liste avec
     * les chaînes {"id":1} et {"id":2}
     * 
     * @param jsonArray Le tableau JSON sous forme de chaîne
     * @return Liste des objets JSON (chacun sous forme de String)
     */
    public static List<String> extraireObjets(String jsonArray) {
        List<String> objets = new ArrayList<>();

        if (jsonArray == null || !jsonArray.trim().startsWith("[")) {
            return objets;
        }

        Matcher matcher = PATTERN_OBJET.matcher(jsonArray);
        while (matcher.find()) {
            objets.add(matcher.group());
        }

        return objets;
    }

    /**
     * Vérifie si une réponse JSON indique un succès.
     * 
     * @param json La réponse JSON
     * @return true si la réponse contient "success":true
     */
    public static boolean estSucces(String json) {
        return json != null && json.contains("\"success\":true");
    }

    /**
     * Vérifie si une chaîne est un tableau JSON valide.
     * 
     * @param json La chaîne à vérifier
     * @return true si c'est un tableau JSON (commence par [)
     */
    public static boolean estTableau(String json) {
        return json != null && json.trim().startsWith("[");
    }
}
