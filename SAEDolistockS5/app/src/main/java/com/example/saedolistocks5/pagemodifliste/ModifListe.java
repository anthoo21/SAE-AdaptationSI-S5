/**
 *
 */
package com.example.saedolistocks5.pagemodifliste;

/**
 * Classe ModifListe.
 */
public class ModifListe {

    /**
     * String pour le libelle de la liste.
     */
    private final String libelleArticle;
    /**
     * String pour le code de l'article.
     */
    private final String codeArticle;
    /**
     * String pour la quantite.
     */
    private final String quantite;

    /**
     * Constructeur
     * @param nomEtCodeArticle le libelle de l'article.
     * @param codeArticle le code de l'article.
     * @param quantite la quantite de l'article.
     */
    public ModifListe(String nomEtCodeArticle, String codeArticle, String quantite) {
        this.libelleArticle = nomEtCodeArticle;
        this.codeArticle = codeArticle;
        this.quantite = quantite;
    }

    /**
     * Getter du libelle.
     * @return le libelle de l'article.
     */
    public String getLibelleArticle() {
        return libelleArticle;
    }

    /**
     * Getter du codeArticle.
     * @return le codeArticle de l'article
     */
    public String getCodeArticle() {
        return codeArticle;
    }

    /**
     * Getter de la quantite.
     * @return la quantite de l'article
     */
    public String getQuantite() {
        return quantite;
    }

}
