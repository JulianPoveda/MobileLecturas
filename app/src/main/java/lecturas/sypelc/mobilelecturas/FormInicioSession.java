package lecturas.sypelc.mobilelecturas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;


import java.io.File;
import java.util.Date;

import async_task.DownLoadParametros;
import async_task.DownLoadTrabajo;
import clases.ClassConfiguracion;
import clases.ClassFlujoInformacion;
import clases.ClassSession;
import dialogos.showDialogBox;
import sistema.Beacon;
import sistema.Bluetooth;
import sistema.DateTime;
import global.global_var;
import sistema.Utilidades;


public class FormInicioSession extends ActionBarActivity implements OnClickListener, global_var{
    public static String name_database      = "TomaLecturasBD";
    public static String path_files_app     = Environment.getExternalStorageDirectory() + File.separator + "TomaLecturas";
    public static String sub_path_pictures  = "Fotos";

    private Intent              new_form;
    Beacon envioActas;

    private ClassSession            FcnSession;
    private ClassConfiguracion      FcnCfg;
    private ClassFlujoInformacion   FcnInf;
    private DateTime                FcnTime;

    private Button      _btnLoggin;
    private EditText    _txtCodigo;
    private TextView    _lblNombre, _lblVersionSoft, _lblVersionBD, _lblMacBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_session);

        this.FcnSession     = ClassSession.getInstance(this);
        this.FcnCfg         = ClassConfiguracion.getInstance(this);
        this.FcnInf         = new ClassFlujoInformacion(this);
        this.FcnTime        = DateTime.getInstance();

        this._btnLoggin     = (Button) findViewById(R.id.LoginBtnIngresar);
        this._txtCodigo     = (EditText) findViewById(R.id.LoginEditTextCodigo);
        this._lblNombre     = (TextView) findViewById(R.id.LoginTxtNombre);
        this._lblVersionBD  = (TextView) findViewById(R.id.LoginTxtVersionBD);
        this._lblVersionSoft= (TextView) findViewById(R.id.LoginTxtVersionSoft);
        this._lblMacBluetooth=(TextView) findViewById(R.id.LoginTxtBluetooth);

        this._lblVersionBD.setText("Version BD "+this.FcnCfg.getVersion_bd());
        this._lblVersionSoft.setText("Version Software "+this.FcnCfg.getVersion_software());
        this._lblMacBluetooth.setText("MAC Bluetooth "+ Bluetooth.getInstance().GetOurDeviceByAddress());

        envioActas 	= new Beacon(this,path_files_app , 86400000, 60000);
        envioActas.start();

        invalidateOptionsMenu();
        this._btnLoggin.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio_session, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.InicioCargarParametros).setEnabled(this.FcnSession.isInicio_sesion());
        menu.findItem(R.id.InicioCargarRuta).setEnabled(this.FcnSession.isInicio_sesion());
        menu.findItem(R.id.InicioVerRutas).setEnabled(this.FcnSession.isInicio_sesion());
        menu.findItem(R.id.InicioCrearBackup).setEnabled(this.FcnSession.isInicio_sesion());
        menu.findItem(R.id.InicioConfiguracion).setEnabled(this.FcnSession.isInicio_sesion());

        this._txtCodigo.setEnabled(!this.FcnSession.isInicio_sesion());
        this._btnLoggin.setEnabled(!this.FcnSession.isInicio_sesion());
        this._lblNombre.setText(this.FcnSession.getNombre());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.InicioCargarParametros:
                new DownLoadParametros(this).execute(this.FcnSession.getCodigo()+"");
                break;

            case R.id.InicioCargarRuta:
                new DownLoadTrabajo(this).execute(this.FcnSession.getCodigo()+"", this.FcnInf.getTrabajoCargdo(), this.FcnTime.GetFecha(), this.FcnTime.GetHora());
                break;

            case R.id.InicioVerRutas:
                this.new_form = new Intent(this, FormInformacionRutas.class);
                //this.new_form.putExtra("FolderAplicacion",Environment.getExternalStorageDirectory() + File.separator + "TomaLecturas");
                startActivity(this.new_form);
                break;

            case R.id.InicioConfiguracion:
                this.new_form = new Intent(this, FormConfiguracion.class);
                startActivity(this.new_form);
                break;

            case R.id.InicioCrearBackup:
                break;

            case R.id.InicioMenuSalir:
                this.FcnSession.IniciarSession(null);
                invalidateOptionsMenu();
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LoginBtnIngresar:
                if(!this._txtCodigo.getText().toString().isEmpty()){
                    if(!this.FcnSession.IniciarSession(this._txtCodigo.getText().toString())){
                        new showDialogBox().showLoginDialog(this,DIALOG_ERROR,"INICIO DE SESION", "Codigo incorrecto.");
                    }
                }
                invalidateOptionsMenu();
                break;
        }
    }
}
