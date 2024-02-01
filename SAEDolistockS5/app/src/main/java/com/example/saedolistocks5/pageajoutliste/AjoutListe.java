package com.example.saedolistocks5.pageajoutliste;

public class AjoutListe {

    private String libelleArticle;

    private String codeArticle;

    private String quantite;

    public AjoutListe(String nomEtCodeArticle, String codeArticle, String quantite) {
        this.libelleArticle = nomEtCodeArticle;
        this.codeArticle = codeArticle;
        this.quantite = quantite;
    }

    public String getLibelleArticle() {
        return libelleArticle;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public String getQuantite() {
        return quantite;
    }

}
