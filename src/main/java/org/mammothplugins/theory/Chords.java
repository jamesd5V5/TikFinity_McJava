package org.mammothplugins.theory;

import java.util.ArrayList;
import java.util.Arrays;

public class Chords {
    static String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static boolean isMajor(String[] chord) {
        String stringChord = String.join(" ", chord);
        for (int i = 0; i < chord.length; i++) {
            String[] majChord = getMajorChord(chord[i]);
            boolean containsAll = true;
            for (int e = 0; e < majChord.length; e++) {
                if (!stringChord.contains(majChord[e]))
                    containsAll = false;
                if (e == majChord.length - 1 && containsAll == true)
                    return true;
            }
        }
        return false;
    }

    public static boolean isMinor(String[] chord) {
        String stringChord = String.join(" ", chord);
        for (int i = 0; i < chord.length; i++) {
            String[] minChord = getMinorChord(chord[i]);
            boolean containsAll = true;
            for (int e = 0; e < minChord.length; e++) {
                if (!stringChord.contains(minChord[e]))
                    containsAll = false;
                if (e == minChord.length - 1 && containsAll == true)
                    return true;
            }
        }
        return false;
    }

    public static String[] getMajorChord(String root) {
        String third = "";
        String fifth = "";

        for (int i = 0; i < notes.length; i++)
            if (notes[i].equals(root)) {
                third = getNoteFromInterval(i, 4);
                fifth = getNoteFromInterval(i, 7);
            }

        String[] majorChord = {root, third, fifth};
        return majorChord;
    }

    public static String[] getMinorChord(String root) {
        String third = "";
        String fifth = "";

        for (int i = 0; i < notes.length; i++)
            if (notes[i].equals(root)) {
                third = getNoteFromInterval(i, 3);
                fifth = getNoteFromInterval(i, 7);
            }

        String[] majorChord = {root, third, fifth};
        return majorChord;
    }

    public static String[] getRootPosition(String[] chord) {
        ArrayList<String> list = new ArrayList<>();
        if (isMajor(chord)) {
            String stringChord = String.join(" ", chord);
            for (int i = 0; i < chord.length; i++) {
                String[] majChord = getMajorChord(chord[i]);
                boolean containsAll = true;
                for (int e = 0; e < majChord.length; e++) {
                    if (!stringChord.contains(majChord[e]))
                        containsAll = false;
                    if (e == majChord.length - 1 && containsAll == true)
                        list.addAll(Arrays.stream(majChord).toList());
                }
            }
        }
        if (isMinor(chord)) {
            String stringChord = String.join(" ", chord);
            for (int i = 0; i < chord.length; i++) {
                String[] minChord = getMinorChord(chord[i]);
                boolean containsAll = true;
                for (int e = 0; e < minChord.length; e++) {
                    if (!stringChord.contains(minChord[e]))
                        containsAll = false;
                    if (e == minChord.length - 1 && containsAll == true)
                        list.addAll(Arrays.stream(minChord).toList());
                }
            }
        }

        return list.toArray(new String[list.size()]);
    }

    private static String getNoteFromInterval(int i, int interval) {
        int indexOfNote = i + interval;
        if (notes.length <= i + interval)
            indexOfNote = (i + interval) - notes.length;
        return notes[indexOfNote];
    }
}
