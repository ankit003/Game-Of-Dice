package com.game;
import java.util.*;

public class GameOfDice {
    public static boolean rolledSix = false;
    public static int timeStamp = 0;
    public static boolean incrementTimeStamp = false;

    public static void main(String[] args) {
        //n = Number of Players
        int n = Integer.parseInt(args[0]);
        //m = Max Score
        int m = Integer.parseInt(args[1]);

        GameOfDice gameOfDice = new GameOfDice();

        //players list will hold the players from 1-n in random order which indicates turns
        ArrayList<Player> players = new ArrayList<Player>(n);

        for(int i=0;i<n;i++) {
            players.add(i, new Player(i+1));
        }
        //Randomising order of turn for Players
        Collections.shuffle(players);

        int turn = 0;
        while(true) {
            //Get current Player who will roll the dice
            Player player = players.get(turn);

            //Handle Consecutive Ones scenario & skip Players who have completed the game by achieving score >= m
            if(player.getSkipTurn() && player.getScore() < m) {
                System.out.println(player.getName() + " rolled consecutive Ones, Skipping turn");
                player.setSkipTurn(false);
                player.setPreviousRollValue(0);
                turn = (turn+1)%n;
                continue;
            } else if(player.getSkipTurn()) {
                turn = (turn+1)%n;
                continue;
            }


            //Display bonus turn message
            gameOfDice.displaySpecialMessage(player, rolledSix);

            //Handling input to either roll dice or abort the game
            gameOfDice.handleInput(player);

            //Add to score of current player & decode next turn
            turn = gameOfDice.rollDice(player, turn, n, m);


            //Display individual player game completion message
            if(player.getScore() >= m) {
                System.out.println(player.getName() + " completed the game");
                player.setSkipTurn(true);
                player.setCompletionTimeStamp(timeStamp +1);
                rolledSix = false;
                incrementTimeStamp = true;
            }

            //Display Score Table
            gameOfDice.displayScoreTable(players);
        }
    }

    /**
     * Takes input from the user
     * User can either roll the dice or abort the game
     */
    public void handleInput(Player player) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println(player.getName() + " its your turn (press 'r' to roll the dice)");
        } catch (NullPointerException e) {
            System.out.println(e);
            System.exit(0);
        }
        String roll = scanner.next();

        while(!roll.equalsIgnoreCase("r") && !roll.equalsIgnoreCase("e")) {
            System.out.println("Please press 'r' to roll the dice or 'e' to end the game");
            roll = scanner.next();
        }
        if(roll.equalsIgnoreCase("e")) {
            System.out.println("Game aborted");
            System.exit(0);
        }
    }

    /**
     * Rolls dice
     * Adds to score of current player
     * Determines if player has rolled a six or if current player's next turn is to be skipped
     */
    public int rollDice(Player player, int turn, int n, int m) {
        Random random = new Random();
        int diceRoll = random.nextInt(6)+1;
        System.out.println("Points achieved by " + player.getName() + " : " + diceRoll);

        //Limit score to m for displaying Score Table
        player.setScore(player.getScore()+diceRoll > m ? m : player.getScore()+diceRoll);

        if(diceRoll == 1 && player.getPreviousRollValue() == 1) {
            player.setSkipTurn(true);
        } else {
            player.setSkipTurn(false);
        }
        player.setPreviousRollValue(diceRoll);

        if(diceRoll == 6) {
            rolledSix = true;
            return turn;
        }
        rolledSix = false;
        return (turn+1)%n;
    }

    /**
     * Calculate rank of each player
     * Display Score Table
     */
    public void displayScoreTable(ArrayList<Player> players) {
        ArrayList<Player> winners = new ArrayList<>();
        ArrayList<Player> active = new ArrayList<>();
        for(Player player : players) {
            if(player.getCompletionTimeStamp() > 0)
                winners.add(player);
            else
                active.add(player);
        }
        Collections.sort(winners, Comparator.comparing(Player::getCompletionTimeStamp));
        Collections.sort(active, Comparator.comparing(Player::getScore).reversed());
        System.out.println("Score Table :");
        System.out.println("Rank\tPlayer Id\tPlayer Name\tScore");

        //Display the winners sorted on the basis of earliest time in which m points was achieved by the player
        for(int i=0;i<winners.size();i++) {
            System.out.println(i+1+"\t"+winners.get(i).getId()+"\t\t"+winners.get(i).getName()+"\t"+winners.get(i).getScore());
        }

        //Display the rest of the active players sorted based on their score
        for(int i=0;i<active.size();i++) {
            System.out.println(i+1+winners.size()+"\t"+active.get(i).getId()+"\t\t"+active.get(i).getName()+"\t"+active.get(i).getScore());
        }

        //Game is complete if there are no active players remaining
        if(active.size() == 0) {
            System.out.println("Game Complete");
            System.exit(0);
        }

        if(incrementTimeStamp) {
            timeStamp++;
            incrementTimeStamp = false;
        }
    }

    private void displaySpecialMessage(Player player, boolean rolledSix) {
        if(rolledSix)
            System.out.println(player.getName() + " rolled a Six");
    }
}

/**
 * Player POJO
 */
class Player {

    private int id;
    private String name;
    private int score;
    private int previousRollValue;
    private boolean skipTurn;
    private int completionTimeStamp;

    public Player(int id) {
        this.id = id;
        this.score = 0;
        this.name = "Player-"+this.id;
        this.previousRollValue = 0;
        this.skipTurn = false;
        this.completionTimeStamp = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPreviousRollValue() {
        return previousRollValue;
    }

    public void setPreviousRollValue(int previousRollValue) {
        this.previousRollValue = previousRollValue;
    }

    public boolean getSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    public int getCompletionTimeStamp() {
        return completionTimeStamp;
    }

    public void setCompletionTimeStamp(int completionTimeStamp) {
        this.completionTimeStamp = completionTimeStamp;
    }
}