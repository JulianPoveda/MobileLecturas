package clases;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 02/02/2015.
 */
public class ClassConfiguracion {
    /**
     * Atributos generales de la clase
     */
    private static ClassConfiguracion   ourInstance;
    private static Context              context;
    private static SQLite               FcnSQL;

    private ArrayList<ContentValues>    _tempTabla;
    private ContentValues               _tempRegistro;

    /**
     * Atributos especificos de la clase
     */
    private String  ip_server;
    private String  port;
    private String  module_web_service;
    private String  web_service;
    private String  printer;
    private String  version_software;
    private String  version_bd;
    private boolean fotos_en_linea;
    private boolean facturas_en_sitio;
    private boolean debug;

    public static ClassConfiguracion getInstance(Context _ctx) {
        if(ourInstance == null){
            ourInstance = new ClassConfiguracion(_ctx);
        }else{
            context = _ctx;
        }
        return ourInstance;
    }

    private ClassConfiguracion(Context _ctx){
        this.context        = _ctx;
        this._tempRegistro  = new ContentValues();
        this._tempTabla     = new ArrayList<ContentValues>();
        this.FcnSQL         = new SQLite(this.context, FormInicioSession.path_files_app);

        this.ip_server          = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Servidor'");
        this.port               = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Puerto'");
        this.module_web_service = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Modulo'");
        this.web_service        = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Web_Service'");
        this.printer            = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Impresora'");
        this.version_software   = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Version_Software'");
        this.version_bd         = this.FcnSQL.StrSelectShieldWhere("param_configuracion","valor","item='Version_BD'");
        this.fotos_en_linea     = this.FcnSQL.ExistRegistros("param_configuracion","item='FotosOnLine' AND valor='true'");
        this.facturas_en_sitio  = this.FcnSQL.ExistRegistros("param_configuracion","item='FacturasOnLine' AND valor='true'");
        this.debug              = this.FcnSQL.ExistRegistros("param_configuracion","item='Debug' AND valor='true'");
    }

    public String getIp_server() {
        return ip_server;
    }

    public void setIp_server(String ip_server) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",ip_server);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Servidor'")){
            this.ip_server = ip_server;
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",port);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Puerto'")){
            this.port = port;
        }
    }

    public String getModule_web_service() {
        return module_web_service;
    }

    public void setModule_web_service(String module_web_service) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",module_web_service);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Modulo'")){
            this.module_web_service = module_web_service;
        }
    }

    public String getWeb_service() {
        return web_service;
    }

    public void setWeb_service(String web_service) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",web_service);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Web_Service'")){
            this.web_service = web_service;
        }
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",printer);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Impresora'")){
            this.printer = printer;
        }
    }

    public String getVersion_software() {
        return version_software;
    }

    public void setVersion_software(String version_software) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",version_software);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Version_Software'")){
            this.version_software = version_software;
        }
    }

    public String getVersion_bd() {
        return version_bd;
    }

    public void setVersion_bd(String version_bd) {
        this._tempRegistro.clear();
        this._tempRegistro.put("valor",version_bd);
        if(this.FcnSQL.UpdateRegistro("param_configuracion",this._tempRegistro,"item='Version_BD'")){
            this.version_bd = version_bd;
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this._tempRegistro.clear();
        if (debug){
            this._tempRegistro.put("valor","true");
        }else{
            this._tempRegistro.put("valor","false");
        }
        if(this.FcnSQL.UpdateRegistro("param_configuracion", this._tempRegistro,"item='Debug'")){
            this.debug = debug;
        }
    }

    public boolean isFotos_en_linea() {
        return fotos_en_linea;
    }

    public void setFotos_en_linea(boolean fotos_en_linea) {
        this._tempRegistro.clear();
        if (fotos_en_linea){
            this._tempRegistro.put("valor","true");
        }else{
            this._tempRegistro.put("valor","false");
        }
        if(this.FcnSQL.UpdateRegistro("param_configuracion", this._tempRegistro,"item='FotosOnLine'")){
            this.fotos_en_linea = fotos_en_linea;
        }
    }

    public boolean isFacturas_en_sitio() {
        return facturas_en_sitio;
    }

    public void setFacturas_en_sitio(boolean facturas_en_sitio) {
        this._tempRegistro.clear();
        if (facturas_en_sitio){
            this._tempRegistro.put("valor","true");
        }else{
            this._tempRegistro.put("valor","false");
        }
        if(this.FcnSQL.UpdateRegistro("param_configuracion", this._tempRegistro,"item='FacturasOnLine'")){
            this.facturas_en_sitio = facturas_en_sitio;
        }
    }
}
