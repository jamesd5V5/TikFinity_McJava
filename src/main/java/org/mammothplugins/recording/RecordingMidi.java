package org.mammothplugins.recording;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.mammothplugins.mc_piano.Mc_Piano;
import org.mammothplugins.theory.Note;
import org.mammothplugins.theory.TheoryBase;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;

import javax.sound.midi.*;

public class RecordingMidi {

    @Getter
    private static boolean isRecording;

    @Getter
    private static MidiDevice midiDevice;
    @Getter
    private static Sequencer sequencer;

    private static BukkitTask recordingTask;
    private static LiveReceiver receiver;

    private static int num = 0;
    private static MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();


    private static Sequence seq;
    private static Track currentTrack;
    private static Transmitter transmitter;

    public static void record() throws MidiUnavailableException, InvalidMidiDataException {
        try {
            midiDevice = MidiSystem.getMidiDevice(infos[5]);

            sequencer = MidiSystem.getSequencer();
            receiver = new LiveReceiver(num++);
            transmitter = midiDevice.getTransmitter();

            midiDevice.open();
            sequencer.open();

            transmitter.setReceiver(receiver);
            seq = new Sequence(Sequence.PPQ, 24);

            currentTrack = seq.createTrack();

            // seq.deleteTrack(currentTrack);
            sequencer.setSequence(seq);
            sequencer.setTickPosition(0);

            // Start the sequencer and wait for MIDI events to arrive
            sequencer.recordEnable(currentTrack, -1);
            sequencer.startRecording();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listen() throws MidiUnavailableException, InvalidMidiDataException {
        if (isRecording) {
            record();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isRecording() == false) {
                        cancel();
                        return;
                    }
                    TheoryBase.checkNotes();
                }
            }.runTaskTimer(Mc_Piano.getInstance(), 0, 2L);
        }

    }

    public static void isRecording(boolean r) {
        try {
            isRecording = r;

            if (isRecording == false) {
                if (receiver != null)
                    receiver.close();
                if (recordingTask != null)
                    recordingTask.cancel();
                if (sequencer != null) {
                    if (sequencer.isRecording()) {
                        sequencer.stopRecording();
                        sequencer.stop();
                        sequencer.close();
                    }
                    if (sequencer.isRunning())
                        sequencer.close();
                }
                if (midiDevice != null)
                    if (midiDevice.isOpen())
                        midiDevice.close();
                if (seq != null)
                    seq.deleteTrack(currentTrack);
                if (transmitter != null)
                    transmitter.close();

                sequencer = null;
                midiDevice = null;
                recordingTask = null;
                receiver = null;
                seq = null;
                currentTrack = null;
                transmitter = null;

                Note.clearNotes();
            }
        } catch (Exception e) {
        }

    }

    public static void check() {
        Common.broadcast("Sequencer: " + (sequencer == null));
        Common.broadcast("Midi: " + (midiDevice == null));
        Common.broadcast("Task: " + (recordingTask == null));
        Common.broadcast("Reciever: " + (receiver == null));
    }
}
