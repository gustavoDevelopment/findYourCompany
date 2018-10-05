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

import bo.Usuario;

import static youcompany.find.findyoucompany.MainActivity.CODE_REQUEST;

public class home extends AppCompatActivity {

    private String latitud;
    private String longitud;
    private LocationManager locManager;
    private Location loc;
    private Usuario logeado;
    String emailLog;
    String claveLog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    private boolean reloadLocationAlter=true;
    long time = 600* 1000;
    long distance = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setEmailLog(getIntent().getExtras().getString(Usuario.PROP_EMAIL));
        this.setClaveLog(getIntent().getExtras().getString(Usuario.PROP_CLAVE));
    }

    @Override
    protected  void onStart() {
        super.onStart();
        this.logeado = new Usuario();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot dt : data) {
                    Usuario loaded = dt.getValue(Usuario.class);
                    if (loaded.getClave().equals(getClaveLog())) {
                        setLogeado(dt.getValue(Usuario.class));
                        reUbicar(getIntent().getExtras().getString(Usuario.PROP_CLAVE));
                        try {
                            ((TextView) findViewById(R.id.text_lat)).setText("Lat: " + getLogeado().getLatitud());
                            ((TextView) findViewById(R.id.text_long)).setText("Lon: " + getLogeado().getLongitud());
                        }catch (Exception e){

                        }
                        break;
                    }
                }
                Log.d("TAG", "Value is: " + data);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    public void lanzarMapa(View vista){
        Intent inte= new Intent(this, mapas.class);
        inte.putExtra("logeado",this.getLogeado());
        startActivity(inte);
    }


    public void makeUseOfNewLocation(Location l,String clave){
        double lat = l.getLatitude();
        //loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            setLatitud(String.valueOf(l.getLatitude()));
            setLongitud(String.valueOf(l.getLongitude()));
            try {
                this.getLogeado().setLatitud(String.valueOf(l.getLatitude()));
                this.getLogeado().setLongitud(String.valueOf(l.getLongitude()));
                this.getMyRef().child(this.getLogeado().getClave()).setValue(this.getLogeado());
            }catch (Exception e){
                Log.d("LOCATION","NO se consigio los daos de localizacion.");
            }

        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }

    public void reUbicar(final String claveUpdate){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
        }else{
            Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    makeUseOfNewLocation(location,claveUpdate);
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

    public Usuario getLogeado() {
        return logeado;
    }

    public void setLogeado(Usuario logeado) {
        this.logeado = logeado;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public void setMyRef(DatabaseReference myRef) {
        this.myRef = myRef;
    }

    public String getEmailLog() {
        return emailLog;
    }

    public void setEmailLog(String emailLog) {
        this.emailLog = emailLog;
    }

    public String getClaveLog() {
        return claveLog;
    }

    public void setClaveLog(String claveLog) {
        this.claveLog = claveLog;
    }

    public boolean isReloadLocationAlter() {
        return reloadLocationAlter;
    }

    public void setReloadLocationAlter(boolean reloadLocationAlter) {
        this.reloadLocationAlter = reloadLocationAlter;
    }

    public void alterLocationForce(){
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
            }else{
                Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                this.getLogeado().setLatitud(String.valueOf(((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLatitude()));
                this.getLogeado().setLongitud(String.valueOf(((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLongitude()));
                this.getMyRef().child(this.getLogeado().getClave()).setValue(this.getLogeado());
                this.setReloadLocationAlter(false);
            }
        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }
}
