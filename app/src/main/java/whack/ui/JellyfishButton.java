package whack.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

import whack.activities.R;

public class JellyfishButton extends GameButton {

    public enum ButtonType {Jellyfish, Fish};
    private ButtonType buttonType;

    public JellyfishButton(Context context) {
        super(context);
//        this.buttonType = type;
        setImageDrawable();
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
//                animation.setDuration(1000);
//                animation.start();
//                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator updatedAnimation) {
//                        // You can use the animated value in a property that uses the
//                        // same type as the animation. In this case, you can use the
//                        // float value in the translationX property.
//                        float animatedValue = (float)updatedAnimation.getAnimatedValue();
//                        view.setTranslationX(animatedValue);
//                    }
//                });
//
//            }
//        });
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
