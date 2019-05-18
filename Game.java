package GamePlay;

import java.util.ArrayList;
import java.util.List;

import Players.ComputerPlayer;
import Players.HumanPlayer;
import Players.Player;

public class Game {

  // the players playing the game
  private List<Player> players;
  // a list of cumulative scores after each round
  private List<CumulativeScore> cumulativeScores;
  // default names to use for players
  public static final List<String> defaultNames = new ArrayList<String>(4) {
    {
      add("You");
      add("West");
      add("North");
      add("East");
    }
  };

  // this is too complicated, the work should be put elsewhere probably
  public Game(List<String> playerNames, List<Boolean> humanPlayers) {
    int index = 0;
    while (index < playerNames.size()) {
      if (humanPlayers.get(index)) {
        players.add(new HumanPlayer(playerNames.get(index)));
      } else {
        players.add(new ComputerPlayer(playerNames.get(index)));
      }
      index++;
    }
    while (index < defaultNames.size()) {
      players.add(new ComputerPlayer(defaultNames.get(index)));
      index++;
    }
  }

}
