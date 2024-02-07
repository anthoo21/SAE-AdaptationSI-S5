/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.saedolistocks5.R;

/**
 * Class ListeAccueilViewHolder
 */
public class ListeAccueilViewHolder extends RecyclerView.ViewHolder{
    /**
     * TextView qui contient le libellé de la liste
     */
    private final TextView titreListe;

    /**
     * TextView qui contient la date de créa de la liste
     */
    private final TextView dateCrea;

    /**
     * TextView qui contient l'heure de créa de la liste
     */
    private final TextView heureCrea;

    /**
     * Bouton pour visualiser une liste
     */
    ImageView btnMenu;

    /**
     * Constructeur avec en argument une vue correspondant
     * à un item de la liste
     * Le constructeur permet d'initialiser les identifiants des
     * widgets déclarés en tant qu'attributs
     * @param itemView vue décrivant l'affichage d'un item de la liste
     */
    public ListeAccueilViewHolder (View itemView) {
        super(itemView);
        titreListe = itemView.findViewById(R.id.titreListe);
        dateCrea = itemView.findViewById(R.id.dateCrea);
        heureCrea = itemView.findViewById(R.id.heureCrea);
        btnMenu = itemView.findViewById(R.id.btnMenu);
    }
    /**
     * Permet de placer les informations contenues dans l'argument
     * dans les widgets d'un item de la liste
     * @param maListe l'instance qui doit être affichée
     */
    public void bind(ListeAccueil maListe){
        titreListe.setText(maListe.getTitreListe());
        dateCrea.setText(maListe.getDateCrea());
        heureCrea.setText(maListe.getHeureCrea());
    }
}