package whack.bl;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import whack.data.DatabaseHelper;

public class DatabaseManager {

    private DatabaseHelper dbHelper;
    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void savePlayer(Player player) {
        LatLng latLng = player.getGameLocation();
        double lat, lng;
        if(latLng == null)
            lat = lng = 0;
        else {
            lat = latLng.latitude;
            lng = latLng.longitude;
        }

        dbHelper.put(player.getName(), player.getScore(),lat, lng);
    }

    public ArrayList<Player> getLimitNumOfPlayers(int limit) {
        ArrayList<Player> players = new ArrayList<>();

        Cursor data = dbHelper.getCursor(limit);

        if(data.getCount() == 0) {
            Toast.makeText(context, "empty DB", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                Player player = new Player(data.getString(1));
                player.setScore(data.getInt(2));
                player.setGameLocation(new LatLng(data.getDouble(3), data.getDouble(4)));
                players.add(player);
            }
        }
        return players;
    }
}
