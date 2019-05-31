package Players;

import GamePlay.Round;
import GamePlay.Round.Trick;
import playingCards.Card;
import playingCards.OrderedCardSet;
import playingCards.Rank;
import playingCards.Suit;

public class ComputerPlayer extends Player {

  public ComputerPlayer(String name) {
    super(name);
    printStream = new NullOutputStream();
  }

  @Override
  // doesn't remove card, only picks it
  public Card pickCardToPlay(Round round, Trick trick) {

    if (round.getTricksPlayed() == 0) {
      // if it's the first trick
      if (getHand().contains(round.getStartingCard())) {
        // if you have the leading card then play it
        return round.getStartingCard();
      }
      // otherwise you are not leading
      if (getHand().hasSuit(trick.getLeadSuit())) {
        // if you have a club then play the highest
        return getHand().getHighest(trick.getLeadSuit());
      }
      // otherwise you can't follow suit, pick something to discard
      if (getHand().isAllPointsCards()) {
        // if you only have points cards, then you can play one
        if (getHand().contains(Card.queenOfSpades)) {
          // play the queen if you have it
          return Card.queenOfSpades;
        }
        // otherwise play highest heart
        return getHand().getHighest(Suit.HEARTS);
      }
      // otherwise you have non-points cards
      if (!getHand().contains(Card.queenOfSpades)) {
        // if you don't have the queen
        if (getHand().hasExactlyOne(Suit.SPADES)) {
          // and you only have one spade (not the queen) then short suit
          return getHand().getHighest(Suit.SPADES);
        }
        if (getHand().contains(Card.kingOfSpades)) {
          // mult. spades, no queen: get rid of the king if possible
          return Card.kingOfSpades;
        }
        if (getHand().contains(Card.aceOfSpades)) {
          // or the ace
          return Card.aceOfSpades;
        }
        // don't have the queen and no nice spades to play; play high diamond
        if (getHand().hasSuit(Suit.DIAMONDS)) {
          return getHand().getHighest(Suit.DIAMONDS);
        }
        return getHand().getHighest(Suit.SPADES);
      }

      // otherwise, have non-points cards and have the queen
      // so play diamonds if possible
      if (getHand().hasSuit(Suit.DIAMONDS)) {
        return getHand().getHighest(Suit.DIAMONDS);
      }
      // if can't play diamonds then can only play spades
      // make sure not to play the queen (don't just have the queen)
      if (!getHand().getLowest(Suit.SPADES).equals(Card.queenOfSpades)) {
        // if there is a card below the queen then play it (he lowest)
        return getHand().getLowest(Suit.SPADES);
      }
      // otherwise the queen is lowest, so play the highest (can't be queen
      // as have non-points cards and no diamonds or clubs)
      return getHand().getHighest(Suit.SPADES);
    }

    if (trick.size() == 0) {
      // if you are leading (and it's not the first trick)
      // ** this part kind of designed with 4-player version in mind **
      if (getHand().isAllHearts()) {
        // if you only have hearts then you need to play one
        return getHand().getLowest(Suit.HEARTS);
      }
      // if early on, short-suit yourself if possible in this order
      if (round.getTricksPlayed() < 4) {
        if (getHand().hasExactlyOne(Suit.DIAMONDS)) {
          return getHand().getHighest(Suit.DIAMONDS);
        }
        if (getHand().hasExactlyOne(Suit.CLUBS)) {
          return getHand().getHighest(Suit.CLUBS);
        }
      }
      // if you have a safeish card, lead it
      if (getHand().getHighestSafe(Suit.SPADES, Rank.Q) != null & !getHand().poorSpadeDistribution()) {
        return getHand().getHighestSafe(Suit.SPADES, Rank.Q);
      }
      if (getHand().getHighestSafe(Suit.HEARTS, Rank.N6) != null & round.areHeartsBroken()) {
        return getHand().getHighestSafe(Suit.HEARTS, Rank.N6);
      }
      if (getHand().getHighestSafe(Suit.CLUBS, Rank.N6) != null) {
        return getHand().getHighestSafe(Suit.CLUBS, Rank.N6);
      }
      if (getHand().getHighestSafe(Suit.DIAMONDS, Rank.N5) != null) {
        return getHand().getHighestSafe(Suit.DIAMONDS, Rank.N5);
      }
      // if you have a safeish card, lead it, attempt 2!
      if (getHand().getHighestSafe(Suit.HEARTS, Rank.N9) != null & round.areHeartsBroken()) {
        return getHand().getHighestSafe(Suit.HEARTS, Rank.N9);
      }
      if (getHand().getHighestSafe(Suit.CLUBS, Rank.N9) != null) {
        return getHand().getHighestSafe(Suit.CLUBS, Rank.N9);
      }
      if (getHand().getHighestSafe(Suit.DIAMONDS, Rank.T) != null) {
        return getHand().getHighestSafe(Suit.DIAMONDS, Rank.T);
      }
      // otherwise
      if (getHand().hasSuit(Suit.DIAMONDS)) {
        return getHand().getLowest(Suit.DIAMONDS);
      }
      if (getHand().hasSuit(Suit.CLUBS)) {
        return getHand().getLowest(Suit.CLUBS);
      }
      if (getHand().hasSuit(Suit.HEARTS) & round.areHeartsBroken()) {
        return getHand().getLowest(Suit.HEARTS);
      }
      // otherwise you only have spades
      // (ok if this plays the queen (think about it))
      return getHand().getLowest(Suit.SPADES);
    }

    // if you are not leading (and it's not the first trick)
    Suit leadSuit = trick.getLeadSuit();
    Rank currentWinningRank = trick.getWinningRank();
    Card highestSafeFollow = getHand().getHighestSafe(leadSuit, currentWinningRank);
    Card highestFollow = getHand().getHighest(leadSuit);
    Card lowestFollow = getHand().getLowest(leadSuit);
    // now actually figure out what card to return
    if (getHand().hasSuit(leadSuit)) {
      // and you can follow suit
      if (getHand().hasExactlyOne(leadSuit)) {
        // if you have only one 'choice'
        return lowestFollow;
      }
      // otherwise we have at least two options
      if (trick.size() == round.getNumberOfPlayers() - 1) {
        // if you are going last
        if (trick.getPointsValue() == 0) {
          // if there are no points then play highest card
          if (highestFollow.equals(Card.queenOfSpades) & currentWinningRank.compareTo(Rank.Q) < 0) {
            // if playing highest would cause win via QS, then play next highest
            // (exists as we have at least 2 and Q is highest)
            return getHand().getHighestSafe(Suit.SPADES, Rank.Q);
          }
          // but typically, just play highest
          return highestFollow;
        }
        if (highestSafeFollow != null) {
          // if there are points then dodge them if possible
          if (highestSafeFollow.equals(Card.kingOfSpades) & getHand().contains(Card.queenOfSpades)) {
            // if KS is safest but have QS, then play QS
            return Card.queenOfSpades;
          }
          // otherwise play safest
          return highestSafeFollow;
        }
        // otherwise can't dodge
        if (highestFollow.equals(Card.queenOfSpades)) {
          // don't win it with QS
          return getHand().getHighestSafe(Suit.SPADES, Rank.Q);
        }
        // but if it's not the QS, play highest
        return highestFollow;
      }
      // otherwise you are not going last (and can follow suit)
      if (highestSafeFollow != null) {
        // if you can, play it safe
        if (highestSafeFollow.equals(Card.kingOfSpades) & getHand().contains(Card.queenOfSpades)) {
          // unless that would mean playing K instead of Q
          return Card.queenOfSpades;
        }
        // otherwise just safety
        return highestSafeFollow;
      }
      // otherwise must take control
      if (!lowestFollow.equals(Card.queenOfSpades)) {
        // play lowest cards in hopes of losing trick later, as long as not QS
        return lowestFollow;
      }
      // otherwise lowest spade is QS; play a higher
      // (exists because have at least 2 spades)
      return highestFollow;
    }

    // otherwise, you can't follow suit (and it's not the first trick)
    // discard any of these cards if you have them: QS KS AS
    if (getHand().contains(Card.queenOfSpades)) {
      return Card.queenOfSpades;
    }
    if (getHand().contains(Card.kingOfSpades)) {
      return Card.kingOfSpades;
    }
    if (getHand().contains(Card.aceOfSpades)) {
      return Card.aceOfSpades;
    }
    // short-suit yourself if possible in this order
    if (getHand().hasExactlyOne(Suit.HEARTS)) {
      return getHand().getHighest(Suit.HEARTS);
    }
    if (getHand().hasExactlyOne(Suit.DIAMONDS)) {
      return getHand().getHighest(Suit.DIAMONDS);
    }
    if (getHand().hasExactlyOne(Suit.CLUBS)) {
      return getHand().getHighest(Suit.CLUBS);
    }
    if (getHand().hasExactlyOne(Suit.SPADES)) {
      return getHand().getHighest(Suit.SPADES);
    }
    // otherwise return right-most card in hand
    // (highest heart if you have one)
    return getHand().get(getHand().size() - 1);
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

//  @Override
//  public void receiveCards(OrderedCardSet cards, String playerName) {
//    for (Card card : cards) {
//      this.addToHand(card);
//    }
//    getHand().sort();
//  }

}
