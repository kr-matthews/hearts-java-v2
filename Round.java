package GamePlay;

import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;
import playingCards.OrderedCardSet;
import playingCards.Suit;

public class Round {

  // the players of the game
  private List<Player> players;
  // the number of players
  private int numberOfPlayers;
  // the score in this individual round
  private ScoreList score;
  // how far to pass cards left
  private int passDistance;
  // number of tricks played
  private int tricksPlayed = 0;
  // index of player to play first in next trick
  private int firstToPlay;

  public ScoreList getScore() {
    return score;
  }

  private int getScore(int playerIndex) {
    return score.get(playerIndex);
  }

  // setup the players, deal the cards, note passing direction
  public Round(Deck deck, List<Player> players, int roundNumber, Card startingCard) {
    this.players = players;
    numberOfPlayers = players.size();
    // deal cards
    for (int index = 0; index < deck.size(); index++) {
      players.get(index % numberOfPlayers).addToHand(deck.get(index));
    }
    // sort player hands, assign first player
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      players.get(playerIndex).getHand().sort();
      if (players.get(playerIndex).getHand().contains(startingCard)) {
        firstToPlay = playerIndex;
      }
    }
    // set the passing direction/distance
    setPassDistance(roundNumber % numberOfPlayers);
  }

  // how far left to pass
  private void setPassDistance(int relativeRoundNumber) {
    if (relativeRoundNumber == 0) {
      // don't pass
      passDistance = 0;
    } else if (relativeRoundNumber % 2 == 0) {
      // if the relative round number is even then pass right
      // that is, pass circumference minus that left
      passDistance = numberOfPlayers - (relativeRoundNumber / 2);
    } else {
      // if it's odd, pass left
      passDistance = (relativeRoundNumber + 1) / 2;
    }
  }

  public void passCards() {
    List<OrderedCardSet> eachCardsToPass = new ArrayList<OrderedCardSet>(4);
    // get the set of 3 cards for each player
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      eachCardsToPass.add(players.get(playerIndex).pickCardsToPass());
    }
    // now actually pass the cards
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      int whoToPassTo = (playerIndex + passDistance) % numberOfPlayers;
      players.get(playerIndex).passCards(eachCardsToPass.get(playerIndex), players.get(whoToPassTo));
    }
  }

  private boolean isOver() {
    return tricksPlayed == 13;
  }

  public void play() {
    // while round is not over, play a trick
    while (!isOver()) {
      // initialize
      Trick trick = new Trick(firstToPlay);
      // play trick
      trick.play();
      // update scores, trick counter, who goes first next
      tricksPlayed++;
      firstToPlay = trick.getWinner();
      trick.updateScore();
    }
  }

  class Trick {

    // index of player who goes first
    int firstPlayerIndex;
    // the cards being played
    OrderedCardSet playedCards = new OrderedCardSet();

    // the lead suit (suit of first card played)
    // assumes at least one card has been played
    public Suit getLeadSuit() {
      return playedCards.get(0).getSuit();
    }

    // update the round's score
    private void updateScore() {
      int winner = getWinner();
      score.set(winner, score.get(winner) + playedCards.getPointsValue());
    }

    // index of who won the trick
    // assumes all cards have been played
    private int getWinner() {
      int winner = firstPlayerIndex;
      Card leadCard = playedCards.get(0);
      for (int playedIndex = 0; playedIndex < numberOfPlayers; playedIndex++) {
        Card cardPlayed = playedCards.get(playedIndex);
        if (cardPlayed.getSuit() == getLeadSuit() & leadCard.getRank().compareTo(cardPlayed.getRank()) > 0) {
          winner = (firstPlayerIndex + playedIndex) % numberOfPlayers;
        }
      }
      return winner;
    }

    // initialize first player
    public Trick(int firstPlayerIndex) {
      this.firstPlayerIndex = firstPlayerIndex;
    }
  }

}
