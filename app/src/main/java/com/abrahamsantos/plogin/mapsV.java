package com.abrahamsantos.plogin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class mapsV extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{
    /*----- Variables -----*/
    static private GoogleMap mMap;
    private LocationManager locManager;
    private LatLng user;
    private Marker marcador;
    private double Latitude = 0.0, Longitude = 0.0;
    private int clicBuscar=1;
    /*------ IntentResult EtiquetaRegistro ---------*/
    private final int CODIGO_REGETI = 100;
    String NombreR,Direccion,ImagenRE;
    int Riesgo;
    Bitmap bitmap;
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
        }

        @Override
        public ArrayList<Ruta> getRutas() {
            return this.lista;
        }

        @Override
        public void setMenuRutas(ArrayList<Ruta> nuevo, View view){
        }

        @Override
        public void nuevaParada(ArrayList<Ruta> lista_ruta) {
            HashMap<String,Object> hashMap = new HashMap<>();
            int i=0;
            Parada nueva_parada = new Parada();
            nueva_parada.coordenadaX = user.latitude;
            nueva_parada.coordenadaY = user.longitude;
            nueva_parada.direccion = Direccion;
            nueva_parada.riesgo = Riesgo;
            nueva_parada.imagen = ImagenRE;
            for(Ruta ruta:lista_ruta){
                if(ruta.getNombre().equals(NombreR)){
                    lista_ruta.get(i).getParada().add(nueva_parada);
                    hashMap.put("Ruta"+i,lista_ruta.get(i));
                }
                i++;
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Rutas").updateChildren(hashMap);
        }

    };

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
            case R.id.menu:
                Intent intentR = new Intent(getBaseContext(), MenuAct.class);
                startActivity(intentR);
                break;

            case R.id.nueva_etiqueta:
                Intent etiquetaN = new Intent(getBaseContext(),RegistroEtiqueta.class);
                startActivityForResult(etiquetaN,CODIGO_REGETI);
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
    /*--- ??Metodos heredado o implementado ?? ---*/
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            user = new LatLng(location.getLatitude(),location.getLongitude());
            if(marcador!=null){
                marcador.remove();
            }
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(user)
                    .title("Usuario")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            );
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
        String nombre_ruta;
        for(Ruta ruta:lista){
            nombre_ruta = ruta.getNombre();
            BitmapDescriptor bitmapDescriptor = colorearRuta(nombre_ruta);
            for(Parada pd:ruta.getParada()){
                lt = new LatLng(pd.getCoordenadaX(),pd.getCoordenadaY());
                /*--- Agregar etiqueta ---*/
                mMap.addMarker(new MarkerOptions()
                        .position(lt)
                        .title(nombre_ruta)
                        .snippet(pd.getImagen())
                        .icon(bitmapDescriptor)
                );
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(mapsV.this));
            }
        }
    }

    private BitmapDescriptor colorearRuta(String nombre_ruta) {
        BitmapDescriptor descriptor = null;
        switch(nombre_ruta){
            case "Chapultepec":
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                break;
            case "NorteSur":
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            case "Bolivar":
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                break;
            case "Petrolera":
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
            case "Ruta 6":
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            default:
                Log.i("|--- ColorMarker ---|", "Caso default");
                break;
           }
           return descriptor;
    }

    /*-- Obtiene la ultima ubicacion del usuario ---*/
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
    /*--- Resultado de EtiquetaRegistro ---*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case CODIGO_REGETI:
                    /*Recivir datos del registro ET*/
                    NombreR = data.getStringExtra("NombreR");
                    Direccion = data.getStringExtra("Direccion");
                    Riesgo = data.getIntExtra("Riesgo",Riesgo);
                    ImagenRE = data.getStringExtra("Imagen");
                    EnviarInformacion();
                    break;
            }
        }
    }

    private void EnviarInformacion() {
        /*--- Instancia a la base de datos ---*/
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Rutas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Ruta> lista_rutas = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Ruta ruta = data.getValue(Ruta.class);
                    lista_rutas.add(ruta);
                }
                data.nuevaParada(lista_rutas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}