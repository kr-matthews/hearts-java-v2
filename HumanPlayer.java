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
    System.out.println();
    System.out.println("Your hand:");
    getHand().display();
    System.out.println();
    // instructions
    System.out.println("Enter the index of a card to play it:");
    // show options
    getHand().giveOptions();
    // collect index
    int index = getIndex(1, getHand().size(), new HashSet<Integer>());
    Card cardToPlay = getHand().get(index - 1);
    return cardToPlay;
  }

  private int getIndex(int lowerBound, int upperBound, Set<Integer> forbiddenIndices) {
    int index = -1;
    Scanner scanner = new Scanner(System.in);
    while (index < lowerBound || index > upperBound || forbiddenIndices.contains(index)) {
      // while the index is invalid, ask for another one
      while (!scanner.hasNextInt()) {
        // while the input is not an integer, discard it
        scanner.next();
      }
      index = scanner.nextInt();
    }
    // scanner.close();
    return index;
  }

  @Override
  public OrderedCardSet pickCardsToPass(String playerName) {
    // display hand to player
    System.out.println();
    System.out.println("Your hand:");
    getHand().display();
    System.out.println();
    // instructions
    System.out.println("Enter three distinct indices of cards to pass to " + playerName + ":");
    // show options
    getHand().giveOptions();
    // collect indices
    Set<Integer> indices = new HashSet<Integer>();
    for (int i = 0; i < 3; i++) {
      indices.add(getIndex(1, getHand().size(), indices));
    }
    // get cards based on indices, display them too
    OrderedCardSet cardsToPass = new OrderedCardSet();
    System.out.println("\nCards passed to " + playerName + ":");
    for (int index : indices) {
      cardsToPass.add(getHand().get(index - 1));
      System.out.println(getHand().get(index - 1));
    }
    return cardsToPass;
  }

  @Override
  public void receiveCards(OrderedCardSet cards, String playerName) {
    cards.sort();
    System.out.println("\nCards recieved from " + playerName + ":");
    for (Card card : cards) {
      addToHand(card);
      System.out.println(card);
    }
    getHand().sort();
    System.out.println();
  }

}
