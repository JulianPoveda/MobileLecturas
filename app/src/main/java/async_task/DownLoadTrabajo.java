package async_task;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

import clases.ClassConfiguracion;
import clases.ClassFlujoInformacion;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import android.os.AsyncTask;

/**
 * Created by SypelcDesarrollo on 03/02/2015.
 */
public class DownLoadTrabajo extends AsyncTask<String, Integer, Integer>{ //doInBakGround, Progress, onPostExecute

    /**Instancias a clases**/
    private Archivos FcnArch;
    private ClassConfiguracion FcnCfg;
    private ClassFlujoInformacion FcnInformacion;

    /**Variables Locales**/
    private Context ConnectServerContext;

    private String URL;
    private String NAMESPACE;

    private SoapObject so;
    private SoapSerializationEnvelope sse;
    private HttpTransportSE htse;

    //Variables con la informacion del web service
    private static final String METHOD_NAME	= "DownLoadTrabajo";
    private static final String SOAP_ACTION	= "DownLoadTrabajo";
    SoapPrimitive response = null;
    ProgressDialog _pDialog;

    //Contructor de la clase
    public DownLoadTrabajo(Context context){
        this.ConnectServerContext 		= context;
        this.FcnCfg						= ClassConfiguracion.getInstance(this.ConnectServerContext);
        this.FcnInformacion             = new ClassFlujoInformacion(this.ConnectServerContext);
        this.FcnArch					= new Archivos(this.ConnectServerContext, FormInicioSession.path_files_app, 10);
    }


    //Operaciones antes de realizar la conexion con el servidor
    protected void onPreExecute(){
        this.URL 			= this.FcnCfg.getIp_server()+":"+this.FcnCfg.getPort()+"/"+this.FcnCfg.getModule_web_service()+"/"+this.FcnCfg.getWeb_service();
        this.NAMESPACE 		= this.FcnCfg.getIp_server()+":"+this.FcnCfg.getPort()+"/"+this.FcnCfg.getModule_web_service();

        Toast.makeText(this.ConnectServerContext,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();
        _pDialog = new ProgressDialog(this.ConnectServerContext);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(100);
        _pDialog.show();
    }

    //Conexion con el servidor en busca de actualizaciones de trabajo programado
    @Override
    protected Integer doInBackground(String... params) {
        int _retorno = 0;
        try{
            this.so=new SoapObject(NAMESPACE, METHOD_NAME);
            this.so.addProperty("id_interno", params[0]);
            this.so.addProperty("rutas_cargadas", params[1]);
            this.sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            this.sse.dotNet=true;
            this.sse.setOutputSoapObject(this.so);
            this.htse=new HttpTransportSE(URL);
            this.htse.call(SOAP_ACTION, this.sse);
            this.response=(SoapPrimitive) this.sse.getResponse();

            /**Inicio de tratamiento de la informacion recibida**/
            if(response.toString()==null) {
                _retorno = -1;
            }else if(response.toString().isEmpty()){
                _retorno = -2;
            }else if(response.toString().equals("\n")){
                _retorno = -3;
            }else{
                String informacion[] = new String(Base64.decode(response.toString()), "ISO-8859-1").split("\\n");
                for(int i=0;i<informacion.length;i++){
                    this.FcnInformacion.CargarTrabajo(informacion[i],"\\|", i);
                    this.onProgressUpdate(i*100/informacion.length);
                }
                _retorno = 1;
            }
        }catch (IOException e) {
            e.printStackTrace();
            _retorno = -4;
        }catch (Exception e) {
            e.toString();
            _retorno = -5;
        }finally{
            if(this.htse != null){
                this.htse.reset();
                try {
                    this.htse.getServiceConnection().disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    _retorno = -6;
                }
            }
        }
        return _retorno;
    }


    //Operaciones despues de finalizar la conexion con el servidor
    @Override
    protected void onPostExecute(Integer rta) {
        if(rta==1){
            Toast.makeText(this.ConnectServerContext,"Carga de Ruta finalizada.", Toast.LENGTH_LONG).show();
        }else if(rta==-1){
            Toast.makeText(this.ConnectServerContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();
        }else if(rta==-2){
            Toast.makeText(this.ConnectServerContext,"Servidor respondio sin informacion.", Toast.LENGTH_SHORT).show();
        }else if(rta==-3){
            Toast.makeText(this.ConnectServerContext,"No hay rutas pendientes por cargar.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.ConnectServerContext,"Error desconocido.", Toast.LENGTH_SHORT).show();
        }
        _pDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}
