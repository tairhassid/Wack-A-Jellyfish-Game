package whack.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageButton;

import whack.activities.R;

public abstract class GameButton extends AppCompatImageButton {

    protected Drawable imageDrawable;
    protected int nextRoundChange;

    public GameButton(Context context) {
        super(context);
    }

    public abstract int getNextRoundChange();
    public abstract void setNextRoundChange(int nextRoundChange);
    protected abstract void setImageDrawable();
    public abstract Drawable getImageDrawable();
}
