package clases;

import android.content.ContentValues;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 06/02/2015.
 */
public class ClassAnomalia {
    private Context context;
    private ContentValues _tempRegistro;

    private SQLite FcnSQL;

    private String anomalia;
    private ArrayList<String> listadoAnomalias;
    private ArrayList<String> listarDatos;

    public ClassAnomalia(Context _ctx){
        this.context = _ctx;
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
        listadoAnomalias = new ArrayList<String>();
    }

    public ArrayList<String> listarAnomalias(String _tipo_uso) {
        this._tempRegistro.clear();
        if(_tipo_uso.equals("RS")){
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                    "id_anomalia, descripcion",
                                                                    "aplica_residencial='t'");
        }
        else{
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                    "id_anomalia, descripcion",
                                                                    "aplica_no_residencial='t'");
        }
        listadoAnomalias.add(this._tempRegistro.getAsString("id_anomalia"));
        listadoAnomalias.add(this._tempRegistro.getAsString("descripcion"));
        return listadoAnomalias;
    }

    public ArrayList<String> validarDatosAnomalia(String _anomalia){
        this._tempRegistro.clear();
        this.anomalia = _anomalia;
        this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                "lectura, mensaje, foto",
                                                                "id_anomalia='"+this.anomalia+"'");
        listarDatos.add(this._tempRegistro.getAsString("lectura"));
        listarDatos.add(this._tempRegistro.getAsString("mensaje"));
        listarDatos.add(this._tempRegistro.getAsString("foto"));
        return listarDatos;
    }
}
