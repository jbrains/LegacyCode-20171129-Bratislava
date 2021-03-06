package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.Assert;
import org.junit.Test;

public class RollDiceTest {
    @Test
    public void howTheFirstPlayerMovesOnTheFirstTurnRolling1() throws Exception {
        class TestableGame extends Game {
            public TestableGame(final int startingPlaceOfFirstPlayer) {
                super();
                add("::irrelevant player name::");
                places[0] = startingPlaceOfFirstPlayer;
            }

            @Override
            protected void reportMessage(final String message) {
                // Shut up.
            }

            public int findPlaceForPlayer(int playerIndex) {
                return places[playerIndex];
            }

            public int findPlaceForTheOnlyPlayer() {
                return findPlaceForPlayer(0);
            }
        }

        final TestableGame game = new TestableGame(0);
        game.roll(1);
        Assert.assertEquals(1, game.findPlaceForTheOnlyPlayer());
    }

    @Test
    public void howTheFirstPlayerMovesOnTheFirstTurnRolling6() throws Exception {
        class TestableGame extends Game {
            public TestableGame(final int startingPlaceOfFirstPlayer) {
                super();
                add("::irrelevant player name::");
                places[0] = startingPlaceOfFirstPlayer;
            }

            @Override
            protected void reportMessage(final String message) {
                // Shut up.
            }

            public int findPlaceForPlayer(int playerIndex) {
                return places[playerIndex];
            }

            public int findPlaceForTheOnlyPlayer() {
                return findPlaceForPlayer(0);
            }
        }

        final TestableGame game = new TestableGame(0);
        game.roll(6);
        Assert.assertEquals(6, game.findPlaceForTheOnlyPlayer());
    }
}
