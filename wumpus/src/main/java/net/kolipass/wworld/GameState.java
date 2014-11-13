package net.kolipass.wworld;

/**
 * Created by kolipass on 12.12.13.
 */
public class GameState {
    public String map = "";
    public boolean gameIsFinish = false;
    public int score = 0;
    public boolean agentIsDied = false;
    public boolean wamplusIsDied = false;
    public boolean sulpmuwIsDied = false;
    public boolean hasGold = false;
    public boolean hasFood = false;
    public boolean hasArrow = false;

    @Override
    public String toString() {
        return "GameState{" +
                "map=" + map +
                ", gameIsFinish=" + gameIsFinish +
                ", score=" + score +
                ", agentIsDied=" + agentIsDied +
                ", wamplusIsDied=" + wamplusIsDied +
                ", sulpmuwIsDied=" + sulpmuwIsDied +
                ", hasGold=" + hasGold +
                ", hasFood=" + hasFood +
                ", hasArrow=" + hasArrow +
                '}';
    }
}
