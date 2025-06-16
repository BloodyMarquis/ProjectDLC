package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects;

import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ITouchable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Button extends Sprite implements ITouchable {
    public interface OnTouchListener {
        public boolean onTouch(boolean pressed);
    }
    protected OnTouchListener listener;
    private static final String TAG = Button.class.getSimpleName();
    public Button(int bitmapResId, float cx, float cy, float width, float height, OnTouchListener listener) {
        super(bitmapResId, cx, cy, width, height);
        this.listener = listener;
    }
    private int capturedPointerId = -1;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        float[] pts = Metrics.fromScreen(e.getX(pointerIndex), e.getY(pointerIndex));
        float x = pts[0], y = pts[1];
        //Log.d(TAG, "onTouch:" + this + " action=" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!dstRect.contains(x, y)) return false;
                capturedPointerId = pointerId;
                return listener.onTouch(true);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointerId == capturedPointerId) {
                    capturedPointerId = -1;
                    return listener.onTouch(false);
                }
                break;
        }
        return false;
    }
}
