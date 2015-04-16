package async_task;

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
import android.content.Intent;
import android.os.AsyncTask;

import java.io.File;

import clases.ClassAnomaliaOld;
import clases.ClassConfiguracion;
import clases.ClassSession;
import lecturas.sypelc.mobilelecturas.FormInformacionRutas;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 12/02/2015.
 */
public class UpLoadFoto extends AsyncTask<String, Void, Integer> {
    private Intent              new_form;
    private ClassConfiguracion  FcnCfg;
    private Archivos            FcnArch;
    private SQLite              FcnSQL;
    private ClassSession        Usuario;

    private Context Context;

    private String URL;
    private String NAMESPACE;
    private String Respuesta   = "";

    private ContentValues				_tempRegistro 	    = new ContentValues();
    private static final String METHOD_NAME	= "FotoTomada";
    private static final String SOAP_ACTION	= "FotoTomada";

    SoapPrimitive response = null;
    ProgressDialog _pDialog;


    public UpLoadFoto(Context context){
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

        try{
            SoapObject so=new SoapObject(NAMESPACE, this.METHOD_NAME);
            so.addProperty("cuenta", params[0]);
            so.addProperty("id_memsa", params[1]);
            so.addProperty("usuario", this.Usuario.getCodigo());
            so.addProperty("informacion",this.FcnArch.FileToArrayBytesOne(params[2]));

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
            }else {
                try {
                    String informacion[] = new String(response.toString()).trim().split("\\|");
                    if(informacion.length>0){
                        this._tempRegistro.clear();
                        for(int i=0;i<informacion.length;i++){
                           // this.FcnSQL.DeleteRegistro("toma_lectura","id="+informacion[i]+"");
                        }
                    }
                    _retorno = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    _retorno = -3;
                }
            }
        } catch (Exception e) {
            this.Respuesta = e.toString();
            _retorno = -4;
        }
        return _retorno;
    }

    @Override
    protected void onPostExecute(Integer rta) {

    }
}




