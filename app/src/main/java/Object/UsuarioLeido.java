package Object;

/**
 * Created by JULIANEDUARDO on 20/02/2015.
 */
public class UsuarioLeido extends Usuario {
    private int     lectura1;
    private int     lectura2;
    private int     lectura3;

    private double  critica1;
    private double  critica2;
    private double  critica3;

    private int     anomalia;
    private int     mensaje;
    private boolean leido;

    private int     intentos;


    public UsuarioLeido(){
        super();
    }


    public int getLectura1() {
        return lectura1;
    }

    public void setLectura1(int lectura1) {
        this.lectura1 = lectura1;
    }

    public int getLectura2() {
        return lectura2;
    }

    public void setLectura2(int lectura2) {
        this.lectura2 = lectura2;
    }

    public int getLectura3() {
        return lectura3;
    }

    public void setLectura3(int lectura3) {
        this.lectura3 = lectura3;
    }

    public double getCritica1() {
        return critica1;
    }

    public void setCritica1(double critica1) {
        this.critica1 = critica1;
    }

    public double getCritica2() {
        return critica2;
    }

    public void setCritica2(double critica2) {
        this.critica2 = critica2;
    }

    public double getCritica3() {
        return critica3;
    }

    public void setCritica3(double critica3) {
        this.critica3 = critica3;
    }

    public int getAnomalia() {
        return anomalia;
    }

    public void setAnomalia(int anomalia) {
        this.anomalia = anomalia;
    }

    public int getMensaje() {
        return mensaje;
    }

    public void setMensaje(int mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
}