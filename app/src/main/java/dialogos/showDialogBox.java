package dialogos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import global.global_var;
import lecturas.sypelc.mobilelecturas.FormInicioSession;
import lecturas.sypelc.mobilelecturas.R;

/**
 * Created by DesarrolloJulian on 19/05/2015.
 */
public class showDialogBox implements global_var {
    /*final int DIALOG_WARNING       = 1;
    final int DIALOG_ERROR         = 2;
    final int DIALOG_INFORMATIVE   = 3;*/


    private TextView    _txtMensaje;
    private ImageView   _imgIcono;

    public void showLoginDialog(Context _ctx, int _typeDialog, String _tittle, String _message) {
        LayoutInflater linf = LayoutInflater.from(_ctx);
        View inflator = linf.inflate(R.layout.dialog_general, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(_ctx);

        alert.setTitle(_tittle);
        alert.setView(inflator);

        this._imgIcono  = (ImageView) inflator.findViewById(R.id.dialogImage);
        this._txtMensaje= (TextView) inflator.findViewById(R.id.dialogMessage);

        switch (_typeDialog){
            case DIALOG_ERROR:
                this._imgIcono.setImageResource(R.mipmap.ic_error_dialog);
                break;

            case DIALOG_WARNING:
                this._imgIcono.setImageResource(R.mipmap.ic_warning_dialog);
                break;

            case DIALOG_INFORMATIVE:
                this._imgIcono.setImageResource(R.mipmap.ic_information_dialog);
                break;

            default:
                this._imgIcono.setImageResource(R.mipmap.ic_information_dialog);
                break;
        }

        this._txtMensaje.setText(_message);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){
            }
        });
        alert.show();
    }
}