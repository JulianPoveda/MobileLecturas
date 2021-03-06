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

        this.FcnPrinter     = new   ClassPrinter(this.context, false);
        this.FcnPrinter.setVerticalPrinter(true);
        this.FcnPrinter.SetSizePage(780,376);
        this.FcnPrinter.SetSizeMargin(10,1,1,30);
        //this.FcnPrinter.setRegisterFont("0", "TITULO", 4, 22, 22);
        //this.FcnPrinter.setRegisterFont("0", "TEXTO", 3, 18, 22);

        this.FcnPrinter.setRegisterFont("INSTRUCT.CPF","TITULO", 0, 21,38);
        this.FcnPrinter.setRegisterFont("JACKINPU.CPF","TEXTO", 0, 18, 18);
        this.FcnPrinter.setRegisterFont("0", "NOTA", 2, 10, 18);
        //this.FcnPrinter.resetEtiqueta();
    }

    public void FormatoPrueba(){
        this.FcnPrinter.resetEtiqueta();
        this.FcnPrinter.DrawImage("EMSA_90.PCX",0,0);
        this.FcnPrinter.WriteDefaultText("TITULO", 0,0,1,"EMSA S.A. E.S.P.");
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

    public void ActaLectura(String _tipo1, int _lectura1, String _tipo2, int _lectura2, String _tipo3, int _lectura3, String _anomalia,
                            int _cuenta, String _municipio, String _nombre, String _direccion, String _medidor, int _inspector){

        String strLectura="";
        //String strTipoLectura;

        if(_nombre.length()>35){
            _nombre = _nombre.substring(0,35);
        }

        if(_direccion.length()>35){
            _direccion = _direccion.substring(0, 35);
        }

        /*if(_tipo1.equals("Lect. A")){
            strLectura = "A:";
        }else if(_tipo1.equals("Lect. R")){
            strLectura = "R:";
        }else{
            strLectura = "A:";
        }*/

        if(_lectura1 != -1){
            strLectura += _tipo1.substring(_tipo1.length()-1)+":"+String.valueOf(_lectura1)+" ";
        }

        if(_lectura2 != -1){
            strLectura += _tipo2.substring(_tipo2.length()-1)+":"+String.valueOf(_lectura2)+" ";
        }

        if(_lectura3 != -1){
            strLectura += _tipo3.substring(_tipo3.length()-1)+":"+String.valueOf(_lectura3)+" ";
        }

        this.FcnPrinter.resetEtiqueta();
        this.FcnPrinter.DrawMargin();
        this.FcnPrinter.DrawImage("EMSA_90.PCX",10,130);
        this.FcnPrinter.WriteDefaultText("TITULO",  120,0,1,"EMSA S.A. E.S.P.");
        this.FcnPrinter.WriteDefaultText("TEXTO",   120,0,1,"Estimado usuario el dia de hoy "+ this.FcnTime.DateWithNameMonthShort()+" a las ");
        this.FcnPrinter.WriteDefaultText("TEXTO",   120,0,1,this.FcnTime.GetHora()+" estuvimos realizando la toma de ");
        this.FcnPrinter.WriteDefaultText("TEXTO",   120,0,2,"lectura con reporte:");
        this.FcnPrinter.WriteDefaultText("TITULO",  120,0,1,"LECT. "+strLectura);
        this.FcnPrinter.WriteDefaultText("TEXTO",   10,1,1,"OBS      : "+_anomalia);
        this.FcnPrinter.WriteDefaultText("TEXTO",   10,0,0,"CUENTA   : "+_cuenta);
        this.FcnPrinter.WriteDefaultText("TEXTO", 450,0,1, "MPIO: "+_municipio);
        this.FcnPrinter.WriteDefaultText("TEXTO",   10,0,1,"NOMBRE   : "+_nombre);
        this.FcnPrinter.WriteDefaultText("TEXTO",   10,0,1,"DIRECCION: "+_direccion);
        this.FcnPrinter.WriteDefaultText("TEXTO",   10,0,0,"MEDIDOR  : "+_medidor);
        this.FcnPrinter.WriteDefaultText("TEXTO", 450,0,1, "INSPECTOR: "+_inspector);
        this.FcnPrinter.DrawImage("SYPELC_9.PCX",600,80);
        this.FcnPrinter.WriteDefaultText("NOTA",    10,3,1,"PARA MAYOR INFORMACION COMUNIQUESE AL 6642636 O AL 018000-918-615");
        this.FcnPrinter.printLabel(this.FcnConfiguracion.getPrinter());
    }
}
