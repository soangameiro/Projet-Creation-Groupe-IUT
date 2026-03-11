package Education;

import java.util.*;
import Utilisateur.*;

/**
 * ========================================================
 * MATIERE.JAVA - Représente une matière enseignée
 * ========================================================
 * 
 * Une matière regroupe plusieurs cours.
 * 
 * Exemples de matières :
 * - "Programmation" (avec cours Java, Python, C...)
 * - "Mathématiques" (avec cours Algèbre, Analyse...)
 * - "Base de données" (avec cours SQL, NoSQL...)
 * 
 * ASSOCIATIONS :
 * - Une Matière appartient à une Filière
 * - Une Matière contient plusieurs Cours
 * - Une Matière a des Responsables
 * 
 * @author SAE S3 - Groupe
 */
public class Matiere {

	// =================================================================
	// ATTRIBUTS
	// =================================================================

	/** Identifiant unique de la matière */
	private int idMatiere;

	/** Nom de la matière (ex: "Programmation") */
	private String nomMatiere;

	/** La filière à laquelle appartient cette matière */
	private Filiere filiere;

	// =================================================================
	// ASSOCIATIONS
	// =================================================================

	/** Liste des responsables de cette matière */
	private Collection<Responsable_filiere> sesResponsables;

	/** Liste des cours de cette matière */
	private Collection<Cours> sesCours;

	// =================================================================
	// CONSTRUCTEUR
	// =================================================================

	/**
	 * Crée une nouvelle matière.
	 * 
	 * @param id  L'identifiant unique
	 * @param nom Le nom de la matière
	 */
	public Matiere(int id, String nom) {
		this.idMatiere = id;
		this.nomMatiere = nom;
		this.sesResponsables = new ArrayList<>();
		this.sesCours = new ArrayList<>();
	}

	/**
	 * Constructeur par défaut.
	 */
	public Matiere() {
		this.sesResponsables = new ArrayList<>();
		this.sesCours = new ArrayList<>();
	}

	// =================================================================
	// METHODES DE GESTION
	// =================================================================

	/**
	 * Ajoute un cours à cette matière.
	 * 
	 * @param cours Le cours à ajouter
	 */
	public void ajouterCours(Cours cours) {
		this.sesCours.add(cours);
		cours.setSaMatiere(this);
	}

	// =================================================================
	// GETTERS ET SETTERS
	// =================================================================

	public int getIdMatiere() {
		return idMatiere;
	}

	public String getNomMatiere() {
		return nomMatiere;
	}

	public Filiere getFiliere() {
		return filiere;
	}

	public Collection<Cours> getSesCours() {
		return sesCours;
	}

	public Collection<Responsable_filiere> getSesResponsables() {
		return sesResponsables;
	}

	public void setNomMatiere(String nom) {
		this.nomMatiere = nom;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}
}