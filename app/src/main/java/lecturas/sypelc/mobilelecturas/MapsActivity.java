package lecturas.sypelc.mobilelecturas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import clases.ClassSession;

public class MapsActivity extends FragmentActivity {

    private GoogleMap       mMap; // Might be null if Google Play services APK is not available.
    private CameraUpdate    mCamera;
    private String          cuenta,marcaMedidor,serieMedidor;
    private double          longitud, latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle       = getIntent().getExtras();
        this.cuenta         = String.valueOf(bundle.getInt("Cuenta"));
        this.marcaMedidor   = bundle.getString("Marca");
        this.serieMedidor   = bundle.getString("Serie");
        try{
            this.longitud       = Double.parseDouble(bundle.getString("Longitud"));
            this.latitud        = Double.parseDouble(bundle.getString("Latitud"));
        }catch(Exception e){
            this.longitud       = 0;
            this.latitud        = 0;
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.setMyLocationEnabled(true);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
         mMap.addMarker(new MarkerOptions()
                //.position(new LatLng(4.07278888888889, -73.6694944444445))
                .position(new LatLng(this.latitud, this.longitud))
                .title("Cuenta " + this.cuenta)
                .snippet("Medidor " + this.marcaMedidor + " " + this.serieMedidor)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_gps)));

        this.mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitud, this.longitud),14);
        this.mMap.animateCamera(this.mCamera);
    }
}