package playingCards;

public enum Suit {
  // the possible suits, in standard order
  CLUBS, DIAMONDS, SPADES, HEARTS;

  // extract the first character
  // example: HEARTS -> H
  public String compactToString() {
    return toString().substring(0, 1);
  }
}