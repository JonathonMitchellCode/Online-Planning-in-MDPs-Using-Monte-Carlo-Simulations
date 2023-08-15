package TicTacToe;

import java.util.Scanner;

import MCTS.MCTS;
import MCTS.State;

public class Driver {

	public static void main(String[] args) {
				
		TicTacToe game = new TicTacToe();
		char winner;
		int winCount=0;
		double avSimTimePerGame = 0;
		
		System.out.println("Game menu");
		System.out.println("1. User vs POMCP");
		System.out.println("2. Random moves vs POMCP");
		System.out.println("3. Random moves vs POMCP");

		

		
		
		Scanner scanner = new Scanner(System.in);
	    int menuChoice = scanner.nextInt();

	    switch (menuChoice) {
	        case 1:
	    		game.play();
	            break;
	        case 2:
	        	game.playRandom();
	        	break;
	        case 3:
	        	for(int i = 0; i < 10; i++) {
	        		winner = game.playRandomTest();
	        		if (winner=='O') {
	        			winCount++;
	        			avSimTimePerGame += game.getAvSimTimeForOneGame();
	        		}
	        	}
	        	System.out.println("MCTS won " + winCount + " times out of 10");
	        	avSimTimePerGame = avSimTimePerGame / 10;
	        	System.out.println("Average Simulation time per game: " + avSimTimePerGame + " ms");

	        	
	        default:
	        	System.out.println("Invalid menu option");
	            // The user input an unexpected choice.
	    }
	}

}
