package youcompany.find.findyoucompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bo.User;
import interfaces.apiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constantes;

public class usuarios extends AppCompatActivity {
    User usuarioLogin;
    ListView list;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Integer> idUsuer = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    public User getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(User usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public ListView getList() {
        return list;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayAdapter getArrayAdapter() {
        return arrayAdapter;
    }

    public void setArrayAdapter(ArrayAdapter arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        this.setUsuarioLogin((User)getIntent().getExtras().get("login"));
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        list = (ListView) findViewById(R.id.lista_usuarios);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inte = new Intent(view.getContext(), registro.class);
                inte.putExtra("login",getUsuarioLogin());
                inte.putExtra("idEdit",idUsuer.get(position));
                startActivity(inte);

            }
        });
        this.getUsuarios();


    }

    private void getUsuarios() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.SERVICE_REST).addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<List<User>> call = service.getUsuarios();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                for(User us : response.body()) {
                    titles.add("N° Documento: "+us.getDocumento()+"\nNombres: "+us.getPrimerNombre()+"\nApellidos: "+us.getPrimerApellido());
                    idUsuer.add(us.getId());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }

    public void toAddUser(View view){
        Intent inte = new Intent(view.getContext(), registro.class);
        inte.putExtra("login",getUsuarioLogin());
        startActivity(inte);
    }
}
