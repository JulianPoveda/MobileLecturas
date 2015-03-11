package Object;

import clases.ClassAnomalias;

/**
 * Created by JULIANEDUARDO on 20/02/2015.
 */
public class UsuarioLeido extends UsuarioEMSA {
    private ClassAnomalias  FcnAnomalias;

    private int     newTipoUso;

    private int     lectura1;
    private int     lectura2;
    private int     lectura3;

    private int     oldLectura1;
    private int     oldLectura2;
    private int     oldLectura3;

    private double  critica1;
    private double  critica2;
    private double  critica3;

    private int     anomalia;
    private int     intentos;
    private int     countFotos;

    private String  mensaje;
    private String  strAnomalia;
    private String  descripcionCritica;

    private boolean needLectura;
    private boolean needFoto;
    private boolean needMensaje;
    private boolean leido;
    private boolean haveCritica;
    private boolean confirmLectura;


    //Atributos usados para cuando se realiza una busqueda
    private boolean flagSearch;
    public String   backupRuta;
    public int      backupConsecutivo;


    public UsuarioLeido(){
        super();
        this.FcnAnomalias = ClassAnomalias.getInstance();
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

    public String getStrAnomalia() {
        return strAnomalia;
    }

    public void setAnomalia(int _anomalia, String _strAnomalia) {
        this.anomalia       = _anomalia;
        this.strAnomalia    = _strAnomalia;
        this.needLectura= this.FcnAnomalias.needLectura(_anomalia);
        this.needFoto   = this.FcnAnomalias.needFoto(_anomalia);
        this.needMensaje= this.FcnAnomalias.needMensaje(_anomalia);
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

    public boolean isNeedLectura() {
        return needLectura;
    }

    public boolean isNeedFoto() {
        return needFoto;
    }

    public boolean isNeedMensaje() {
        return needMensaje;
    }

    public int getCountFotos() {
        return countFotos;
    }

    public void setCountFotos(int countFotos) {
        this.countFotos = countFotos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isHaveCritica() {
        return haveCritica;
    }

    public void setHaveCritica(boolean haveCritica) {
        this.haveCritica = haveCritica;
    }

    public boolean isFlagSearch() {
        return flagSearch;
    }

    public void setFlagSearch(boolean flagSearch) {
        this.flagSearch = flagSearch;
    }

    public String getBackupRuta() {
        return backupRuta;
    }

    public void setBackupRuta(String backupRuta) {
        this.backupRuta = backupRuta;
    }

    public int getBackupConsecutivo() {
        return backupConsecutivo;
    }

    public void setBackupConsecutivo(int backupConsecutivo) {
        this.backupConsecutivo = backupConsecutivo;
    }

    public int getOldLectura1() {
        return oldLectura1;
    }

    public void setOldLectura1(int oldLectura1) {
        this.oldLectura1 = oldLectura1;
    }

    public int getOldLectura2() {
        return oldLectura2;
    }

    public void setOldLectura2(int oldLectura2) {
        this.oldLectura2 = oldLectura2;
    }

    public int getOldLectura3() {
        return oldLectura3;
    }

    public void setOldLectura3(int oldLectura3) {
        this.oldLectura3 = oldLectura3;
    }

    public boolean isConfirmLectura() {
        return confirmLectura;
    }

    public void setConfirmLectura(boolean confirmLectura) {
        this.confirmLectura = confirmLectura;
    }

    public int getNewTipoUso() {
        return newTipoUso;
    }

    public void setNewTipoUso(int newTipoUso) {
        this.newTipoUso = newTipoUso;
    }

    public String getDescripcionCritica() {
        return descripcionCritica;
    }

    public void setDescripcionCritica(String descripcionCritica) {
        this.descripcionCritica = descripcionCritica;
    }
}
