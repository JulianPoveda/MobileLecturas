package lecturas.sypelc.mobilelecturas;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import Adapter.AdaptadorFourItems;
import Adapter.DetalleFourItems;
import clases.ClassTomarLectura;


public class FormBuscar extends ActionBarActivity implements TextWatcher, OnItemSelectedListener, OnItemClickListener {
    private EditText                    _txtBuscar;
    private Spinner                     _cmbFiltro;
    private ListView                    _lstClientes;
    private ArrayList<String>           ArrayFiltro;
    private ArrayAdapter<String>        AdaptadorFiltro;

    private AdaptadorFourItems          AdaptadorUsuarios;
    private ArrayList<DetalleFourItems> ArrayUsuarios       = new ArrayList<DetalleFourItems>();

    private ArrayList<ContentValues>    _tempTabla;
    private ContentValues               _tempRegistro;

    private ClassTomarLectura           FcnLectura;

    private String                      clienteSeleccionado;
    private String                      _cuenta;
    private String                      _medidor;
    private String                      _nombre;
    private String                      _direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_form_buscar);

        this.FcnLectura         = new ClassTomarLectura(this, FormInicioSession.path_files_app);

        this._txtBuscar     = (EditText) findViewById(R.id.BuscarTxtBuscar);
        this._cmbFiltro     = (Spinner) findViewById(R.id.BuscarCmbFiltro);
        this._lstClientes   = (ListView) findViewById(R.id.BuscarLstClientes);
        this._tempTabla     = new ArrayList<ContentValues>();
        this.ArrayFiltro    = new ArrayList<String>();

        this.ArrayFiltro.clear();
        this.ArrayFiltro.add("Cuenta");
        this.ArrayFiltro.add("Medidor");
        this.ArrayFiltro.add("Nombre");
        this.ArrayFiltro.add("Direccion");



        this.AdaptadorFiltro    = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayFiltro);
        this._cmbFiltro.setAdapter(this.AdaptadorFiltro);

        this._tempTabla = this.FcnLectura.ListaClientes("",false);
        this.ArrayUsuarios.clear();
        for(int i=0;i<this._tempTabla.size();i++){
            this._tempRegistro  = this._tempTabla.get(i);
            ArrayUsuarios.add(new DetalleFourItems( this._tempRegistro.getAsString("cuenta"),
                                                    this._tempRegistro.getAsString("serie_medidor"),
                                                    this._tempRegistro.getAsString("nombre"),
                                                    this._tempRegistro.getAsString("direccion")));
        }
        this.AdaptadorUsuarios  = new AdaptadorFourItems(this, ArrayUsuarios);
        this._lstClientes.setAdapter(this.AdaptadorUsuarios);
        registerForContextMenu(this._lstClientes);
        _lstClientes.setOnItemClickListener(this);
        this.AdaptadorUsuarios.notifyDataSetChanged();
        this._txtBuscar.addTextChangedListener(this);

        this._cmbFiltro.setOnItemSelectedListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        this.clienteSeleccionado = ArrayUsuarios.get(info.position).getItem1();
        switch(v.getId()){
            case R.id.BuscarLstClientes:
                menu.setHeaderTitle("Cuenta" +" "+this.clienteSeleccionado);
                super.onCreateContextMenu(menu, v, menuInfo);
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_lista_buscar, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.BuscarMenuIniciar:

                return true;

            case R.id.BuscarMenuSincronizar:

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
        switch(parent.getId()){
            case R.id.BuscarLstClientes:
                this._cuenta    = ArrayUsuarios.get(position).getItem1();
                this._medidor   = ArrayUsuarios.get(position).getItem2();
                this._nombre    = ArrayUsuarios.get(position).getItem3();
                this._direccion = ArrayUsuarios.get(position).getItem4();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form_buscar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterTextChanged(Editable s) {
        this.AdaptadorUsuarios.Filtrar(this._cmbFiltro.getSelectedItemPosition(),s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //What you want to do
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.BuscarCmbFiltro:
                this.AdaptadorUsuarios.Filtrar(this._cmbFiltro.getSelectedItemPosition(), this._txtBuscar.getText().toString());
                break;

            case R.id.LecturaSpnTipoUso:
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
