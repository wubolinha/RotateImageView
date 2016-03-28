package halin.rotateimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class RotateImageView extends ImageView{

    private static final String Tag = "bolin";

    private static final int ANIMATION_SPEED = 120; // 120 deg/sec

    private int mCurrentDegree = 0; // [0, 359]
    private int mStartDegree = 0;
    private int mTargetDegree = 0;

    private boolean mEnableAnimation = true;

    private long mAnimationStartTime = 0;

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageView(Context context) {
        super(context);
    }

    protected int getTargetDegree() {
        return mTargetDegree;
    }


    // Rotate the view counter-clockwise
    private void setRotateDegree(int degree ) {
        degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
        mTargetDegree = degree;
        if (mEnableAnimation) {
            mStartDegree = mCurrentDegree;
            mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis();
            int diff = mTargetDegree - mCurrentDegree;
            diff = diff >= 0 ? diff : 360 + diff; // make it in range [0, 359]
          //  Log.v(Tag,"diff "+diff);
        } else {
            return;
        }
        invalidate();//请求View树进行重绘
    }

    @Override
    protected void onDraw(Canvas canvas) {
     //   Log.v(Tag,"onDraw mEnableAnimation "+mEnableAnimation);
        Drawable drawable = getDrawable();
        if (drawable == null){
            Log.v(Tag,"drawable = null " );
            return;
        }

        Rect bounds = drawable.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;

        if (w == 0 || h == 0) {
            Log.v(Tag,"nothing to draw " );
            return;
        }
        if (mEnableAnimation) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            int deltaTime = (int)(time - mAnimationStartTime);
            int degree = mStartDegree + ANIMATION_SPEED
                    * (deltaTime > 0 ? deltaTime : -deltaTime) / 1000;
            degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
            mCurrentDegree = degree;
            invalidate();

        }

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int width = getWidth() - left - right;
        int height = getHeight() - top - bottom;

        int saveCount = canvas.getSaveCount();

        // Scale down the image first if required.
        if ((getScaleType() == ScaleType.FIT_CENTER) &&
                ((width < w) || (height < h))) {
            float ratio = Math.min((float) width / w, (float) height / h);
            canvas.scale(ratio, ratio, width / 2.0f, height / 2.0f);
        }
        canvas.translate(left + width / 2, top + height / 2);
        canvas.rotate(mCurrentDegree);
        canvas.translate(-w / 2, -h / 2);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public void stopRotate(boolean isRotate){
       // Log.v(Tag,"isRotate "+isRotate);
        if(isRotate){
            setRotateDegree(90);
        }
            mEnableAnimation = isRotate;
    }

}