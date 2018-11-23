package youcompany.find.findyoucompany;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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

public class mapas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng POLITECNICO_GRANCOLOMBIANO= new LatLng(4.636892,-74.055462);
    private List<User> usuarios= new ArrayList<>();
    private User usuarioLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        this.setUsuarioLogin((User) getIntent().getExtras().getSerializable("login"));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(POLITECNICO_GRANCOLOMBIANO).title("Eyy estamos en el poli en clase de Dispositivos Moviles!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(POLITECNICO_GRANCOLOMBIANO,17.0f));
        this.agregarUsuariosMapa();

    }

    public void agregarUsuariosMapa(){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
            apiService service = retrofit.create(apiService.class);
            Call<List<User>> call = service.getUsuarios();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    for(User us : response.body()) {
                        usuarios.add(us);
                        Log.d("Usuario", "User:" + us.getPrimerNombre()+ " " + us.getPrimerApellido());
                        LatLng pos_user = new LatLng(us.getLatitud(), us.getLonguitud());
                        mMap.addMarker(new MarkerOptions().position(pos_user).title("Hola soy yo: " + us.getPrimerNombre() + " " + us.getPrimerApellido()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_user, 10.05f));
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                }
            });

    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public User getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(User usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }
}
