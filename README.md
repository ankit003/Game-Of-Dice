#Instructions for running the Game
- Clone the repository in any path. (Eg. Program/)
- Navigate to GameOfDice Class which contains main method. (Program/GameOfDice/src/com/game/)
- Open terminal.
- Execute the command 'java GameOfDice.java n m' where : n = Number of Players, m = Maximum Score.
- Follow the console instructions within the game.

#Rules of the game 
- The order in which the users roll the dice is decided randomly at the start of the game.
- If a player rolls the value "6" then they immediately get another chance to roll again and move ahead in the game. 
- If a player rolls the value "1" two consecutive times then they are forced to skip their next turn as a penalty. 

#Implementation Details 
- Program takes the values N (number of players) and M (points of accumulate) as command line arguments.
- Players are named as Player-1 to Player-N and the order in which they will roll the dice is decided randomly. 
- When it's the turn for Player-X to roll the dice a message is prompted like “Player-3 its your turn (press ‘r’ to roll the dice) 
- Dice roll is simulated randomly, points are added to the user’s score and displayed. 
- The current rank table is printed which displays the points of all users and their rank after each roll. 
- If the user gets another chance because they rolled a ‘6’ or they are penalised because they rolled ‘1’ twice consecutively then appropriate message is printed on standard output to inform the user. 
- If a user completes the game, an appropriate message is printed on the output. 
