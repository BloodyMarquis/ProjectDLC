package kr.ac.tukorea.rudy.projectdlc.game;

public class ComboManager {
    private int combo = 0;
    private int maxCombo = 0;

    public void reset() {
        combo = 0;
    }

    public void increase() {
        combo++;
        if (combo > maxCombo) maxCombo = combo;
    }

    public void miss() {
        reset();
    }

    public int getCombo() {
        return combo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }
}