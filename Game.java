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
  private List<Player> players = new ArrayList<Player>(numberOfPlayers);
  // a list of cumulative scores after each round
  private List<CumulativeScore> cumulativeScores = new ArrayList<CumulativeScore>(10);
  // the deck to be used for playing (shuffle before each new use!)
  private Deck deck = new Deck();

  public int getCurrentScore(String name) {
    if (cumulativeScores.size() == 0) {
      return 0;
    }
    return cumulativeScores.get(cumulativeScores.size() - 1).get(name);
  }

  private int getMinScore() {
    int lowestScore = Integer.MAX_VALUE;
    for (Player player : players) {
      lowestScore = Math.min(lowestScore, this.getCurrentScore(player.getName()));
    }
    return lowestScore;
  }

  private int getMaxScore() {
    int highestScore = Integer.MAX_VALUE;
    for (Player player : players) {
      highestScore = Math.max(highestScore, this.getCurrentScore(player.getName()));
    }
    return highestScore;
  }

  private boolean hasUniqueFirstPlace() {
    int howMany = 0;
    for (Player player : players) {
      if (getCurrentScore(player.getName()) == getMinScore()) {
        howMany++;
      }
    }
    return howMany == 1;
  }

  public boolean isOver() {
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
    // assumes deck is sorted
    while (deck.size() % numberOfPlayers > 0) {
      deck.removeFirst();
    }
    // set lowest card, to lead each round
    startingCard = deck.getFirst();
  }

  public void addCumulativeScore(CumulativeScore score) {
    cumulativeScores.add(score);
  }

  // to display after each round
  public void displayCumulativeScores() {
    int maxNameWidth = 03;
    for (Player player : players) {
      maxNameWidth = Math.max(maxNameWidth, player.getName().length());
    }
    for (Player player : players) {
      System.out.printf("%" + maxNameWidth + "s ", player.getName());
    }
    System.out.println();
    for (CumulativeScore scores : cumulativeScores) {
      for (Player player : players) {
        System.out.printf("%" + maxNameWidth + "s ", scores.get(player.getName()));
      }
      System.out.println();
    }
  }

}
