package GamePlay;

import java.util.List;

import Players.Player;
import playingCards.Deck;

public class Round {

  // the players of the game
  private List<Player> players;

  // the score in this individual round
  private List<Integer> score;

  // passing direction
  private int passDirection;

  // setup the players, deal the cards, note passing direction
  public Round(Deck deck, List<Player> players, int passDirection) {
    this.players = players;
    for (int index = 0; index < deck.size(); index++) {
      players.get(index % players.size()).addToHand(deck.get(index));
    }
    this.passDirection = passDirection;
  }
}
