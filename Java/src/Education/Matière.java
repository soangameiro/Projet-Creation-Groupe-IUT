package Education;

import java.util.*;

import Utilisateur.*;

public class Matière {

	Collection<Responsable_filiere> sesResponsables;
	Collection<Cours> sesCours;
	private int id_mat;
	private String nom_mat;
	private Filiere filiere;

    public Matière(int id_mat, String nom_mat, Filiere saFiliere) {
        this.id_mat = id_mat;
        this.nom_mat = nom_mat;
        this.filiere = saFiliere;
    }

    public String getNomMat() { return nom_mat; }
}