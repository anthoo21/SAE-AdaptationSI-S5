/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pagevisualisation;

import android.view.View;
import android.widget.TextView;
import com.example.saedolistocks5.R;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description du contenant de la liste de type RecyclerView.
 */
public class ItemVisualisationHolder extends RecyclerView.ViewHolder {

    /** TextView pour le libelle article. */
    private final TextView libelleArticle;

    /** TextView pour le mode d'ajout. */
    private final TextView libelleMode;

    /** TextView pour la quantite. */
    private final TextView valeurQuantite;

    /**
     * Constructeur.
     * @param itemView vue décrivant l'item d'une liste.
     */
    public ItemVisualisationHolder(View itemView) {
        super(itemView);
        libelleArticle = itemView.findViewById(R.id.libelleArticle);
        libelleMode = itemView.findViewById(R.id.libelleMode);
        valeurQuantite = itemView.findViewById(R.id.valeurQuantite);
    }

    /**
     * Place les items dans un widjet de la liste.
     * @param itemVisualisation ce qui doit être affiché.
     */
    public void bind (ItemVisualisation itemVisualisation) {
        libelleArticle.setText(itemVisualisation.getLibelleArticle());
        libelleMode.setText(itemVisualisation.getLibelleMode());
        valeurQuantite.setText(itemVisualisation.getValeurQuantite());
    }
}
