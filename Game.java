package GamePlay;

import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;

public class Game {

  // how many players (including human and computer)
  private int numberOfPlayers;
  // the players playing the game
  public List<Player> players = new ArrayList<Player>();
  // the card which must lead first each round (depends on number of players)
  private Card startingCard;
  // a list of cumulative scores after each round
  private List<GameScoreList> scoreHistory = new ArrayList<GameScoreList>(10);
  // the deck to be used for playing (shuffle before each new use!)
  private Deck deck = new Deck();

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

  // display the player names and controllers
  // TODO: configure display streams
  // TODO: call Player methods instead
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

  // return the score of a player at the end of the most recent round
  private int getCurrentScore(int playerIndex) {
    if (scoreHistory.size() == 0) {
      return 0;
    }
    return scoreHistory.get(scoreHistory.size() - 1).get(playerIndex);
  }

  // return the score at the end of the most recent round (of all players)
  private GameScoreList getCurrentScore() {
    if (scoreHistory.size() == 0) {
      return GameScoreList.zeros(numberOfPlayers);
    }
    return scoreHistory.get(scoreHistory.size() - 1);
  }

  // the game is over if someone hits 100 and there is a clear winner
  private boolean isOver(GameScoreList scores) {
    return scores.hasUniqueFirstPlace() & scores.getMaxScore() >= 100;
  }

  // add to the score history; account for shooting the moon and such
  private void addScore(RoundScoreList score) {
    GameScoreList newTotalScore = new GameScoreList();
    if (score.shotTheMoon()) {
      // if the moon was shot then modify the score
      if (normalShootingWouldLose(score.indexOf(26))) {
        // if adding 26 to everyone else would cause the winner to lose the game
        // then subtract 26 from the winner instead
        score.specialMoonModification(score.indexOf(26));
      } else {
        // otherwise winner gets 0 and everyone else gets 26
        score.moonModification(score.indexOf(26));
      }
    }
    // now actually add scores, after potential above modifications
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      newTotalScore.add(getCurrentScore(playerIndex) + score.get(playerIndex));
    }
    scoreHistory.add(newTotalScore);
  }

  // would adding 26 to everyone else cause them to lose?
  private boolean normalShootingWouldLose(int player) {
    GameScoreList potentialGameScore = new GameScoreList();
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (playerIndex == player) {
        potentialGameScore.add(getCurrentScore(playerIndex));
      } else {
        potentialGameScore.add(getCurrentScore(playerIndex) + 26);
      }
    }
    return isOver(potentialGameScore) & (getWinner(potentialGameScore) != player);
  }

  // get the index of the winner (assumes there is a unique winner)
  private int getWinner(GameScoreList scores) {
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (getCurrentScore(playerIndex) == scores.getMinScore()) {
        return playerIndex;
      }
    }
    // unreachable
    return -1;
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
    for (GameScoreList scores : scoreHistory) {
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        System.out.printf("%" + maxNameWidth + "s ", scores.get(playerIndex));
      }
      System.out.println();
    }
  }

  // display the winner
  // TODO: configure print streams
  private void displayWinner() {
    int winner = getWinner(getCurrentScore());
    System.out.println();
    System.out.print("The winner is " + players.get(winner).getName() + ".");
    if (players.get(winner) instanceof Players.HumanPlayer) {
      // if the winner is human controlled then congratulate them
      System.out.println(" Congratulations!");
    }
  }

  // actually play the game
  public void play() {
    // while the game is not over, play a round
    while (!isOver(getCurrentScore())) {
      // initialize
      deck.shuffle();
      int roundNumber = scoreHistory.size() + 1;
      // create round
      Round round = new Round(deck, players, roundNumber, startingCard);
      // play round
      round.play();
      // update score and display them
      addScore(round.getScore());
      displayScoresHistory();
      System.out.println(); // TODO: configure print stream
    }
    // display winner
    displayWinner();
  }

}
