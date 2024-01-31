package com.example.saedolistocks5.pageliste;

public class ListeAccueil {
    private String titreListe;
    private String dateCrea;
    private String heureCrea;

    /**
     * Constructeur avec en argument les valeurs des 2 attributs
     * @param titreListe libellé de la photo
     * @param dateCrea identifiant de la photo
     */
    public ListeAccueil(String titreListe, String dateCrea, String heureCrea) {
        this.titreListe = titreListe;
        this.dateCrea = dateCrea;
        this.heureCrea = heureCrea;
    }
    /**
     * Renvoie le libellé de la liste
     * @return une chaîne contenant le libellé
     */
    public String getTitreListe() {
        return titreListe;
    }

    /**
     * Renvoie la date de création de la liste
     * @return une chaîne contenant la date
     */
    public String getDateCrea() {
        return dateCrea;
    }

    /**
     * Renvoie l'heure de création de la liste
     * @return une chaîne contenant l'heure
     */
    public String getHeureCrea() {
        return heureCrea;
    }

}