package com.example.saedolistocks5;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Description du contenant de la liste de type RecyclerView
 */
public class ItemVisualisationHolder extends RecyclerView.ViewHolder {

    /** TextView pour le libelle article */
    private TextView libelleArticle;

    /** TextView pour le mode d'ajout */
    private TextView libelleMode;

    /** TextView pour la quantite */
    private TextView valeurQuantite;

    /**
     * Constructeur
     * @param itemView vue décrivant l'item d'une liste
     */
    public ItemVisualisationHolder(View itemView) {
        super(itemView);
        libelleArticle = (TextView) itemView.findViewById(R.id.libelleArticle);
        libelleMode = (TextView) itemView.findViewById(R.id.libelleMode);
        valeurQuantite =  (TextView) itemView.findViewById(R.id.valeurQuantite);
    }

    /**
     * Place les items dans un widjet de la liste
     * @param itemVisualisation ce qui doit être affiché
     */
    public void bind (ItemVisualisation itemVisualisation) {
        libelleArticle.setText(itemVisualisation.getLibelleArticle());
        libelleMode.setText(itemVisualisation.getLibelleMode());
        valeurQuantite.setText(itemVisualisation.getValeurQuantite());
    }
}
