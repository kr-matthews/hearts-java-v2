package GamePlay;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;
import playingCards.Hand;
import playingCards.OrderedCardSet;
import playingCards.Rank;
import playingCards.Suit;

public class Round {

  // the players of the game
  private List<Player> players;
  // the number of players
  private int numberOfPlayers;
  // the score in this individual round
  private RoundScoreList score = new RoundScoreList();
  // how far to pass cards left
  private int passDistance;
  // number of tricks played
  private int tricksPlayed = 0;
  // index of player to play first in next trick
  private int firstToPlay;
  // the card which leads the first trick
  private Card startingCard;
  // whether a heart has been played
  boolean heartsBroken = false;

  // setup the players, deal the cards, note passing direction
  public Round(Deck deck, List<Player> players, int roundNumber, Card startingCard) {
    this.players = players;
    this.startingCard = startingCard;
    numberOfPlayers = players.size();
    // deal cards
    for (int index = 0; index < deck.size(); index++) {
      players.get(index % numberOfPlayers).addToHand(deck.get(index));
    }
    // sort player hands, initialize round score
    // can't assign first player because passing may change that
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      players.get(playerIndex).getHand().sort();
      score.add(0);
    }
    // set the passing direction/distance
    setPassDistance(roundNumber % numberOfPlayers);
  }

  public int getTricksPlayed() {
    return tricksPlayed;
  }

  public RoundScoreList getScore() {
    return score;
  }

  public Card getStartingCard() {
    return startingCard;
  }

  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  public boolean areHeartsBroken() {
    return heartsBroken;
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

  // gets 3 cards from each player and passes them based on passDistance
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

  // is the round over?
  private boolean isOver() {
    return getTricksPlayed() == 52 / numberOfPlayers;
    // may be less than 52 cards in deck if not 4 players
    // still works
  }

  // actually play the round
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
      if (players.get(playerIndex).getHand().contains(getStartingCard())) {
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
      trick.displayWinner();
      trick.updateScore();
    }
  }

  @SuppressWarnings("serial")
  public class Trick extends OrderedCardSet {

    // index of player who goes first
    int firstPlayerIndex;

    // initialize first player
    public Trick(int firstPlayerIndex) {
      this.firstPlayerIndex = firstPlayerIndex;
    }

    // the lead suit (suit of first card played)
    public Suit getLeadSuit() {
      if (size() == 0) {
        return null;
      }
      return get(0).getSuit();
    }

    // update the *round*'s score
    private void updateScore() {
      int winner = getWinner();
      score.set(winner, score.get(winner) + getPointsValue());
    }

    // rank of the winning (so far) card
    public Rank getWinningRank() {
      Rank winnerSoFar = null;
      for (Card card : this) {
        if (card.getSuit() == getLeadSuit()) {
          if (winnerSoFar == null) {
            winnerSoFar = card.getRank();
          } else if (winnerSoFar.compareTo(card.getRank()) < 0) {
            winnerSoFar = card.getRank();
          }
        }
      }
      return winnerSoFar;
    }

    // index of who won the trick
    private int getWinner() {
      if (size() != numberOfPlayers) {
        return -1;
      }
      // the index of the card played (adjust by firstPLayerIndex at end)
      int winnerSoFar = 0;
      for (int playedIndex = 0; playedIndex < numberOfPlayers; playedIndex++) {
        Card contender = get(playedIndex);
        Card defender = get(winnerSoFar);
        if (contender.getSuit() == getLeadSuit() & defender.getRank().compareTo(contender.getRank()) < 0) {
          winnerSoFar = playedIndex;
        }
      }
      return (winnerSoFar + firstPlayerIndex) % numberOfPlayers;
    }

    // display who won the trick
    // TODO: configure print stream
    private void displayWinner() {
      int winner = getWinner();
      String name = players.get(winner).getName();
      System.out.println("\n" + name + " wins the trick.\n");
    }

    // play the trick
    private void play() {
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        // for each player
        Player currentPlayer = players.get((firstPlayerIndex + playerIndex) % numberOfPlayers);
        // ask them to pick a card to play
        Card cardToPlay = currentPlayer.pickCardToPlay(Round.this, this);
        while (!validCardToPlay(cardToPlay, currentPlayer.getHand(), currentPlayer.getPrintStream())) {
          // if the card was not a valid one to play then ask again
          // the validity test will display the reason to human players
          cardToPlay = currentPlayer.pickCardToPlay(Round.this, this);
        }
        // remove the card from their hand and add it to the trick
        currentPlayer.removeFromHand(cardToPlay);
        add(cardToPlay);
        // if it was a heart, note that hearts are broken
        if (cardToPlay.isHeart()) {
          heartsBroken = true;
        }
        // display the card
        displayCardPlayed(currentPlayer.getName(), cardToPlay);
      }
    }

    // display that a player played a card into the trick
    // TODO configure print streams
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

    // checks if a card may be played by that player, and prints the reason it
    // cannot into the print stream (if applicable)
    private boolean validCardToPlay(Card card, Hand hand, PrintStream printStream) {
      if (!hand.contains(card)) {
        // you must play a card from your hand
        printStream.println("You don't have the " + card + ".");
        int i = 1 / 0;
        return false;
      }

      if (size() > 0) {
        // if you aren't leading
        if (!card.getSuit().equals(getLeadSuit()) & hand.hasSuit(getLeadSuit())) {
          // you must follow suit if possible
          printStream.println("You must follow the lead suit: " + getLeadSuit());
          int i = 1 / 0;
          return false;
        }
      }

      if (getTricksPlayed() == 0) {
        // if this is the first trick of the round
        if (size() == 0 & !card.equals(getStartingCard())) {
          // and if you are going first then you must lead the starting card
          printStream.println("You must lead with the " + getStartingCard() + ".");
          int i = 1 / 0;
          return false;
        }
        if (card.isPointsCard() & !hand.isAllPointsCards()) {
          // can't play a point card on the first trick
          printStream.println("You cannot play a heart or the queen of spades on the first trick.");
          int i = 1 / 0;
          return false;
        }
      }

      if (size() == 0 & card.isHeart() & !heartsBroken & !hand.isAllHearts()) {
        // can't lead a heart until hearts have been broken
        printStream.println("Cannot lead with the " + card + " because hearts have not been broken yet.");
        int i = 1 / 0;
        return false;
      }
      return true;
    }
  }

}