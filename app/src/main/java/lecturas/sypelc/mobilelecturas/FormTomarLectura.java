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

import async_task.UploadLecturas;
import clases.ClassAnomalia;
import clases.ClassFormatos;
import clases.ClassTomarLectura;
import clases.ClassSession;
import dialogos.DialogoInformativo;
import sistema.Archivos;
import Object.TomaLectura;


public class FormTomarLectura extends ActionBarActivity implements OnTouchListener, OnClickListener, OnItemSelectedListener{
    static int 				    INICIAR_CAMARA			= 1;
    static int                  FROM_BUSCAR             = 2;

    private Intent 			    IniciarCamara;
    private Intent              new_form;
    private DialogoInformativo  dialogo;
    private Bundle              argumentos;

    private ClassFormatos       FcnFormatos;
    //private TomaLectura         FcnTomaLectura;
    private ClassTomarLectura   FcnLectura;
    private ClassAnomalia       FcnAnomalia;
    private Archivos            FcnArchivos;
    private UploadLecturas      Upload;
    private ClassSession        FcnUsuario;

    private ViewFlipper _viewFlipper;
    private TextView    _lblCuenta, _lblNombre, _lblDireccion, _lblRuta, _lblMedidor, _lblTipoUso;
    private TextView    _lblAnomalia, _lblLectura1, _lblLectura2, _lblLectura3, _lblMensaje;
    private EditText    _txtLectura1, _txtLectura2, _txtLectura3, _txtMensaje;
    private Spinner     _cmbTipoUso, _cmbAnomalia;
    private Button      _btnGuardar;


    private int         backupId;
    private boolean     flag_search;

    private String      _ruta;
    private float       init_x;
    double              critica1, critica2, critica3;
    boolean             b_critica1, b_critica2,b_critica3, critica_general;
    private int         intentos;
    private int         lectura1, lectura2,lectura3, lecturaEnviar1, lecturaEnviar2, lecturaEnviar3;
    private String      tipo_uso;

    private ContentValues           DetalleAnomalia;
    private ContentValues           _tempRegistro;
    private ArrayList<String>       ArrayAnomalias;
    private ArrayList<String>       ArrayUso;
    private ArrayAdapter<String>    AdaptadorAnomalias;
    private ArrayAdapter<String>    AdaptadorUso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_lectura);
        Bundle bundle       = getIntent().getExtras();
        this._ruta          = bundle.getString("Ruta");

        this.argumentos     = new Bundle();
        this.dialogo        = new DialogoInformativo();

        this.IniciarCamara	= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //this.FcnTomaLectura = new TomaLectura(this,this._ruta);
        this.FcnLectura     = new ClassTomarLectura(this, this._ruta);
        this.FcnAnomalia    = new ClassAnomalia(this);
        this.FcnFormatos    = new ClassFormatos(this, false);
        this.FcnArchivos    = new Archivos(this, FormInicioSession.path_files_app,10);
        this.Upload         = new UploadLecturas(this);
        this.FcnUsuario     = ClassSession.getInstance(this);

        this.b_critica1     = false;
        this.b_critica2     = false;
        this.b_critica3     = false;

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

        this._viewFlipper = (ViewFlipper) findViewById(R.id.InicioViewFlipper);
        this._viewFlipper.setOnTouchListener(this);

        //if(this.FcnLectura.getDatosUsuario(true)) {
        if(this.FcnLectura.getDatosUsuarioByIdSerial(this.FcnLectura.getId_consecutivo())) {
            this.MostrarInformacionBasica();
            this.intentos       = this.FcnLectura.getIntentos();
            this._tempRegistro  = this.FcnLectura.getLecturasIntento();
            this.lectura1       = this._tempRegistro.getAsInteger("lectura1");
            this.lectura2       = this._tempRegistro.getAsInteger("lectura2");
            this.lectura3       = this._tempRegistro.getAsInteger("lectura3");
            this.MostrarInputLectura(this.FcnLectura.getEstado().equals("P"));
            this._btnGuardar.setEnabled(this.FcnLectura.getEstado().equals("P"));
        }

        this.ArrayAnomalias     = this.FcnAnomalia.listarAnomalias(this.FcnLectura.getTipo_uso());
        this.AdaptadorAnomalias = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayAnomalias);
        this._cmbAnomalia.setAdapter(this.AdaptadorAnomalias);


        this.ArrayUso       = this.FcnAnomalia.listarTiposUso();
        this.AdaptadorUso   = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.ArrayUso);
        this._cmbTipoUso.setAdapter(this.AdaptadorUso);

        this._cmbTipoUso.setOnItemSelectedListener(this);
        this._cmbAnomalia.setOnItemSelectedListener(this);

        this._btnGuardar.setOnClickListener(this);
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
                if(!this.FcnArchivos.ExistFolderOrFile(FormInicioSession.sub_path_pictures,true)){
                    this.FcnArchivos.MakeDirectory(FormInicioSession.sub_path_pictures,true);
                }
                File imagesFolder   = new File(FormInicioSession.path_files_app, FormInicioSession.sub_path_pictures);
                File image          = new File( imagesFolder,
                                                this.FcnLectura.getCuenta()+"_"+this.FcnArchivos.numArchivosInFolderBeginByName(FormInicioSession.sub_path_pictures+"",
                                                                                                                                this.FcnLectura.getCuenta()+"",
                                                                                                                                true)+".jpeg");
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


    private void MostrarInputLectura(boolean _caso){
        if(_caso){
            if(this.FcnLectura.getId_serial1() != -1){
                this._lblLectura1.setVisibility(View.VISIBLE);
                this._txtLectura1.setVisibility(View.VISIBLE);
                this._txtLectura1.setText("");
                if(this.FcnLectura.getTipo_energia1().equals("A")){
                    this._lblLectura1.setText("Activa");
                }else if(this.FcnLectura.getTipo_energia1().equals("R")){
                    this._lblLectura1.setText("Reactiva");
                }else{
                    this._lblLectura1.setText("No Valido");
                }
            }else{
                this._lblLectura2.setVisibility(View.INVISIBLE);
                this._txtLectura1.setVisibility(View.INVISIBLE);
                this._txtLectura1.setText("-1");
            }

            if(this.FcnLectura.getId_serial2() != -1){
                this._lblLectura2.setVisibility(View.VISIBLE);
                this._txtLectura2.setVisibility(View.VISIBLE);
                this._txtLectura2.setText("");
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
                this._txtLectura2.setText("-1");
            }

            if(this.FcnLectura.getId_serial3() != -1){
                this._lblLectura3.setVisibility(View.VISIBLE);
                this._txtLectura3.setVisibility(View.VISIBLE);
                this._txtLectura3.setText("");
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
                this._txtLectura3.setText("-1");
            }
        }else{
            this._lblLectura1.setVisibility(View.INVISIBLE);
            this._txtLectura1.setVisibility(View.INVISIBLE);
            this._lblLectura2.setVisibility(View.INVISIBLE);
            this._txtLectura2.setVisibility(View.INVISIBLE);
            this._lblLectura3.setVisibility(View.INVISIBLE);
            this._txtLectura3.setVisibility(View.INVISIBLE);
            this._txtLectura1.setText("-1");
            this._txtLectura2.setText("-1");
            this._txtLectura3.setText("-1");
        }
    }


    private boolean ValidacionFoto(){
        boolean _retorno= false;
        if(this.DetalleAnomalia.getAsString("foto").equals("t")){
            if(this.FcnArchivos.numArchivosInFolderBeginByName("Fotos",this.FcnLectura.getCuenta()+"", true)>0){
                _retorno = true;
            }else{
                argumentos.clear();
                argumentos.putString("Titulo","ERROR.");
                argumentos.putString("Mensaje","No se encontraron fotos asociadas a la cuenta.");
                this.dialogo.setArguments(argumentos);
                this.dialogo.show(getFragmentManager(), "SaveDialog");
            }
        }else{
            _retorno = true;
        }
        return _retorno;
    }


    private boolean ValidacionMensaje(){
        boolean _retorno= false;
        if(this.DetalleAnomalia.getAsString("mensaje").equals("S")){
            if(!this._txtMensaje.getText().toString().isEmpty()){
                _retorno = true;
            }else{
                argumentos.clear();
                argumentos.putString("Titulo","ERROR.");
                argumentos.putString("Mensaje","Es necesario que ingrese un mensaje.");
                this.dialogo.setArguments(argumentos);
                this.dialogo.show(getFragmentManager(), "SaveDialog");
            }
        }else{
            _retorno = true;
        }
        return _retorno;
    }


    private boolean ValidacionDatosCompletos(){
        boolean _retorno1 = false;
        boolean _retorno2 = false;
        boolean _retorno3 = false;
        if((this.FcnLectura.getId_serial1() != -1)){
            if(this._txtLectura1.getText().toString().isEmpty()) {
                _retorno1 = false;
                argumentos.clear();
                argumentos.putString("Titulo","ERROR.");
                argumentos.putString("Mensaje","Falta ingresar la lectura 1.");
                this.dialogo.setArguments(argumentos);
                this.dialogo.show(getFragmentManager(), "SaveDialog");
            }else {
                this.validarCritica(1, _txtLectura1.getText().toString());
                this.lecturaEnviar1 = Integer.parseInt(this._txtLectura1.getText().toString());
                _retorno1 = true;
            }
        }else{
            this.lecturaEnviar1 = -1;
            _retorno1 = true;
        }

        if((this.FcnLectura.getId_serial2() != -1)){
            if(this._txtLectura2.getText().toString().isEmpty()) {
                _retorno2 = false;
                argumentos.clear();
                argumentos.putString("Titulo","ERROR.");
                argumentos.putString("Mensaje","Falta ingresar la lectura 2.");
                this.dialogo.setArguments(argumentos);
                this.dialogo.show(getFragmentManager(), "SaveDialog");
            }else {
                this.validarCritica(2, _txtLectura2.getText().toString());
                this.lecturaEnviar2 = Integer.parseInt(this._txtLectura2.getText().toString());
                _retorno2 = true;
            }
        }else{
            this.lecturaEnviar2 = -1;
            _retorno2 = true;
        }

        if((this.FcnLectura.getId_serial3() != -1)){
            if(this._txtLectura3.getText().toString().isEmpty()) {
                _retorno3 = false;
                argumentos.clear();
                argumentos.putString("Titulo","ERROR.");
                argumentos.putString("Mensaje","Falta ingresar la lectura 3.");
                this.dialogo.setArguments(argumentos);
                this.dialogo.show(getFragmentManager(), "SaveDialog");
            }else {
                this.validarCritica(3, _txtLectura3.getText().toString());
                this.lecturaEnviar3 = Integer.parseInt(this._txtLectura3.getText().toString());
                _retorno3 = true;
            }
        }else{
            this.lecturaEnviar3 = -1;
            _retorno3 = true;
        }

        String _tipo_uso[]    = this._cmbTipoUso.getSelectedItem().toString().split("-");
        this.tipo_uso         = _tipo_uso[0];
        this.critica_general= this.b_critica1 | this.b_critica2 | this.b_critica3;
        return _retorno1 & _retorno2 & _retorno3;
    }


    private void validarCritica(int indice, String lectura){
        if(indice == 1){
            int consumo1 = Integer.parseInt(lectura)- this.FcnLectura.getLectura_anterior1();
            this.critica1 = (consumo1/(this.FcnLectura.getPromedio1()+ Double.parseDouble("0.000001")))*this.FcnLectura.getFactor_multiplicacion();
            this.b_critica1= this.FcnLectura.getCritica(this.critica1);
        }

        if(indice == 2){
            int consumo2 = Integer.parseInt(lectura)- this.FcnLectura.getLectura_anterior2();
            this.critica2 = (consumo2/(this.FcnLectura.getPromedio2()+ Double.parseDouble("0.000001")))*this.FcnLectura.getFactor_multiplicacion();
            this.b_critica2= this.FcnLectura.getCritica(this.critica2);
        }

        if(indice == 3){
            int consumo3 = Integer.parseInt(lectura)- this.FcnLectura.getLectura_anterior3();
            this.critica3 = (consumo3/(this.FcnLectura.getPromedio3()+ Double.parseDouble("0.000001")))*this.FcnLectura.getFactor_multiplicacion();
            this.b_critica3= this.FcnLectura.getCritica(this.critica3);
        }
    }


    private boolean compararDatosLocales(){
        return  this._txtLectura1.getText().toString().equals(this.lectura1+"") &
                this._txtLectura2.getText().toString().equals(this.lectura2+"") &
                this._txtLectura3.getText().toString().equals(this.lectura3+"");
    }


    private void MostrarInformacionBasica(){
        this._lblCuenta.setText(this.FcnLectura.getCuenta() + "");
        this._lblNombre.setText(this.FcnLectura.getNombre());
        this._lblDireccion.setText(this.FcnLectura.getDireccion());
        this._lblRuta.setText(this.FcnLectura.getRuta());
        this._lblMedidor.setText(this.FcnLectura.getMarca_medidor()+" "+this.FcnLectura.getSerie_medidor());
    }


    private void publishDatosUsuario(){
        this.MostrarInformacionBasica();
        this._cmbAnomalia.setSelection(0);
        this.intentos   = this.FcnLectura.getIntentos();
        this._txtMensaje.setText("");
        this._btnGuardar.setEnabled(this.FcnLectura.getEstado().equals("P"));
        this._tempRegistro  = this.FcnLectura.getLecturasIntento();
        this.lectura1       = this._tempRegistro.getAsInteger("lectura1");
        this.lectura2       = this._tempRegistro.getAsInteger("lectura2");
        this.lectura3       = this._tempRegistro.getAsInteger("lectura3");
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LecturasBtnGuardar:
                if(this.ValidacionDatosCompletos()){
                    if ( this.ValidacionFoto() && this.ValidacionMensaje()){
                        this.FcnLectura.guardarLectura(this.FcnLectura.getId_serial1(),
                                                       this.FcnLectura.getId_serial2(),
                                                       this.FcnLectura.getId_serial3(),
                                                       this.DetalleAnomalia.getAsInteger("id_anomalia"),
                                                       this._txtMensaje.getText().toString(),
                                                       this.lecturaEnviar1,
                                                       this.lecturaEnviar2,
                                                       this.lecturaEnviar3,
                                                       this.critica1,
                                                       this.critica2,
                                                       this.critica3,
                                                       this.tipo_uso);

                        if(this.critica_general && this.intentos == 1){
                            this.intentos ++;
                            this.lectura1 = Integer.parseInt(this._txtLectura1.getText().toString());
                            this.lectura2 = Integer.parseInt(this._txtLectura2.getText().toString());
                            this.lectura3 = Integer.parseInt(this._txtLectura3.getText().toString());
                            MostrarInputLectura(true);
                            argumentos.clear();
                            argumentos.putString("Titulo","ADVERTENCIA.");
                            argumentos.putString("Mensaje","Se ha generado critica. Favor ingresar datos de nuevo.");
                            this.dialogo.setArguments(argumentos);
                            this.dialogo.show(getFragmentManager(), "SaveDialog");
                        }else if(this.critica_general && this.intentos == 2){
                            this.intentos++;
                            if(this.compararDatosLocales()){
                                this._btnGuardar.setEnabled(false);
                                MostrarInputLectura(true);
                                Toast.makeText(this, "Lectura Registrada.", Toast.LENGTH_LONG).show();
                                this.FcnFormatos.ActaLectura(   this.FcnLectura.getTipo_energia1(),
                                                                this.lectura1,
                                                                this._cmbAnomalia.getSelectedItem().toString(),
                                                                this.FcnLectura.getCuenta(),
                                                                this.FcnLectura.getMunicipio(),
                                                                this.FcnLectura.getNombre(),
                                                                this.FcnLectura.getDireccion(),
                                                                this.FcnLectura.getSerie_medidor()+"-"+this.FcnLectura.getMarca_medidor(),
                                                                ClassSession.getInstance(this).getCodigo());

                                this.FcnLectura.cambiarEstadoLectura(this.FcnLectura.getId_serial());
                                this.Upload.execute(this.FcnUsuario.getCodigo()+"");

                                if(this.flag_search){
                                    this.flag_search = false;
                                    if(this.FcnLectura.getDatosUsuarioByIdSerial(this.backupId)){
                                        this.publishDatosUsuario();
                                    }
                                }else {
                                    if (this.FcnLectura.getNextDatosUsuario()) {
                                        this.publishDatosUsuario();
                                    }
                                }
                            }else{
                                MostrarInputLectura(true);
                                argumentos.clear();
                                argumentos.putString("Titulo","ADVERTENCIA.");
                                argumentos.putString("Mensaje","Se ha generado critica. Favor ingresar datos de nuevo.");
                                this.dialogo.setArguments(argumentos);
                                this.dialogo.show(getFragmentManager(), "SaveDialog");
                            }
                        }else{
                            this._btnGuardar.setEnabled(false);
                            MostrarInputLectura(true);
                            Toast.makeText(this, "Lectura Registrada.", Toast.LENGTH_LONG).show();
                            this.FcnFormatos.ActaLectura(   this.FcnLectura.getTipo_energia1(),
                                                            this.lectura1,
                                                            this._cmbAnomalia.getSelectedItem().toString(),
                                                            this.FcnLectura.getCuenta(),
                                                            this.FcnLectura.getMunicipio(),
                                                            this.FcnLectura.getNombre(),
                                                            this.FcnLectura.getDireccion(),
                                                            this.FcnLectura.getSerie_medidor()+"-"+this.FcnLectura.getMarca_medidor(),
                                                            ClassSession.getInstance(this).getCodigo());
                            this.FcnLectura.cambiarEstadoLectura(this.FcnLectura.getId_serial());
                            this.Upload.execute(this.FcnUsuario.getCodigo()+"");

                            if(this.flag_search){
                                this.flag_search = false;
                                if(this.FcnLectura.getDatosUsuarioByIdSerial(this.backupId)){
                                    this.publishDatosUsuario();
                                }
                            }else {
                                if (this.FcnLectura.getNextDatosUsuario()) {
                                    this.publishDatosUsuario();
                                }
                            }
                            this.FcnFormatos.FormatoPrueba();
                        }
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
                this.DetalleAnomalia = this.FcnAnomalia.validarDatosAnomalia(this._cmbAnomalia.getSelectedItem().toString());
                this.MostrarInputLectura(this.DetalleAnomalia.getAsString("lectura").equals("t"));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == RESULT_OK && requestCode == FROM_BUSCAR){
                if(data.getExtras().getBoolean("response")){
                    this.backupId       = this.FcnLectura.getId_serial();
                    this.flag_search    = true;
                    this.FcnLectura.getSearchDatosUsuario(data.getExtras().getString("cuenta"),data.getExtras().getString("medidor"));
                    this.publishDatosUsuario();

                    //String meidor = data.getExtras().getString("medidor");
                    //String cuenta = data.getExtras().getString("cuenta");
                    //Toast.makeText(this, data.getExtras().getString("medidor"),Toast.LENGTH_SHORT);
                    //Toast.makeText(this, data.getExtras().getString("cuenta"),Toast.LENGTH_SHORT);
                }
            }
        }catch(Exception e){

        }
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
                        this.publishDatosUsuario();
                    }
                }

                if(distance<0){
                    if(this.FcnLectura.getBackDatosUsuario()) {
                        this.publishDatosUsuario();
                        /*this.MostrarInformacionBasica();
                        this._cmbAnomalia.setSelection(0);
                        this.intentos       = this.FcnLectura.getIntentos();
                        this._btnGuardar.setEnabled(this.FcnLectura.getEstado().equals("P"));
                        this._tempRegistro  = this.FcnLectura.getLecturasIntento();
                        this.lectura1       = this._tempRegistro.getAsInteger("lectura1");
                        this.lectura2       = this._tempRegistro.getAsInteger("lectura2");
                        this.lectura3       = this._tempRegistro.getAsInteger("lectura3");*/
                    }
                }

            default:
                break;
        }
        return false;
    }

}
