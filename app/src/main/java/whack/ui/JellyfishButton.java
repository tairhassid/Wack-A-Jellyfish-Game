package whack.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageButton;

import whack.activities.R;

public class JellyfishButton extends GameButton {

    public enum ButtonType {Jellyfish, Fish};
    private ButtonType buttonType;

    public JellyfishButton(Context context) {
        super(context);
//        this.buttonType = type;
        setImageDrawable();
    }

    @Override
    public void setImageDrawable() {
         this.imageDrawable = getContext().getDrawable(R.drawable.gold_jellyfish);
    }

    @Override
    public Drawable getImageDrawable() {
        return this.imageDrawable;
    }

    @Override
    public int getNextRoundChange() {
        return nextRoundChange;
    }

    @Override
    public void setNextRoundChange(int nextRoundChange) {
        this.nextRoundChange = nextRoundChange;
    }



}
