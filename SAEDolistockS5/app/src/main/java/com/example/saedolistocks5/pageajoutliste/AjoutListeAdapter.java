/**
 * Package de la SAE
 */
package com.example.saedolistocks5.pageajoutliste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.saedolistocks5.R;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Classe AjoutListeAdapter.
 */
public class AjoutListeAdapter extends RecyclerView.Adapter<AjoutListeViewHolder> {

    /**
     * Liste ajout liste.
     */
    private final List<AjoutListe> lesDonnees;

    /**
     * Constructeur.
     * @param donnees les données.
     */
    public AjoutListeAdapter(List<AjoutListe> donnees) {
        lesDonnees = donnees;
    }

    /**
     * Renvoie un contenant de type AjoutListeViewHolder qui permettra d'afficher
     * un élément de la liste.
     */
    @Override
    public AjoutListeViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.article_ajouter_layout,
                viewGroup, false);
        return new AjoutListeViewHolder(view);
    }

    /**
     * On remplit un item de la liste en fonction de sa position.
     */
    @Override
    public void onBindViewHolder(AjoutListeViewHolder myViewHolder, int position) {
        AjoutListe myObject = lesDonnees.get(position);
        myViewHolder.btnDltArticle.setTag(position);
        myViewHolder.bind(myObject);
    }

    /**
     * Renvoie la taille de la liste.
     */
    @Override
    public int getItemCount() {
        return lesDonnees.size();
    }
}
