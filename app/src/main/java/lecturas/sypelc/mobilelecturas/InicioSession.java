package lecturas.sypelc.mobilelecturas;

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

import sistema.SQLite;
import clases.ClassUsuario;


public class InicioSession extends ActionBarActivity implements OnClickListener{
    public static String name_database  = "TomaLecturasBD";
    public static String path_files_app = Environment.getExternalStorageDirectory() + File.separator + "TomaLecturas";

    private ClassUsuario    FcnUsuario;

    private Button      _btnLoggin;
    private EditText    _txtCodigo;
    private TextView    _lblNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_session);

        this.FcnUsuario = ClassUsuario.getInstance(this);

        this._btnLoggin = (Button) findViewById(R.id.LoginBtnIngresar);
        this._txtCodigo = (EditText) findViewById(R.id.LoginEditTextCodigo);
        this._lblNombre = (TextView) findViewById(R.id.LoginTxtNombre);

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
            menu.findItem(R.id.InicioMenuRuta).setEnabled(true);
            menu.findItem(R.id.InicioMenuBackup).setEnabled(true);
            menu.findItem(R.id.InicioMenuTrabajo).setEnabled(true);

            this._txtCodigo.setEnabled(false);
            this._lblNombre.setText(this.FcnUsuario.getNombre());
            this._btnLoggin.setEnabled(false);
        }else{
            menu.findItem(R.id.InicioMenuRuta).setEnabled(false);
            menu.findItem(R.id.InicioMenuBackup).setEnabled(false);
            menu.findItem(R.id.InicioMenuTrabajo).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.InicioCargarParametros:
                break;

            case R.id.InicioCargarRuta:
                break;

            case R.id.InicioVerRutas:
                break;

            case R.id.InicioCrearBackup:
                break;

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
