package org.mammothplugins.recording;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Instruments {

    BASS(1, 2),
    PIANO(3, 4),
    BELLS(5, 6);

    private int startingOctave;
    private int endingOctave;
}
