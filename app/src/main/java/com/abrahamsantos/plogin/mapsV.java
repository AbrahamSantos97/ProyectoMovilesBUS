package com.abrahamsantos.plogin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mapsV extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    /*----- Variables -----*/
    static private GoogleMap mMap;
    private LocationManager locManager;
    private Location loc;
    private Marker marcador;
    private double Latitude = 0.0, Longitude = 0.0;
    private int clicBuscar=1;
    /*----- Inicio -----*/
    AutoCompleteTextView Predic;
    ArrayList<Ruta> listrutas = new ArrayList<>();
    String [] nombres = new String[15];
    /*{"Juan","Juanito","Julian","Maria","Maria Fernanda"}*/
    /*------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_v);
        Toolbar toolt = findViewById(R.id.toolz);
        Predic = findViewById(R.id.textPredic);

        setSupportActionBar(toolt);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.busca){
            if(clicBuscar==1){
                Predic.setVisibility(View.VISIBLE);
                clicBuscar=2;
                LlenarAdapter();
            }else{
                clicBuscar=1;
                Predic.setText("");
                Predic.setVisibility(View.GONE);
                InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imn != null;
                imn.hideSoftInputFromWindow(Predic.getWindowToken(),0);
                //Aqui se debe de llamar a la funcion que recoja los datos del Predic
            }
        }
        return true;
    }

    private void LlenarAdapter() {
        ArrayAdapter<String> adaptador =new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,nombres);
        Predic.setThreshold(2);
        Predic.setAdapter(adaptador);
    }

    /*--------------- Firebase ---------------*/
    public static void DescargarJson(){
        /*------------------*/
        DatabaseReference database;
        /*------------------*/
        // Write a message to the database
        /*instancia a la raiz de la base de datos*/
        database = FirebaseDatabase.getInstance().getReference();
        /*referencia a al elemento dentro de la base dde datos*/
        DatabaseReference rutas = database.child("Rutas");

        // Read from the database
        rutas.addValueEventListener(new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*-------------*/
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    try{
                        Ruta ruta = item.getValue(Ruta.class);
                        //listrutas.add(ruta);
                        Vars.listrutas.add(ruta);
                    }catch(Exception e){
                        Log.i("|----- RUTA -----|"," Error: "+e);
                    }
                }
                /*----------*/
                Imprimir_etiquetas();
                //Buscador_rutas(listrutas);
                /*----------*/
                //Datos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("CancelacionDatabase", "Failed to read value.", error.toException());
            }
        });
    }
    /*----------------------------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*----------------------*/
        Log.i("|----- Entro a la funcion -----|","Json");
        DescargarJson();
        Log.i("|----- Salio de la funcion -----|","Json");
        /*----------------------*/
        Datos();
        mMap = googleMap;
        UbicacionUser();
    }

    private void AgregarMarcador(double Latitude, double Longitude) {
        LatLng user = new LatLng(Latitude, Longitude);
        CameraUpdate ubicacion =CameraUpdateFactory.newLatLngZoom(user,18);
        if(marcador!=null){
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions().position(user).title("Usuario"));
        mMap.animateCamera(ubicacion);
    }

    private void Actualizar(Location location) {
        if (location != null){
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            AgregarMarcador(Latitude,Longitude);
        }

    }

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

    private void UbicacionUser() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No se dieron los permisos", Toast.LENGTH_LONG).show();
        } else{
            assert locationManager != null;
            Location lol = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            Actualizar(lol);
        }
    }

    public static void Imprimir_etiquetas(){
        LatLng lt;
        /*----------------------*/
        if(Vars.listrutas != null) {
            /*--------------*/
            for (Ruta rt : Vars.listrutas) {
                for (Parada prd : rt.getParada()) {
                    lt = new LatLng(prd.getCoordenadaX(), prd.getCoordenadaY());
                    mMap.addMarker(new MarkerOptions().position(lt).title(prd.getDireccion()));
                    /*Log.e("|----- Prueba -----", prd.getDireccion());*/
                }
            }
            /*--------------*/
        }else {
            Log.i("|----- Error -----|", "La lista se encuentra vacia.");
        }
        /*----------------------*/
    }

    public void Buscador_rutas(ArrayList<Ruta> lista){
        int i = 0;
        for(Ruta rt : lista){
            nombres[i] = rt.getNombre();
            i++;
        }

    }

    /*Funciones de prueba*/
    public static void Datos(){
        Log.i("|----- Ruta0 -----|","La parada de la ruta "+Vars.listrutas.get(0).getNombre()+" es: "+Vars.listrutas.get(3).getParada().get(0).direccion);
    }
}