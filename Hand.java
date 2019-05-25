package playingCards;

import java.io.PrintStream;

@SuppressWarnings("serial")
public class Hand extends OrderedCardSet {

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

  public boolean hasSuit(Suit suit) {
    for (Card card : this) {
      if (card.getSuit().equals(suit)) {
        return true;
      }
    }
    return false;
  }

  public boolean isAllPointsCards() {
    for (Card card : this) {
      if (!card.isPointsCard()) {
        return false;
      }
    }
    return true;
  }

  public boolean isAllHearts() {
    for (Card card : this) {
      if (!card.isHeart()) {
        return false;
      }
    }
    return true;
  }

  public void giveOptions(PrintStream printStream) {
    for (int index = 0; index < size(); index++) {
      printStream.print((index + 1) + ": " + get(index).compactToString() + "  ");
    }
    printStream.println();
  }

}