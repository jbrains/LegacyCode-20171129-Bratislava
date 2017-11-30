package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.GameWithInspectableQuestionDecks;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class InitializeQuestionsTest {
    @Test
    public void goldenMaster() throws Exception {
        final Map<String, LinkedList<String>> questionDecks = new GameWithInspectableQuestionDecks().getQuestionDecks();
        Assert.assertEquals(
                questionDecks.mapValues(Queue::ofAll),
                createQuestionDecks());
    }

    private Map<String, Queue<String>> createQuestionDecks() {
        return HashMap.<String, Queue<String>> ofEntries(
                List.of("Pop", "Rock", "Science", "Sports")
                        .map(categoryName -> standardQuestionDeckForCategory(categoryName)));
    }

    private Tuple2 standardQuestionDeckForCategory(final String categoryName) {
        return new Tuple2(categoryName, generateQuestionsForCategory(50, categoryName));
    }

    private Queue<String> generateQuestionsForCategory(final int howMany, final String categoryName) {
        return Queue.ofAll(List.range(0, howMany).map(i -> String.format("%s Question %d", categoryName, i)));
    }
}
