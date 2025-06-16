package kr.ac.tukorea.rudy.projectdlc.game;

public class JudgeManager {
    private static final float CRITICAL_WINDOW = 1.5f / 60f;
    private static final float PERFECT_WINDOW = 3f / 60f;
    private static final float GOOD_WINDOW = 6f / 60f;
    public static final float MISS_WINDOW = 12f / 60f;

    private final int totalNotes;
    private final float baseScore;
    private final float bonusScorePerNote;

    private int criticalCount = 0;

    public JudgeManager(int totalNotes) {
        this.totalNotes = totalNotes;
        this.baseScore = 1_000_000f / totalNotes;
        this.bonusScorePerNote = 5_000f / totalNotes;
    }

    public JudgeResult judge(float noteTime, float currentTime) {
        float diff = Math.abs(noteTime - currentTime);

        if (diff <= CRITICAL_WINDOW) {
            criticalCount++;
            return JudgeResult.CRITICAL;
        }
        if (diff <= PERFECT_WINDOW) return JudgeResult.PERFECT;
        if (diff <= GOOD_WINDOW) return JudgeResult.GOOD;

        if (diff <= MISS_WINDOW) return JudgeResult.MISS;

        return JudgeResult.NONE;
    }

    public float getScore(JudgeResult result) {
        switch (result) {
            case CRITICAL:
                return baseScore + bonusScorePerNote;
            case PERFECT:
                return baseScore;
            case GOOD:
                return baseScore * 0.5f;
            default:
                return 0f;
        }
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public void debugPrintTotalScore(int perfect, int good, int miss) {
        float total = 0;
        total += criticalCount * (baseScore + bonusScorePerNote);
        total += perfect * baseScore;
        total += good * (baseScore * 0.5f);
        total += miss * 0f;
        System.out.println("총 점수 예측: " + total);
    }

}
