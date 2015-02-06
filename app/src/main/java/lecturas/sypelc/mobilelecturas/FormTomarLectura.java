package lecturas.sypelc.mobilelecturas;

import android.content.ContentValues;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import clases.ClassAnomalia;
import clases.ClassTomarLectura;


public class FormTomarLectura extends ActionBarActivity implements OnTouchListener, OnClickListener, OnItemSelectedListener{

    private ClassTomarLectura   FcnLectura;
    private ClassAnomalia       FcnAnomalia;

    private ViewFlipper _viewFlipper;
    private TextView    _lblCuenta, _lblNombre, _lblDireccion, _lblRuta, _lblMedidor, _lblTipoUso;
    private TextView    _lblAnomalia, _lblLectura1, _lblLectura2, _lblLectura3, _lblMensaje;
    private EditText    _txtLectura1, _txtLectura2, _txtLectura3, _txtMensaje;
    private Spinner     _cmbTipoUso, _cmbAnomalia;
    private Button      _btnGuardar, _btnImprimir;

    private String      _ruta;
    private int         _consecutivo_ruta;
    private float         init_x;

    private ContentValues           DetalleAnomalia;
    private ArrayList<String>       ArrayAnomalias;
    private ArrayList<String>       ArrayUso;
    private ArrayAdapter<String>    AdaptadorAnomalias;
    private ArrayAdapter<String>    AdaptadorUso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_lectura);
        Bundle bundle   = getIntent().getExtras();
        this._ruta      = bundle.getString("Ruta");

        this.FcnLectura     = new ClassTomarLectura(this,this._ruta);
        this.FcnAnomalia    = new ClassAnomalia(this);

        this._lblCuenta     = (TextView) findViewById(R.id.LecturaTxtCuenta);
        this._lblNombre     = (TextView) findViewById(R.id.LecturaTxtNombre);
        this._lblDireccion  = (TextView) findViewById(R.id.LecturaTxtDireccion);
        this._lblRuta       = (TextView) findViewById(R.id.LecturaTxtRuta);
        this._lblMedidor    = (TextView) findViewById(R.id.LecturaTxtMedidor);

        this._lblLectura1   = (TextView) findViewById(R.id.LecturaTxtLectura1);
        this._lblLectura2   = (TextView) findViewById(R.id.LecturaTxtLectura2);
        this._lblLectura3   = (TextView) findViewById(R.id.LecturaTxtLectura3);

        this._txtLectura1   = (EditText) findViewById(R.id.LecturaEditLectura1);
        this._txtLectura2   = (EditText) findViewById(R.id.LecturaEditLectura2);
        this._txtLectura3   = (EditText) findViewById(R.id.LecturaEditLectura3);
        this._txtMensaje    = (EditText) findViewById(R.id.LecturaEditMensaje);

        this._cmbAnomalia   = (Spinner) findViewById(R.id.LecturaSpnAnomalia);
        this._cmbTipoUso    = (Spinner) findViewById(R.id.LecturaSpnTipoUso);

        this._btnGuardar    = (Button) findViewById(R.id.LecturasBtnGuardar);
        this._btnImprimir   = (Button) findViewById(R.id.LecturasBtnImprimir);

        this._viewFlipper = (ViewFlipper) findViewById(R.id.InicioViewFlipper);
        this._viewFlipper.setOnTouchListener(this);

        if(this.FcnLectura.getNextDatosUsuario()) {
            this.MostrarInformacionBasica();
            this.MostrarInputLectura(true);
        }

        this.ArrayAnomalias     = this.FcnAnomalia.listarAnomalias(this.FcnLectura.getTipo_uso());
        this.AdaptadorAnomalias = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayAnomalias);
        this._cmbAnomalia.setAdapter(this.AdaptadorAnomalias);

        /*this.ArrayUso       = this.FcnParametros.listaUsos();
        this.AdaptadorUso   = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayUso);
        this._cmbTipoUso.setAdapter(this.AdaptadorUso);*/

        this._cmbTipoUso.setOnItemSelectedListener(this);
        this._cmbAnomalia.setOnItemSelectedListener(this);

        this._btnGuardar.setOnClickListener(this);
        this._btnImprimir.setOnClickListener(this);
    }


    private void MostrarInformacionBasica(){
        this._lblCuenta.setText(this.FcnLectura.getCuenta()+"");
        this._lblNombre.setText(this.FcnLectura.getNombre());
        this._lblDireccion.setText(this.FcnLectura.getDireccion());
        this._lblRuta.setText(this.FcnLectura.getRuta());
        this._lblMedidor.setText(this.FcnLectura.getMarca_medidor()+" "+this.FcnLectura.getSerie_medidor());
    }


    private void MostrarInputLectura(boolean _caso){
        if(_caso){
            if(this.FcnLectura.getId_serial1() != -1){
                this._lblLectura1.setVisibility(View.VISIBLE);
                this._txtLectura1.setVisibility(View.VISIBLE);
                if(this.FcnLectura.getTipo_energia1().equals("A")){
                    this._lblLectura1.setText("Activa");
                }else if(this.FcnLectura.getTipo_energia1().equals("R")){
                    this._lblLectura1.setText("Reactiva");
                }else{
                    this._lblLectura1.setText("No Valido");
                }
            }else{
                this._lblLectura2.setVisibility(View.INVISIBLE);
                this._txtLectura2.setVisibility(View.INVISIBLE);
            }

            if(this.FcnLectura.getId_serial2() != -1){
                this._lblLectura2.setVisibility(View.VISIBLE);
                this._txtLectura2.setVisibility(View.VISIBLE);
                if(this.FcnLectura.getTipo_energia2().equals("A")){
                    this._lblLectura2.setText("Activa");
                }else if(this.FcnLectura.getTipo_energia2().equals("R")){
                    this._lblLectura2.setText("Reactiva");
                }else{
                    this._lblLectura2.setText("No Valido");
                }
            }else{
                this._lblLectura2.setVisibility(View.INVISIBLE);
                this._txtLectura2.setVisibility(View.INVISIBLE);
            }

            if(this.FcnLectura.getId_serial3() != -1){
                this._lblLectura3.setVisibility(View.VISIBLE);
                this._txtLectura3.setVisibility(View.VISIBLE);
                if(this.FcnLectura.getTipo_energia3().equals("A")){
                    this._lblLectura3.setText("Activa");
                }else if(this.FcnLectura.getTipo_energia3().equals("R")){
                    this._lblLectura3.setText("Reactiva");
                }else{
                    this._lblLectura3.setText("No Valido");
                }
            }else{
                this._lblLectura3.setVisibility(View.INVISIBLE);
                this._txtLectura3.setVisibility(View.INVISIBLE);
            }
        }else{
            this._lblLectura1.setVisibility(View.INVISIBLE);
            this._txtLectura1.setVisibility(View.INVISIBLE);
            this._lblLectura2.setVisibility(View.INVISIBLE);
            this._txtLectura2.setVisibility(View.INVISIBLE);
            this._lblLectura3.setVisibility(View.INVISIBLE);
            this._txtLectura3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tomar_lectura, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.LecturaMenuBuscar:
                break;

            case R.id.LecturaMenuFoto:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LecturasBtnGuardar:
                break;

            case R.id.LecturasBtnImprimir:
                break;

            default:
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.LecturaSpnAnomalia:
                //this.DetalleAnomalia = this.FcnAnomalia.validarDatosAnomalia(this._cmbAnomalia.getSelectedItem().toString());
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
                init_x = event.getX();
                return true;
            case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
                float distance =init_x-event.getX();

                if(distance>0){
                    if(this.FcnLectura.getNextDatosUsuario()) {
                        this.MostrarInformacionBasica();
                        this.MostrarInputLectura(true);
                    }
                }

                if(distance<0){
                    if(this.FcnLectura.getBackDatosUsuario()) {
                        this.MostrarInformacionBasica();
                        this.MostrarInputLectura(true);
                    }
                }

            default:
                break;
        }
        return false;
    }

}
