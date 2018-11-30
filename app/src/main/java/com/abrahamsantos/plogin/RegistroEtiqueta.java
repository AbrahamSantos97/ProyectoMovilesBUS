package com.abrahamsantos.plogin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegistroEtiqueta extends AppCompatActivity {
    /*--- Botones ---|*/
    Button bt1,bt2;
    /*--- Combobox ---*/
    EditText edtx1;
    /*--- Radiobutton ---*/
    RadioGroup rdg1,rdg2;
    /*--- Variables de retorno ---*/
    String Ruta,Direccion,Imagenbits;
    int riesgo = 3;
    /*--- Camara ---*/
    private final String CARPETA_RAIZ="FotoAppRutas/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ + "Fotos";
    private final int CODIGO_FOTO = 450;
    int flag = 0;
    /*--- Ruta ---*/
    String path;
    ImageView imagen;
    /*-------------------*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_etiqueta);

        edtx1 = findViewById(R.id.direccion);
        rdg1 = findViewById(R.id.rdg1);
        rdg2 = findViewById(R.id.rdg2);
        bt1 = findViewById(R.id.foto);
        bt2 = findViewById(R.id.envio);


        rdg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rd1_1:
                        Ruta = "Chapultepec";
                        break;
                    case R.id.rd1_2:
                        Ruta = "NorteSur";
                        break;
                    case R.id.rd1_3:
                        Ruta = "Bolivar";
                        break;
                    case R.id.rd1_4:
                        Ruta = "Petrolera";
                            break;
                    case R.id.rd1_5:
                        Ruta = "Ruta 6";
                        break;
                }
            }
        });

        if(validarPermisos()){
            bt1.setEnabled(true);
        }else{
            bt1.setEnabled(false);
        }

        /*--- Menu RadioGroup ---*/
        rdg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd2_1:
                        Log.i("|---- RadioGroup ----|","1");
                        riesgo = 1;
                        break;
                    case R.id.rd2_2:
                        Log.i("|---- RadioGroup ----|","2");
                        riesgo = 2;
                        break;
                    case R.id.rd2_3:
                        Log.i("|---- RadioGroup ----|","3");
                        riesgo = 3;
                        break;
                    case R.id.rd2_4:
                        Log.i("|---- RadioGroup ----|","4");
                        riesgo = 4;
                        break;
                    case R.id.rd2_5:
                        Log.i("|---- RadioGroup ----|","5");
                        riesgo = 5;
                        break;
                }
            }
        });
        /*--- Button_1 ---*/
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("|---- Button ----|","1");
                TomarFoto();
            }
        });
        /*--- Button_2 ---*/
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("|---- Button ----|","2");
                /*--- EditText ---*/
                Direccion = edtx1.getText().toString();
                /*--- Validaciones ---*/
                if((Ruta.isEmpty()) || (Direccion.isEmpty()) || (flag == 0)){
                    Toast.makeText(RegistroEtiqueta.this, "Faltan elementos en el formulario",
                            Toast.LENGTH_LONG).show();
                }else{
                    try {
                        /*------------------*/
                        Intent returnInt = new Intent();
                        returnInt.putExtra("NombreR",Ruta);
                        returnInt.putExtra("Direccion",Direccion);
                        returnInt.putExtra("Riesgo",riesgo);
                        returnInt.putExtra("Imagen",Imagenbits);
                        setResult(Activity.RESULT_OK,returnInt);
                        /*-----------------*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }

            }
        });
    }

    private boolean validarPermisos() {
        /*--- Validar version del codigo ---*/
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        /*--- Validar permisos ---*/
        if((checkCallingPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkCallingPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            MostrarDialogo();
        }else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                bt1.setEnabled(true);
            }
        }
    }

    private void MostrarDialogo() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(RegistroEtiqueta.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debera aceptar los permisos de uso para utilizar la App.");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    private void TomarFoto(){
        /*--- Almacenar archivos ---*/
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        /*--- Existencia de archivos ---*/
        boolean existe = fileImagen.exists();
        /*--- NombreImagen ---*/
        String nombre;
        /*--- Crear ruta del archivos ---*/
        if(existe == false){
            existe = fileImagen.mkdirs();
        }
        /*--- Existe/Crear ---*/
        if(existe == true){
            nombre = (System.currentTimeMillis()/1000) + ".jpg";
        }else{
            return;
        }
        /*--- Formar ruta ---*/
        path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombre;
        Intent intCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intCamera,CODIGO_FOTO);
        flag = 1;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && data != null){
            switch (requestCode){
                case CODIGO_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("|--- Ruta Alm ---|","Path:"+path);

                        }
                    });
                    Imagenbits =  BitMapToString((Bitmap) data.getExtras().get("data"));
                    break;
            }
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
