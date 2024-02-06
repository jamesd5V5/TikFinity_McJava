package org.mammothplugins.theory;

import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TheoryBase {

    public static void checkNotes() {
        ArrayList<Note> currentNotes = Note.getCurrentNotes();
        HashMap<Note, String[]> map = new HashMap<>();

        if (currentNotes.size() >= 3) {
            for (Note note : currentNotes) {
                String[] majorChord = Chords.getMajorChord(note.getNoteName());
                boolean hasMajor = true;

                for (int i = 0; i < majorChord.length; i++) {
                    if (!(Note.containsNoteWithinOctave(majorChord[i], note.getOctave()))) {
                        hasMajor = false;
                        break;
                    }
                }

                if (hasMajor)
                    Common.broadcast("Found Major Chord: " + Arrays.stream(majorChord).toList());
            }

        }
    }
}
