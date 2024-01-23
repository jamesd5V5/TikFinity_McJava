package org.mammothplugins.recording;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.mc_piano.Mc_Piano;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.HashMap;

public class MidiPiano extends BukkitRunnable implements Receiver {
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    private static boolean isRunning;
    private long listeningSpeed = 20L;
    private static HashMap<String, Long> currentNotes = new HashMap<>();

    public void listen() {
        MidiPiano musicTest = new MidiPiano();
        if (isRunning)
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    if (isRunning == false)
                        cancel();
                    count++;
                    try {
                        musicTest.play();
                    } catch (Exception e) {
                    }
                }
            }.runTaskTimer(Mc_Piano.getInstance(), 0L, 20L);
    }

    public static void pianoOn() {
        isRunning = true;
    }

    public static void pianoOff() {
        isRunning = false;
    }


//    private void play() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
//        MidiDevice inputDevice = MidiSystem.getMidiDevice(infos[5]);
//        //MidiDevice outputDevice = MidiSystem.getMidiDevice(infos[0]);
//
//        Sequencer sequencer = MidiSystem.getSequencer();
//        Transmitter transmitter;
//        Receiver receiver;
//
//        inputDevice.open();
//        sequencer.open();
//        transmitter = inputDevice.getTransmitter();
//        receiver = sequencer.getReceiver();
//        transmitter.setReceiver(receiver);
//
//        Sequence seq = new Sequence(Sequence.PPQ, 24);
//        Track currentTrack = seq.createTrack();
//        sequencer.setSequence(seq);
//        sequencer.setTickPosition(0);
//        sequencer.recordEnable(currentTrack, -1);
//        sequencer.startRecording();
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (sequencer.isRecording()) {
//                    sequencer.stopRecording();
//                    Sequence tmp = sequencer.getSequence();
//                    try {
//                        readMidi(tmp);
//                    } catch (Exception e) {
//                    }
//                    //MidiSystem.write(tmp, 0, new File("MyMidiFile.mid"));
//                }
//            }
//        }.runTaskLater(Mc_Piano.getInstance(), listeningSpeed);
//    }

    private void play() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        MidiDevice inputDevice = MidiSystem.getMidiDevice(infos[5]);
        inputDevice.open();
        Transmitter transmitter = inputDevice.getTransmitter();

        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        Receiver receiver = sequencer.getReceiver();

        // Set up the receiver to process incoming MIDI events as they arrive
        transmitter.setReceiver(new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        currentNotes.put(noteName, 20L);
                        noteCommands(noteName);
                        Common.broadcast("On");
                    } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        Common.broadcast("Off");
                    }
                }
            }

            @Override
            public void close() {
            }
        });

        // Set up the sequencer to use a dummy sequence and track
        Sequence seq = new Sequence(Sequence.PPQ, 24);
        Track currentTrack = seq.createTrack();
        sequencer.setSequence(seq);
        sequencer.setTickPosition(0);

        // Start the sequencer and wait for MIDI events to arrive
        sequencer.start();
        while (isRunning) {
            Thread.sleep(100);
        }
    }

    private void readMidi(Sequence sequence) throws InvalidMidiDataException, IOException {
        String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        int NOTE_ON = 0x90;
        int NOTE_OFF = 0x80;
        //Sequence sequence = MidiSystem.getSequence(new File("MyMidiFile.mid"));
        for (Track track : sequence.getTracks())
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    int command = sm.getCommand();
                    int data1 = sm.getData1();
                    int data2 = sm.getData2();
                    if (command == NOTE_ON) {
                        int key = data1;
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = data2;
                        System.out.println("Note on: " + noteName + octave + " velocity: " + velocity);
                    } else if (command == NOTE_OFF) {
                        int key = data1;
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = data2;
                        System.out.println("Note off: " + noteName + octave + " velocity: " + velocity);
                    }
                }
            }
    }

    public void noteCommands(String noteName) {
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


    int count = 0;

    @Override
    public void run() {
        if (isRunning) {
            count++;
            ///for (Player player : Remain.getOnlinePlayers())
            //CompSound.NOTE_PIANO.play(player.getLocation(), CompSound.DEFAULT_VOLUME, 100 + count);
            ///player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(count, Note.Tone.C));

//            for (String key : currentNotes.keySet())
//                currentNotes.put(key, currentNotes.get(key).longValue() - 10);
//
//
//            for (Iterator<Map.Entry<String, Long>> it = currentNotes.entrySet().iterator(); it.hasNext(); ) {
//                Map.Entry<String, Long> entry = it.next();
//                if (entry.getValue() <= 0) {
//                    it.remove();
//                }
//            }
        } else
            cancel();
    }


    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage sm && sm.getCommand() == ShortMessage.NOTE_ON) {
            Common.broadcast("Message: " + message.toString());

        }
    }

    @Override
    public void close() {

    }
}
