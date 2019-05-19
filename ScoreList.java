package GamePlay;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ScoreList extends ArrayList<Integer> {

  public boolean shotTheMoon() {
    return this.contains(26);
  }

}