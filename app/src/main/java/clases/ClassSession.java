package clases;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by JULIANEDUARDO on 02/02/2015.
 */
public class ClassSession {
    /**
     * Variables generales de la clase
     */
    private static Context              context;
    private SQLite                      FcnSQL;
    private static ClassSession ourInstance;
    private ArrayList<ContentValues>    _tempTabla;
    private ContentValues               _tempRegistro;

    /**
     * Variables para los atributos del usuario
     */
    private static boolean  inicio_sesion;
    private static String   codigo;
    private static int      nivel;
    private static String   nombre;

    public static ClassSession getInstance(Context _ctx) {
        if(ourInstance == null){
            ourInstance = new ClassSession(_ctx);
        }else{
           context = _ctx;
        }
        return ourInstance;
    }


    private ClassSession(Context _ctx) {
        this.inicio_sesion  = false;
        this.context        = _ctx;
        this.codigo         = null;
        this.nivel          = -1;
        this.nombre         = "Sin Iniciar Session";
        this._tempRegistro  = new ContentValues();
        this._tempTabla     = new ArrayList<ContentValues>();
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
    }


    public boolean IniciarSession(String _codigo){
        if(!this.FcnSQL.ExistRegistros("param_usuarios","id_inspector='"+_codigo+"'") || _codigo == null){
            this.inicio_sesion  = false;
            this.codigo         = null;
            this.nivel          = -1;
            this.nombre         = "Sin Iniciar Session";
        }else{
            this._tempRegistro   =  this.FcnSQL.SelectDataRegistro( "param_usuarios",
                                                                    "id_inspector,nombre,perfil",
                                                                    "id_inspector='"+_codigo+"'");

            this.setCodigo(this._tempRegistro.getAsString("id_inspector"));
            this.setNombre(this._tempRegistro.getAsString("nombre"));
            this.setNivel(this._tempRegistro.getAsInteger("perfil"));
            this.setInicio_sesion(true);
        }
        return this.inicio_sesion;
    }





    public static boolean isInicio_sesion() {
        return inicio_sesion;
    }

    private static void setInicio_sesion(boolean inicio_sesion) {
        ClassSession.inicio_sesion = inicio_sesion;
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    private void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getNivel() {
        return nivel;
    }

    private void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
