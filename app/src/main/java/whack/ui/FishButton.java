package whack.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageButton;

import whack.activities.R;

public class FishButton extends GameButton {

    public FishButton(Context context) {
        super(context);
        setImageDrawable();
    }

    @Override
    public int getNextRoundChange() {
        return nextRoundChange;
    }

    @Override
    public void setNextRoundChange(int nextRoundChange) {
        this.nextRoundChange = nextRoundChange;
    }

    @Override
    public void setImageDrawable() {
        this.imageDrawable = getContext().getDrawable(R.drawable.fish);
    }

    @Override
    public Drawable getImageDrawable() {
        return this.imageDrawable;
    }
}
