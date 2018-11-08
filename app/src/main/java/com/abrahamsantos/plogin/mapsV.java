package com.abrahamsantos.plogin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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

import java.io.Console;
import java.util.ArrayList;

public class mapsV extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private LocationManager locManager;
    private Location loc;
    private Marker marcador;
    private double Latitude = 0.0, Longitude = 0.0;
    private ArrayList<Camiones> puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_v);
        android.support.v7.widget.Toolbar barra = findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*puntos.add(new Camiones("Bolivar",19.4326077, -99.13320799999997));
        puntos.add(new Camiones("Boca del rio",19.4326077, -99.13320799999997));
        puntos.add(new Camiones("Norte sur",19.4326077, -99.13320799999997));
        puntos.add(new Camiones("Revolucion",19.4326077, -99.13320799999997));
        puntos.add(new Camiones("Volcanes",19.4326077, -99.13320799999997));*/

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*for (Camiones bus: puntos) {
            LatLng punBus = new LatLng(bus.getLatitud(),bus.getLongitud());
            mMap.addMarker(new MarkerOptions().position(punBus).title(bus.getRuta()).icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punBus,14));
        }*/
        LatLng parada1 = new LatLng(19.4326077,-99.13320799999997);
        mMap.addMarker(new MarkerOptions().position(parada1).title("Parada1"));

        LatLng parada2 = new LatLng(0.0,0.0);
        mMap.addMarker(new MarkerOptions().position(parada2).title("Parada2"));

        LatLng parada3 = new LatLng(10.845,-67.34563);
        mMap.addMarker(new MarkerOptions().position(parada3).title("Parada3"));

        //LatLng parada4 = new LatLng(19.4326077,-99.13320799999997);
        //mMap.addMarker(new MarkerOptions().position(parada1).title("Parada1"));
        UbicacionUser();
        /*LatLng usuario = new LatLng(loc.getLatitude(), loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(usuario).title("Marker of Abraham"));*/


        /*LatLng bus = new LatLng(19.4326077, -99.13320799999997);
        mMap.addMarker(new MarkerOptions().position(bus).title("Marker of Bus").icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bus));
        /mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usuario, 14));*/
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

