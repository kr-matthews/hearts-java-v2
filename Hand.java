package playingCards;

@SuppressWarnings("serial")
public class Hand extends OrderedCardSet {

  public void display() {
    int suitWidth = 0;
    for (Suit suit : Suit.values()) {
      suitWidth = Math.max(suitWidth, suit.name().length());
    }
    for (Suit suit : Suit.values()) {
      System.out.printf("%-" + suitWidth + "s:", suit);
      for (Card card : this) {
        if (card.getSuit().equals(suit)) {
          System.out.print(" " + card.getRank());
        }
      }
      System.out.println();
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

  public void giveOptions() {
    for (int index = 0; index < size(); index++) {
      System.out.print((index + 1) + ": " + get(index).compactToString() + "  ");
    }
    System.out.println();
  }

}