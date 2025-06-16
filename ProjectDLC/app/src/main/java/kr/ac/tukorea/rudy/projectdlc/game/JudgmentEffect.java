package kr.ac.tukorea.rudy.projectdlc.game;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.rudy.projectdlc.R;

public class JudgmentEffect extends Sprite implements IRecyclable {
    private float remainingTime;

    private JudgmentEffect() {
        super(0);
    }

    public static JudgmentEffect get(String judgment, float x, float y) {
        JudgmentEffect effect = Scene.top().getRecyclable(JudgmentEffect.class);
        if (effect == null) {
            effect = new JudgmentEffect();
        }
        return effect.init(judgment, x, y);
    }

    private JudgmentEffect init(String judgment, float x, float y) {
        int resId = getBitmapIdForJudgment(judgment);
        if (resId == 0) {
            remainingTime = 0;
            return this;
        }
        setImageResourceId(resId);
        setPosition(x, y, 200, 100);
        remainingTime = 0.5f;
        return this;
    }

    private static int getBitmapIdForJudgment(String judgment) {
        switch (judgment) {
            case "CRITICAL":
            case "PERFECT":
                return R.mipmap.judge_perfect;
            case "GOOD": return R.mipmap.judge_good;
            case "MISS":
                return R.mipmap.judge_miss;
            case "NONE":
            default:
                return 0;
        }
    }

    @Override
    public void update() {
        remainingTime -= GameView.frameTime;
        if (remainingTime <= 0f) {
            MainScene.scene.remove(MainScene.Layer.effect, this);
        }
    }

    @Override
    public void onRecycle() {
    }
}
