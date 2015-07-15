package async_task;

/**
 * Created by DesarrolloJulian on 11/06/2015.
 * Version: 1.0
 * Novedades:   Se realiza esta tarea asincronna con el fin de poder enviar la impresion del tiquete
 *              sin que esto genere retrasos en el formulario toma lectura y de esta forma poder agilizar
 *              el proceso de toma de lectura.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import clases.ClassFormatos;
import clases.ClassSession;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
import Object.UsuarioLeido;



public class PrintVoucher extends AsyncTask<String, Integer, String> { //doInBakGround, Progress, onPostExecute
    /*
    Instancias a clases
     */
    private ClassFormatos   FcnFormatos;
    private UsuarioLeido    ObjUsuarioLeido;
    private ClassSession    FcnSession;


    /**Instancias a clases**/
    private Archivos            FcnArch;
    private ClassConfiguracion  FcnCfg;
    //private ClassFlujoInformacion FcnInformacion;

    /**Variables Locales**/
    private Context ConnectServerContext;

    //Contructor de la clase
    public PrintVoucher(Context context){
        this.ConnectServerContext 	= context;
        this.FcnFormatos            = new ClassFormatos(this.ConnectServerContext, false);
        this.ObjUsuarioLeido        = new UsuarioLeido();
        this.FcnSession             = ClassSession.getInstance(this.ConnectServerContext);
    }


    //Operaciones antes de realizar la conexion con el servidor
    protected void onPreExecute(){

    }



    //Conexion con el servidor en busca de actualizaciones de trabajo programado
    @Override
    protected String doInBackground(String... params) {
        String _retorno = "";
        //this.ObjUsuarioLeido = params[0];
        this.FcnFormatos.ActaLectura(params[0],
                Integer.parseInt(params[1]),
                params[2],
                Integer.parseInt(params[3]),
                params[4],
                Integer.parseInt(params[5]),
                params[6],
                Integer.parseInt(params[7]),
                params[8],
                params[9],
                params[10],
                params[11],
                Integer.parseInt(params[12]));



        /*try{
            this.so = new SoapObject(NAMESPACE, this.METHOD_NAME);
            this.so.addProperty("id_interno", params[0]);
            this.sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            this.sse.dotNet=true;
            this.sse.setOutputSoapObject(this.so);
            this.htse = new HttpTransportSE(this.URL);
            this.htse.call(SOAP_ACTION, this.sse);
            response=(SoapPrimitive) this.sse.getResponse();*/

            /**Inicio de tratamiento de la informacion recibida**/
            /*if(response.toString()==null) {
                _retorno = -1;
            }else if(response.toString().isEmpty()){
                _retorno = -2;
            }else{
                this.FcnInformacion.EliminarParametros();
                String informacion[] = new String(Base64.decode(response.toString()), "ISO-8859-1").split("\\n");
                for(int i=0;i<informacion.length;i++){
                    this.FcnInformacion.CargarParametros(informacion[i],"\\|");
                    onProgressUpdate(i*100/informacion.length);
                }
                _retorno = 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            _retorno = -3;
        }catch (Exception e) {
            e.toString();
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
        }*/
        return _retorno;
    }



    //Operaciones despues de finalizar la conexion con el servidor
    @Override
    protected void onPostExecute(String rta){
        /*if(rta==1){
            Toast.makeText(this.ConnectServerContext,"Carga de parametros finalizada.", Toast.LENGTH_LONG).show();
        }else if(rta==-1){
            Toast.makeText(this.ConnectServerContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();
        }else if(rta==-2){
            Toast.makeText(this.ConnectServerContext,"No hay nuevas ordenes pendientes para cargar.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.ConnectServerContext,"Error desconocido.", Toast.LENGTH_SHORT).show();
        }
        _pDialog.dismiss();*/
    }


    /*@Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }*/
}