package Players;

import java.io.InputStream;
import java.io.PrintStream;

import GamePlay.Game.Round;
import GamePlay.Game.Round.Trick;
import playingCards.Card;
import playingCards.Hand;
import playingCards.OrderedCardSet;

public abstract class Player {

  // player name
  private String name;
  // the cards in their hand
  private Hand hand = new Hand();
  // where to get input
  protected InputStream inputStream = System.in;
  // where to put output
  protected PrintStream printStream = System.out;

  // constructor: set player name
  public Player(String name) {
    this.name = name;
  }

  // get player name
  public String getName() {
    return name;
  }

  // get the print stream
  public PrintStream getPrintStream() {
    return printStream;
  }

  // get the player's hand
  public Hand getHand() {
    return hand;
  }

  // add a card to the hand
  public void addToHand(Card card) {
    hand.add(card);
  }

  // remove a card from the hand
  // show warning if it failed
  public void removeFromHand(Card card) {
    if (hand.contains(card)) {
      hand.remove(card);
    } else {
      printStream.println("WARNING: Unsuccessfully tried to remove " + card + " from " + name);
    }
  }

  // which card to play into the current trick (given trick & game states)
  public abstract Card pickCardToPlay(Round round, Trick trick);

  // which cards to pass, given pass direction and game state
  // should really have more information provided as parameters (game score at
  // least)
  public abstract OrderedCardSet pickCardsToPass(String playerName);

  // these remove/add 3 cards to respective hands
  // only call after everyone has chosen what to pass
  public void passCards(OrderedCardSet cardsToPass, Player player) {
    for (Card card : cardsToPass) {
      // remove each card
      removeFromHand(card);
    }
    // then give them to the other player
    player.receiveCards(cardsToPass, getName());
  }

  // receive cards from player
  public void receiveCards(OrderedCardSet cards, String playerName) {
    // sort them, to prevent knowing the order they were picked
    cards.sort();
    printStream.println("\nCards recieved from " + playerName + ":");
    for (Card card : cards) {
      // add each card to hand, and display card to user
      addToHand(card);
      printStream.println(card);
    }
    // resort hand
    getHand().sort();
    printStream.println();
  }

  // display 'name (type)' to given print stream
  public void displayNameAndType(PrintStream givenPrintStream) {
    String type;
    if (this instanceof Players.HumanPlayer) {
      type = "human";
    } else {
      type = "computer";
    }
    givenPrintStream.println(getName() + " (" + type + ")");
  }

}