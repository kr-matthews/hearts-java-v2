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
    getHand().shuffle();
    Card cardToPlay = getHand().get(0);
    getHand().sort();
    return cardToPlay;
  }

  @Override
  // doesn't remove cards, only picks them
  public OrderedCardSet pickCardsToPass(String playerName) {
    // TODO temporary solution: pick final three cards
    OrderedCardSet cardsToPass = new OrderedCardSet();
    for (int i = 0; i < 3; i++) {
      cardsToPass.add(getHand().get(getHand().size() - i - 1));
    }
    return cardsToPass;
  }

  @Override
  public void receiveCards(OrderedCardSet cards, String playerName) {
    for (Card card : cards) {
      this.addToHand(card);
    }
    getHand().sort();
  }

}
