package whack.bl;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import whack.activities.R;
import whack.activities.TopTenActivity;
import whack.data.DatabaseHelper;

public class DatabaseManager {

    DatabaseHelper dbHelper;
    Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void savePlayer(Player player) {
        dbHelper.put(player.getName(), player.getScore(),player.getGameLocation().latitude, player.getGameLocation().longitude);
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
