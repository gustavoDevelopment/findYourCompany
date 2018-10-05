package bo;

import java.io.Serializable;

public class Usuario implements Serializable {

    public static String PROP_ID="id";
    public static String PROP_EMAIL="email";
    public static String PROP_NOMBRE="nombre";
    public static String PROP_APELLIDO="apellido";
    public static String PROP_CLAVE="clave";
    public static String PROP_LATITUD="latitud";
    public static String PROP_LONGUITUD="longitud";


    private String id;
    private String email;
    private String nombre;
    private String apellido;
    private String clave;
    private String latitud;
    private String longitud;

    public Usuario() {
    }

    public Usuario(String id, String email, String nombre, String apellido) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Usuario(String id, String email, String nombre, String apellido, String clave, String latitud, String longitud) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.clave = clave;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}