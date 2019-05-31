package GamePlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;

public class PlayHearts {

  public static void main(String[] args) {

    List<String> names = new LinkedList<String>();
    names.add("Able");
    names.add("Berta");
    names.add("Kevin"); // Charlie");
    names.add("Declan");
    // names.add("Evelyn");

    List<Boolean> isHuman = new LinkedList<Boolean>();
    isHuman.add(false);
    isHuman.add(false);
    isHuman.add(true);
    isHuman.add(false);
    // isHuman.add(false);

    List<Player> players = new ArrayList<Player>(4);
    for (int index = 0; index < names.size(); index++) {
      if (isHuman.get(index)) {
        players.add(new HumanPlayer(names.get(index)));
      } else {
        players.add(new ComputerPlayer(names.get(index)));
      }
    }
//
//    Hand hand = new Hand();
//    hand.add(new Card(Rank.N4, Suit.HEARTS));
//    hand.add(new Card(Rank.Q, Suit.HEARTS));
//    hand.add(new Card(Rank.N8, Suit.HEARTS));
//
//    System.out.println(hand.getHighest(Suit.HEARTS));

    Game game = new Game(players);

    game.play();

//    Scores:
//      Able   Brian Charlie  Declan 
//         1      15       8       2 
//         1      24      25       2 
//        14      24      37       3 
//        14      42      43       5 
//        14      43      47      26 
//        14      56      60      26 
//        27      57      72      26 
//        32      57      85      34 
//        34      64     102      34 
//        60      64     128      60 
//        68      65     128      77 
//
//
//   The winner is Brian.

  }

}
