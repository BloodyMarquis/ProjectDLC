package kr.ac.tukorea.rudy.projectdlc.game;

public class Bubble {
    public int lane;
    public float bar;
    public static Bubble parse(String line) {
        String[] comps = line.split("\\s+");
        if (comps.length < 3) return null;
        if (comps[0].equals("N") || comps[0].equals("D")) return null;

        Bubble bubble = new Bubble();
        bubble.lane = Integer.parseInt(comps[1]);
        bubble.bar = Float.parseFloat(comps[2]);
        return bubble;
    }
}