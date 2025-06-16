package kr.ac.tukorea.rudy.projectdlc.game;

public class Note {
    public int lane;
    public float bar;
    public static Note parse(String line, float bpm) {
        String[] comps = line.split("\\s+");
        if (comps.length < 3) return null;
        if (!comps[0].equals("N")) return null;

        Note note = new Note();
        note.lane = Integer.parseInt(comps[1]);
        note.bar = Float.parseFloat(comps[2]) * (240f / bpm);
        return note;
    }
}
