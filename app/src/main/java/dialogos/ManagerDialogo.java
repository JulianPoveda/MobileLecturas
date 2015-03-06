package dialogos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by JULIANEDUARDO on 06/03/2015.
 */
public class ManagerDialogo {
    private ManagerDialogo      ourInstance;// = new ManagerDialogo();
    private Bundle              argumentos;
    private DialogoInformativo  dialogo;


    private ManagerDialogo(Context _ctx, String _titulo, String _mensaje) {
        this.argumentos     = new Bundle();
        this.dialogo        = new DialogoInformativo();

        this.argumentos.clear();
        this.argumentos.putString("Titulo","ERROR.");
        this.argumentos.putString("Mensaje","Se ha generado critica, ingrese la lectura nuevamente.");
        this.dialogo.setArguments(argumentos);
        this.dialogo.show(_ctx, "SaveDialog");
    }



}
