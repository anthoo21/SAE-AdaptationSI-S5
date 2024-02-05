package com.example.saedolistocks5.pageliste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.saedolistocks5.R;

import java.util.List;

/**
 * Adaptateur spécifique pour afficher une liste de type RecyclerView
 * dont les items sont de type PhotoParis
 * @author GP1
 * @version 1.0
 */
public class ListeAccueilAdapter extends RecyclerView.Adapter<ListeAccueilViewHolder> {
    /**
     * Source de données à afficher par la liste
     */
    private List<ListeAccueil> lesDonnees;

    /**
     * Constructeur avec en argument la liste source des données
     *
     * @param donnees liste contenant les instances de type
     *                ListeAccueil que l'adapteur sera chargé de gérer
     */
    public ListeAccueilAdapter(List<ListeAccueil> donnees) {
        lesDonnees = donnees;
    }

    /**
     * Renvoie un contenant de type ListeAccueilViewHolder qui permettra d'afficher
     * un élément de la liste
     */
    @Override
    public ListeAccueilViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.vue_liste_accueil,
                viewGroup, false);
        return new ListeAccueilViewHolder(view);
    }

    /**
     * On remplit un item de la liste en fonction de sa position
     */
    @Override
    public void onBindViewHolder(ListeAccueilViewHolder myViewHolder, int position) {
        ListeAccueil myObject = lesDonnees.get(position);
        myViewHolder.btnMenu.setTag(position);
        myViewHolder.bind(myObject);
    }

    /**
     * Renvoie la taille de la liste
     */
    @Override
    public int getItemCount() {
        return lesDonnees.size();
    }
}