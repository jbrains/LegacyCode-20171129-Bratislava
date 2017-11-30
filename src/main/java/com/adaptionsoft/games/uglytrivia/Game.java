package com.adaptionsoft.games.uglytrivia;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    private final Map<String, Queue<String>> questionDecks;
    protected int[] places = new int[6];
    ArrayList players = new ArrayList();
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        this(new StandardQuestions().getQuestionDecks());
    }

    public Game(final Map<String, Queue<String>> questionDecks) {
        this.questionDecks = questionDecks;
        this.popQuestions = new StandardQuestions().convertSafelyToLegacyQuestionDeck("Pop");
        this.sportsQuestions = new StandardQuestions().convertSafelyToLegacyQuestionDeck("Sports");
        this.scienceQuestions = new StandardQuestions().convertSafelyToLegacyQuestionDeck("Science");
        this.rockQuestions = new StandardQuestions().convertSafelyToLegacyQuestionDeck("Rock");
    }

    public Map<String, LinkedList<String>> getLegacyQuestionDecks() {
        // SMELL Depends on package-level fields in superclass
        return HashMap.of(
                "Pop", popQuestions,
                "Rock", rockQuestions,
                "Science", scienceQuestions,
                "Sports", sportsQuestions
        );
    }

    /** @deprecated Scheduled for removal 2018-05-31 */
    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= 2);
    }

    public boolean add(String playerName) {
        // REFACTOR Mutates players; make this more obvious.
        addPlayerNamed(playerName);
        onPlayerAdded(playerName, players.size());
        return true;
    }

    private void onPlayerAdded(final String playerName, final int inPosition) {
        reportMessage(playerName + " was added");
        reportMessage("They are player number " + inPosition);
    }

    private void addPlayerNamed(final String playerName) {
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;
    }

    protected void reportMessage(final String message) {
        System.out.println(message);
    }

    public int howManyPlayers() {
        return players.size();
    }

    // REFACTOR It would be nice if currentPlayer and players
    // didn't change during a single invocation of this method!
    public void roll(int roll) {
        onTurnStarted((String) players.get(currentPlayer), roll);

        if (isCurrentPlayerInPenaltyBox()) {
            if (getsOutOfThePenaltyBoxWhenRolling(roll)) {
                signalCurrentPlayerIsGettingOutOfThePenaltyBox();
                onPlayerGettingOutOfThePenaltyBox((String) players.get(currentPlayer));
                advanceCurrentPlayerBy(roll);
                askQuestionToCurrentPlayer();
            } else {
                onPlayerStayingInPenaltyBox((String) players.get(currentPlayer));
                signalCurrentPlayerIsNotGettingOutOfThePenaltyBox();
            }
        } else {
            advanceCurrentPlayerBy(roll);
            askQuestionToCurrentPlayer();
        }
    }

    private void onTurnStarted(final String playerName, final int roll) {
        reportCurrentPlayerName(playerName);
        reportPlayerHasRolled(playerName, roll);
    }

    private void askQuestionToCurrentPlayer() {
        onPlayerLandedAt((String) players.get(currentPlayer), places[currentPlayer]);
        reportCategoryForQuestion(currentCategory());
        askQuestion();
    }

    private void signalCurrentPlayerIsNotGettingOutOfThePenaltyBox() {
        isGettingOutOfPenaltyBox = false;
    }

    private void onPlayerStayingInPenaltyBox(final String playerName) {
        System.out.println(playerName + " is not getting out of the penalty box");
    }

    private void reportCategoryForQuestion(final String category) {
        System.out.println("The category is " + category);
    }

    private void onPlayerLandedAt(final String playerName, final int place) {
        System.out.println(playerName
                + "'s new location is "
                + place);
    }

    private void advanceCurrentPlayerBy(final int roll) {
        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
    }

    private void onPlayerGettingOutOfThePenaltyBox(final String playerName) {
        System.out.println(playerName + " is getting out of the penalty box");
    }

    private void signalCurrentPlayerIsGettingOutOfThePenaltyBox() {
        isGettingOutOfPenaltyBox = true;
    }

    private boolean getsOutOfThePenaltyBoxWhenRolling(final int roll) {
        return roll % 2 != 0;
    }

    private boolean isCurrentPlayerInPenaltyBox() {
        return inPenaltyBox[currentPlayer];
    }

    private void reportPlayerHasRolled(final String playerName, final int roll) {
        reportMessage("They have rolled a " + roll);
    }

    private void onTurnStarted(final String currentPlayerName) {
        reportCurrentPlayerName(currentPlayerName);
    }

    private void reportCurrentPlayerName(final String currentPlayerName) {
        reportMessage(currentPlayerName + " is the current player");
    }

    // REFACTOR Replace with lookup table
    private void askQuestion() {
        if (currentCategory() == "Pop")
            reportMessage((String) popQuestions.removeFirst());
        if (currentCategory() == "Science")
            reportMessage((String) scienceQuestions.removeFirst());
        if (currentCategory() == "Sports")
            reportMessage((String) sportsQuestions.removeFirst());
        if (currentCategory() == "Rock")
            reportMessage((String) rockQuestions.removeFirst());
    }

    // REFACTOR Replace with lookup table and formula.
    private String currentCategory() {
        if (places[currentPlayer] == 0) return "Pop";
        if (places[currentPlayer] == 4) return "Pop";
        if (places[currentPlayer] == 8) return "Pop";
        if (places[currentPlayer] == 1) return "Science";
        if (places[currentPlayer] == 5) return "Science";
        if (places[currentPlayer] == 9) return "Science";
        if (places[currentPlayer] == 2) return "Sports";
        if (places[currentPlayer] == 6) return "Sports";
        if (places[currentPlayer] == 10) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (isCurrentPlayerInPenaltyBox()) {
            if (currentPlayerIsGettingOutOfThePenaltyBox()) {
                reportAnswerWasCorrect();
                giveCurrentPlayerAGoldCoin();
                reportPlayerHasGoldCoins((String) players.get(currentPlayer), purses[currentPlayer]);

                // REFACTOR Temporal coupling: advanceToNextPlayer() writes
                // to shared memory and didPlayerWin() reads from it.
                boolean winner = didPlayerWin();
                advanceToNextPlayer();
                return winner;
            } else {
                advanceToNextPlayer();
                return true;
            }
        } else {
            reportAnswerWasCorrectWithTypo();
            giveCurrentPlayerAGoldCoin();
            reportPlayerHasGoldCoins((String) players.get(currentPlayer), purses[currentPlayer]);

            // REFACTOR Temporal coupling: advanceToNextPlayer() writes
            // to shared memory and didPlayerWin() reads from it.
            boolean winner = didPlayerWin();
            advanceToNextPlayer();
            return winner;
        }
    }

    private void reportAnswerWasCorrectWithTypo() {
        System.out.println("Answer was corrent!!!!");
    }

    private void advanceToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    private void reportPlayerHasGoldCoins(final String playerName, final int goldCoins) {
        System.out.println(playerName
                + " now has "
                + goldCoins
                + " Gold Coins.");
    }

    private void giveCurrentPlayerAGoldCoin() {
        purses[currentPlayer]++;
    }

    private void reportAnswerWasCorrect() {
        System.out.println("Answer was correct!!!!");
    }

    private boolean currentPlayerIsGettingOutOfThePenaltyBox() {
        return isGettingOutOfPenaltyBox;
    }

    public boolean wrongAnswer() {
        onPlayerAnsweredQuestionIncorrectly((String) players.get(currentPlayer));
        putCurrentPlayerInThePenaltyBox();
        advanceToNextPlayer();
        return true;
    }

    private void putCurrentPlayerInThePenaltyBox() {
        inPenaltyBox[currentPlayer] = true;
    }

    private void onPlayerAnsweredQuestionIncorrectly(final String players) {
        System.out.println("Question was incorrectly answered");
        System.out.println(players + " was sent to the penalty box");
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }

    public Map<String, Queue<String>> getQuestionDecks() {
        return questionDecks;
    }
}
