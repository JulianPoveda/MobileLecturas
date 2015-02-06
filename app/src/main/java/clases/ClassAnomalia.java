package clases;

import android.content.ContentValues;
import android.content.Context;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 06/02/2015.
 */
public class ClassAnomalia {
    private Context context;
    private ContentValues _tempRegistro;

    private SQLite FcnSQL;

    private String tipo_uso;
    private String anomalia;

    public ClassAnomalia(Context _ctx){
        this.context = _ctx;
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
    }

    public ContentValues listarAnomalias(String _tipo_uso) {
        this._tempRegistro.clear();
        this.tipo_uso = _tipo_uso;
        if(this.tipo_uso == "Residencial"){
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                    "id_anomalia, descripcion",
                                                                    "aplica_residencial='t'");
        }
        else{
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                    "id_anomalia, descripcion",
                                                                    "aplica_no_residencial='t'");
        }
        return _tempRegistro;
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
