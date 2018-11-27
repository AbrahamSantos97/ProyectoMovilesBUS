package com.abrahamsantos.plogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegistroEtiqueta extends AppCompatActivity {
    /*--- Botones ---|*/
    Button bt1,bt2;
    /*--- Combobox ---*/
    EditText edtx1,edxt2;
    /*--- Radiobutton ---*/
    RadioGroup rdg;
    /*--- Variables de retorno ---*/
    String Ruta,Direccion;
    int radiobutton;
    /*-------------------*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_etiqueta);

        edtx1 = findViewById(R.id.nombre_ruta);
        edxt2 = findViewById(R.id.direccion);
        rdg = findViewById(R.id.rdg1);
        bt1 = findViewById(R.id.foto);
        bt2 = findViewById(R.id.envio);
        /*--- EditText ---*/
       Ruta = edtx1.getText().toString();
       Direccion = edxt2.getText().toString();
        /*--- Menu RadioGroup ---*/
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd1:
                        Log.i("|---- RadioGroup ----|","1");
                        radiobutton = 1;
                        break;
                    case R.id.rd2:
                        Log.i("|---- RadioGroup ----|","2");
                        radiobutton = 2;
                        break;
                    case R.id.rd3:
                        Log.i("|---- RadioGroup ----|","3");
                        radiobutton = 3;
                        break;
                    case R.id.rd4:
                        Log.i("|---- RadioGroup ----|","4");
                        radiobutton = 4;
                        break;
                    case R.id.rd5:
                        Log.i("|---- RadioGroup ----|","5");
                        radiobutton = 5;
                        break;
                }
            }
        });
        /*--- Button ---*/
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("|---- Button ----|","1");
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("|---- Button ----|","2");
            }
        });
    }
}
