package Players;

import java.io.InputStream;
import java.io.PrintStream;

import GamePlay.Round;
import GamePlay.Round.Trick;
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
      printStream.println("Unsuccessfully tried to remove " + card + " from " + name);
    }
  }

  // which card to play into the current trick (given trick & game states)
  public abstract Card pickCardToPlay(Round round, Trick trick);

  // which cards to pass, given pass direction and game state
  public abstract OrderedCardSet pickCardsToPass(String playerName); // should have other param

  // these remove/add 3 cards to respective hands
  // only call after everyone has chosen what to pass
  public void passCards(OrderedCardSet cardsToPass, Player player) {
    for (Card card : cardsToPass) {
      removeFromHand(card);
    }
    player.receiveCards(cardsToPass, getName());
  }

  public void receiveCards(OrderedCardSet cards, String playerName) {
    cards.sort();
    printStream.println("\nCards recieved from " + playerName + ":");
    for (Card card : cards) {
      addToHand(card);
      printStream.println(card);
    }
    getHand().sort();
    printStream.println();
  }

}