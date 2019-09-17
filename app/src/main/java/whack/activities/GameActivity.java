package whack.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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

import whack.bl.DatabaseManager;
import whack.bl.GameManager;
import whack.bl.Player;
import whack.ui.FishButton;
import whack.ui.GameButton;
import whack.ui.JellyfishButton;
import whack.utils.ScreenDimensions;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MAX_NUM_OF_BUTTONS_TO_CHANGE = 6;
    public static final int MIN_NUM_OF_BUTTONS_TO_CHANGE = 3;

    private enum Result {Win, Lose}

    public static final int ROWS = 4;
    public static final int COLS = 3;
    public static final int MAX_MISS = 3;
    public static final int DELAY_MILLIS = 2000;
    public static final String GAME_RESULT = "result";
    public static final String TIME = "time";

    private boolean timerRunning;
    private long timeLeftInMillis;
    private long interval;
    private int score;
    private int miss;
    private DatabaseManager databaseManager;

    private RelativeLayout gameLayout;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private Runnable jellyfishVisibilityRunnable;
    private ArrayList<GameButton> jellyfishImageButtons;
    private ArrayList<ImageView> missSigns;

    private ProgressBar progressBar;
    private TextView scoreTextView;

    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerRunning = false;
        timeLeftInMillis = 31000;
        interval = DELAY_MILLIS/2;
        score = 0;
        miss = 0;

        gameManager = GameManager.getInstance();
        handler = new Handler();
        progressBar = findViewById(R.id.progress);
        scoreTextView = findViewById(R.id.score);
        databaseManager = new DatabaseManager(this);

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
                selectItemsToShowNextRound();

                changeVisibilityForItems();

                handler.postDelayed(jellyfishVisibilityRunnable, DELAY_MILLIS);
            }

            private void selectItemsToShowNextRound() {
                Random changeRandom = new Random();

                //random number of items to show between min and max
                int numOfButtonsToChange = changeRandom.nextInt(
                        (MAX_NUM_OF_BUTTONS_TO_CHANGE-MIN_NUM_OF_BUTTONS_TO_CHANGE) + 1)
                        + MIN_NUM_OF_BUTTONS_TO_CHANGE;
                ArrayList<Integer> indexArray = new ArrayList<>();
                Log.d("TAIR", "round:\n");
                for(int i = 0; i <= numOfButtonsToChange; i++) {
                    int index;
                    do {
                        index = changeRandom.nextInt(jellyfishImageButtons.size());
                        Log.d("TAIR", "selectItemsToShowNextRound: " + index);
                    } while ((indexArray.contains(index+1) && index%2 == 0) || (indexArray.contains(index-1)) && index%2 == 1);
                    GameButton btn = jellyfishImageButtons.get(index);
                    indexArray.add(index);
                    btn.setNextRoundChange(1);
                }
            }

            private void changeVisibilityForItems() {
                //show selected jellyfish
                for(GameButton btn : jellyfishImageButtons) {
                    if(btn.getNextRoundChange() == 1) {
                        btn.setNextRoundChange(0);
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.GONE);
                    }
                }
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
        relParams.width = imageWidth - 200;
        relParams.height = imageWidth - 200;

        GameButton jellyfishButton = new JellyfishButton(this);
        GameButton fishButton = new FishButton(this);

        createButton(relParams, jellyfishButton);
        createButton(relParams, fishButton);

        relativeLayout.addView(jellyfishButton);
        relativeLayout.addView(fishButton);

    }

    private void createButton(RelativeLayout.LayoutParams relParams, GameButton gameButton) {
        gameButton.setBackground(gameButton.getImageDrawable());
        gameButton.setVisibility(View.GONE);

        gameButton.setLayoutParams(relParams);

        jellyfishImageButtons.add(gameButton);
        gameButton.setOnClickListener(this);
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
        for(GameButton btn : jellyfishImageButtons) {
            btn.setOnClickListener(null);
        }
        gameLayout.setOnClickListener(null);
    }

    private void stopTimer(Result gameResult) {
        countDownTimer.cancel();
        timerRunning = false;
        moveToGameOverActivity(gameResult);
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
                moveToGameOverActivity(Result.Win);
            }
        }.start();

        timerRunning = true;
    }

    private void moveToGameOverActivity(Result gameResult) {
        Player player = gameManager.getCurrentPlayer();
        player.setScore(score);

        databaseManager.savePlayer(player);
        int secondsLeft = (int) (timeLeftInMillis % 60000 / 1000);

        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra(GAME_RESULT, gameResult.name());
        intent.putExtra(TIME, (30-secondsLeft));
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        Log.d("tair", "onClick: ");
        if(view instanceof JellyfishButton) {
            score++;
            progressBar.setProgress(score);
            updateScore();

            animateJellyfish(view);

        } else if(view instanceof FishButton) {
            view.setVisibility(View.GONE);
            score -= 3;
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

    private void animateJellyfish(View view) {
        final JellyfishButton jellyfishButton = (JellyfishButton) view;

        int animationDuration = 1000;
        ObjectAnimator animatorFade = ObjectAnimator.ofFloat(jellyfishButton, View.ALPHA, 1f, 0f);

        animatorFade.setDuration(animationDuration);
        animatorFade.start();

        animatorFade.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                jellyfishButton.setVisibility(View.GONE);
                jellyfishButton.setAlpha(1f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void updateScore() {
        if(score == 30) {
            //win
            manageTime(Result.Win);
        }
        StringBuffer currentScore = new StringBuffer();
        currentScore.append(score);
        scoreTextView.setText(currentScore);
    }
}
