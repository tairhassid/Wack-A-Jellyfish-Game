package whack.activities;

import android.Manifest;
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
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final String NAME_EXTRA = "name";
    private EditText playerName;
    private boolean requestedLocationPermission;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LatLng latLng;
    private GameManager gameManager;

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
                if(name.isEmpty())
                    name = "Player";
                Player player = new Player(name);
                player.setGameLocation(latLng);
                gameManager.setCurrentPlayer(player);
                startActivity(intent);
            }
        });

        getLastKnownLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkMapServices()) {
            if(requestedLocationPermission) {
                //something
            } else {
                getLocationPermission();
            }
        }
    }

    //https://www.youtube.com/watch?v=1f4b2-Y_q2A&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=4
    private boolean checkMapServices() {
        if(isGoogleServicesWorking()){
            if(isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGoogleServicesWorking() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isGoogleServicesWorking: YES");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, 9002);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");
        switch (requestCode) {
            case 9002: {
                if(requestedLocationPermission) {

                } else {
                    getLocationPermission();
                }
            }
        }
    }

    private void getLocationPermission() {
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            requestedLocationPermission = true;
            //something
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    9003);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String permissions[],
                                          @NonNull int[] grantResults) {
        requestedLocationPermission = false;
        switch(requestCode) {
            case 9003: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestedLocationPermission = true;
                }
            }
        }
    }

    private void getLastKnownLocation() {
        Log.d("Tair", "getLastKnownLocation: called  " + fusedLocationProviderClient );

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d("******Tair", "onComplete:");

                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    currentLocation = location;
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "onComplete: lat=" + latLng.latitude);
                    Log.d(TAG, "onComplete: lng=" + latLng.longitude);

                }
            }
        });
    }

}
