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
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import Adapter.RutasAdapter;
import Adapter.RutasData;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 04/02/2015.
 */
public class FormInformacionRutas extends Activity implements OnItemClickListener{
    private String FolderAplicacion;
    private String ruta_seleccionada;
    private String ruta;
    private String totalPendientes;
    private String totalLeidas;
    private String totalRutas;

    private Intent      new_form;
    private ListView    listadoRutas;
    private SQLite      sqlConsulta;

    private RutasAdapter listadoRutasAdapter;
    private ArrayList<RutasData> arrayListadoRutas = new ArrayList<>();

    private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
    private ContentValues _tempRegistro 		= new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_rutas);

        Bundle bundle = getIntent().getExtras();
        this.FolderAplicacion= bundle.getString("FolderAplicacion");

        sqlConsulta = new SQLite(this, this.FolderAplicacion);
        listadoRutasAdapter = new RutasAdapter(FormInformacionRutas.this, arrayListadoRutas);

        listadoRutas = (ListView)findViewById(R.id.InfoListRutas);
        listadoRutas.setAdapter(listadoRutasAdapter);

        arrayListadoRutas.clear();

        this._tempTabla = sqlConsulta.SelectData("maestro_rutas","ruta","ruta is not null");
        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro = this._tempTabla.get(i);
            Integer totalR = sqlConsulta.CountRegistrosWhere("maestro_clientes","ruta='"+this._tempRegistro.getAsString("ruta")+"'");
            Integer totalP = sqlConsulta.CountRegistrosWhere("maestro_clientes","ruta='"+this._tempRegistro.getAsString("ruta")+"' AND estado='P'");
            Integer totalL = totalR - totalP;
            arrayListadoRutas.add(new RutasData(this._tempRegistro.getAsString("ruta"),String.valueOf(totalP),String.valueOf(totalL),String.valueOf(totalR)));
        }
        listadoRutasAdapter.notifyDataSetChanged();
        registerForContextMenu(this.listadoRutas);
        listadoRutas.setOnItemClickListener(this);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_informacion_rutas, menu);
        return true;
    }*/

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
                this.new_form = new Intent(this, FormTomarLectura.class);
                this.new_form.putExtra("Ruta",this.ruta_seleccionada);
                startActivity(this.new_form);
                return true;

            case R.id.RutasMenuSincronizar:

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
        switch(parent.getId()){
            case R.id.InfoListRutas:
                this.ruta 	         = arrayListadoRutas.get(position).getCodigoRuta();
                this.totalPendientes = arrayListadoRutas.get(position).getTotalPendientes();
                this.totalLeidas     = arrayListadoRutas.get(position).getTotalLeidas();
                this.totalRutas      = arrayListadoRutas.get(position).getTotalRutas();
                break;
        }
    }
}
