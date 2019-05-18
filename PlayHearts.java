package GamePlay;

import java.util.LinkedList;
import java.util.List;

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
    hand.add(new Card(Rank.N3, Suit.SPADES));
    hand.add(new Card(Rank.T, Suit.SPADES));

    hand.sort();
    hand.display();

    Player p1 = new ComputerPlayer("Kevin1");
    System.out.println(p1.getName());

    Player p2 = new HumanPlayer("Kevin2");
    System.out.println(p2.getName());

    List<String> names = new LinkedList<String>();
    names.add("A");
    names.add("Brian");
    names.add("Carson");
    System.out.println(names);

    List<Boolean> humans = new LinkedList<Boolean>();
    humans.add(true);
    humans.add(false);
    humans.add(true);
    System.out.println(humans);

    Game game = new Game(names, humans);
    CumulativeScore sc1 = new CumulativeScore();
    sc1.put("A", 2);
    sc1.put("Carson", 13);
    sc1.put("Brian", 0);
    game.addCumulativeScore(sc1);

    CumulativeScore sc2 = new CumulativeScore();
    sc2.put("Carson", 123);
    sc2.put("Brian", 4);
    sc2.put("A", 11);
    game.addCumulativeScore(sc2);
    game.addCumulativeScore(sc1);

    game.displayCumulativeScores();

  }

}
