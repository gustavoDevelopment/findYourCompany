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

import java.util.HashMap;
import java.util.List;

import bo.Usuario;

public class mapas extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng POLITECNICO_GRANCOLOMBIANO= new LatLng(4.636892,-74.055462);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    private static HashMap<String, Usuario> usuarios = new HashMap<String,Usuario>();
    private Usuario logeado;


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
        this.setLogeado((Usuario) getIntent().getExtras().getSerializable("logeado"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                for (DataSnapshot dt : data) {
                    Usuario loaded = dt.getValue(Usuario.class);
                    if(!usuarios.containsKey(dt.child(Usuario.PROP_CLAVE).getValue(String.class))) {
                        usuarios.put(dt.child(Usuario.PROP_CLAVE).getValue(String.class), loaded);
                    }
                    Log.d("USUARIOS", "DATA--->" + dt.child(Usuario.PROP_CLAVE) + "| " + dt.child(Usuario.PROP_NOMBRE) + " " + dt.child(Usuario.PROP_APELLIDO));

                }
                for(Usuario user:usuarios.values()){
                    Log.d("Usuario","User:"+user.getNombre()+" "+user.getApellido());
                    LatLng pos_user= new LatLng(Double.parseDouble(user.getLatitud()),Double.parseDouble(user.getLongitud()));
                    mMap.addMarker(new MarkerOptions().position(pos_user).title("Hola soy yo: "+user.getNombre()+" "+user.getApellido()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_user,15.05f));
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
        for(Usuario user:usuarios.values()){
            if(!this.logeado.getClave().equals(user.getClave())) {
                Log.d("Usuario", "User:" + user.getNombre() + " " + user.getApellido());
                LatLng pos_user = new LatLng(Double.parseDouble(user.getLatitud()), Double.parseDouble(user.getLongitud()));
                mMap.addMarker(new MarkerOptions().position(pos_user).title("Hola soy yo: " + user.getNombre() + " " + user.getApellido()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_user, 10.05f));
            }
        }
    }

    public Usuario getLogeado() {
        return logeado;
    }

    public void setLogeado(Usuario logeado) {
        this.logeado = logeado;
    }
}
