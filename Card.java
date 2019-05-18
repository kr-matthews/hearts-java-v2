package playingCards;

public class Card implements Comparable<Card> {
  // a playing card consists of a rank and a suit
  private Rank rank;
  private Suit suit;

  // to create a card you must provide the rank and suit
  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  // common cards
  public static final Card queenOfSpades = new Card(Rank.Q, Suit.SPADES);
  public static final Card twoOfClubs = new Card(Rank.N2, Suit.CLUBS);

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  // is the card a heart?
  public boolean isHeart() {
    return this.getSuit().equals(Suit.HEARTS);
  }

  // is the card the queen of spades?
  public boolean isQueenOfSpades() {
    return this.equals(queenOfSpades);
  }

  // is the card a heart or the queen of spades?
  public boolean isPointsCard() {
    return this.isHeart() || this.isQueenOfSpades();
  }

  @Override
  public String toString() {
    return rank + " of " + suit;
  }

  // lexicographically compare (suit,rank)
  @Override
  public int compareTo(Card card) {
    int comparedSuits = this.getSuit().compareTo(card.getSuit());
    if (comparedSuits == 0) {
      return this.getRank().compareTo(card.getRank());
    }
    return comparedSuits;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rank == null) ? 0 : rank.hashCode());
    result = prime * result + ((suit == null) ? 0 : suit.hashCode());
    return result;
  }

  // two cards are equal if they have the same rank & suit
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Card other = (Card) obj;
    if (rank != other.rank)
      return false;
    if (suit != other.suit)
      return false;
    return true;
  }

}