package interfaces;

import java.util.List;

import bo.Ciudad;
import bo.Departamento;
import bo.Empresa;
import bo.Grupo;
import bo.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface apiService {

    String api_route_usuarios = "/fyc/usuarios";
    String api_route_empresas = "/fyc/empresas";
    String api_route_ciudades = "/fyc/ciudades";
    String api_route_grupos = "/fyc/grupos";
    String api_route_departmentos = "/fyc/departamentos";
    String api_route_user_username = "/fyc/loadUsuario/{username}";
    String api_route_user_id = "/fyc/loadUsuario/{id}";

    @GET(api_route_usuarios)
    Call<List<User>> getUsuarios();

    @GET(api_route_user_username)
    Call<User> getUser(@Path("username") String username);

    @GET(api_route_user_id)
    Call<User> getUserById(@Path("id") Integer id);

    @GET(api_route_grupos)
    Call<List<Grupo>> getGrupos();

    @GET(api_route_empresas)
    Call<List<Empresa>> getEmpresas();

    @GET(api_route_ciudades)
    Call<List<Ciudad>> getCiudades();

    @GET(api_route_departmentos)
    Call<List<Departamento>> getDepartamentos();

}
