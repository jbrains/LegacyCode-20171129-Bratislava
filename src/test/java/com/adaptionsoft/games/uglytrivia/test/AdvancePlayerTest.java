package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import io.vavr.collection.List;
import org.junit.Assert;
import org.junit.Test;

public class AdvancePlayerTest {
    @Test
    public void theTestsBecauseJunit4StillDoesParameterizedTestsPoorly() throws Exception {
        theOnlyPlayerShouldAdvanceByRoll(0, 4, 4);
        theOnlyPlayerShouldAdvanceByRoll(2, 5, 7);
        // The Board is circular with 12 squares
        theOnlyPlayerShouldAdvanceByRoll(8, 6, 2);
        theOnlyPlayerShouldAdvanceByRoll(0, 12, 0);
        // MISTAKE!
        theOnlyPlayerShouldAdvanceByRoll(3, 29, 8);
    }

    private void theOnlyPlayerShouldAdvanceByRoll(final int startingPlace, final int roll, final int expectedEndingPlace) {
        Assert.assertEquals(expectedEndingPlace, Game.advancePlayerAroundBoardBy(startingPlace, roll));
    }
}
