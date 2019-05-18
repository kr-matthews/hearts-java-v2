package GamePlay;

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

  // initialize by supplying player names and whether they are human controlled
  public Game(List<String> playerNames, List<Boolean> humanPlayers) {
    for (int index = 0; index < playerNames.size(); index++) {
      if (humanPlayers.get(index)) {
        players.add(new HumanPlayer(playerNames.get(index)));
      } else {
        players.add(new ComputerPlayer(playerNames.get(index)));
      }
    }
  }

}
