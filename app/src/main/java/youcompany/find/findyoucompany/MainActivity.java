package youcompany.find.findyoucompany;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int estadoPermisoLocalizacion;
    static final int CODE_REQUEST=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setEstadoPermisoLocalizacion(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        if(this.getEstadoPermisoLocalizacion()== PackageManager.PERMISSION_DENIED){
            Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
        }else{
            Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
        }

    }

    public void lanzarHome(View vista){
        Intent inte= new Intent(this, mapas.class);
        startActivity(inte);
    }

    public int getEstadoPermisoLocalizacion() {
        return estadoPermisoLocalizacion;
    }

    public void setEstadoPermisoLocalizacion(int estadoPermisoLocalizacion) {
        this.estadoPermisoLocalizacion = estadoPermisoLocalizacion;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permisos[],int [] resultados){
        switch (requestCode){
            case CODE_REQUEST:{
                if (resultados.length >0 && resultados[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"El usuario dio  permisos de Localizacíon",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this,"No se puede usar no hay permisos de Localizacíon",Toast.LENGTH_LONG).show();
                }
                return ;
            }
        }
    }
}
