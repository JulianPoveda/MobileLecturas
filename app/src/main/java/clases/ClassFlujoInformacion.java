package clases;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.InicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 03/02/2015.
 */
public class ClassFlujoInformacion {
    private Context         context;
    private String          _campos[];
    private ContentValues   _tempRegistro;

    private static SQLite FcnSQL;


    public ClassFlujoInformacion(Context _ctx){
        this.context        = _ctx;
        this._tempRegistro  = new ContentValues();
        this.FcnSQL         = new SQLite(this.context, InicioSession.path_files_app);

    }

    public void CargarParametros(ArrayList<String> _informacion, String _delimitador){
        /**Eliminar los parametros previamente cargados**/
        this.FcnSQL.DeleteRegistro("param_usuarios","perfil<>0");
        this.FcnSQL.DeleteRegistro("param_municipios","id_municipio IS NOT NULL");
        this.FcnSQL.DeleteRegistro("param_anomalias","id_anomalia IS NOT NULL");
        this.FcnSQL.DeleteRegistro("param_critica","descripcion IS NOT NULL");

        /**Se inicia el registro de los nuevos parametros**/
        for(int i=0;i<_informacion.size();i++){
            this._campos = _informacion.get(i).split(_delimitador);
            this._tempRegistro.clear();
            if(this._campos[0].equals("Inspector")){
                this._tempRegistro.put("id_inspector",this._campos[1]);
                this._tempRegistro.put("nombre",this._campos[2]);
                this._tempRegistro.put("perfil",1);
                this.FcnSQL.InsertRegistro("param_usuarios",this._tempRegistro);
            }else if(this._campos[0].equals("Municipio")){
                this._tempRegistro.put("id_municipio",this._campos[1]);
                this._tempRegistro.put("municipio",this._campos[2]);
                this.FcnSQL.InsertRegistro("param_municipios",this._tempRegistro);
            }else if(this._campos[0].equals("Anomalia")){
                this._tempRegistro.put("id_anomalia",this._campos[1]);
                this._tempRegistro.put("descripcion",this._campos[2]);
                this._tempRegistro.put("aplica_residencial",this._campos[3]);
                this._tempRegistro.put("aplica_no_residencial",this._campos[4]);
                this._tempRegistro.put("lectura",this._campos[5]);
                this._tempRegistro.put("mensaje",this._campos[6]);
                this._tempRegistro.put("foto",this._campos[7]);
                this.FcnSQL.InsertRegistro("param_anomalias",this._tempRegistro);
            }else if(this._campos[0].equals("Critica")){
                this._tempRegistro.put("minimo",this._campos[1]);
                this._tempRegistro.put("maximo",this._campos[2]);
                this._tempRegistro.put("descripcion",this._campos[3]);
                this.FcnSQL.InsertRegistro("param_critica",this._tempRegistro);
            }
        }
    }
}
