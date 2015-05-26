package lecturas.sypelc.mobilelecturas;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.RutasAdapter;
import Adapter.RutasData;
import async_task.UploadLecturas;
import clases.ClassConfiguracion;
import clases.ClassSession;
import sistema.Bluetooth;
import sistema.SQLite;
import dialogos.showDialogBox;

/**
 * Created by SypelcDesarrollo on 04/02/2015.
 */
public class FormInformacionRutas extends Activity{
    private String FolderAplicacion;
    private String ruta_seleccionada;

    private Intent              new_form;
    private ListView            listadoRutas;
    private SQLite              sqlConsulta;
    private ClassSession        FcnSession;
    private ClassConfiguracion  FcnCfg;
    private Bluetooth           FcnBluetooth;

    private RutasAdapter listadoRutasAdapter;
    private ArrayList<RutasData> arrayListadoRutas = new ArrayList<>();

    private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
    private ContentValues _tempRegistro 		= new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_rutas);

        Bundle bundle = getIntent().getExtras();
        //this.FolderAplicacion= bundle.getString("FolderAplicacion");

        this.FcnSession     = ClassSession.getInstance(this);
        this.FcnCfg         = ClassConfiguracion.getInstance(this);
        this.FcnBluetooth   = Bluetooth.getInstance();

        sqlConsulta = new SQLite(this, FormInicioSession.path_files_app);
        listadoRutasAdapter = new RutasAdapter(FormInformacionRutas.this, arrayListadoRutas);

        listadoRutas = (ListView)findViewById(R.id.InfoListRutas);
        listadoRutas.setAdapter(listadoRutasAdapter);

        arrayListadoRutas.clear();

        this._tempTabla = sqlConsulta.SelectData("maestro_rutas","id_municipio, ruta","id_inspector="+this.FcnSession.getCodigo());
        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            Integer totalR = sqlConsulta.CountRegistrosWhere("maestro_clientes","id_municipio="+this._tempRegistro.getAsInteger("id_municipio")+" AND ruta='"+this._tempRegistro.getAsString("ruta")+"'");
            Integer totalP = sqlConsulta.CountRegistrosWhere("maestro_clientes","id_municipio="+this._tempRegistro.getAsInteger("id_municipio")+" AND ruta='"+this._tempRegistro.getAsString("ruta")+"' AND estado='P'");
            Integer totalL = totalR - totalP;
            arrayListadoRutas.add(new RutasData(this._tempRegistro.getAsString("id_municipio")+"-"+this._tempRegistro.getAsString("ruta"),
                                                String.valueOf(totalP),
                                                String.valueOf(totalL),
                                                String.valueOf(totalR)));
        }
        listadoRutasAdapter.notifyDataSetChanged();
        registerForContextMenu(this.listadoRutas);
    }

    /**Funciones para el control del menu contextual del listview que muestra las ordenes de trabajo**/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        this.ruta_seleccionada = arrayListadoRutas.get(info.position).getCodigoRuta();
        switch(v.getId()){
            case R.id.InfoListRutas:
                menu.setHeaderTitle("Ruta " + this.ruta_seleccionada);
                super.onCreateContextMenu(menu, v, menuInfo);
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_lista_rutas, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.RutasMenuIniciar:
                if(this.FcnCfg.getPrinter().isEmpty()){
                    new showDialogBox().showLoginDialog(this, 2, "ERROR IMPRESORA", "No ha seleccionado la impresora con cual trabajar, verfique los parametros de configuracion.");
                }else if(!this.FcnBluetooth.EnabledBluetoth()) {
                    new showDialogBox().showLoginDialog(this, 2, "ERROR BLUETOOTH", "Error con el bluetooth, verifique que se encuentre encendido.");
                }else{
                    this.new_form = new Intent(this, FormTomarLectura.class);
                    this.new_form.putExtra("Ruta", this.ruta_seleccionada);
                    startActivity(this.new_form);
                }
                return true;

            case R.id.RutasMenuSincronizar:
                new UploadLecturas(this).execute(this.ruta_seleccionada);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
