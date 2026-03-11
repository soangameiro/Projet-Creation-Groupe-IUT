package critere;

/**
 * Classe unifiée représentant un étudiant avec tous les attributs
 * nécessaires pour les différents modes de création de groupes.
 * 
 * @author Maelan Jahier, Thomas Grillot, Soan Gameiro
 */
public class Etudiant {

    // ========== ENUM ==========

    public enum StatutApprentissage {
        RECHERCHE,
        AUCUN
    }

    // ========== ATTRIBUTS COMMUNS ==========

    private int id;
    private String nom;
    private String prenom;
    private double moyenne;
    private String typeBac; // "G"/"T" ou "General"/"Techno" ou int 1/2
    private char sexe; // 'M', 'F', 'N'

    // ========== ATTRIBUTS MODE SOAN (Parcours/Covoiturage) ==========

    private String parcours; // "Dev", "Reseau", "Cyber"
    private int idCovoiturage; // -1 si aucun

    // ========== ATTRIBUTS MODE THOMAS (Notes/Compétences) ==========

    private double noteJava;
    private double noteBD;
    private boolean redoublant;
    private StatutApprentissage apprentissage;

    // ========== CONSTRUCTEURS ==========

    /**
     * Constructeur complet avec tous les attributs.
     */
    public Etudiant(int id, String nom, String prenom, double moyenne, String typeBac, char sexe,
            String parcours, int idCovoiturage, double noteJava, double noteBD,
            boolean redoublant, StatutApprentissage apprentissage) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.moyenne = moyenne;
        this.typeBac = typeBac;
        this.sexe = sexe;
        this.parcours = parcours;
        this.idCovoiturage = idCovoiturage;
        this.noteJava = noteJava;
        this.noteBD = noteBD;
        this.redoublant = redoublant;
        this.apprentissage = apprentissage;
    }

    /**
     * Constructeur Mode Maelan (équilibre bac/moyenne).
     */
    public Etudiant(int id, String nom, String prenom, double moyenne, int typeBac, String genre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.moyenne = moyenne;
        this.typeBac = (typeBac == 1) ? "General" : "Techno";
        this.sexe = (genre != null && genre.length() > 0) ? genre.charAt(0) : 'N';
        this.parcours = "";
        this.idCovoiturage = -1;
        this.noteJava = 0;
        this.noteBD = 0;
        this.redoublant = false;
        this.apprentissage = StatutApprentissage.AUCUN;
    }

    /**
     * Constructeur Mode Soan (parcours/covoiturage).
     */
    public Etudiant(String nom, double moyenne, String typeBac, char sexe, String parcours, int idCovoiturage) {
        this.id = 0;
        this.nom = nom;
        this.prenom = "";
        this.moyenne = moyenne;
        this.typeBac = typeBac;
        this.sexe = sexe;
        this.parcours = parcours;
        this.idCovoiturage = idCovoiturage;
        this.noteJava = 0;
        this.noteBD = 0;
        this.redoublant = false;
        this.apprentissage = StatutApprentissage.AUCUN;
    }

    /**
     * Constructeur Mode Thomas (notes/compétences).
     */
    public Etudiant(String nom, String typeBac, boolean redoublant, StatutApprentissage apprentissage,
            double noteJava, double noteBD) {
        this.id = 0;
        this.nom = nom;
        this.prenom = "";
        this.moyenne = (noteJava + noteBD) / 2;
        this.typeBac = typeBac;
        this.sexe = 'N';
        this.parcours = "";
        this.idCovoiturage = -1;
        this.noteJava = noteJava;
        this.noteBD = noteBD;
        this.redoublant = redoublant;
        this.apprentissage = apprentissage;
    }

    /**
     * Constructeur simplifié (nom, moyenne, bac, sexe).
     */
    public Etudiant(String nom, double moyenne, String typeBac, String sexe) {
        this.id = 0;
        this.nom = nom;
        this.prenom = "";
        this.moyenne = moyenne;
        this.typeBac = typeBac;
        this.sexe = (sexe != null && sexe.length() > 0) ? sexe.charAt(0) : 'N';
        this.parcours = "";
        this.idCovoiturage = -1;
        this.noteJava = 0;
        this.noteBD = 0;
        this.redoublant = false;
        this.apprentissage = StatutApprentissage.AUCUN;
    }

    // ========== GETTERS ==========

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public double getMoyenne() {
        return moyenne;
    }

    public String getTypeBac() {
        return typeBac;
    }

    public char getSexe() {
        return sexe;
    }

    public String getParcours() {
        return parcours;
    }

    public int getIdCovoiturage() {
        return idCovoiturage;
    }

    public double getNoteJava() {
        return noteJava;
    }

    public double getNoteBD() {
        return noteBD;
    }

    public boolean isRedoublant() {
        return redoublant;
    }

    public StatutApprentissage getApprentissage() {
        return apprentissage;
    }

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Vérifie si l'étudiant est une fille.
     */
    public boolean estFille() {
        return Character.toUpperCase(sexe) == 'F';
    }

    /**
     * Vérifie si l'étudiant a un bac Général.
     */
    public boolean estBacGeneral() {
        return typeBac.equals("G") || typeBac.equals("General") || typeBac.equals("1");
    }

    /**
     * Vérifie si l'étudiant est un "leader" en Java (note >= 14).
     * Utilisé par le mode Thomas.
     */
    public boolean estLeaderJava() {
        return noteJava >= 14.0;
    }

    /**
     * Vérifie si l'étudiant est un "leader" en BD (note >= 14).
     * Utilisé par le mode Thomas.
     */
    public boolean estLeaderBD() {
        return noteBD >= 14.0;
    }

    @Override
    public String toString() {
        if (prenom != null && !prenom.isEmpty()) {
            return prenom + " " + nom + " (moy=" + String.format("%.2f", moyenne) + ", bac=" + typeBac + ", " + sexe
                    + ")";
        }
        return nom + " (" + sexe + ", " + parcours + ")";
    }
}
