package com.adaptionsoft.games.uglytrivia;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    private final QuestionDecks questionDecks;
    
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

    public Game(final Map<String, Queue<String>> nakedQuestionDecks) {
        this.questionDecks = new QuestionDecks(nakedQuestionDecks);
        this.popQuestions = questionDecks.convertSafelyToLegacyQuestionDeck("Pop");
        this.sportsQuestions = questionDecks.convertSafelyToLegacyQuestionDeck("Sports");
        this.scienceQuestions = questionDecks.convertSafelyToLegacyQuestionDeck("Science");
        this.rockQuestions = questionDecks.convertSafelyToLegacyQuestionDeck("Rock");
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
        places[currentPlayer] = advancePlayerAroundBoardBy(places[currentPlayer], roll);
    }

    public static int advancePlayerAroundBoardBy(final int fromPlace, final int roll) {
        return (fromPlace + roll) % 12;
    }

    private void onPlayerGettingOutOfThePenaltyBox(final String playerName) {
        System.out.println(playerName + " is getting out of the penalty box");
    }

    private void signalCurrentPlayerIsGettingOutOfThePenaltyBox() {
        isGettingOutOfPenaltyBox = true;
    }

    // REFACTOR This looks like part of a Rule-type class
    private boolean getsOutOfThePenaltyBoxWhenRolling(final int roll) {
        return roll % 2 != 0;
    }

    private boolean isCurrentPlayerInPenaltyBox() {
        return inPenaltyBox[currentPlayer];
    }

    private void reportPlayerHasRolled(final String playerName, final int roll) {
        reportMessage("They have rolled a " + roll);
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
        return findCategoryNameByPlace(places[currentPlayer]);
    }

    public static String findCategoryNameByPlace(final int place) {
        if (place >= 0 && place < 12) {
            return List.of("Pop", "Science", "Sports", "Rock").get(place % 4);
        }
        else {
            return "Rock";
        }
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
        return questionDecks.getQuestionDecks();
    }
}
