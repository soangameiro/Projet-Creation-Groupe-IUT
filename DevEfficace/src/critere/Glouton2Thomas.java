package critere;

import java.util.*;

/**
 * Algorithme Glouton 2 : Par Étages (Centré Groupe).
 * Distribution des leaders Java, puis BD, puis remplissage.
 * @author Thomas Grillot
 */
public class Glouton2Thomas {

    public static List<Groupe> repartir(List<Etudiant> etudiants, int nbGroupes, int tailleMax) {
        // Init
        List<Groupe> groupes = new ArrayList<>();
        for (int i = 0; i < nbGroupes; i++) {
            groupes.add(new Groupe(i + 1, tailleMax));
        }

        // =====================================================================
        // BLOC 1 : SEPARATION DES PROFILS (LES FILES D'ATTENTE)
        // =====================================================================
        LinkedList<Etudiant> expertsJava = new LinkedList<>();
        LinkedList<Etudiant> expertsBD = new LinkedList<>();
        LinkedList<Etudiant> autres = new LinkedList<>();

        for (Etudiant e : etudiants) {
            if (e.estLeaderJava())
                expertsJava.add(e);
            else if (e.estLeaderBD())
                expertsBD.add(e);
            else
                autres.add(e);
        }

        // =====================================================================
        // BLOC 2 : VAGUE 1 - DISTRIBUTION EQUITABLE JAVA (Tourniquet)
        // =====================================================================
        int indexGroupe = 0;
        while (!expertsJava.isEmpty()) {
            // On utilise le Modulo (%) pour tourner en rond : 0, 1, 2, 3, 4, 0, 1...
            Groupe g = groupes.get(indexGroupe % nbGroupes);
            
            if (!g.estPlein()) {
                // poll() récupère le premier étudiant de la liste et L'ENLEVE de la liste
                g.ajouter(expertsJava.poll());
            }
            indexGroupe++;
        }

        // =====================================================================
        // BLOC 3 : VAGUE 2 - DISTRIBUTION INTELLIGENTE BD
        // =====================================================================
        
        // --- TRI MANUEL DES GROUPES ---
        // On veut mettre les groupes qui N'ONT PAS de leader BD en premier.
        // Comme ça, on leur donne la priorité.
        for (int i = 0; i < groupes.size() - 1; i++) {
            for (int j = 0; j < groupes.size() - 1 - i; j++) {
                Groupe g1 = groupes.get(j);
                Groupe g2 = groupes.get(j + 1);

                // Si g1 a un leader et g2 n'en a pas, g1 est "moins prioritaire".
                // On l'échange pour le mettre plus loin dans la liste.
                if (g1.aUnLeaderBD() && !g2.aUnLeaderBD()) {
                    groupes.set(j, g2);
                    groupes.set(j + 1, g1);
                }
            }
        }

        // Distribution prioritaire aux groupes vides
        for (Groupe g : groupes) {
            if (!expertsBD.isEmpty() && !g.estPlein()) {
                // Si le groupe n'a toujours pas de leader BD, on lui en donne un
                if (!g.aUnLeaderBD()) {
                    g.ajouter(expertsBD.poll());
                }
            }
        }

        // S'il reste des experts BD (surplus), on les distribue aux autres
        indexGroupe = 0;
        while (!expertsBD.isEmpty()) {
            Groupe g = groupes.get(indexGroupe % nbGroupes);
            if (!g.estPlein())
                g.ajouter(expertsBD.poll());
            indexGroupe++;
        }

        // =====================================================================
        // BLOC 4 : VAGUE 3 - REMPLISSAGE FINAL
        // =====================================================================
        for (Etudiant e : autres) {
            Groupe meilleur = null;
            double meilleurScore = Double.MAX_VALUE;

            for (Groupe g : groupes) {
                if (g.estPlein())
                    continue;

                // Le score, c'est juste la taille. Plus le groupe est petit, mieux c'est.
                double score = g.getTaille();
                
                // Petit ajustement pour éviter les surplus de Bac
                if (e.getTypeBac().equals("G") && g.nbGeneral() > g.nbTechno())
                    score += 5;
                if (e.getTypeBac().equals("T") && g.nbTechno() > g.nbGeneral())
                    score += 5;

                if (score < meilleurScore) {
                    meilleurScore = score;
                    meilleur = g;
                }
            }
            if (meilleur != null)
                meilleur.ajouter(e);
        }

        // --- TRI FINAL POUR L'AFFICHAGE (Remettre dans l'ordre 1, 2, 3...) ---
        for (int i = 0; i < groupes.size() - 1; i++) {
            for (int j = 0; j < groupes.size() - 1 - i; j++) {
                if (groupes.get(j).getId() > groupes.get(j + 1).getId()) {
                    Groupe temp = groupes.get(j);
                    groupes.set(j, groupes.get(j + 1));
                    groupes.set(j + 1, temp);
                }
            }
        }
        
        return groupes;
    }
}