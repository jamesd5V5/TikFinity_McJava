package org.mammothplugins.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Ranks {

    RANK0(0, 0),
    RANK1(1, 25),
    RANK2(2, 50);

    @Getter
    private int rank;
    @Getter
    private int points;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
