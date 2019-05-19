package GamePlay;

import java.util.ArrayList;
import java.util.List;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;
import playingCards.Card;
import playingCards.Deck;

public class Game {

  private int numberOfPlayers;
  // the card which must lead first each round
  private Card startingCard;
  // the players playing the game
  public List<Player> players = new ArrayList<Player>(numberOfPlayers);
  // a list of cumulative scores after each round
  private List<ScoreList> scoreHistory = new ArrayList<ScoreList>(10);
  // the deck to be used for playing (shuffle before each new use!)
  private Deck deck = new Deck();

  // return the score of a player at the end of the most recent round
  private int getCurrentScore(int playerIndex) {
    if (scoreHistory.size() == 0) {
      return 0;
    }
    return scoreHistory.get(scoreHistory.size() - 1).get(playerIndex);
  }

  // the minimum score among all players
  private int getMinScore() {
    int lowestScore = Integer.MIN_VALUE;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      lowestScore = Math.min(lowestScore, this.getCurrentScore(playerIndex));
    }
    return lowestScore;
  }

  // the maximum score among all players
  private int getMaxScore() {
    int highestScore = Integer.MAX_VALUE;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      highestScore = Math.max(highestScore, this.getCurrentScore(playerIndex));
    }
    return highestScore;
  }

  // is there a strict first place (not shared)
  private boolean hasUniqueFirstPlace() {
    int minScore = getMinScore();
    int howMany = 0;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (getCurrentScore(playerIndex) == minScore) {
        howMany++;
      }
    }
    return howMany == 1;
  }

  // the game is over if someone hits 100 and there is a clear winner
  private boolean isOver() {
    return hasUniqueFirstPlace() & getMaxScore() >= 100;
  }

  // initialize by supplying player names and whether they are human controlled
  // remove the lowest clubs from the deck until an even split remains
  public Game(List<String> playerNames, List<Boolean> isHuman) {
    // set number of players
    numberOfPlayers = playerNames.size();
    // initialize players
    for (int index = 0; index < numberOfPlayers; index++) {
      if (isHuman.get(index)) {
        players.add(new HumanPlayer(playerNames.get(index)));
      } else {
        players.add(new ComputerPlayer(playerNames.get(index)));
      }
    }
    // discard from top of deck until even split between all players
    // assumes deck is sorted (maybe 2D should be removed first??)
    while (deck.size() % numberOfPlayers > 0) {
      System.out.println("Playing without the " + deck.getFirst() + ".");
      deck.removeFirst();
    }
    // set lowest card, to lead each round
    startingCard = deck.getFirst();
  }

  // add to the score history; account for shooting the moon and such
  private void addScore(ScoreList score) {
    ScoreList newTotalScore = new ScoreList();
    if (score.shotTheMoon()) {
      // if the moon was shot then modify the score
      if (normalShootingWouldLose(score.indexOf(26))) {
        // if adding 26 to everyone else would cause the winner to lose the game
        // then subtract 26 from the winner instead
        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
          score.set(playerIndex, -score.get(playerIndex));
        }
      } else {
        // otherwise winner gets 0 and everyone else gets 26
        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
          score.set(playerIndex, -(score.get(playerIndex) - 13) + 13);
        }
      }
    }
    // now actually add scores, after potential above modifications
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      newTotalScore.set(playerIndex, getCurrentScore(playerIndex) + score.get(playerIndex));
    }
    scoreHistory.add(score);
  }

  // would adding 26 to everyone else cause them to lose?
  // annoying because can't use the methods already written
  private boolean normalShootingWouldLose(int winner) {
    ScoreList hypotheticalNewTotalScore = new ScoreList();
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (playerIndex == winner) {
        hypotheticalNewTotalScore.set(playerIndex, 0);
      } else {
        hypotheticalNewTotalScore.set(playerIndex, 26);
      }
    }
  }

  // to display after each round
  private void displayScoresHistory() {
    int maxNameWidth = 03;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      maxNameWidth = Math.max(maxNameWidth, players.get(playerIndex).getName().length());
    }
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      System.out.printf("%" + maxNameWidth + "s ", players.get(playerIndex).getName());
    }
    System.out.println();
    for (ScoreList scores : scoreHistory) {
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        System.out.printf("%" + maxNameWidth + "s ", scores.get(playerIndex));
      }
      System.out.println();
    }
  }

  private void displayWinner() {
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (getCurrentScore(playerIndex) == getMinScore()) {
        System.out.println();
        System.out.println("The winner is " + players.get(playerIndex).getName() + ". Congratulations!");
      }
    }
  }

  public void play() {
    // while the game is not over, play a round
    while (!isOver()) {
      // initialize
      deck.shuffle();
      int roundNumber = scoreHistory.size() + 1;
      Round round = new Round(deck, players, roundNumber, startingCard);
      // play round
      round.play();
      // update score and display them
      addScore(round.getScore());
      System.out.println();
      displayScoresHistory();
      System.out.println();
    }
    // display winner
    displayWinner();

  }

}
