package async_task;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;

//import clases.ClassAnomaliaOld;
import clases.ClassConfiguracion;
import clases.ClassSession;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import sistema.Bluetooth;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 12/02/2015.
 */
public class UploadLecturas extends AsyncTask<String, Void, Integer> {
    private Intent              new_form;
    private ClassConfiguracion  FcnCfg;
    private Archivos            FcnArch;
    private SQLite              FcnSQL;
    private ClassSession        Usuario;
    private Bluetooth           FcnBluetooth = Bluetooth.getInstance();

    private Context Context;

    private String URL;
    private String NAMESPACE;
    private String Respuesta   = "";
    private String infRuta[];

    private ContentValues				_tempRegistro 	    = new ContentValues();
    private ContentValues				_tempRegistro1 	    = new ContentValues();
    private ArrayList<ContentValues>	_tempTabla	    	= new ArrayList<ContentValues>();
    private ArrayList<ContentValues>	_tempTabla1		    = new ArrayList<ContentValues>();
    private String                      InformacionCarga;

    private static final String METHOD_NAME	= "UploadTrabajoExplicitInspector";
    private static final String SOAP_ACTION	= "UploadTrabajoExplicitInspector";

    private SoapObject so;
    private SoapSerializationEnvelope sse;
    private HttpTransportSE htse;

    SoapPrimitive response = null;
    ProgressDialog _pDialog;


    public UploadLecturas(Context context){
        this.Context    = context;
        this.FcnCfg     = ClassConfiguracion.getInstance(this.Context);
        this.FcnSQL     = new SQLite(this.Context, FormInicioSession.path_files_app);
        this.FcnArch	= new Archivos(this.Context, FormInicioSession.path_files_app, 10);
        this.Usuario    = ClassSession.getInstance(this.Context);
    }

    protected void onPreExecute() {
        this.URL        = this.FcnCfg.getIp_server() + ":" + this.FcnCfg.getPort() + "/" + this.FcnCfg.getModule_web_service() + "/" + this.FcnCfg.getWeb_service();
        this.NAMESPACE  = this.FcnCfg.getIp_server() + ":" + this.FcnCfg.getPort() + "/" + this.FcnCfg.getModule_web_service();
    }

    @Override
    protected Integer doInBackground(String... params) {
        int _retorno    = 0;
        this.InformacionCarga = "";


        //Se evalua el parametro recibido, si esta vacio es poque el llamado se realizo desde la clase beacon
        //la cual realiza la sincronizacion cada X tiempo, en caso de no estar vacia es porque fue llamado desde
        //el formulario toma lectura.
        if(params[0].isEmpty()){
            this._tempTabla = this.FcnSQL.SelectData("maestro_clientes", "id_serial_1, id_serial_2, id_serial_3", "estado='T'");
        }else {
            this.infRuta = params[0].split("\\-");
            this._tempTabla = this.FcnSQL.SelectData("maestro_clientes", "id_serial_1, id_serial_2, id_serial_3", "estado='T' AND ruta='" + infRuta[2] + "' AND id_municipio = "+infRuta[1]);
        }

        if(this._tempTabla.size()>0){
            for(int i=0; i<this._tempTabla.size();i++){
                this._tempRegistro  = this._tempTabla.get(i);
                this._tempTabla1	= this.FcnSQL.SelectData(   "toma_lectura",
                        "id, id_serial1, lectura1, critica1, id_serial2, lectura2, critica2, id_serial3, lectura3, critica3, anomalia, mensaje, tipo_uso,fecha_toma,longitud,latitud,id_inspector",
                        "id_serial1="+this._tempRegistro.getAsString("id_serial_1")+" AND id_serial2="+this._tempRegistro.getAsString("id_serial_2")+" and id_serial3="+this._tempRegistro.getAsString("id_serial_3")+"");

                for(int j=0; j<this._tempTabla1.size();j++){
                    this._tempRegistro1 = this._tempTabla1.get(j);
                    this.InformacionCarga += this._tempRegistro1.getAsString("id")+","+this._tempRegistro1.getAsString("id_serial1") + "," + this._tempRegistro1.getAsString("lectura1") + "," + this._tempRegistro1.getAsString("critica1") + "," +
                            "" + this._tempRegistro1.getAsString("id_serial2") + "," + this._tempRegistro1.getAsString("lectura2") + "," + this._tempRegistro1.getAsString("critica2") + "," +
                            "" + this._tempRegistro1.getAsString("id_serial3") + "," + this._tempRegistro1.getAsString("lectura3") + "," + this._tempRegistro1.getAsString("critica3") + "," +
                            "" + this._tempRegistro1.getAsString("anomalia") + "," + this._tempRegistro1.getAsString("mensaje").replace("\n",".") + "," + this._tempRegistro1.getAsString("tipo_uso") + "," +
                            "" + this._tempRegistro1.getAsString("fecha_toma") + "," +this._tempRegistro1.getAsString("longitud")+","+this._tempRegistro1.getAsString("latitud")+ "," +
                            "" + this._tempRegistro1.getAsString("id_inspector")+ "\r\n";
                }
            }

            //this.FcnArch.DoFile("Descarga",this.Usuario.getCodigo()+"_"+params[0]+".txt",this.InformacionCarga);

            try{
                this.so=new SoapObject(NAMESPACE, this.METHOD_NAME);
                //this.so.addProperty("informacion",this.FcnArch.FileToArrayBytes("Descarga", this.Usuario.getCodigo() + "_" + params[0] + ".txt", true));
                this.so.addProperty("Informacion",this.InformacionCarga);
                this.so.addProperty("Bluetooth", this.FcnBluetooth.GetOurDeviceByAddress());

                this.sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                //new MarshalBase64().register(this.sse);
                this.sse.dotNet=true;
                this.sse.setOutputSoapObject(this.so);
                this.htse=new HttpTransportSE(URL,45000);
                this.htse.call(SOAP_ACTION, this.sse);
                this.response=(SoapPrimitive) this.sse.getResponse();

                if(response==null) {
                    this.Respuesta = "-1";
                }else if(response.toString().isEmpty()){
                    this.Respuesta = "-2";
                }else {
                    String informacion[] = new String(response.toString()).trim().split("\\|");
                    if(informacion.length>0){
                        this._tempRegistro1.clear();
                        this._tempRegistro1.put("estado","E");

                        for(int i=0;i<informacion.length;i++){
                            //Con el id local se consulta los id_seriales originales para poder actualizar el registro general
                            this._tempRegistro = this.FcnSQL.SelectDataRegistro("toma_lectura","id_serial1,id_serial2,id_serial3","id="+informacion[i]);
                            //Se hace el cambio de estado de (T)erminado a (E)nviado
                            this.FcnSQL.UpdateRegistro("maestro_clientes",
                                    this._tempRegistro1,
                                    "id_serial_1="+this._tempRegistro.getAsString("id_serial1")+" AND id_serial_2="+this._tempRegistro.getAsString("id_serial2")+" AND id_serial_3="+this._tempRegistro.getAsString("id_serial3"));
                        }
                    }
                    _retorno = 1;
                }
            } catch (Exception e) {
                this.Respuesta = e.toString();
                _retorno = -4;
            }finally{
                if(this.htse != null){
                    this.htse.reset();
                    try {
                        this.htse.getServiceConnection().disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                        _retorno = -5;
                    }
                }
            }
        }else{
            _retorno = 0;
        }

        return _retorno;
    }

    @Override
    protected void onPostExecute(Integer rta) {

    }
}




