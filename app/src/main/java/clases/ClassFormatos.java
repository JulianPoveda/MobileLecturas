package clases;

import android.content.Context;

//import ZebraPrinter.Zebra_QL420plus;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import printerZebra.ClassPrinter;
import sistema.DateTime;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 13/02/2015.
 */
public class ClassFormatos {
    private Context 		    context;
    private boolean             copiaSistema;

    private SQLite              FcnSQL;
    private ClassPrinter        FcnPrinter;
    private DateTime            FcnTime;
    //private Zebra_QL420plus     FcnZebra;
    private ClassConfiguracion  FcnConfiguracion;


    public ClassFormatos(Context context, boolean _copiaSistema){
        this.context = context;
        this.copiaSistema 		= _copiaSistema;

        this.FcnTime            = DateTime.getInstance();
        this.FcnConfiguracion   = ClassConfiguracion.getInstance(this.context);
        this.FcnSQL             = new SQLite(this.context, FormInicioSession.path_files_app);
        //this.FcnZebra           = new Zebra_QL420plus(200, 40, 5, 5, 50, _copiaSistema);

        this.FcnPrinter     = new   ClassPrinter(false);
        this.FcnPrinter.setVerticalPrinter(true);
        this.FcnPrinter.SetSizePage(750,376);
        this.FcnPrinter.SetSizeMargin(10,1,1,30);
        this.FcnPrinter.setRegisterFont("0", "TITULO", 4, 22, 22);
        this.FcnPrinter.setRegisterFont("0", "TEXTO", 3, 18, 22);
        this.FcnPrinter.setRegisterFont("0", "NOTA", 2, 10, 18);
        //this.FcnPrinter.resetEtiqueta();
    }

    public void FormatoPrueba(){
        this.FcnPrinter.resetEtiqueta();
        this.FcnPrinter.WriteDefaultText("TITULO",80,0,1,"EMSA S.A. E.S.P.");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,1,"Estimado usuario el dia de hoy "+ this.FcnTime.DateWithNameMonthShort());
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,1,"a las "+this.FcnTime.GetHora()+" estuvimos realizando la ");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,1,"toma de lectura con reporte:");
        this.FcnPrinter.WriteDefaultText("TITULO", 0,1,1,"LECTURA:  AS  :17347.0 ");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,1,1,"OBS      : 00-INSTALACION NORMAL");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,0,"CUENTA   : 306382388");
        this.FcnPrinter.WriteDefaultText("TEXTO",400,0,1,"MPIO: PUERTO GAITAN");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,1,"NOMBRE   : ROSA MARIA AVILA");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,1,"DIRECCION: CL 13 05 39 BRR POPULAR");
        this.FcnPrinter.WriteDefaultText("TEXTO",  0,0,0,"MEDIDOR  : 110193718 - MET");
        this.FcnPrinter.WriteDefaultText("TEXTO",500,0,1,"INSPECTOR: 1395");
        this.FcnPrinter.WriteDefaultText("NOTA",   0,3,1,"PARA MAYOR INFORMACION COMUNIQUESE AL 6642636 O AL 018000-918-615");
        this.FcnPrinter.printLabel(this.FcnConfiguracion.getPrinter());
    }

    public void ActaLectura(String _tipo, int _lectura, String _anomalia, int _cuenta, String _municipio, String _nombre, String _direccion, String _medidor, int _inspector){
        if(_nombre.length()>35){
            _nombre = _nombre.substring(0,35);
        }

        if(_direccion.length()>35){
            _direccion = _direccion.substring(0,35);
        }

        this.FcnPrinter.resetEtiqueta();
        this.FcnPrinter.WriteDefaultText("TITULO", 80,0,1,"EMSA S.A. E.S.P.");
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,1,"Estimado usuario el dia de hoy "+ this.FcnTime.DateWithNameMonthShort());
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,1,"a las "+this.FcnTime.GetHora()+" estuvimos realizando la ");
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,1,"toma de lectura con reporte:");
        this.FcnPrinter.WriteDefaultText("TITULO",  0,1,1,"LECTURA "+_tipo+" : "+_lectura);
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,1,1,"OBS      : "+_anomalia);
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,0,"CUENTA   : "+_cuenta);
        this.FcnPrinter.WriteDefaultText("TEXTO", 450,0,1,"MPIO: "+_municipio);
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,1,"NOMBRE   : "+_nombre);
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,1,"DIRECCION: "+_direccion);
        this.FcnPrinter.WriteDefaultText("TEXTO",   0,0,0,"MEDIDOR  : "+_medidor);
        this.FcnPrinter.WriteDefaultText("TEXTO", 450,0,1,"INSPECTOR: "+_inspector);
        this.FcnPrinter.WriteDefaultText("NOTA",    0,3,1,"PARA MAYOR INFORMACION COMUNIQUESE AL 6642636 O AL 018000-918-615");
        this.FcnPrinter.printLabel(this.FcnConfiguracion.getPrinter());
    }
}
