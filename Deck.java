package playingCards;

@SuppressWarnings("serial")
public class Deck extends OrderedCardSet {

  // creates a sorted deck
  public Deck() {
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        this.add(new Card(rank, suit));
      }
    }
  }

}