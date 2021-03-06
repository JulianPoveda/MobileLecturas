package dialogos;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import lecturas.sypelc.mobilelecturas.FormInicioSession;
import lecturas.sypelc.mobilelecturas.R;
import sistema.SQLite;

/**
 * Created by JULIANEDUARDO on 11/03/2015.
 */
public class ShowDialog {
    private EditText _txtPendientes;
    private EditText _txtEnCola;
    private EditText _txtEnviados;
    private EditText _txtTotal;

    private int     totalRuta;
    private int     totalPendientes;
    private int     totalCola;
    private int     totalEnviadas;

    public void showLoginDialog(Context _ctx, int _municipio, String _ruta) {

        SQLite  FcnSQL = new SQLite(_ctx, FormInicioSession.path_files_app);
        this.totalRuta          = FcnSQL.CountRegistrosWhere("maestro_clientes","id_municipio = "+_municipio+" AND ruta='"+_ruta+"'");
        this.totalPendientes    = FcnSQL.CountRegistrosWhere("maestro_clientes","id_municipio = "+_municipio+" AND ruta='"+_ruta+"' AND estado = 'P'");
        this.totalCola          = FcnSQL.CountRegistrosWhere("maestro_clientes","id_municipio = "+_municipio+" AND ruta='"+_ruta+"' AND estado = 'T'");
        this.totalEnviadas      = FcnSQL.CountRegistrosWhere("maestro_clientes","id_municipio = "+_municipio+" AND ruta='"+_ruta+"' AND estado = 'E'");

        LayoutInflater linf = LayoutInflater.from(_ctx);
        View inflator = linf.inflate(R.layout.dialog_rendimiento, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(_ctx);

        alert.setTitle("RENDIMIENTO");
        alert.setView(inflator);

        this._txtPendientes = (EditText) inflator.findViewById(R.id.dialog_txt_pendientes);
        this._txtEnCola     = (EditText) inflator.findViewById(R.id.dialog_txt_cola);
        this._txtEnviados   = (EditText) inflator.findViewById(R.id.dialog_txt_enviadas);
        this._txtTotal      = (EditText) inflator.findViewById(R.id.dialog_txt_total);

        this._txtPendientes.setText(totalPendientes+"");
        this._txtEnCola.setText(totalCola+"");
        this._txtEnviados.setText(totalEnviadas+"");
        this._txtTotal.setText(totalRuta+"");

        this._txtPendientes.setEnabled(false);
        this._txtEnCola.setEnabled(false);
        this._txtEnviados.setEnabled(false);
        this._txtTotal .setEnabled(false);

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){
            }
        });
        alert.show();
    }
}
