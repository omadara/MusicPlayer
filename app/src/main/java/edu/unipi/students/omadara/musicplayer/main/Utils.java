package edu.unipi.students.omadara.musicplayer.main;

public class Utils {
    public static String durationToString(long duration) {
        long sec, min, h;

        duration /= 1000;
        sec = duration % 60;
        duration /= 60;
        min = duration % 60;
        duration /= 60;
        h = duration % 24;

        return (String.format(h > 0 ? "%d:%02d:%02d" : "%2$d:%3$02d", h, min, sec));
    }
}
