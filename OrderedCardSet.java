package playingCards;

import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("serial")
public abstract class OrderedCardSet extends LinkedList<Card> {

  // shuffle the cards
  public void shuffle() {
    Collections.shuffle(this);
  }

  // sort the cards (according to the order on Card)
  public void sort() {
    Collections.sort(this);
  }
}
