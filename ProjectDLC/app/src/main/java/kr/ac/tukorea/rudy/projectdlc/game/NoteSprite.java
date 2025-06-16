package kr.ac.tukorea.rudy.projectdlc.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.rudy.projectdlc.R;

public class NoteSprite extends Sprite implements IRecyclable {
    private static final float X_SPACE = 200f;
    private static final float LEFT = 800f - 2 * X_SPACE;
    private static final float WIDTH = 192f;
    private static final float HEIGHT = 192f;
    public static final float SPEED = 250f;
    public static final float GOAL_Y = 650f;
    protected Note note;


    private NoteSprite() {
        super(R.mipmap.tapred);
        setPosition(0, 0, WIDTH, HEIGHT);
    }

    public static NoteSprite get(Note note) {
        NoteSprite ns = Scene.top().getRecyclable(NoteSprite.class);
        if (ns == null) {
            ns = new NoteSprite();
        }
        return ns.init(note);
    }

    private NoteSprite init(Note note) {
        this.note = note;

        Bitmap resId = getBitmapForLane(note.lane);
        setBitmap(resId);

        float x = LEFT + note.lane * X_SPACE;
        float y = -note.bar;
        setPosition(x, y);
        return this;
    }

    public void update() {
        float musicTime = MainScene.scene.getMusicTime();
        float timeDiff = note.bar - musicTime;
        float y = GOAL_Y - timeDiff * SPEED;
        if (y > Metrics.height + HEIGHT) {
            MainScene.scene.remove(MainScene.Layer.note, this);
            return;
        }
        setPosition(x, y);
    }

    public RectF getBoundingRect() {
        return dstRect;
    }

    public static float screenfulTime() {
        return Metrics.height / SPEED;
    }

    private Bitmap getBitmapForLane(int lane) {
        Resources res = GameActivity.activity.getResources();
        int resId;
        switch (lane) {
            case 0: resId = R.mipmap.tappink; break;
            case 1: resId = R.mipmap.tapred; break;
            case 2: resId = R.mipmap.tapgreen; break;
            case 3: resId = R.mipmap.tapblue; break;
            case 4: resId = R.mipmap.tappurple; break;
            default: resId = R.mipmap.tapred; break;
        }
        return BitmapFactory.decodeResource(res, resId);
    }

    @Override
    public void onRecycle() {
        note = null;
    }
}