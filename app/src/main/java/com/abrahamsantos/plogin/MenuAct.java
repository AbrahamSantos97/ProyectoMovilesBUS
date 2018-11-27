package com.abrahamsantos.plogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MenuAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_act);
        Intent nuevo = new Intent(MenuAct.this,Recycler.class);
        startActivity(nuevo);
    }

}
