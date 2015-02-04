package lecturas.sypelc.mobilelecturas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class tomar_lectura extends ActionBarActivity implements OnTouchListener, OnClickListener{

    private ViewFlipper _viewFlipper;
    private TextView    _lblCuenta, _lblNombre, _lblDireccion, _lblRuta, _lblMedidor, _lblTipoUso;
    private TextView    _lblAnomalia, _lblLectura1, _lblLectura2, _lblLectura3, _lblMensaje;
    private EditText    _txtLectura1, _txtLectura2, _txtLectura3, _txtMensaje;
    private Spinner     _cmbTipoUso, _cmbAnomalia;
    private Button      _btnGuardar, _btnImprimir;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_lectura);

        this._lblCuenta     = (TextView) findViewById(R.id.LecturaTxtCuenta);
        this._lblNombre     = (TextView) findViewById(R.id.LecturaTxtNombre);
        this._lblDireccion  = (TextView) findViewById(R.id.LecturaTxtDireccion);
        this._lblRuta       = (TextView) findViewById(R.id.LecturaTxtRuta);
        this._lblMedidor    = (TextView) findViewById(R.id.LecturaTxtMedidor);

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tomar_lectura, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
                //init_x=event.getX();
                return true;
            case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
                //float distance =init_x-event.getX();

                /*if(distance>0){
                    _viewFlipper.showPrevious();
                }

                if(distance<0){
                    _viewFlipper.showNext();
                }*/

            default:
                break;
        }
        return false;
    }

}
