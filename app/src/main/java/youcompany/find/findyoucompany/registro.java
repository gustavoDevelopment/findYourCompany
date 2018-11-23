package youcompany.find.findyoucompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bo.User;
import interfaces.apiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constantes;

public class registro extends AppCompatActivity {


    private User usuarioNuevo;
    private Integer idUserLoaded;
    private User usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        this.setUsuarioLogin((User)getIntent().getExtras().get("login"));
        try{
            this.setIdUserLoaded(Integer.valueOf(getIntent().getExtras().get("idEdit").toString()));
            this.loadUsuario(this.getIdUserLoaded());
        }catch (Exception e){

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        this.usuarioNuevo= new User();
    }

    public void goToRegistrar(View vista){
        this.loadDatos();
        if(!this.getUsuarioNuevo().getEmail().equals("") ) {
            if (!this.getUsuarioNuevo().getPrimerNombre().equals("")) {
                if (!this.getUsuarioNuevo().getPrimerApellido().equals("")) {
                    this.crearUsuario(this.getUsuarioNuevo());
                }else{
                    ((EditText)findViewById(R.id.input_username)).setError("El password es requerido!");
                }
            }else{
                ((EditText)findViewById(R.id.input_nombres)).setError("El Nombre es requerido!");
            }
        }else{
                ((EditText)findViewById(R.id.input_email)).setError("El E-mail es requerido!");
        }
    }

    public void loadDatos(){
        this.getUsuarioNuevo().setUsername(((EditText)findViewById(R.id.input_username)).getText().toString().trim());
        this.getUsuarioNuevo().setDocumento(((EditText)findViewById(R.id.input_cedula)).getText().toString().trim());
        this.getUsuarioNuevo().setEmail(((EditText)findViewById(R.id.input_email)).getText().toString().trim());
        this.getUsuarioNuevo().setPrimerNombre(((EditText)findViewById(R.id.input_nombres)).getText().toString().trim());
        this.getUsuarioNuevo().setPrimerApellido(((EditText)findViewById(R.id.input_apellidos)).getText().toString().trim());
        this.getUsuarioNuevo().setIdCiudad(1);
        this.getUsuarioNuevo().setIdDepartamento(1);
        this.getUsuarioNuevo().setIdEmpresa(1);
        if(this.getUsuarioNuevo().getLonguitud()==0)
            this.getUsuarioNuevo().setLatitud(Float.valueOf("4.624793"));
        if(this.getUsuarioNuevo().getLonguitud()==0)
            this.getUsuarioNuevo().setLonguitud(Float.valueOf("-74.064364"));
    }

    protected  void crearUsuario(User usuario){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<User> call =null;
        if(this.getIdUserLoaded()==null)
             call=service.agregarUsuario(usuario);
        else if(this.getIdUserLoaded()!=null)
            call=service.actualizarUsuario(usuario);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUsuarioLogin(response.body());
                goToUsers();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    protected  void loadUsuario(Integer id){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<User> call = service.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                setUsuarioNuevo(response.body());
                establecerFormulario();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void establecerFormulario(){
        ((EditText)findViewById(R.id.input_username)).setText(this.usuarioNuevo.getUsername());
        ((EditText)findViewById(R.id.input_username)).setEnabled(false);
        ((EditText)findViewById(R.id.input_cedula)).setText(this.usuarioNuevo.getDocumento());
        ((EditText)findViewById(R.id.input_cedula)).setEnabled(false);
        ((EditText)findViewById(R.id.input_email)).setText(this.usuarioNuevo.getEmail());
        ((EditText)findViewById(R.id.input_nombres)).setText(this.usuarioNuevo.getPrimerNombre());
        ((EditText)findViewById(R.id.input_apellidos)).setText(this.usuarioNuevo.getPrimerApellido());
    }

    public void goToUsers(){
        Intent inte= new Intent(this, usuarios.class);
        inte.putExtra("login",this.getUsuarioLogin());
        startActivity(inte);
    }


    public User getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(User usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public User getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(User usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public Integer getIdUserLoaded() {
        return idUserLoaded;
    }

    public void setIdUserLoaded(Integer idUserLoaded) {
        this.idUserLoaded = idUserLoaded;
    }
}
