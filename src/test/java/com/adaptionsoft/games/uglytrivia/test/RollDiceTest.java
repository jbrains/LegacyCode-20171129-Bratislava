package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.Test;

public class RollDiceTest {
    @Test
    public void iHaveNoIdea() throws Exception {
        class TestableGame extends Game {
            @Override
            protected void reportMessage(final String message) {
                // Shut up.
            }
        }
        final TestableGame game = new TestableGame();
        game.add("::irrelevant player name::");
        game.roll(1);
    }
}
