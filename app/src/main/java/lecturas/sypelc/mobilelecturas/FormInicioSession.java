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

import async_task.DownLoadParametros;
import async_task.DownLoadTrabajo;
import clases.ClassConfiguracion;
import clases.ClassUsuario;


public class FormInicioSession extends ActionBarActivity implements OnClickListener{
    public static String name_database      = "TomaLecturasBD";
    public static String path_files_app     = Environment.getExternalStorageDirectory() + File.separator + "TomaLecturas";
    public static String sub_path_pictures  = "Fotos";

    private Intent              new_form;
    private ClassUsuario        FcnUsuario;
    private ClassConfiguracion  FcnCfg;

    private Button      _btnLoggin;
    private EditText    _txtCodigo;
    private TextView    _lblNombre, _lblVersionSoft, _lblVersionBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_session);

        this.FcnUsuario = ClassUsuario.getInstance(this);
        this.FcnCfg     = ClassConfiguracion.getInstance(this);

        this._btnLoggin     = (Button) findViewById(R.id.LoginBtnIngresar);
        this._txtCodigo     = (EditText) findViewById(R.id.LoginEditTextCodigo);
        this._lblNombre     = (TextView) findViewById(R.id.LoginTxtNombre);
        this._lblVersionBD  = (TextView) findViewById(R.id.LoginTxtVersionBD);
        this._lblVersionSoft= (TextView) findViewById(R.id.LoginTxtVersionSoft);

        this._lblVersionBD.setText("Version BD "+this.FcnCfg.getVersion_bd());
        this._lblVersionSoft.setText("Version Software "+this.FcnCfg.getVersion_bd());

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
        if(this.FcnUsuario.isInicio_sesion()) {
            menu.findItem(R.id.InicioCargarParametros).setEnabled(true);
            menu.findItem(R.id.InicioCargarRuta).setEnabled(true);
            menu.findItem(R.id.InicioVerRutas).setEnabled(true);
            menu.findItem(R.id.InicioCrearBackup).setEnabled(true);

            this._txtCodigo.setEnabled(false);
            this._lblNombre.setText(this.FcnUsuario.getNombre());
            this._btnLoggin.setEnabled(false);
        }else{
            menu.findItem(R.id.InicioCargarParametros).setEnabled(false);
            menu.findItem(R.id.InicioCargarRuta).setEnabled(false);
            menu.findItem(R.id.InicioVerRutas).setEnabled(false);
            menu.findItem(R.id.InicioCrearBackup).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.InicioCargarParametros:
                new DownLoadParametros(this).execute(this.FcnUsuario.getCodigo()+"");
                break;

            case R.id.InicioCargarRuta:
                new DownLoadTrabajo(this).execute(this.FcnUsuario.getCodigo()+"");
                break;

            case R.id.InicioVerRutas:
                this.new_form = new Intent(this, FormInformacionRutas.class);
                this.new_form.putExtra("FolderAplicacion",Environment.getExternalStorageDirectory() + File.separator + "TomaLecturas");
                startActivity(this.new_form);
                break;

            case R.id.InicioConfiguracion:
                this.new_form = new Intent(this, FormConfiguracion.class);
                startActivity(this.new_form);
                break;

            case R.id.InicioCrearBackup:
                break;

            case R.id.InicioMenuSalir:
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
                    this.FcnUsuario.IniciarSession(Integer.parseInt(this._txtCodigo.getText().toString()));
                }

                invalidateOptionsMenu();
                break;
        }
    }
}
