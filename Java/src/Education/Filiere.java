package Education;

import java.util.*;

/**
 * ========================================================
 * FILIERE.JAVA - Représente une filière d'études
 * ========================================================
 * 
 * Une filière regroupe plusieurs promotions.
 * Exemple : "Informatique" contient BUT1 Info, BUT2 Info, BUT3 Info
 * 
 * HIERARCHIE :
 * Filière -> Promotion(s) -> Groupe(s) -> Etudiant(s)
 * 
 * @author SAE S3 - Groupe
 */
public class Filiere {

	// =================================================================
	// ATTRIBUTS
	// =================================================================

	/** Identifiant unique de la filière */
	private int idFiliere;

	/** Nom de la filière (ex: "Informatique", "GEA") */
	private String libelle;

	/** Liste des promotions de cette filière */
	private Collection<Promotion> sesPromotions;

	// =================================================================
	// CONSTRUCTEUR
	// =================================================================

	/**
	 * Crée une nouvelle filière.
	 * 
	 * @param id      L'identifiant unique
	 * @param libelle Le nom de la filière
	 */
	public Filiere(int id, String libelle) {
		this.idFiliere = id;
		this.libelle = libelle;
		this.sesPromotions = new ArrayList<>();
	}

	/**
	 * Constructeur par défaut (pour compatibilité).
	 */
	public Filiere() {
		this.sesPromotions = new ArrayList<>();
	}

	// =================================================================
	// GESTION DES PROMOTIONS
	// =================================================================

	/**
	 * Ajoute une promotion à cette filière.
	 * 
	 * @param promotion La promotion à ajouter
	 */
	public void ajouterPromotion(Promotion promotion) {
		this.sesPromotions.add(promotion);
		promotion.setSaFiliere(this);
	}

	// =================================================================
	// GETTERS ET SETTERS
	// =================================================================

	public int getIdFiliere() {
		return idFiliere;
	}

	public String getLibelle() {
		return libelle;
	}

	public Collection<Promotion> getSesPromotions() {
		return sesPromotions;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}