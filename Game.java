package GamePlay;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Players.Player;
import playingCards.Card;
import playingCards.Deck;
import playingCards.Hand;
import playingCards.OrderedCardSet;
import playingCards.Rank;
import playingCards.Suit;

public class Game {

  // the players playing the game
  public List<Player> players = new ArrayList<Player>();
  // how many players (including humans and computers)
  private int numberOfPlayers;
  // the print stream of the game, to display info to all players at once
  private PrintStream printStream = System.out;
  // the card which must lead first each round (depends on number of players)
  private Card startingCard;
  // a list of cumulative scores after each round
  private List<GameScoreList> scoreHistory = new ArrayList<GameScoreList>(10);
  // the deck to be used for playing (shuffle before each new use!)
  private Deck deck = new Deck();

  // initialize things and remove the lowest clubs from the deck until an even
  // split remains
  public Game(List<Player> players) {
    // set (number of) players
    this.players = players;
    numberOfPlayers = players.size();
    // display player list to all
    displayPlayers();
    printStream.println();
    // remove cards from deck until they can be dealt out evenly
    prepareDeck();
    // set lowest card, to lead each round
    startingCard = deck.getFirst();
  }

  // get the printStream
  public PrintStream getPrintStream() {
    return printStream;
  }

  // display the player names and controllers
  private void displayPlayers() {
    printStream.println("The players:");
    for (Player player : players) {
      player.displayNameAndType(printStream);
    }
  }

  // discard the 2D and then lowest Clubs until there is an even split
  // (assumes deck is sorted)
  private void prepareDeck() {
    // first remove 2D
    if (deck.size() % numberOfPlayers > 0) {
      printStream.println("Playing without the " + Card.twoOfDiamonds + ".");
      deck.remove(Card.twoOfDiamonds);
    }
    // then remove lowest spades
    while (deck.size() % numberOfPlayers > 0) {
      printStream.println("Playing without the " + deck.getFirst() + ".");
      deck.removeFirst();
    }
    // if printed messages above, then print a new line now
    if (deck.size() < 52) {
      printStream.println();
    }
  }

  // return the score of a player at the end of the most recent round
  private int getCurrentScore(int playerIndex) {
    if (scoreHistory.size() == 0) {
      return 0;
    }
    return scoreHistory.get(scoreHistory.size() - 1).get(playerIndex);
  }

  // return the score at the end of the most recent round (of all players)
  private GameScoreList getCurrentScore() {
    if (scoreHistory.size() == 0) {
      return GameScoreList.zeros(numberOfPlayers);
    }
    return scoreHistory.get(scoreHistory.size() - 1);
  }

  // the game is over if someone hits 100 and there is a clear winner
  private boolean isOver(GameScoreList scores) {
    return scores.hasUniqueFirstPlace() & scores.getMaxScore() >= 100;
  }

  // add to the score history; account for shooting the moon and such
  private void addScore(RoundScoreList score) {
    GameScoreList newTotalScore = new GameScoreList();
    if (score.shotTheMoon()) {
      // if the moon was shot then modify the score
      if (normalShootingWouldLose(score.indexOf(26))) {
        // if adding 26 to everyone else would cause the winner to lose the game
        // then subtract 26 from the winner instead
        score.specialMoonModification(score.indexOf(26));
      } else {
        // otherwise winner gets 0 and everyone else gets 26
        score.moonModification(score.indexOf(26));
      }
    }
    // now actually add scores, after potential above modifications
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      newTotalScore.add(getCurrentScore(playerIndex) + score.get(playerIndex));
    }
    scoreHistory.add(newTotalScore);
  }

  // would adding 26 to everyone else cause them to lose?
  private boolean normalShootingWouldLose(int player) {
    GameScoreList potentialGameScore = new GameScoreList();
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (playerIndex == player) {
        potentialGameScore.add(getCurrentScore(playerIndex));
      } else {
        potentialGameScore.add(getCurrentScore(playerIndex) + 26);
      }
    }
    return isOver(potentialGameScore) & (getWinner(potentialGameScore) != player);
  }

  // get the index of the winner (assumes there is a unique winner)
  private int getWinner(GameScoreList scores) {
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      if (getCurrentScore(playerIndex) == scores.getMinScore()) {
        return playerIndex;
      }
    }
    // unreachable
    return -1;
  }

  // to display after each round
  private void displayScoresHistory() {
    int maxNameWidth = getNameDisplayWidth();
    printStream.println("Scores:");
    for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
      printStream.printf("%" + maxNameWidth + "s ", players.get(playerIndex).getName());
    }
    printStream.println();
    for (GameScoreList scores : scoreHistory) {
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        printStream.printf("%" + maxNameWidth + "s ", scores.get(playerIndex));
      }
      printStream.println();
    }
  }

  // display the winner
  private void displayWinner() {
    int winner = getWinner(getCurrentScore());
    printStream.println("The winner is " + players.get(winner).getName() + ".");
    // congratulate the player in their print stream
    players.get(winner).getPrintStream().println("Congratulations, you won!");
  }

  // actually play the game
  public void play() {
    // while the game is not over, play a round
    while (!isOver(getCurrentScore())) {
      // initialize
      deck.shuffle();
      int roundNumber = scoreHistory.size() + 1;
      // create round
      Round round = new Round(deck, roundNumber);
      // play round
      round.play();
      // update score and display them
      addScore(round.getScore());
      displayScoresHistory();
      printStream.println();
      printStream.println();
      printStream.println();
    }
    // display winner
    displayWinner();
  }

  // the max length of a player name (\geq 3), for displaying purposes
  private int getNameDisplayWidth() {
    int maxNameWidth = 03;
    for (int otherPlayerIndex = 0; otherPlayerIndex < numberOfPlayers; otherPlayerIndex++) {
      maxNameWidth = Math.max(maxNameWidth, players.get(otherPlayerIndex).getName().length());
    }
    return maxNameWidth;
  }

  public class Round {

    // the score in this individual round
    private RoundScoreList score = new RoundScoreList();
    // how far to pass cards left
    private int passDistance;
    // number of tricks played
    private int tricksPlayed = 0;
    // index of player to play first in next trick
    private int firstToPlay;
    // whether a heart has been played
    boolean heartsBroken = false;

    // setup the players, deal the cards, note passing direction
    public Round(Deck deck, int roundNumber) {
      // deal cards
      for (int index = 0; index < deck.size(); index++) {
        players.get(index % numberOfPlayers).addToHand(deck.get(index));
      }
      // sort player hands, initialize round score
      // can't assign first player because passing may change that
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        players.get(playerIndex).getHand().sort();
        score.add(0);
      }
      // set the passing direction/distance
      setPassDistance(roundNumber % numberOfPlayers);
    }

    public int getTricksPlayed() {
      return tricksPlayed;
    }

    public RoundScoreList getScore() {
      return score;
    }

    public Card getStartingCard() {
      return startingCard;
    }

    public int getNumberOfPlayers() {
      return numberOfPlayers;
    }

    public boolean areHeartsBroken() {
      return heartsBroken;
    }

    // how far left to pass
    private void setPassDistance(int relativeRoundNumber) {
      if (relativeRoundNumber == 0) {
        // don't pass
        passDistance = 0;
      } else if (relativeRoundNumber % 2 == 0) {
        // if the relative round number is even then pass right
        // that is, pass circumference minus that left
        passDistance = numberOfPlayers - (relativeRoundNumber / 2);
      } else {
        // if it's odd, pass left
        passDistance = (relativeRoundNumber + 1) / 2;
      }
    }

    // gets 3 cards from each player and passes them based on passDistance
    public void passCards() {
      List<OrderedCardSet> eachCardsToPass = new ArrayList<OrderedCardSet>(4);
      // get the set of 3 cards for each player
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        eachCardsToPass.add(players.get(playerIndex)
            .pickCardsToPass(players.get((playerIndex + passDistance) % numberOfPlayers).getName()));
      }
      // now actually pass the cards
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        int whoToPassTo = (playerIndex + passDistance) % numberOfPlayers;
        players.get(playerIndex).passCards(eachCardsToPass.get(playerIndex), players.get(whoToPassTo));
      }
    }

    // is the round over?
    private boolean isOver() {
      return getTricksPlayed() == 52 / numberOfPlayers;
      // may be less than 52 cards in deck if not 4 players
      // still works, as / ignores remainder
    }

    // actually play the round
    public void play() {
      // start by passing cards, unless it's a no pass round
      if (!(passDistance == 0)) {
        List<OrderedCardSet> cardssToPass = new ArrayList<OrderedCardSet>();
        // let each player pick cards to pass
        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
          cardssToPass.add(players.get(playerIndex)
              .pickCardsToPass(players.get((playerIndex + passDistance) % numberOfPlayers).getName()));
        }
        // now pass them
        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
          players.get(playerIndex).passCards(cardssToPass.get(playerIndex),
              players.get((playerIndex + passDistance) % numberOfPlayers));
        }
      }
      // the starting card may have changed hands, so set firstPlayer now
      for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
        if (players.get(playerIndex).getHand().contains(getStartingCard())) {
          firstToPlay = playerIndex;
        }
      }

      // while round is not over, play a trick
      while (!isOver()) {
        // initialize
        Trick trick = new Trick(firstToPlay);
        // play trick
        trick.play();
        // update & display scores, trick counter, who goes first next
        tricksPlayed++;
        firstToPlay = trick.getWinner();
        trick.displayWinner();
        trick.updateScore();
        printStream.println();
      }
      printStream.println();
    }

    @SuppressWarnings("serial")
    public class Trick extends OrderedCardSet {

      // index of player who goes first
      int firstPlayerIndex;

      // initialize first player
      public Trick(int firstPlayerIndex) {
        this.firstPlayerIndex = firstPlayerIndex;
      }

      // the lead suit (suit of first card played)
      public Suit getLeadSuit() {
        if (size() == 0) {
          return null;
        }
        return get(0).getSuit();
      }

      // update the *round*'s score
      private void updateScore() {
        int winner = getWinner();
        score.set(winner, score.get(winner) + getPointsValue());
      }

      // rank of the winning (so far) card
      public Rank getWinningRank() {
        Rank winnerSoFar = null;
        for (Card card : this) {
          if (card.getSuit() == getLeadSuit()) {
            if (winnerSoFar == null) {
              winnerSoFar = card.getRank();
            } else if (winnerSoFar.compareTo(card.getRank()) < 0) {
              winnerSoFar = card.getRank();
            }
          }
        }
        return winnerSoFar;
      }

      // index of who won the trick
      private int getWinner() {
        if (size() != numberOfPlayers) {
          return -1;
        }
        // the index of the card played (adjust by firstPLayerIndex at end)
        int winnerSoFar = 0;
        for (int playedIndex = 0; playedIndex < numberOfPlayers; playedIndex++) {
          Card contender = get(playedIndex);
          Card defender = get(winnerSoFar);
          if (contender.getSuit() == getLeadSuit() & defender.getRank().compareTo(contender.getRank()) < 0) {
            winnerSoFar = playedIndex;
          }
        }
        return (winnerSoFar + firstPlayerIndex) % numberOfPlayers;
      }

      // display who won the trick
      private void displayWinner() {
        int winner = getWinner();
        String name = players.get(winner).getName();
        printStream.println("\n" + name + " wins the trick.\n");
      }

      // play the trick
      private void play() {
        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
          // for each player
          Player currentPlayer = players.get((firstPlayerIndex + playerIndex) % numberOfPlayers);
          // ask them to pick a card to play
          Card cardToPlay = currentPlayer.pickCardToPlay(Round.this, this);
          while (!validCardToPlay(cardToPlay, currentPlayer.getHand(), currentPlayer.getPrintStream())) {
            // if the card was not a valid one to play then ask again
            // the validity test will display the reason to human players
            cardToPlay = currentPlayer.pickCardToPlay(Round.this, this);
          }
          // remove the card from their hand and add it to the trick
          currentPlayer.removeFromHand(cardToPlay);
          add(cardToPlay);
          // if it was a heart, note that hearts are broken
          if (cardToPlay.isHeart()) {
            heartsBroken = true;
          }
          // display the card
          displayCardPlayed(currentPlayer.getName(), cardToPlay);
        }
      }

      // display that a player played a card into the trick
      private void displayCardPlayed(String name, Card card) {
        printStream.printf("%-" + getNameDisplayWidth() + "s plays " + card, name);
        printStream.println();
      }

      // checks if a card may be played by that player, and prints the reason it
      // cannot into the print stream (if applicable)
      private boolean validCardToPlay(Card card, Hand hand, PrintStream printStream) {
        if (!hand.contains(card)) {
          // you must play a card from your hand
          printStream.println("You don't have the " + card + ".");
          return false;
        }

        if (size() > 0) {
          // if you aren't leading
          if (!card.getSuit().equals(getLeadSuit()) & hand.hasSuit(getLeadSuit())) {
            // you must follow suit if possible
            printStream.println("You must follow the lead suit: " + getLeadSuit());
            return false;
          }
        }

        if (getTricksPlayed() == 0) {
          // if this is the first trick of the round
          if (size() == 0 & !card.equals(getStartingCard())) {
            // and if you are going first then you must lead the starting card
            printStream.println("You must lead with the " + getStartingCard() + ".");
            return false;
          }
          if (card.isPointsCard() & !hand.isAllPointsCards()) {
            // can't play a point card on the first trick
            printStream.println("You cannot play a heart or the queen of spades on the first trick.");
            return false;
          }
        }

        if (size() == 0 & card.isHeart() & !heartsBroken & !hand.isAllHearts()) {
          // can't lead a heart until hearts have been broken
          printStream.println("Cannot lead with the " + card + " because hearts have not been broken yet.");
          return false;
        }
        return true;
      }
    }

  }

}
