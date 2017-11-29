package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;

import java.util.Random;


public class GoldenMasterGameRunner {

    private static boolean notAWinner;

    public static void main(String[] args) {
        final GoldenMasterGameRunnerArguments goldenMasterGameRunnerArguments = parseArguments(args);
        final long sampleSize = goldenMasterGameRunnerArguments.sampleSize;

        for (long i = 0; i < sampleSize; i++) {
            final long seed = goldenMasterGameRunnerArguments.seed + i;
            printTestRunMetadata(seed);

            Game aGame = new Game();

            aGame.add("Chet");
            aGame.add("Pat");
            aGame.add("Sue");

            Random rand = new Random(seed);

            do {

                aGame.roll(rand.nextInt(5) + 1);

                if (rand.nextInt(9) == 7) {
                    notAWinner = aGame.wrongAnswer();
                } else {
                    notAWinner = aGame.wasCorrectlyAnswered();
                }


            } while (notAWinner);
        }
    }

    private static GoldenMasterGameRunnerArguments parseArguments(final String[] args) {
        final long seed = parseLong(args, 0, -1L);
        final long sampleSize = parseLong(args, 1, 1L);
        return new GoldenMasterGameRunnerArguments(seed, sampleSize);
    }

    private static long parseLong(final String[] args, final int index, final long valueIfAbsent) {
        try {
            return Long.parseLong(args[index], 10);
        } catch (RuntimeException oops) {
            return valueIfAbsent;
        }
    }

    private static void printTestRunMetadata(final long seed) {
        System.out.println("---");
        System.out.println(String.format("seed: %d", seed));
        System.out.println("---");
    }

    private static class GoldenMasterGameRunnerArguments {
        public final long seed;
        public final long sampleSize;

        public GoldenMasterGameRunnerArguments(final long seed, final long sampleSize) {
            this.seed = seed;
            this.sampleSize = sampleSize;
        }

        public boolean hasSeed() {
            return seed >= 0;
        }
    }
}
