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
    private ArrayList<ContentValues> _tempTabla;

    private SQLite FcnSQL;

    private String anomalia;
    private ArrayList<String> listadoAnomalias;
    private ArrayList<String> listarDatos;

    public ClassAnomalia(Context _ctx){
        this.context     = _ctx;
        this.FcnSQL      = new SQLite(this.context, FormInicioSession.path_files_app);
        this.listadoAnomalias = new ArrayList<String>();
        this._tempTabla       = new ArrayList<ContentValues>();
        this._tempRegistro    = new ContentValues();
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
        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            listadoAnomalias.add(this._tempRegistro.getAsString("id_anomalia")+ this._tempRegistro.getAsString("descripcion"));
        }

        return listadoAnomalias;
    }

    public ContentValues validarDatosAnomalia(String _anomalia){
        this._tempRegistro.clear();
        this.anomalia = _anomalia;
        this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                "lectura, mensaje, foto",
                                                                "id_anomalia='"+this.anomalia+"'");

        return _tempRegistro;
    }
}
