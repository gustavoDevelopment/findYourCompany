package youcompany.find.findyoucompany;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bo.User;
import bo.Usuario;
import interfaces.apiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constantes;

import static youcompany.find.findyoucompany.MainActivity.CODE_REQUEST;

public class home extends AppCompatActivity {

    private String latitud;
    private String longitud;
    private LocationManager locManager;
    private Location loc;
    private boolean reloadLocationAlter=true;
    long time = 600* 1000;
    long distance = 10;
    User usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setUsuarioLogin((User)getIntent().getExtras().get("login"));
        ((TextView) findViewById(R.id.app_user_login)).setText("Login: " + getUsuarioLogin().getPrimerNombre());
        reUbicar(this.getUsuarioLogin());
        ((TextView) findViewById(R.id.text_long)).setText("Longitud: " + getUsuarioLogin().getLonguitud());
        ((TextView) findViewById(R.id.text_lat)).setText("Latitud: " + getUsuarioLogin().getLatitud());
        this.actualizarPosicion(this.getUsuarioLogin());
    }

    @Override
    protected  void onStart() {
        super.onStart();
    }


    public void goToMapa(View vista){
        Intent inte= new Intent(this, mapas.class);
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }

    public void goToUser(View vista){
        Intent inte= new Intent(this, usuarios.class);
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }

    public void goToGrupos(View vista){
        Intent inte= new Intent(this, grupos.class);
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }

    public void reUbicar(final User usuarioLogeado){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
        }else{
            Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    makeUseOfNewLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,locationListener);
            if(this.isReloadLocationAlter())
                alterLocationForce();

        }
    }

    public void makeUseOfNewLocation(Location l){
        double lat = l.getLatitude();
        //loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            setLatitud(String.valueOf(l.getLatitude()));
            setLongitud(String.valueOf(l.getLongitude()));
            try {
                this.getUsuarioLogin().setLatitud(Float.valueOf(String.valueOf(l.getLatitude())));
                this.getUsuarioLogin().setLonguitud(Float.valueOf(String.valueOf(l.getLongitude())));
            }catch (Exception e){
                Log.d("LOCATION","NO se consigio los daos de localizacion.");
            }

        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public boolean isReloadLocationAlter() {
        return reloadLocationAlter;
    }

    public void setReloadLocationAlter(boolean reloadLocationAlter) {
        this.reloadLocationAlter = reloadLocationAlter;
    }

    public User getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(User usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public void alterLocationForce(){
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
            }else{
                Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                this.getUsuarioLogin().setLatitud(Float.valueOf (String.valueOf( ((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLatitude())));
                this.getUsuarioLogin().setLonguitud(Float.valueOf (String.valueOf(((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLongitude())));
                this.setReloadLocationAlter(false);
            }
        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }

    private void actualizarPosicion(User usuario){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<User> call = service.actualizarUsuario(usuario);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUsuarioLogin(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
