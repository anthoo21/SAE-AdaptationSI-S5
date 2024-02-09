/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageajoutliste;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.saedolistocks5.R;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Classe AjoutListeViewHolder.
 */
public class AjoutListeViewHolder extends RecyclerView.ViewHolder {

    /**
     * Textview pour le nom et le code de l'article.
     */
    private final TextView nomEtCodeArticle;

    /**
     * Textview de la quantite.
     */
    private final TextView quantite;

    /**
     * boutton pour l'image.
     */
    ImageButton btnDltArticle;

    /**
     * Constructeur
     * @param itemView la vue
     */
    public AjoutListeViewHolder(View itemView) {
        super(itemView);
        nomEtCodeArticle = itemView.findViewById(R.id.libelleArticle);
        quantite = itemView.findViewById(R.id.valeurQuantite);
        btnDltArticle = itemView.findViewById(R.id.btnDltArticle);
    }

    /**
     * Place les informations contenus.
     * @param maListe la liste.
     */
    public void bind(AjoutListe maListe) {
        nomEtCodeArticle.setText(String.format("%s (%s)", maListe.getLibelleArticle(),
                maListe.getCodeArticle()));
        quantite.setText(maListe.getQuantite());
    }

}