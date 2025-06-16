package kr.ac.tukorea.rudy.projectdlc.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.rudy.projectdlc.R;

public class Character extends Sprite {
    private static final float PLANE_WIDTH = 100f;
    private static final int PLANE_SRC_WIDTH = 80;
    private static final float SPEED = 300f;
    private float targetX;
    private static final float MAX_ROLL_TIME = 0.4f;
    private float rollTime;

    private float minX = 300f;
    private float maxX = 1300f;

    public Character() {
        super(R.mipmap.fighters);
        setPosition(Metrics.width / 2, Metrics.height - 200, PLANE_WIDTH, PLANE_WIDTH);
        targetX = x;

        srcRect = new Rect();
    }

    public void update() {
        updateRoll();
        if (targetX < x) {
            dx = -SPEED;
        } else if (x < targetX) {
            dx = SPEED;
        } else {
            dx = 0;
        }
        super.update();
        float adjx = x;
        if ((dx < 0 && x < targetX) || (dx > 0 && x > targetX)) {
            adjx = targetX;
        } else {
            adjx = Math.max(radius, Math.min(x, Metrics.width - radius));
        }
        if (adjx != x) {
            setPosition(adjx, y, PLANE_WIDTH, PLANE_WIDTH);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    private void updateRoll() {
        int sign = targetX < x ? -1 : x < targetX ? 1 : 0;
        if (x == targetX) {
            if (rollTime > 0) sign = -1;
            else if (rollTime < 0) sign = 1;
        }
        rollTime += sign * GameView.frameTime;
        if (x == targetX) {
            if (sign < 0 && rollTime < 0) rollTime = 0;
            if (sign > 0 && rollTime > 0) rollTime = 0;
        }
        if (rollTime < -MAX_ROLL_TIME) rollTime = -MAX_ROLL_TIME;
        else if (rollTime > MAX_ROLL_TIME) rollTime = MAX_ROLL_TIME;

        int rollIndex = 5 + (int)(rollTime * 5 / MAX_ROLL_TIME);
        srcRect.set(rollIndex * PLANE_SRC_WIDTH, 0, (rollIndex + 1) * PLANE_SRC_WIDTH, PLANE_SRC_WIDTH);
    }
    private void setTargetX(float x) {
        targetX = Math.max(minX, Math.min(x, maxX));
    }
    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetX(pts[0]);
                return true;

        }
        return false;
    }

    public void setTargetLeft() {
        setTargetX(x - 200f);
    }
    public void setTargetRight() {
        setTargetX(x + 200f);
    }
}

