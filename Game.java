package GamePlay;

import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;

public class Game {

  private int numberOfPlayers;
  // the card which must lead first each round
  private Card startingCard;
  // the players playing the game
  public List<Player> players = new ArrayList<Player>();
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
    int lowestScore = Integer.MAX_VALUE;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      lowestScore = Math.min(lowestScore, this.getCurrentScore(playerIndex));
    }
    return lowestScore;
  }

  // the maximum score among all players
  private int getMaxScore() {
    int highestScore = Integer.MIN_VALUE;
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

  // initialize things and remove the lowest clubs from the deck until an even
  // split remains
  public Game(List<Player> players) {
    // set players
    this.players = players;
    // set number of players
    numberOfPlayers = players.size();
    // discard from top of deck until even split between all players
    // assumes deck is sorted (maybe 2D should be removed first??)
    displayPlayers();
    while (deck.size() % numberOfPlayers > 0) {
      System.out.println("Playing without the " + deck.getFirst() + ".");
      deck.removeFirst();
    }
    // set lowest card, to lead each round
    startingCard = deck.getFirst();
  }

  private void displayPlayers() {
    System.out.println("The players:");
    for (Player player : players) {
      String type;
      if (player instanceof Players.HumanPlayer) {
        type = "human";
      } else {
        type = "computer";
      }
      System.out.println(player.getName() + " (" + type + ")");
    }
    System.out.println();
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
      newTotalScore.add(getCurrentScore(playerIndex) + score.get(playerIndex));
    }
    scoreHistory.add(newTotalScore);
  }

  // would adding 26 to everyone else cause them to lose?
  // TODO: annoying because can't use the methods already written
  private boolean normalShootingWouldLose(int winner) {
    return false;
  }

  // to display after each round
  private void displayScoresHistory() {
    int maxNameWidth = 03;
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      maxNameWidth = Math.max(maxNameWidth, players.get(playerIndex).getName().length());
    }
    System.out.println("Scores:");
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
        // if they have the lowest score then they won
        System.out.println();
        System.out.print("The winner is " + players.get(playerIndex).getName() + ".");
        if (players.get(playerIndex) instanceof Players.HumanPlayer) {
          // if the winner is human controlled then congratulate them
          System.out.println(" Congratulations!");
        }
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
      displayScoresHistory();
      System.out.println();
    }
    // display winner
    displayWinner();

  }

}
