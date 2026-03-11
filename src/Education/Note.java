package Education;

/**
 * ========================================================
 * NOTE.JAVA - Représente une note d'un étudiant
 * ========================================================
 * 
 * Une note est associée à :
 * - Un étudiant (qui a reçu la note)
 * - Une matière ou un type (ex: "Math", "Info", "TP")
 * 
 * Cette classe est utilisée pour :
 * - Calculer les moyennes par matière
 * - Aider l'algorithme de répartition des groupes
 * 
 * @author SAE S3 - Groupe
 */
public class Note {

    // =================================================================
    // ATTRIBUTS
    // =================================================================

    /** Identifiant unique de la note */
    private int idNote;

    /** Type ou matière de la note (ex: "Math", "Info", "Anglais") */
    private String type;

    /** Valeur de la note (sur 20) */
    private float valeur;

    // =================================================================
    // CONSTRUCTEUR
    // =================================================================

    /**
     * Crée une nouvelle note.
     * 
     * @param idNote L'identifiant unique
     * @param type   Le type/matière
     * @param valeur La valeur sur 20
     */
    public Note(int idNote, String type, float valeur) {
        this.idNote = idNote;
        this.type = type;
        this.valeur = valeur;
    }

    // =================================================================
    // GETTERS
    // =================================================================

    /** @return L'ID de la note */
    public int getIdNote() {
        return idNote;
    }

    /** @return Le type/matière de la note */
    public String getType() {
        return type;
    }

    /** @return La valeur de la note (sur 20) */
    public float getValeur() {
        return valeur;
    }

    // =================================================================
    // SETTERS
    // =================================================================

    /** Modifie la valeur de la note */
    public void setValeur(float nouvelleValeur) {
        this.valeur = nouvelleValeur;
    }
}