package whack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import whack.utils.ScreenDimensions;

public class GameOverActivity extends AppCompatActivity {
    private static final double LAYOUT_RELATIVE_SIZE = 0.7;
    private static final String NAME_EXTRA = "name";
    private static final String GAME_RESULT = "result";
    private static final String TIME = "time";
    private static final String SCORE = "score";

    private String playerName;
    private String result;
    private int score;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        setAsPopupWindow();
        initializeVariablesFromIntent();


        Log.d("tair", "onCreate: " + playerName);
        WriteMessageToUser();

        Button mainMenuButton = findViewById(R.id.back_to_main_button);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                intent.putExtra(NAME_EXTRA, playerName);
                startActivity(intent);
            }
        });

        Button playAgainButton = findViewById(R.id.play_again_button);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.putExtra(NAME_EXTRA, playerName);
                startActivity(intent);
            }
        });

        Button topTenButton = findViewById(R.id.top_ten_button);
        topTenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOverActivity.this, TopTenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAsPopupWindow() {
        int width = ScreenDimensions.screenWidthPixels(this);
        int height = ScreenDimensions.screenHeightPixels(this);

        getWindow().setLayout((int)(width*LAYOUT_RELATIVE_SIZE), (int)(height*LAYOUT_RELATIVE_SIZE));
    }

    private void initializeVariablesFromIntent() {
        playerName = getIntent().getStringExtra(NAME_EXTRA);
        result = getIntent().getStringExtra(GAME_RESULT);
        score = getIntent().getIntExtra(SCORE, 0);
        time = getIntent().getIntExtra(TIME, 0);

    }

    private void WriteMessageToUser() {
        TextView resultText = findViewById(R.id.game_result_text);
        TextView scoreAndTimeText = findViewById(R.id.score_and_time_text);

        StringBuilder stringBuilderResult = new StringBuilder();
        stringBuilderResult.append(playerName).append("\n").append("You ").append(result + "!");

        resultText.setText(stringBuilderResult.toString());

        StringBuilder stringBuilderScoreTime = new StringBuilder();
        stringBuilderScoreTime.append("Time: ").append(time).append(" Score: ").append(score);

        scoreAndTimeText.setText(stringBuilderScoreTime.toString());
    }
}
