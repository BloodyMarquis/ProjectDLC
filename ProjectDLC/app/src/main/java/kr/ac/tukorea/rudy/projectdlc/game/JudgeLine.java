package kr.ac.tukorea.rudy.projectdlc.game;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.rudy.projectdlc.R;

public class JudgeLine extends Sprite {
    public JudgeLine() {
        super(R.mipmap.down);
        setPosition(800f, NoteSprite.GOAL_Y, 1600f, 10f);
    }
}