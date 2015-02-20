package clases;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 05/02/2015.
 */
public class ClassTomarLectura {
    private Context                     context;
    private ContentValues               _tempRegistro;
    private ArrayList<ContentValues>    _tempTabla  = new ArrayList<ContentValues>();

    private SQLite          FcnSQL;

    private int         id_serial;
    private String      ruta;
    private int         id_consecutivo;
    private int         lectura;
    private int         cuenta;
    private String      marca_medidor;
    private String      serie_medidor;
    private String      nombre;
    private String      direccion;
    private String      tipo_uso;
    private int         factor_multiplicacion;
    private int         id_serial1;
    private int         id_serial2;
    private int         id_serial3;
    private int         lectura_anterior1;
    private int         lectura_anterior2;
    private int         lectura_anterior3;
    private String      tipo_energia1;
    private String      tipo_energia2;
    private String      tipo_energia3;
    private double      promedio1;
    private double      promedio2;
    private double      promedio3;
    private String      municipio;
    private String      estado;


    public ClassTomarLectura(Context _ctx, String _ruta){
        this.context        = _ctx;
        this.ruta           = _ruta;
        this.id_consecutivo = -1;
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);
    }

    public boolean getNextDatosUsuario(){
        boolean _retorno  = false;
        if(this.id_consecutivo == -1){
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                    "id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                    "ruta='"+this.ruta+"' AND estado='P' ORDER BY id_secuencia ASC");
        }else{
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                    "id_serial,id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                    "ruta='"+this.ruta+"' AND id_secuencia>"+this.id_consecutivo+" AND estado='P' ORDER BY id_secuencia ASC");
        }

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.getDatosConsulta();
        }
        return _retorno;
    }

    public boolean getBackDatosUsuario(){
        boolean _retorno  = false;
        if(this.id_consecutivo == -1){
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                    "id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado",
                                                                    "ruta='"+this.ruta+"' AND estado='P' ORDER BY id_secuencia ASC");
        }else{
            this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                    "id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado",
                                                                    "ruta='"+this.ruta+"' AND id_secuencia<"+this.id_consecutivo+" AND estado='P' ORDER BY id_secuencia DESC");
        }

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.getDatosConsulta();
        }
        return _retorno;
    }


    public boolean getSearchDatosUsuario(String _cuenta, String _medidor){
        boolean _retorno  = false;
        this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                "id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                "cuenta="+_cuenta+" AND serie_medidor ='"+_medidor+"' ORDER BY id_secuencia ASC");

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.getDatosConsulta();
        }
        return _retorno;
    }


    public boolean getDatosUsuarioByIdSerial(int _id){
        boolean _retorno  = false;
        this._tempRegistro =    this.FcnSQL.SelectDataRegistro( "maestro_clientes",
                                                                "id_serial, id_secuencia, cuenta,marca_medidor,serie_medidor,nombre,direccion,tipo_uso,factor_multiplicacion,id_serial_1,lectura_anterior_1,tipo_energia_1,promedio_1,id_serial_2,lectura_anterior_2,tipo_energia_2,promedio_2,id_serial_3,lectura_anterior_3,tipo_energia_3,promedio_3,estado,id_municipio",
                                                                "id_serial="+_id+" ORDER BY id_secuencia ASC");

        if(this._tempRegistro.size()>0){
            _retorno = true;
            this.getDatosConsulta();
        }
        return _retorno;
    }


    public ArrayList<ContentValues> ListaClientes(String _ruta, boolean _filtro){
        if(_filtro){
            this._tempTabla = this.FcnSQL.SelectData(   "maestro_clientes",
                                                        "cuenta,serie_medidor,nombre,direccion",
                                                        "ruta='"+_ruta+"'");
        }else{
            this._tempTabla = this.FcnSQL.SelectData(   "maestro_clientes",
                                                        "cuenta,serie_medidor,nombre,direccion",
                                                        "id_serial IS NOT NULL");
        }
        return this._tempTabla;
    }

    public boolean getCritica(double critica){
        String descripcion = this.FcnSQL.StrSelectShieldWhere("param_critica","descripcion","minimo<="+critica+" AND maximo >= "+critica+"");
        return !descripcion.equals("Normal");
    }

    public void guardarLectura(int id_serial1, int id_serial2, int id_serial3,int anomalia, String mensaje ,int lectura1, int lectura2, int lectura3, double critica1, double critica2, double critica3, String _tipo_uso){
                this._tempRegistro.clear();
                this._tempRegistro.put("id_serial1",id_serial1);
                this._tempRegistro.put("id_serial2",id_serial2);
                this._tempRegistro.put("id_serial3",id_serial3);
                this._tempRegistro.put("anomalia",anomalia);
                this._tempRegistro.put("mensaje",mensaje);
                this._tempRegistro.put("lectura1",lectura1);
                this._tempRegistro.put("lectura2",lectura2);
                this._tempRegistro.put("lectura3",lectura3);
                this._tempRegistro.put("critica1",critica1);
                this._tempRegistro.put("critica2",critica2);
                this._tempRegistro.put("critica3",critica3);
                this._tempRegistro.put("tipo_uso",_tipo_uso);
                this.FcnSQL.InsertRegistro("toma_lectura",this._tempRegistro);
    }

    public void cambiarEstadoLectura(Integer _id_serial){
        this._tempRegistro.clear();
        this._tempRegistro.put("estado","T");
        this.FcnSQL.InsertOrUpdateRegistro("maestro_clientes",_tempRegistro,"id_serial="+_id_serial+"");
    }

    private void getDatosConsulta(){
        this.setId_serial(this._tempRegistro.getAsInteger("id_serial"));
        this.setId_consecutivo(this._tempRegistro.getAsInteger("id_secuencia"));
        this.setCuenta(this._tempRegistro.getAsInteger("cuenta"));
        this.setMarca_medidor(this._tempRegistro.getAsString("marca_medidor"));
        this.setSerie_medidor(this._tempRegistro.getAsString("serie_medidor"));
        this.setNombre(this._tempRegistro.getAsString("nombre"));
        this.setDireccion(this._tempRegistro.getAsString("direccion"));
        this.setFactor_multiplicacion(this._tempRegistro.getAsInteger("factor_multiplicacion"));
        this.setTipo_uso(this._tempRegistro.getAsString("tipo_uso"));
        this.setMunicipio(this.FcnSQL.StrSelectShieldWhere("param_municipios","municipio","id_municipio="+this._tempRegistro.getAsString("id_municipio")));

        this.setId_serial1(this._tempRegistro.getAsInteger("id_serial_1"));
        this.setLectura_anterior1(this._tempRegistro.getAsInteger("lectura_anterior_1"));
        this.setTipo_energia1(this._tempRegistro.getAsString("tipo_energia_1"));
        this.setPromedio1(this._tempRegistro.getAsInteger("promedio_1"));

        this.setId_serial2(this._tempRegistro.getAsInteger("id_serial_2"));
        this.setLectura_anterior2(this._tempRegistro.getAsInteger("lectura_anterior_2"));
        this.setTipo_energia2(this._tempRegistro.getAsString("tipo_energia_2"));
        this.setPromedio2(this._tempRegistro.getAsInteger("promedio_2"));

        this.setId_serial3(this._tempRegistro.getAsInteger("id_serial_3"));
        this.setLectura_anterior3(this._tempRegistro.getAsInteger("lectura_anterior_3"));
        this.setTipo_energia3(this._tempRegistro.getAsString("tipo_energia_3"));
        this.setPromedio3(this._tempRegistro.getAsInteger("promedio_3"));
        this.setEstado(this._tempRegistro.getAsString("estado"));
    }

    public int getIntentos(){
        return this.FcnSQL.CountRegistrosWhere( "toma_lectura",
                                                "id_serial1="+this.getId_serial1()+" AND id_serial2="+this.getId_serial2()+" AND id_serial3="+this.getId_serial3())+1;
    }

    public ContentValues getLecturasIntento(){
        this._tempRegistro.clear();
        this._tempRegistro = this.FcnSQL.SelectDataRegistro("toma_lectura",
                                                            "lectura1,lectura2,lectura3",
                                                            "id_serial1="+this.getId_serial1()+" AND id_serial2="+this.getId_serial2()+" AND id_serial3="+this.getId_serial3()+" ORDER BY fecha_toma DESC");

        if(this._tempRegistro.size()==0){
            this._tempRegistro.put("lectura1",-1);
            this._tempRegistro.put("lectura2",-1);
            this._tempRegistro.put("lectura3",-1);
        }
        return this._tempRegistro;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getId_consecutivo() {
        return id_consecutivo;
    }

    public void setId_consecutivo(int id_consecutivo) {
        this.id_consecutivo = id_consecutivo;
    }

    public int getLectura() {
        return lectura;
    }

    public void setLectura(int lectura) {
        this.lectura = lectura;
    }

    public int getCuenta() {
        return cuenta;
    }

    public void setCuenta(int cuenta) {
        this.cuenta = cuenta;
    }

    public String getMarca_medidor() {
        return marca_medidor;
    }

    public void setMarca_medidor(String marca_medidor) {
        this.marca_medidor = marca_medidor;
    }

    public String getSerie_medidor() {
        return serie_medidor;
    }

    public void setSerie_medidor(String serie_medidor) {
        this.serie_medidor = serie_medidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo_uso(){return tipo_uso;}

    public void  setTipo_uso(String tipo_uso){this.tipo_uso = tipo_uso;}

    public int getFactor_multiplicacion(){return  factor_multiplicacion;}

    public void  setFactor_multiplicacion(int factor_multiplicacion){this.factor_multiplicacion = factor_multiplicacion;}

    public int getId_serial1() {
        return id_serial1;
    }

    public void setId_serial1(int id_serial1) {
        this.id_serial1 = id_serial1;
    }

    public int getId_serial2() {
        return id_serial2;
    }

    public void setId_serial2(int id_serial2) {
        this.id_serial2 = id_serial2;
    }

    public int getId_serial3() {
        return id_serial3;
    }

    public void setId_serial3(int id_serial3) {
        this.id_serial3 = id_serial3;
    }

    public int getLectura_anterior1() {
        return lectura_anterior1;
    }

    public void setLectura_anterior1(int lectura_anterior1) {
        this.lectura_anterior1 = lectura_anterior1;
    }

    public int getLectura_anterior2() {
        return lectura_anterior2;
    }

    public void setLectura_anterior2(int lectura_anterior2) {
        this.lectura_anterior2 = lectura_anterior2;
    }

    public int getLectura_anterior3() {
        return lectura_anterior3;
    }

    public void setLectura_anterior3(int lectura_anterior3) {
        this.lectura_anterior3 = lectura_anterior3;
    }

    public String getTipo_energia1() {
        return tipo_energia1;
    }

    public void setTipo_energia1(String tipo_energia1) {
        this.tipo_energia1 = tipo_energia1;
    }

    public String getTipo_energia2() {
        return tipo_energia2;
    }

    public void setTipo_energia2(String tipo_energia2) {
        this.tipo_energia2 = tipo_energia2;
    }

    public String getTipo_energia3() {
        return tipo_energia3;
    }

    public void setTipo_energia3(String tipo_energia3) {
        this.tipo_energia3 = tipo_energia3;
    }

    public double getPromedio1() {return promedio1;}

    public void setPromedio1(double promedio1) {this.promedio1 = promedio1;}

    public double getPromedio2() {return promedio2;}

    public void setPromedio2(double promedio2) {this.promedio2 = promedio2;}

    public double getPromedio3() {return promedio3;}

    public void setPromedio3(double promedio3) {this.promedio3 = promedio3;}

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_serial() {
        return id_serial;
    }

    public void setId_serial(int id_serial) {
        this.id_serial = id_serial;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
