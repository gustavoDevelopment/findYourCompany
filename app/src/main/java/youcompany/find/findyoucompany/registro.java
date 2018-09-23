package youcompany.find.findyoucompany;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class registro extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private Usuario usuario;
    private FirebaseAuth miLogin;
    private FirebaseUser userLogin;

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
    }

    public void crearUsuario(Usuario usu){
        this.getMyRef().child("users").child(usu.getNombre()).setValue(usu);
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
        startActivity(inte);
    }

}
