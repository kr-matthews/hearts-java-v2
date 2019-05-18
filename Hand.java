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
}