package lecturas.sypelc.mobilelecturas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import clases.ClassConfiguracion;
import sistema.Bluetooth;


public class configuracion extends ActionBarActivity implements OnClickListener {
    private ClassConfiguracion  FcnCfg;
    private Bluetooth           FcnBluetooth;

    private ArrayAdapter<String> AdapLstImpresoras;
    private ArrayList<String>   _listaImpresoras = new ArrayList<String>();

    private EditText            _txtServidor, _txtPuerto, _txtModulo, _txtWebService;
    private Spinner             _cmbImpresora;
    private Button              _btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        this.FcnCfg         = ClassConfiguracion.getInstance(this);
        this.FcnBluetooth   = Bluetooth.getInstance();

        this._txtServidor   = (EditText) findViewById(R.id.ConfiguracionTxtServidor);
        this._txtPuerto     = (EditText) findViewById(R.id.ConfiguracionTxtPuerto);
        this._txtModulo     = (EditText) findViewById(R.id.ConfiguracionTxtModulo);
        this._txtWebService = (EditText) findViewById(R.id.ConfiguracionTxtWebService);
        this._cmbImpresora  = (Spinner) findViewById(R.id.ConfiguracionCmbImpresora);
        this._btnGuardar    = (Button) findViewById(R.id.ConfiguracionBtnGuardar);

        this._listaImpresoras   = this.FcnBluetooth.GetDeviceBluetoothByAddress();
        this.AdapLstImpresoras 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._listaImpresoras);
        this._cmbImpresora.setAdapter(this.AdapLstImpresoras);
        this._cmbImpresora.setSelection(this.AdapLstImpresoras.getPosition(this.FcnCfg.getPrinter()));

        this._txtServidor.setText(this.FcnCfg.getIp_server());
        this._txtPuerto.setText(this.FcnCfg.getPort());
        this._txtModulo.setText(this.FcnCfg.getModule_web_service());
        this._txtWebService.setText(this.FcnCfg.getWeb_service());

        this._btnGuardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ConfiguracionBtnGuardar:
                this.FcnCfg.setIp_server(this._txtServidor.getText().toString());
                this.FcnCfg.setPort(this._txtPuerto.getText().toString());
                this.FcnCfg.setModule_web_service(this._txtModulo.getText().toString());
                this.FcnCfg.setWeb_service(this._txtWebService.getText().toString());
                this.FcnCfg.setPrinter(this._cmbImpresora.getSelectedItem().toString());
                break;
        }
    }
}
