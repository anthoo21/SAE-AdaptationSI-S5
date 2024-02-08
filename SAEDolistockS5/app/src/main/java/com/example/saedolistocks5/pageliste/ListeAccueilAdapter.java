/**
 * Package de la SAE.
 */
package com.example.saedolistocks5.pageliste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saedolistocks5.R;
import java.util.List;

/**
 * Class ListeAccueilAdapter.
 */
public class ListeAccueilAdapter extends RecyclerView.Adapter<ListeAccueilViewHolder> {
    /**
     * Source de données à afficher par la liste.
     */
    private final List<ListeAccueil> lesDonnees;

    /**
     * Constructeur avec en argument la liste source des données.
     * @param donnees liste contenant les instances de type
     *                ListeAccueil que l'adapteur sera chargé de gérer.
     */
    public ListeAccueilAdapter(List<ListeAccueil> donnees) {
        lesDonnees = donnees;
    }


    /**
     * Renvoie un contenant de type ListeAccueilViewHolder qui permettra d'afficher
     * un élément de la liste.
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param itemType  The view type of the new View.
     * @return une vue
     */
    @Override
    public ListeAccueilViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.vue_liste_accueil,
                viewGroup, false);
        return new ListeAccueilViewHolder(view);
    }


    /**
     * On remplit un item de la liste en fonction de sa position.
     * @param myViewHolder The ViewHolder which should be updated to represent the contents of the
     *                     item at the given position in the data set.
     * @param position     The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ListeAccueilViewHolder myViewHolder, int position) {
        ListeAccueil myObject = lesDonnees.get(position);
        myViewHolder.btnMenu.setTag(position);
        myViewHolder.bind(myObject);
    }

    /**
     * Renvoie la taille de la liste
     * @return la taille des données
     */
    @Override
    public int getItemCount() {
        return lesDonnees.size();
    }
}