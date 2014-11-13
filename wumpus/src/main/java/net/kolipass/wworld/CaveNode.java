package net.kolipass.wworld;

public class CaveNode {
    //////////// player knowledge variables

    public double pitProbability; // 0.0 <= p <= 1.0
    public double wumpusProbability; // 0.0 <= p <= 1.0
    public double supmuwProbability; // 0.0 <= p <= 1.0

    public boolean wasVisited;
    public boolean isSafe;
    public boolean foundNorthWall;
    public boolean foundSouthWall;
    public boolean foundEastWall;
    public boolean foundWestWall;


    //////////// world knowledge variables

    public int x;
    public int y;

    public boolean hasBreeze;
    public boolean hasStench;
    public boolean hasMoo;
    public boolean hasGold;

    public boolean hasWumpus;
    public boolean hasSupmuw;
    public boolean hasPit;
    public boolean hasObstacle;
    public boolean hasEntrance;


    public CaveNode(int x, int y) {

        // initialize world knowledge and player knowledge about this node.

        pitProbability = 0.5;
        wumpusProbability = 0.1;
        supmuwProbability = 0.1;

        wasVisited = false;
        isSafe = false;
        foundNorthWall = false;
        foundSouthWall = false;
        foundEastWall = false;
        foundWestWall = false;

        hasBreeze = false;
        hasStench = false;
        hasMoo = false;
        hasGold = false;

        hasWumpus = false;
        hasSupmuw = false;
        hasPit = false;
        hasObstacle = false;

        if (x == 1 && y == 1)
            hasEntrance = true;
        else
            hasEntrance = false;

        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "CaveNode{" +
                "x=" + x +
                ", y=" + y +
                ", pitProbability=" + pitProbability +
                ", wumpusProbability=" + wumpusProbability +
                ", supmuwProbability=" + supmuwProbability +
                ", wasVisited=" + wasVisited +
                ", isSafe=" + isSafe +
                ", foundNorthWall=" + foundNorthWall +
                ", foundSouthWall=" + foundSouthWall +
                ", foundEastWall=" + foundEastWall +
                ", foundWestWall=" + foundWestWall +
                ", hasBreeze=" + hasBreeze +
                ", hasStench=" + hasStench +
                ", hasMoo=" + hasMoo +
                ", hasGold=" + hasGold +
                ", hasWumpus=" + hasWumpus +
                ", hasSupmuw=" + hasSupmuw +
                ", hasPit=" + hasPit +
                ", hasObstacle=" + hasObstacle +
                ", hasEntrance=" + hasEntrance +
                '}';
    }

    public String toShortString() {
        return "(" + x + ";" + y + ")"
                + (hasBreeze ? " hasBreeze " : "")
                + (hasStench ? " hasStench " : "")
                + (hasMoo ? " hasMoo " : "")
                ;
    }
}
