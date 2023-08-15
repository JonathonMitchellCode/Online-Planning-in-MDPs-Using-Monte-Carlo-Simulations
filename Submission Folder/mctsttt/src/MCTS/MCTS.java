package MCTS;

import java.util.List;
import java.util.Random;

public class MCTS {
    private static final int SIMULATION_COUNT = 10000;
    private static final double EXPLORATION_PARAMETER = 1.4;

    private Node rootNode;
    private double averageTime;
    
    public MCTS(State initialState) {
        this.rootNode = new Node(initialState, null);
        this.averageTime=0;
    }

    public void search() {
    	
    	long totalTime = 0;
    	
    	for (int i = 0; i < SIMULATION_COUNT; i++) {
    		
    	    long startTime = System.nanoTime();

    		
    		
        	Node current = rootNode;

    		// is the current node a leaf node (no children)
        		// no: current = child node of current that maximises ucb1 formula and repeat until a leaf node is found
    	    while(!current.isLeaf()) {
    			current = selectNode(current);
    	    }
    		
			// if current is a leaf node, has it been visited yet
    	    	// yes: simulate rollout
    		if (current.getVisits()==0) {
    			
    			simulate(current);
    		
    			// no: for each available action, add a new state (expand)
    				// current = first new child node
    				// simulate 
    		} else { 
    			
    			current.expand();
    			//current = selectNode(current);
    			if (current.getChildren().size()!=0) {
	    			current = current.getChildren().get(0);
    			}
    			simulate(current);
    			

    		}
    	    long endTime = System.nanoTime();
    	    long elapsedTime = endTime - startTime;
    	    totalTime += elapsedTime;

    	}
    	averageTime = (double) totalTime / SIMULATION_COUNT / 1_000_000.0; // Convert to milliseconds

    }
    
    
    public double getAvSimTime() {
    	return averageTime;
    }



    private Node selectNode(Node node) {
    	        
    	while (!node.isLeaf()) {
        		
    		node = node.selectChild(EXPLORATION_PARAMETER);
        		
        }
        	
        return node;
        
    }
    
    
    
    
    private void simulate(Node node) {
        Random random = new Random();
        State state = node.getState();
            
        while (!state.isTerminal()) {
            List<Integer> legalMoves = state.getLegalMoves();
            System.out.println("number of legal moves " + legalMoves.size());
            int randomMove = -1;
            while (randomMove < 0) {
                randomMove = legalMoves.get(random.nextInt(legalMoves.size()));
            }
            System.out.println("move index chosen " + randomMove);
            state = state.getResult(randomMove);
        }
            
        double reward = state.getReward();
        System.out.println("Reward from simulation " + reward);
        node.updateStats(reward);
        backpropagate(node, reward);
    }
    

    

    private void backpropagate(Node node, double reward) {
    	
        while (node != null) {
        	
            node.updateStats(reward);
            node = node.getParent();
            
            reward = -reward;
        }
        
    }
    
    
    public int getBestMove() {
    	
        Node bestChild = null;
        
        double bestScore = Double.NEGATIVE_INFINITY;
        
        for (Node child : rootNode.getChildren()) {
        
        	double score = child.getTotalReward() / child.getVisits();
            
        	if(score==bestScore) {
        		if (child.getVisits()>bestChild.getVisits()) {
        			bestChild=child;
        		}
        	}else if (score > bestScore) {
        		
                bestScore = score;
        		System.out.println("Score: " + score + " BestScore: " + bestScore);

                
                bestChild = child;

            }
        }
               
        return bestChild.getState().getMovesPlayed().get(bestChild.getState().getMovesPlayed().size()-1);
        
    }
}

