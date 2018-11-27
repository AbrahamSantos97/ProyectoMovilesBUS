package com.abrahamsantos.plogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

public class MenuRuta extends Fragment {
    /*----- Recivir Arraylist -----*/
    ArrayList<Ruta> rutas = new ArrayList<>();
    View intver;
    /*-------------------------*/
    DatabaseReference database;
    /*----- Recycler View -----*/
    RecyclerView recyclerView;
    /*-------------------------*/
    onDataReceived data_received = new onDataReceived() {
        @Override
        public void setRutas(ArrayList<Ruta> nuevo) {

        }

        @Override
        public ArrayList<Ruta> getRutas() {
            return null;
        }

        @Override
        public void setNombres(String[] nuevo) {

        }

        @Override
        public String[] getNombres() {
            return new String[0];
        }

        @Override
        public void setMenuRutas(ArrayList<Ruta> nuevo, View view) {
            Log.i("|--- Informacion ---|","EEEEEEEEEEEEEEEEEEEEEEEEEEEEE1");
            recyclerView = view.findViewById(R.id.rv1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            MenuRutasAdapter adapter = new MenuRutasAdapter(rutas);
            recyclerView.setAdapter (adapter);
        }
    };

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_menu_ruta, container, false);
        intver = view;
        /*---------------*/
        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nodos = database.child("Rutas");

        ValueEventListener value = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    rutas.add(item.getValue(Ruta.class));
                }
                data_received.setMenuRutas(rutas,intver);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w ("Problema", databaseError.toException ());
            }
        };

        nodos.addValueEventListener(value);

        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        View view = inflater.inflate (R.layout.formato_recyclerview1, parent, false);
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
        Button bto1;

        public MenuRutaViewHolder(@NonNull View itemView) {
            super(itemView);
            bto1 = itemView.findViewById(R.id.bto1);
            bto1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("|-- BotonPresionado --|",""+bto1.getText());
                    Activity retorn;
                }
            });
        }

        void setData (String ruta) {
            bto1.setText(ruta);
        }

    }

}