package org.mammothplugins.recording;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.mammothplugins.mc_piano.Mc_Piano;
import org.mineacademy.fo.Common;

import javax.sound.midi.*;

public class RecordingMidi {

    @Getter
    private static boolean isRecording;

    @Getter
    private MidiDevice midiDevice;
    @Getter
    private Sequencer sequencer;

    private BukkitTask recordingTask;

    private static int num = 0;

    public void record() throws MidiUnavailableException, InvalidMidiDataException {
        try {
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            this.midiDevice = MidiSystem.getMidiDevice(infos[5]);

            this.sequencer = MidiSystem.getSequencer();
            LiveReceiver receiver = new LiveReceiver(num++, this);
            Transmitter transmitter = midiDevice.getTransmitter();

            midiDevice.open();
            sequencer.open();

            transmitter.setReceiver(receiver);

            Sequence seq = new Sequence(Sequence.PPQ, 24);
            Track currentTrack = seq.createTrack();
            sequencer.setSequence(seq);
            sequencer.setTickPosition(0);

            // Start the sequencer and wait for MIDI events to arrive
            sequencer.recordEnable(currentTrack, -1);
            sequencer.startRecording();

        } catch (Exception e) {
        }
    }

    public void listen() {
        RecordingMidi rm = this;
        if (isRecording)
            recordingTask = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        rm.record();
                    } catch (Exception e) {
                    }
                }
            }.runTaskTimer(Mc_Piano.getInstance(), 0L, 2L);
    }

    public void isRecording(boolean isRecording) {
        try {
            this.isRecording = isRecording;

            if (isRecording == false) {
                if (recordingTask != null)
                    recordingTask.cancel();
                if (sequencer != null) {
                    if (sequencer.isRecording())
                        sequencer.stopRecording();
                    if (sequencer.isRunning())
                        sequencer.close();
                }
                if (midiDevice != null) {
                    if (midiDevice.isOpen())
                        midiDevice.close();
                }
            }
        } catch (Exception e) {
        }

    }
}
