package com.abrahamsantos.plogin;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    CardView init;
    CardView regist;
    Intent cambioPage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init = findViewById(R.id.BtnIniciarSesion);
        regist = findViewById(R.id.BtnRegistrar);
        init.setOnClickListener(this);
        regist.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BtnIniciarSesion:
                IniciarSesion.bandera=1;
                cambioPage= new Intent(this,IniciarSesion.class);
                startActivity(cambioPage);
                break;
            case R.id.BtnRegistrar:
                IniciarSesion.bandera=2;
                cambioPage= new Intent(this,IniciarSesion.class);
                startActivity(cambioPage);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
