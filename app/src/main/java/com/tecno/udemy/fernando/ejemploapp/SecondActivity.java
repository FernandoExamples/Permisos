package com.tecno.udemy.fernando.ejemploapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    TextView textViewHellow;
    EditText txtPhone;
    EditText txtWeb;
    ImageButton btnPhone;
    ImageButton btnWeb;
    ImageButton btnDialPhone;
    ImageButton btnContacts;
    ImageButton btnEmail;
    ImageButton btnFastEmail;
    ImageButton btnCamara;
    private final int CALL_CODE = 100; //valor arbitrario
    private final int CAMERA_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon_round);

        textViewHellow = (TextView) findViewById(R.id.textViewHellow);
        txtPhone = (EditText) findViewById(R.id.editTextPhone);
        txtWeb = (EditText) findViewById(R.id.editTextWeb);
        btnPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        btnWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        btnContacts = (ImageButton) findViewById(R.id.imageButtonContacts);
        btnEmail = (ImageButton) findViewById(R.id.imageButtonEmail);
        btnFastEmail = (ImageButton) findViewById(R.id.imageButtonFastEmail);
        btnDialPhone = (ImageButton) findViewById(R.id.imageButtonDialCall);
        btnCamara = (ImageButton) findViewById(R.id.buttonCamara);

        Bundle bundle = getIntent().getExtras();
        textViewHellow.setText(bundle.getString("gretter"));

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = txtPhone.getText().toString();
                if(!phoneNumber.isEmpty()){
                    //comprobar la version que estamos corriendo
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        if(checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                            //ya ha aceptado
                            launchCallIntent(phoneNumber);
                        }else{
                            //Ha denegado o es la primera vez que se le pregunta
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //no se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_CODE);
                            }else{
                                //ha denegado
                                Toast.makeText(SecondActivity.this, "Please enable the request permission",Toast.LENGTH_LONG).show();
                                Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                settingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                settingIntent.setData(Uri.parse("package:" + getPackageName())); //esta linea le pasa al intent el nombre de la aplicacion para ir a los settings especificos de esta
                                settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                settingIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                settingIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(settingIntent);
                            }

                        }

                    }else{
                        /* En versiones anteriores al API 23 los permisos se concedian desde instalar la aplicacion. Por tanto
                         *no es necesario pedir permiso en tiempo de ejecución*/
                        launchCallIntent(phoneNumber);
                    }
                }else
                    Toast.makeText(SecondActivity.this, "Insert a phone number", Toast.LENGTH_LONG).show();
            }

            private void launchCallIntent(String phoneNumber) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intentCall);
            }

        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String web = txtWeb.getText().toString();
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+web));
                startActivity(webIntent);
            }
        });

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
                startActivity(contactIntent);
            }
        });

        btnFastEmail.setOnClickListener(new View.OnClickListener() {
            String email = "fernandotovar9902@gamil.com";
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi Fernando!!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the mesagge's text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"pepe@gemail.com", "maria@gmail.com"});
                startActivity(emailIntent);
            }
        });

        btnDialPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*de esta forma de llamar al telefono nos ahorramos los permisos porque no es nuestra aplicacion la que
                realiza la llamada en si sino la propia aplicacion de llamadas del telefono*/
                Intent dialCallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4111172266"));
                dialCallIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                dialCallIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(dialCallIntent);
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            String email = "fernandotovar9902@gamil.com";
            @Override
            public void onClick(View v) {
                /*Este es un intent de un email que no va dirigido, notar la diferencia con el SENDTO que va dirigido
                * De hecho es una forma equivalente al anterior porque se le puede pasar los emails con el mismo extra
                * EXTRA_EMAIL*/
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse(email));
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi Fernando!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the other mesagge's text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"pepe@gemail.com", "maria@gmail.com"});
                startActivity(Intent.createChooser(emailIntent, "Choose the email server")); //no jalo el chooser
            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        launchCameraIntent();
                    }else if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
                    }else{
                        Toast.makeText(SecondActivity.this, "Please enable the request permission",Toast.LENGTH_LONG).show();
                        Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        settingsIntent.setData(Uri.parse("package:"+getPackageName()));
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(settingsIntent);
                    }
                }else{
                    launchCameraIntent();
                }
            }

            private void launchCameraIntent(){
                Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamera, CAMERA_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CALL_CODE){
            if(permissions[0].equals(Manifest.permission.CALL_PHONE))
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    String phoneNumber = txtPhone.getText().toString();
                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                    startActivity(intentCall);
                }else
                    Toast.makeText(SecondActivity.this, "You declined the permission", Toast.LENGTH_SHORT).show();
        }

        else if(requestCode == CAMERA_CODE){
            //if(permissions[0].equals(Manifest.permission.CAMERA))
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamera, CAMERA_CODE);
            }else
                Toast.makeText(SecondActivity.this, "You declined the permission", Toast.LENGTH_SHORT).show();
        }

        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_CODE){
            if(resultCode == RESULT_OK){
                //se manipula la imagen
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
