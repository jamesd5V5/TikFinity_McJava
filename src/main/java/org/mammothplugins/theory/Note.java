package org.mammothplugins.theory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;

public class Note {

    @Getter
    @Setter
    private static ArrayList<Note> currentNotes = new ArrayList<>();

    @Getter
    private String noteName;
    @Getter
    private int octave;
    @Getter
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

    public static boolean containsNote(Note note, boolean withVelocity) {
        if (!currentNotes.isEmpty())
            for (Note currentNote : currentNotes)
                if (withVelocity == false)
                    if (currentNote.noteName.equals(note.noteName) && currentNote.octave == note.octave)
                        return true;
        return false;
    }

    public static boolean containsNoteWithinOctave(String noteName, int octave) {
        for (int i = 0; i < currentNotes.size(); i++)
            if (currentNotes.get(i).noteName.equals(noteName) && (currentNotes.get(i).octave == octave
                    || currentNotes.get(i).octave == octave - 1) || currentNotes.get(i).octave == octave + 1)
                return true;

        return false;
    }

    public static void addNote(Note note) {
        currentNotes.add(note);
    }

    public static void removeNote(Note note) {
        if (containsNote(note, false))
            for (int i = 0; i < currentNotes.size(); i++)
                if (currentNotes.get(i).noteName.equals(note.noteName) && currentNotes.get(i).octave == note.octave) {
                    currentNotes.remove(i);
                    break;
                }
    }

    public static void clearNotes() {
        currentNotes.clear();
    }

    public static void playNote(Note note) {
        for (Player player : Remain.getOnlinePlayers()) {
            int octave = note.octave;
            if (octave <= 2)
                CompSound.NOTE_BASS.play(player, CompSound.DEFAULT_VOLUME, getFrequency(note, 1));
            else if (octave <= 4)
                CompSound.NOTE_PIANO.play(player, CompSound.DEFAULT_VOLUME, getFrequency(note, 3));
            else if (octave <= 6)
                CompSound.BLOCK_NOTE_BLOCK_BELL.play(player, CompSound.DEFAULT_VOLUME, getFrequency(note, 5));
        }
    }

    public static Float getFrequency(Note note, int baseOctave) {
        float frequency = 0.0f;

        switch (note.noteName) {
            case "F#":
                frequency = (note.octave == baseOctave) ? 0.5f : 1.0f;
                break;
            case "G":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -11.0 / 12.0) : (float) Math.pow(2, 1.0 / 12.0);
                break;
            case "G#":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -10.0 / 12.0) : (float) Math.pow(2, 2.0 / 12.0);
                break;
            case "A":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -9.0 / 12.0) : (float) Math.pow(2, 3.0 / 12.0);
                break;
            case "A#":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -8.0 / 12.0) : (float) Math.pow(2, 4.0 / 12.0);
                break;
            case "B":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -7.0 / 12.0) : (float) Math.pow(2, 5.0 / 12.0);
                break;
            case "C":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -6.0 / 12.0) : (float) Math.pow(2, 6.0 / 12.0);
                break;
            case "C#":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -5.0 / 12.0) : (float) Math.pow(2, 7.0 / 12.0);
                break;
            case "D":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -4.0 / 12.0) : (float) Math.pow(2, 8.0 / 12.0);
                break;
            case "D#":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -3.0 / 12.0) : (float) Math.pow(2, 9.0 / 12.0);
                break;
            case "E":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -2.0 / 12.0) : (float) Math.pow(2, 10.0 / 12.0);
                break;
            case "F":
                frequency = (note.octave == baseOctave) ? (float) Math.pow(2, -1.0 / 12.0) : (float) Math.pow(2, 11.0 / 12.0);
                break;
            default:
                System.out.println("Invalid note name.");
        }

        System.out.println("Frequency of " + note.noteName + " in octave " + note.octave + ": " + frequency);

        return frequency;
    }
}
