package GamePlay;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class GameScoreList extends ArrayList<Integer> {

  // the all-zeros score list; score at start of game
  public static GameScoreList zeros(int numberOfPlayers) {
    GameScoreList allZeros = new GameScoreList();
    for (int index = 0; index < numberOfPlayers; index++) {
      allZeros.add(0);
    }
    return allZeros;
  }

  // find the minimum score
  public int getMinScore() {
    int lowestScore = Integer.MAX_VALUE;
    for (int score : this) {
      lowestScore = Math.min(lowestScore, score);
    }
    return lowestScore;
  }

  // find the maximum score
  public int getMaxScore() {
    int highestScore = Integer.MIN_VALUE;
    for (int score : this) {
      highestScore = Math.max(highestScore, score);
    }
    return highestScore;
  }

  // is there a strict first place? (not shared)
  public boolean hasUniqueFirstPlace() {
    int minScore = this.getMinScore();
    int howMany = 0;
    for (int score : this) {
      if (score == minScore) {
        howMany++;
      }
    }
    return howMany == 1;
  }

}