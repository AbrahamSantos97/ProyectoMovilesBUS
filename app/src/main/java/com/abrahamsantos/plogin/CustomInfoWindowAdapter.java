package com.abrahamsantos.plogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mView;
    private Context context;
    private Bitmap bitmap;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
        this.mView = LayoutInflater.from(context).inflate(R.layout.info_windos_market,null);
        this.bitmap = null;
    }

    private void Estructura_ventana(Marker marker,View view){
        /*--- Obtencion de variables ---*/
        TextView txt1 = view.findViewById(R.id.Info_title);
        ImageView img = view.findViewById(R.id.InfoImage);
        /*--- Obtener info del market establecido ---*/
        String title = marker.getTitle();
        String Imagen = marker.getSnippet();
        /*--- Asignar estilos ---*/
        txt1.setText(title);
        bitmap = StringToBitMap(Imagen);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
            img.setVisibility(View.VISIBLE);
        } else {
            if(!title.equals("Usuario")){
                img.setImageResource(R.drawable.no_image);
                img.setVisibility(View.VISIBLE);
            }else{
                img.setVisibility(View.GONE);
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Estructura_ventana(marker,mView);
        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Estructura_ventana(marker,mView);
        return mView;
    }
}
