/**
 * Package de la SAE
 */
package com.example.saedolistocks5.pagemodifliste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.saedolistocks5.R;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Classe ModifListeAdapter.
 */
public class ModifListeAdapter extends RecyclerView.Adapter<ModifListeViewHolder> {

    /**
     * Liste modif liste.
     */
    private final List<ModifListe> lesDonnees;

    /**
     * Constructeur.
     * @param donnees les données.
     */
    public ModifListeAdapter(List<ModifListe> donnees) {
        lesDonnees = donnees;
    }

    /**
     * Renvoie un contenant de type ModifListeViewHolder qui permettra d'afficher
     * un élément de la liste.
     */
    @Override
    public ModifListeViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.article_modif_layout,
                viewGroup, false);
        return new ModifListeViewHolder(view);
    }

    /**
     * On remplit un item de la liste en fonction de sa position.
     */
    @Override
    public void onBindViewHolder(ModifListeViewHolder myViewHolder, int position) {
        ModifListe myObject = lesDonnees.get(position);
        myViewHolder.btnDltArticle.setTag(position);
        myViewHolder.btnModifArticle.setTag(position);
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
