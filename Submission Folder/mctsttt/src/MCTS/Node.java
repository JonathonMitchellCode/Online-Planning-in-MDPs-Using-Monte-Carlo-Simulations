package MCTS;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private State state;
    private Node parent;
    private List<Node> children;
    private int visits;
    private double totalReward;
    private boolean fullyExpanded;

    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
        this.children = new ArrayList<Node>();
        this.visits = 0;
        this.totalReward = 0.0;
        this.fullyExpanded = false;
        

    }
    

    public void expand() {
        List<Integer> legalMoves = state.getLegalMoves();
        for (int move : legalMoves) {
        	// error could be here
            State nextState = state.getResult(move);
            Node childNode = new Node(nextState, this);
            children.add(childNode);
            System.out.println("child added for move " + move);
        }
        fullyExpanded = true;
    }
    
    
    
    
    public boolean isFullyExpanded() {
    	return fullyExpanded;
    }
    
    public void setFullyExpanded() {
    	this.fullyExpanded = true;
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getVisits() {
        return visits;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void updateStats(double reward) {
        visits++;
        totalReward += reward;
    }


    
    
    
    public Node selectChild(double explorationParameter) {
        Node selectedChild = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        final double C = 1.0; // constant used to balance exploration and exploitation

        System.out.println("Number of children: " + children.size());

        for (Node child : children) {
            if (child.getVisits() == 0) {
                // If the child has not been visited, return it immediately
                System.out.println("First unvisited child selected");
                return child;
            }
            double exploitation = child.getTotalReward() / child.getVisits();
            double exploration = Math.sqrt(C * Math.log(visits) / child.getVisits());

            double score = exploitation + explorationParameter * exploration;

            if (score > bestScore) {
                bestScore = score;
                selectedChild = child;
            }
        }
        if (selectedChild == null) {
            // return a default value
            System.out.println("Default node selected");
            return children.get(0);
        }
        return selectedChild;
    }
    
    
    

    public boolean isLeaf() {
    	return children.isEmpty();
    }
}
