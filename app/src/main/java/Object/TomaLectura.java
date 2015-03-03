package Object;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import clases.ClassCritica;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.Archivos;
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
    private Archivos                    FcnArchivos;
    private ClassCritica                FcnCritica;


    public TomaLectura(Context _ctx, String _ruta){
        this.Ctx        = _ctx;
        this.ObjUsuario = new UsuarioLeido();
        this.ObjUsuario.setRuta(_ruta);
        this.FcnCritica     = ClassCritica.getInstance(this.Ctx);
        this.FcnSQL         = new SQLite(this.Ctx, FormInicioSession.path_files_app);
        this.FcnArchivos    = new Archivos(this.Ctx, FormInicioSession.path_files_app,10);

        if(!this.FcnArchivos.ExistFolderOrFile(FormInicioSession.sub_path_pictures,true)){
            this.FcnArchivos.MakeDirectory(FormInicioSession.sub_path_pictures,true);
        }

        this.ObjUsuario.setFlagSearch(false);
        this.ObjUsuario.setBackupRuta(null);
        this.ObjUsuario.setBackupConsecutivo(-1);
    }


    public boolean getDatosUsuario(boolean _next){
        boolean _retorno  = false;
        if(this.ObjUsuario.getId_consecutivo() == -1){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio,anomalia_anterior_1",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND estado='P' ORDER BY id_secuencia ASC");
        }else if(this.ObjUsuario.isFlagSearch()){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio,anomalia_anterior_1",
                                                                "ruta='"+this.ObjUsuario.getBackupRuta()+"' AND id_secuencia="+this.ObjUsuario.getBackupConsecutivo()+" ORDER BY id_secuencia ASC");
            this.ObjUsuario.setFlagSearch(false);
            this.ObjUsuario.setBackupRuta(null);
            this.ObjUsuario.setBackupConsecutivo(-1);
        }else if(_next){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio,anomalia_anterior_1",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND id_secuencia>"+this.ObjUsuario.getId_consecutivo()+" AND estado='P' ORDER BY id_secuencia ASC");
        }else{
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("maestro_clientes",
                                                                "ruta, id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio,anomalia_anterior_1",
                                                                "ruta='"+this.ObjUsuario.getRuta()+"' AND id_secuencia<"+this.ObjUsuario.getId_consecutivo()+" AND estado='P' ORDER BY id_secuencia DESC");
        }

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.setInfUsuario();
        }
        return _retorno;
    }


    public boolean getSearchDatosUsuario(String _cuenta, String _medidor){
        boolean _retorno    = false;
        this._tempRegistro  = this.FcnSQL.SelectDataRegistro(   "maestro_clientes",
                                                                "ruta, id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio,anomalia_anterior_1",
                                                                "cuenta="+_cuenta+" AND serie_medidor ='"+_medidor+"' ORDER BY id_secuencia ASC");

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.setInfUsuario();
        }
        return _retorno;
    }


    private void setInfUsuario(){
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

        this.ObjUsuario.setLeido(this._tempRegistro.getAsString("estado").equals("T"));
        this.ObjUsuario.setAnomalia_anterior(this._tempRegistro.getAsInteger("anomalia_anterior_1"));
        //this.ObjUsuario.setAnomalia(this._tempRegistro.getAsInteger("anomalia_anterior_1"),null);

        this.getNumIntentos();
        this.getNumeroFotos();
        this.getLastLectura();

        //this.ObjUsuario.setIntentos(this.FcnSQL.CountRegistrosWhere("toma_lectura",
        //                                                            "id_serial1="+this.ObjUsuario.getId_serial1()+" AND id_serial2="+this.ObjUsuario.getId_serial2()+" AND id_serial3="+this.ObjUsuario.getId_serial3()));

        //this.ObjUsuario.setCountFotos(this.FcnArchivos.numArchivosInFolderBeginByName(  FormInicioSession.sub_path_pictures,
        //                                                                                this.ObjUsuario.getCuenta()+"",
        //                                                                                true));
    }


    public void getLastLectura(){
        if(this.FcnSQL.ExistRegistros(  "toma_lectura",
                                        "id_serial1="+this.ObjUsuario.getId_serial1()+" AND id_serial2="+this.ObjUsuario.getId_serial2()+" AND id_serial3="+this.ObjUsuario.getId_serial3())){
            this._tempRegistro = this.FcnSQL.SelectDataRegistro("toma_lectura",
                                                                "lectura1,lectura2,lectura3",
                                                                "id_serial1="+this.ObjUsuario.getId_serial1()+" AND id_serial2="+this.ObjUsuario.getId_serial2()+" AND id_serial3="+this.ObjUsuario.getId_serial3()+ " ORDER BY id DESC ");
            this.ObjUsuario.setLectura1(this._tempRegistro.getAsInteger("lectura1"));
            this.ObjUsuario.setLectura2(this._tempRegistro.getAsInteger("lectura2"));
            this.ObjUsuario.setLectura3(this._tempRegistro.getAsInteger("lectura3"));

        }else{
            this.ObjUsuario.setLectura1(0);
            this.ObjUsuario.setLectura2(0);
            this.ObjUsuario.setLectura3(0);
        }
    }


    public UsuarioLeido getInfUsuario() {
        return this.ObjUsuario;
    }


    public void getNumeroFotos(){
        this.ObjUsuario.setCountFotos(this.FcnArchivos.numArchivosInFolderBeginByName(  FormInicioSession.sub_path_pictures,
                                                                                        this.ObjUsuario.getCuenta()+"",
                                                                                        true));
    };


    public void getNumIntentos(){
        this.ObjUsuario.setIntentos(this.FcnSQL.CountRegistrosWhere("toma_lectura",
                                    "id_serial1="+this.ObjUsuario.getId_serial1()+" AND id_serial2="+this.ObjUsuario.getId_serial2()+" AND id_serial3="+this.ObjUsuario.getId_serial3()));
    }


    private void setEstado(String _estado){
        this._tempRegistro.clear();
        this._tempRegistro.put("estado",_estado);
        this.FcnSQL.UpdateRegistro( "maestro_clientes",
                                    this._tempRegistro,
                                    "id_serial="+this.ObjUsuario.getId_serial());
    }


    public boolean guardarLectura(String _lectura1, String _lectura2, String _lectura3, String _mensaje){
        boolean _retorno = false;
        int     _oldLectura1 = 0;
        int     _oldLectura2 = 0;
        int     _oldLectura3 = 0;

        if(_lectura1.isEmpty() && this.ObjUsuario.isView_tipo_energia1() && this.ObjUsuario.isNeedLectura()){
            //falta la lectura 1
        }else if(_lectura2.isEmpty() && this.ObjUsuario.isView_tipo_energia2() && this.ObjUsuario.isNeedLectura()){

        }else if(_lectura3.isEmpty() && this.ObjUsuario.isView_tipo_energia3() && this.ObjUsuario.isNeedLectura()){

        }else{
            this.ObjUsuario.setMensaje(_mensaje);

            if(this.ObjUsuario.isView_tipo_energia1() && this.ObjUsuario.isNeedLectura()){
                _oldLectura1 = Integer.parseInt(_lectura1) - this.ObjUsuario.getLectura1();
                this.ObjUsuario.setLectura1(Integer.parseInt(_lectura1));
                this.ObjUsuario.setCritica1(this.FcnCritica.calculateCritica(   this.ObjUsuario.getLectura1(),
                                                                                this.ObjUsuario.getLectura_anterior1(),
                                                                                this.ObjUsuario.getPromedio1(),
                                                                                this.ObjUsuario.getFactor_multiplicacion()));
            }else{
                _oldLectura1 = 0;
                this.ObjUsuario.setLectura1(-1);
                this.ObjUsuario.setCritica1(1);
            }

            if(this.ObjUsuario.isView_tipo_energia2() && this.ObjUsuario.isNeedLectura()){
                _oldLectura2 = Integer.parseInt(_lectura2) - this.ObjUsuario.getLectura2();
                this.ObjUsuario.setLectura2(Integer.parseInt(_lectura2));
                this.ObjUsuario.setCritica2(this.FcnCritica.calculateCritica(   this.ObjUsuario.getLectura2(),
                                                                                this.ObjUsuario.getLectura_anterior2(),
                                                                                this.ObjUsuario.getPromedio2(),
                                                                                this.ObjUsuario.getFactor_multiplicacion()));
            }else{
                _oldLectura2 = 0;
                this.ObjUsuario.setLectura2(-1);
                this.ObjUsuario.setCritica2(1);
            }

            if(this.ObjUsuario.isView_tipo_energia3() && this.ObjUsuario.isNeedLectura()){
                _oldLectura3 = Integer.parseInt(_lectura3) - this.ObjUsuario.getLectura3();
                this.ObjUsuario.setLectura3(Integer.parseInt(_lectura3));
                this.ObjUsuario.setCritica3(this.FcnCritica.calculateCritica(   this.ObjUsuario.getLectura3(),
                                                                                this.ObjUsuario.getLectura_anterior3(),
                                                                                this.ObjUsuario.getPromedio3(),
                                                                                this.ObjUsuario.getFactor_multiplicacion()));
            }else{
                _oldLectura3 = 0;
                this.ObjUsuario.setLectura3(-1);
                this.ObjUsuario.setCritica3(1);
            }

            this.ObjUsuario.setHaveCritica( this.FcnCritica.haveCritica(this.ObjUsuario.getCritica1()) ||
                                            this.FcnCritica.haveCritica(this.ObjUsuario.getCritica2()) ||
                                            this.FcnCritica.haveCritica(this.ObjUsuario.getCritica3()));
        }
        this._tempRegistro.clear();
        this._tempRegistro.put("id_serial1",this.ObjUsuario.getId_serial1());
        this._tempRegistro.put("id_serial2",this.ObjUsuario.getId_serial2());
        this._tempRegistro.put("id_serial3",this.ObjUsuario.getId_serial3());
        this._tempRegistro.put("anomalia",  this.ObjUsuario.getAnomalia());
        this._tempRegistro.put("mensaje",   this.ObjUsuario.getMensaje());
        this._tempRegistro.put("lectura1",  this.ObjUsuario.getLectura1());
        this._tempRegistro.put("lectura2",  this.ObjUsuario.getLectura2());
        this._tempRegistro.put("lectura3",  this.ObjUsuario.getLectura3());
        this._tempRegistro.put("critica1",  this.ObjUsuario.getCritica1());
        this._tempRegistro.put("critica2",  this.ObjUsuario.getCritica2());
        this._tempRegistro.put("critica3",  this.ObjUsuario.getCritica3());
        this._tempRegistro.put("tipo_uso",  this.ObjUsuario.getTipo_uso());

        _retorno =  this.FcnSQL.InsertRegistro("toma_lectura",this._tempRegistro);
        this.getNumIntentos();

        if(!this.ObjUsuario.isNeedLectura() || this.ObjUsuario.getIntentos() == 3 || !this.ObjUsuario.isHaveCritica() ||
                (this.ObjUsuario.getIntentos() == 2 && _oldLectura1 == 0 && _oldLectura2 == 0 && _oldLectura3 == 0)){
            this.ObjUsuario.setLeido(true);
            this.setEstado("T");
            this.ObjUsuario.setHaveCritica(false);
        }
        return _retorno;
    }
}
