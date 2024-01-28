package org.mammothplugins.recording;

import lombok.Getter;
import lombok.Setter;
import org.mineacademy.fo.Common;

import java.util.ArrayList;

public class Note {

    @Getter
    @Setter
    private static ArrayList<Note> currentNotes = new ArrayList<>();

    private String noteName;
    private int octave;
    private int velocity;

    public Note(String noteName, int octave, int velocity) {
        this.noteName = noteName;
        this.octave = octave;
        this.velocity = velocity;
    }

    @Override
    public boolean equals(Object note) {
        boolean whenFalse = false;
        if (note instanceof Note) {
            Note n = (Note) note;
            if (n.noteName.equals(noteName) && n.octave == octave && n.velocity == velocity)
                whenFalse = true;
        }
        return whenFalse;
    }

    public static boolean containsNote(Note note) {
        if (!currentNotes.isEmpty())
            for (Note currentNote : currentNotes)
                if (currentNote.equals(note))
                    return true;
        return false;
    }

    public static void addNote(Note note) {
        currentNotes.add(note);
    }

    public static void removeNote(Note note) {
        if (containsNote(note))
            currentNotes.add(note);
    }

}
