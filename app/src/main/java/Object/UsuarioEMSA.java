package Object;

/**
 * Created by JULIANEDUARDO on 20/02/2015.
 */
public class UsuarioEMSA {

    //Datos de referencia en la base de datos
    private int         id_serial;
    private int         id_consecutivo;
    private int         id_serial1;
    private int         id_serial2;
    private int         id_serial3;

    //Datos de identificacion del usuario
    private String      ruta;
    private int         cuenta;
    private String      marca_medidor;
    private String      serie_medidor;
    private String      nombre;
    private String      direccion;
    private String      tipo_uso;
    private int         factor_multiplicacion;
    private int         id_municipio;
    private String      municipio;
    private String      observacion;

    //Datos de ultima lectura tomada al usuario
    private int         anomalia_anterior;
    private int         lectura_anterior1;
    private int         lectura_anterior2;
    private int         lectura_anterior3;
    private boolean     view_tipo_energia1;
    private boolean     view_tipo_energia2;
    private boolean     view_tipo_energia3;
    private String      tipo_energia1;
    private String      tipo_energia2;
    private String      tipo_energia3;
    private double      promedio1;
    private double      promedio2;
    private double      promedio3;

    public UsuarioEMSA(){
        this.id_consecutivo = -1;
    }


    public int getId_serial() {
        return id_serial;
    }

    public void setId_serial(int id_serial) {
        this.id_serial = id_serial;
    }

    public int getId_consecutivo() {
        return id_consecutivo;
    }

    public void setId_consecutivo(int id_consecutivo) {
        this.id_consecutivo = id_consecutivo;
    }

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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSerie_medidor() {
        return serie_medidor;
    }

    public void setSerie_medidor(String serie_medidor) {
        this.serie_medidor = serie_medidor;
    }

    public String getTipo_uso() {
        return tipo_uso;
    }

    public void setTipo_uso(String tipo_uso) {
        this.tipo_uso = tipo_uso;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getFactor_multiplicacion() {
        return factor_multiplicacion;
    }

    public void setFactor_multiplicacion(int factor_multiplicacion) {
        this.factor_multiplicacion = factor_multiplicacion;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getAnomalia_anterior() {
        return anomalia_anterior;
    }

    public void setAnomalia_anterior(int anomalia_anterior) {
        this.anomalia_anterior = anomalia_anterior;
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
        this.tipo_energia1      = "Lect. "+tipo_energia1;
        this.view_tipo_energia1 = !tipo_energia1.equals("N");
    }

    public String getTipo_energia2() {
        return tipo_energia2;
    }

    public void setTipo_energia2(String tipo_energia2) {
        this.tipo_energia2      = "Lect. "+tipo_energia2;
        this.view_tipo_energia2 = !tipo_energia2.equals("N");
    }

    public String getTipo_energia3() {
        return tipo_energia3;
    }

    public void setTipo_energia3(String tipo_energia3) {
        this.tipo_energia3      = "Lect. "+tipo_energia3;
        this.view_tipo_energia3 = !tipo_energia3.equals("N");
    }

    public double getPromedio1() {
        return promedio1;
    }

    public void setPromedio1(double promedio1) {
        this.promedio1 = promedio1;
    }

    public double getPromedio2() {
        return promedio2;
    }

    public void setPromedio2(double promedio2) {
        this.promedio2 = promedio2;
    }

    public double getPromedio3() {
        return promedio3;
    }

    public void setPromedio3(double promedio3) {
        this.promedio3 = promedio3;
    }

    public boolean isView_tipo_energia1() {
        return view_tipo_energia1;
    }

    public boolean isView_tipo_energia2() {
        return view_tipo_energia2;
    }

    public boolean isView_tipo_energia3() {
        return view_tipo_energia3;
    }
}
