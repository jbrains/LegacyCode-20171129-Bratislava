package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.StandardQuestions;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

// This only tells us what happens when we create the questions.
// All bets are off (at least so far) regarding what happens when
// we change the questions, such as when we ask one.
public class InitializeQuestionsTest {
    private final StandardQuestions standardQuestions = new StandardQuestions();

    @Test
    public void legacyQuestionDecksMatchNewStandardQuestionsDecks() throws Exception {
        final Map<String, LinkedList<String>> questionDecks = new Game().getLegacyQuestionDecks();
        Assert.assertEquals(
                questionDecks.mapValues(Queue::ofAll),
                standardQuestions.getQuestionDecks());
    }

    @Test
    public void gameInjectsStandardQuestionsByDefault() throws Exception {
        Assert.assertEquals(
                new Game().getQuestionDecks(),
                new Game(standardQuestions.getQuestionDecks()).getQuestionDecks());
    }

    @Test
    public void legacyQuestionDecksMatchNewQuestionDecks() throws Exception {
        Assert.assertEquals(
                new Game().getQuestionDecks().mapValues(Queue::toJavaList),
                new Game(standardQuestions.getQuestionDecks()).getLegacyQuestionDecks());
    }

    @Test
    public void legacyQuestionDecksMatchNewQuestionsDecksWhenNotTheStandardQuestions() throws Exception {
        final HashMap<String, Queue<String>> questionDecks = HashMap.of("Rock", Queue.of("::first question in Rock category::"));
        Assert.assertEquals(
                new Game(questionDecks).getQuestionDecks().mapValues(Queue::toJavaList),
                new Game(questionDecks).getLegacyQuestionDecks());
    }
}
