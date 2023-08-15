package MCTS;

import java.util.ArrayList;
import java.util.List;

public class State {
    private char[][] board;
    private char currentPlayer;
    private List<Integer> movesPlayed;    

    
    public State(char[][] board, char currentPlayer, List<Integer> movesPlayed) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.movesPlayed = movesPlayed;
    }
    
    public char[][] getBoard() {
        return board;
    }
    
    public void setBoard(char[][] newBoard) {
    	this.board=newBoard;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Integer> getMovesPlayed() {
        return movesPlayed;
    }
    
    
    public char getWinner() {
        if (checkRows() || checkColumns() || checkDiagonals()) {
            return currentPlayer;
        } else {
            return '-';
        }
    }
 
    
    public List<Integer> getLegalMoves() {
    	
        List<Integer> legalMoves = new ArrayList<Integer>();
        
        
        for (int i = 0; i < 3; i++) {
        	for(int j = 0; j < 3; j++)
            if (board[i][j] == '-') {
                legalMoves.add(i * 3 + j);
            }
        }
        return legalMoves;
    }
    
    
    // without rewards shaping
    /*public double getReward() {
        if (checkWin('X')) {
        	// win
            return 100;
        } else if (checkWin('O')) {
        	// loss
            return -100;
        } else {
        	return 0;
        }
    }*/
    
    
    
    
    
    // with rewards shaping
    public double getReward() {
        if (checkWin('X')) {
        	// win
            return 100;
        } else if (checkWin('O')) {
        	// loss
            return -100;
        } else if (movesPlayed.size()>=9) {
        	return 0;
        } else {
        	// rewards shaping for having 2 marks in a row
            double reward = 0.0;
            for (int i = 0; i < 3; i++) {
                if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == '-') {
                    reward += 10;
                } else if (board[i][1] == currentPlayer && board[i][2] == currentPlayer && board[i][0] == '-') {
                    reward += 10;
                } else if (board[i][0] == currentPlayer && board[i][2] == currentPlayer && board[i][1] == '-') {
                    reward += 10;
                }
                
                if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == '-') {
                    reward += 10;
                } else if (board[1][i] == currentPlayer && board[2][i] == currentPlayer && board[0][i] == '-') {
                    reward += 10;
                } else if (board[0][i] == currentPlayer && board[2][i] == currentPlayer && board[1][i] == '-') {
                    reward += 10;
                }
            }
            
            if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == '-') {
                reward += 10;
            } else if (board[1][1] == currentPlayer && board[2][2] == currentPlayer && board[0][0] == '-') {
                reward += 10;
            } else if (board[0][0] == currentPlayer && board[2][2] == currentPlayer && board[1][1] == '-') {
                reward += 10;
            }
            
            if (board[2][0] == currentPlayer && board[1][1] == currentPlayer && board[0][2] == '-') {
                reward += 10;
            } else if (board[1][1] == currentPlayer && board[0][2] == currentPlayer && board[2][0] == '-') {
                reward += 10;
            } else if (board[2][0] == currentPlayer && board[0][2] == currentPlayer && board[1][1] == '-') {
                reward += 10;
            }
            
            return reward;

        }
        
        
    }
    
    private boolean checkWin(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
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
    
    
    
    public void makeMove(int index) {
        int row = index / 3;
        int col = index % 3;
        if (board[row][col] != '-') {
            throw new IllegalArgumentException("Invalid move");
        }
        board[row][col] = currentPlayer;
        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
        movesPlayed.add(index);
    }


    public boolean isTerminal() {
    	boolean boardFull = false;
    	int emptySpaceCount = 0;
    	for (int i = 0; i < 3; i++) {
    		for(int j = 0; j < 3; j++) {
    			if (board[i][j]=='-') {
    				emptySpaceCount++;
    			}
    		}
    	}
    	if (emptySpaceCount==0) {
    		boardFull=true;
    	}
        // Check for win or tie
        return checkRows() || checkColumns() || checkDiagonals() || movesPlayed.size() >= 9 || boardFull || checkDraw();
    }
    
    
    public State getResult(int move) {
        if (board[move / 3][move % 3] != '-') {
            throw new IllegalArgumentException("Invalid move");
        }

        char[][] newBoard = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBoard[i][j] = board[i][j];
            }
        }

        newBoard[move / 3][move % 3] = currentPlayer;

        char nextPlayer = switchPlayer(currentPlayer);

        List<Integer> newMovesPlayed = new ArrayList<>(movesPlayed);
        newMovesPlayed.add(move);

        return new State(newBoard, nextPlayer, newMovesPlayed);
    }

    
    
    private static char switchPlayer(char player) {
        return player == 'X' ? 'O' : 'X';
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
    
    
}

    


                    		
                 