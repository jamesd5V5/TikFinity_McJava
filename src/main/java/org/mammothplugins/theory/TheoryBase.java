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
            String[] notes = new String[Note.getCurrentNotes().size()];
            for (int i = 0; i < currentNotes.size(); i++)
                notes[i] = currentNotes.get(i).getNoteName();

            Chords chord = new Chords(notes);
            Common.broadcast(chord.getRootNote() + "___" + chord.getChordType());
//            for (Note note : currentNotes) {
//                matchChords(note);
//            }

        }
    }

//    public static void matchMajor(Note note) {
//        String[] majorChord = Chords.getMajorChord(note.getNoteName());
//        boolean hasMajor = true;
//
//        for (int i = 0; i < majorChord.length; i++) {
//            if (!(Note.containsNoteWithinOctave(majorChord[i], note.getOctave()))) {
//                hasMajor = false;
//                break;
//            }
//        }
//
//        if (hasMajor)
//            Common.broadcast("Found Major Chord: " + Arrays.stream(majorChord).toList());
//
//    }
}
