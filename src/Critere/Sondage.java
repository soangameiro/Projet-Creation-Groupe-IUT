package Critere;

import java.util.ArrayList;
import java.util.List;

/**
 * SONDAGE - Represente un sondage cree par un enseignant
 */
public class Sondage {
    
    private int idSondage;
    private String question;
    private List<Reponse> sesReponses;

    public Sondage(int idSondage, String question) {
        this.idSondage = idSondage;
        this.question = question;
        this.sesReponses = new ArrayList<>();
    }

    public void ajouterReponse(Reponse reponse) {
        this.sesReponses.add(reponse);
    }
    
    public int getNombreReponses() {
        return sesReponses.size();
    }

    public int getIdSondage() { 
        return idSondage; 
    }
    
    public String getQuestion() { 
        return question; 
    }
    
    public List<Reponse> getSesReponses() { 
        return sesReponses; 
    }
    
    public String getCritSondage() { 
        return question; 
    }
}
