package interfaces;

import java.util.List;

import bo.Ciudad;
import bo.Departamento;
import bo.Empresa;
import bo.Grupo;
import bo.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface apiService {

    String api_usuarios = "/fyc/usuarios";
    String api_empresas = "/fyc/empresas";
    String api_ciudades = "/fyc/ciudades";
    String api_grupos = "/fyc/grupos";
    String api_departmentos = "/fyc/departamentos";
    String api_user_username = "/fyc/loadUsuario/{username}";
    String api_user_id = "/fyc/loadUsuario/{id}";
    String api_user_login = "/fyc/login";
    String api_add_user = "/fyc/addUsuario";
    String api_update_user = "/fyc/upUsuario";
    String api__delete_user = "/fyc/delUsuario/{id}";

    @GET(api_usuarios)
    Call<List<User>> getUsuarios();

    @GET(api_user_username)
    Call<User> getUser(@Path("username") String username);

    @GET(api_user_id)
    Call<User> getUserById(@Path("id") Integer id);

    @GET(api_grupos)
    Call<List<Grupo>> getGrupos();

    @GET(api_empresas)
    Call<List<Empresa>> getEmpresas();

    @GET(api_ciudades)
    Call<List<Ciudad>> getCiudades();

    @GET(api_departmentos)
    Call<List<Departamento>> getDepartamentos();

    @FormUrlEncoded
    @POST(api_user_login)
    Call<User> login(@Field("username") String username,@Field("pass") String pass);


    @POST(api_add_user)
    Call<User> agregarUsuario(@Body User user);

    @PUT(api_update_user)
    Call<User> actualizarUsuario(@Body User user);




}
