package com.abrahamsantos.plogin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mapsV extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{
    /*----- Variables -----*/
    static private GoogleMap mMap;
    private LocationManager locManager;
    private Location loc;
    private Marker marcador;
    private double Latitude = 0.0, Longitude = 0.0;
    private int clicBuscar=1;
    /*-------------------*/
    Bundle info = new Bundle();
    Intent intentR = new Intent(this, MenuRuta.class);
    /*----- Inicio -----*/
    AutoCompleteTextView Predic;
    /*Interfaces*/
    onDataReceived data = new onDataReceived() {
        ArrayList<Ruta> lista;
        String[] nombre;
        @Override
        public void setRutas(ArrayList<Ruta> nuevo) {
            this.lista = nuevo;
            /*Es obligatorio almacenar la informacion en este punto, dado que en este punto del sistema, la informacion
            se a descargado por completo al programa*/
            Imprimir_etiquetas(data.getRutas());
            LlenarAdapter();
            info.putParcelableArrayList("Lista", (ArrayList<? extends Parcelable>) nuevo);
            intentR.putExtra(info);
        }

        @Override
        public ArrayList<Ruta> getRutas() {
            return this.lista;
        }

        @Override
        public void setNombres(String[] nombre) {
            this.nombre = nombre;
        }

        @Override
        public String[] getNombres() {
            return this.nombre;
        }
    };
    /*--------------- Firebase ---------------*/
    public void DescargarJson(){
        /*------------------*/
        DatabaseReference database;
        /*------------------*/
        // Write a message to the database
        /*instancia a la raiz de la base de datos*/
        database = FirebaseDatabase.getInstance().getReference();
        /*referencia a al elemento dentro de la base dde datos*/
        DatabaseReference rutas = database.child("Rutas");

        // Read from the database
        ValueEventListener value = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Ruta> nuevo = new ArrayList<>();
                /*-------------*/
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    try{
                        Ruta ruta = item.getValue(Ruta.class);
                        nuevo.add(ruta);
                    }catch(Exception e){
                        Log.i("|----- RUTA -----|"," Error: "+e);
                    }
                }
                /*----------*/
                data.setRutas(nuevo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("CancelacionDatabase", "Failed to read value.", error.toException());
            }
        };

        rutas.addValueEventListener(value);
    }
    /*---------- Almacenar nombres ------------*/
    private void LlenarAdapter() {
        String [] nombres = new String[data.getRutas().size()];

        for(int k=0; k < nombres.length;k++){
            nombres[k] = data.getRutas().get(k).getNombre();
        }
        data.setNombres(nombres);

        ArrayAdapter<String> adaptador =new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,data.getNombres());
        Predic.setThreshold(2);
        Predic.setAdapter(adaptador);
    }
    /*----------- Creando Actividad -----------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_v);
        Toolbar toolt = findViewById(R.id.toolz);
        Predic = findViewById(R.id.textPredic);
        setSupportActionBar(toolt);
        DescargarJson();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try{
            mapFragment.getMapAsync(this);
        }catch(Exception e){
            Log.i("|--- Error(Mapa) ---|",""+e);
        }

    }
    /*-------------- MapaCreado ---------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*----------------------*/
        mMap = googleMap;
        UbicacionUser();
    }
    /*-------------- CreandoMenu --------------*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    /*-------------- Seleccion de items -------*/
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.busca:
                if(clicBuscar==1){
                    Predic.setVisibility(View.VISIBLE);
                        clicBuscar=2;
                }else{
                    clicBuscar=1;
                    Predic.setText("");
                    Predic.setVisibility(View.GONE);
                    InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    try {
                        imn.hideSoftInputFromWindow(Predic.getWindowToken(), 0);
                    }catch(Exception e){
                        Log.i("|--- Error(ItemSelected) ---|",""+e);
                    }
                }
                break;
            case R.id.menu:
                startActivity(intentR);
                break;
        }
        return true;
    }
    /*-------------- Crear un marcador --------*/
    private void AgregarMarcador(double Latitude, double Longitude) {
        LatLng user = new LatLng(Latitude, Longitude);
        CameraUpdate ubicacion =CameraUpdateFactory.newLatLngZoom(user,18);
        if(marcador != null){
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions().position(user).title("Usuario"));
        mMap.animateCamera(ubicacion);
    }
    /*--- Actualiza la localizacion del usuario -----*/
    private void Actualizar(Location location) {
        try{
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            AgregarMarcador(Latitude,Longitude);
        }catch(Exception e){
            Log.i("|--- Error(Actualizar) ---|",""+e);
        }

    }
    /*--- ??Metoso heredado o implementado ?? ---*/
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng user = new LatLng(location.getLatitude(),location.getLongitude());
            if(marcador!=null){
                marcador.remove();
            }
            marcador = mMap.addMarker(new MarkerOptions().position(user).title("Usuario"));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    /*--- Imprime las etiquetas descargadas por firebase ---*/
    private void Imprimir_etiquetas(ArrayList<Ruta> lista){
        LatLng lt;
        for(Ruta ruta:lista){
            for(Parada pd:ruta.getParada()){
                lt = new LatLng(pd.getCoordenadaX(),pd.getCoordenadaY());
                mMap.addMarker(new MarkerOptions().position(lt).title(pd.direccion));
            }
        }
    }
    /*-- Optiene la ultima ubicacion del usuario ---*/
    private void UbicacionUser() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No se dieron los permisos", Toast.LENGTH_LONG).show();
        } else{
            try {
                Location lol = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                Actualizar(lol);
            }catch(Exception e){
                Log.i("|--- Error(UbicacionUser) ---|",""+e);
            }
        }
    }


}