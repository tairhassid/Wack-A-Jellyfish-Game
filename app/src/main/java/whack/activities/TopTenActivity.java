package whack.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import whack.bl.Player;
import whack.bl.PlayerListAdapter;
import whack.data.DatabaseHelper;

public class TopTenActivity extends AppCompatActivity {

    private static final String TAG = "TopTenActivity";
    DatabaseHelper db;
    private final int numOfRows = 10;
    private ImageButton backImgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        db = new DatabaseHelper(this);
        ListView listView = findViewById(R.id.score_list_view);

        ArrayList<Player> players = new ArrayList<>();
        Cursor data = db.getCursor("SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " ORDER BY " + DatabaseHelper.COL_SCORE + " DESC LIMIT " + numOfRows);

        if(data.getCount() == 0) {
            Toast.makeText(TopTenActivity.this, "empty DB", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                Player player = new Player(data.getString(1));
                player.setScore(data.getInt(2));
                player.setGameLocation(new LatLng(data.getDouble(3), data.getDouble(4)));
                players.add(player);
            }

            ArrayAdapter listAdapter = new PlayerListAdapter(this, R.layout.list_layout, players);
            listView.setAdapter(listAdapter);

        }

        backImgButton = findViewById(R.id.back_button);
        backImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
