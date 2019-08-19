package whack.ui;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageButton;

public class JellyfishButton extends AppCompatImageButton {

    private int nextRoundChange;

    public JellyfishButton(Context context) {
        super(context);
    }

    public int getNextRoundChange() {
        return nextRoundChange;
    }

    public void setNextRoundChange(int nextRoundChange) {
        this.nextRoundChange = nextRoundChange;
    }



}
