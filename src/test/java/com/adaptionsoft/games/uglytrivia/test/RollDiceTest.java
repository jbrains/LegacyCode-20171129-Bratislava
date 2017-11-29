package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.Assert;
import org.junit.Test;

public class RollDiceTest {
    @Test
    public void iHaveNoIdea() throws Exception {
        class TestableGame extends Game {
            @Override
            protected void reportMessage(final String message) {
                // Shut up.
            }

            public int findPlaceForPlayer(int playerIndex) {
                return places[playerIndex];
            }
        }
        final TestableGame game = new TestableGame();
        game.add("::irrelevant player name::");
        game.roll(1);
        Assert.assertEquals(1, game.findPlaceForPlayer(0));
    }
}
