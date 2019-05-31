package GamePlay;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoundScoreList extends ArrayList<Integer> {

  // did somebody shoot the moon this round?
  // should check if all other scores are zero, in case want to play with other
  // rules involving other points cards
  public boolean shotTheMoon() {
    return this.contains(26);
  }

  // give winner 0, everyone else 26
  public void moonModification(int winner) {
    for (int playerIndex = 0; playerIndex < this.size(); playerIndex++) {
      if (playerIndex == winner) {
        this.set(playerIndex, 0);
      } else {
        this.set(playerIndex, 26);
      }
    }
  }

  // give winner -26, everyone else 0
  public void specialMoonModification(int winner) {
    for (int playerIndex = 0; playerIndex < this.size(); playerIndex++) {
      if (playerIndex == winner) {
        this.set(playerIndex, -26);
      } else {
        this.set(playerIndex, 0);
      }
    }
  }
}