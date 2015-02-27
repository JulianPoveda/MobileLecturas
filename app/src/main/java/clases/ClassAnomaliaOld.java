package clases;

import android.content.ContentValues;
import android.content.Context;

import org.kobjects.util.Strings;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 06/02/2015.
 */
public class ClassAnomaliaOld {
    private Context context;
    private ContentValues _tempRegistro;
    private ArrayList<ContentValues> _tempTabla;

    private SQLite FcnSQL;

    private String anomalia[];
    private ArrayList<String> listadoAnomalias, listadoTiposUso;
    private ArrayList<String> listarDatos;

    public ClassAnomaliaOld(Context _ctx){
        this.context     = _ctx;
        this.FcnSQL      = new SQLite(this.context, FormInicioSession.path_files_app);
        this.listadoAnomalias = new ArrayList<String>();
        this.listadoTiposUso  = new ArrayList<String>();
        this._tempTabla       = new ArrayList<ContentValues>();
        this._tempRegistro    = new ContentValues();
    }

    public ArrayList<String> listarAnomalias(String _tipo_uso) {
        this._tempRegistro.clear();
        if(_tipo_uso.equals("RS")){
            this._tempTabla =    this.FcnSQL.SelectData("param_anomalias",
                                                        "id_anomalia, descripcion",
                                                        "aplica_residencial='t'");
        }else{
            this._tempTabla =    this.FcnSQL.SelectData("param_anomalias",
                                                        "id_anomalia, descripcion",
                                                        "aplica_no_residencial='t'");
        }

        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            listadoAnomalias.add(this._tempRegistro.getAsString("id_anomalia")+"-"+this._tempRegistro.getAsString("descripcion"));
        }

        return listadoAnomalias;
    }

    public ArrayList<String> listarTiposUso(){
        this._tempRegistro.clear();
        this._tempTabla.clear();

        this._tempTabla = this.FcnSQL.SelectData("param_tipos_uso","id_uso, descripcion","id_uso is not null");

        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            listadoTiposUso.add(this._tempRegistro.getAsString("id_uso")+"-"+this._tempRegistro.getAsString("descripcion"));
        }

        return listadoTiposUso;
    }

    public ContentValues validarDatosAnomalia(String _anomalia){
        this._tempRegistro.clear();
        this.anomalia = _anomalia.split("-");
        if(this.anomalia.length > 1){
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "param_anomalias",
                                                                    "id_anomalia, lectura, mensaje, foto",
                                                                    "id_anomalia='"+this.anomalia[0]+"'");
        }else{
            this._tempRegistro.put("id_anomalia",-1);
            this._tempRegistro.put("lectura","f");
            this._tempRegistro.put("mensaje","f");
            this._tempRegistro.put("foto","f");
        }
        return _tempRegistro;
    }

    public void finalizarTomaLectura(String[] descargado){
        this._tempRegistro.clear();
        for(int i=0;i<descargado.length;i++){
            this.FcnSQL.DeleteRegistro("toma_lectura","id="+descargado[i]+"");
        }
    }

}
