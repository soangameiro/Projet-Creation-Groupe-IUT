package critere;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un cluster d'étudiants qui font du covoiturage ensemble.
 * Utilisé par les algorithmes de Soan.
 * 
 * @author Soan Gameiro
 */
public class Cluster {

    private List<Etudiant> membres;

    public Cluster() {
        this.membres = new ArrayList<>();
    }

    public void add(Etudiant e) {
        membres.add(e);
    }

    public int size() {
        return membres.size();
    }

    public List<Etudiant> getMembres() {
        return membres;
    }

    /**
     * Retourne le parcours du premier membre (les clusters regroupent
     * souvent des étudiants du même parcours).
     */
    public String getParcours() {
        if (membres.isEmpty())
            return "";
        return membres.get(0).getParcours();
    }

    /**
     * Compte le nombre de filles dans le cluster.
     */
    public int getNbFilles() {
        int compteur = 0;
        for (Etudiant e : membres) {
            if (e.estFille()) {
                compteur++;
            }
        }
        return compteur;
    }
}
