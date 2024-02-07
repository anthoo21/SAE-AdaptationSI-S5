/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pagevisualisation;

/**
 * Class ItemVisualisation.
 */
public class ItemVisualisation {

    /** Libelle du Mode. */
    private String libelleMode;
    /** Libelle de la l'article. */
    private String libelleArticle;
    /** Valeur de la quantité pour un article. */
    private String valeurQuantite;
    /**
     * Constructeur.
     * @param libelleMode libelle du mode, ajout ou modification.
     * @param libelleArticle le libelle de l'article.
     * @param valeurQuantite la quantite pour l'article.
     */
    public ItemVisualisation(String libelleMode, String libelleArticle, String valeurQuantite) {
        this.libelleMode = libelleMode;
        this.libelleArticle = libelleArticle;
        this.valeurQuantite = valeurQuantite;
    }

    /**
     * Getter du libelle.
     * @return le libelle du mode, ajout ou modification
     */
    public String getLibelleMode() {
        return libelleMode;
    }

    /**
     * Setter du libelle.
     * @param libelleMode modifie le libelle du mode, ajout ou modfication
     */
    public void setLibelleMode(String libelleMode) {
        this.libelleMode = libelleMode;
    }

    /**
     *
     * @return le libelle de l'article
     */
    public String getLibelleArticle() {
        return libelleArticle;
    }

    /**
     *
     * @param libelleArticle modifie le numéro de l'article
     */
    public void setLibelleArticle(String libelleArticle) {
        this.libelleArticle = libelleArticle;
    }

    /**
     *
     * @return la valeurquantite, un nombre entier
     */
    public String getValeurQuantite() {
        return valeurQuantite;
    }

    /**
     *
     * @param valeurQuantite modifie la valeur quantité, un entier
     */
    public void setValeurQuantite(String valeurQuantite) {
        this.valeurQuantite = valeurQuantite;
    }
}
