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
import dialogos.showDialogBox;
import global.global_var;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import android.os.AsyncTask;

/**
 * Created by SypelcDesarrollo on 03/02/2015.
 */
public class DownLoadTrabajo extends AsyncTask<String, Integer, Integer> implements global_var { //doInBakGround, Progress, onPostExecute

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
    private static final String METHOD_NAME	= "DownLoadTrabajoSync";
    private static final String SOAP_ACTION	= "DownLoadTrabajoSync";
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
            this.so.addProperty("fecha_movil", params[2]);
            this.so.addProperty("hora_movil", params[3]);
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
            }else{
                String informacion[] = response.toString().split("\\n");
                if(informacion[0].equals("1")){
                    if(informacion.length>1){
                        for(int i=1;i<informacion.length;i++){
                            this.FcnInformacion.CargarTrabajo(informacion[i],"\\|", i);
                            this.onProgressUpdate(i*100/informacion.length);
                            _retorno = 1;
                        }
                    }else{
                        _retorno = 2;
                    }
                }else if(informacion[0].equals("-1")){
                    _retorno = -3;
                }else if(informacion[0].equals("-2")){
                    _retorno = -4;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            _retorno = -5;
        }catch (Exception e) {
            e.toString();
            _retorno = -6;
        }finally{
            if(this.htse != null){
                this.htse.reset();
                try {
                    this.htse.getServiceConnection().disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    _retorno = -7;
                }
            }
        }
        return _retorno;
    }


    //Operaciones despues de finalizar la conexion con el servidor
    @Override
    protected void onPostExecute(Integer rta) {
        switch (rta){
            case -1:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","No se ha recibido respuesta del servidor.");
                break;

            case -2:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","El servidor ha enviado informacion no valida.");
                break;

            case -3:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","No hay sincronia de fecha, favor verificar la fecha y el formato (dd/mm/yyyy).");
                break;

            case -4:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","No hay sincronia de hora, favor verificar la hora.");
                break;

            case -5:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","Hubo un error en el envio de la informacion al servidor.");
                break;

            case -6:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","Hubo un error en el envio de la informacion al servidor.");
                break;

            case -7:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","Hubo un error al momento de cerrar la conexion con el servidor.");
                break;

            case 1:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_INFORMATIVE,"RECEPCION TRABAJO","Recepcion de trabajo realizada correctamente.");
                break;

            case 2:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_INFORMATIVE,"RECEPCION TRABAJO","No hay rutas pendientes por cargar.");
                break;

            default:
                new showDialogBox().showLoginDialog(this.ConnectServerContext,DIALOG_ERROR,"RECEPCION TRABAJO","Error desconocido.");
                break;
        }
        _pDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}
