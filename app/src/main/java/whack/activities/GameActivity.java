package whack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import whack.ui.JellyfishButton;
import whack.utils.ScreenDimensions;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private enum Result {Win, Lose}

    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final int MAX_MISS = 3;
    private static final String NAME_EXTRA = "name";
    private static final String GAME_RESULT = "result";
    private static final String TIME = "time";
    private static final String SCORE = "score";

    private boolean timerRunning;
    private long timeLeftInMillis;
    private long interval;
    private int score;
    private int miss;

    private RelativeLayout gameLayout;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private Runnable jellyfishVisibilityRunnable;
    private ArrayList<JellyfishButton> jellyfishImageButtons;
    private ArrayList<ImageView> missSigns;

    private ProgressBar progressBar;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerRunning = false;
        timeLeftInMillis = 31000;
        interval = 1000;
        score = 0;
        miss = 0;

        handler = new Handler();
        progressBar = findViewById(R.id.progress);
        scoreTextView = findViewById(R.id.score);

        designGridLayout();
        createRunnableForJellyfish();
        initializeMissSigns();
        updateTimer();
        updateScore();
        manageTime(Result.Lose);
    }

    private void initializeMissSigns() {
        missSigns = new ArrayList<>();
        for(int i = 0; i < MAX_MISS; i++) {
            int res = getResources().getIdentifier("miss_" + (i+1), "id", getPackageName());
            ImageView imageView = findViewById(res);
            missSigns.add(imageView);
        }
    }

    private void createRunnableForJellyfish() {
        jellyfishVisibilityRunnable = new Runnable() {
            @Override
            public void run() {
                //random number of jellyfish to show
                Random changeRandom = new Random();
                int numOfButtonsToChange = changeRandom.nextInt(4);
                for(int i = 0; i < numOfButtonsToChange+1; i++) {
                    int index = changeRandom.nextInt(ROWS*COLS);
                    JellyfishButton btn = jellyfishImageButtons.get(index);
                    btn.setNextRoundChange(1);
                }

                //show selected jellyfish
                for(JellyfishButton btn : jellyfishImageButtons) {
                    if(btn.getNextRoundChange() == 1) {
                        btn.setNextRoundChange(0);
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.INVISIBLE);
                    }
                }
                handler.postDelayed(jellyfishVisibilityRunnable, 2000);
            }
        };
        jellyfishVisibilityRunnable.run();
    }

    private void designGridLayout() {
        jellyfishImageButtons = new ArrayList<>();

        gameLayout = findViewById(R.id.game_layout);
        GridLayout gridLayout = createGridLayout();
        gameLayout.setOnClickListener(this);

        gameLayout.addView(gridLayout);

        for(int i = 0 ; i < COLS * ROWS ; i++) {
            int screenWidth = ScreenDimensions.screenWidthPixels(this);
            int imageWidth = screenWidth / COLS;

            RelativeLayout relativeLayout = new RelativeLayout(this);
            createVortexImage(relativeLayout, imageWidth);
            createJellyfishImageButton(relativeLayout, imageWidth);

            gridLayout.addView(relativeLayout);
        }
    }

    private void createVortexImage(RelativeLayout relativeLayout, int imageWidth) {
        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView imageView = new ImageView(this);
        imageView.setBackground(getDrawable(R.drawable.vortex));

        relParams.width = imageWidth;
        relParams.height = imageWidth;
        imageView.setLayoutParams(relParams);
        relativeLayout.addView(imageView);
    }

    private void createJellyfishImageButton(RelativeLayout relativeLayout, int imageWidth) {
        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        JellyfishButton jellyfishButton = new JellyfishButton(this);
        jellyfishButton.setBackground(getDrawable(R.drawable.gold_jellyfish));
        jellyfishButton.setVisibility(View.INVISIBLE);

        relParams.width = imageWidth - 200;
        relParams.height = imageWidth - 200;

        jellyfishButton.setLayoutParams(relParams);
        relativeLayout.addView(jellyfishButton);

        jellyfishImageButtons.add(jellyfishButton);
        jellyfishButton.setOnClickListener(this);
    }

    private GridLayout createGridLayout() {
        RelativeLayout.LayoutParams gridLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        GridLayout gridLayout = new GridLayout(this);

        gridLayoutParams.addRule(RelativeLayout.BELOW, R.id.top_layout_game);

        gridLayout.setLayoutParams(gridLayoutParams);
        gridLayout.setColumnCount(COLS);
        gridLayout.setRowCount(ROWS);

        return gridLayout;
    }

    private void manageTime(Result gameResult) {
        if(timerRunning) {
            stopTimer(gameResult);
            cancelListener();
        } else {
            startTimer();
        }
    }

    private void cancelListener() {
        for(JellyfishButton btn : jellyfishImageButtons) {
            btn.setOnClickListener(null);
        }
        gameLayout.setOnClickListener(null);
    }

    private void stopTimer(Result gameResult) {
        countDownTimer.cancel();
        timerRunning = false;
        moveToNextActivity(gameResult);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, interval) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                moveToNextActivity(Result.Win);
            }
        }.start();

        timerRunning = true;
    }

    private void moveToNextActivity(Result gameResult) {
        int secondsLeft = (int) (timeLeftInMillis % 60000 / 1000);
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra(NAME_EXTRA, getIntent().getStringExtra(NAME_EXTRA));
        intent.putExtra(GAME_RESULT, gameResult.name());
        intent.putExtra(TIME, (30-secondsLeft));
        intent.putExtra(SCORE, score);
        startActivity(intent);
    }

    private void updateTimer() {
        TextView timerText = findViewById(R.id.timer);
        StringBuffer secondsLeftText;
        int secondsLeft = (int) (timeLeftInMillis % 60000 / 1000);

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

    @Override
    public void onClick(View view) {
        Log.d("tair", "onClick: ");
        if(view instanceof JellyfishButton) {
            view.setVisibility(View.INVISIBLE);
            score++;
            progressBar.setProgress(score);
            updateScore();
        } else {
            miss++;
            missSigns.get(miss-1).setAlpha(1f);
            if(miss == 3) {
                //game over
                manageTime(Result.Lose);
            }
        }
    }

    private void updateScore() {
        if(score == 30) {
            //win
            manageTime(Result.Win);
        }
//            handler.post(progressRunnable);
        StringBuffer currentScore = new StringBuffer();
        currentScore.append(score);
        scoreTextView.setText(currentScore);
    }
}
