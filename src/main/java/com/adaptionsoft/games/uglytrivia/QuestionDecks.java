package com.adaptionsoft.games.uglytrivia;

import io.vavr.collection.Map;
import io.vavr.collection.Queue;

import java.util.LinkedList;

public class QuestionDecks {
    private final Map<String, Queue<String>> questionDecks;

    public QuestionDecks(final Map<String, Queue<String>> questionDecks) {
        this.questionDecks = questionDecks;
    }

    public LinkedList convertSafelyToLegacyQuestionDeck(final String categoryName) {
        return new LinkedList(
                questionDecks.get(categoryName)
                        .getOrElse(Queue.empty()).toJavaList());
    }

    public Map<String, Queue<String>> getQuestionDecks() {
        return questionDecks;
    }
}