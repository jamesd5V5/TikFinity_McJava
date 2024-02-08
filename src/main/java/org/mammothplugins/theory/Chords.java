package org.mammothplugins.theory;

import java.util.ArrayList;
import java.util.Arrays;

public class Chords {
    static String[] generalNotes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    private String[] notes;
    private String chordType;
    private String rootNote;

    public Chords(String[] notes) {
        this.notes = notes;
    }

    public int getSize() {
        return this.notes.length;
    }

    public String getNote(int i) {
        return notes[i];
    }

    public String getChordType() {
        if (chordType == null)
            if (getSize() == 3) {
                if (isMajor(notes))
                    chordType = "Major";
                if (isMinor(notes))
                    chordType = "Minor";
            }


        return chordType;
    }

    public String getRootNote() {
        if (getChordType() != null && rootNote == null) {
            for (String noteName : notes) {
                if (getSize() >= 3) {
                    String[] majorChord = Chords.getMajorChord(noteName);
                    String[] minorChord = Chords.getMinorChord(noteName);

                    if (inRange(majorChord, 6) || inRange(minorChord, 6))
                        return noteName;
                }
            }
        }
        return rootNote;
    }

    private boolean inRange(String[] chord, int octave) {
        for (int i = 0; i < chord.length; i++)
            if (!(Note.containsNoteWithinOctave(chord[i], octave))) //hardcoded to 6 for now,
                return false;
        return true;
    }

    //<editor-fold desc="Static Methods">
    //=======================================================================
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~STATIC~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //=======================================================================

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

        for (int i = 0; i < generalNotes.length; i++)
            if (generalNotes[i].equals(root)) {
                third = getNoteFromInterval(i, 4);
                fifth = getNoteFromInterval(i, 7);
            }

        String[] majorChord = {root, third, fifth};
        return majorChord;
    }

    public static String[] getMinorChord(String root) {
        String third = "";
        String fifth = "";

        for (int i = 0; i < generalNotes.length; i++)
            if (generalNotes[i].equals(root)) {
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
        if (generalNotes.length <= i + interval)
            indexOfNote = (i + interval) - generalNotes.length;
        return generalNotes[indexOfNote];
    }
    //</editor-fold>
}
