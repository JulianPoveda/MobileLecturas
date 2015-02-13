package async_task;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.IOException;
import java.io.File;

import clases.ClassAnomalia;
import clases.ClassConfiguracion;
import clases.ClassTomarLectura;
import clases.ClassUsuario;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 12/02/2015.
 */
public class UploadLecturas extends AsyncTask<String, Void, Integer> {

    private ClassConfiguracion  FcnCfg;
    private Archivos            FcnArch;
    private SQLite              FcnSQL;
    private ClassUsuario        Usuario;
    private ClassAnomalia       FcnTL;

    private Context Context;

    private String URL;
    private String NAMESPACE;
    private String Respuesta   = "";
    private String carpeta      ="Descarga";

    private String listado[];
    private ContentValues				_tempRegistro 	    = new ContentValues();
    private ContentValues				_tempRegistro1 	    = new ContentValues();
    private ContentValues			    InformacionArchivos = new ContentValues();
    private ArrayList<ContentValues>	_tempTabla	    	= new ArrayList<ContentValues>();
    private ArrayList<ContentValues>	_tempTabla1		    = new ArrayList<ContentValues>();
    private ArrayList<ContentValues>    RegistroArchivos 	= new ArrayList<ContentValues>();
    private ArrayList<String> 	        InformacionCarga    = new ArrayList<String>();
    private File[]				        ListaArchivos;

    private static final String METHOD_NAME	= "UploadTrabajo";
    private static final String SOAP_ACTION	= "UploadTrabajo";

    SoapPrimitive response = null;
    ProgressDialog _pDialog;


    public UploadLecturas(Context context){
        this.Context    = context;
        this.FcnCfg     = ClassConfiguracion.getInstance(this.Context);
        this.FcnSQL     = new SQLite(this.Context, FormInicioSession.path_files_app);
        this.FcnArch	= new Archivos(this.Context, FormInicioSession.path_files_app, 10);
        this.Usuario    = ClassUsuario.getInstance(this.Context);
        this.FcnTL      = new ClassAnomalia(Context);
    }

    protected void onPreExecute() {
        this.URL        = this.FcnCfg.getIp_server() + ":" + this.FcnCfg.getPort() + "/" + this.FcnCfg.getModule_web_service() + "/" + this.FcnCfg.getWeb_service();
        this.NAMESPACE  = this.FcnCfg.getIp_server() + ":" + this.FcnCfg.getPort() + "/" + this.FcnCfg.getModule_web_service();
    }

    @Override
    protected Integer doInBackground(String... params) {
        int _retorno    = 0;
        String listSgd  = "";
        this.InformacionCarga.clear();

        this._tempTabla	= this.FcnSQL.SelectData("maestro_clientes", "id_serial_1, id_serial_2, id_serial_3","estado='T'");
        for(int i=0; i<this._tempTabla.size();i++){
            this._tempRegistro  = this._tempTabla.get(i);
            this._tempTabla1	= this.FcnSQL.SelectData("toma_lectura",
                                                             "id,id_serial1, lectura1, critica1, id_serial2, lectura2, critica2, id_serial3, lectura3, critica3, anomalia,mensaje, tipo_uso,fecha_toma",
                                                             "id_serial1="+this._tempRegistro.getAsString("id_serial_1")+" AND id_serial2="+this._tempRegistro.getAsString("id_serial_2")+" and id_serial3="+this._tempRegistro.getAsString("id_serial_3")+"");

            for(int j=0; j<this._tempTabla1.size();j++){
                this._tempRegistro1 = this._tempTabla1.get(j);
                this.InformacionCarga.add(this._tempRegistro1.getAsString("id")+","+this._tempRegistro1.getAsString("id_serial1") + "," + this._tempRegistro1.getAsString("lectura1") + "," + this._tempRegistro1.getAsString("critica1") + "," +
                        "" + this._tempRegistro1.getAsString("id_serial2") + "," + this._tempRegistro1.getAsString("lectura2") + "," + this._tempRegistro1.getAsString("critica2") + "," +
                        "" + this._tempRegistro1.getAsString("id_serial3") + "," + this._tempRegistro1.getAsString("lectura3") + "," + this._tempRegistro1.getAsString("critica3") + "," +
                        "" + this._tempRegistro1.getAsString("anomalia") + "," + this._tempRegistro1.getAsString("mensaje") + "," + this._tempRegistro1.getAsString("tipo_uso") + "," +
                        "" + this._tempRegistro1.getAsString("fecha_toma") + "\r\n");
            }
        }

        for (String s : InformacionCarga){
            listSgd += s;
        }

        FcnArch.DoFile(this.carpeta,"registros",listSgd);

        File carpeta        = new File(FormInicioSession.path_files_app+File.separator+this.carpeta);
        this.ListaArchivos  = new File(carpeta.toString()).listFiles();
        if(this.ListaArchivos.length == 0){
            FcnArch.DeleteFile(carpeta.toString());
        }else{
            this.InformacionArchivos.clear();
            this.InformacionArchivos.put("usuario", this.Usuario.getCodigo());
            for(int j=0;j<this.ListaArchivos.length;j++){
                this.InformacionArchivos.put("ruta",this.ListaArchivos[j].toString());
                this.RegistroArchivos.add(this.InformacionArchivos);
            }
        }

        for(int i=0; i<this.RegistroArchivos.size();i++){
            this.InformacionArchivos = this.RegistroArchivos.get(i);
            try{
                SoapObject so=new SoapObject(NAMESPACE, this.METHOD_NAME);
                so.addProperty("usuario", this.InformacionArchivos.getAsString("usuario"));
                so.addProperty("informacion",this.FcnArch.FileToArrayBytes(this.InformacionArchivos.getAsString("ruta")));

                SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                new MarshalBase64().register(sse);
                sse.dotNet=true;
                sse.setOutputSoapObject(so);
                HttpTransportSE htse=new HttpTransportSE(URL);
                htse.call(SOAP_ACTION, sse);
                response=(SoapPrimitive) sse.getResponse();

                if(response==null) {
                    this.Respuesta = "-1";
                }else if(response.toString().isEmpty()){
                    this.Respuesta = "-2";
                }else if(response.toString().equals("1")){
                    this.Respuesta = "1";
                    _retorno=1;
                   finalizarLectura();
                }
            } catch (Exception e) {
                this.Respuesta = e.toString();
            }
        }
        return _retorno;
    }

    @Override
    protected void onPostExecute(Integer rta) {
       // _pDialog.dismiss();
    }

        //Se debe cambiar la funcion para enviarle el array recibido y utilizar el trigger de sqlite. no olvidar revisar al 3 envio se cae el webservice
    public void finalizarLectura(){
            for (int i=0;i<this.InformacionCarga.size();i++){
                String _leidas  = this.InformacionCarga.get(i);
                this.listado    = _leidas.split(",");
                this.FcnTL.finalizarTomaLectura(this.listado[0]);
            }
    }

}




