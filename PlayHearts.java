package GamePlay;

import java.util.LinkedList;
import java.util.List;

public class PlayHearts {

  public static void main(String[] args) {

    List<String> names = new LinkedList<String>();
    names.add("P1");
    names.add("P2");
    names.add("P3");
    names.add("P4");
    names.add("P5");

    List<Boolean> isHuman = new LinkedList<Boolean>();
    isHuman.add(true);
    isHuman.add(false);
    isHuman.add(true);
    isHuman.add(false);
    isHuman.add(false);

    Game game = new Game(names, isHuman);

    game.play();

  }

}
