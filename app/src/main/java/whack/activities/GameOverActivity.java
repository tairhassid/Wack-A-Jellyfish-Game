package whack.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameOverActivity extends AppCompatActivity {
    private final String NAME_EXTRA = "name";
    private final String GAME_RESULT = "result";
    private final String WIN = "win";
    private final String LOSE = "lose";
    private String playerName;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        playerName = getIntent().getStringExtra(NAME_EXTRA);
        result = getIntent().getStringExtra(GAME_RESULT);
        Log.d("tair", "onCreate: " + playerName);
        WriteMessageToUser();
    }

    private void WriteMessageToUser() {
        TextView resultText = findViewById(R.id.game_result_text);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(playerName).append("\n").append("You ").append(result + "!");
        resultText.setText(stringBuffer.toString());
    }
}
