package lecturas.sypelc.mobilelecturas;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import Adapter.RutasAdpater;
import Adapter.RutasData;
import sistema.SQLite;

/**
 * Created by SypelcDesarrollo on 04/02/2015.
 */
public class InformacionRutas extends Activity implements OnItemClickListener{

    private String FolderAplicacion;
    private String ruta;
    private String totalPendientes;
    private String totalLeidas;
    private String totalRutas;

    ListView listadoRutas;
    SQLite sqlConsulta;

    RutasAdpater listadoRutasAdapter;
    ArrayList<RutasData> arrayListadoRutas = new ArrayList<>();

    private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
    private ContentValues _tempRegistro 		= new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_rutas);

        Bundle bundle = getIntent().getExtras();
        this.FolderAplicacion= bundle.getString("FolderAplicacion");

        sqlConsulta = new SQLite(this, this.FolderAplicacion);
        listadoRutasAdapter = new RutasAdpater(InformacionRutas.this, arrayListadoRutas);

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
        listadoRutas.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_informacion_rutas, menu);
        return true;
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
