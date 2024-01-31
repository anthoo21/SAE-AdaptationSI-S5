package com.example.saedolistocks5.pageajoutliste;

public class AjoutListe {

    private String nomEtCodeArticle;

    private String quantite;

    public AjoutListe(String nomEtCodeArticle, String quantite) {
        this.nomEtCodeArticle = nomEtCodeArticle;
        this.quantite = quantite;
    }

    public String getNomEtCodeArticle() {
        return nomEtCodeArticle;
    }

    public String getQuantite() {
        return quantite;
    }

}
