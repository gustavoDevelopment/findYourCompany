package youcompany.find.findyoucompany;

import android.Manifest;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import bo.User;
import bo.Usuario;
import interfaces.apiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int estadoPermisoLocalizacion;
    static final int CODE_REQUEST=10;
    private FirebaseAuth miLogin;
    private String email;
    private String pass;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser userLogin;
    User usuarioLogin;

    public FirebaseAuth getMiLogin() {
        return miLogin;
    }

    public void setMiLogin(FirebaseAuth miLogin) {
        this.miLogin = miLogin;
    }

    public User getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(User usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public int getEstadoPermisoLocalizacion() {
        return estadoPermisoLocalizacion;
    }

    public void setEstadoPermisoLocalizacion(int estadoPermisoLocalizacion) {
        this.estadoPermisoLocalizacion = estadoPermisoLocalizacion;
    }


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
        this.miLogin.getCurrentUser();
        this.crearFirebaseDB();
    }

    public void loadDatos(){
        this.setEmail(((EditText)findViewById(R.id.user_email)).getText().toString().trim());
        this.setPass(((EditText)findViewById(R.id.user_pass)).getText().toString().trim());
    }

    public void login(View vista){
        this.loadDatos();
        if(!this.getEmail().equals("") ){
            if(!this.getPass().equals("")) {
                this.validarLogin(this.getEmail(),this.getPass());
                System.out.println("Intento para ----> "+((EditText)findViewById(R.id.user_email)).getText().toString());
                System.out.println("Usando el password ----> "+((EditText)findViewById(R.id.user_pass)).getText().toString());

            }else{
                ((EditText)findViewById(R.id.user_email)).setError("El e-mail es requerido!");
            }
        }else{
            ((EditText)findViewById(R.id.user_pass)).setError("la clave es requerida!");
        }
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
       this.setDatabase(FirebaseDatabase.getInstance());
       this.setMyRef(this.getDatabase().getReference());
    }

    protected  void validarLogin(final String email, final String pass){

        this.getUsuario(email);
        if(this.usuarioLogin!=null)
            if(this.usuarioLogin.getId()!=null)
                this.lanzarLgoin();
    }

    protected void lanzarRegistro(){
        Intent inte= new Intent(this, registro.class);
        inte.putExtra("email",this.email);
        startActivity(inte);
    }

    protected void lanzarLgoin(){
        Intent inte= new Intent(this, home.class);
        inte.putExtra(Usuario.PROP_CLAVE,this.getPass());
        inte.putExtra(Usuario.PROP_EMAIL,this.getEmail());
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }

    private void getUsuario(String userName) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://cunlmsprueba.catedra.edu.co:8090").addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<User> call = service.getUserById(1);

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
