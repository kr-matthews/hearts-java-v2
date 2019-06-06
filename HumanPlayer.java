package Players;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import GamePlay.Game.Round;
import GamePlay.Game.Round.Trick;
import playingCards.Card;
import playingCards.OrderedCardSet;

public class HumanPlayer extends Player {

  public HumanPlayer(String name) {
    super(name);
  }

  @Override
  public Card pickCardToPlay(Round round, Trick trick) {
    // display hand to player
    printStream.println("Your hand:");
    getHand().display(printStream);
    // instructions
    printStream.println("Specify cards by entering their corresponding index:");
    // show options
    getHand().giveOptions(printStream);
    // collect index
    int index = getIndex(1, getHand().size(), new HashSet<Integer>());
    Card cardToPlay = getHand().get(index - 1);
    return cardToPlay;
  }

  private int getIndex(int lowerBound, int upperBound, Set<Integer> forbiddenIndices) {
    int index = -1;
    Scanner scanner = new Scanner(inputStream);
    while (index < lowerBound || index > upperBound || forbiddenIndices.contains(index)) {
      // while the index is invalid, ask for another one
      printStream.println("Please provide a valid integer index.");
      while (!scanner.hasNextInt()) {
        // while the input is not an integer, discard it
        printStream.println("Please provide an integer index.");
        scanner.next();
      }
      index = scanner.nextInt();
    }
    printStream.println("Index " + index + " received.");
    // scanner.close();
    return index;
  }

  @Override
  public OrderedCardSet pickCardsToPass(String playerName) {
    // display hand to player
    printStream.println("Your hand:");
    getHand().display(printStream);
    // instructions
    printStream.println("Enter three distinct indices of cards to pass to " + playerName + ":");
    // show options
    getHand().giveOptions(printStream);
    // collect indices
    Set<Integer> indices = new HashSet<Integer>();
    for (int i = 0; i < 3; i++) {
      indices.add(getIndex(1, getHand().size(), indices));
    }
    // get cards based on indices, display them too
    OrderedCardSet cardsToPass = new OrderedCardSet();
    printStream.println("\nCards passed to " + playerName + ":");
    for (int index : indices) {
      cardsToPass.add(getHand().get(index - 1));
      printStream.println(getHand().get(index - 1));
    }
    return cardsToPass;
  }

}