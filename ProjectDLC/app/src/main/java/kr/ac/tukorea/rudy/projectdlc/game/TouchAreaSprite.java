package kr.ac.tukorea.rudy.projectdlc.game;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.rudy.projectdlc.R;

public class TouchAreaSprite extends Sprite {
    private Character character;


    public TouchAreaSprite(float x, float y, float width, float height, Character character) {
        super(R.mipmap.dropshadow);
        this.character = character;
        setPosition(x, y, width, height);
    }

    public void onTouch(float tx) {
        float centerX = dstRect.centerX();
        if (tx < centerX) {
            character.setTargetLeft();
        } else {
            character.setTargetRight();
        }
    }

    public boolean contains(float x, float y) {
        return dstRect.contains(x, y);
    }
}