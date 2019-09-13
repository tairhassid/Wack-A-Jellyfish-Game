package whack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText playerName;
    private final String NAME_EXTRA = "name";
    private boolean didAlreadyRequestLocationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = findViewById(R.id.name_input_text);
        didAlreadyRequestLocationPermission = false;

        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                String name = playerName.getText().toString();
                if(name.isEmpty())
                    name = "Player";
                intent.putExtra(NAME_EXTRA, name);
                startActivity(intent);
            }
        });


    }

}
