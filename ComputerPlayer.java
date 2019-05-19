package Players;

import playingCards.Card;
import playingCards.OrderedCardSet;

public class ComputerPlayer extends Player {

  public ComputerPlayer(String name) {
    super(name);
  }

  @Override
  // doesn't remove card, only picks it
  public Card pickCardToPlay() {
    // TODO temporary solution (TODO) shuffle and pick first valid card
    System.out.println("TODO: computer pick a card to play now!");
    return null;
  }

  @Override
  // doesn't remove cards, only picks them
  public OrderedCardSet pickCardsToPass() {
    // TODO temporary solution: pick final three cards
    System.out.println("TODO: computer pick 3 cards to pass now!");
    OrderedCardSet cardsToPass = new OrderedCardSet();
    for (int i = 0; i < 3; i++) {
      cardsToPass.add(this.getHand().get(12 - i));
    }
    return cardsToPass;
  }

  @Override
  public void receiveCards(OrderedCardSet cards) {
    for (Card card : cards) {
      this.addToHand(card);
    }
  }

}
