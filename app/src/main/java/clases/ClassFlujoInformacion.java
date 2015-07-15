package clases;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 03/02/2015.
 */
public class ClassFlujoInformacion {
    private Context             context;
    private String              _campos[];
    private ContentValues       _tempRegistro;
    private ArrayList<ContentValues> _tempTabla;

    private static SQLite FcnSQL;

    private String currentDateandTime;
    ProgressDialog _pDialog;


    public ClassFlujoInformacion(Context _ctx){
        this.context        = _ctx;
        this._tempRegistro  = new ContentValues();
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentDateandTime = sdf.format(date);

    }


    public String getTrabajoCargdo(){
        String _retorno = "";
        this._tempTabla = this.FcnSQL.SelectData("maestro_rutas","id_ciclo||'-'||id_municipio||'-'||ruta AS trabajo" ,"id_inspector IS NOT NULL");
        for(int i=0; i<this._tempTabla.size();i++){
            _retorno +="'"+this._tempTabla.get(i).getAsString("trabajo")+"',";
        }
        if(!_retorno.isEmpty()){
            _retorno = _retorno.substring(0,_retorno.length()-1);
        }
        return _retorno;
    }

    public void EliminarParametros(){
        /**Eliminar los parametros previamente cargados**/
        this.FcnSQL.DeleteRegistro("param_usuarios","perfil<>0");
        this.FcnSQL.DeleteRegistro("param_municipios","id_municipio IS NOT NULL");
        this.FcnSQL.DeleteRegistro("param_anomalias","id_anomalia IS NOT NULL");
        this.FcnSQL.DeleteRegistro("param_critica","descripcion IS NOT NULL");
        this.FcnSQL.DeleteRegistro("param_codigos_mensajes","codigo IS NOT NULL");
    }


    public void CargarParametros(String _informacion, String _delimitador){
        /**Se inicia el registro de los nuevos parametros**/
        this._campos = _informacion.split(_delimitador);
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
            this._tempRegistro.put("mensaje",this._campos[4]);
            this.FcnSQL.InsertRegistro("param_critica",this._tempRegistro);
        }else if(this._campos[0].equals("Uso")){
            this._tempRegistro.put("id_uso",this._campos[1]);
            this._tempRegistro.put("descripcion",this._campos[2]);
            this.FcnSQL.InsertRegistro("param_tipos_uso",this._tempRegistro);
        }else if(this._campos[0].equals("Msj")){
            this._tempRegistro.put("codigo",this._campos[1]);
            this._tempRegistro.put("descripcion",this._campos[2]);
            this.FcnSQL.InsertRegistro("param_codigos_mensajes",this._tempRegistro);
        }
    }

    public void CargarTrabajo(String _informacion, String _delimitador, int secuencia){
        /**Se inicia el registro de la ruta programada**/
        this._campos = _informacion.split(_delimitador);
        this._tempRegistro.clear();
        if(this._campos[0].equals("MaestroRutas")){
            this.FcnSQL.DeleteRegistro("maestro_clientes","id_ciclo="+this._campos[2]+" AND ruta='"+this._campos[3]+"'");
            this._tempRegistro.put("id_inspector",this._campos[1]);
            this._tempRegistro.put("id_ciclo",this._campos[2]);
            this._tempRegistro.put("id_municipio",this._campos[3]);
            this._tempRegistro.put("ruta",this._campos[4]);
            this._tempRegistro.put("fecha_cargue",currentDateandTime);
            this.FcnSQL.InsertRegistro("maestro_rutas",this._tempRegistro);
        }else if(this._campos[0].equals("MaestroClientes")){
            //this._tempRegistro.put("id_serial",secuencia);
            this._tempRegistro.put("id_secuencia",this._campos[1]);
            this._tempRegistro.put("id_ciclo",this._campos[2]);
            this._tempRegistro.put("ruta",this._campos[3]);
            this._tempRegistro.put("cuenta",this._campos[4]);
            this._tempRegistro.put("marca_medidor",this._campos[5]);
            this._tempRegistro.put("serie_medidor",this._campos[6]);
            this._tempRegistro.put("digitos",this._campos[7]);
            this._tempRegistro.put("nombre",this._campos[8]);
            this._tempRegistro.put("direccion",this._campos[9]);
            this._tempRegistro.put("factor_multiplicacion",this._campos[10]);
            this._tempRegistro.put("tipo_uso",this._campos[11]);
            this._tempRegistro.put("id_serial_1",this._campos[12]);
            this._tempRegistro.put("lectura_anterior_1",this._campos[13]);
            this._tempRegistro.put("tipo_energia_1",this._campos[14]);
            this._tempRegistro.put("anomalia_anterior_1",this._campos[15]);
            this._tempRegistro.put("promedio_1",this._campos[16]);
            this._tempRegistro.put("id_serial_2",this._campos[17]);
            this._tempRegistro.put("lectura_anterior_2",this._campos[18]);
            this._tempRegistro.put("tipo_energia_2",this._campos[19]);
            this._tempRegistro.put("anomalia_anterior_2",this._campos[20]);
            this._tempRegistro.put("promedio_2",this._campos[21]);
            this._tempRegistro.put("id_serial_3",this._campos[22]);
            this._tempRegistro.put("lectura_anterior_3",this._campos[23]);
            this._tempRegistro.put("tipo_energia_3",this._campos[24]);
            this._tempRegistro.put("anomalia_anterior_3",this._campos[25]);
            this._tempRegistro.put("promedio_3",this._campos[26]);
            this._tempRegistro.put("id_municipio",this._campos[27]);
            this._tempRegistro.put("estado",this._campos[28]);
            this.FcnSQL.InsertRegistro("maestro_clientes",this._tempRegistro);
        }else if(this._campos[0].equals("MaestroRutasTerminadas")){
            this.FcnSQL.DeleteRegistro( "maestro_rutas",
                                        "id_inspector="+this._campos[1]+" AND id_ciclo="+this._campos[2]+" AND id_municipio="+this._campos[3]+" AND ruta='"+this._campos[4]+"'");

            this.FcnSQL.DeleteRegistro( "maestro_clientes",
                    "id_ciclo="+this._campos[2]+" AND id_municipio="+this._campos[3]+" AND ruta='"+this._campos[4]+"'");
        }
    }
}
