package com.adaptionsoft.games.uglytrivia;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;

import java.util.LinkedList;

// SMELL I have no idea what makes these the "standard" questions,
// but I can't think of anything else to call them so far.
public class StandardQuestions {
    private final QuestionDecks questionDecks;

    public StandardQuestions() {
        questionDecks = new QuestionDecks(HashMap.ofEntries(
                standardCategories()
                        .map(StandardQuestions::standardQuestionDeckForCategory)));
    }

    private static List<String> standardCategories() {
        return List.of("Pop", "Rock", "Science", "Sports");
    }

    private static Tuple2<String, Queue<String>> standardQuestionDeckForCategory(final String categoryName) {
        return new Tuple2<>(categoryName, generateQuestionsForCategory(50, categoryName));
    }

    private static Queue<String> generateQuestionsForCategory(final int howMany, final String categoryName) {
        return Queue.ofAll(List.range(0, howMany).map(i -> String.format("%s Question %d", categoryName, i)));
    }

    public Map<String, Queue<String>> getQuestionDecks() {
        return questionDecks.getQuestionDecks();
    }
}