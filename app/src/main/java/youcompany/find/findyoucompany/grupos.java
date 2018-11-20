package youcompany.find.findyoucompany;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bo.Grupo;
import bo.User;
import interfaces.apiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class grupos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        this.setUsuarioLogin((User)getIntent().getExtras().get("login"));
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        list = (ListView) findViewById(R.id.listado_grupos);
        list.setAdapter(arrayAdapter);
        this.getGrupos();
    }

    User usuarioLogin;
    ListView list;
    ArrayList<String> titles = new ArrayList<>();
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

    private void getGrupos() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://cunlmsprueba.catedra.edu.co:8090").addConverterFactory(GsonConverterFactory.create()).build();
        apiService service = retrofit.create(apiService.class);
        Call<List<Grupo>> call = service.getGrupos();

        call.enqueue(new Callback<List<Grupo>>() {
            @Override
            public void onResponse(Call<List<Grupo>> call, Response<List<Grupo>> response) {
                for(Grupo gr : response.body()) {
                    titles.add("Cod: "+gr.getCodigo()+"\nGrupo: "+gr.getNombre());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Grupo>> call, Throwable t) {
            }
        });
    }
}
