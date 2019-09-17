package whack.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import whack.bl.DatabaseManager;
import whack.bl.Player;
import whack.bl.PlayerListAdapter;

public class TopTenActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private final int numOfRows = 10;
    private ImageButton backImgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        databaseManager = new DatabaseManager(this);
        ListView listView = findViewById(R.id.score_list_view);

        ArrayList<Player> players = databaseManager.getLimitNumOfPlayers(numOfRows);

        if(players != null) {
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
