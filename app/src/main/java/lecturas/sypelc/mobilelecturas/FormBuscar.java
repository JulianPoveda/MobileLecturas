package lecturas.sypelc.mobilelecturas;

import android.content.ContentValues;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import clases.ClassTomarLectura;


public class FormBuscar extends ActionBarActivity{
    private EditText                    _txtBuscar;
    private Spinner                     _cmbFiltro;
    private ListView                    _lstClientes;
    private ArrayList<String>           ArrayFiltro;
    private ArrayList<String>           ArrayUsuarios;
    private ArrayAdapter<String>        AdaptadorFiltro;
    private ArrayAdapter<String>        AdaptadorUsuarios;
    private ArrayList<ContentValues>    _tempTabla;
    private ContentValues               _tempRegistro;

    private ClassTomarLectura           FcnLectura;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modal_form_buscar);

        this._txtBuscar     = (EditText) findViewById(R.id.BuscarTxtBuscar);
        this._cmbFiltro     = (Spinner) findViewById(R.id.BuscarCmbFiltro);
        this._lstClientes   = (ListView) findViewById(R.id.BuscarLstClientes);
        this._tempTabla     = new ArrayList<ContentValues>();
        this.ArrayFiltro    = new ArrayList<String>();

        this.FcnLectura     = new ClassTomarLectura(this, FormInicioSession.path_files_app);


        this.ArrayFiltro.clear();
        this.ArrayFiltro.add("Cuenta Cliente");
        this.ArrayFiltro.add("Serie Medidor");



        this.AdaptadorFiltro = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayFiltro);
        this._cmbFiltro.setAdapter(this.AdaptadorFiltro);

        this.ArrayUsuarios      = this.FcnLectura.ListaClientes("",false);
        this.AdaptadorUsuarios  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayUsuarios);
        this._lstClientes.setAdapter(this.AdaptadorUsuarios);

        this._txtBuscar.addTextChangedListener(new ListenerEditText(this.AdaptadorUsuarios));
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


    private class ListenerEditText implements TextWatcher{
        ArrayAdapter adapter;

        public ListenerEditText(ArrayAdapter listViewAdapter) {
            this.adapter = listViewAdapter;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s.toString());
        }
    }
}
