package youcompany.find.findyoucompany;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bo.Usuario;

import static youcompany.find.findyoucompany.MainActivity.CODE_REQUEST;

public class registro extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private Usuario usuario;
    private FirebaseAuth miLogin;
    private FirebaseUser userLogin;
    private LocationManager locManager;
    private Location loc;
    private boolean reloadLocationAlter=true;
    long time = 600* 1000;
    long distance = 10;


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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @Override
    public void onStart(){
        super.onStart();
        this.crearFirebaseDB();
        this.usuario= new Usuario();
        this.miLogin= FirebaseAuth.getInstance();
        this.getUsuario().setEmail(getIntent().getExtras().getString("email"));
        ((EditText)findViewById(R.id.user_email_r)).setText(this.getUsuario().getEmail());
    }

    protected  void crearFirebaseDB(){
        this.setDatabase(FirebaseDatabase.getInstance());
        this.setMyRef(this.getDatabase().getReference());
    }

    public void registrar(View vista){
        this.loadDatos();
        if(!this.getUsuario().getEmail().equals("") ) {
            if (!this.getUsuario().getNombre().equals("")) {
                if (!this.getUsuario().getClave().equals("")) {
                    this.crearUsuario(this.getUsuario());
                }else{
                    ((EditText)findViewById(R.id.user_pass_r)).setError("El password es requerido!");
                }
            }else{
                ((EditText)findViewById(R.id.user_name_r)).setError("El Nombre es requerido!");
            }
        }else{
                ((EditText)findViewById(R.id.user_email_r)).setError("El E-mail es requerido!");
        }
    }

    public void loadDatos(){
        this.getUsuario().setEmail(((EditText)findViewById(R.id.user_email_r)).getText().toString().trim());
        this.getUsuario().setNombre(((EditText)findViewById(R.id.user_name_r)).getText().toString().trim());
        this.getUsuario().setApellido(((EditText)findViewById(R.id.user_last_name_r)).getText().toString().trim());
        this.getUsuario().setClave(((EditText)findViewById(R.id.user_pass_r)).getText().toString().trim());
        this.getUsuario().setId(this.myRef.push().getKey());

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
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance,locationListener);

        }


    }

    public void makeUseOfNewLocation(Location l){
        double lat = l.getLatitude();
        //loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            this.getUsuario().setLatitud(String.valueOf(l.getLatitude()));
            this.getUsuario().setLongitud(String.valueOf(l.getLongitude()));
            this.getMyRef().child("users").child(this.getUsuario().getClave()).setValue(this.getUsuario());
            this.setReloadLocationAlter(false);
        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }

    public void alterLocationForce(){
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"No tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUEST );
            }else{
                Toast.makeText(this,"tiene permisos de Localizacíon",Toast.LENGTH_LONG).show();
                this.getUsuario().setLatitud(String.valueOf(((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLatitude()));
                this.getUsuario().setLongitud(String.valueOf(((Location) locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).getLongitude()));
                this.getMyRef().child("users").child(this.getUsuario().getClave()).setValue(this.getUsuario());
                this.setReloadLocationAlter(false);
            }
        }catch (Exception e){
            Log.d("LOCATION","NO se consigio los daos de localizacion.");
        }
    }

    public void crearUsuario(Usuario usu){
        if(this.isReloadLocationAlter())
            alterLocationForce();
        this.getMyRef().child("users").child(usu.getClave()).setValue(usu);
        this.miLogin.createUserWithEmailAndPassword(usu.getEmail(), usu.getClave())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Create", "createUserWithEmail:success");
                            userLogin = miLogin.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("No-create", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

        Intent inte= new Intent(this, home.class);
        inte.putExtra(Usuario.PROP_EMAIL,this.usuario.getEmail());
        inte.putExtra(Usuario.PROP_CLAVE,this.usuario.getClave());
        startActivity(inte);
    }

    public boolean isReloadLocationAlter() {
        return reloadLocationAlter;
    }

    public void setReloadLocationAlter(boolean reloadLocationAlter) {
        this.reloadLocationAlter = reloadLocationAlter;
    }
}
