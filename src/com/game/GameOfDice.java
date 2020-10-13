package com.game;
import java.util.*;
public class GameOfDice {
    public static boolean rolledSix = false;
    public static int timeStamp = 0;
    public static boolean incrementTimeStamp = false;


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        GameOfDice gameOfDice = new GameOfDice();

        //players list will hold the players from 1-n in random order which indicates turns
        ArrayList<Player> players = new ArrayList<Player>(n);

        Random random = new Random();
        for(int i=0;i<n;i++) {
            players.add(i, new Player(i+1));
        }
        Collections.shuffle(players);

        int turn = 0;
        while(true) {

            Player player = players.get(turn);

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
            gameOfDice.displaySpecialMessage(player, rolledSix);

            gameOfDice.handleInput(player);

            turn = gameOfDice.rollDice(player, turn, n, m);

            if(player.getScore() >= m) {
                System.out.println(player.getName() + " completed the game");
                player.setSkipTurn(true);
                player.setCompletionTimeStamp(timeStamp +1);
                rolledSix = false;
                incrementTimeStamp = true;
            }
            gameOfDice.displayScoreTable(players, incrementTimeStamp);
        }
    }

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

    public int rollDice(Player player, int turn, int n, int m) {
        Random random = new Random();
        int diceRoll = random.nextInt(6)+1;
        System.out.println("Points achieved by " + player.getName() + " : " + diceRoll);
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

    public void displayScoreTable(ArrayList<Player> players, boolean incrementSortIndex) {
        ArrayList<Player> scoreTable = new ArrayList<>(players);
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
        for(int i=0;i<winners.size();i++) {
            System.out.println(i+1+"\t"+winners.get(i).getId()+"\t\t"+winners.get(i).getName()+"\t"+winners.get(i).getScore());
        }
        for(int i=0;i<active.size();i++) {
            System.out.println(i+1+winners.size()+"\t"+active.get(i).getId()+"\t\t"+active.get(i).getName()+"\t"+active.get(i).getScore());
        }

        if(active.size() == 0) {
            System.out.println("Game Complete");
            System.exit(0);
        }

        if(incrementSortIndex) {
            timeStamp++;
            incrementSortIndex = false;
        }
    }

    private void displaySpecialMessage(Player player, boolean rolledSix) {
        if(rolledSix)
            System.out.println(player.getName() + " rolled a Six");
    }
}


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