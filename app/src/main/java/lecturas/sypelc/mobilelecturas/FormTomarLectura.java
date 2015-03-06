package lecturas.sypelc.mobilelecturas;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;

import clases.ClassAnomalias;
import clases.ClassFormatos;
import clases.ClassSession;
import dialogos.DialogoInformativo;
import sistema.Archivos;
import Object.TomaLectura;
import Object.UsuarioLeido;


public class FormTomarLectura extends ActionBarActivity implements OnClickListener, OnItemSelectedListener{
    static int 				    INICIAR_CAMARA			= 1;
    static int                  FROM_BUSCAR             = 2;

    private Intent 			    IniciarCamara;
    private Intent              new_form;



    private ClassSession        FcnSession;
    private TomaLectura         FcnLectura;
    private ClassAnomalias      FcnAnomalias;
    private ClassFormatos       FcnFormatos;

    private ViewFlipper _viewFlipper;
    private TextView    _lblCuenta, _lblNombre, _lblDireccion, _lblRuta, _lblMedidor, _lblLectura1, _lblLectura2, _lblLectura3;
    private EditText    _txtLectura1, _txtLectura2, _txtLectura3, _txtMensaje;
    private Spinner     _cmbTipoUso, _cmbAnomalia;
    private Button      _btnGuardar, _btnSiguiente, _btnAnterior;


    //private int         backupId;
    //private boolean     flag_search;

    private String                  _ruta;
    private float                   init_x;

    private ArrayAdapter<String>    AdaptadorAnomalias;
    private ArrayAdapter<String>    AdaptadorUso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_lectura);
        Bundle bundle       = getIntent().getExtras();
        this._ruta          = bundle.getString("Ruta");

        this.IniciarCamara	= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        this.FcnSession     = ClassSession.getInstance(this);
        this.FcnAnomalias   = ClassAnomalias.getInstance(this);
        this.FcnLectura     = new TomaLectura(this, this._ruta);
        this.FcnFormatos    = new ClassFormatos(this, false);

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
        this._btnSiguiente  = (Button) findViewById(R.id.LecturaBtnSiguiente);
        this._btnAnterior   = (Button) findViewById(R.id.LecturaBtnAnterior);

        //this._viewFlipper = (ViewFlipper) findViewById(R.id.InicioViewFlipper);
        //this._viewFlipper.setOnTouchListener(this);

        if(this.FcnLectura.getDatosUsuario(true)){
            this.MostrarInformacionBasica();
            this._btnGuardar.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        }

        this.AdaptadorAnomalias = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.FcnAnomalias.getAnomalias(this.FcnLectura.getInfUsuario().getTipo_uso()));
        this._cmbAnomalia.setAdapter(this.AdaptadorAnomalias);

        this._cmbTipoUso.setOnItemSelectedListener(this);
        this._cmbAnomalia.setOnItemSelectedListener(this);

        this._btnGuardar.setOnClickListener(this);
        this._btnAnterior.setOnClickListener(this);
        this._btnSiguiente.setOnClickListener(this);
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
                this.new_form = new Intent(this, FormBuscar.class);
                startActivityForResult(this.new_form, FROM_BUSCAR);
                break;

            case R.id.LecturaMenuFoto:
                File imagesFolder   = new File(FormInicioSession.path_files_app, FormInicioSession.sub_path_pictures);
                File image          = new File( imagesFolder,
                                                this.FcnLectura.getInfUsuario().getCuenta()+"_"+this.FcnLectura.getInfUsuario().getCountFotos()+".jpeg");

                Uri uriSavedImage = Uri.fromFile(image);
                this.IniciarCamara.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(IniciarCamara, INICIAR_CAMARA);
                break;

            case R.id.LecturaMenuReImprimir:
                //this.FcnFormatos.FormatoPrueba();
                //this.FcnFormatos.ActaLectura();
                /*this.FcnFormatos.ActaLectura(   this.lectura1,
                                                this.FcnLectura.getCuenta(),
                                                "FALTA",
                                                this.FcnLectura.getNombre(),
                                                this.FcnLectura.getDireccion(),
                                                this.FcnLectura.getSerie_medidor()+"-"+this.FcnLectura.getMarca_medidor(),
                                                ClassUsuario.getInstance(this).getCodigo());*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void MostrarInformacionBasica(){
        this._lblRuta.setText(this.FcnLectura.getInfUsuario().getRuta());
        this._lblCuenta.setText(this.FcnLectura.getInfUsuario().getCuenta() + "");
        this._lblMedidor.setText(this.FcnLectura.getInfUsuario().getMarca_medidor()+" "+this.FcnLectura.getInfUsuario().getSerie_medidor());
        this._lblNombre.setText(this.FcnLectura.getInfUsuario().getNombre());
        this._lblDireccion.setText(this.FcnLectura.getInfUsuario().getDireccion());

        this._lblLectura1.setText(this.FcnLectura.getInfUsuario().getTipo_energia1());
        this._lblLectura2.setText(this.FcnLectura.getInfUsuario().getTipo_energia2());
        this._lblLectura3.setText(this.FcnLectura.getInfUsuario().getTipo_energia3());


        if(this.FcnLectura.getInfUsuario().isView_tipo_energia1() && this.FcnLectura.getInfUsuario().isNeedLectura()){
            this._lblLectura1.setVisibility(View.VISIBLE);
            this._txtLectura1.setVisibility(View.VISIBLE);
        }else{
            this._lblLectura1.setVisibility(View.INVISIBLE);
            this._txtLectura1.setVisibility(View.INVISIBLE);
        }

        if(this.FcnLectura.getInfUsuario().isView_tipo_energia2() && this.FcnLectura.getInfUsuario().isNeedLectura()){
            this._lblLectura2.setVisibility(View.VISIBLE);
            this._txtLectura2.setVisibility(View.VISIBLE);
        }else{
            this._lblLectura2.setVisibility(View.INVISIBLE);
            this._txtLectura2.setVisibility(View.INVISIBLE);
        }

        if(this.FcnLectura.getInfUsuario().isView_tipo_energia3() && this.FcnLectura.getInfUsuario().isNeedLectura()){
            this._lblLectura3.setVisibility(View.VISIBLE);
            this._txtLectura3.setVisibility(View.VISIBLE);
        }else{
            this._lblLectura3.setVisibility(View.INVISIBLE);
            this._txtLectura3.setVisibility(View.INVISIBLE);
        }

        this._btnGuardar.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._cmbAnomalia.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._txtLectura1.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._txtLectura2.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._txtLectura3.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._txtMensaje.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
        this._cmbTipoUso.setEnabled(!this.FcnLectura.getInfUsuario().isLeido());
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LecturaBtnAnterior:
                if(this.FcnLectura.getDatosUsuario(false)) {
                    this._cmbAnomalia.setSelection(0);
                    this._cmbTipoUso.setSelection(0);
                    this.MostrarInformacionBasica();
                }
                break;

            case R.id.LecturaBtnSiguiente:
                if(this.FcnLectura.getDatosUsuario(true)){
                    this._cmbAnomalia.setSelection(0);
                    this._cmbTipoUso.setSelection(0);
                    this.MostrarInformacionBasica();
                }
                break;


            case R.id.LecturasBtnGuardar:
                /*if(this.FcnLectura.getInfUsuario().isNeedFoto() && this.FcnLectura.getInfUsuario().getCountFotos() == 0){
                    this.argumentos.clear();
                    this.argumentos.putString("Titulo","ERROR.");
                    this.argumentos.putString("Mensaje","Falta asociar fotos con la cuenta.");
                    this.dialogo.setArguments(argumentos);
                    this.dialogo.show(getFragmentManager(), "SaveDialog");
                }else */
                if(this.FcnLectura.getInfUsuario().isNeedMensaje() && this._txtMensaje.getText().toString().isEmpty()){

                }else{
                    if(this.FcnLectura.guardarLectura(this._txtLectura1.getText().toString(),
                            this._txtLectura2.getText().toString(),
                            this._txtLectura3.getText().toString(),
                            this._txtMensaje.getText().toString())){
                        this._txtLectura1.setText("");
                        this._txtLectura2.setText("");
                        this._txtLectura3.setText("");

                        if(this.FcnLectura.getInfUsuario().isHaveCritica()){
                            /*this.argumentos.clear();
                            this.argumentos.putString("Titulo","ERROR.");
                            this.argumentos.putString("Mensaje","Se ha generado critica, ingrese la lectura nuevamente.");
                            this.dialogo.setArguments(argumentos);
                            this.dialogo.show(getFragmentManager(), "SaveDialog");*/
                        }

                        if(this.FcnLectura.getInfUsuario().isLeido()){
                            //Si se genero critica tomar foto

                            this._btnGuardar.setEnabled(false);
                            this.FcnFormatos.ActaLectura(   this.FcnLectura.getInfUsuario().getTipo_energia1(),
                                                            this.FcnLectura.getInfUsuario().getLectura1(),
                                                            this.FcnLectura.getInfUsuario().getStrAnomalia(),
                                                            this.FcnLectura.getInfUsuario().getCuenta(),
                                                            this.FcnLectura.getInfUsuario().getMunicipio(),
                                                            this.FcnLectura.getInfUsuario().getNombre(),
                                                            this.FcnLectura.getInfUsuario().getDireccion(),
                                                            this.FcnLectura.getInfUsuario().getSerie_medidor() + "-" + this.FcnLectura.getInfUsuario().getMarca_medidor(),
                                                            ClassSession.getInstance(this).getCodigo());

                            if(this.FcnLectura.getDatosUsuario(true)) {
                                this._cmbAnomalia.setSelection(0);
                                this._cmbTipoUso.setSelection(0);
                                this.MostrarInformacionBasica();
                            }
                        }
                    }else{
                        this.argumentos.clear();
                        this.argumentos.putString("Titulo","ERROR.");
                        this.argumentos.putString("Mensaje","No ha sido posible registrar la lectura.");
                        this.dialogo.setArguments(argumentos);
                        this.dialogo.show(getFragmentManager(), "SaveDialog");
                    }
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.LecturaSpnAnomalia:
                String _anomalia[] = this._cmbAnomalia.getSelectedItem().toString().split("-");

                if(this.FcnLectura.getInfUsuario().getAnomalia_anterior() != Integer.parseInt(_anomalia[0])){
                    this.argumentos.clear();
                    this.argumentos.putString("Titulo","ALERTA.");
                    this.argumentos.putString("Mensaje","La anomalia seleccionada no coincide con la anomalia anterior.");
                    this.dialogo.setArguments(argumentos);
                    this.dialogo.show(getFragmentManager(), "SaveDialog");
                }

                this.FcnLectura.getInfUsuario().setAnomalia(Integer.parseInt(_anomalia[0]),_anomalia[1]);
                this.MostrarInformacionBasica();
                break;

            case R.id.LecturaSpnTipoUso:
                this.FcnLectura.getInfUsuario().setTipo_uso(/*this._cmbTipoUso.getSelectedItem().toString()*/"00");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == RESULT_OK && requestCode == FROM_BUSCAR){
                if(data.getExtras().getBoolean("response")){
                    this.FcnLectura.getInfUsuario().setFlagSearch(true);
                    this.FcnLectura.getInfUsuario().setBackupRuta(this.FcnLectura.getInfUsuario().getRuta());
                    this.FcnLectura.getInfUsuario().setBackupConsecutivo(this.FcnLectura.getInfUsuario().getId_consecutivo());

                    this.FcnLectura.getSearchDatosUsuario(data.getExtras().getString("cuenta"),data.getExtras().getString("medidor"));
                    this._cmbAnomalia.setSelection(0);
                    this._cmbTipoUso.setSelection(0);
                    this.MostrarInformacionBasica();
                }
            }else if(resultCode == RESULT_OK && requestCode == INICIAR_CAMARA){
                this.FcnLectura.getNumeroFotos();
            }
        }catch(Exception e){

        }
    }


    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
                init_x = event.getX();
                return true;
            case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
                float distance =init_x-event.getX();

                if(distance>0){
                    if(this.FcnLectura.getDatosUsuario(true)) {
                        this._cmbAnomalia.setSelection(0);
                        this._cmbTipoUso.setSelection(0);
                        this.MostrarInformacionBasica();
                    }
                }

                if(distance<0){
                    if(this.FcnLectura.getDatosUsuario(false)){
                        this._cmbAnomalia.setSelection(0);
                        this._cmbTipoUso.setSelection(0);
                        this.MostrarInformacionBasica();
                    }
                }

            default:
                break;
        }
        return false;
    }*/

}
