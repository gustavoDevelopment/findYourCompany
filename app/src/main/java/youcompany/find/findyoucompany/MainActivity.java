package youcompany.find.findyoucompany;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import utils.Constantes;

public class MainActivity extends AppCompatActivity {

    private int estadoPermisoLocalizacion;
    static final int CODE_REQUEST=10;
    private FirebaseAuth miLogin;
    private String email;
    private String pass;
    User usuarioLogin;

    private ProgressDialog pd = null;
    private Object data = null;

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
    }

    public void loadDatos(){
        this.setEmail(((EditText)findViewById(R.id.user_email)).getText().toString().trim());
        this.setPass(((EditText)findViewById(R.id.user_pass)).getText().toString().trim());
    }

    public void login(View vista){
        this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);
        this.loadDatos();
        if(!this.getEmail().equals("") ){
            if(!this.getPass().equals("")) {
                this.validarLogin(this.getEmail(),this.getPass());
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

    protected  void validarLogin(final String email, final String pass){
        this.getUsuario(email,pass);
    }

    protected void lanzarRegistro(){
        Intent inte= new Intent(this, registro.class);
        inte.putExtra("email",this.email);
        startActivity(inte);
    }

    protected void lanzarLgoin(){
        this.pd.dismiss();
        Intent inte= new Intent(this, home.class);
        inte.putExtra(Usuario.PROP_CLAVE,this.getPass());
        inte.putExtra(Usuario.PROP_EMAIL,this.getEmail());
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }

    protected void noExisteUser(){
        this.pd.dismiss();
        Toast.makeText(this,"El usuario o clave incorrectos",Toast.LENGTH_LONG).show();
    }



    private void getUsuario(String userName,String pass) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<User> call = service.login(userName,pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                   setUsuarioLogin(response.body());
                if(usuarioLogin!=null) {
                    if (usuarioLogin.getId() != null)
                        lanzarLgoin();
                    else{
                        noExisteUser();
                    }
                }else{
                    noExisteUser();
                    }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }



}
