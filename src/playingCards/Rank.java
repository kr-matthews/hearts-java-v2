package playingCards;

public enum Rank {
  // the possible ranks, in order
  N2, N3, N4, N5, N6, N7, N8, N9, T, J, Q, K, A;

  // extract the last character to get the actual rank
  // that is, get rid of the 'N' if it's there
  public String toString() {
    String stringName = this.name();
    int length = stringName.length();
    return stringName.substring(length - 1);
  }
}
