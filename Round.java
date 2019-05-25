package GamePlay;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;
import playingCards.Hand;
import playingCards.OrderedCardSet;
import playingCards.Suit;

public class Round {

  // the players of the game
  private List<Player> players;
  // the number of players
  private int numberOfPlayers;
  // the score in this individual round
  private ScoreList score = new ScoreList();
  // how far to pass cards left
  private int passDistance;
  // number of tricks played
  private int tricksPlayed = 0;
  // index of player to play first in next trick
  private int firstToPlay;
  // the card which leads the first trick
  private Card startingCard;
  // whether a heart has been played
  boolean areHeartsBroken = false;

  public ScoreList getScore() {
    return score;
  }

  // setup the players, deal the cards, note passing direction
  public Round(Deck deck, List<Player> players, int roundNumber, Card startingCard) {
    this.players = players;
    this.startingCard = startingCard;
    numberOfPlayers = players.size();
    // deal cards
    for (int index = 0; index < deck.size(); index++) {
      players.get(index % numberOfPlayers).addToHand(deck.get(index));
    }
    // sort player hands, assign first player
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      players.get(playerIndex).getHand().sort();
      score.add(0);
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
      eachCardsToPass.add(players.get(playerIndex)
          .pickCardsToPass(players.get((playerIndex + passDistance) % numberOfPlayers).getName()));
    }
    // now actually pass the cards
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      int whoToPassTo = (playerIndex + passDistance) % numberOfPlayers;
      players.get(playerIndex).passCards(eachCardsToPass.get(playerIndex), players.get(whoToPassTo));
    }
  }

  private boolean isOver() {
    return tricksPlayed == 52 / numberOfPlayers;
  }

  public void play() {
    // start by passing cards, unless it's a no pass round
    if (!(passDistance == 0)) {
      List<OrderedCardSet> cardssToPass = new ArrayList<OrderedCardSet>();
      // let each player pick cards to pass
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        cardssToPass.add(players.get(playerIndex)
            .pickCardsToPass(players.get((playerIndex + passDistance) % numberOfPlayers).getName()));
      }
      // now pass them
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        players.get(playerIndex).passCards(cardssToPass.get(playerIndex),
            players.get((playerIndex + passDistance) % numberOfPlayers));
      }
    }
    // the starting card may have changed hands, so set firstPlayer now
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (players.get(playerIndex).getHand().contains(startingCard)) {
        firstToPlay = playerIndex;
      }
    }

    // while round is not over, play a trick
    while (!isOver()) {
      // initialize
      Trick trick = new Trick(firstToPlay);
      // play trick
      trick.play();
      // update & display scores, trick counter, who goes first next
      tricksPlayed++;
      firstToPlay = trick.getWinner();
      displayWinner(players.get(trick.getWinner()).getName());
      trick.updateScore();
    }
  }

  private void displayWinner(String name) {
    System.out.println("\n" + name + " wins the trick.\n");
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

    // update the *round*'s score
    private void updateScore() {
      int winner = getWinner();
      score.set(winner, score.get(winner) + playedCards.getPointsValue());
    }

    // index of who won the trick
    // assumes all cards have been played
    private int getWinner() {
      // the index of the card played (adjust by firstPLayerIndex at end
      int winnerSoFar = 0;
      for (int playedIndex = 0; playedIndex < numberOfPlayers; playedIndex++) {
        Card contender = playedCards.get(playedIndex);
        Card defender = playedCards.get(winnerSoFar);
        if (contender.getSuit() == getLeadSuit() & defender.getRank().compareTo(contender.getRank()) < 0) {
          winnerSoFar = playedIndex;
        }
      }
      return (winnerSoFar + firstPlayerIndex) % numberOfPlayers;
    }

    private int numberOfCardsPlayed() {
      return playedCards.size();
    }

    // initialize first player
    public Trick(int firstPlayerIndex) {
      this.firstPlayerIndex = firstPlayerIndex;
    }

    // play the trick
    public void play() {
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        // for each player
        Player currentPlayer = players.get((firstPlayerIndex + playerIndex) % numberOfPlayers);
        // ask them to pick a card to play
        Card cardToPlay = currentPlayer.pickCardToPlay(playedCards, numberOfPlayers, areHeartsBroken);
        while (!validCardToPlay(cardToPlay, currentPlayer.getHand(), currentPlayer.getPrintStream())) {
          // if the card was not a valid one to play then ask again
          // the validity test will display the reason to human players
          cardToPlay = currentPlayer.pickCardToPlay(playedCards, numberOfPlayers, areHeartsBroken);
        }
        // remove the card from their hand and add it to the trick
        currentPlayer.removeFromHand(cardToPlay);
        playedCards.add(cardToPlay);
        // if it was a heart, note that hearts are broken
        if (cardToPlay.isHeart()) {
          areHeartsBroken = true;
        }
        // display the card
        displayCardPlayed(currentPlayer.getName(), cardToPlay);
      }
    }

    private void displayCardPlayed(String name, Card card) {
      // from here... is copied from Game (bad!)
      int maxNameWidth = 03;
      for (int otherPlayerIndex = 0; otherPlayerIndex < numberOfPlayers; otherPlayerIndex++) {
        maxNameWidth = Math.max(maxNameWidth, players.get(otherPlayerIndex).getName().length());
      }
      // ...to here
      System.out.printf("%-" + maxNameWidth + "s plays " + card, name);
      System.out.println();
    }

    private boolean validCardToPlay(Card card, Hand hand, PrintStream printStream) {
      if (!hand.contains(card)) {
        // you must play a card from your hand
        printStream.println("You don't have the " + card + ".");
        return false;
      }

      if (numberOfCardsPlayed() > 0) {
        // if you aren't leading
        if (!card.getSuit().equals(getLeadSuit()) & hand.hasSuit(getLeadSuit())) {
          // you must follow suit if possible
          printStream.println("You must follow the lead suit: " + getLeadSuit());
          return false;
        }
      }

      if (tricksPlayed == 0) {
        // if this is the first trick of the round
        if (numberOfCardsPlayed() == 0 & !card.equals(startingCard)) {
          // and if you are going first then you must lead the starting card
          printStream.println("You must lead with the " + startingCard + ".");
          return false;
        }
        if (card.isPointsCard() & !hand.isAllPointsCards()) {
          // can't play a point card on the first trick
          printStream.println("You cannot play a heart or the queen of spades on the first trick.");
          return false;
        }
      }

      if (numberOfCardsPlayed() == 0 & card.isHeart() & !areHeartsBroken & !hand.isAllHearts()) {
        // can't lead a heart until hearts have been broken
        printStream.println("Cannot lead with the " + card + " because hearts have not been broken yet.");
        return false;
      }
      return true;
    }
  }

}
