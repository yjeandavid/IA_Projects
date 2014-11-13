package main.java.net.kolipass.wworld;


public class Wumpus {
    public int x;
    public int y;
    public boolean isDead;

    public Wumpus(int x, int y) {
        this.x = x;
        this.y = y;

        isDead = false;
    }
}