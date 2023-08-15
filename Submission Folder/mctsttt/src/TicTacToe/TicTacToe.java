package TicTacToe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import MCTS.MCTS;
import MCTS.State;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private boolean gameWon;
    private List<Integer> movesPlayed;
    private double avSimTimeForOneGame;
    private int numAgentMoves;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameWon = false;
        movesPlayed = new ArrayList<>();
        avSimTimeForOneGame = 0;
        numAgentMoves = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        printBoard();

        while (!gameWon) {

            if (currentPlayer == 'X') {
                System.out.println("Player " + currentPlayer + ", please enter row (0-2): ");
                int row = scanner.nextInt();
                System.out.println("Player " + currentPlayer + ", please enter column (0-2): ");
                int col = scanner.nextInt();
                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
                if (board[row][col] == '-') {
                    board[row][col] = currentPlayer;
                    movesPlayed.add(row * 3 + col);
                    printBoard();
                    checkWin();
                    checkDraw();

                    if (!gameWon) {
                        switchPlayer();
                    }
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
            } else {
                System.out.println("MCTS is thinking...");
                MCTS mcts = new MCTS(getState());
                mcts.search();
                printBoard();
                int bestMove = mcts.getBestMove();
                int row = bestMove/3;
                int col = bestMove%3;
                if (board[row][col] == '-') {
                	System.out.println("Array index of best move: " + bestMove);
                    System.out.println("MCTS chose row " + row + ", column " + col);
                    board[row][col] = currentPlayer;
                    movesPlayed.add(bestMove);
                    printBoard();
                    checkWin();
                    checkDraw();
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
                
                if (!gameWon) {
                    switchPlayer();
                }
               
            }
        }
        if (currentPlayer == 'X') {
            System.out.println("Game over. Player X wins!");
        } else {
            System.out.println("Game over. Player 0 wins!");
        }
        scanner.close();
    }
    
    
    public void playRandom() {
        printBoard();

        while (!gameWon) {
        	Random rand = new Random();

            if (currentPlayer == 'X') {
                System.out.println("Player " + currentPlayer + ", please enter row (0-2): ");
                int row = rand.nextInt(9);
                System.out.println("Player " + currentPlayer + ", please enter column (0-2): ");
                int col = rand.nextInt(9);
                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
                if (board[row][col] == '-') {
                    board[row][col] = currentPlayer;
                    movesPlayed.add(row * 3 + col);
                    printBoard();
                    checkWin();
                    checkDraw();

                    if (!gameWon) {
                        switchPlayer();
                    }
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
            } else {
                System.out.println("MCTS is thinking...");
                MCTS mcts = new MCTS(getState());
                mcts.search();
                avSimTimeForOneGame += mcts.getAvSimTime();
                
                printBoard();
                int bestMove = mcts.getBestMove();
                int row = bestMove/3;
                int col = bestMove%3;
                if (board[row][col] == '-') {
                	System.out.println("Array index of best move: " + bestMove);
                    System.out.println("MCTS chose row " + row + ", column " + col);
                    board[row][col] = currentPlayer;
                    movesPlayed.add(bestMove);
                    numAgentMoves++;
                    printBoard();
                    checkWin();
                    checkDraw();
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
                
                if (!gameWon) {
                    switchPlayer();
                }
               
            }
        }
        if (currentPlayer == 'X') {
            System.out.println("Game over. Player X wins!");
            avSimTimeForOneGame = avSimTimeForOneGame / numAgentMoves;
        	System.out.println("Average simulation time: " + avSimTimeForOneGame + " ms");

        } else {
            System.out.println("Game over. Player 0 wins!");
            avSimTimeForOneGame = avSimTimeForOneGame / numAgentMoves;
        	System.out.println("Average simulation time: " + avSimTimeForOneGame + " ms");

        }
    }
    
    
    public double getAvSimTimeForOneGame() {
    	return avSimTimeForOneGame;
    }

    
    
    public char playRandomTest() {
        printBoard();

        while (!gameWon) {
        	Random rand = new Random();

            if (currentPlayer == 'X') {
                System.out.println("Player " + currentPlayer + ", please enter row (0-2): ");
                int row = rand.nextInt(9);
                System.out.println("Player " + currentPlayer + ", please enter column (0-2): ");
                int col = rand.nextInt(9);
                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
                if (board[row][col] == '-') {
                    board[row][col] = currentPlayer;
                    movesPlayed.add(row * 3 + col);
                    printBoard();
                    checkWin();
                    checkDraw();

                    if (!gameWon) {
                        switchPlayer();
                    }
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
            } else {
                System.out.println("MCTS is thinking...");
                MCTS mcts = new MCTS(getState());
                mcts.search();
                avSimTimeForOneGame += mcts.getAvSimTime();

                
                printBoard();
                int bestMove = mcts.getBestMove();
                int row = bestMove/3;
                int col = bestMove%3;
                if (board[row][col] == '-') {
                	System.out.println("Array index of best move: " + bestMove);
                    System.out.println("MCTS chose row " + row + ", column " + col);
                    board[row][col] = currentPlayer;
                    movesPlayed.add(bestMove);
                    printBoard();
                    checkWin();
                    checkDraw();
                } else {
                    System.out.println("That spot is already taken. Please choose another spot.");
                }
                
                if (!gameWon) {
                    switchPlayer();
                }
               
            }
        }
        if (currentPlayer == 'X') {
            System.out.println("Game over. Player X wins!");
            avSimTimeForOneGame = avSimTimeForOneGame / numAgentMoves;
        	System.out.println("Average simulation time: " + avSimTimeForOneGame + " ms");

            return 'X';
        } else {
            System.out.println("Game over. Player 0 wins!");
            avSimTimeForOneGame = avSimTimeForOneGame / numAgentMoves;
        	System.out.println("Average simulation time: " + avSimTimeForOneGame + " ms");

            return 'O';
        }
    }
    

    
    private void switchPlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    private void checkWin() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            gameWon = true;
        }
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '-' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != '-' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        if (board[0][0] != '-' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '-' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    }
    
    public boolean checkDraw() {
        // Check if all cells are occupied
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    // Found an empty cell, the game is not a draw
                    return false;
                }
            }
        }
        // All cells are occupied and there is no winner, the game is a draw
        return true;
    }
    
    
    public State getState() {

        return new State(board, currentPlayer, movesPlayed);
    }

}
