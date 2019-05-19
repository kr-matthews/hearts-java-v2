package Players;

import playingCards.Card;
import playingCards.OrderedCardSet;

public class HumanPlayer extends Player {

  public HumanPlayer(String name) {
    super(name);
  }

  @Override
  public Card pickCardToPlay() {
    // TODO Auto-generated method stub
    System.out.println("TODO: human pick a card to play now!");
    return null;
  }

  @Override
  public OrderedCardSet pickCardsToPass() {
    // TODO Auto-generated method stub
    System.out.println("TODO: human pick 3 cards to pass now!");
    // TODO temp code
    OrderedCardSet cardsToPass = new OrderedCardSet();
    for (int i = 0; i < 3; i++) {
      cardsToPass.add(this.getHand().get(12 - i));
    }
    return cardsToPass;
  }

  @Override
  public void receiveCards(OrderedCardSet cards) {
    System.out.println("Cards Recieved:");
    for (Card card : cards) {
      this.addToHand(card);
      System.out.println(card);
    }
  }

}
