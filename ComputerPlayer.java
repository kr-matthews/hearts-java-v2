package Players;

import playingCards.Hand;
import playingCards.OrderedCardSet;

public class ComputerPlayer extends Player {

  public ComputerPlayer(String name) {
    super(name);
  }

  @Override
  // doesn't remove card, only picks it
  public void pickCardToPlay() {
    // TODO temporary solution (TODO) shuffle and pick first valid card

  }

  @Override
  // doesn't remove cards, only picks them
  public OrderedCardSet pickCardsToPass() {
    // TODO temporary solution: pick final three cards
    Hand cardsToPass = new Hand();
    for (int i = 0; i < 3; i++) {
      cardsToPass.add(this.getHand().getLast());
    }
    return cardsToPass;
  }

}
