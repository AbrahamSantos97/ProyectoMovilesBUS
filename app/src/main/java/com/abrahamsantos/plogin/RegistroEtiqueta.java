package com.abrahamsantos.plogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    /*--- Camara ---*/
    private final String CARPETA_RAIZ="FotoAppRutas/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ + "Fotos";
    private final int CODIGO_FOTO = 450;
    /*--- Ruta ---*/
    String path;
    ImageView imagen;
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

        if(validarPermisos()){
            bt1.setEnabled(true);
        }else{
            bt1.setEnabled(false);
        }

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
        String path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombre;
        /*---  ---*/
        File imagenf = new File(path);
        Uri ArchivoSalida = FileProvider.getUriForFile(RegistroEtiqueta.this, getApplicationContext().getPackageName()+".provider",imagenf);
        Intent intCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intCamera.putExtra(MediaStore.EXTRA_OUTPUT,ArchivoSalida);
        startActivityForResult(intCamera,CODIGO_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            switch (resultCode){
                case CODIGO_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("|--- Ruta Alm ---|","Path"+path);
                        }
                    });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    break;
            }
        }
    }

}
