package whack.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import whack.bl.GameManager;
import whack.bl.Player;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private static final int ERROR_DIALOG_REQ = 9001;
    private static final int PERMISSION_REQ_ENABLE_GPS = 9002;
    private static final int PERMISSION_REQ_ACCESS_FINE_LOCATION = 9003;

    private EditText playerName;
    private boolean requestedLocationPermission;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng latLng;
    private GameManager gameManager;
    private Location currentLocation;

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = findViewById(R.id.name_input_text);
        requestedLocationPermission = false;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        gameManager = GameManager.getInstance();

        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                String name = playerName.getText().toString();
                if (name.isEmpty())
                    name = "Player";
                Player player = new Player(name);
                player.setGameLocation(latLng);
                gameManager.setCurrentPlayer(player);
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreate: ");
        getLocationPermission();
//        getLastKnownLocation();

//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);


//        if (requestedLocationPermission)
//            getLastKnownLocation();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (requestedLocationPermission) {
                Log.d(TAG, "onResume: requestedLocationPermission=" + requestedLocationPermission);
                getLastKnownLocation();
            } else {
                Log.d(TAG, "onResume: requestedLocationPermission=" + requestedLocationPermission);
//                getLocationPermission();
//                getLastKnownLocation();
            }
        }
    }

    //https://www.youtube.com/watch?v=1f4b2-Y_q2A&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=4
    private boolean checkMapServices() {
        if (isGoogleServicesWorking()) {
//            if (isMapsEnabled()) {
            return true;
//            }
        }
        return false;
    }

    //if google services are installed in the device- needed to work with google maps
    public boolean isGoogleServicesWorking() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQ);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


//    /*Check if GPS is enabled on the device*/
//    public boolean isMapsEnabled() {
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//            return false;
//        }
//        return true;
//    }

//    /*Create a dialog that notifies that we need GPS*/
//    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    //if they click "yes" - ask permission for GPS
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        Log.d(TAG, "onClick: yes");
//                        // to know if the user accepted the permission, onActivityResult is called after
//                        startActivityForResult(enableGpsIntent, PERMISSION_REQ_ENABLE_GPS);
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: called");
//        if (requestCode == PERMISSION_REQ_ENABLE_GPS) {
//            if (!requestedLocationPermission) {
//                getLocationPermission();
//            }
//        }
//    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: " + requestedLocationPermission);
            requestedLocationPermission = true;
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQ_ACCESS_FINE_LOCATION);
        }
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestedLocationPermission = false;
        Log.d(TAG, "onRequestPermissionsResult: before if");
        if (requestCode == PERMISSION_REQ_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestedLocationPermission = true;
                Log.d(TAG, "onRequestPermissionsResult: calling fused");
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: ");
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            Log.d(TAG, "getLastKnownLocation: listener");
            if (location != null) {
                Log.d(TAG, "getLastKnownLocation: location " + location );
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();
                latLng = new LatLng(wayLatitude,wayLongitude);
            }
        });
    }
}
