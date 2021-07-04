package playingCards;

import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class OrderedCardSet extends LinkedList<Card> {

  // shuffle the cards
  public void shuffle() {
    Collections.shuffle(this);
  }

  // sort the cards (according to the order on Card)
  public void sort() {
    Collections.sort(this);
  }

  // how many points are in the hand
  // hearts count for 1 each, QS is 13
  public int getPointsValue() {
    int pointsTotal = 0;
    for (Card card : this) {
      if (card.isHeart()) {
        pointsTotal += 1;
      }
      if (card.isQueenOfSpades()) {
        pointsTotal += 13;
      }
    }
    return pointsTotal;
  }
}
