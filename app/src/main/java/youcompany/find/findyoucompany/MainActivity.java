package youcompany.find.findyoucompany;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private int estadoPermisoLocalizacion;
    static final int CODE_REQUEST=10;
    private FirebaseAuth miLogin;
    private String named;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.miLogin= FirebaseAuth.getInstance();
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

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser userLogin=this.miLogin.getCurrentUser();
    }


    public void lanzarHome(View vista){
        this.setNamed(((EditText)findViewById(R.id.user_name)).getText().toString().trim());
        this.setPass(((EditText)findViewById(R.id.user_pass)).getText().toString().trim());
        if(!this.getNamed().equals("") ){
            if(!this.getPass().equals("")) {
                this.crearFirebaseDB();
                this.validarLogin(this.getNamed(),this.getPass());

                System.out.println("Login para ----> "+((EditText)findViewById(R.id.user_name)).getText().toString());
                System.out.println("Usando el password ----> "+((EditText)findViewById(R.id.user_pass)).getText().toString());
                Intent inte= new Intent(this, home.class);
                startActivity(inte);


            }else{
                ((EditText)findViewById(R.id.user_name)).setError("El password es requerido!");
            }
        }else{
            ((EditText)findViewById(R.id.user_name)).setError("El usuario es requerido!");
        }
    }

    public int getEstadoPermisoLocalizacion() {
        return estadoPermisoLocalizacion;
    }

    public void setEstadoPermisoLocalizacion(int estadoPermisoLocalizacion) {
        this.estadoPermisoLocalizacion = estadoPermisoLocalizacion;
    }

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    protected  void crearFirebaseDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
    }
    protected  void validarLogin(String named, String pass){
        this.miLogin.signInWithEmailAndPassword(named, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AASKDJAS", "signInWithEmail:success");
                            FirebaseUser user = miLogin.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ASDASDASD", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

}
