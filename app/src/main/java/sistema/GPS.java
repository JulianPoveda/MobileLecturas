package sistema;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by JULIANEDUARDO on 14/04/2015.
 */

public class GPS implements LocationListener{
    private static GPS instanceGPS;
    private boolean estadoGPS;
    private String 	latitudGPS;
    private String 	longitudGPS;


    public static GPS getInstance() {
        if(instanceGPS == null){
            instanceGPS = new GPS();
        }
        return instanceGPS;
     }


    private GPS(){
        this.setEstadoGPS(false);
        this.setLatitudGPS("0.0");
        this.setLongitudGPS("0.0");
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(!this.latitudGPS.equals(String.valueOf(location.getLatitude()))||!this.longitudGPS.equals(String.valueOf(location.getLongitude()))){
            this.latitudGPS = String.valueOf(location.getLatitude());
            this.longitudGPS= String.valueOf(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        this.estadoGPS = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        this.estadoGPS = false;
    }

    public boolean isEstadoGPS() {
        return estadoGPS;
    }

    private void setEstadoGPS(boolean estadoGPS) {
        this.estadoGPS = estadoGPS;
    }

    public String getLatitudGPS() {
        return latitudGPS;
    }

    private void setLatitudGPS(String latitudGPS) {
        this.latitudGPS = latitudGPS;
    }

    public String getLongitudGPS() {
        return longitudGPS;
    }

    private void setLongitudGPS(String longitudGPS) {
        this.longitudGPS = longitudGPS;
    }
}