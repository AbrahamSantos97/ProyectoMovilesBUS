package com.abrahamsantos.plogin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class mapsV extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private LocationManager locManager;
    private Location loc;
    private Marker marcador;
    private double Latitude = 0.0, Longitude = 0.0;
    private int clicBuscar=1;
    ArrayList<Ruta> rutas = new ArrayList<>();
    AutoCompleteTextView Predic;
    String [] nombres= {"Juan","Juanito","Julian","Maria","Maria Fernanda"};

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
                imn.hideSoftInputFromWindow(Predic.getWindowToken(),0);

                //Aqui se debe de llamar a la funcion que recoja los datos del Predic
            }
        }
        return true;
    }

    private void LlenarAdapter() {
        ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,nombres);
        Predic.setThreshold(2);
        Predic.setAdapter(adaptador);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng lt;
        Parada prd;
        /*----------------------*/
        mMap = googleMap;
        /*----------------------*/
        rutas.add(new Ruta("Chapultepec",new Parada(1,"asdQqssQwe","Av. La Bamba 24, Geovillas del Puerto.",19.153184,-96.161923)));
        rutas.add(new Ruta("Chapultepec",new Parada(5,"asdQqssQwe","Eje 1 Poniente 2647 Mz. 161Lt. 8, Fracc. Geo Villas del Puerto, Geovillas del Puerto.",19.152184, -96.160806)));
        rutas.add(new Ruta("Chapultepec",new Parada(3,"asdQqssQwe","Violetas, Coyol Zona D.",19.153707,-96.157326)));
        rutas.add(new Ruta("Chapultepec",new Parada(3,"asdQqssQwe","Cerro Azul, Coyol Zona D.",19.154959,-96.155167)));
        /*----------------------*/
        for(Ruta rt: rutas){
            prd = rt.getparada(0);
            lt = new LatLng(prd.getcoordenadaX(),prd.getcoordenadaY());
            mMap.addMarker(new MarkerOptions().position(lt).title(prd.getdireccion()));
        }
        /*----------------------*/
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
            return;
        } else{
            Location lol = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, listener);
            Actualizar(lol);
        }
    }
}

