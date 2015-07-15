package sistema;

import java.io.File;

import async_task.UpLoadFoto;
import async_task.UploadLecturas;
import clases.ClassConfiguracion;

import android.content.Context;
import android.os.CountDownTimer;

public class Beacon extends CountDownTimer{
    private String 	FolderAplicacion;
    private Context	TemporizadorCtx;

    private ClassConfiguracion  FcnCfg;
    private SQLite              BeaconSQL;

    public Beacon(Context _ctx, String _folder, long _millisInFuture, long _countDownInterval) {
        // TODO Auto-generated constructor stub
        super(_millisInFuture, _countDownInterval);
        this.TemporizadorCtx 	= _ctx;
        this.FolderAplicacion	= _folder;

        this.FcnCfg     = ClassConfiguracion.getInstance(this.TemporizadorCtx);
        this.BeaconSQL  = new SQLite(this.TemporizadorCtx, this.FolderAplicacion);
    }


    @Override
    public void onTick(long millisUntilFinished) {
        //Llamado a los metodos que se deben ejercutar cada x tiempo

        //Llamado al proceso de envio de fotos en linea
        if(this.FcnCfg.isFotos_en_linea()){
           /*File f = new File(this.FolderAplicacion+"/Fotos");
            File[] fotos = f.listFiles();
            for (int i=0;i<fotos.length;i++){
                if(!fotos[i].isDirectory()){
                    String extension = getFileExtension(fotos[i]);
                    if(extension.equals("jpeg")){
                        String[] _foto = fotos[i].getName().split("_");
                        new UpLoadFoto(this.TemporizadorCtx).execute(_foto[0],this.BeaconSQL.StrSelectShieldWhere("maestro_clientes","id_serial_1","cuenta='"+_foto[0]+"'"),fotos[i].toString());
                    }
                }
            }*/
        }

        //Envio al proceso de lecturas en linea
        new UploadLecturas(this.TemporizadorCtx).execute("");


    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }


    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
    }
}
