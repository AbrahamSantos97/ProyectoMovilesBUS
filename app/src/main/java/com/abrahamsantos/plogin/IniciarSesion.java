package com.abrahamsantos.plogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    public static int bandera=0;
    public TextView textoButton,textoTitle;
    public CardView button;
    public EditText user,pass;
    public String corre="";
    public String password="";
    public FirebaseAuth auth;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        textoButton= findViewById(R.id.botonChange);
        textoTitle=findViewById(R.id.titulo);
        user=findViewById(R.id.correo);
        pass=findViewById(R.id.Contraseña);
        button = findViewById(R.id.Iniciar);
        textoTitle.setTextSize(20.0f);
        textoButton.setTextSize(20.0f);
        button.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();
        if(bandera==1){
            textoTitle.setText("Iniciar Sesion");
            textoButton.setText("Ingresar");
        }
        if(bandera==2){
            textoTitle.setText("Ingresa tus datos");
            textoButton.setText("Registrar");
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.Iniciar){
            corre=user.getText().toString().trim();
            password=pass.getText().toString().trim();
            if(TextUtils.isEmpty(corre) || TextUtils.isEmpty(password)){
                Toast.makeText(this,"Llenar los campos correspondientes",Toast.LENGTH_LONG).show();
                return;
            }else{
                if(bandera==1){
                    initSesion();
                }
                if(bandera==2){
                    registrar();
                }
            }
        }
    }

    public void registrar() {
        auth.createUserWithEmailAndPassword(corre,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(IniciarSesion.this,"El usuario que intenta registrar ya existe",Toast.LENGTH_LONG).show();
                }
                if(task.isSuccessful()){
                    Toast.makeText(IniciarSesion.this,"Se pudo registrar exitosamente",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),mapsV.class);
                    startActivity(i);
                }else{
                    Toast.makeText(IniciarSesion.this,"No se pudo registrar",Toast.LENGTH_LONG).show();
                    }
                }

        });
    }

    public void initSesion() {
        auth.signInWithEmailAndPassword(corre,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(IniciarSesion.this,"Iniciando sesion, espere",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),mapsV.class);
                    startActivity(i);

                }else{
                    Toast.makeText(IniciarSesion.this,"No se encontro ningun usuario con ese nombre",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
