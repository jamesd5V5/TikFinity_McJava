package org.mammothplugins.recording;

import org.bukkit.entity.Player;
import org.mammothplugins.theory.Chords;
import org.mammothplugins.theory.Note;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;
import java.util.HashMap;

public class LiveReceiver implements Receiver {

    private String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    private int num;

    public LiveReceiver(int num) {
        this.num = num;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        try {
            if (!RecordingMidi.isRecording()) {
                this.close();
                return;
            }
//            } else
//                Common.log("In Send: " + num);
            if (message instanceof ShortMessage sm) {
                int key = sm.getData1();
                int velocity = sm.getData2();
                //int octave = (key / 12) - 1;
                if (key <= 29)
                    return;
                int octave = ((key - 18) / 12); //Centering Around F#
                int n = key % 12;
                String noteName = NOTE_NAMES[n];
                Note note = new Note(noteName, octave, velocity);

                if (sm.getCommand() == ShortMessage.NOTE_ON && velocity != 0) {
                    Note.addNote(note);
                    Common.broadcast("On: " + noteName + "_" + octave + ": " + velocity);
                    Note.playNote(note);
                } else if (sm.getCommand() == ShortMessage.NOTE_ON && velocity == 0) {
                    Note.removeNote(note);
                    Common.broadcast("Off: " + noteName);
                }
            }
        } catch (Exception e) {
            Common.log("Exception " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (RecordingMidi.getSequencer() != null) {
                if (RecordingMidi.getSequencer().isRecording())
                    RecordingMidi.getSequencer().stopRecording();
                if (RecordingMidi.getSequencer().isRunning())
                    RecordingMidi.getSequencer().close();
            }
            if (RecordingMidi.getMidiDevice() != null) {
                if (RecordingMidi.getMidiDevice().isOpen())
                    RecordingMidi.getMidiDevice().close();
            }
        } catch (Exception e) {
        }
    }

    private void noteCommands(String noteName) { //Play the Notes Ingame
        HashMap<String, Float> noteFrequencies = new HashMap<>();
        noteFrequencies.put("C", 130.81F);
        noteFrequencies.put("C#", 138.59F);
        noteFrequencies.put("D", 146.83F);
        noteFrequencies.put("D#", 155.56F);
        noteFrequencies.put("E", 164.81F);
        noteFrequencies.put("F", 174.61F);
        noteFrequencies.put("F#", 185F);
        noteFrequencies.put("G", 196F);
        noteFrequencies.put("G#", 207.65F);
        noteFrequencies.put("A", 220F);
        noteFrequencies.put("A#", 233.08F);
        noteFrequencies.put("B", 246.94F);

        for (Player player : Remain.getOnlinePlayers()) {
            //if (noteName.contains("#"))
            //CompSound.NOTE_PIANO.play(player.getLocation(), CompSound.DEFAULT_VOLUME, );
            System.out.println(noteFrequencies.get(noteName));
            for (int i = 0; i < 100; i++) {
                CompSound.NOTE_PIANO.play(player.getLocation(), CompSound.DEFAULT_VOLUME, noteFrequencies.get(noteName));
            }
            //if (noteName.equalsIgnoreCase(note)) {
            //   Common.broadcast(noteName);

            // }
        }
    }
}
