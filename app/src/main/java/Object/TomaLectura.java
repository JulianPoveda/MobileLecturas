package Object;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 20/02/2015.
 */
public class TomaLectura {
    private ContentValues               _tempRegistro;
    private ArrayList<ContentValues>    _tempTabla;
    private Context                     Ctx;
    private SQLite                      FcnSQL;
    private UsuarioLeido                ObjUsuario;


    public TomaLectura(Context _ctx, String _ruta){
        this.Ctx        = _ctx;
        this.FcnSQL     = new SQLite(this.Ctx, FormInicioSession.path_files_app);
        this.ObjUsuario = new UsuarioLeido();
        this.ObjUsuario.setRuta(_ruta);
    }


    public boolean getDatosUsuario(boolean _next){
        boolean _retorno  = false;
        if(this.ObjUsuario.getId_consecutivo() == -1){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND estado='P' ORDER BY id_secuencia ASC");
        }else if(_next){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND id_secuencia>"+this.ObjUsuario.getId_consecutivo()+" AND estado='P' ORDER BY id_secuencia ASC");
        }else{
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND id_secuencia<"+this.ObjUsuario.getId_consecutivo()+" AND estado='P' ORDER BY id_secuencia DESC");
        }

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.setFcnUsuario();
        }
        return _retorno;
    }


    public boolean getSearchDatosUsuario(String _cuenta, String _medidor){
        boolean _retorno    = false;
        this._tempRegistro  = this.FcnSQL.SelectDataRegistro(   "maestro_clientes",
                                                                "ruta, id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                "cuenta="+_cuenta+" AND serie_medidor ='"+_medidor+"' ORDER BY id_secuencia ASC");

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.setFcnUsuario();
        }
        return _retorno;
    }


    private void setFcnUsuario(){
        this.ObjUsuario.setRuta(this._tempRegistro.getAsString("ruta"));
        this.ObjUsuario.setId_serial(this._tempRegistro.getAsInteger("id_serial"));
        this.ObjUsuario.setId_consecutivo(this._tempRegistro.getAsInteger("id_secuencia"));
        this.ObjUsuario.setCuenta(this._tempRegistro.getAsInteger("cuenta"));
        this.ObjUsuario.setMarca_medidor(this._tempRegistro.getAsString("marca_medidor"));
        this.ObjUsuario.setSerie_medidor(this._tempRegistro.getAsString("serie_medidor"));
        this.ObjUsuario.setNombre(this._tempRegistro.getAsString("nombre"));
        this.ObjUsuario.setDireccion(this._tempRegistro.getAsString("direccion"));
        this.ObjUsuario.setFactor_multiplicacion(this._tempRegistro.getAsInteger("factor_multiplicacion"));
        this.ObjUsuario.setTipo_uso(this._tempRegistro.getAsString("tipo_uso"));
        this.ObjUsuario.setMunicipio(this.FcnSQL.StrSelectShieldWhere("param_municipios", "municipio", "id_municipio=" + this._tempRegistro.getAsString("id_municipio")));

        this.ObjUsuario.setId_serial1(this._tempRegistro.getAsInteger("id_serial_1"));
        this.ObjUsuario.setLectura_anterior1(this._tempRegistro.getAsInteger("lectura_anterior_1"));
        this.ObjUsuario.setTipo_energia1(this._tempRegistro.getAsString("tipo_energia_1"));
        this.ObjUsuario.setPromedio1(this._tempRegistro.getAsInteger("promedio_1"));

        this.ObjUsuario.setId_serial2(this._tempRegistro.getAsInteger("id_serial_2"));
        this.ObjUsuario.setLectura_anterior2(this._tempRegistro.getAsInteger("lectura_anterior_2"));
        this.ObjUsuario.setTipo_energia2(this._tempRegistro.getAsString("tipo_energia_2"));
        this.ObjUsuario.setPromedio2(this._tempRegistro.getAsInteger("promedio_2"));

        this.ObjUsuario.setId_serial3(this._tempRegistro.getAsInteger("id_serial_3"));
        this.ObjUsuario.setLectura_anterior3(this._tempRegistro.getAsInteger("lectura_anterior_3"));
        this.ObjUsuario.setTipo_energia3(this._tempRegistro.getAsString("tipo_energia_3"));
        this.ObjUsuario.setPromedio3(this._tempRegistro.getAsInteger("promedio_3"));

        this.ObjUsuario.setLeido(this._tempRegistro.getAsString("estado").equals('T'));
    }

    public UsuarioLeido getFcnUsuario() {
        return this.ObjUsuario;
    }
}
