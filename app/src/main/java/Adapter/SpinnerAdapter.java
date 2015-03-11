package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lecturas.sypelc.mobilelecturas.R;

/**
 * Created by JULIANEDUARDO on 10/03/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context             ctx;
    private ArrayList<String>   myStrings;
    private String              colorText;
    private String              colorBack;


    public SpinnerAdapter(Context _ctx, int _txtViewResourceId, ArrayList<String> _objects, String _colorText, String _colorBack){
        super(_ctx, _txtViewResourceId, _objects);
        this.ctx        = _ctx;
        this.myStrings  = _objects;
        this.colorText  = _colorText;
        this.colorBack  = _colorBack;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }


    public View getCustomView(int _position, View _convertView, ViewGroup _parent){
        LayoutInflater inflater = LayoutInflater.from(this.ctx);
        View        mySpinner = inflater.inflate(R.layout.custom_spinner, _parent, false);
        TextView    txtSpinner= (TextView) mySpinner.findViewById(R.id.custom_text_spinner);
        txtSpinner.setText(this.myStrings.get(_position));
        txtSpinner.setTextColor(Color.parseColor(this.colorText));
        txtSpinner.setBackgroundColor(Color.parseColor(this.colorBack));
        return mySpinner;
    }
}
