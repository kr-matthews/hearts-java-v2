package GamePlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;

public class PlayHearts {

  public static void main(String[] args) {

    // list of player names
    List<String> names = new LinkedList<String>();
    names.add("North");
    names.add("East");
    names.add("You");
    names.add("West");
    // names.add("Player Five");

    // list of whether each player is human or not (if not, then computer)
    List<Boolean> isHuman = new LinkedList<Boolean>();
    isHuman.add(false);
    isHuman.add(false);
    isHuman.add(true);
    isHuman.add(false);
    // isHuman.add(false);

    // create list of players for game
    List<Player> players = new ArrayList<Player>(4);
    for (int index = 0; index < names.size(); index++) {
      if (isHuman.get(index)) {
        players.add(new HumanPlayer(names.get(index)));
      } else {
        players.add(new ComputerPlayer(names.get(index)));
      }
    }

    // create game
    Game game = new Game(players);

    // play game
    game.play();

  }

}