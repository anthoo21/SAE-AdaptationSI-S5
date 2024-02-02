package com.example.saedolistocks5.pageajoutliste;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.saedolistocks5.R;

import org.w3c.dom.Text;

import androidx.recyclerview.widget.RecyclerView;

public class AjoutListeViewHolder extends RecyclerView.ViewHolder {

    private TextView nomEtCodeArticle;

    private TextView quantite;

    ImageButton btnDltArticle;

    public AjoutListeViewHolder(View itemView) {
        super(itemView);
        nomEtCodeArticle = itemView.findViewById(R.id.libelleArticle);
        quantite = itemView.findViewById(R.id.valeurQuantite);
        btnDltArticle = itemView.findViewById(R.id.btnDltArticle);
    }

    public void bind(AjoutListe maListe) {
        nomEtCodeArticle.setText(maListe.getLibelleArticle() + " (" + maListe.getCodeArticle() + ")");
        quantite.setText(maListe.getQuantite());
    }


}
