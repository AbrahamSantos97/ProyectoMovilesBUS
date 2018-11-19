package com.abrahamsantos.plogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

public class MenuRuta extends Fragment {
    Intent intent = getIntent();
    ArrayList<Ruta> lista = getIntent().getParcelableArrayListExtra("Lista");
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_menu_ruta, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity == null) return;
        recyclerView = activity.findViewById(R.id.MenuRutaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter (new MenuRutasAdapter(lista));
    }
}
class MenuRutasAdapter extends RecyclerView.Adapter<MenuRutasAdapter.MenuRutaViewHolder>{
    private ArrayList<Ruta> listR;

    MenuRutasAdapter(ArrayList<Ruta> listR){
        this.listR = listR;
    }

    @NonNull
    @Override
    public MenuRutaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from (parent.getContext ());
        View view = inflater.inflate (R.layout.formato_recyclerview, parent, false);
        return new MenuRutaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRutaViewHolder holder, int i) {
        Ruta ruta = listR.get(i);
        holder.setData(ruta.getNombre());
    }

    @Override
    public int getItemCount() {
        return listR.size();
    }

    class MenuRutaViewHolder extends RecyclerView.ViewHolder{
        TextView tx1;
        public MenuRutaViewHolder(@NonNull View itemView) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.tex1);
        }

        void setData (String nombre) {
            tx1.setText(nombre);
        }

    }

}