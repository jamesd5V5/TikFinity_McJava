package org.mammothplugins.command;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.mammothplugins.recording.RecordingMidi;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompSound;

public class PianoCommands extends SimpleCommand {
    public PianoCommands() {
        super("piano");
        setPermission("piano.commands");
    }

    @Override
    protected void onCommand() {
        this.checkConsole();
        boolean foundCommand = false;
        if (this.args.length == 1) {
            if ("on".equalsIgnoreCase(this.args[0])) {
                Common.tell(getSender(), "Piano is on.");
                RecordingMidi.isRecording(true);
                RecordingMidi.listen(); //Starts Loops

                foundCommand = true;
            }
            if ("off".equalsIgnoreCase(this.args[0])) {
                Common.tell(getSender(), "Piano is off.");
                RecordingMidi.isRecording(false);
                foundCommand = true;
            }
            if ("check".equalsIgnoreCase(this.args[0])) {
                RecordingMidi.check();
                foundCommand = true;
            }
        }
        if (this.args.length == 2) {
            if ("note".equalsIgnoreCase(this.args[0])) {
                getPlayer().playNote(getPlayer().getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.valueOf(args[1])));
                Common.tell(getSender(), "Playing note " + this.args[1]);
                foundCommand = true;
            }
        }
        if (!foundCommand) {
            Common.tell(this.getPlayer(), new String[]{"&cWrong Arguments! Use /piano for help."});
            CompSound.VILLAGER_NO.play(this.getPlayer());
        }
    }
}
