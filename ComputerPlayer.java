package Players;

import playingCards.Card;
import playingCards.OrderedCardSet;
import playingCards.Rank;
import playingCards.Suit;

public class ComputerPlayer extends Player {

  public ComputerPlayer(String name) {
    super(name);
  }

  @Override
  // doesn't remove card, only picks it
  public Card pickCardToPlay(OrderedCardSet cardsPlayed, int numberOfPlayers, boolean areHeartsBroken) {
    System.out.println(getHand()); // TODO temp
    if (cardsPlayed.size() == 0) {
      // if you are leading
      // TODO

    } else {
      // if you are not leading
      Suit leadSuit = cardsPlayed.get(0).getSuit();
      Rank currentWinningRank = cardsPlayed.get(0).getRank();
      Card highestSafeFollow = null;
      Card highestFollow = null;
      Card lowestFollow = null;
      // compute currentWinningRank (these should be helper methods!)
      for (Card card : cardsPlayed) {
        if (card.getSuit().equals(leadSuit) & card.getRank().compareTo(currentWinningRank) > 0) {
          currentWinningRank = card.getRank();
        }
      }
      // compute possible follow cards
      for (Card card : getHand()) {
        if (card.getSuit().equals(leadSuit)) {
          highestFollow = card;
          if (card.getRank().compareTo(currentWinningRank) < 0) {
            highestSafeFollow = card;
          }
          if (lowestFollow == null) {
            lowestFollow = card;
          }
        }
      }
      // TODO temp 3 lines
      System.out.println(lowestFollow);
      System.out.println(highestSafeFollow);
      System.out.println(highestFollow);
      // now actually figure out what card to return
      if (getHand().hasSuit(leadSuit)) {
        // if it's the first trick then highest
        // TODO
        // if it's spades and you have the queen, play highest non-queen
        // TODO
        // and you can follow suit
        if (cardsPlayed.size() == numberOfPlayers - 1) {
          // if you are going last
          if (cardsPlayed.getPointsValue() == 0) {
            // if there are no points then play highest card
            return highestFollow;
          }
          if (highestSafeFollow != null) {
            // if there are points then dodge them if possible
            return highestSafeFollow;
          }
          // otherwise can't dodge
          return highestFollow;
        }
        // otherwise you are not going last (and can follow suit)
        if (highestSafeFollow != null) {
          // if you can, play it safe
          return highestSafeFollow;
        }
        // otherwise play lowest
        return lowestFollow;
      }

      // otherwise, you can't follow suit
      // TODO can't do this if it's the first hand
      if (getHand().contains(Card.queenOfSpades)) {
        // if you have the QS then discard it
        return Card.queenOfSpades;
      }
      if (getHand().contains(Card.kingOfSpades)) {
        return Card.kingOfSpades;
      }
      if (getHand().contains(Card.aceOfSpades)) {
        return Card.aceOfSpades;
      }
      // otherwise return right-most card in hand
      // (highest heart if you have one)
      return getHand().get(getHand().size() - 1);
    }

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
