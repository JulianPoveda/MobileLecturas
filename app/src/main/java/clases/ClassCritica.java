package clases;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 27/02/2015.
 */
public class ClassCritica {
    private static Context              context;
    private SQLite                      FcnSQL;
    private static ClassCritica         ourInstance;
    private ArrayList<ContentValues>    _tempTabla;
    private ContentValues               _tempRegistro;

    //private static ClassCritica ourInstance = new ClassCritica();

    public static ClassCritica getInstance(Context _ctx) {
        if(ourInstance == null){
            ourInstance = new ClassCritica(_ctx);
        }else{
            context = _ctx;
        }
        return ourInstance;
    }

    private ClassCritica(Context _ctx) {
        this.context    = _ctx;
        this._tempRegistro  = new ContentValues();
        this._tempTabla     = new ArrayList<ContentValues>();
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
    }


    public double calculateCritica(int _newLectura, int _oldLectura, double _promedio, int _factorMultiplicacion){
        return ((_newLectura - _oldLectura)/(_promedio+ Double.parseDouble("0.000001"))) * _factorMultiplicacion;
    }

    public boolean haveCritica(double _critica){
        String descripcion = this.FcnSQL.StrSelectShieldWhere("param_critica","descripcion","minimo<="+_critica+" AND maximo > "+_critica);
        return !descripcion.equals("Normal");
    }

    public String getDescripcionCritica(double _critica){
        return this.FcnSQL.StrSelectShieldWhere("param_critica","descripcion","minimo<="+_critica+" AND maximo >= "+_critica+"");
    }

    public String getMensajeCritica(double _critica){
        return this.FcnSQL.StrSelectShieldWhere("param_critica","mensaje","minimo<="+_critica+" AND maximo >= "+_critica+"");
    }
}
