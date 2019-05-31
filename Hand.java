package playingCards;

import java.io.PrintStream;

@SuppressWarnings("serial")
public class Hand extends OrderedCardSet {

  // prints out the hand nicely, one suit per line
  public void display(PrintStream printStream) {
    int suitWidth = 0;
    for (Suit suit : Suit.values()) {
      suitWidth = Math.max(suitWidth, suit.name().length());
    }
    for (Suit suit : Suit.values()) {
      printStream.printf("%-" + suitWidth + "s:", suit);
      for (Card card : this) {
        if (card.getSuit().equals(suit)) {
          printStream.print(" " + card.getRank());
        }
      }
      printStream.println();
    }
  }

  // does the hand have at least one card of that suit
  public boolean hasSuit(Suit suit) {
    for (Card card : this) {
      if (card.getSuit().equals(suit)) {
        return true;
      }
    }
    return false;
  }

  // only have hearts and QS?
  public boolean isAllPointsCards() {
    for (Card card : this) {
      if (!card.isPointsCard()) {
        return false;
      }
    }
    return true;
  }

  // only have hearts?
  public boolean isAllHearts() {
    for (Card card : this) {
      if (!card.isHeart()) {
        return false;
      }
    }
    return true;
  }

  // prints hand on one line, with index in front of each card
  public void giveOptions(PrintStream printStream) {
    for (int index = 0; index < size(); index++) {
      printStream.print("(" + (index + 1) + ") " + get(index).compactToString() + "  ");
    }
    printStream.println();
  }

  // returns the highest card in the hand which matches the given suit
  public Card getHighest(Suit suit) {
    Card highestOfSuit = null;
    for (Card card : this) {
      if (card.getSuit().equals(suit)) {
        if (highestOfSuit == null) {
          highestOfSuit = card;
        } else if (highestOfSuit.compareTo(card) < 0) {
          highestOfSuit = card;
        }
      }
    }
    return highestOfSuit;
  }

  // returns the lowest card in the hand which matches the given suit
  public Card getLowest(Suit suit) {
    Card lowestOfSuit = null;
    for (Card card : this) {
      if (card.getSuit().equals(suit)) {
        if (lowestOfSuit == null) {
          lowestOfSuit = card;
        } else if (lowestOfSuit.compareTo(card) > 0) {
          lowestOfSuit = card;
        }
      }
    }
    return lowestOfSuit;
  }

  // returns the highest card matching the given suit which is also below the
  // given rank
  public Card getHighestSafe(Suit suit, Rank rank) {
    Card highestSafe = null;
    for (Card card : this) {
      if (card.getSuit().equals(suit) & rank.compareTo(card.getRank()) > 0) {
        if (highestSafe == null) {
          highestSafe = card;
        } else if (highestSafe.getRank().compareTo(card.getRank()) < 0) {
          highestSafe = card;
        }
      }
    }
    return highestSafe;
  }

  // have exactly one card of the given suit?
  public boolean hasExactlyOne(Suit suit) {
    return hasSuit(suit) & (getLowest(suit) == getHighest(suit));
  }

  // are spade tricks something we want to avoid
  // ** designed with 4 players in mind **
  public boolean poorSpadeDistribution() {
    int goodSpades = 0;
    int badSpades = 0;
    // compute spade balances
    for (Card card : this) {
      // Q is bad
      if (card.equals(Card.queenOfSpades)) {
        badSpades += 2;
      } else if (card.equals(Card.kingOfSpades) || card.equals(Card.aceOfSpades)) {
        // K is bad without the Q, good with the Q
        if (contains(Card.queenOfSpades)) {
          goodSpades += 1;
        } else {
          badSpades += 1;
        }
      } else if (card.getSuit().equals(Suit.SPADES)) {
        goodSpades += 1;
      }
    }
    // decide if good or bad
    if (badSpades == 0) {
      // if not bad spades then not poor
      return false;
    }
    // otherwise, poor if less than 4 covering the queen,
    // or if less than 2 net
    return goodSpades - badSpades < 2;
  }

}