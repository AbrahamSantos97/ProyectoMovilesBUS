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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private ArrayList<Ruta> rutas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_v);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng lt;
        Parada prd;
        /*----------------------*/
        mMap = googleMap;
        /*----------------------*/
        rutas.add(new Ruta("Bolivar",new Parada(1,"asdQqssQwe","Bolivar fracc. asDa",18.58664,-97.23423)));
        rutas.add(new Ruta("Chapultepec",new Parada(3,"asdQqssQwe","Chapultepec fracc. asDa",17.78664,16.23423)));
        rutas.add(new Ruta("Chapultepec",new Parada(3,"asdQqssQwe","Petrolera fracc. asDa",17.78664,16.23423)));
        /*----------------------*/
        for(Ruta rt: rutas){
            prd = rt.getparada(0);
            lt = new LatLng(prd.getcoordenadaX(),prd.getcoordenadaY());
            mMap.addMarker(new MarkerOptions().position(lt).title(prd.getdireccion()));
        }
        /*----------------------*/

    }

    private void AgregarMarcador(double Latitude, double Longitude) {
        LatLng user = new LatLng(Latitude, Longitude);
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(user,18);
        if(marcador != null){
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
            Location lol = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, listener);
            Actualizar(lol);
        }
    }
}

