package lecturas.sypelc.mobilelecturas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class DialogoInformacion extends ActionBarActivity implements OnClickListener{
    TextView    _lblMensaje;
    Button      _btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogo_informacion);

        Bundle bundle 	= getIntent().getExtras();

        _lblMensaje	= (TextView) findViewById(R.id.di_Lbl_Mensaje);
        _btnAceptar	= (Button) findViewById(R.id.di_Btn_Aceptar);
        _lblMensaje.setText(bundle.getString("informacion"));
        _btnAceptar.setOnClickListener(this);
    }

    public void finish(boolean _caso) {
        Intent data = new Intent();
        data.putExtra("accion", _caso);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.di_Btn_Aceptar:
                finish(true);
                break;

            default:
                finish(true);
                break;
        }

    }
}
