package Players;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import playingCards.Card;
import playingCards.OrderedCardSet;

public class HumanPlayer extends Player {

  public HumanPlayer(String name) {
    super(name);
  }

  @Override
  public Card pickCardToPlay(OrderedCardSet cardsPlayed, int numberOfPlayers, boolean areHeartsBroken) {
    // display hand to player
    printStream.println();
    printStream.println("Your hand:");
    getHand().display(printStream);
    printStream.println();
    // instructions
    printStream.println("Enter the index of a card to play it:");
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
      printStream.println("Please provide a valid index.");
      while (!scanner.hasNextInt()) {
        // while the input is not an integer, discard it
        printStream.println("Please provide an integer.");
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
    printStream.println();
    printStream.println("Your hand:");
    getHand().display(printStream);
    printStream.println();
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

  @Override
  public void receiveCards(OrderedCardSet cards, String playerName) {
    cards.sort();
    printStream.println("\nCards recieved from " + playerName + ":");
    for (Card card : cards) {
      addToHand(card);
      printStream.println(card);
    }
    getHand().sort();
    printStream.println();
  }

}
