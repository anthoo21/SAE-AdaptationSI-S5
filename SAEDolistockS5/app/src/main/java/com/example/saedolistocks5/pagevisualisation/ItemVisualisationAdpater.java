package com.example.saedolistocks5.pagevisualisation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saedolistocks5.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemVisualisationAdpater extends RecyclerView.Adapter<ItemVisualisationHolder> {

    /**  Source de données à afficher */
    private List<ItemVisualisation> donnes;

    /**
     * Constructeur
     * @param donnes Listes contenant des items que l'adapter
     */
    public ItemVisualisationAdpater(List<ItemVisualisation> donnes) {
        this.donnes = donnes;
    }

    /**
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return un "contenant" qui permeterra d'afficher la liste
     */
    @Override
    public ItemVisualisationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.vue_visualisation_liste,parent,false);
        return new ItemVisualisationHolder(view);
    }

    /**
     * Remplissage d'un item de la liste en fonction de sa position
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ItemVisualisationHolder holder, int position) {
        ItemVisualisation itemVisualisation = donnes.get(position);
        holder.bind(itemVisualisation);
    }

    /**
     *
     * @return la taile de liste
     */
    @Override
    public int getItemCount() {
        return donnes.size();
    }
}
