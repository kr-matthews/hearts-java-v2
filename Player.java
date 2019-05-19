package Players;

import playingCards.Card;
import playingCards.Hand;
import playingCards.OrderedCardSet;

public abstract class Player {

  // player name
  private String name;
  // the cards in their hand
  private Hand hand = new Hand();

  public Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Hand getHand() {
    return hand;
  }

  public void addToHand(Card card) {
    hand.add(card);
  }

  public void removeFromHand(Card card) {
    if (hand.contains(card)) {
      hand.remove(card);
    } else {
      System.out.println("Unsuccessfully tried to remove " + card + " from " + name);
    }
  }

  // which card to play into the current trick (given trick & game states)
  public abstract Card pickCardToPlay(); // TODO: will need to take arguments

  // which cards to pass, given pass direction and game state
  public abstract OrderedCardSet pickCardsToPass(); // TODO: will need to take arguments

  // these remove/add 3 cards to respective hands
  // only call after everyone has chosen what to pass
  public void passCards(OrderedCardSet cardsToPass, Player player) {
    for (Card card : cardsToPass) {
      removeFromHand(card);
    }
    player.receiveCards(cardsToPass);
  }

  public abstract void receiveCards(OrderedCardSet cards);

}
