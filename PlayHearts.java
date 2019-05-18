package GamePlay;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;
import playingCards.Card;
import playingCards.Hand;
import playingCards.Rank;
import playingCards.Suit;

public class PlayHearts {

  public static void main(String[] args) {

    Hand hand = new Hand();
    hand.add(Card.queenOfSpades);
    hand.add(Card.twoOfClubs);
    hand.add(new Card(Rank.N3, Suit.SPADES));
    hand.add(new Card(Rank.T, Suit.SPADES));

    hand.sort();
    hand.display();

    Player p1 = new ComputerPlayer("Kevin1");
    System.out.println(p1.getName());

    Player p2 = new HumanPlayer("Kevin2");
    System.out.println(p2.getName());

  }

}
