/***********************************************************************************************************************************
 * Version 1.0
 * Fecha: Mayo 14-2014
 * News:	ExecuteArrayListQuery(String _campo, ArrayList<ContentValues> _informacion)
 * 			Funcion encargada de recibir un array List con los querys a ejecutar, se ejecuta un replace de DELETE a DELETE FROM
 * 			de esta forma se garantiza que la sentencia SQL DELETE se ejecute efectivamente en la base de datos SQLite, por ultimo
 * 			retorna la cantidad de ejecuciones que realizo.
 ***********************************************************************************************************************************/



package sistema;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lecturas.sypelc.mobilelecturas.InicioSession;

public class SQLite {
    private static Archivos ArchSQL;
    private static String N_BD = null;
    private static final int VERSION_BD = 5;

    private BDHelper nHelper;
    private Context nContexto;
    private String Directorio;
    private SQLiteDatabase nBD;

    private boolean ValorRetorno;

    private static class BDHelper extends SQLiteOpenHelper{

        public BDHelper(Context context) {
            super(context, N_BD, null, VERSION_BD);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            /**************************************************************************************************************************/
            /************************Creacion de las tablas basicas para el correcto funcionamiento del sistema************************/
            /**************************************************************************************************************************/

            //Tabla con los usuarios del sistema
            db.execSQL(	"CREATE TABLE param_usuarios(   id_inspector    INTEGER NOT NULL PRIMARY KEY,"
                                                    + "	nombre	        VARCHAR(255) NOT NULL,"
                                                    + "	perfil 		    INTEGER NOT NULL)");

            db.execSQL("INSERT INTO param_usuarios('id_inspector','nombre','perfil') VALUES ('0','Administrador',0)");

            //Tabla con los datos de configuracion
            db.execSQL("CREATE TABLE    param_configuracion (item   VARCHAR(100) PRIMARY KEY, " +
                                                            "valor  VARCHAR(100) NOT NULL, " +
                                                            "nivel  INTEGER NOT NULL)");

            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Servidor','http://192.168.1.37',0) ");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Puerto','80',0) ");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Modulo','DesarrolloLecturas/ServerMobile',0) ");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Web_Service','WS_Lecturas.php?wsdl',0)");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Impresora','Sin Asignar',1)");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Version_Software','1.0',0)");
            db.execSQL("INSERT INTO     param_configuracion (item,valor,nivel) VALUES ('Version_BD','1.0',0)");

            //Tabla con los datos de configuracion
            db.execSQL("CREATE TABLE    param_anomalias (id_anomalia            INTEGER NOT NULL PRIMARY KEY, " +
                                                        "descripcion            VARCHAR(500) NOT NULL, " +
                                                        "aplica_residencial     INTEGER NOT NULL," +
                                                        "aplica_no_residencial  INTEGER NOT NULL," +
                                                        "lectura                INTEGER NOT NULL," +
                                                        "mensaje                VARCHAR(1) NOT NULL," +
                                                        "foto                   INTEGER NOT NULL)");

            //Tabla con los datos de configuracion
            db.execSQL("CREATE TABLE    param_critica   (minimo                 NUMERIC(15,7) NOT NULL," +
                                                        "maximo                 NUMERIC(15,7) NOT NULL," +
                                                        "descripcion            VARCHAR(255) PRIMARY KEY)");

            db.execSQL("CREATE TABLE    param_municipios(id_municipio           INTEGER NOT NULL PRIMARY KEY," +
                                                        "municipio              VARCHAR(255) NOT NULL)");

            db.execSQL("CREATE TABLE    maestro_rutas   (id_inspector           INTEGER NOT NULL," +
                                                        "ruta                   VARCHAR(255) NOT NULL," +
                                                        "fecha_cargue           TIMESTAMP NOT NULL DEFAULT current_timestamp," +
                                                        "PRIMARY KEY(id_inspector,ruta))");

            db.execSQL("CREATE TABLE     maestro_clientes(id_serial              INTEGER NOT NULL PRIMARY KEY," +
                                                        "cuenta                 NUMERIC(15,0) NOT NULL," +
                                                        "nombre                 VARCHAR(255) NOT NULL," +
                                                        "direccion              VARCHAR(255) NOT NULL," +
                                                        "marca_medidor          VARCHAR(50) NOT NULL," +
                                                        "serie_medidor          VARCHAR(50) NOT NULL," +
                                                        "digitos_medidor        INTEGER NOT NULL," +
                                                        "tipo_lectura           VARCHAR(10) NOT NULL," +
                                                        "lectura                INTEGER NOT NULL," +
                                                        "anomalia               INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE    toma_lectura    (id                     INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                        "id_serial              INTEGER NOT NULL," +
                                                        "lectura                INTEGER NOT NULL," +
                                                        "anomalia               INTEGER NOT NULL," +
                                                        "critica                NUMERIC(15,7) NOT NULL," +
                                                        "mensaje                VARCHAR(255)," +
                                                        "fecha_toma             TIMESTAMP NOT NULL DEFAULT current_timestamp)");

            db.execSQL(	"CREATE TRIGGER tg_fecha_cargue AFTER INSERT ON maestro_rutas FOR EACH ROW BEGIN " +
                        "	UPDATE maestro_rutas SET fecha_cargue = datetime('now','localtime') " +
                        "   WHERE id_inspector = NEW.id_inspector AND ruta = NEW.ruta;" +
                        "END;");

            db.execSQL(	"CREATE TRIGGER tg_fecha_toma AFTER INSERT ON toma_lectura FOR EACH ROW BEGIN " +
                        "	UPDATE toma_lectura SET fecha_toma = datetime('now','localtime') " +
                        "   WHERE id = NEW.id;" +
                        "END;");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("UPDATE amd_configuracion SET valor = '2.2' WHERE item = 'version'");
			
			/*db.execSQL(	"CREATE TRIGGER tg_fecha_impresion AFTER INSERT ON dig_impresiones_inf FOR EACH ROW BEGIN " +
						"	UPDATE dig_impresiones_inf SET fecha_imp=datetime('now','localtime') WHERE solicitud = NEW.solicitud AND id_impresion = NEW.id_impresion;" +
						"END;");
			
			db.execSQL( "CREATE TABLE dig_impresiones_inf(   solicitud 		VARCHAR(20) NOT NULL," +
															"id_impresion 	INTEGER NOT NULL," +
															"nombre_archivo	VARCHAR(100)," +
															"fecha_imp		TIMESTAMP NOT NULL DEFAULT current_timestamp," +
															"PRIMARY KEY(solicitud, id_impresion));");*/
        }
    }

    public SQLite (Context c, String CurrentDirectory){
        this.nContexto = c;
        this.Directorio = CurrentDirectory;
        SQLite.N_BD = this.Directorio + File.separator + InicioSession.name_database;
        ArchSQL = new Archivos(this.nContexto, this.Directorio, 10);
        if(!ArchSQL.ExistFolderOrFile(this.Directorio)){
            ArchSQL.MakeDirectory();
        }
    }

    /*public boolean SQLString(String Query){
        ValorRetorno = false;
        try{
            nBD.execSQL(Query);
            ValorRetorno = true;
        }catch(Exception e){
            ValorRetorno = false;
        }
        return ValorRetorno;
    }*/





    private SQLite abrir(){
        nHelper = new BDHelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;
    }

    private void cerrar() {
        nHelper.close();
    }



    /**Funcion para ejecutar una sentencia SQL recibida por parametro
     * @param _sql	->Sentencia SQL a ejecutar
     * @return		->true en caso de ejecutarse correctamente, false en otros casos
     */
    public boolean EjecutarSQL(String _sql){
        boolean _retorno = false;
        abrir();
        try{
            nBD.execSQL(_sql);
            _retorno = true;;
        }catch(Exception e){
        }
        cerrar();
        return _retorno;
    }


    /**Funcion para realizar INSERT
     * @param _tabla 		-> tabla a la cual se va a realizar el INSERT
     * @param _informacion	-> informacion que se va a insertar
     * @return				-> true si se realizo el insert correctamente, false en otros casos
     */
    public boolean InsertRegistro(String _tabla, ContentValues _informacion){
        abrir();
        ValorRetorno = false;
        try{
            if(nBD.insert(_tabla,null, _informacion)>=0){
                ValorRetorno = true;
            }
        }catch(Exception e){
        }
        cerrar();
        return ValorRetorno;
    }


    /**
     *
     * @param Tabla
     * @param Informacion
     * @param Condicion
     * @return
     */
    public boolean UpdateRegistro(String Tabla, ContentValues Informacion, String Condicion){
        ValorRetorno = false;
        abrir();
        try{
            if(nBD.update(Tabla, Informacion, Condicion, null)>=0){
                ValorRetorno = true;
            }
        }catch(Exception e){
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion para realizar un insert en caso de no existir el registro o update en caso de existir
     * @param _tabla		->tabla sobre la cual se va a operar
     * @param _informacion	->informacion que se va a insertar o actualizar
     * @param _condicion	->Condicion que debe cumplirse para realizar el update y/o insert
     * @return				->String con el mensaje de retorno, ya puede ser insert/update realizado o no realizado.
     */
    public String InsertOrUpdateRegistro(String _tabla, ContentValues _informacion, String _condicion){
        String _retorno = "Sin acciones";
        if(!this.ExistRegistros(_tabla, _condicion)){
            if(this.InsertRegistro(_tabla, _informacion)){
                _retorno = "Registro ingresado en "+_tabla;
            }else{
                _retorno = "Error al ingresar el registro en "+_tabla;
            }
        }else{
            if(this.UpdateRegistro(_tabla, _informacion, _condicion)){
                _retorno = "Registro actualizado en "+_tabla;
            }else{
                _retorno = "Error al actualizar el registro en "+_tabla;
            }
        }
        return _retorno;
    }


    /**Funcion para eliminar un registro de una tabla en particular
     * @param _tabla		->tabla sobre la cual se va a trabajar
     * @param _condicion	->condicion que debe cumplirse para ejecutar el delete respectivo
     * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
     */
    public boolean DeleteRegistro(String _tabla, String _condicion){
        ValorRetorno = false;
        abrir();
        try{
            if(nBD.delete(_tabla, _condicion,null)>0){
                ValorRetorno = true;
            }
        }catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion encargada de realizar una consulta y retornarla con un array list de content values, similar a un array de diccionarios
     * @param _tabla		->tabla sobre la cual va a correr la consulta
     * @param _campos		->campos que se van a consultar
     * @param _condicion	->condicion para filtrar la consulta
     * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
     */
    public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion){
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _registro.put(_columnas[i], c.getString(i));
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }


    /**Funcion para realizar la consulta de un registro, retorna un contentvalues con la informacion consultada
     * @param _tabla		->tabla sobre la cual se va a ejecutar la consulta
     * @param _campos		->campos que se quieren consultar
     * @param _condicion	->condicion para ejecutar la consulta
     * @return				-->ContentValues con la informacion del registro producto de la consulta
     */
    public ContentValues SelectDataRegistro(String _tabla, String _campos, String _condicion){
        ContentValues _query = new ContentValues();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" LIMIT 1", null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                //ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _query.put(_columnas[i], c.getString(i));
                }
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }


    //Relizar la consulta teniendo en cuenta varios JOIN a la izquierda
    public ArrayList<ContentValues> SelectNJoinLeftData(String _tabla, String _campos, String _join_left[], String _on_left[], String _condicion){
        String Query = "";
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Query = "SELECT DISTINCT "+ _campos + " FROM "+ _tabla;
            for(int i=0;i<_join_left.length;i++){
                Query += " LEFT JOIN " +_join_left[i] + " ON "+_on_left[i];
            }
            Query += " WHERE "+ _condicion;
            Cursor c = nBD.rawQuery(Query, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    if(c.getString(i) == null){
                        _registro.put(_columnas[i], "");
                    }else{
                        _registro.put(_columnas[i], c.getString(i));
                    }
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }



    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un entero
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public int IntSelectShieldWhere(String _tabla, String _campo, String _condicion){
        int intRetorno = -1;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                intRetorno = c.getInt(0);
            }
        }
        catch(Exception e){
            intRetorno = -1;
        }
        cerrar();
        return intRetorno;
    }


    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un double
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public double DoubleSelectShieldWhere(String _tabla, String _campo, String _condicion){
        double intRetorno = -1;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                intRetorno = c.getDouble(0);
            }
        }
        catch(Exception e){
            intRetorno = -1;
        }
        cerrar();
        return intRetorno;
    }


    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un String
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public String StrSelectShieldWhere(String _tabla, String _campo, String _condicion){
        String strRetorno = null;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                strRetorno = c.getString(0);
            }
        }
        catch(Exception e){
            e.toString();
            strRetorno = null;
        }
        cerrar();
        return strRetorno;
    }


    /**Funcion retorna la cantidad de registros de una tabla que cumplen la condicion recibida por parametro
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public int CountRegistrosWhere(String _tabla, String _condicion){
        int ValorRetorno = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT count(*) FROM "+ _tabla +" WHERE "+ _condicion, null);
            c.moveToFirst();
            ValorRetorno = c.getInt(0);
        }
        catch(Exception e){
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion que retorna true o falso segun existan o no registros que cumplan la condicion recibida por parametro
     * @param _tabla		->Tabla sobre la cual se va trabajar
     * @param _condicion	->Condicion de filtro
     * @return
     */
    public boolean ExistRegistros(String _tabla, String _condicion){
        ValorRetorno = false;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + _tabla +" WHERE " + _condicion , null);
            c.moveToFirst();
            if(c.getDouble(0)>0)
                ValorRetorno = true;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion que retorna la cantidad de minutos transcurridos desde la fecha actual y la recibida por parametro
     * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
     * @return 			->String con el resultado en minutos
     */
    public String MinutesBetweenDateAndNow(String _oldDate){
        String _retorno = "";
        int _minutos = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
            c.moveToFirst();
            _retorno = c.getString(0);
            _minutos = Integer.parseInt(_retorno)/60;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return String.valueOf(_minutos);
    }


    /**Funcion que retorna la cantidad de minutos transcurridos entre las fechas recibidas por parametro
     * @param _newDate	->fecha mas reciente contra la cual se quiere caldular la diferencia
     * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
     * @return 			->Strig con el resultado en minutos
     */
    public String MinutesBetweenDates(String _newDate, String _oldDate){
        String _retorno = "";
        int _minutos = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT strftime('%s','"+_newDate+"')-strftime('%s','"+_oldDate+"') as segundos", null);
            c.moveToFirst();
            _retorno = c.getString(0);
            _minutos = Integer.parseInt(_retorno)/60;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return String.valueOf(_minutos);
    }
}
