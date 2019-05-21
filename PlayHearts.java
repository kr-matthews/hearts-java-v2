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
    names.add("South");
    names.add("Kevin");
    names.add("North");
    names.add("Easter Bunny");
    // names.add("East");

    List<Boolean> isHuman = new LinkedList<Boolean>();
    isHuman.add(false);
    isHuman.add(true);
    isHuman.add(false);
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

    Game game = new Game(players);

    game.play();

  }

}
