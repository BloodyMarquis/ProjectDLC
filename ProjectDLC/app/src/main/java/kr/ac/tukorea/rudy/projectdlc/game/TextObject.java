package kr.ac.tukorea.rudy.projectdlc.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;

public class TextObject implements IGameObject {
    public String text;
    protected float x, y;
    protected final RectF dstRect = new RectF();
    private final Paint paint;

    public TextObject(String text, float textSize, int color, Paint.Align al) {
        this.text = text;
        this.paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(al);
        paint.setColor(color);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        float width = paint.measureText(text);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float height = metrics.bottom - metrics.top;

        RectUtil.setRect(dstRect, x, y, width, height);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
    }
}