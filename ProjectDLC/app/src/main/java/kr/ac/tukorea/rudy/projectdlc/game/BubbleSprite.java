package kr.ac.tukorea.rudy.projectdlc.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.rudy.projectdlc.R;

public class BubbleSprite extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final float BUBBLE_RADIUS = 68f;
    private static final float SPEED = 500f;
    private BubbleSprite(int mipmapID, float x, float y) {
        super(R.mipmap.bubble);
        setImageResourceId(mipmapID);
        setPosition(x, y, BUBBLE_RADIUS);
        dy = SPEED;
    }
    public static BubbleSprite get(int mipmapID, float x, float y) {
        BubbleSprite bubbleSprite = (BubbleSprite) Scene.top().getRecyclable(BubbleSprite.class);
        if(bubbleSprite == null) {
            bubbleSprite = new BubbleSprite(mipmapID, x, y);
        } else {
            bubbleSprite.setPosition(x, y, BUBBLE_RADIUS);
        }
        return bubbleSprite;
    }
    @Override
    public void update() {
        super.update();
        if(dstRect.bottom < 0) {
            Scene.top().remove(this);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public void onRecycle() {

    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.bubble;
    }
}
