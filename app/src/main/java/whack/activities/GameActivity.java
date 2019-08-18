package whack.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private final int ROWS = 3;
    private final int COLS = 3;
    private DisplayMetrics _metrics;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 30000;
    private long interval = 1000;
    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        designGridLayout();
        updateTimer();
        manageTime();
    }

    private void designGridLayout() {
        RelativeLayout gameLayout = findViewById(R.id.game_layout);
        GridLayout gridLayout = createGridLayout(COLS, ROWS);

        gameLayout.addView(gridLayout);

        for(int i = 0 ; i < COLS * ROWS ; i++) {
            int fraction = COLS;
            int screenHeight = screenHeightPixels();
            int screenWidth = screenWidthPixels();
            int ImageWidth = screenWidth / fraction;

            ImageView imageView = new ImageView(this);
            imageView.setBackground(getDrawable(R.drawable.vortex));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = ImageWidth;
            params.height = ImageWidth;
            imageView.setLayoutParams(params);
            gridLayout.addView(imageView);
        }
    }

    private GridLayout createGridLayout(int cols, int rows) {
        RelativeLayout.LayoutParams gridLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        GridLayout gridLayout = new GridLayout(this);

        gridLayoutParams.addRule(RelativeLayout.BELOW, R.id.top_layout_game);

        gridLayout.setLayoutParams(gridLayoutParams);
        gridLayout.setColumnCount(cols);
        gridLayout.setRowCount(rows);


        return gridLayout;
    }

    public int screenWidthPixels() {
        return getMetrics().widthPixels;
    }

    public int screenHeightPixels() {
        return getMetrics().heightPixels;
    }

    private DisplayMetrics getMetrics() {
        if (_metrics == null) {
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            _metrics = new DisplayMetrics();
            display.getMetrics(_metrics);
        }

        return _metrics;
    }

    private void manageTime() {
        if(timerRunning) {
            stopTimer();
        } else {
            Log.d("tair", "manageTime: timer is not running");
            startTimer();
        }
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void startTimer() {
        Log.d("tair", "startTimer: ");
        countDownTimer = new CountDownTimer(timeLeftInMillis, interval) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                Log.d("tair", "onTick: time left " + timeLeftInMillis);
                updateTimer();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                startActivity(intent);
            }
        }.start();

        timerRunning = true;
    }

    private void updateTimer() {
        timerText = findViewById(R.id.timer);
        StringBuffer secondsLeftText;
        int secondsLeft = (int) (timeLeftInMillis % 60000 / 1000);

        Log.d("tair", "updateTimer: ");
        secondsLeftText = new StringBuffer();
        if(secondsLeft < 10)
            secondsLeftText.append(0);
        secondsLeftText.append(secondsLeft);

        timerText.setText(secondsLeftText);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
