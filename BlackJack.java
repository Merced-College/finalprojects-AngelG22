//Angel Grajeda-Cervantes
//Final Programming Project

//This code imports the Random class, which is used to generate random numbers.
  import java.util.Random;
//This code imports the Scanner class, which is used to get user input.
  import java.util.Scanner;
//(project line 11-14 and 20-24)
  import java.util.HashMap;
  import java.util.LinkedList;
  import java.util.Queue;

  public class BlackJack {

    

    // (Project line 20-24 and 20-24))Data structures for player profiles, hand history, and waiting list.
    static HashMap<String, PlayerProfile> profiles = new HashMap<>();
    static Queue<String> waitingPlayers = new LinkedList<>();
    static LinkedList<String> playerHandHistory = new LinkedList<>();
    static LinkedList<String> dealerHandHistory = new LinkedList<>();

    // (Project line 26-62)Player Profile Management (HashMap)
public static PlayerProfile getOrCreateProfile(String name) {
    PlayerProfile profile = profiles.get(name);
    if (profile == null) {
        profile = new PlayerProfile(name);
        profiles.put(name, profile);
        System.out.println("New profile created for " + name);
    } else {
        System.out.println("Welcome back, " + name + "!");
    }
    return profile;
}

//Project Waiting List Management (Queue)
public static void addToWaitingList(java.util.Scanner scanner) {
    System.out.print("Enter your name to join the waiting list: ");
    String name = scanner.nextLine();
    waitingPlayers.add(name);
    System.out.println(name + " added to the waiting list.");
}

public static String getNextPlayer() {
    return waitingPlayers.poll();
}

// Hand History Tracking (LinkedList)
public static void addCardToHand(LinkedList<String> hand, String card) {
    hand.add(card);
}


public static void showHandHistory() {
    System.out.println("Player's hand: " + playerHandHistory);
    System.out.println("Dealer's hand: " + dealerHandHistory);
    playerHandHistory.clear();
    dealerHandHistory.clear();
}

    //constants - cannot change theur values
    //Static - I can use these in every function without having to pass them in
    private static final String[] SUITS = { "Hearts", "Diamonds", "Clubs", "Spades" };
    private static final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King",
            "Ace" };
    private static final int[] DECK = new int[52];
    private static int currentCardIndex = 0;

    //Added variables to store each persons' wins and print them out at the end of the match 
    public static int playerWins, dealerWins;
    //(project line 74-122)
   public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Add player to waiting list
    addToWaitingList(scanner);

    // Get next player from queue
    String playerName = getNextPlayer();
    PlayerProfile profile = getOrCreateProfile(playerName);

    boolean playAgain;
    do {
        // Initialize and shuffle the deck
        initializeDeck();
        shuffleDeck();

        // Deal initial cards
        int playerTotal = dealInitialPlayerCards();
        int dealerTotal = dealInitialDealerCards();

        // Player's turn
        playerTotal = playerTurn(scanner, playerTotal);

        // Check if player busted
        if (playerTotal > 21) {
            System.out.println("You busted! Dealer wins.");
            dealerWins++;
        } else {
        // Dealer's turn
            dealerTotal = dealerTurn(dealerTotal);

        // Determine the winner
        determineWinner(playerTotal, dealerTotal, profile);        }

        // Show hand history
        showHandHistory();

        // Update and display player stats
        profile.displayStats();

        // Ask if the player wants to play again
        playAgain = askToPlayAgain(scanner);

    } while (playAgain);

    System.out.println("Thanks for playing! Goodbye.");
    scanner.close();
}
    //initializing the deck integers from 0-51
    private static void initializeDeck() {
        for (int i = 0; i < DECK.length; i++) {
            DECK[i] = i;
        }
    }
    //Loads the array with randomized integers
    private static void shuffleDeck() {
        Random random = new Random();
        for (int i = 0; i < DECK.length; i++) {
            //Swapping two integers within the array, creates temp variable to hold the value while swapping
            int index = random.nextInt(DECK.length);
            int temp = DECK[i];
            DECK[i] = DECK[index];
            DECK[index] = temp;
        }
    
    }
    //assigns the player's first two cards, the array has each value randomized then the mod and division operators are used on each array position
    //to find the card's number/royalty and suit, with the results being printed and then returned by the method 
    private static int dealInitialPlayerCards() {
        int card1 = dealCard();
        int card2 = dealCard();
        System.out.println("Your cards: " + RANKS[card1] + " of " + SUITS[DECK[currentCardIndex] % 4] + " and "
                + RANKS[card2] + " of " + SUITS[card2 / 13]);
        return cardValue(card1) + cardValue(card2);
    }
    //Similar method to dealInitialPlayerCards() but the dealer only is dealt one card
    private static int dealInitialDealerCards() {
        int card1 = dealCard();
        System.out.println("Dealer's card: " + RANKS[card1] + " of " + SUITS[DECK[currentCardIndex] % 4]);
        return cardValue(card1);
    }
    //the 'main' part of the game, inputs are the players input + the total of their current deck
    //Project line 165-189 allows player to decide 
    private static int playerTurn(Scanner scanner, int playerTotal) {
    while (true) {
        System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
        String action = scanner.nextLine().toLowerCase();

        if (action.equals("hit")) {
            int newCard = dealCard();
            String cardStr = RANKS[newCard] + " of " + SUITS[newCard % 4];
            addCardToHand(playerHandHistory, cardStr);

            playerTotal += cardValue(newCard);
            System.out.println("You drew a " + cardStr);

            if (playerTotal > 21) {
                break; // Player busts
            }
        } else if (action.equals("stand")) {
            break;
        } else {
            System.out.println("Invalid action. Please type 'hit' or 'stand'.");
        }
    }
    return playerTotal;
}
     //Project line 190-202 the code represents dealers turn who will automatically add a new card to their hand if their hand is <17
    private static int dealerTurn(int dealerTotal) {
    while (dealerTotal < 17) {
        int newCard = dealCard();
        String cardStr = RANKS[newCard] + " of " + SUITS[newCard % 4];
        addCardToHand(dealerHandHistory, cardStr);

        dealerTotal += cardValue(newCard);
        System.out.println("Dealer drew a " + cardStr);
    }
    System.out.println("Dealer's total is " + dealerTotal);
    return dealerTotal;
}

        
    // Project line 256-220 the winner has three potential results: player wins tie, and dealer wins
    private static boolean determineWinner(int playerTotal, int dealerTotal, PlayerProfile profile) {
    if (dealerTotal > 21 || playerTotal > dealerTotal) {
        playerWins++;
        profile.wins++; // Increment player's wins
        System.out.println("You win!");
    } else if (dealerTotal == playerTotal) {
        System.out.println("It's a tie!");
    } else {
        dealerWins++;
        profile.losses++; // Increment player's losses
        System.out.println("Dealer wins!");
    }
    System.out.println("Player Wins: " + playerWins + " | Dealer Wins: " + dealerWins);
    return true;
}
    
    //Project line 222- 235 check if the player wants to play again
    public static boolean askToPlayAgain(Scanner scanner) {
    while (true) {
        System.out.println("Play again? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            return true;
        } else if (response.equals("no")) {
            return false;
        } else {
            System.out.println("Invalid response. Please type 'yes' or 'no'.");
        }
    }
}

    //Project line 237-244
    private static int dealCard() {
    if (currentCardIndex >= DECK.length) {
        shuffleDeck(); // Reshuffle the deck if all cards are used
        currentCardIndex = 0;
    }
    return DECK[currentCardIndex++] % 13;
}
    
    //the cardValue method returns the card 
    private static int cardValue(int card) {
    if (card >= 0 && card <= 8) { // Cards 2 through 9
        return card + 2; // Add 2 because card indices start at 0 (e.g., 0 = 2, 1 = 3, etc.)
    } else if (card >= 9 && card <= 11) { // Cards Jack, Queen, King
        return 10; // Face cards are worth 10
    } else if (card == 12) { // Ace
        return 11; // Default value for Ace is 11
    }
    return 0; // Fallback in case of invalid card index
}
 // this method is not used throughout main    
    int linearSearch(int[] numbers, int key) {
        int i = 0;
        for (i = 0; i < numbers.length; i++) {
            if (numbers[i] == key) {
                return i; //returns original array value or null value
            }
        }
        return -1; // not found
    }
}