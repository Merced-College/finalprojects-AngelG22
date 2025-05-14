//project line 2-18
public class PlayerProfile {
    public String name;
    public int wins;
    public int losses;
    public int chips;

    public PlayerProfile(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.chips = 100; // Starting chips
    }

    public void displayStats() {
        System.out.println("Stats for " + name + ": Wins: " + wins + ", Losses: " + losses + ", Chips: " + chips);
    }
}