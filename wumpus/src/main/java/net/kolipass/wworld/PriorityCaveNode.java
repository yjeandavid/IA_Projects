package main.java.net.kolipass.wworld;

import java.util.Vector;

// encapsulation class used for AbstractAgent's shortestSafePathToUnvisited method.

public class PriorityCaveNode implements Comparable {
    public int cost;
    public CaveNode node;
    public Vector<Character> directions;

    public PriorityCaveNode(CaveNode node, int cost, Vector<Character> lastDirVector, char newDir) {
        this.node = node;
        this.cost = cost;
        this.directions = new Vector<Character>(lastDirVector);
        this.directions.add(newDir);
    }

    public int compareTo(Object o) {
        PriorityCaveNode other = (PriorityCaveNode) o;

        if (this.cost < other.cost)
            return -1;
        else if (this.cost == other.cost)
            return 0;
        else
            return 1;
    }
}