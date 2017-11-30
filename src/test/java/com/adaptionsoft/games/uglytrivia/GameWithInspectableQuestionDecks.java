package com.adaptionsoft.games.uglytrivia;

import io.vavr.collection.HashMap;

import java.util.LinkedList;

// REFACTOR Move me out of this package!
public class GameWithInspectableQuestionDecks extends Game {
    public HashMap getQuestionDecks() {
        // SMELL Depends on package-level fields in superclass
        return HashMap.of(
                "Pop", popQuestions,
                "Rock", rockQuestions,
                "Science", scienceQuestions,
                "Sports", sportsQuestions
        );
    }
}
